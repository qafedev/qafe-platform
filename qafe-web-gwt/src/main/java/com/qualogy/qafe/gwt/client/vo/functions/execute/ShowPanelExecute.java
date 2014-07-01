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
package com.qualogy.qafe.gwt.client.vo.functions.execute;

import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.widgetideas.table.client.overrides.FlexTable;
import com.qualogy.qafe.gwt.client.component.ShowPanelComponent;
import com.qualogy.qafe.gwt.client.context.ClientApplicationContext;
import com.qualogy.qafe.gwt.client.ui.renderer.AnyComponentRenderer;
import com.qualogy.qafe.gwt.client.ui.renderer.RendererHelper;
import com.qualogy.qafe.gwt.client.util.ComponentRepository;
import com.qualogy.qafe.gwt.client.vo.functions.BuiltInFunctionGVO;
import com.qualogy.qafe.gwt.client.vo.functions.ShowPanelGVO;

public class ShowPanelExecute implements ExecuteCommand {

	private static final Object CENTER_POSITION = "center";

	@SuppressWarnings("deprecation")
	public void execute(BuiltInFunctionGVO builtInFunction) {
		if (builtInFunction instanceof ShowPanelGVO) {
			ShowPanelGVO showPanelGVO = (ShowPanelGVO) builtInFunction;
			String id = "showPanel_" + showPanelGVO.getBuiltInComponentGVO().getComponentIdUUID();
			final ShowPanelComponent showPanel = new ShowPanelComponent(showPanelGVO.isAutoHide(), showPanelGVO.isModal(), showPanelGVO.getLeft(), showPanelGVO.getTop());
			
			// We have to make sure that all other showPanels using the same panel-definition is cleared
			List<UIObject> uiObjects = ComponentRepository.getInstance().getComponent(id);
			if(uiObjects != null){
				UIObject uiObject = uiObjects.iterator().next();
				if (uiObject instanceof ShowPanelComponent) {
					ShowPanelComponent showPanelComponent = (ShowPanelComponent)uiObject;
					showPanelComponent.hide();// This will call showPanelComponent.onDetach()
				}
			}
			
			UIObject ui = AnyComponentRenderer.getInstance().render(showPanelGVO.getSrc(), showPanelGVO.getUuid(), showPanelGVO.getBuiltInComponentGVO().getWindowId(), showPanelGVO.getSrc().getContext());
			Widget w = null;
			
			if (ui instanceof Widget) {
				w = (Widget) ui;
			}
			
			FlexTable panel = new FlexTable();
			if (!showPanelGVO.isAutoHide()) {
				Label close = new Label("X");
				close.setStyleName("qafe_close_on_showPanel");
				close.addClickHandler(new ClickHandler() {
					public void onClick(ClickEvent event) {
						showPanel.hide();
					}
				});
				panel.setWidget(0, 1, close);	
			}
			panel.setWidget(1, 0, w);
			showPanel.setWidget(panel);
			
			showPanel.setId(id);
			showPanel.setWindow(showPanelGVO.getBuiltInComponentGVO().getWindowId());
			String componentUUID= showPanelGVO.getBuiltInComponentGVO().getComponentIdUUID();
			String windowIdentifier =  componentUUID.substring(componentUUID.indexOf('|'));
			windowIdentifier = showPanelGVO.getBuiltInComponentGVO().getWindowId() + windowIdentifier;
			showPanel.setWindowIdentifier(windowIdentifier);
			// Adding to component repository to be picked up when executing close-panel.				
			ComponentRepository.getInstance().putComponent(id, (UIObject)showPanel);
			
			handleMask(showPanelGVO, id, windowIdentifier);
			
			showPanel.show();

			handleStyle(showPanelGVO, showPanel, w);
			handleSize(showPanelGVO, showPanel, panel);
			
			String position = showPanelGVO.getPosition();
			if (CENTER_POSITION.equals(position)) {
				showPanel.center();
			}
		}
	}

	private void handleMask(ShowPanelGVO showPanelGVO, String id, String windowIdentifier) {
		if(showPanelGVO.isModal()) {
			List<String> panelDefsOpened = ClientApplicationContext.getInstance().getPanelDefinitionsOpened(showPanelGVO.getBuiltInComponentGVO().getWindowId());
			if(panelDefsOpened != null && panelDefsOpened.size() > 0) {
				//add mask on the panel from which showpanel is called .
				String lastPanelDefOpened = panelDefsOpened.get(panelDefsOpened.size() - 1);
				SetMaskHelper.setMask(lastPanelDefOpened, RendererHelper.QAFE_GLASS_PANEL_STYLE, true);
			} else {
				// if the show-panel is called for first time then do mask on the window.
				SetMaskHelper.setMask(windowIdentifier, RendererHelper.QAFE_GLASS_PANEL_STYLE, true);
			}
			ClientApplicationContext.getInstance().addPanelDefinitionsOpened(showPanelGVO.getBuiltInComponentGVO().getWindowId(), id);
		}
	}

	private void handleStyle(ShowPanelGVO showPanelGVO, ShowPanelComponent showPanel, UIObject widget) {
		String styleClass = showPanelGVO.getSrc().getStyleClass();
		if (styleClass != null) {
			// The popup panel inherits the styleClass of the panel-definition
			// to avoid a white area on the right and bottom
			showPanel.addStyleName(styleClass);
			widget.removeStyleName(styleClass);
		}
	}
	
	private void handleSize(ShowPanelGVO showPanelGVO, ShowPanelComponent showPanel, UIObject container) {
		// Obtained these values by measuring
		int intVScrollbarWidth = 16;
		int intHScrollbarHeight = 16;
		int offsetWidth = 34;
		int offsetHeight = 38;
					
		
		String height = showPanelGVO.getSrc().getHeight();
		if (height != null) {
			// The height of the panel-definition inside the container,
			// so the height of the popup panel should be bigger to avoid unnecessary scrollbar  
			int intHeight = Integer.valueOf(height);
			height = String.valueOf(intHeight + intHScrollbarHeight + offsetHeight);
			showPanel.setHeight(height);	
		}
		
		String width = showPanelGVO.getSrc().getWidth();
		if (width != null) {
			// The width of the panel-definition inside the container,
			// so the width of the popup panel should be bigger to avoid unnecessary scrollbar
			int intWidth = Integer.valueOf(width);
			width = String.valueOf(intWidth + intVScrollbarWidth + offsetWidth);
			showPanel.setWidth(width);
		}
	}
}