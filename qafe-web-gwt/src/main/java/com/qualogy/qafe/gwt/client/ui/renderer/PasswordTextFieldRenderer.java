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
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.UIObject;
import com.qualogy.qafe.gwt.client.component.HasEditable;
import com.qualogy.qafe.gwt.client.component.LabeledPasswordFieldWidget;
import com.qualogy.qafe.gwt.client.component.QPasswordTextBox;
import com.qualogy.qafe.gwt.client.vo.ui.ComponentGVO;
import com.qualogy.qafe.gwt.client.vo.ui.PasswordTextFieldGVO;

public class PasswordTextFieldRenderer extends AbstractComponentRenderer {

	public UIObject render(ComponentGVO component, String uuid, String parent, String context) {
		UIObject uiObject = null;
		if (component != null) {
			if (component instanceof PasswordTextFieldGVO) {
				PasswordTextFieldGVO gvo = (PasswordTextFieldGVO) component;
				final ComponentGVO finalComponentGVO = component;
				final String finalUuid = uuid;
				final String finalParent = parent;
				if (gvo.getDisplayname() != null && gvo.getDisplayname().length() > 0) {
					if (gvo.getMenu() != null) {
						uiObject = new LabeledPasswordFieldWidget(gvo.getDisplayname(), gvo.getOrientation()) {
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
						uiObject = new LabeledPasswordFieldWidget(gvo.getDisplayname(), gvo.getOrientation());
					}
				} else {
					if (gvo.getMenu() != null) {
						uiObject = new QPasswordTextBox() {
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
						uiObject = new QPasswordTextBox();
					}
				}
				RendererHelper.fillIn(component, uiObject, uuid, parent, context);
				if (uiObject instanceof HasText) {
					((HasText) uiObject).setText(gvo.getValue());
				}
				if (uiObject instanceof HasEditable) {
					((HasEditable) uiObject).setEditable(gvo.getEditable().booleanValue());
				}
								
				RendererHelper.handleRequiredAttribute(gvo, uiObject);
				//uiObject.setStylePrimaryName(gvo.getStyleClassName());
			}
		}
		return uiObject;
	}
}