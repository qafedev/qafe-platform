/**
 * Copyright 2008-2014 Qualogy Solutions B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.qualogy.qafe.core.errorhandling;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.logging.Logger;

import org.apache.commons.lang.StringUtils;

import com.qualogy.qafe.bind.commons.error.ErrorHandler;
import com.qualogy.qafe.bind.commons.error.ServiceError;
import com.qualogy.qafe.bind.core.application.ApplicationContext;
import com.qualogy.qafe.bind.item.Item;
import com.qualogy.qafe.bind.presentation.component.Window;
import com.qualogy.qafe.bind.presentation.event.Event;
import com.qualogy.qafe.bind.presentation.event.function.EventRef;
import com.qualogy.qafe.core.datastore.DataIdentifier;
import com.qualogy.qafe.core.datastore.DataStore;
import com.qualogy.qafe.core.i18n.MessagesHandler;

public class ErrorProcessor {
	
	private final static Logger logger = Logger.getLogger(ErrorProcessor.class.getName());
	
	public static ErrorResult processError(ListIterator<? extends Item> itrItem, Item triggeredItem, ApplicationContext context, DataIdentifier dataId, Exception externalException) {
		ErrorResult errorResult = processError(itrItem, triggeredItem, context, null, dataId, externalException, logger);
		return errorResult; 
	}

	public static ErrorResult processError(ListIterator<? extends Item> itrItem, Item triggeredItem, ApplicationContext context, Window window, DataIdentifier dataId, Exception externalException, Logger log) {
		if (externalException == null) {
			throw new IllegalArgumentException("ExternalException is null, and therefore cannot be processed");
		}	
		if (externalException.getCause() == null) {
			throw new IllegalArgumentException("ExternalException cause is null, and therefore cannot be processed");
		}			
		ErrorResult errorResult = new ErrorResult();
		ErrorHandler errorHandler = null;
		try {
			if (itrItem != null) {
				List<ErrorHandler> errorHandlerList = getErrorHandlers(itrItem, context, window);
				errorHandler = resolveErrorHandler(errorHandlerList, externalException);
				if (errorHandler != null) {
					log.fine("ErrorHandler found for exception [" + externalException.getCause().getClass() + "], continue in alternative path");
					logError(errorHandler, context, dataId, log);
					errorResult.setItems(errorHandler.getResultItems());

					// Go to the position of the errorHandler, 
					// so further processing can be continued
					goBackToItemPosition(itrItem, errorHandler, context, window);
				}
			}	
		} finally {
			// Rethrow if handler is not defined (no action specified) 
			// or handler specifies that the exception should be rethrown
			errorResult = resolveRethrow(errorResult, errorHandler, externalException);
			if (externalException.getCause() != null) {
				DataStore.store(dataId, DataStore.KEY_ERROR_MESSAGE, externalException.getCause().getMessage());
			}	
		}
		return errorResult; 
	}
	
	private static ErrorResult resolveRethrow(ErrorResult errorResult, ErrorHandler errorHandler, Exception externalException) {
		if ((errorHandler == null) || ErrorHandler.FINALLY_RETHROW.equals(errorHandler.getFinalAction())) {
			errorResult.setExternalException(new ExternalException(externalException.getMessage(), externalException));
		}
		return errorResult;
	}
	
	private static ErrorHandler resolveErrorHandler(List<ErrorHandler> errorHandlers, Exception externalException) {
		ErrorHandler errorHandler = null;
		if ((errorHandlers != null) && (externalException != null)) {
			// The exceptionMessage is null when it is a NullPointerException
			String exceptionMessage = externalException.getCause().getMessage();
			
			Map<String,Class<?>> exceptionClassHierarchy = resolveExceptionClassHierarchy(externalException);
			for (ErrorHandler handler : errorHandlers) {
				if (handler instanceof ErrorHandler){
					ServiceError serviceError = ((ErrorHandler)handler).getErrorRef().getRef();
					String exception = serviceError.getException();
					boolean found = false;
					if ((exceptionClassHierarchy != null) && exceptionClassHierarchy.containsKey(exception)) {
						found = true;
					} else if ((exceptionMessage != null) && exceptionMessage.contains(exception)) {
						found = true;
					}
					if (found) {
						errorHandler = handler;
						break;
					}
				}
			}
		}
		return errorHandler;
	}
	
	private static Map<String,Class<?>> resolveExceptionClassHierarchy(Exception externalException) {
		Map<String,Class<?>> exceptionClassHierarchy = new LinkedHashMap<String,Class<?>>();
		if (externalException != null) { 
			Class<?> exceptionClass = externalException.getCause().getClass();
			while (exceptionClass != null) {
				String qualifiedClassName = exceptionClass.getName();
				exceptionClassHierarchy.put(qualifiedClassName, exceptionClass);
				exceptionClass = exceptionClass.getSuperclass();
				if (exceptionClass.equals(Object.class)) {
					exceptionClass = null;
				} 
			}
		}	
		return exceptionClassHierarchy;
	}
	
	private static List<ErrorHandler> getErrorHandlers(ListIterator<? extends Item> itrItem, ApplicationContext context, Window window) {
		List<ErrorHandler> errorHandlerList = new ArrayList<ErrorHandler>();
		if (itrItem != null) {
			while (itrItem.hasNext()) {
				Item item = itrItem.next();
				if (item instanceof ErrorHandler) {
					errorHandlerList.add((ErrorHandler)item);
				} else if (item instanceof EventRef) {
					EventRef eventRef = (EventRef)item;
					String eventId = eventRef.getEvent();
					Event event = getEvent(eventId, context, window);
					if (event != null) {
						List<ErrorHandler> errorHandlers = getErrorHandlers(event.getEventItems().listIterator(), context, window);
						if (errorHandlers != null) {
							errorHandlerList.addAll(errorHandlers);
						}
					}
				}
			}
		}
		return errorHandlerList;
	}
	
	private static void goBackToItemPosition(ListIterator<? extends Item> itrItem, Item targetItem, ApplicationContext context, Window window) {
		if (itrItem != null) {
			boolean found = false;
			while (itrItem.hasPrevious()) {
				Item item = itrItem.previous();
				if (item == targetItem) {
					found = true;
				} else if (item instanceof EventRef) {
					found = containsItem((EventRef)item, targetItem, context, window);
				}
				if (found) {
					itrItem.next();
					break;
				}
			}	
		}
	}
	
	private static boolean containsItem(EventRef eventRef, Item targetItem, ApplicationContext context, Window window) {
		boolean result = false;
		if (eventRef == null) {
			return result;
		}
		String eventId = eventRef.getEvent();
		Event event = getEvent(eventId, context, window);
		if (event != null) {
			Iterator<? extends Item> itrItem = event.getEventItems().iterator();
			while (itrItem.hasNext()) {
				Item item = itrItem.next();
				if (item == targetItem) {
					result = true;
				} else if (item instanceof EventRef) {
					result = containsItem((EventRef)item, targetItem, context, window); 
				}
				if (result) {
					break;
				}
			}
		}
		return result;
	}
	
	private static Event getEvent(String eventId, ApplicationContext context, Window window) {
		Event event = null;		
		if (window != null) {
			event = window.getEventsMap().get(eventId);
		}
		if (event == null) {
			event = context.getApplicationMapping().getPresentationTier().getEventsMap().get(eventId);
		}
		return event;
	}
	
	private static void logError(ErrorHandler errorHandler, ApplicationContext context, DataIdentifier dataId, Logger log) {
		if ((errorHandler.getErrorRef() != null) && (errorHandler.getErrorRef().getRef() != null)) {
			ServiceError serviceError = errorHandler.getErrorRef().getRef();
			if (serviceError.getLoggingSettings() != null) {
				String message = "";
				String messageKey = serviceError.getLoggingSettings().getMessageKey();
				if (messageKey != null) {
					message += MessagesHandler.getMessage(context, dataId, messageKey);
				}
				String solutionKey = serviceError.getLoggingSettings().getSolutionKey();
				if(solutionKey != null) {
					message += MessagesHandler.getMessage(context, dataId, solutionKey);
				}	
				String errorMessage = serviceError.getLoggingSettings().getErrorMessage();
				if (StringUtils.isBlank(message) && (errorMessage != null)) {
					message += errorMessage;
				}
				if(!StringUtils.isBlank(message)) {
					log.severe(message);
				}	
			}
		}
	}

	// TBD
//	/**
//	 * Method processes the items accordingly to the ErrorHandler if the exception caught applies to this errorhandler 
//	 * @param context
//	 * @param id
//	 * @param iter
//	 * @param t
//	 * @throws UnableToProcessException
//	 */
//	public static ErrorResult processError(ApplicationContext context, DataIdentifier id, ListIterator<?> iter, ExternalException externalException){
//		
//		if(externalException == null)
//			throw new IllegalArgumentException("null externalException cannot be processed");
//		
//		if(externalException.getCause() == null)
//			throw new IllegalArgumentException("externalexception cause is null, and therefore cannot be processed");
//				
//		ErrorResult result = new ErrorResult();
//		ErrorHandler handler = null;
//		try{
//			if(iter!=null && iter.hasNext()){
//				List<Item> handlers = getHandlers(iter);
//				
//				handler = selectHandler(handlers, externalException.getCause().getClass(), 0);
//					
//				if(handler!=null){
//					
//					logger.debug("handler found for exception ["+externalException.getCause().getClass()+"], continue in alternative path");
//					logError(handler, context, id);
//					
//					result.setItems(handler.getResultItems());
//					
//				}
//			}	
//		}finally{
//			result = handleException(result, handler, externalException);
//			if(externalException.getCause()!=null)
//				DataStore.store(id, DataStore.KEY_ERROR_MESSAGE, externalException.getCause().getMessage());
//		}
//		return result; 
//	}
//	
//	private static ErrorHandler selectHandler(List<Item> handlers, Class<?> exceptionClass, int level) {
//		ErrorHandler handler = null;
//		Class<?> classToCheck = getExceptionClass(exceptionClass, level);
//		
//		if(classToCheck!=null){
//			for (Item item : handlers) {
//				if (item instanceof ErrorHandler){
//					ErrorHandler aHandler = (ErrorHandler) item;
//					Class<?> handlerClass = aHandler.getErrorRef().getRef().getErrorClass();
//					
//					if(handlerClass.equals(classToCheck)){
//						handler = aHandler;
//					}
//				}
//			}
//			
//		
//			if(handler==null)
//				selectHandler(handlers, exceptionClass, 1+level);
//		}
//		return handler;
//	}
//
//	private static Class<?> getExceptionClass(Class<?> exceptionClass, int level) {
//		Class<?> clazz = exceptionClass;
//		for (int i = 0; i < level; i++) {
//			clazz = clazz.getSuperclass();
//			if(clazz.equals(Object.class)){
//				clazz = null;
//				break;
//			}
//		}
//		return clazz;
//	}
//
//	private static List<Item> getHandlers(ListIterator<?> iter) {
//		List<Item> handlers = new ArrayList<Item>();
//		while(iter.hasNext()) {
//			Object o  = iter.next();
//			if (o instanceof Item){
//				Item item = (Item)o;
//				if(!(item instanceof ErrorHandler)){
//					if(iter.hasPrevious())iter.previous();//reset to last position
//					break;
//				}
//				handlers.add(item);
//			}
//		}
//		return handlers;
//	}
//
//	private static void logError(ErrorHandler handler, ApplicationContext context, DataIdentifier id) {
//		if(handler.getErrorRef()!=null && handler.getErrorRef().getRef()!=null){
//			ServiceError error = handler.getErrorRef().getRef();
//			if(error.getLoggingSettings()!=null){
//				String message = "";
//				
//				if(error.getLoggingSettings().getMessageKey()!=null)
//					message += MessagesHandler.getMessage(context, id, error.getLoggingSettings().getMessageKey());
//				if(error.getLoggingSettings().getSolutionKey()!=null)
//					message += MessagesHandler.getMessage(context, id, error.getLoggingSettings().getSolutionKey());
//				if(StringUtils.isBlank(message) && error.getLoggingSettings().getErrorMessage()!=null)
//					message += error.getLoggingSettings().getErrorMessage();
//				
//				if(!StringUtils.isBlank(message))
//					logger.error(message);
//			}
//		}
//	}
//
//	/**
//	 * If handler is null (no action specified) or handler specifies the exception should be 
//	 * rethrown this method throws the exception given, otherwise swallows the exception.
//	 * @param handler
//	 * @param t
//	 */
//	private static ErrorResult handleException(ErrorResult result, ErrorHandler handler, ExternalException externalException){
//		if(handler == null || ErrorHandler.FINALLY_RETHROW.equals(handler.getFinalAction())){
//			result.setExternalException(externalException);
//		}
//		return result;
//	}
}
