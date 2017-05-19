/**
 * Copyright 2008-2017 Qualogy Solutions B.V.
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

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

import com.google.gwt.user.client.ui.UIObject;
import com.qualogy.qafe.gwt.client.component.QTableModel;
import com.qualogy.qafe.gwt.client.vo.data.EventItemDataGVO;
import com.qualogy.qafe.gwt.client.vo.functions.BuiltInFunctionGVO;
import com.qualogy.qafe.gwt.client.vo.functions.BusinessActionRefGVO;
import com.qualogy.qafe.gwt.client.vo.ui.event.ParameterGVO;

public class BusinessActionRefHandler extends AbstractBuiltInHandler {

	protected BuiltInState executeBuiltIn(final UIObject sender,
			final String listenerType, Map<String, String> mouseInfo,
			final BuiltInFunctionGVO builtInGVO, final String appId,
			final String windowId, final String eventSessionId,
			Queue derivedBuiltIns) {
    	BusinessActionRefGVO businessActionRefGVO = (BusinessActionRefGVO) builtInGVO;
        EventItemDataGVO eventItemDataGVO = new EventItemDataGVO();
        eventItemDataGVO.setAppId(appId);
        eventItemDataGVO.setWindowId(windowId);
        eventItemDataGVO.setSessionId(getSessionId());
        eventItemDataGVO.setBuiltInGVO(builtInGVO);        
		Map<String, Object> inputValues = collectInputValues(sender, appId,
				windowId, eventSessionId, businessActionRefGVO);
        eventItemDataGVO.setInputValues(inputValues);
		Map<String, Object> internalVariables = collectInteralVariables(sender,
				appId, windowId, eventSessionId);
		eventItemDataGVO.setInternalVariables(internalVariables);
        Map<String, String> outputVariables = collectOutputVariables(businessActionRefGVO);
        eventItemDataGVO.setOutputVariables(outputVariables);

		executeBuiltInServerSide(sender, listenerType, mouseInfo,
				eventItemDataGVO, appId, windowId, eventSessionId);

        storeInputValues(inputValues, eventSessionId);
        
        return BuiltInState.SUSPEND;
    }

	private Map<String, Object> collectInputValues(final UIObject sender,
			final String appId, final String windowId,
			final String eventSessionId,
			BusinessActionRefGVO businessActionRefGVO) {
        Map<String, Object> inputValues = new HashMap<String, Object>();
		for (ParameterGVO parameterGVO : businessActionRefGVO
				.getInputParameters()) {
            String key = parameterGVO.getName();
			Object value = getValue(sender, parameterGVO, appId, windowId,
					eventSessionId);
            inputValues.put(key, value);
        }
        return inputValues;
    }

	/**
	 * Collects all available internal variables.
	 * 
	 * @param sender
	 *            the UIObject sender
	 * @param appId
	 *            the app id
	 * @param windowId
	 *            the window id
	 * @param eventSessionId
	 *            the event session id
	 * @return a map of internal variables
	 */
	private Map<String, Object> collectInteralVariables(final UIObject sender,
			final String appId, final String windowId,
			final String eventSessionId) {
		Map<String, Object> internalVariables = new HashMap<String, Object>();
		for (String reservedKeyword : QTableModel.RESERVED_KEWORDS) {
			Object data = getData(eventSessionId, reservedKeyword);
			internalVariables.put(reservedKeyword, data);
		}
		return internalVariables;
	}

	private Map<String, String> collectOutputVariables(
			BusinessActionRefGVO businessActionRefGVO) {
	    Map<String, String> outputVariables = new HashMap<String, String>();
		for (ParameterGVO parameterGVO : businessActionRefGVO
				.getOutputParameters()) {
            String key = parameterGVO.getName();
            String reference = parameterGVO.getReference();
            outputVariables.put(key, reference);
        }
        return outputVariables;
    }
    
	private void storeInputValues(Map<String, Object> inputValues,
			String eventSessionId) {
    	for (String key : inputValues.keySet()) {
    		Object value = inputValues.get(key);
			storeData(eventSessionId, key, value);
		}
	}
}