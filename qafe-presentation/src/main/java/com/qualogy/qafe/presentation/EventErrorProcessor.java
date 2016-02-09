/**
 * Copyright 2008-2016 Qualogy Solutions B.V.
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
package com.qualogy.qafe.presentation;

import java.util.ListIterator;
import java.util.logging.Logger;

import com.qualogy.qafe.bind.core.application.ApplicationContext;
import com.qualogy.qafe.bind.presentation.component.Window;
import com.qualogy.qafe.bind.presentation.event.EventItem;
import com.qualogy.qafe.core.datastore.DataIdentifier;
import com.qualogy.qafe.core.errorhandling.ErrorProcessor;
import com.qualogy.qafe.core.errorhandling.ErrorResult;
import com.qualogy.qafe.core.errorhandling.ExternalException;
import com.qualogy.qafe.core.framework.presentation.EventData;

public class EventErrorProcessor {
	
	private final static Logger logger = Logger.getLogger(EventErrorProcessor.class.getName());

	public static ErrorResult processError(ListIterator<EventItem> itrEventItem, EventItem triggeredEventItem, ApplicationContext context, DataIdentifier dataId, ExternalException externalException, EventData eventData) {
		Window window = getWindow(eventData.getWindowId(), context);
		ErrorResult errorResult = ErrorProcessor.processError(itrEventItem, triggeredEventItem, context, window, dataId, externalException, logger);
		return errorResult; 
	}
	
	private static Window getWindow(String windowId, ApplicationContext context) {
		Window window = context.getApplicationMapping().getPresentationTier().getView().getWindowNamesMap().get(windowId);
		return window;
	}
	// TBD
//	/**
//	 * Method processes the items accordingly to the ErrorHandler if the exception caught applies to this errorhandler 
//	 * @param context
//	 * @param id
//	 * @param iter
//	 * @param eventData 
//	 * @param t
//	 * @throws UnableToProcessException
//	 */
//	public static ErrorResult processError(ApplicationContext context, DataIdentifier id, ListIterator<?> iter, Exception externalException, EventData eventData){
//		if (externalException == null) {
//			throw new IllegalArgumentException("null externalException cannot be processed");
//		}	
//		if (externalException.getCause() == null) {
//			throw new IllegalArgumentException("externalexception cause is null, and therefore cannot be processed");
//		}	
//		ErrorResult errorResult = new ErrorResult();
//		ErrorHandler errorHandler = null;
//		try {
//			if(iter!=null && iter.hasNext()){
//				List<Item> handlers = getHandlers(iter,eventData,context);
//				
//				errorHandler = selectHandler(handlers, externalException.getCause().getClass(), 0);
//					
//				if(errorHandler!=null){
//					
//					logger.debug("handler found for exception ["+externalException.getCause().getClass()+"], continue in alternative path");
//					logError(errorHandler, context, id);
//					
//					errorResult.setItems(errorHandler.getResultItems());
//					
//				}
//			}	
//		}finally{
//			errorResult = handleException(errorResult, errorHandler, externalException);
//			if(externalException.getCause()!=null)
//				DataStore.store(id, DataStore.KEY_ERROR_MESSAGE, externalException.getCause().getMessage());
//		}
//		return errorResult; 
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
//					aHandler.getErrorRef().getRef().getException();
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
//	static void getErrorHandlers(Event event, List<Item> handlers,final  ApplicationContext context,final EventData eventData){
//
//		if (event!=null){
//			for (EventItem eventItem : event.getEventItems()) {
//				if (eventItem instanceof ErrorHandler){
//					handlers.add(eventItem);
//				}
//				if (eventItem instanceof EventRef){
//					String eventToCall = ((EventRef)eventItem).getEvent();
//					Set<String> visitedEvents = new HashSet<String>(17);
//					visitedEvents.add(eventToCall);
//					if(EventRefExecute.isRecursiveCall(eventToCall,context,event,eventData,visitedEvents)){
//						throw new RuntimeException("Recursive call of events detected");
//					}
//					if (eventData.getWindowId()!=null && eventData.getWindowId().length()>0){
//						// so this call is made from a MDI window application
//						Window window = (Window)context.getApplicationMapping().getPresentationTier().getView().getWindowNamesMap().get(eventData.getWindowId());
//						if (window!=null){
//							// check local event
//							Event toCallEvent =(Event)window.getEventsMap().get(eventToCall);
//							if(toCallEvent != null) {
//								getErrorHandlers(toCallEvent,handlers,context,eventData);
//							} 
//						} 
//					}
//				}
//			}
//		}
//	}
//	
//	private static List<Item> getHandlers(ListIterator<?> iter, EventData eventData, ApplicationContext context) {
//		List<Item> handlers = new ArrayList<Item>();
//		while(iter.hasNext()) {
//			Object o  = iter.next();
//			if (o instanceof Item){
//				Item item = (Item)o;
//				if (item instanceof EventRef){
//					String eventToCall = ((EventRef)item).getEvent();
//					if (eventData.getWindowId()!=null && eventData.getWindowId().length()>0){
//						// so this call is made from a MDI window application
//						Window window = (Window)context.getApplicationMapping().getPresentationTier().getView().getWindowNamesMap().get(eventData.getWindowId());
//						if (window!=null){
//							Event toCallEvent =(Event)window.getEventsMap().get(eventToCall); // check local event
//							if(toCallEvent == null) {
//								toCallEvent =(Event)context.getApplicationMapping().getPresentationTier().getEventsMap().get(eventToCall); // check Global event
//							} 
//							getErrorHandlers(toCallEvent,handlers,context,eventData);
//						}
//					}		
//				}
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
//	private static ErrorResult handleException(ErrorResult result, ErrorHandler handler, Exception externalException){
//		if(handler == null || ErrorHandler.FINALLY_RETHROW.equals(handler.getFinalAction())){
//			result.setExternalException(new ExternalException(externalException));
//		}
//		return result;
//	}
}
