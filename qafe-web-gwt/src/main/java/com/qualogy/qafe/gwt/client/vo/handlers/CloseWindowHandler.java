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

import java.util.List;
import java.util.Map;
import java.util.Queue;

import org.gwt.mosaic.ui.client.WindowPanel;

import com.google.gwt.user.client.ui.UIObject;
import com.qualogy.qafe.gwt.client.context.ClientApplicationContext;
import com.qualogy.qafe.gwt.client.factory.WindowFactory;
import com.qualogy.qafe.gwt.client.vo.functions.BuiltInFunctionGVO;
import com.qualogy.qafe.gwt.client.vo.functions.CloseWindowGVO;

public class CloseWindowHandler extends AbstractBuiltInHandler {

	protected BuiltInState executeBuiltIn(UIObject sender, String listenerType, Map<String, String> mouseInfo, BuiltInFunctionGVO builtInGVO, String appId, String windowId, String eventSessionId, Queue derivedBuiltIns) {
        CloseWindowGVO closeWindowGVO = (CloseWindowGVO) builtInGVO;
        closeWindow(closeWindowGVO, sender, appId, windowId, eventSessionId);
        return BuiltInState.EXECUTED;
    }
    
    private void closeWindow(CloseWindowGVO closeWindowGVO, UIObject sender, String appId, String windowId, String eventSessionId) {
        String window = (String) getValue(sender, closeWindowGVO.getWindowGVO(), appId, windowId, eventSessionId);
        if (window == null || window.length() == 0) {
        	return;
        }
        if (ClientApplicationContext.getInstance().isMDI()){
        	String uuid = getUUId(sender);
			ClientApplicationContext.getInstance().removeWindow(window, appId, uuid);
		} else {
			WindowFactory.clearWidgetFromMainPanel();
		}
        removeVariables(appId, window, eventSessionId);
    }
    
    private void removeVariables(String appId, String windowId, String eventSessionId) {
    	removeUserVariables(appId, windowId, eventSessionId);
    	removeGlobalVariables(appId, windowId, eventSessionId);
    }
    
    private void removeUserVariables(String appId, String windowId, String eventSessionId) {
    	String dataId = generateDataId(BuiltInFunctionGVO.SOURCE_APP_LOCAL_STORE_ID, appId, windowId, eventSessionId);
		ClientApplicationContext.getInstance().getDataStorage().removeData(dataId);
    }
    
    private void removeGlobalVariables(String appId, String windowId, String eventSessionId) {
    	List<WindowPanel> existingWindows = ClientApplicationContext.getInstance().getWindows();
    	if (existingWindows.size() > 0) {
    		for (WindowPanel window : existingWindows) {
    			String openedWindowAppId = getAppId(window);
    			if (openedWindowAppId.equals(appId)) {
    				return;
    			}
    		}
    	}
    	String dataId = generateDataId(BuiltInFunctionGVO.SOURCE_APP_GLOBAL_STORE_ID, appId, windowId, eventSessionId);
		ClientApplicationContext.getInstance().getDataStorage().removeData(dataId);    	
    }    
}