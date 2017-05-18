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

import com.google.gwt.user.client.ui.UIObject;
import com.qualogy.qafe.gwt.client.ui.renderer.RendererHelper;
import com.qualogy.qafe.gwt.client.vo.functions.BuiltInComponentGVO;
import com.qualogy.qafe.gwt.client.vo.functions.BuiltInFunctionGVO;
import com.qualogy.qafe.gwt.client.vo.functions.ToggleGVO;

public class ToggleHandler extends AbstractBuiltInHandler {

	protected BuiltInState executeBuiltIn(UIObject sender, String listenerType, Map<String, String> mouseInfo, BuiltInFunctionGVO builtInGVO, String appId, String windowId, String eventSessionId, Queue derivedBuiltIns) {
        ToggleGVO toggleGVO = (ToggleGVO) builtInGVO;
        toggle(toggleGVO, appId, windowId, eventSessionId);
        return BuiltInState.EXECUTED;
    }
    
    private void toggle(ToggleGVO toggleGVO, String appId, String windowId, String eventSessionId) {
        List<BuiltInComponentGVO> builtInComponentGVOs = toggleGVO.getComponents(); 
		if (builtInComponentGVOs != null) {
			for (BuiltInComponentGVO builtInComponentGVO : builtInComponentGVOs) {
				List<UIObject> uiObjects = null;
				String componentId = builtInComponentGVO.getComponentId();
				if (componentId != null) {
					String componentKey = resolveComponentKey(componentId, appId, windowId, eventSessionId);
					uiObjects = RendererHelper.getComponent(componentKey);
				} else {
					String componentName = builtInComponentGVO.getComponentName();
					String componentKey = resolveComponentKey(componentName, appId, windowId, eventSessionId);
					uiObjects = RendererHelper.getNamedComponent(componentKey);
				}
				if (uiObjects == null) {
					continue;
				}
				for (UIObject uiObject : uiObjects) {
					boolean currentVisibility = uiObject.isVisible();
					uiObject.setVisible(!currentVisibility);
				}
			}
		}        
    }
    
    private String resolveComponentKey(String reference, String appId, String windowId, String eventSessionId) {
		reference = resolveVariables(reference, null, eventSessionId);
		reference = stripString(reference);
    	return RendererHelper.generateId(reference, windowId, appId);
    }
	
	private String stripString(String input) {
		String componentId = "";
		
		if (input != null) {
			String[] splitString = input.split("\\.");
			String componentIdWithBrackets = splitString[0];
			if (splitString.length == 2) {
				componentIdWithBrackets = splitString[1];
			}
			
			String[] removeBrackets = componentIdWithBrackets.split("\\[");
			
			componentId = removeBrackets[0];
		}
		
		return componentId;
	}
}
