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
package com.qualogy.qafe.gwt.client.vo.handlers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.ui.UIObject;
import com.qualogy.qafe.gwt.client.ui.renderer.events.EventCallbackHandler;
import com.qualogy.qafe.gwt.client.vo.data.EventItemDataGVO;
import com.qualogy.qafe.gwt.client.vo.functions.BuiltInFunctionGVO;
import com.qualogy.qafe.gwt.client.vo.functions.BusinessActionRefGVO;
import com.qualogy.qafe.gwt.client.vo.ui.event.ParameterGVO;

public class BusinessActionRefHandler extends AbstractBuiltInHandler {

    public final boolean handleBuiltIn(final UIObject sender, final String listenerType,
            final BuiltInFunctionGVO builtInFunctionGVO, final String appId, final String windowId, final String eventSessionId) {
        EventItemDataGVO eventItemDataGVO = new EventItemDataGVO();
        eventItemDataGVO.setAppId(appId);
        eventItemDataGVO.setBuiltInFunctionGVO(builtInFunctionGVO);
        BusinessActionRefGVO businessActionRefGVO = (BusinessActionRefGVO) builtInFunctionGVO;
        Map<String, Object> inputValues =
            collectInputValues(sender, appId, windowId, eventSessionId, businessActionRefGVO);
        eventItemDataGVO.setInputValues(inputValues);
        List<String> outputVariables = collectOutputValues(businessActionRefGVO);
        eventItemDataGVO.setOutputVariables(outputVariables);
            eventItemDataGVO.setInputValues(inputValues);

        EventCallbackHandler.invokeService(sender, listenerType, eventItemDataGVO, appId, windowId, eventSessionId);

        return false;
    }

    private Map<String, Object> collectInputValues(final UIObject sender, final String appId,
            final String windowId, final String eventSessionId, BusinessActionRefGVO businessActionRefGVO) {
        Map<String, Object> inputValues = new HashMap<String, Object>();
        for (ParameterGVO parameterGVO : businessActionRefGVO.getInputParameters()) {
            String key = parameterGVO.getName();
            Object value = getValue(sender, parameterGVO, appId, windowId, eventSessionId);
            inputValues.put(key, value);
        }
        return inputValues;
    }

    private List<String> collectOutputValues(BusinessActionRefGVO businessActionRefGVO) {
        List<String> outputVariables = new ArrayList<String>();
        for (ParameterGVO parameterGVO : businessActionRefGVO.getOutputParameters()) {
            String key = parameterGVO.getName();
            outputVariables.add(key);
        }
        return outputVariables;
    }

}
