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
import com.qualogy.qafe.bind.presentation.event.function.ChangeStyle;
import com.qualogy.qafe.bind.presentation.event.function.ChangeStyleAction;
import com.qualogy.qafe.gwt.client.vo.functions.BuiltInComponentGVO;
import com.qualogy.qafe.gwt.client.vo.functions.BuiltInFunctionGVO;
import com.qualogy.qafe.gwt.client.vo.functions.ChangeStyleActionGVO;
import com.qualogy.qafe.gwt.client.vo.functions.ChangeStyleGVO;
import com.qualogy.qafe.util.StyleDomUtil;

public class ChangeStyleAssembler extends AbstractEventItemAssembler {

	public BuiltInFunctionGVO assemble(EventItem eventItem, Event event, ApplicationContext applicationContext) {
    	ChangeStyleGVO eventItemGVO = null;
        if (eventItem instanceof ChangeStyle) {
            eventItemGVO = new ChangeStyleGVO();
            assembleAttributes(eventItemGVO, (ChangeStyle)eventItem, event, applicationContext);
        }
        return eventItemGVO;
	}
	
    private void assembleAttributes(ChangeStyleGVO eventItemGVO, ChangeStyle eventItem, Event event, ApplicationContext applicationContext) {
		// Copy the components
		List<Component> components = eventItem.getComponents();
		List<BuiltInComponentGVO> componentGVOs = new ArrayList<BuiltInComponentGVO>();
		
		if (components != null) {
			for (Component component : components) {
				BuiltInComponentGVO componentGVO = assembleComponentGVO(component);
				componentGVOs.add(componentGVO);
			}			
		}
		eventItemGVO.setComponents(componentGVOs);
		
		List<ChangeStyleActionGVO> styleActionGVOs = new ArrayList<ChangeStyleActionGVO>();
		// Copy the style actions
		List<ChangeStyleAction> styleActions = eventItem.getActions();
		if (styleActions != null) {
			for (ChangeStyleAction changeStyleAction : styleActions) {
				 ChangeStyleActionGVO changeStyleActionGVO = assembleChangeStyleActionGVO(changeStyleAction);
				 styleActionGVOs.add(changeStyleActionGVO);
			}
		}
		eventItemGVO.setActions(styleActionGVOs);    	
    }

	private ChangeStyleActionGVO assembleChangeStyleActionGVO(ChangeStyleAction changeStyleAction) {
		ChangeStyleActionGVO changeStyleActionGVO = new ChangeStyleActionGVO();
		changeStyleActionGVO.setAction(changeStyleAction.getAction());
		changeStyleActionGVO.setKey(StyleDomUtil.initCapitalize(changeStyleAction.getKey()));
		changeStyleActionGVO.setValue(changeStyleAction.getValue());
		changeStyleActionGVO.setStyle(changeStyleAction.getStyle());
		return changeStyleActionGVO;
	}

	private BuiltInComponentGVO assembleComponentGVO(Component component) {
		BuiltInComponentGVO componentGVO = new BuiltInComponentGVO();
		componentGVO.setComponentId(component.getComponentId());
		componentGVO.setComponentName(component.getComponentName());
		return componentGVO;
	}
}
