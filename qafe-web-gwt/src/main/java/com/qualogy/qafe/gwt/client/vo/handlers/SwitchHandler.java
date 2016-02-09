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
package com.qualogy.qafe.gwt.client.vo.handlers;

import java.util.Collection;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import com.google.gwt.user.client.ui.UIObject;
import com.qualogy.qafe.gwt.client.vo.functions.BuiltInFunctionGVO;
import com.qualogy.qafe.gwt.client.vo.functions.SwitchGVO;
import com.qualogy.qafe.gwt.client.vo.ui.event.ParameterGVO;

public class SwitchHandler extends AbstractBuiltInHandler {

	/**
	 * 
	 */
	@Override
	protected BuiltInState executeBuiltIn(UIObject sender, String listenerType,
			Map<String, String> mouseInfo, BuiltInFunctionGVO builtInGVO,
			String appId, String windowId, String eventSessionId,
			Queue derivedBuiltIns) {
		SwitchGVO switchGVO = (SwitchGVO) builtInGVO;
		handleSwitch(switchGVO, sender, appId, windowId, eventSessionId, derivedBuiltIns);
		return BuiltInState.EXECUTED;
	}
	
	/**
	 * 
	 * @param switchGVO
	 * @param sender
	 * @param appId
	 * @param windowId
	 * @param eventSessionId
	 */
	@SuppressWarnings("unchecked")
	private void handleSwitch(SwitchGVO switchGVO, UIObject sender, String appId, String windowId, String eventSessionId, Queue derivedBuiltIns) {
		ParameterGVO expression = switchGVO.getExpression();
		String result = (String) getValue(sender, expression, appId, windowId, eventSessionId);
		Collection<BuiltInFunctionGVO> eventItems = null;
		
		Set<Object> selectionCases = switchGVO.getSelectionCases();
		for (Object selectionCase : selectionCases) {
			Object value = selectionCase;
			if (selectionCase instanceof ParameterGVO) {
				value = getValue(sender, (ParameterGVO)selectionCase, appId, windowId, eventSessionId);
			}
			if (value == null) {
				continue;
			}
			if (value.equals(result)) {
				eventItems = switchGVO.getEventItems(selectionCase);
				break;
			}
		}
		if (eventItems == null) {
			eventItems = switchGVO.getDefaultEventItems();
		}
		if (eventItems != null) {
			derivedBuiltIns.addAll(eventItems);
		}
	}
}
