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
package com.qualogy.qafe.gwt.client.ui.renderer;

import com.google.gwt.event.dom.client.LoadEvent;
import com.google.gwt.event.dom.client.LoadHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.UIObject;
import com.qualogy.qafe.gwt.client.component.AreaWidget;
import com.qualogy.qafe.gwt.client.component.MapWidget;
import com.qualogy.qafe.gwt.client.vo.ui.AreaGVO;
import com.qualogy.qafe.gwt.client.vo.ui.ComponentGVO;
import com.qualogy.qafe.gwt.client.vo.ui.MapGVO;

public class MapRenderer extends AbstractComponentRenderer {

	public UIObject render(ComponentGVO component, String uuid, String parent, String context) {
		Panel uiObject = null;
		if (component != null) {
			if (component instanceof MapGVO) {
				MapGVO gvo = (MapGVO) component;

				if (gvo.getMenu() != null) {
					final ComponentGVO finalComponentGVO = component;
					final String finalUuid = uuid;
					final String finalParent = parent;
					uiObject = new FlowPanel() {
						@Override
						public void onBrowserEvent(Event event) {
							if (event.getTypeInt() == Event.ONCONTEXTMENU) {
								DOM.eventPreventDefault(event);
								applyContextMenu(event, finalComponentGVO, finalUuid, finalParent);
							}
							super.onBrowserEvent(event);
						}

						@Override
						protected void setElement(Element elem) {
							super.setElement(elem);
							sinkEvents(Event.ONCONTEXTMENU);
						}
					};
				} else {
					uiObject = new FlowPanel();
				}
				AreaGVO[] areas = gvo.getAreas();
				AreaWidget areasWidget[] = null;
				if (areas != null) {
					areasWidget = new AreaWidget[areas.length];
					for (int i = 0; i < areas.length; i++) {
						if (areas[i] != null) {
							areasWidget[i] = new AreaWidget(areas[i].getShape(), areas[i].getCoords(), areas[i].getAlt(), null);

						}
					}
				}
				final Image image = new Image();
				image.setUrl(gvo.getImageUrl());
				uiObject.add(image);
				final MapWidget map = new MapWidget(areasWidget);
				for (int i = 0; i < areasWidget.length; i++) {
					String tempId = gvo.getId();
					gvo.setId(gvo.getId() + "-" + i);
					RendererHelper.fillIn(gvo, areasWidget[i], uuid, parent, context);
					gvo.setId(tempId);
				}

				// Id is set by the RendererHelper
				RendererHelper.fillIn(component, map, uuid, parent, context);
				map.setName(map.getID());
				image.addLoadHandler(new LoadHandler() {
					public void onLoad(LoadEvent event) {
						map.bindImage(image);

					}
				});
				uiObject.add(map);
			}
		}
		return uiObject;
	}
}
