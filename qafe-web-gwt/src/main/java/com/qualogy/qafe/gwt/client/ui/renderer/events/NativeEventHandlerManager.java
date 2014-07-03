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
package com.qualogy.qafe.gwt.client.ui.renderer.events;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.gwt.mosaic.ui.client.WindowPanel;

import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.UIObject;
import com.qualogy.qafe.gwt.client.component.QWindowPanel;
import com.qualogy.qafe.gwt.client.context.ClientApplicationContext;

public abstract class NativeEventHandlerManager {
	
	private static HandlerRegistration handlerRegistration = null;
	
	private static Map<Integer,Map<UIObject,NativeEventHandler>> eventType2Handler = new HashMap<Integer,Map<UIObject,NativeEventHandler>>();
	
	static {
		registerNativeEvent();
	}

	public static void addNativeEventHandler(UIObject source, NativeEventHandler eventHandler, int eventType) {		
		Map<UIObject,NativeEventHandler> source2EventHandler = eventType2Handler.get(eventType);
		if (source2EventHandler == null) {
			source2EventHandler = new HashMap<UIObject,NativeEventHandler>();
			eventType2Handler.put(eventType, source2EventHandler);
		}
		source2EventHandler.put(source, eventHandler);
	}
	
	private static void registerNativeEvent() {
		if (handlerRegistration == null) {
			handlerRegistration = Event.addNativePreviewHandler(new Event.NativePreviewHandler() {
			    public void onPreviewNativeEvent(Event.NativePreviewEvent event) {
			        fireNativeEvent(event);
			    }
			});
		}
	}
	
	private static void fireNativeEvent(Event.NativePreviewEvent event) {
		int eventType = event.getTypeInt();
		Map<UIObject,NativeEventHandler> source2EventHandler = eventType2Handler.get(eventType);
		if (source2EventHandler != null) {
			UIObject source = resolveSource();
			NativeEventHandler eventHandler = source2EventHandler.get(source);
			if (eventHandler != null) {
				NativeEvent nativeEvent = event.getNativeEvent();
				eventHandler.onNativeEvent(nativeEvent, eventType);
			}
		}
	}
	
	private static UIObject resolveSource() {
		UIObject source = null;
		List<WindowPanel> windows = ClientApplicationContext.getInstance().getWindows();
		if (windows != null) {
			for (WindowPanel window : windows) {
				if (window.isActive()) {
					if (window instanceof QWindowPanel) {
						source = ((QWindowPanel)window).getQRootPanel();
						break;
					}
				}
			}
		}
		return source;
	}
}
