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
import com.qualogy.qafe.gwt.client.component.DialogComponent;
import com.qualogy.qafe.gwt.client.component.QWindowPanel;
import com.qualogy.qafe.gwt.client.context.ClientApplicationContext;
import com.qualogy.qafe.gwt.client.ui.renderer.RendererHelper;
import com.qualogy.qafe.gwt.client.util.ComponentRepository;
import com.qualogy.qafe.gwt.client.vo.functions.BuiltInFunctionGVO;
import com.qualogy.qafe.gwt.client.vo.functions.dialog.GenericDialogGVO;
import com.qualogy.qafe.gwt.client.vo.ui.event.ParameterGVO;

public class GenericDialogHandler extends AbstractBuiltInHandler {

	protected BuiltInState executeBuiltIn(UIObject sender, String listenerType, Map<String, String> mouseInfo, BuiltInFunctionGVO builtInGVO, String appId, String windowId, String eventSessionId, Queue derivedBuiltIns) {
        GenericDialogGVO genericDialogGVO = (GenericDialogGVO) builtInGVO;
        showDialog(sender, genericDialogGVO, appId, windowId, eventSessionId);
        return BuiltInState.EXECUTED;
    }
	
	private void showDialog(UIObject sender, GenericDialogGVO genericDialogGVO, String appId, String windowId, String eventSessionId) {
		int left = 0;
		int top = 0;
		QWindowPanel window = getWindow(appId, windowId);
		if (window != null) {
			int windowHeight = resolveNumeric(window.getHeight(), 0);
			int windowWidth = resolveNumeric(window.getWidth(), 0);
			int windowXCoordiniate = ClientApplicationContext.getInstance().getStartXPosition();
			int windowYCoordiniate = ClientApplicationContext.getInstance().getStartYPosition();
			left = (windowXCoordiniate + windowWidth)/2;
			top = (windowYCoordiniate + windowHeight)/2;
		}
		String title = getTitle(sender, genericDialogGVO.getTitleGVO(), appId, windowId, eventSessionId);
		String message = getMessage(sender, genericDialogGVO.getMessageGVO(), appId, windowId, eventSessionId);
		String type = genericDialogGVO.getType();
		String detailMessage = new NullPointerException().getLocalizedMessage();
		DialogComponent.showDialog(title,message, type, detailMessage, left, top);
	}
	
	private String getTitle(UIObject sender, ParameterGVO parameterGVO, String appId, String windowId, String eventSessionId) {
		Object value = getValue(sender, parameterGVO, appId, windowId, eventSessionId);
		if (value == null) {
			return "No Title.";
		}
		
		if (value.toString() == null) {
			return "No Title.";
		} 
		return value.toString();
	}
	

	private String getMessage(UIObject sender, ParameterGVO parameterGVO, String appId, String windowId, String eventSessionId) {
		Object value = getValue(sender, parameterGVO, appId, windowId, eventSessionId);
		if (value == null) {
			return "No message.";
		}
		
		if (value.toString() == null) {
			return "No message.";
		} 
		return value.toString();			
	}
	
	private QWindowPanel getWindow(String appId, String windowId) {
		String windowKey = RendererHelper.generateId(windowId, windowId, appId);
		List<UIObject> uiObjects = ComponentRepository.getInstance().getComponent(windowKey);
		if (uiObjects != null) {
			for (UIObject uiObject : uiObjects) {
				if(uiObject instanceof QWindowPanel){
					return (QWindowPanel) uiObject;
				}
			}
		}
		return null;
	}
	
	private int resolveNumeric(String value, int defaultValue) {
		if (value == null) {
			return defaultValue;
		}
		int indexOfPX = value.indexOf("px");
		if (indexOfPX > -1) {
			value = value.substring(0, indexOfPX);
		}
		return Integer.parseInt(value);
	}
}