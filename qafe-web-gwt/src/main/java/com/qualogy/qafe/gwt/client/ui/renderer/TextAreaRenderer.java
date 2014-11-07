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

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.RichTextArea;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.qualogy.qafe.gwt.client.component.LabeledTextAreaFieldWidget;
import com.qualogy.qafe.gwt.client.component.LabeledTextFieldWidget;
import com.qualogy.qafe.gwt.client.component.QTextArea;
import com.qualogy.qafe.gwt.client.context.ClientApplicationContext;
import com.qualogy.qafe.gwt.client.ui.widget.RichTextToolbar;
import com.qualogy.qafe.gwt.client.vo.ui.ComponentGVO;
import com.qualogy.qafe.gwt.client.vo.ui.TextAreaGVO;

public class TextAreaRenderer extends AbstractComponentRenderer {

    // CHECKSTYLE.OFF: CyclomaticComplexity
	public UIObject render(ComponentGVO component, String uuid, String parent, String context) {
		Widget uiObject = null;
		if (component != null) {
			if (component instanceof TextAreaGVO) {
				final ComponentGVO finalComponentGVO = component;
				final String finalUuid = uuid;
				final String finalParent = parent;
				TextAreaGVO gvo = (TextAreaGVO) component;
				if (gvo.getDisplayname() != null && gvo.getDisplayname().length() > 0) {
					if (gvo.getMenu() != null) {
						uiObject = new LabeledTextAreaFieldWidget(gvo.getDisplayname(), gvo.getOrientation(), gvo, uuid, parent) {
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
						uiObject = new LabeledTextAreaFieldWidget(gvo.getDisplayname(), gvo.getOrientation(), gvo, uuid, parent);
					}
				} else {
					if (gvo.getRich() != null) {
						if (gvo.getRich().booleanValue()) {
							// This sample is taken from the KitchenSick demo
							RichTextArea area = new RichTextArea();
							RichTextToolbar tb = new RichTextToolbar(area);

							VerticalPanel p = null;
							if (gvo.getMenu() != null) {
								p = new VerticalPanel() {
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
								p = new VerticalPanel();
							}
							p.add(tb);
							p.add(area);
							area.setHeight("14em");
							area.setWidth("100%");
							tb.setWidth("100%");
							p.setWidth("100%");
						    //p.setStyleName("qafe_rich_textarea");
							DOM.setStyleAttribute(p.getElement(), "marginRight", "4px");

							RendererHelper.fillIn(component, area, uuid, parent, context);
							RendererHelper.fillIn(component, p, uuid, parent, context);
							area.setText(gvo.getValue());
							area.setEnabled(gvo.getEditable().booleanValue());

							uiObject = p;
							if (gvo.getRequired() != null && gvo.getRequired().booleanValue()) {
								DOM.setElementProperty(uiObject.getElement(), "required", "true");
								RendererHelper.setStyleForElement(uiObject.getElement(), "background", "red");

							}
						} else {
							if (gvo.getMenu() != null) {
								uiObject = new QTextArea() {
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
								uiObject = new QTextArea();
							}
							QTextArea ta = (QTextArea)uiObject;
							RendererHelper.fillIn(component, uiObject, uuid, parent, context);
							if (gvo.getCols() > 0) {
								ta.setCharacterWidth(gvo.getCols());
							} else {
								ta.setCharacterWidth(80);
							}

							if (gvo.getMaxLength() != null) {
								if (gvo.getMaxLength().intValue() > 0) {
									DOM.setElementAttribute(uiObject.getElement(), "maxlength", gvo.getMaxLength().toString());
								}
							}
							ta.setVisibleLines(gvo.getRows());

							ta.setText(gvo.getValue());
							ta.setEditable(gvo.getEditable().booleanValue());
							if (gvo.getRequired() != null && gvo.getRequired().booleanValue()) {
								DOM.setElementProperty(uiObject.getElement(), "required", "true");
								RendererHelper.setStyleForElement(uiObject.getElement(), "background", "red");
								((QTextArea)uiObject).addValueChangeHandler(new ValueChangeHandler<String>() {

									public void onValueChange(ValueChangeEvent<String> event) {
										if (event.getSource() instanceof QTextArea) {
											UIObject ui = (UIObject) event.getSource();
											String value = ((QTextArea) ui).getText();

											if (value != null && value.length() > 0) {
												ui.removeStyleName("qafe_invalid_field");

											} else {
												ui.addStyleName("qafe_invalid_field");
											}
										}
									}
								});
							}
						}
					}
				}
				//uiObject.setStylePrimaryName(gvo.getStyleClassName());
				handleMaxLengthAttribute(gvo, uiObject);
				handleMinLengthAttribute(gvo, uiObject);
			}
		}

		return uiObject;
	}
    // CHECKSTYLE.ON: CyclomaticComplexity

	private void handleMinLengthAttribute(final TextAreaGVO gvo, UIObject uiObject) {
		if (gvo.getMinLength() != null) {
			final int minLength = gvo.getMinLength().intValue();
			if (uiObject instanceof QTextArea) {
				((QTextArea) uiObject).addKeyPressHandler(new KeyPressHandler() {

					public void onKeyPress(KeyPressEvent event) {
						if (event.getCharCode() == KeyCodes.KEY_TAB || event.getCharCode() == KeyCodes.KEY_ENTER) {
							if (event.getSource() instanceof QTextArea) {
								QTextArea textArea = (QTextArea) event.getSource();
								if ((textArea.getText() != null && textArea.getText().length() < minLength)) {
									ClientApplicationContext.getInstance().log("Textarea minimum length check", "The length of this field [" + gvo.getId() + "] must be larger than " + minLength, true);
								}
							}
						}

					}
				});

				((QTextArea) uiObject).addBlurHandler(new BlurHandler() {

					public void onBlur(BlurEvent event) {
						if (event.getSource() instanceof QTextArea) {
							QTextArea textArea = (QTextArea) event.getSource();
							if ((textArea.getText() != null && textArea.getText().length() < minLength)) {
								ClientApplicationContext.getInstance().log("Textarea minimum length check", "The length of this field [" + gvo.getId() + "] must be larger than " + minLength, true);
							}
						}

					}
				});

			} else if (uiObject instanceof LabeledTextFieldWidget) {
				final String prompt = ((LabeledTextFieldWidget) uiObject).getLabel().getText();
				((LabeledTextFieldWidget) uiObject).addKeyPressHandler(new KeyPressHandler() {

					public void onKeyPress(KeyPressEvent event) {
						if (event.getCharCode() == KeyCodes.KEY_TAB || event.getCharCode() == KeyCodes.KEY_ENTER) {
							if (event.getSource() instanceof QTextArea) {
								QTextArea textArea = (QTextArea) event.getSource();
								if ((textArea.getText() != null && textArea.getText().length() < minLength)) {
									ClientApplicationContext.getInstance().log("Textarea minimum length check", "The length of this field [" + prompt + "] must be larger than " + minLength, true);
								}
							}
						}
					}
				});

				((LabeledTextFieldWidget) uiObject).addBlurHandler(new BlurHandler() {
					public void onBlur(BlurEvent event) {
						if (event.getSource() instanceof LabeledTextFieldWidget) {
							QTextArea textArea = (QTextArea) event.getSource();
							if ((textArea.getText() != null && textArea.getText().length() < minLength)) {
								ClientApplicationContext.getInstance().log("Textarea minimum length check", "The length of this field [" + prompt + "] must be larger than " + minLength, true);
							}
						}
					}
				});
			}
		}
	}


	private void handleMaxLengthAttribute(final TextAreaGVO gvo, UIObject uiObject) {
		if (gvo.getMaxLength() != null) {
			final int maxLength = gvo.getMaxLength().intValue();
			if (uiObject instanceof QTextArea) {
				((QTextArea) uiObject).setMaxlength(gvo.getMaxLength().intValue());
				((QTextArea) uiObject).addKeyPressHandler(new KeyPressHandler() {

					public void onKeyPress(KeyPressEvent event) {
						if (event.getCharCode() == KeyCodes.KEY_TAB || event.getCharCode() == KeyCodes.KEY_ENTER) {
							if (event.getSource() instanceof QTextArea) {
								QTextArea textArea = (QTextArea) event.getSource();
								if ((textArea.getText() != null && textArea.getText().length() > maxLength)) {
									ClientApplicationContext.getInstance().log("Textarea maximum length check", "The length of this field [" + gvo.getId() + "] must be smaller than " + maxLength, true);
								}
							}
						}

					}
				});

				((QTextArea) uiObject).addBlurHandler(new BlurHandler() {

					public void onBlur(BlurEvent event) {
						if (event.getSource() instanceof QTextArea) {
							QTextArea textArea = (QTextArea) event.getSource();
							if ((textArea.getText() != null && textArea.getText().length() > maxLength)) {
								ClientApplicationContext.getInstance().log("Textarea maximum length check", "The length of this field [" + gvo.getId() + "] must be smaller than " + maxLength, true);
							}
						}
					}
				});
			} else if (uiObject instanceof LabeledTextFieldWidget) {
				((LabeledTextFieldWidget) uiObject).setMaxLength(gvo.getMaxLength().intValue());
				final String prompt = ((LabeledTextFieldWidget) uiObject).getLabel().getText();
				((LabeledTextFieldWidget) uiObject).addKeyPressHandler(new KeyPressHandler() {

					public void onKeyPress(KeyPressEvent event) {
						if (event.getCharCode() == KeyCodes.KEY_TAB || event.getCharCode() == KeyCodes.KEY_ENTER) {
							if (event.getSource() instanceof QTextArea) {
								QTextArea textArea = (QTextArea) event.getSource();
								if ((textArea.getText() != null && textArea.getText().length() > maxLength)) {
									ClientApplicationContext.getInstance().log("Textarea maximum length check", "The length of this field [" + prompt + "] must be smaller than " + maxLength, true);
								}
							}
						}

					}
				});

				((LabeledTextFieldWidget) uiObject).addBlurHandler(new BlurHandler() {

					public void onBlur(BlurEvent event) {
						if (event.getSource() instanceof LabeledTextFieldWidget) {
							QTextArea textArea = (QTextArea) event.getSource();
							if ((textArea.getText() != null && textArea.getText().length() > maxLength)) {
								ClientApplicationContext.getInstance().log("Textarea maximum length check", "The length of this field [" + prompt + "] must be smaller than " + maxLength, true);
							}
						}

					}
				});
			}
		}
	}
}
