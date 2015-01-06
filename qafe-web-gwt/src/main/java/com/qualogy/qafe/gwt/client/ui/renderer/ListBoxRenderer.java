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
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.UIObject;
import com.qualogy.qafe.gwt.client.vo.ui.ComponentGVO;
import com.qualogy.qafe.gwt.client.vo.ui.DropDownItemGVO;
import com.qualogy.qafe.gwt.client.vo.ui.ListBoxGVO;

public class ListBoxRenderer extends AbstractComponentRenderer {

	public UIObject render(ComponentGVO component, String uuid, String parent, String context) {
		ListBox uiObject = null;
		if (component != null) {
			if (component instanceof ListBoxGVO) {
				ListBoxGVO gvo = (ListBoxGVO) component;
				if (gvo.getMenu() != null) {
					final ComponentGVO finalComponentGVO = component;
					final String finalUuid = uuid;
					final String finalParent = parent;
					uiObject = new ListBox() {
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
					uiObject = new ListBox();
				}
				RendererHelper.fillIn(component, uiObject, uuid, parent, context);
				if (gvo.getNrOfVisibleItems() != null) {
					uiObject.setVisibleItemCount(gvo.getNrOfVisibleItems());
				} else {
					uiObject.setVisibleItemCount(gvo.getDropDownItems() == null ? 1 : gvo.getDropDownItems().length);
				}
				uiObject.setMultipleSelect(gvo.getMultipleSelect());
				DropDownItemGVO[] items = gvo.getDropDownItems();
				if (items != null) {
					for (int i = 0; i < items.length; i++) {
						if (items[i] != null) {
							uiObject.addItem(items[i].getDisplayname(), items[i].getValue());
							if (items[i].getSelected() != null) {
								if (items[i].getSelected().booleanValue()) {
									uiObject.setItemSelected(i, true);
									//uiObject.setSelectedIndex(i);
								}
							}
						}
					}
				}
				//uiObject.setStylePrimaryName(gvo.getStyleClassName());
			}
		}
		return uiObject;
	}
}
