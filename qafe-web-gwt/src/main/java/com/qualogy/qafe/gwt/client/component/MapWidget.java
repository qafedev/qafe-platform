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

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;

public class MapWidget extends Widget implements  HasClickHandlers {
	private AreaWidget[] items;

	public MapWidget() {
		super();
		setElement(DOM.createElement("map"));
		sinkEvents(Event.ONCLICK);
	}

	public MapWidget(AreaWidget[] areas) {
		this();
		items = areas;
		if (items != null && items.length > 0) {
			for (int i = 0; i < items.length; i++) {
				DOM.appendChild(getElement(), items[i].getElement());
			}
		}
	}

	public void bindImage(Image image) {
		usemap(image.getElement(), getName());
	}

	native void usemap(Element element, String name)/*-{
	 element.useMap = "#"+name;
	 }-*/;

	public String getID() {
		return DOM.getElementAttribute(getElement(), "id");
	}

	public String getName() {
		return DOM.getElementAttribute(getElement(), "name");
	}

	/* @Override */
	public void onBrowserEvent(Event event) {
		switch (DOM.eventGetType(event)) {
		case Event.ONCLICK:
			if (items != null && items.length > 0) {
				Element target = DOM.eventGetTarget(event);
				for (int i = 0; i < items.length; i++) {
					if (items[i].getElement()!=null){
						//	if (DOM.compare(target, items[i].getElement())) {
						if (target.getId()!=null && target.getId().equals(items[i].getElement().getId())) {
							Command command = items[i].getCommand();
							if (command != null) {
								DeferredCommand.addCommand(command);
							}
						}
					}
				}
				DOM.eventPreventDefault(event);
				return;
			}
		}
		super.onBrowserEvent(event);
	}

	public void setID(String id) {
		DOM.setElementAttribute(getElement(), "id", (id == null) ? "" : id);
	}

	public void setName(String name) {
		DOM.setElementAttribute(getElement(), "name", (name == null) ? ""
				: name);
	}



	public AreaWidget[] getItems() {
		return items;
	}

	public void setItems(AreaWidget[] items) {
		this.items = items;
	}

	public HandlerRegistration addClickHandler(ClickHandler handler) {
		
		return addDomHandler(handler,  ClickEvent.getType());
	}


}