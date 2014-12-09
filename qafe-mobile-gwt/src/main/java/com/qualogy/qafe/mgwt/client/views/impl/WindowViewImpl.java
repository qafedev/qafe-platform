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
package com.qualogy.qafe.mgwt.client.views.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.mgwt.ui.client.MGWT;
import com.googlecode.mgwt.ui.client.MGWTStyle;
import com.googlecode.mgwt.ui.client.widget.LayoutPanel;
import com.googlecode.mgwt.ui.client.widget.buttonbar.ButtonBar;
import com.qualogy.qafe.mgwt.client.ui.events.LoadEvent;
import com.qualogy.qafe.mgwt.client.ui.events.LoadHandler;
import com.qualogy.qafe.mgwt.client.ui.events.TimerEvent;
import com.qualogy.qafe.mgwt.client.ui.events.TimerHandler;
import com.qualogy.qafe.mgwt.client.ui.events.UnloadEvent;
import com.qualogy.qafe.mgwt.client.ui.events.UnloadHandler;
import com.qualogy.qafe.mgwt.client.views.WindowView;

public class WindowViewImpl extends AbstractViewImpl implements WindowView {

	private ButtonBar toolbar;
	private Map<String,TimerHandler> timerHandlers = new HashMap<String,TimerHandler>();
	
	public WindowViewImpl() {
		this(true, false);
	}
	
	public WindowViewImpl(boolean showBackButton, boolean subWindow) {
		headerPanel.setLeftWidget(headerBackButton);
		if (MGWT.getOsDetection().isTablet() && !subWindow) {
			headerBackButton.addStyleName(MGWTStyle.getTheme().getMGWTClientBundle().getUtilCss().portraitonly());
		}
		headerBackButton.setVisible(showBackButton || subWindow);
	}
	
	@Override
	public HandlerRegistration addLoadHandler(LoadHandler handler) {
		return addHandler(handler, LoadEvent.getType());
	}
	
	@Override
	public HandlerRegistration addUnloadHandler(UnloadHandler handler) {
		return addHandler(handler, UnloadEvent.getType());
	}
	
	@Override
	public HandlerRegistration addTimerHandler(TimerHandler handler) {
		if (handler == null) {
			return null;
		}
		String key = handler.getKey();
		if (timerHandlers.containsKey(key)) {
			handler.dispose();
			return null;
		}
		timerHandlers.put(key, handler);
		return addHandler(handler, TimerEvent.getType());
	}
	
	@Override
	public void setContent(Widget widget) {
		if (widget instanceof LayoutPanel) {
			mainPanel = (LayoutPanel)widget;
			mainPanel.insert(headerPanel, 0);
			if (toolbar != null) {
				setToolbar(toolbar);
			}
		}
		
		mainPanel.addAttachHandler(new AttachEvent.Handler() {
			@Override
			public void onAttachOrDetach(AttachEvent event) {
				// The mainPanel is the widget which being added and not this view, 
				// so the onAttach and onDetach of the view will not be invoked
				if (event.isAttached()) {
					onAttach();
				} else {
					onDetach();	
				}
			}
		});
	}
	
	@Override
	public void setToolbar(ButtonBar toolbar) {
		this.toolbar = toolbar;
		if (toolbar != null) {
			mainPanel.add(this.toolbar);	
		}
	}
	
	@Override
	protected void onAttach() {
		super.onAttach();
	}
	
	@Override
	protected void onDetach() {
		super.onDetach();
		Iterator<String> itrKey = timerHandlers.keySet().iterator();
		while (itrKey.hasNext()) {
			String key = itrKey.next();
			TimerHandler handler = timerHandlers.get(key);
			handler.dispose();
		}
		timerHandlers.clear();
	}
}