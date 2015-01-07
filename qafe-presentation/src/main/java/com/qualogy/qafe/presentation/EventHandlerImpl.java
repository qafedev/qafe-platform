/**
 * Copyright 2008-2015 Qualogy Solutions B.V.
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ListIterator;
import java.util.logging.Logger;

import org.apache.commons.logging.LogFactory;

import com.qualogy.qafe.bind.business.action.BusinessActionRef;
import com.qualogy.qafe.bind.commons.type.In;
import com.qualogy.qafe.bind.commons.type.Parameter;
import com.qualogy.qafe.bind.core.application.ApplicationContext;
import com.qualogy.qafe.bind.core.application.Configuration;
import com.qualogy.qafe.bind.domain.ApplicationMapping;
import com.qualogy.qafe.bind.item.Item;
import com.qualogy.qafe.bind.presentation.event.Event;
import com.qualogy.qafe.bind.presentation.event.EventItem;
import com.qualogy.qafe.bind.presentation.event.function.BuiltInFunction;
import com.qualogy.qafe.bind.presentation.event.function.LogFunction;
import com.qualogy.qafe.bind.presentation.event.function.dialog.GenericDialog;
import com.qualogy.qafe.core.application.ApplicationCluster;
import com.qualogy.qafe.core.application.DestoryFailedException;
import com.qualogy.qafe.core.application.InitializationFailedException;
import com.qualogy.qafe.core.datastore.ApplicationLocalStore;
import com.qualogy.qafe.core.datastore.DataIdentifier;
import com.qualogy.qafe.core.datastore.DataStore;
import com.qualogy.qafe.core.errorhandling.ErrorResult;
import com.qualogy.qafe.core.errorhandling.ExternalException;
import com.qualogy.qafe.core.framework.presentation.EventData;
import com.qualogy.qafe.core.framework.presentation.EventHandler;
import com.qualogy.qafe.presentation.handler.EventItemExecutor;
import com.qualogy.qafe.presentation.handler.executors.AbstractEventItemExecute;
import com.qualogy.qafe.presentation.handler.executors.ReturnBuiltInException;

public class EventHandlerImpl implements EventHandler {
	
	public final static Logger logger = Logger.getLogger(EventHandlerImpl.class.getName());
	
	private boolean showlog = false;
	
	public Collection<BuiltInFunction> manage(EventData eventData) throws ExternalException {
		ApplicationContext context = ApplicationCluster.getInstance().get(eventData.getApplicationIdentifier());
		ApplicationMapping applicationMapping = context.getApplicationMapping();

		LogFactory.getLog(this.getClass().getName()).info(ApplicationCluster.getInstance().toString());

		List<BuiltInFunction> builtInList = new ArrayList<BuiltInFunction>();
		if (applicationMapping != null) {
			// Events to process in the order that they are listed in the input XML's
			// First create a collection to store all the found events
			List<Event> eventList = new ArrayList<Event>();
			
			// Lookup event to execute
			Event eventToExecute = null;
			if(applicationMapping.getEvents().get(eventData.getEventId()) != null) {
				// Fetching Global Events
				eventToExecute = applicationMapping.getEvents().get(eventData.getEventId());
			} else {
				// Fetching Local Events
				eventToExecute = applicationMapping.getEvents().get(eventData.getWindowId()+eventData.getEventId());
			}
			if (eventToExecute != null) {
				eventList.add(eventToExecute);	
			}

			DataIdentifier dataId = DataStore.register();
			resolveLookupData(context, eventList, dataId, eventData);
			storeInternals(dataId, eventData);
			
			// Add the parameters to this event handling
			DataStore.store(dataId,DataStore.KEY_PARAMETERS,eventData.getParameters());
			
			logger.fine("BEFORE executing any of the events" + DataStore.toLogString(dataId));
			
			try {
				for (Event event : eventList) {
					builtInList.addAll(handle(context, event, dataId, eventData));
				}
				if ((context.getWarningMessages() != null) && (context.getWarningMessages().size() > 0)) {
					fetchWarningMessages(context, builtInList);
				}
				if (showlog){
					builtInList.add(new LogFunction(DataStore.toLogString(dataId) +"\n\nLocalStorage: " + ApplicationLocalStore.getInstance().toLogString(eventData.getWindowSession()) ));
				}
			} finally { 
				// Cleanup the DataStore
				DataStore.unregister(dataId);
			}
		}
		return builtInList;
	}

	private void fetchWarningMessages(ApplicationContext context, List<BuiltInFunction> builtInList) {
		StringBuffer warningMessages = new StringBuffer();
		for (String warningMessage : context.getWarningMessages()) {
			warningMessages.append(warningMessage + " ");
		}
		GenericDialog dialog = new GenericDialog("Warning message", null);
		dialog.setMessageData(warningMessages.toString());
		builtInList.add(dialog);
		context.getWarningMessages().clear();
	}
	
	protected void storeInternals(DataIdentifier dataId, EventData eventData) {
		// Store the source and sourceValue of the event (if applicable) 
		// in the DataStore before executing
		if (eventData.getSourceId() != null) {
			DataStore.store(dataId, eventData.getSourceId(), eventData.getSourceIdValue());
		}
		if (eventData.getSourceName() != null) {
			DataStore.store(dataId, eventData.getSourceName(), eventData.getSourceNameValue());
		}
		if (eventData.getSourceValue() != null) {
			DataStore.store(dataId, eventData.getSourceValue(), eventData.getSourceValueValue());
		}
		if (eventData.getSourceListenerType() != null) {
			DataStore.store(dataId, eventData.getSourceListenerType(), eventData.getSourceListenerTypeValue());
		}
		if (eventData.getInternalVariables() != null){
			DataStore.store(dataId, eventData.getInternalVariables());
		}
		if (eventData.getRequest() != null){
			DataStore.store(dataId, EventData.REQUEST_BASE_URL, eventData.getRequest().get(EventData.REQUEST_BASE_URL));
			DataStore.store(dataId, EventData.REQUEST_PROTOCOL, eventData.getRequest().get(EventData.REQUEST_PROTOCOL));
			DataStore.store(dataId, EventData.REQUEST_SERVER_NAME, eventData.getRequest().get(EventData.REQUEST_SERVER_NAME));
			DataStore.store(dataId, EventData.REQUEST_PORT, eventData.getRequest().get(EventData.REQUEST_PORT));
			DataStore.store(dataId, EventData.REQUEST_CONTEXT_ROOT, eventData.getRequest().get(EventData.REQUEST_CONTEXT_ROOT));
			if(eventData.getRequest().get(EventData.REQUEST_GEO) != null) {
				DataStore.store(dataId, EventData.REQUEST_GEO, eventData.getRequest().get(EventData.REQUEST_GEO));				
			}
			Object requestData = eventData.getRequest().get(EventData.REQUEST); 
			if (requestData!=null) {
				DataStore.store(dataId, EventData.REQUEST, requestData);
			}
			Object cookiesData =eventData.getRequest().get(EventData.REQUEST_COOKIES); 
			if (cookiesData != null) {
				DataStore.store(dataId, EventData.REQUEST_COOKIES, cookiesData);
			}
			Object mouseData = eventData.getMouse();
			if (mouseData != null) {
				DataStore.store(dataId,EventData.MOUSE,mouseData);
			}
		}
	}
	
	// TBD
//	public Collection<BuiltInFunction> handle(ApplicationContext context, Event event, DataIdentifier dataId, EventData eventData) throws ExternalException {
//
//		Collection<BuiltInFunction> listToExecute = new ArrayList<BuiltInFunction>();
//		List<EventItem> eventItems = event.getEventItems();
//
//		if (eventItems != null) {
//			ListIterator<EventItem> iter = eventItems.listIterator();
//			boolean stopProcessing = false;
//			try {
//				while (iter.hasNext() &&!stopProcessing) {
//					EventItem eventItem = iter.next();
//
//					stopProcessing = EventItemExecutor.getInstance().execute(eventItem, context, event, eventData, listToExecute, this, dataId);
//				}
//			}
//			
//			catch (Exception e) {
//				ErrorResult result = EventErrorProcessor.processError(context, dataId, iter, e,eventData);
//				if (result.hasItems())
//					for (Item item : result.getItems()) {
//						if (item instanceof EventItem) {
//							EventItemExecutor.getInstance().execute((EventItem)item, context, event, eventData, listToExecute, this, dataId);
//						}
//					}
//
//				if (result.getExternalException() != null){
//					result.getExternalException().setStackTrace(e.getStackTrace());
//					throw result.getExternalException();
//				}
//			}
//		}
//		return listToExecute;
//	}
	public Collection<BuiltInFunction> handle(ApplicationContext context, Event event, DataIdentifier dataId, EventData eventData) throws ExternalException {
		Collection<BuiltInFunction> builtInList = new ArrayList<BuiltInFunction>();
		if (event != null) {
			Collection<BuiltInFunction> eventBuiltInList = handleEventItems(event.getEventItems(), context, event, dataId, eventData);
			builtInList.addAll(eventBuiltInList);
		}
		return builtInList;
	}
	
	private Collection<BuiltInFunction> handleEventItems(List<EventItem> eventItems, ApplicationContext context, Event event, DataIdentifier dataId, EventData eventData) throws ExternalException {
		Collection<BuiltInFunction> builtInList = new ArrayList<BuiltInFunction>();
		if (eventItems != null) {
			ListIterator<EventItem> itrEventItem = eventItems.listIterator();
			while (itrEventItem.hasNext()) {
				EventItem eventItem = itrEventItem.next();
				try {
					boolean stopProcessing = EventItemExecutor.getInstance().execute(eventItem, context, event, eventData, builtInList, this, dataId);
					if (stopProcessing) {
						break;
					}
				} catch (ReturnBuiltInException e) {
					break;
				} catch (ExternalException e) {
					ErrorResult errorResult = EventErrorProcessor.processError(itrEventItem, eventItem, context, dataId, e, eventData);
					boolean rethrow = (errorResult.getExternalException() != null);
					if (rethrow) {
						throw errorResult.getExternalException();
					} else if (errorResult.hasItems()) {
						List<EventItem> exceptionHandlingEventItems = new ArrayList<EventItem>();
						for (Item item : errorResult.getItems()) {
							if (item instanceof EventItem) {
								exceptionHandlingEventItems.add((EventItem)item);
							}
						}
						Collection<BuiltInFunction> exceptionHandlingBuiltInList = handleEventItems(exceptionHandlingEventItems, context, event, dataId, eventData);
						builtInList.addAll(exceptionHandlingBuiltInList);
					}
				}
			}
		}
		return builtInList;
	}
	
	private void resolveLookupData(ApplicationContext context, List<Event> eventList, DataIdentifier dataId, EventData eventData) {
		for (Event event : eventList) {
			List<EventItem> eventItemList = event.getEventItems();
			if (eventItemList != null) {
				for (EventItem eventItem : eventItemList) {
					if (eventItem instanceof BusinessActionRef) {
						BusinessActionRef businessActionRef = (BusinessActionRef)eventItem;
						if (businessActionRef.getInput() != null) {
							List<Parameter> parameters = businessActionRef.getInput();
							for (Parameter parameter : parameters) {
								if (parameter instanceof In) {
									AbstractEventItemExecute executor = EventItemExecutor.getInstance().getExecutor(eventItem);
									if (executor != null) {
										In in = (In) parameter;
										Object value = executor.getValue(context, dataId, in, eventData);
									}
								}
							}
						}
					}	
				}
			}
		}
	}
	
	public void destroy(ApplicationContext context) throws DestoryFailedException {
		// TODO Auto-generated method stub
	}

	public void init(ApplicationContext context) throws InitializationFailedException {
		String configShowLog = ApplicationCluster.getInstance().getConfigurationItem(Configuration.SHOW_LOG);
		if (configShowLog != null) {
			if (Boolean.valueOf(configShowLog)) {
				showlog = true;
			} 
		}
	}

	public void refresh(ApplicationContext context) throws InitializationFailedException {
		// TODO Auto-generated method stub
	}
}
