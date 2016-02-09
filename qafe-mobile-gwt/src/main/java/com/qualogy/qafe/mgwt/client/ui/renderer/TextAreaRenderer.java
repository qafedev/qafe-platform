/**
 * Copyright 2008-2016 Qualogy Solutions B.V.
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
package com.qualogy.qafe.mgwt.client.ui.renderer;

import com.google.gwt.user.client.ui.UIObject;
import com.qualogy.qafe.mgwt.client.activities.AbstractActivity;
import com.qualogy.qafe.mgwt.client.ui.component.QTextArea;
import com.qualogy.qafe.mgwt.client.vo.ui.ComponentGVO;
import com.qualogy.qafe.mgwt.client.vo.ui.TextAreaGVO;

public class TextAreaRenderer extends AnyTextFieldRenderer {

	public UIObject render(ComponentGVO component, String owner, String uuid, String parent, String context, AbstractActivity activity) {
		UIObject widget = null;
		if (component instanceof TextAreaGVO) {
			TextAreaGVO textAreaGVO = (TextAreaGVO)component;
			QTextArea textArea = new QTextArea(textAreaGVO.getDisplayname());
			init(textAreaGVO, textArea, owner, uuid, parent, context, activity);
			widget = textArea;
		}
		registerComponent(component, widget, owner, parent, context);
		return widget;
	}
	
//	public UIObject render(ComponentGVO component, String uuid, String parent, String context) {
//		Widget uiObject = null;
//		if (component != null) {
//			if (component instanceof TextAreaGVO) {
//				final ComponentGVO finalComponentGVO = component;
//				final String finalUuid = uuid;
//				final String finalParent = parent;
//				TextAreaGVO gvo = (TextAreaGVO) component;
//				if (gvo.getDisplayname() != null || gvo.getDisplayname().length() > 0) {
////					if (gvo.getMenu() != null) {
////						uiObject = new LabeledTextAreaFieldWidget(gvo.getDisplayname(), gvo.getOrientation(), gvo, uuid, parent) {
////							@Override
////							public void onBrowserEvent(Event event) {
////								if (event.getTypeInt() == Event.ONCONTEXTMENU) {
////									DOM.eventPreventDefault(event);
////									applyContextMenu(event, finalComponentGVO, finalUuid, finalParent);
////								}
////								super.onBrowserEvent(event);
////							}
////
////							@Override
////							protected void setElement(Element elem) {
////								super.setElement(elem);
////								sinkEvents(Event.ONCONTEXTMENU);
////							}
////						};
////					} else {
////						uiObject = new LabeledTextAreaFieldWidget(gvo.getDisplayname(), gvo.getOrientation(), gvo, uuid, parent);
////					}
//				} else {
//					if (gvo.getRich() != null) {
//						if (gvo.getRich().booleanValue()) {
//							// This sample is taken from the KitchenSick demo
//							RichTextArea area = new RichTextArea();
//						//	RichTextToolbar tb = new RichTextToolbar(area);
//
//							VerticalPanel p = null;
//							if (gvo.getMenu() != null) {
//								p = new VerticalPanel() {
//									@Override
//									public void onBrowserEvent(Event event) {
//										if (event.getTypeInt() == Event.ONCONTEXTMENU) {
//											DOM.eventPreventDefault(event);
//											applyContextMenu(event, finalComponentGVO, finalUuid, finalParent);
//										}
//										super.onBrowserEvent(event);
//									}
//
//									@Override
//									protected void setElement(Element elem) {
//										super.setElement(elem);
//										sinkEvents(Event.ONCONTEXTMENU);
//									}
//								};
//							} else {
//								p = new VerticalPanel();
//							}
//						//	p.add(tb);
//							p.add(area);
//							area.setHeight("14em");
//							area.setWidth("100%");
//					//		tb.setWidth("100%");
//							p.setWidth("100%");
//						    //p.setStyleName("qafe_rich_textarea");
//							DOM.setStyleAttribute(p.getElement(), "marginRight", "4px");
//
//							RendererHelper.fillIn(component, area, uuid, parent, context);
//							RendererHelper.fillIn(component, p, uuid, parent, context);
//							area.setText(gvo.getValue());
//							area.setEnabled(gvo.getEditable().booleanValue());
//
//							uiObject = p;
//							if (gvo.getRequired() != null && gvo.getRequired().booleanValue()) {
//								DOM.setElementProperty(uiObject.getElement(), "required", "true");
//								RendererHelper.setStyleForElement(uiObject.getElement(), "background", "red");
//
//							}
//						} else {
//							if (gvo.getMenu() != null) {
//								uiObject = new TextArea() {
//									@Override
//									public void onBrowserEvent(Event event) {
//										if (event.getTypeInt() == Event.ONCONTEXTMENU) {
//											DOM.eventPreventDefault(event);
//											applyContextMenu(event, finalComponentGVO, finalUuid, finalParent);
//										}
//										super.onBrowserEvent(event);
//									}
//
//									@Override
//									protected void setElement(Element elem) {
//										super.setElement(elem);
//										sinkEvents(Event.ONCONTEXTMENU);
//									}
//								};
//							} else {
//								uiObject = new TextArea();
//							}
//							TextArea ta = (TextArea) uiObject;
//							RendererHelper.fillIn(component, uiObject, uuid, parent, context);
//							if (gvo.getMaxLength() != null) {
//								if (gvo.getMaxLength().intValue() > 0) {
//									ta.setCharacterWidth(gvo.getMaxLength().intValue());
//								} else {
//									ta.setCharacterWidth(80);
//								}
//							} else {
//								ta.setCharacterWidth(80);
//							}
//							ta.setVisibleLines(gvo.getRows());
//
//							ta.setText(gvo.getValue());
//							ta.setReadOnly(!gvo.getEditable().booleanValue());
//							if (gvo.getRequired() != null && gvo.getRequired().booleanValue()) {
//								DOM.setElementProperty(uiObject.getElement(), "required", "true");
//								RendererHelper.setStyleForElement(uiObject.getElement(), "background", "red");
//								((TextArea) uiObject).addValueChangeHandler(new ValueChangeHandler<String>() {
//
//									public void onValueChange(ValueChangeEvent<String> event) {
//										if (event.getSource() instanceof TextBox) {
//											UIObject ui = (UIObject) event.getSource();
//											String value = ((TextBox) ui).getText();
//
//											if (value != null && value.length() > 0) {
//												ui.removeStyleName("qafe_invalid_field");
//
//											} else {
//												ui.addStyleName("qafe_invalid_field");
//
//											}
//										}
//
//									}
//								});
//							}
//						}
//					}
//				}
//				//uiObject.setStylePrimaryName(gvo.getStyleClassName());
//			}
//		}
//
//		return uiObject;
//	}
}
