/**
 * Copyright 2008-2017 Qualogy Solutions B.V.
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

import java.util.List;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.UIObject;
import com.qualogy.qafe.gwt.client.component.QDropDown;
import com.qualogy.qafe.gwt.client.vo.ui.ComponentGVO;
import com.qualogy.qafe.gwt.client.vo.ui.DropDownGVO;
import com.qualogy.qafe.gwt.client.vo.ui.DropDownItemGVO;

public class DropDownRenderer extends AbstractComponentRenderer {

	public final static String EMPTY_ITEM_VALUE				= "emptyItemValue";
	public final static String EMPTY_ITEM_DISPLAYVALUE		= "emptyItemDisplayValue";

    // CHECKSTYLE.OFF: CyclomaticComplexity
	public UIObject render(ComponentGVO component, String uuid, String parent, String context) {
		QDropDown uiObject = null;
		if (component != null) {
			if (component instanceof DropDownGVO) {
				DropDownGVO gvo = (DropDownGVO) component;
				if (gvo.getMenu() != null) {
					final ComponentGVO finalComponentGVO = component;
					final String finalUuid = uuid;
					final String finalParent = parent;
					uiObject = new QDropDown() {
						@Override
						public void onBrowserEvent(Event event) {
							if (event.getTypeInt() == Event.ONCONTEXTMENU) {
								DOM.eventPreventDefault(event);
								applyContextMenu(event, finalComponentGVO,
										finalUuid, finalParent);
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
					uiObject = new QDropDown();
				}
				uiObject.setEditable(Boolean.valueOf(gvo.getEditable()));
				RendererHelper.fillIn(component, uiObject, uuid, parent, context);
				if( gvo.getEmptyItemValue() != null || gvo.getEmptyItemDisplayName() != null ){
					String displayValue=gvo.getEmptyItemDisplayName();
					if (gvo.getEmptyItemMessageKey()!=null){
						displayValue=gvo.getEmptyItemMessageKey();
					}
					if(gvo.getEmptyItemValue() != null) {
						DOM.setElementAttribute(uiObject.getElement(),EMPTY_ITEM_VALUE, gvo.getEmptyItemValue());
					}
					if (displayValue!=null){
						DOM.setElementAttribute(uiObject.getElement(), EMPTY_ITEM_DISPLAYVALUE, displayValue);
					}
					adaptEmptyItem(uiObject);
				}

				DropDownItemGVO[] items = gvo.getDropDownItems();
				if (items != null) {
					for (int i = 0; i < items.length; i++) {
						if (items[i] != null) {
							uiObject.addItem(items[i].getDisplayname(), items[i].getValue());


							if (items[i].getSelected() != null) {
								if (items[i].getSelected().booleanValue()) {
									uiObject.setSelectedIndex(i);
								}
							}
						}
					}
				}
				if (hasEmptyItem(uiObject)) {
                    RendererHelper.handleRequiredAttribute(gvo, uiObject);
				}

				if(gvo.getStyleClass() != null){
					uiObject.setStylePrimaryName(gvo.getStyleClass());
				}/*else {
					uiObject.setStylePrimaryName(gvo.getStyleClassName());
				}*/
			}
		}
		return uiObject;
	}
    // CHECKSTYLE.OFF: CyclomaticComplexity

	public static boolean hasEmptyItem(ListBox component) {
		if (component != null) {
			String emptyItemLabel = null;
			String emptyItemValue = null;
			if (hasAttribute(component, EMPTY_ITEM_DISPLAYVALUE)) {
				emptyItemLabel = DOM.getElementAttribute(component.getElement(), EMPTY_ITEM_DISPLAYVALUE);
			}
			if (hasAttribute(component, EMPTY_ITEM_VALUE)) {
				emptyItemValue = DOM.getElementAttribute(component.getElement(), EMPTY_ITEM_VALUE);
			}
			if ((emptyItemLabel != null) || (emptyItemValue != null)) {
				return true;
			}
		}
		return false;
	}

	public static void adaptEmptyItem(ListBox component) {
		if (hasEmptyItem(component)){
			String emptyItemLabel = DOM.getElementAttribute(component.getElement(), EMPTY_ITEM_DISPLAYVALUE);
			String emptyItemValue = DOM.getElementAttribute(component.getElement(), EMPTY_ITEM_VALUE);
			component.addItem(emptyItemLabel, emptyItemValue);
		}
	}

	public static void adaptItems(ListBox component, List items, boolean cleanup) {
		if ((component != null) && (items != null)) {
			if (cleanup) {
				component.clear();
				adaptEmptyItem(component);
			}
			for (int i=0; i<items.size(); i++) {
				Object item = items.get(i);
				adaptItem(component, item);
			}
		}
	}

	public static void adaptItem(ListBox component, Object item) {
		if ((component != null) && (item != null)) {
			component.addItem(item.toString());
		}
	}

	public static int getIndexOfValue(ListBox component, Object value) {
		int indexOfValue = -1;
		if ((component != null) && (value != null)) {
			for (int i=0; i<component.getItemCount(); i++) {
				if (component.getItemText(i).equals(value) || component.getValue(i).equals(value)) {
					indexOfValue = i;
					break;
				}
			}
		}
		return indexOfValue;
	}

	private static boolean hasAttribute(ListBox component, String attributeName) {
		if ((component != null) && (attributeName != null)) {
			return component.toString().toLowerCase().contains(attributeName.toLowerCase());
		}
		return false;
	}
}
