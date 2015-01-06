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
package com.qualogy.qafe.gwt.client.component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;
import com.qualogy.qafe.gwt.client.context.ClientApplicationContext;
import com.qualogy.qafe.gwt.client.ui.renderer.events.NativeEventHandler;
import com.qualogy.qafe.gwt.client.ui.renderer.events.NativeEventHandlerManager;
import com.qualogy.qafe.gwt.client.ui.renderer.events.NativeKeyDownEvent;
import com.qualogy.qafe.gwt.client.ui.renderer.events.NativeKeyDownHandler;
import com.qualogy.qafe.gwt.client.ui.renderer.events.NativeKeyHandler;
import com.qualogy.qafe.gwt.client.ui.renderer.events.NativeKeyUpEvent;
import com.qualogy.qafe.gwt.client.ui.renderer.events.NativeKeyUpHandler;

public class QRootPanel extends AbsolutePanel implements HasNativeKeyHandlers, NativeEventHandler {

	private Widget menuAndToolBar = null;
	private Widget rootPanel = null;
	private Widget messageBox = null;
	private UIObject menuBar = null;
	private UIObject toolbar = null;

	private Map<Integer,List<NativeKeyHandler>> eventType2Handlers = new HashMap<Integer,List<NativeKeyHandler>>();

	public int getMenuAndToolBarMargin(){
		if (menuAndToolBar!=null){
			String value = DOM.getElementAttribute(menuAndToolBar.getElement(), "totalMargin");
			if(value != null && value.length() > 0) {
				try{
					return Integer.parseInt(value);
				} catch (NumberFormatException e){
					ClientApplicationContext.getInstance().log("Exception in QRootPanel: "+ e.getMessage());
					return 0;
				}
			}
		}
		return 0;
	}
	
	public Widget getMenuAndToolBar() {
		return menuAndToolBar;
	}
	
	public void setMenuAndToolBar(Widget menuAndToolBar) {
		this.menuAndToolBar = menuAndToolBar;
	}
	
	public Widget getRootPanel() {
		return rootPanel;
	}
	
	public void setRootPanel(Widget rootPanel) {
		this.rootPanel = rootPanel;
	}
	
	public Widget getMessageBox() {
		return messageBox;
	}
	
	public void setMessageBox(Widget messageBox) {
		this.messageBox = messageBox;
	}
	
	public UIObject getMenuBar() {
		return menuBar;
	}

	public void setMenuBar(UIObject menuBar) {
		this.menuBar = menuBar;
	}

	public UIObject getToolbar() {
		return toolbar;
	}

	public void setToolbar(UIObject toolbar) {
		this.toolbar = toolbar;
	}

	public void onNativeEvent(NativeEvent event, int eventType) {
		List<NativeKeyHandler> nativeKeyHandlers = eventType2Handlers.get(eventType);
		if (nativeKeyHandlers != null) {
			for (NativeKeyHandler handler : nativeKeyHandlers) {
				if (handler instanceof NativeKeyDownHandler) {
					NativeKeyDownHandler keyDownHandler = (NativeKeyDownHandler)handler;
					NativeKeyDownEvent keyDownEvent = new NativeKeyDownEvent(this, event);
					keyDownHandler.onNativeKeyDown(keyDownEvent);
				} else if (handler instanceof NativeKeyUpHandler) {
					NativeKeyUpHandler keyUpHandler = (NativeKeyUpHandler)handler;
					NativeKeyUpEvent keyUpEvent = new NativeKeyUpEvent(this, event);
					keyUpHandler.onNativeKeyUp(keyUpEvent);
				}		
			}
		}
	}

	public void addNativeKeyDownHandler(NativeKeyDownHandler handler) {
		int eventType = Event.ONKEYDOWN;
		NativeEventHandlerManager.addNativeEventHandler(this, this, eventType);
		addNativeKeyHandler(handler, eventType);
	}

	public void addNativeKeyUpHandler(NativeKeyUpHandler handler) {
		int eventType = Event.ONKEYUP;
		NativeEventHandlerManager.addNativeEventHandler(this, this, eventType);
		addNativeKeyHandler(handler, eventType);
	}
	
	private void addNativeKeyHandler(NativeKeyHandler handler, int eventType) {
		List<NativeKeyHandler> nativeKeyHandlers = eventType2Handlers.get(eventType);
		if (nativeKeyHandlers == null) {
			nativeKeyHandlers = new ArrayList<NativeKeyHandler>();
			eventType2Handlers.put(eventType, nativeKeyHandlers);
		}
		nativeKeyHandlers.add(handler);
	}
}
