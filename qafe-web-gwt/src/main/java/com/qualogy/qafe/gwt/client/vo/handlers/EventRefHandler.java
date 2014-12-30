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

import java.util.Map;
import java.util.Queue;

import com.google.gwt.user.client.ui.UIObject;
import com.qualogy.qafe.gwt.client.vo.functions.BuiltInFunctionGVO;
import com.qualogy.qafe.gwt.client.vo.functions.EventGVO;
import com.qualogy.qafe.gwt.client.vo.functions.EventRefGVO;
import com.qualogy.qafe.gwt.client.vo.ui.UIGVO;

public class EventRefHandler extends AbstractBuiltInHandler {

    protected BuiltInState executeBuiltIn(UIObject sender, String listenerType, Map<String, String> mouseInfo
    		, BuiltInFunctionGVO builtInGVO, String appId, String windowId, String eventSessionId, Queue derivedBuiltIns) {
        EventRefGVO eventRefGVO = (EventRefGVO) builtInGVO;
        resolveDerivedBuiltIns(eventRefGVO, sender, appId, windowId, eventSessionId, derivedBuiltIns);
        return BuiltInState.EXECUTED;
    }
    
    private void resolveDerivedBuiltIns(EventRefGVO eventRefGVO, UIObject sender
    		, String appId, String windowId, String eventSessionId, Queue derivedBuiltIns) {
    	UIGVO applicationGVO = EventHandler.getInstance().getApplication(appId);
        if (applicationGVO == null) {
            return;
        }
        String eventId = eventRefGVO.getEventId();
        EventGVO eventGVO = EventHandler.getInstance().getEvent(eventId, windowId, applicationGVO);
        if (eventGVO == null) {
            return;
        }
        Queue builtIns = EventHandler.getInstance().getBuiltIns(eventGVO);
       	derivedBuiltIns.addAll(builtIns);
    }
}