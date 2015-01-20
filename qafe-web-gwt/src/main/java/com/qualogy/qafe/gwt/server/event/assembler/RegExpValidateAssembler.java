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
package com.qualogy.qafe.gwt.server.event.assembler;

import java.util.ArrayList;
import java.util.List;

import com.qualogy.qafe.bind.core.application.ApplicationContext;
import com.qualogy.qafe.bind.presentation.event.Component;
import com.qualogy.qafe.bind.presentation.event.Event;
import com.qualogy.qafe.bind.presentation.event.EventItem;
import com.qualogy.qafe.bind.presentation.event.function.RegExpValidate;
import com.qualogy.qafe.gwt.client.vo.functions.BuiltInComponentGVO;
import com.qualogy.qafe.gwt.client.vo.functions.BuiltInFunctionGVO;
import com.qualogy.qafe.gwt.client.vo.functions.RegExpValidateGVO;

public class RegExpValidateAssembler extends AbstractEventItemAssembler {

	@Override
	public BuiltInFunctionGVO assemble(EventItem eventItem, Event event,
			ApplicationContext applicationContext) {
		RegExpValidateGVO eventItemGVO = null;
		if (eventItem instanceof RegExpValidate) {
			eventItemGVO = new RegExpValidateGVO();
			assembleAttributes(eventItemGVO, (RegExpValidate) eventItem, event,
					applicationContext);
		}
		return eventItemGVO;
	}

	/**
	 * 
	 * @param eventItemGVO
	 * @param eventItem
	 * @param event
	 * @param applicationContext
	 */
	private void assembleAttributes(RegExpValidateGVO eventItemGVO,
			RegExpValidate eventItem, Event event,
			ApplicationContext applicationContext) {
		List<Component> components = eventItem.getComponents();
		if (components != null) {
			List<BuiltInComponentGVO> componentGVOs = new ArrayList<BuiltInComponentGVO>();
			for (Component component : components) {
				BuiltInComponentGVO componentGVO = assembleBuiltInComponent(component);
				componentGVOs.add(componentGVO);
			}
			eventItemGVO.setComponents(componentGVOs);
		}
		eventItemGVO.setRegExp(eventItem.getRegexp());
		eventItemGVO.setType(eventItem.getType());
		eventItemGVO.setMessage(eventItem.getMessage());
	}
}
