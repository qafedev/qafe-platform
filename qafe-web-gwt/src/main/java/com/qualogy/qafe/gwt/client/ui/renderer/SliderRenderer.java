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
import com.qualogy.qafe.gwt.client.component.QSliderBar;
import com.qualogy.qafe.gwt.client.vo.ui.ComponentGVO;
import com.qualogy.qafe.gwt.client.vo.ui.SliderGVO;

public class SliderRenderer extends AbstractComponentRenderer {

	public UIObject render(ComponentGVO component, String uuid, String parent, String context) {
		QSliderBar uiObject = null;
		if (component != null) {
			if (component instanceof SliderGVO) {
				final ComponentGVO finalComponentGVO = component;
				final String finalUuid = uuid;
				final String finalParent = parent;
				SliderGVO gvo = (SliderGVO) component;
				if ("HORIZONTAL".equalsIgnoreCase(gvo.getOrientation())) {
					if (gvo.getMenu() != null) {
						uiObject = new QSliderBar(gvo.getMinTicks().doubleValue(), gvo.getMaxTicks().doubleValue()) {
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
						uiObject = new QSliderBar(gvo.getMinTicks().doubleValue(), gvo.getMaxTicks().doubleValue());
					}
				} else if ("VERTICAL".equalsIgnoreCase(gvo.getOrientation())) {
					if (gvo.getMenu() != null) {
						uiObject = new QSliderBar(gvo.getMinTicks().doubleValue(), gvo.getMaxTicks().doubleValue()) {
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
						uiObject = new QSliderBar(gvo.getMinTicks().doubleValue(), gvo.getMaxTicks().doubleValue());
					}
				}
				if (uiObject != null) {
					RendererHelper.fillIn(component, uiObject, uuid, parent, context);
					uiObject.setStepSize(gvo.getTickSize());
					uiObject.setTickLabels(gvo.getTickLabels());
					uiObject.setValue(gvo.getValue());
					uiObject.redraw();
				}
			}
		}
		return uiObject;
	}
}
