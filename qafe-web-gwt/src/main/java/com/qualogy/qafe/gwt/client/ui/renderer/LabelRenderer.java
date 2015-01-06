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
import com.google.gwt.user.client.ui.UIObject;
import com.qualogy.qafe.gwt.client.component.QLabel;
import com.qualogy.qafe.gwt.client.vo.ui.ComponentGVO;
import com.qualogy.qafe.gwt.client.vo.ui.LabelGVO;

public class LabelRenderer extends AbstractComponentRenderer {

	public UIObject render(ComponentGVO component, String uuid, String parent, String context) {
		QLabel uiObject = null;
		if (component != null) {
			if (component instanceof LabelGVO) {
				LabelGVO gvo = (LabelGVO) component;
				if (gvo.getMenu() != null) {
					final ComponentGVO finalComponentGVO = component;
					final String finalUuid = uuid;
					final String finalParent = parent;
					uiObject = new QLabel(gvo.getDisplayname()) {
						@Override
						public void onBrowserEvent(Event event) {
							if (isEnabled()){
								if (event.getTypeInt() == Event.ONCONTEXTMENU) {
									DOM.eventPreventDefault(event);
									applyContextMenu(event, finalComponentGVO,
											finalUuid, finalParent);
								}
								super.onBrowserEvent(event);
							}
						}

					};
				} else {
					uiObject = new QLabel(gvo.getDisplayname());
				}
				uiObject.setEnabled(!gvo.isDisabled());
				//uiObject.setStylePrimaryName(gvo.getStyleClassName());
				RendererHelper.fillIn(component, uiObject, uuid, parent, context);
			}
		}
		return uiObject;
	}
}
