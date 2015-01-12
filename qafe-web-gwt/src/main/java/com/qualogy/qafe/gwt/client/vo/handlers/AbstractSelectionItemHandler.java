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
package com.qualogy.qafe.gwt.client.vo.handlers;

import java.util.Map;

import com.google.gwt.user.client.ui.UIObject;
import com.qualogy.qafe.gwt.client.vo.functions.DataContainerGVO;
import com.qualogy.qafe.gwt.client.vo.ui.event.ParameterGVO;

public abstract class AbstractSelectionItemHandler extends AbstractBuiltInHandler {
	
	/**
	 * 
	 * @param expression
	 * @param sender
	 * @param appId
	 * @param windowId
	 * @param eventSessionId
	 * @return
	 */
	protected String evaluateExpression(ParameterGVO expression, UIObject sender, String appId, String windowId, String eventSessionId) {
		String expr = expression.getExpression();
		Map<String, Object> placeHolderValues = resolvePlaceholderValues(expression, sender, appId, windowId, eventSessionId);
		expr = resolveExpression(expr, placeHolderValues, eventSessionId);
		return super.evaluateExpression(expr);
	}
	
	/**
	 * 
	 * @param name
	 * @param placeHolderValues
	 * @param eventSessionId
	 * @return
	 */
	protected String resolveExpression(String name, Map<String, Object> placeHolderValues,
            String eventSessionId) {

        while (name != null && name.contains("${")) {
            String varName = name.substring(name.indexOf("{") + 1, name.indexOf("}"));
            Object value = null;
            if (placeHolderValues != null && placeHolderValues.containsKey(varName)) {
                value = placeHolderValues.get(varName);
            } else {
                value = getData(eventSessionId, varName);
            }
            if (value == null) {
                value = "null";
            } else {
            	if (value instanceof DataContainerGVO) {
            		value = DataContainerGVO.createType((DataContainerGVO) value);
            	}  
            	if (value instanceof String) {
                	value = "'" + value + "'";
                } else if (value instanceof Boolean) {
                	boolean bool = (Boolean) value;
                	value = bool ? "True" : "False";		
                }
            }
            name = name.replace("${" + varName + "}", value.toString());
        }
        return name;
    }
	
}
