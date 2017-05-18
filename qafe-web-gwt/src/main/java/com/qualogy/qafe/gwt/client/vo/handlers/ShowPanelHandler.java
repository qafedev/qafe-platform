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

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.widgetideas.table.client.overrides.FlexTable;
import com.qualogy.qafe.gwt.client.component.ShowPanelComponent;
import com.qualogy.qafe.gwt.client.ui.renderer.RendererHelper;
import com.qualogy.qafe.gwt.client.util.ComponentRepository;
import com.qualogy.qafe.gwt.client.vo.data.EventDataI;
import com.qualogy.qafe.gwt.client.vo.functions.BuiltInFunctionGVO;
import com.qualogy.qafe.gwt.client.vo.functions.ShowPanelGVO;
import com.qualogy.qafe.gwt.client.vo.ui.ComponentGVO;

public class ShowPanelHandler extends AbstractBuiltInHandler {

	private static final Object CENTER_POSITION = "center";
	
	protected BuiltInState executeBuiltIn(UIObject sender, String listenerType, Map<String, String> mouseInfo, BuiltInFunctionGVO builtInGVO, String appId, String windowId, String eventSessionId, Queue derivedBuiltIns) {
        ShowPanelGVO showPanelGVO = (ShowPanelGVO) builtInGVO;
        showPanel(showPanelGVO, mouseInfo, appId, windowId, eventSessionId);
        return BuiltInState.EXECUTED;
    }
    
    private void showPanel(ShowPanelGVO showPanelGVO, Map<String, String> mouseInfo, String appId, String windowId, String eventSessionId) {
    	final ComponentGVO panelDefGVO = showPanelGVO.getSrc();
    	final String panelDefId = panelDefGVO.getId();
    	BuiltinHandlerHelper.closeOpenedPanelDefinition(panelDefId, appId, windowId, eventSessionId);
    	
    	final boolean autoHide = showPanelGVO.isAutoHide();
    	final boolean modal = showPanelGVO.isModal();    	
    	int left = showPanelGVO.getLeft();
    	int top = showPanelGVO.getTop();
    	String position = showPanelGVO.getPosition();
    	if ((left > 0) && (top > 0)) {
    		position = null;
    	} else {
    		left = 0;
    		top = 0;
    		if (position == null) {
    			if (mouseInfo.containsKey(EventDataI.MOUSE_X)) {
        			left = Integer.parseInt(mouseInfo.get(EventDataI.MOUSE_X));
    			}
    			if (mouseInfo.containsKey(EventDataI.MOUSE_Y)) {
    				top = Integer.parseInt(mouseInfo.get(EventDataI.MOUSE_Y));
    			}
    		}
    	}
    	
		final ShowPanelComponent showPanel = new ShowPanelComponent(autoHide, modal, left, top);
		Widget panelDef = (Widget) renderComponent(panelDefGVO, eventSessionId, windowId, appId);
		FlexTable panel = new FlexTable();
		if (!autoHide) {
			Label close = new Label("X");
			close.setStyleName("qafe_close_on_showPanel");
			close.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					showPanel.hide();
				}
			});
			panel.setWidget(0, 1, close);	
		}
		panel.setWidget(1, 0, panelDef);
		showPanel.setWidget(panel);
		
		String panelDefKey = BuiltinHandlerHelper.generatePanelDefinitionKey(panelDefId, appId, windowId);
		String windowKey =  RendererHelper.generateId(windowId, windowId, appId);
		showPanel.setId(panelDefKey);
		showPanel.setWindow(windowId);		
		showPanel.setWindowIdentifier(windowKey);
		ComponentRepository.getInstance().putComponent(panelDefKey, (UIObject)showPanel, true);
		
		handleMask(showPanelGVO, panelDefKey, windowKey, windowId);
		
		showPanel.show();

		handleStyle(showPanelGVO, showPanel, panelDef);
		handleSize(showPanelGVO, showPanel, panel);
		
		if (CENTER_POSITION.equals(position)) {
			showPanel.center();
		}		
    }
    
	private void handleMask(ShowPanelGVO showPanelGVO, String panelDefKey, String windowKey, String windowId) {
		BuiltinHandlerHelper.handleMask(showPanelGVO, panelDefKey, windowKey, windowId);
	}

	private void handleStyle(ShowPanelGVO showPanelGVO, ShowPanelComponent showPanel, UIObject widget) {
		BuiltinHandlerHelper.handleStyle(showPanelGVO, showPanel, widget);
	}
	
	private void handleSize(ShowPanelGVO showPanelGVO, ShowPanelComponent showPanel, UIObject container) {
		BuiltinHandlerHelper.handleSize(showPanelGVO, showPanel, container);
	}
}
