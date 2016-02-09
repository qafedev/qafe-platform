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
package com.qualogy.qafe.gwt.server.event.assembler;

import java.util.Map;

import com.qualogy.qafe.bind.core.application.ApplicationContext;
import com.qualogy.qafe.bind.presentation.event.Event;
import com.qualogy.qafe.bind.presentation.event.EventItem;
import com.qualogy.qafe.bind.presentation.event.function.SetValue;
import com.qualogy.qafe.gwt.client.vo.functions.BuiltInFunctionGVO;
import com.qualogy.qafe.gwt.client.vo.functions.SetValueGVO;
import com.qualogy.qafe.gwt.client.vo.ui.event.ParameterGVO;

public class SetValueAssembler extends AbstractEventItemAssembler {

    public BuiltInFunctionGVO assemble(EventItem eventItem, Event event, ApplicationContext applicationContext) {
        SetValueGVO eventItemGVO = null;
        if (eventItem instanceof SetValue) {
            eventItemGVO = new SetValueGVO();
            assembleAttributes(eventItemGVO, (SetValue)eventItem, event, applicationContext);
        }
        return eventItemGVO;
    }

    private void assembleAttributes(SetValueGVO eventItemGVO, SetValue eventItem, Event event, ApplicationContext applicationContext) {
        ParameterGVO parameterGVO = assembleParameter(eventItem.getParameter());
        eventItemGVO.setParameter(parameterGVO);
        
        Map<String,String> mapping = assembleMapping(eventItem.getMapping());
        eventItemGVO.setMapping(mapping);
        
        eventItemGVO.setComponentId(eventItem.getComponentId());
        eventItemGVO.setNamedComponentId(parameterGVO.getName());
        eventItemGVO.setGroup(eventItem.getGroup());
        eventItemGVO.setAction(eventItem.getAction());
    }
}