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
package com.qualogy.qafe.gwt.client.component;

import java.util.List;

import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;
import com.qualogy.qafe.gwt.client.context.ClientApplicationContext;
import com.qualogy.qafe.gwt.client.ui.renderer.RendererHelper;
import com.qualogy.qafe.gwt.client.util.ComponentRepository;
import com.qualogy.qafe.gwt.client.util.QAMLUtil;
import com.qualogy.qafe.gwt.client.vo.functions.execute.SetMaskHelper;

public class ShowPanelComponent extends PopupPanel implements ResizeHandler {
	
	private String id;
	private String window;
	private String windowIdentifier;
	private HandlerRegistration resizeRegistration;
	
	public ShowPanelComponent(boolean autoHide, boolean modal, int left, int top) {
		super(autoHide, modal);
		setPopupPosition(left, top);
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
		DOM.setElementAttribute(this.getElement(), "id", id);
	}
	
	@Override
	public void setWidth(String width) {
		DOM.setStyleAttribute(getElement(), "width", QAMLUtil.unitPX(width));
	}
	
	@Override
	public void setHeight(String height) {
		DOM.setStyleAttribute(getElement(), "height", QAMLUtil.unitPX(height));
	}
	
	@Override
	public void show() {
		super.show();
		DOM.setStyleAttribute(getElement(), "overflow", "auto");
	}

	@Override
	public void center() {
		super.center();
		if(resizeRegistration == null) {
			resizeRegistration =  Window.addResizeHandler(this);
		}
	}
	
	@Override
	public void onDetach(){		
		if(resizeRegistration != null) {
			resizeRegistration.removeHandler();
		}
		
		// This is to clear all existing components from repository. showPanel.hide() will implicitly call this method.
		List<UIObject> uiObjects = ComponentRepository.getInstance().getComponent(this.getId());
		if(uiObjects != null){
			UIObject uiObject = uiObjects.iterator().next();
			if (uiObject instanceof ShowPanelComponent) {
				ShowPanelComponent showPanel = (ShowPanelComponent)uiObject;				
				Widget innerComponent = showPanel.getWidget();
				if (innerComponent != null) {
					ComponentRepository.getInstance().clearContainerComponent(innerComponent);
				}				
			}
		}
		
		ComponentRepository.getInstance().remove(this.getId());
		
		removeMaskForActivePanel();
		super.onDetach();
	}

	private void removeMaskForActivePanel() {
		ClientApplicationContext.getInstance().removePanelDefinitionsOpened(getWindow(), this.getId());
		List<String> panelDefsOpened = ClientApplicationContext.getInstance().getPanelDefinitionsOpened(getWindow());
		if(panelDefsOpened != null && panelDefsOpened.size() > 0) {
			// Remove the mask on the panel on top.
			String lastPanelDefOpened = panelDefsOpened.get(panelDefsOpened.size() - 1);
			SetMaskHelper.setMask(lastPanelDefOpened, RendererHelper.QAFE_GLASS_PANEL_STYLE, false);
		} else if(panelDefsOpened != null && panelDefsOpened.size() == 0) {
			//This means there is no panel definitions opened using show-panel in this window. Then remove the mask on the window.
			SetMaskHelper.setMask(getWindowIdentifier(), RendererHelper.QAFE_GLASS_PANEL_STYLE, false);
		}
	}

	public void setWindow(String windowId) {
		this.window = windowId;		
	}
	
	public String getWindow() {
		return window;
	}

	public String getWindowIdentifier() {
		return windowIdentifier;
	}

	public void setWindowIdentifier(String windowIdentifier) {
		this.windowIdentifier = windowIdentifier;
	}

	public void onResize(ResizeEvent event) {				
		if(isAttached()) {
			center();
		}		
	}
	
}