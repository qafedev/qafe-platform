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

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.UIObject;
import com.qualogy.qafe.gwt.client.component.QCheckBox;
import com.qualogy.qafe.gwt.client.vo.ui.CheckBoxGVO;
import com.qualogy.qafe.gwt.client.vo.ui.ComponentGVO;

public class CheckBoxRenderer extends AbstractComponentRenderer {

	public UIObject render(ComponentGVO component, String uuid, String parent, String context) {
		QCheckBox uiObject = null;
		if (component != null) {
			if (component instanceof CheckBoxGVO) {
				CheckBoxGVO gvo = (CheckBoxGVO) component;
				final ComponentGVO finalComponentGVO = component;
				final String finalUuid = uuid;
				final String finalParent = parent;
				if (gvo.getMenu() != null) {
					uiObject = new QCheckBox(DOM.createInputCheck(), gvo.getDisplaynamePosition()) {
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
					uiObject = new QCheckBox(DOM.createInputCheck(), gvo.getDisplaynamePosition());
				}
				RendererHelper.fillIn(component, uiObject, uuid, parent, context);
				uiObject.setText(gvo.getDisplayname());
				uiObject.setEditable(Boolean.valueOf(gvo.getEditable()));
				if (gvo.getValue() != null) {
					uiObject.setChecked(gvo.getValue().booleanValue());
				}
				setValueForDomain(uiObject, gvo.getUnCheckedValue(), CheckBoxGVO.UNCHECKED_VALUE_ATTRIBUTE_TAG);
				setValueForDomain(uiObject, gvo.getCheckedValue(), CheckBoxGVO.CHECKED_VALUE_ATTRIBUTE_TAG);
				setValueForDomain(uiObject, createStringForDomain(gvo.getCheckedValueDomain()), CheckBoxGVO.CHECKED_VALUE_DOMAIN_ATTRIBUTE_TAG);
				setValueForDomain(uiObject, createStringForDomain(gvo.getUnCheckedValueDomain()), CheckBoxGVO.UNCHECKED_VALUE_DOMAIN_ATTRIBUTE_TAG);
			}
		}
		return uiObject;
	}

	private String createStringForDomain(String[] checkedValueDomain) {
		String value = null;
		if (checkedValueDomain != null) {
			value = "";
			for (int i = 0; i < checkedValueDomain.length; i++) {
				value += checkedValueDomain[i];
				if (i < (checkedValueDomain.length - 1)) {
					value += ",";
				}
			}
		}
		return value;
	}

	private void setValueForDomain(CheckBox uiObject, String value, String attribute) {
		if (value != null) {
			DOM.setElementAttribute(uiObject.getElement(), attribute, value);
		}
	}
}
