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

import java.util.ArrayList;
import java.util.List;

import com.qualogy.qafe.bind.core.application.ApplicationContext;
import com.qualogy.qafe.bind.presentation.event.Component;
import com.qualogy.qafe.bind.presentation.event.Event;
import com.qualogy.qafe.bind.presentation.event.EventItem;
import com.qualogy.qafe.bind.presentation.event.function.SetProperty;
import com.qualogy.qafe.gwt.client.vo.functions.BuiltInComponentGVO;
import com.qualogy.qafe.gwt.client.vo.functions.BuiltInFunctionGVO;
import com.qualogy.qafe.gwt.client.vo.functions.SetPropertyGVO;
import com.qualogy.qafe.gwt.client.vo.ui.event.ParameterGVO;


public class SetPropertyAssembler extends AbstractEventItemAssembler {

    @Override
    public BuiltInFunctionGVO assemble(EventItem eventItem, Event event, ApplicationContext applicationContext) {
        SetPropertyGVO setPropertyGVO = null;
        if (eventItem instanceof SetProperty) {
            setPropertyGVO = new SetPropertyGVO();           
            SetProperty setProperty = (SetProperty)eventItem;

            assembleAttributes(setPropertyGVO, setProperty);
        }
        return setPropertyGVO;
    }

    private void assembleAttributes(SetPropertyGVO setPropertyGVO, SetProperty setProperty) {
        List<Component> components = setProperty.getComponents();
        List<BuiltInComponentGVO> componentGVOs = new ArrayList<BuiltInComponentGVO>();
        
        if (components != null) {
            for (Component component : components) {
                BuiltInComponentGVO componentGVO = assembleBuiltInComponent(component);
                componentGVOs.add(componentGVO);
            }           
        }
        ParameterGVO paramGVO = assembleParameter(setProperty.getParameter());
        setPropertyGVO.setParameter(paramGVO);
        setPropertyGVO.setComponents(componentGVOs);
        setPropertyGVO.setProperty(setProperty.getProperty());  
    }
}
