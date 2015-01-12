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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.UIObject;
import com.qualogy.qafe.gwt.client.component.QWindowPanel;
import com.qualogy.qafe.gwt.client.context.ClientApplicationContext;
import com.qualogy.qafe.gwt.client.ui.renderer.RendererHelper;
import com.qualogy.qafe.gwt.client.util.ComponentRepository;
import com.qualogy.qafe.gwt.client.vo.functions.BuiltInComponentGVO;
import com.qualogy.qafe.gwt.client.vo.functions.BuiltInFunctionGVO;
import com.qualogy.qafe.gwt.client.vo.functions.ChangeStyleActionGVO;
import com.qualogy.qafe.gwt.client.vo.functions.ChangeStyleGVO;

public class ChangeStyleHandler extends AbstractBuiltInHandler {

	private static final String ACTION_REMOVE = "remove";
	private static final String ACTION_SET = "set";	
	
	@Override
	protected BuiltInState executeBuiltIn(UIObject sender, String listenerType,
			Map<String, String> mouseInfo, BuiltInFunctionGVO builtInGVO,
			String appId, String windowId, String eventSessionId,
			Queue derivedBuiltIns) {
		ChangeStyleGVO changeStyleGVO = (ChangeStyleGVO) builtInGVO;
		changeStyle(changeStyleGVO , sender, appId, windowId, eventSessionId);
		return BuiltInState.EXECUTED;
	}

	private void changeStyle(ChangeStyleGVO changeStyleGVO, UIObject sender,
			String appId, String windowId, String eventSessionId) {
		List<BuiltInComponentGVO> components = changeStyleGVO.getComponents();
		if (components == null) {
			return;
		}
		
		for (BuiltInComponentGVO builtInComponentGVO : components) {
			List<UIObject> uiObjects = getUIObjects(builtInComponentGVO, appId, windowId, eventSessionId);
			
			if (uiObjects == null) { // Components within a window are not found. Check for SDI.
				uiObjects = checkSDIMode(uiObjects);
			}
			
			if (uiObjects == null) {
				continue;
			}
			
			for (UIObject object : uiObjects) {
                if (object == null) {
                	continue;
                }
				
                if (object instanceof QWindowPanel) {
                    object = ((QWindowPanel)object).getQRootPanel();
                }
                Element element = object.getElement();
                if (element == null) {
                	continue;
                }
                
                List<ChangeStyleActionGVO> actions = changeStyleGVO.getActions();
                if (actions == null) {
                	continue;
                }

                handleChangeStyle(object, element, actions);  
            }
		}
	}

    private void handleChangeStyle(UIObject object, Element element, List<ChangeStyleActionGVO> actions) {
        for (ChangeStyleActionGVO changeStyleAction : actions) {
            String action = changeStyleAction.getAction().toLowerCase();
        	String key = changeStyleAction.getKey();
        	String style = changeStyleAction.getStyle();
        	
        	if (ACTION_REMOVE.equals(action)) {
                if (key != null && key.trim().length() > 0) {
                	RendererHelper.setStyleForElement(element, key, null);
                }
                if (style != null && style.trim().length() > 0) {
                	object.removeStyleName(style);
                }
            } else if (ACTION_SET.equals(action)) {
        	    if (key != null && key.trim().length() > 0) {
        	    	RendererHelper.setStyleForElement(element, key, changeStyleAction.getValue());
        	    }
        	    if (style != null && style.trim().length() > 0) {
        	    	object.addStyleName(style);
        	    }
        	}									
        }
    }

    private List<UIObject> checkSDIMode(List<UIObject> uiObjects) {
        if (!(ClientApplicationContext.getInstance().isMDI())) {
        	UIObject mainPanel = ClientApplicationContext.getInstance().getMainPanel().getWidget();
        	uiObjects = new ArrayList<UIObject>();
        	if (mainPanel!=null){
        		uiObjects.add(mainPanel);
        	}
        }
        return uiObjects;
    }

	private List<UIObject> getUIObjects(BuiltInComponentGVO builtInComponentGVO, String appId, String windowId, String eventSessionId) {
		List<UIObject> uiObjects = null;
		String componentId = builtInComponentGVO.getComponentId();
		componentId = resolveVariables(componentId, null, eventSessionId);
		String key = generateId(componentId, windowId, appId, eventSessionId);
		uiObjects = ComponentRepository.getInstance().getComponent(key);
		if (uiObjects != null) {
			return uiObjects;
		}
		
		String componentName = builtInComponentGVO.getComponentName();
		componentName = resolveVariables(componentName, null, eventSessionId);
		key = generateId(componentName, windowId, appId, eventSessionId);
		uiObjects = ComponentRepository.getInstance().getNamedComponent(key);
		return uiObjects;
	}
}
