/**
 * Copyright 2008-2017 Qualogy Solutions B.V.
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
package com.qualogy.qafe.presentation.handler.executors;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.qualogy.qafe.bind.commons.type.Parameter;
import com.qualogy.qafe.bind.core.application.ApplicationContext;
import com.qualogy.qafe.bind.presentation.component.Window;
import com.qualogy.qafe.bind.presentation.event.Event;
import com.qualogy.qafe.bind.presentation.event.EventItem;
import com.qualogy.qafe.bind.presentation.event.function.BuiltInFunction;
import com.qualogy.qafe.bind.presentation.event.function.EventRef;
import com.qualogy.qafe.bind.presentation.event.function.dialog.GenericDialog;
import com.qualogy.qafe.core.datastore.DataIdentifier;
import com.qualogy.qafe.core.errorhandling.ExternalException;
import com.qualogy.qafe.core.framework.presentation.EventData;
import com.qualogy.qafe.presentation.EventHandlerImpl;
import com.qualogy.qafe.presentation.handler.ExecuteEventItem;

@Deprecated
public class EventRefExecute extends AbstractEventItemExecute implements ExecuteEventItem {

	public boolean execute(EventItem eventItem,ApplicationContext context, Event event, EventData eventData,Collection<BuiltInFunction> listToExecute,EventHandlerImpl eventHandler,DataIdentifier dataId) throws ExternalException {
		boolean stopProcessing=false;
		if (eventItem instanceof EventRef){
			EventRef eventRef = (EventRef)eventItem;
			String eventToCall = eventRef.getEvent();
			Set<String> visitedEvents = new HashSet<String>(17);
			visitedEvents.add(eventToCall);
			if (isRecursiveCall(eventToCall,context,event,eventData,visitedEvents)){
				GenericDialog dialog = new GenericDialog();
				dialog.setTitle(new Parameter("Recursive call of events detected"));
				dialog.setTitleData(dialog.getTitle().getValue().getStaticValue());
				dialog.setMessage(new Parameter("Recursive call detected. Cancelling the call builtin for this event, but still continuing the rest of builtins "));
				dialog.setMessageData(dialog.getMessage().getValue().getStaticValue());
				listToExecute.add(dialog);
			} else {
				if (eventData.getWindowId()!=null && eventData.getWindowId().length()>0){
					// so this call is made from a MDI window application
					Window window = (Window)context.getApplicationMapping().getPresentationTier().getView().getWindowNamesMap().get(eventData.getWindowId());
					try {
						if (window!=null){
							// check local event
							Event toCallEvent =(Event)window.getEventsMap().get(eventToCall);
							if (toCallEvent!=null){
								listToExecute.addAll(eventHandler.handle(context, toCallEvent, dataId,eventData));
							} else { // check global event
								toCallEvent =(Event)context.getApplicationMapping().getPresentationTier().getEventsMap().get(eventToCall);
								if (toCallEvent!=null){
									listToExecute.addAll(eventHandler.handle(context, toCallEvent, dataId,eventData));
								}	
							}
						} else { // so RootPanel only call
							Event toCallEvent =(Event)context.getApplicationMapping().getPresentationTier().getEventsMap().get(eventToCall);
							if (toCallEvent!=null){
								listToExecute.addAll(eventHandler.handle(context, toCallEvent, dataId,eventData));
							}
						}
					} catch (ReturnBuiltInException e){
						stopProcessing=true;
					}
				}
			}
			
		}
		return stopProcessing;
	}
	
	  public static boolean isRecursiveCall(String eventToCall, ApplicationContext context, Event event, EventData eventData, Set<String> visitedEvents) {
	    	if (eventData.getWindowId()!=null && eventData.getWindowId().length()>0){
				// so this call is made from a MDI window application
				Window window = (Window)context.getApplicationMapping().getPresentationTier().getView().getWindowNamesMap().get(eventData.getWindowId());
				if (window!=null){
					// check local event
					Event toCallEvent =(Event)window.getEventsMap().get(eventToCall);
					
					if (toCallEvent!=null){
						return checkVisitedCall(toCallEvent, event, visitedEvents, eventData, context);										
					} else { // check global event
						toCallEvent =(Event)context.getApplicationMapping().getPresentationTier().getEventsMap().get(eventToCall);
						return checkVisitedCall(toCallEvent, event, visitedEvents, eventData, context);						
					}
				}
			} else {
				Event toCallEvent =(Event)context.getApplicationMapping().getPresentationTier().getEventsMap().get(eventToCall);
				return checkVisitedCall(toCallEvent, event, visitedEvents, eventData, context);	
			}
			return false;
		}
	    
	    public static boolean checkVisitedCall(Event toCallEvent,Event event,Set<String> visitedEvents,EventData eventData,ApplicationContext context){
	    	if (toCallEvent!=null){
				if (toCallEvent.getEventItems()!=null){
					Iterator<EventItem> itr = toCallEvent.getEventItems().iterator();
					while(itr.hasNext()){
						EventItem eventItem =  itr.next();
						if (eventItem instanceof EventRef){
							EventRef eventRef = (EventRef)eventItem;
							String eventToInnerCall = eventRef.getEvent();
							if (visitedEvents.contains(eventToInnerCall)){
								return true;
							} else {
								return isRecursiveCall(eventToInnerCall, context, event, eventData,visitedEvents);
							}						
						}
					}
				}
	    	} 
	    	return false;
	    }


}
