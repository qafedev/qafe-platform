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

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.UIObject;
import com.qualogy.qafe.gwt.client.component.DataChangeHandler;
import com.qualogy.qafe.gwt.client.component.HasDataChangeHandlers;
import com.qualogy.qafe.gwt.client.component.QChoiceHorizontal;
import com.qualogy.qafe.gwt.client.component.QChoiceVertical;
import com.qualogy.qafe.gwt.client.component.QRadioButton;
import com.qualogy.qafe.gwt.client.vo.ui.ChoiceGVO;
import com.qualogy.qafe.gwt.client.vo.ui.ChoiceItemGVO;
import com.qualogy.qafe.gwt.client.vo.ui.ComponentGVO;

public class ChoiceRenderer extends AbstractComponentRenderer {

	public UIObject render(ComponentGVO component, String uuid, String parent, String context) {
		Panel uiObject = null;
		if (component != null) {
			if (component instanceof ChoiceGVO) {
				ChoiceGVO gvo = (ChoiceGVO) component;
				final ComponentGVO finalComponentGVO = component;
				final String finalUuid = uuid;
				final String finalParent = parent;
				if (gvo.getHorizontalOrientation() != null && gvo.getHorizontalOrientation().booleanValue()) {
					if (gvo.getMenu() != null) {
						uiObject = new QChoiceHorizontal() {
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
						uiObject = new QChoiceHorizontal();
					}
				} else {
					if (gvo.getMenu() != null) {
						uiObject = new QChoiceVertical() {
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
						uiObject = new QChoiceVertical();
					}
				}
				RendererHelper.fillIn(component, uiObject, uuid, parent, context);
				
				registerDataChange(gvo, uiObject);
				
				if (uiObject != null) {
					ChoiceItemGVO[] items = gvo.getChoiceItems();
					if (items != null) {
						for (int i = 0; i < items.length; i++) {
							if (items[i] != null) {
								RadioButton radio = new QRadioButton(gvo.getId(), items[i].getDisplayname(), items[i].getValue());
								//radio.setStylePrimaryName(gvo.getStyleClassName());
								uiObject.add(radio);

								RendererHelper.fillIn(items[i], radio, uuid, parent, context);
								RendererHelper.addEvents(component, radio, uuid);
								RendererHelper.addId(component, radio, uuid, parent, context, true);

								// If parent is disabled all its children should be also disabled
								if (gvo.isDisabled()) {	
									radio.setEnabled(false);
								}

								if (items[i].getSelected() != null) {
									if (items[i].getSelected().booleanValue()) {
										radio.setChecked(items[i].getSelected()); // Don't want to fire events from here hence using the setChecked().
										DOM.setElementAttribute(radio.getElement(), "isSelected","true");
									}
								}
							}
						}
					}
				}
				
				RendererHelper.handleRequiredAttribute(component, uiObject);
			}
		}
		return uiObject;
	}
	
	
	private void registerDataChange(final ChoiceGVO gvo, UIObject uiObject) {
		if (uiObject instanceof HasDataChangeHandlers) {
			((HasDataChangeHandlers)uiObject).addDataChangeHandler(new DataChangeHandler() {
				
				public void onDataChange(UIObject uiObject, Object oldValue, Object newValue) {
					doDataChange(gvo, uiObject, oldValue, newValue);					
				}
			});	
		}
	}
	
	private void doDataChange(ChoiceGVO gvo, UIObject uiObject, Object oldValue, Object newValue) {
		RendererHelper.handleRequiredStyle(gvo, uiObject);
	}
}
