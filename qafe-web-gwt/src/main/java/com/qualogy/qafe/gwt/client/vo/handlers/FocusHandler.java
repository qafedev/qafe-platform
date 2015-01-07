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

import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;
import com.qualogy.qafe.gwt.client.component.HasDataGridMethods;
import com.qualogy.qafe.gwt.client.util.ComponentRepository;
import com.qualogy.qafe.gwt.client.vo.functions.BuiltInFunctionGVO;
import com.qualogy.qafe.gwt.client.vo.functions.FocusGVO;

public class FocusHandler extends AbstractBuiltInHandler {

    protected BuiltInState executeBuiltIn(UIObject sender, String listenerType, Map<String, String> mouseInfo
    		, BuiltInFunctionGVO builtInGVO, String appId, String windowId, String eventSessionId, Queue derivedBuiltIns) {
        FocusGVO focusGVO = (FocusGVO) builtInGVO;
        focus(focusGVO, sender, appId, windowId, eventSessionId);
        return BuiltInState.EXECUTED;
    }
    
	private void focus(FocusGVO focusGVO, UIObject sender, String appId, String windowId, String eventSessionId) {
		String componentId = focusGVO.getComponentId();
		List<UIObject> uiObjects = getUIObjects(componentId, appId, windowId, eventSessionId);
		if (uiObjects == null) {
			return;
		}
		for (UIObject uiObject : uiObjects) {
			if (uiObject instanceof Widget) {
				makeParentVisible((Widget) uiObject);
			}
			if (uiObject instanceof Focusable) {
				((Focusable) uiObject).setFocus(true);
			}
		}
	}
	
	private List<UIObject> getUIObjects(String componentId, String appId, String windowId, String eventSessionId) {
		componentId = resolveVariables(componentId, null, eventSessionId);
		String key = generateId(componentId, windowId, appId);
		List<UIObject> uiObjects = ComponentRepository.getInstance().getComponent(key);
		if (uiObjects == null) {
			List<UIObject> parentUIObjects = getParentUIObjects(key);
			if (parentUIObjects != null) {
				uiObjects = new ArrayList<UIObject>();
				for (UIObject parentUIObject : parentUIObjects) {
					if (parentUIObject instanceof HasDataGridMethods) {
						HasDataGridMethods hasDataGridMethods = (HasDataGridMethods) parentUIObject;						
						int rowIndex = getRowIndex(key, hasDataGridMethods);
						uiObjects = collectCellUIObjects(key, rowIndex, uiObjects);
					}
				}
			}
		}
		if (uiObjects == null) {
			uiObjects = ComponentRepository.getInstance().getNamedComponent(key);
		}
		return uiObjects;
	}
	
	private void makeParentVisible(Widget widget) {
		if (widget == null) {
			return;
		}
		Widget parent = widget.getParent();
		if (parent == null) {
			return;
		}
		makeParentVisible(parent);
		if (parent instanceof DeckPanel) {
			DeckPanel deckPanel = (DeckPanel) parent;
			int widgetIndex = deckPanel.getWidgetIndex(widget);
			deckPanel.showWidget(widgetIndex);
			TabPanel tabPanel = (TabPanel) deckPanel.getParent().getParent();
			tabPanel.selectTab(widgetIndex);
		}
	}
}