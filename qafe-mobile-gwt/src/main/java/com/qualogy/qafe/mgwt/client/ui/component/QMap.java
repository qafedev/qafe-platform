/**
 * Copyright 2008-2016 Qualogy Solutions B.V.
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
package com.qualogy.qafe.mgwt.client.ui.component;

import com.google.gwt.event.dom.client.LoadEvent;
import com.google.gwt.event.dom.client.LoadHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.mgwt.dom.client.event.tap.HasTapHandlers;
import com.googlecode.mgwt.dom.client.event.tap.TapEvent;
import com.googlecode.mgwt.dom.client.event.tap.TapHandler;
import com.googlecode.mgwt.dom.client.recognizer.longtap.HasLongTapHandlers;
import com.googlecode.mgwt.dom.client.recognizer.longtap.LongTapEvent;
import com.googlecode.mgwt.dom.client.recognizer.longtap.LongTapHandler;
import com.qualogy.qafe.mgwt.client.ui.events.DataChangeHandler;

public class QMap extends FlowPanel implements HasData, HasTapHandlers, HasLongTapHandlers {

	private static final String MAP 		= "map";
	private static final String MAP_NAME 	= "name";
	
	public class MapWidget extends Widget {
		
		private String name;
		private QMapArea[] areas;
		
		public MapWidget() {
			setElement(DOM.createElement(MAP));
			sinkEvents(Event.ONCLICK);
		}

		public QMapArea[] getAreas() {
			return areas;
		}
		
		public void setAreas(QMapArea[] areas) {
			this.areas = areas;
			if (areas == null) {
				return;
			}
			for (int i=0; i<areas.length; i++) {
				QMapArea area = areas[i];
				DOM.appendChild(getElement(), area.getElement());
			}
		}
		
		public String getName() {
			return name;
		}
		
		public void setName(String name) {
			this.name = name;
			if (name == null) {
				name = "";
			}
			DOM.setElementAttribute(getElement(), MAP_NAME, name);
		}
		
		public void bindImage(Image image) {
			usemap(image.getElement(), getName());
		}
		
		@Override
		public void onBrowserEvent(Event event) {
			int eventType = DOM.eventGetType(event); 
			switch (eventType) {
				case Event.ONCLICK: {
					if (areas != null) {
						Element targetElement = DOM.eventGetTarget(event);
						for (int i=0; i<areas.length; i++) {
							QMapArea area = areas[i];
							if (area.getElement().equals(targetElement)) {
								QMap.this.fireAreaSelect(area, event.getClientX(), event.getClientY());
							}
						}
					}
					DOM.eventPreventDefault(event);
					return;
				}
			}
			super.onBrowserEvent(event);
		}
		
		private native void usemap(Element element, String name)/*-{
			element.useMap = "#" + name;
		}-*/;
	}
	
	private MapWidget mapWidget;
	private QMapArea selectedArea = null;
	private String dataName;

	public QMap(String imageURL) {
		mapWidget = new MapWidget();
		super.add(mapWidget);
		final Image image = new Image(imageURL);
		image.addLoadHandler(new LoadHandler() {
			@Override
			public void onLoad(LoadEvent event) {
				mapWidget.bindImage(image);
			}
		});
		super.add(image);
	}

	public void setAreas(QMapArea[] areas) {
		mapWidget.setAreas(areas);
	}
	
	public void setName(String name) {
		mapWidget.setName(name);
	}
	
	@Override
	public HandlerRegistration addTapHandler(TapHandler handler) {
		return addHandler(handler, TapEvent.getType());
	}

	@Override
	public HandlerRegistration addLongTapHandler(LongTapHandler handler) {
		return addHandler(handler, LongTapEvent.getType());
	}
	
	@Override
	public HandlerRegistration addDataChangeHandler(DataChangeHandler handler) {
		return ComponentHelper.addDataChangeHandler(this, handler);
	}
	
	@Override
	public void setDataName(String dataName) {
		this.dataName = dataName;
	}

	@Override
	public String getDataName() {
		return dataName;
	}

	@Override
	public void setData(Object data) {
		// TODO Auto-generated method stub
	}

	@Override
	public Object getData() {
		if (selectedArea != null) {
			return selectedArea.getAlt();
		}
		return null;
	}

	@Override
	public Object getDataModel() {
		return getData();
	}

	@Override
	public Object getModel() {
		return getData();
	}
	
	protected void fireAreaSelect(QMapArea selectedArea, int clientX, int clientY) {
		this.selectedArea = selectedArea;
		TapEvent event = new TapEvent(this, clientX, clientY);
		fireEvent(event);
	}
}