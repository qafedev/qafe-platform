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
package com.qualogy.qafe.gwt.client.ui.renderer;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;
import com.qualogy.qafe.gwt.client.component.Tiles;
import com.qualogy.qafe.gwt.client.vo.ui.ComponentGVO;
import com.qualogy.qafe.gwt.client.vo.ui.TileListGVO;

public class TileListRenderer extends AbstractComponentRenderer {

	public UIObject render(ComponentGVO component, String uuid, String parent, String context) {
		Tiles uiObject = null;
		if (component != null) {
			if (component instanceof TileListGVO) {
				TileListGVO gvo = (TileListGVO) component;
				if (gvo.getMenu() != null) {
					final ComponentGVO finalComponentGVO = component;
					final String finalUuid = uuid;
					final String finalParent = parent;
					uiObject = new Tiles() {
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
					uiObject = new Tiles();
				}
				uiObject.setColCount(gvo.getColumns());
				uiObject.setTitle(component.getTooltip());
				uiObject.setWidth("100%");
				uiObject.setHeight("100%");
				gvo.getComponent().setUuid(uuid);
				uiObject.setInnerComponent(gvo.getComponent());
				assignParent(uiObject, gvo.getId());
				// in fillIn we are adding events which is creating duplicate events in this case.
				//(events are added for inner component seperately in their renderer.) 
				//RendererHelper.fillIn(component, uiObject, uuid, parent, context);
				
				RendererHelper.addDimension(component, uiObject, uuid, parent);
				RendererHelper.addId(component, uiObject, uuid, parent, context, true);
				RendererHelper.addUUID(component, uiObject, uuid);
				RendererHelper.addWindowID(component, uiObject, uuid);
				RendererHelper.addDisabledInfo(component, uiObject);
				RendererHelper.addStyle(component, uiObject);
				RendererHelper.addVisibleInfo(component, uiObject);
				RendererHelper.addTooltip(component, uiObject);
			}
		}		
		return uiObject;
	}

	private void assignParent(UIObject uiObject, String parent) {
		DOM.setElementAttribute(uiObject.getElement(), "pc", parent);
		if(uiObject instanceof HasWidgets){
			HasWidgets hasWidgets = (HasWidgets) uiObject;
			for (Widget widget : hasWidgets) {
				assignParent(widget, parent);
			}
		}
	}
}
