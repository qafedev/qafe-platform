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

import java.util.Map;
import java.util.Queue;

import com.google.gwt.user.client.ui.UIObject;
import com.qualogy.qafe.gwt.client.vo.functions.BuiltInFunctionGVO;
import com.qualogy.qafe.gwt.client.vo.functions.LocalStoreGVO;
import com.qualogy.qafe.gwt.client.vo.ui.event.ParameterGVO;

public class LocalStoreHandler extends AbstractBuiltInHandler {

	protected BuiltInState executeBuiltIn(UIObject sender, String listenerType, Map<String, String> mouseInfo, BuiltInFunctionGVO builtInGVO, String appId, String windowId, String eventSessionId, Queue derivedBuiltIns) {
        LocalStoreGVO localStoreGVO = (LocalStoreGVO) builtInGVO;
        ParameterGVO parameterGVO = localStoreGVO.getParameter();
        Object value = getValue(sender, parameterGVO, appId, windowId, eventSessionId);
        storeValue(localStoreGVO, value, appId, windowId, eventSessionId);
        return BuiltInState.EXECUTED;
    }
    
    private void storeValue(LocalStoreGVO localStoreGVO, Object value, String appId, String windowId, String eventSessionId) {
        ParameterGVO parameterGVO = localStoreGVO.getParameter();
        String name = parameterGVO.getName();
        String target = localStoreGVO.getTarget();
        String dataId = generateDataId(target, appId, windowId, eventSessionId);
        storeData(dataId, name, value);        
    }
}
