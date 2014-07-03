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
import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.UIObject;
import com.qualogy.qafe.gwt.client.vo.ui.ComponentGVO;
import com.qualogy.qafe.gwt.client.vo.ui.FrameGVO;

public class FrameRenderer extends AbstractComponentRenderer {

	public UIObject render(ComponentGVO component, String uuid, String parent, String context) {
		Frame uiObject = null;
		if (component != null) {
			if (component instanceof FrameGVO) {
				FrameGVO gvo = (FrameGVO) component;
				if (gvo.getMenu() != null) {
					final ComponentGVO finalComponentGVO = component;
					final String finalUuid = uuid;
					final String finalParent = parent;
					uiObject = new Frame() {
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
					uiObject = new Frame();
				}
				RendererHelper.fillIn(component, uiObject, uuid, parent, context);
				uiObject.setUrl(gvo.getSrc());
			}
		}

		return uiObject;
	}
}
