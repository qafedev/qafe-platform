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
import java.util.Queue;

import org.gwt.mosaic.ui.client.WindowPanel;

import com.google.gwt.user.client.ui.UIObject;
import com.qualogy.qafe.gwt.client.component.QWindowPanel;
import com.qualogy.qafe.gwt.client.context.ClientApplicationContext;
import com.qualogy.qafe.gwt.client.vo.functions.BuiltInFunctionGVO;
import com.qualogy.qafe.gwt.client.vo.functions.LogFunctionGVO;
import com.qualogy.qafe.gwt.client.vo.ui.event.ParameterGVO;

public class LogHandler extends AbstractBuiltInHandler {

	protected BuiltInState executeBuiltIn(UIObject sender, String listenerType, Map<String, String> mouseInfo, BuiltInFunctionGVO builtInGVO, String appId, String windowId, String eventSessionId, Queue derivedBuiltIns) {
        LogFunctionGVO logFunctionGVO = (LogFunctionGVO) builtInGVO;
        logMessage(sender, logFunctionGVO, appId, windowId, eventSessionId);
        return BuiltInState.EXECUTED;
    }
    
    private void logMessage(UIObject sender, LogFunctionGVO logFunctionGVO, String appId, String windowId, String eventSessionId) {
        ParameterGVO parameterGVO = logFunctionGVO.getMessageGVO();
        String message = getValue(sender, parameterGVO, appId, windowId, eventSessionId).toString();
		int delay = logFunctionGVO.getDelay();
		String styleClass = logFunctionGVO.getStyleClass();
		String[][] styleProperties = logFunctionGVO.getStyleProperties();
		
		ClientApplicationContext.getInstance().log(message);
		if (!logFunctionGVO.getDebug().booleanValue()){
			String uuid = getUUId(sender);
			WindowPanel wp = ClientApplicationContext.getInstance().getWindow(uuid, windowId);
			if (wp instanceof QWindowPanel){
				QWindowPanel qwp = (QWindowPanel)wp;
				qwp.showMessage(message, delay, styleClass, styleProperties);
			}
		}
    }
}
