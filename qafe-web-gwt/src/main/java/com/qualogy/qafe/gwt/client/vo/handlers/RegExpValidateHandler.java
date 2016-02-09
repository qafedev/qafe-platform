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

import java.util.List;
import java.util.Map;
import java.util.Queue;

import com.google.gwt.user.client.ui.TextBoxBase;
import com.google.gwt.user.client.ui.UIObject;
import com.qualogy.qafe.gwt.client.vo.functions.BuiltInComponentGVO;
import com.qualogy.qafe.gwt.client.vo.functions.BuiltInFunctionGVO;
import com.qualogy.qafe.gwt.client.vo.functions.RegExpValidateGVO;

public class RegExpValidateHandler extends AbstractBuiltInHandler {

	@Override
	protected BuiltInState executeBuiltIn(UIObject sender, String listenerType,
			Map<String, String> mouseInfo, BuiltInFunctionGVO builtInGVO,
			String appId, String windowId, String eventSessionId,
			Queue derivedBuiltIns) {
		RegExpValidateGVO validateGVO = (RegExpValidateGVO) builtInGVO;
		return handleValidate(validateGVO, sender, appId, windowId,
				eventSessionId, derivedBuiltIns);
	}

	/**
	 * 
	 * @param validateGVO
	 * @param sender
	 * @param appId
	 * @param windowId
	 * @param eventSessionId
	 * @param derivedBuiltIns
	 * @return
	 */
	private BuiltInState handleValidate(RegExpValidateGVO validateGVO,
			UIObject sender, String appId, String windowId,
			String eventSessionId, Queue derivedBuiltIns) {
		for (BuiltInComponentGVO builtInComponentGVO : validateGVO
				.getComponents()) {
			String componentId = builtInComponentGVO.getComponentId();
			String componentUUID = generateId(componentId, windowId, appId,
					eventSessionId);
			List<UIObject> uiObjects = getUIObjects(componentUUID);
			if (uiObjects == null) {
				continue;
			}
			for (UIObject uiObject : uiObjects) {
				if (!(uiObject instanceof TextBoxBase)) {
					continue;
				}
				String textValue = ((TextBoxBase) uiObject).getText();
				if (textValue == null) {
					continue;
				}
				String expression = validateGVO.getRegExp();
				if (!matchExpression(textValue, expression)) {
					String message = validateGVO.getMessage();
					showMessage("Validation error", message);
					return BuiltInState.TERMINATE;
				}
			}
		}
		return BuiltInState.EXECUTED;
	}

	/**
	 * Matches a text value against a regular expression.
	 * 
	 * @param textValue
	 *            the value to match
	 * @param expression
	 *            the regular expression
	 * @return true if a match is found, false if no match is found
	 */
	private boolean matchExpression(String textValue, String expression) {
		return (textValue.replaceFirst(expression, "").length() == 0);
	}

}
