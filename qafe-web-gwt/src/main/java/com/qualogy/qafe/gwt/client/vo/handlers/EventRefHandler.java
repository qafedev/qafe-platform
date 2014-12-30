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
package com.qualogy.qafe.gwt.client.vo.handlers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import com.google.gwt.user.client.ui.UIObject;
import com.qualogy.qafe.gwt.client.vo.functions.BuiltInFunctionGVO;
import com.qualogy.qafe.gwt.client.vo.functions.EventGVO;
import com.qualogy.qafe.gwt.client.vo.functions.EventRefGVO;
import com.qualogy.qafe.gwt.client.vo.ui.UIGVO;

public class EventRefHandler extends AbstractBuiltInHandler {

    protected BuiltInState executeBuiltIn(UIObject sender, String listenerType, Map<String, String> mouseInfo
    		, BuiltInFunctionGVO builtInGVO, String appId, String windowId, String eventSessionId, Queue derivedBuiltIns) {
        EventRefGVO eventRefGVO = (EventRefGVO) builtInGVO;
        EventGVO eventGVO = getEvent(eventRefGVO, appId, windowId);
        if (eventGVO != null) {
        	String eventId = eventRefGVO.getEventId();
        	List<String> eventReferencesCall = new ArrayList<String>();
        	if (isRecursiveCall(eventId, eventGVO, eventReferencesCall, appId, windowId)) {
        		showRecursiveMessage(eventId, eventGVO, eventReferencesCall);
            } else {
            	derivedBuiltIns.addAll(eventGVO.getEventItems());	
            }
        }
        return BuiltInState.EXECUTED;
    }
    
    private boolean isRecursiveCall(String eventId, EventGVO eventGVO, List<String> eventReferencesCall
    		, String appId, String windowId) {
    	return isRecursiveCall(eventId, eventGVO, new HashSet<String>(), eventReferencesCall, appId, windowId);
    }
    
    private boolean isRecursiveCall(String eventId, EventGVO eventGVO, Set<String> visitedEvents, List<String> eventReferencesCall
    		, String appId, String windowId) {
    	if (eventGVO == null) {
    		return false;
    	}
    	Collection<BuiltInFunctionGVO> eventItems = eventGVO.getEventItems();
		if (eventItems == null) {
			return false;
		}
		visitedEvents.add(eventId);
		for (BuiltInFunctionGVO eventItemGVO : eventItems) {
			if (eventItemGVO instanceof EventRefGVO) {
				EventRefGVO eventRefGVO = (EventRefGVO) eventItemGVO;
				String otherEventId = eventRefGVO.getEventId();
				eventReferencesCall.add(otherEventId);
				if (visitedEvents.contains(otherEventId)) {
					return true;
				}
				visitedEvents.add(otherEventId);
				EventGVO otherEventGVO = getEvent(eventRefGVO, appId, windowId);
				if (isRecursiveCall(otherEventId, otherEventGVO, visitedEvents, eventReferencesCall, appId, windowId)) {
					return true;
				}
				eventReferencesCall.remove(otherEventId);
			}
		}
		return false;	
	}
    
    private void showRecursiveMessage(String eventId, EventGVO eventGVO, List<String> eventReferencesCall) {
    	String title = "Recursive call of events detected";
    	
		StringBuffer message = new StringBuffer();
		message.append("Ignore the event reference [" + eventId + "], call of events: ");
		message.append("[");
		for (int i=0; i<eventReferencesCall.size(); i++) {
			if (i > 0) {
				message.append(" -> ");
			}
			String otherEventId = eventReferencesCall.get(i);
			message.append(otherEventId);
		}
		message.append("]");
    	showMessage(title, message.toString());
    }

	private EventGVO getEvent(EventRefGVO eventRefGVO, String appId, String windowId) {
    	UIGVO applicationGVO = EventHandler.getInstance().getApplication(appId);
        if (applicationGVO == null) {
            return null;
        }
        String eventId = eventRefGVO.getEventId();
        return EventHandler.getInstance().getEvent(eventId, windowId, applicationGVO);
    }
}