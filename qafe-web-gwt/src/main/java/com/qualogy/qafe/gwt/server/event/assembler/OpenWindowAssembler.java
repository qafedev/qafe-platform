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
package com.qualogy.qafe.gwt.server.event.assembler;

import com.qualogy.qafe.bind.commons.type.Parameter;
import com.qualogy.qafe.bind.commons.type.Value;
import com.qualogy.qafe.bind.core.application.ApplicationContext;
import com.qualogy.qafe.bind.presentation.event.Event;
import com.qualogy.qafe.bind.presentation.event.EventItem;
import com.qualogy.qafe.bind.presentation.event.function.OpenWindow;
import com.qualogy.qafe.gwt.client.vo.functions.BuiltInFunctionGVO;
import com.qualogy.qafe.gwt.client.vo.functions.OpenWindowGVO;

public class OpenWindowAssembler extends AbstractEventItemAssembler {

    public BuiltInFunctionGVO assemble(EventItem eventItem, Event event, ApplicationContext applicationContext) {
        OpenWindowGVO eventItemGVO = null;
        if (eventItem instanceof OpenWindow) {
            eventItemGVO = new OpenWindowGVO();
            assembleAttributes(eventItemGVO, (OpenWindow)eventItem, event, applicationContext);
        }
        return eventItemGVO;
    }
    
    private void assembleAttributes(OpenWindowGVO eventItemGVO, OpenWindow eventItem, Event event, ApplicationContext applicationContext) {
        String appId = applicationContext.getId().toString();
        eventItemGVO.setUuid(appId);
        eventItemGVO.setContext(appId);
        
        String invokeWindowId = eventItem.getWindowData();
        if (invokeWindowId == null) {
            Parameter reference = eventItem.getWindow();
            if (reference != null) {
                Value value = reference.getValue();
                if (value != null) {
                    invokeWindowId = value.getStaticValue();        
                }
            }
        }
        eventItemGVO.setWindow(invokeWindowId);
        
        eventItemGVO.setUrl(eventItem.getUrlData());      
        eventItemGVO.setTitle(eventItem.getTitleData());
        eventItemGVO.setParams(eventItem.getParamsData());
        eventItemGVO.setExternal(eventItem.getExternal());
        eventItemGVO.setPlacement(eventItem.getPlacement());   
    }
}
