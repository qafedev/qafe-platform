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

import com.google.gwt.user.client.ui.UIObject;
import com.qualogy.qafe.gwt.client.vo.functions.BuiltInFunctionGVO;
import com.qualogy.qafe.gwt.client.vo.functions.IfGVO;
import com.qualogy.qafe.gwt.client.vo.ui.event.ParameterGVO;

public class IfHandler extends AbstractBuiltInHandler {

	/**
	 * 
	 */
	@Override
	protected BuiltInState executeBuiltIn(UIObject sender, String listenerType,
			Map<String, String> mouseInfo, BuiltInFunctionGVO builtInGVO,
			String appId, String windowId, String eventSessionId,
			Queue derivedBuiltIns) {
		IfGVO ifGVO = (IfGVO) builtInGVO;
		handleIf(ifGVO, sender, appId, windowId, eventSessionId, derivedBuiltIns);
		return BuiltInState.EXECUTED;
	}
	
	/**
	 * 
	 * @param ifGVO
	 * @param sender
	 * @param appId
	 * @param windowId
	 * @param eventSessionId
	 */
	@SuppressWarnings("unchecked")
	private void handleIf(IfGVO ifGVO, UIObject sender, String appId, String windowId, String eventSessionId, Queue derivedBuiltIns) {
		ParameterGVO expression = ifGVO.getExpression();
		String result = (String) getValue(sender, expression, appId, windowId, eventSessionId);
		boolean bool = Boolean.valueOf(result);
		Collection<BuiltInFunctionGVO> eventItems = ifGVO.getEventItems(bool);
		if (eventItems != null) {
			derivedBuiltIns.addAll(eventItems);
		}
	}
}
