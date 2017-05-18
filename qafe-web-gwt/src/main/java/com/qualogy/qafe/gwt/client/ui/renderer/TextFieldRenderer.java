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

import org.gwt.mosaic.core.client.DOM;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.UIObject;
import com.qualogy.qafe.gwt.client.component.ComponentRendererHelper;
import com.qualogy.qafe.gwt.client.component.DataChangeHandler;
import com.qualogy.qafe.gwt.client.component.HasData;
import com.qualogy.qafe.gwt.client.component.HasDataChangeHandlers;
import com.qualogy.qafe.gwt.client.component.HasEditable;
import com.qualogy.qafe.gwt.client.component.HasRequiredValidationMessage;
import com.qualogy.qafe.gwt.client.component.LabeledTextFieldWidget;
import com.qualogy.qafe.gwt.client.component.QDatePicker;
import com.qualogy.qafe.gwt.client.component.QSuggestBox;
import com.qualogy.qafe.gwt.client.component.QSuggestOracle;
import com.qualogy.qafe.gwt.client.component.QTextField;
import com.qualogy.qafe.gwt.client.component.QValueSpinner;
import com.qualogy.qafe.gwt.client.context.ClientApplicationContext;
import com.qualogy.qafe.gwt.client.vo.functions.SetValueGVO;
import com.qualogy.qafe.gwt.client.vo.ui.ComponentGVO;
import com.qualogy.qafe.gwt.client.vo.ui.EditableComponentGVO;
import com.qualogy.qafe.gwt.client.vo.ui.TextFieldGVO;

public class TextFieldRenderer extends AbstractTextComponentRenderer {

    // CHECKSTYLE.OFF: CyclomaticComplexity
	public UIObject render(ComponentGVO component, String uuid, String parent, String context) {
		UIObject uiObject = null;
		if (component != null && component instanceof TextFieldGVO) {
			final TextFieldGVO gvo = (TextFieldGVO) component;
			final ComponentGVO finalComponentGVO = component;
			final String finalUuid = uuid;
			final String finalParent = parent;
			if (gvo.getDisplayname() == null || gvo.getDisplayname().length() == 0) {
				if (gvo.getType() != null) {
					if (TextFieldGVO.TYPE_DATE.equals(gvo.getType())) {
						if (gvo.getMenu() != null) {
							uiObject = new QDatePicker(gvo.getFormat()) {
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
							uiObject = new QDatePicker(gvo.getFormat());
						}
						uiObject.setHeight(TextFieldGVO.DEFAULT_HEIGHT);

						RendererHelper.fillIn(component, ((QDatePicker)uiObject).getTextBox(), uiObject, uuid, parent, context);
						RendererHelper.addEvents(component, uiObject, uuid);

					} else if (TextFieldGVO.TYPE_SPINNER.equals(gvo.getType())) {
						String height = TextFieldGVO.DEFAULT_HEIGHT;
						String width = TextFieldGVO.DEFAULT_WIDTH;
						int minValue = TextFieldGVO.DEFAULT_MINIMUM;
						int maxValue = TextFieldGVO.DEFAULT_MAXIMUM;
						if (gvo.getHeight() != null)
							height = gvo.getHeight();
						if (gvo.getWidth() != null)
							width = gvo.getWidth();
						if (gvo.getMinValue() != null)
							minValue = Integer.parseInt(gvo.getMinValue());
						if (gvo.getMaxValue() != null)
							maxValue = Integer.parseInt(gvo.getMaxValue());
						long initialValue;
						if (gvo.getValue() != null) {
							initialValue = Long.parseLong(gvo.getValue());
							if(initialValue < (long)minValue || initialValue > (long)maxValue) {
								initialValue = minValue;
							}
						} else {
							initialValue = minValue;
						}
						if (gvo.getMenu() != null) {
							uiObject = new QValueSpinner(initialValue, minValue, maxValue) {
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
							uiObject = new QValueSpinner(initialValue, minValue, maxValue);
						}
						TextBox spinnerTextBox = ((QValueSpinner)uiObject).getTextBox();
						RendererHelper.setStyleForElement(spinnerTextBox.getElement(), "width", width);
						RendererHelper.setStyleForElement(spinnerTextBox.getElement(), "height", height);
						//spinnerTextBox.setStylePrimaryName(gvo.getStyleClassName());
						//spinnerTextBox.setStyleName(gvo.getStyleClass());
						RendererHelper.fillIn(component, spinnerTextBox, uuid, parent, context);
					} else if (gvo.getSuggest()) {
						QSuggestOracle oracle = new QSuggestOracle();
						QSuggestBox suggest = null;
						if (gvo.getMenu() != null) {
							suggest = new QSuggestBox(oracle) {
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
							suggest = new QSuggestBox(oracle);
						}
						suggest.setSuggestCharactersLength(gvo.getSuggestCharacters());

						uiObject = suggest;
//							RendererHelper.fillIn(component, ((QSuggestBox)uiObject).getTextBox(), uuid, parent, context);
						RendererHelper.fillIn(component, uiObject, uuid, parent, context);
					} else {
						if (gvo.getMenu() != null) {
							uiObject = new QTextField(gvo) {
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
							uiObject = new QTextField(gvo);
						}
						RendererHelper.fillIn(component, uiObject, uuid, parent, context);
					}
				}
			} else {
                if (gvo.getMenu() != null) {
                	uiObject = new LabeledTextFieldWidget(gvo, gvo.getDisplayname(), gvo.getOrientation(), gvo.getType(), gvo.getFormat()){
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
				}else{
					uiObject = new LabeledTextFieldWidget(gvo, gvo.getDisplayname(), gvo.getOrientation(), gvo.getType(), gvo.getFormat());
				}
                UIObject labeledTextBox = ((LabeledTextFieldWidget) uiObject).getTextbox();
                UIObject actualComp = labeledTextBox;
                UIObject labelOnComp = ((LabeledTextFieldWidget) uiObject).getLabel();
                if (labeledTextBox instanceof QDatePicker) {
                	actualComp = ((QDatePicker)labeledTextBox).getTextBox();
                	RendererHelper.addEvents(component, labeledTextBox, uuid);
                } else if (labeledTextBox instanceof QValueSpinner) {
                	actualComp = ((QValueSpinner)labeledTextBox).getTextBox();
                } else if (labeledTextBox instanceof QSuggestBox) {
                	actualComp = ((QSuggestBox)labeledTextBox).getTextBox();
                }
                RendererHelper.fillIn(component, actualComp, uuid, parent, context);
			}

			// for checking based on type introduces this.
			if( gvo.getRegExp() != null) {
				DOM.setElementAttribute(RendererHelper.getElement(uiObject), TextFieldGVO.REGEXPTYPE, gvo.getType());
			}

			registerDataChange(gvo, uiObject);

			handleTypeAttribute(uiObject, gvo.getRegExp(), gvo.getValidationTitle(),gvo.getValidationMessage());
			handleEditableAttribute(gvo, uiObject);
			handleMinLengthAttribute(gvo, uiObject);
			handleMaxLengthAttribute(gvo, uiObject);
			handleValueAttribute(gvo, uiObject);
			handleRequiredValidationMessageAttribute(gvo, uiObject);

			RendererHelper.handleRequiredAttribute(gvo, uiObject);
		}
		return uiObject;
	}
    // CHECKSTYLE.ON: CyclomaticComplexity

	private void handleValueAttribute(TextFieldGVO gvo, UIObject uiObject) {
		String value = gvo.getValue();
		if ((value != null) && (value.length() > 0)) {
			if (uiObject instanceof HasData) {
				HasData hasData = (HasData)uiObject;
				hasData.setData(value, SetValueGVO.ACTION_ADD, null);
			} else if (uiObject instanceof HasText) {
				HasText hasText = (HasText)uiObject;
				String oldValue = hasText.getText();
				hasText.setText(value);
				doDataChange(gvo, uiObject, oldValue, hasText.getText());
			}
		}
	}

	private void handleEditableAttribute(TextFieldGVO gvo, UIObject uiObject) {
		boolean editable = gvo.getEditable();
		if (uiObject instanceof HasEditable) {
			HasEditable hasEditable = (HasEditable)uiObject;
			hasEditable.setEditable(editable);
		}
	}

	private void handleMinLengthAttribute(final TextFieldGVO gvo, UIObject uiObject) {
		if (gvo.getMinLength() != null) {
			final int minLength = gvo.getMinLength().intValue();
			if (uiObject instanceof TextBox) {

				((TextBox) uiObject).addKeyPressHandler(new KeyPressHandler() {

					public void onKeyPress(KeyPressEvent event) {
						if (event.getCharCode() == KeyCodes.KEY_TAB || event.getCharCode() == KeyCodes.KEY_ENTER) {
							if (event.getSource() instanceof TextBox) {
								TextBox textBox = (TextBox) event.getSource();
								if ((textBox.getText() != null && textBox.getText().length() < minLength)) {
									ClientApplicationContext.getInstance().log("Textbox minimum length check", "The length of this field [" + gvo.getId() + "] must be larger than " + minLength, true);
								}
							}
						}

					}
				});

				((TextBox) uiObject).addBlurHandler(new BlurHandler() {

					public void onBlur(BlurEvent event) {
						if (event.getSource() instanceof TextBox) {
							TextBox textBox = (TextBox) event.getSource();
							if ((textBox.getText() != null && textBox.getText().length() < minLength)) {
								ClientApplicationContext.getInstance().log("Textbox minimum length check", "The length of this field [" + gvo.getId() + "] must be larger than " + minLength, true);
							}
						}

					}
				});

			} else if (uiObject instanceof LabeledTextFieldWidget) {
				final String prompt = ((LabeledTextFieldWidget) uiObject).getLabel().getText();
				((LabeledTextFieldWidget) uiObject).addKeyPressHandler(new KeyPressHandler() {

					public void onKeyPress(KeyPressEvent event) {
						if (event.getCharCode() == KeyCodes.KEY_TAB || event.getCharCode() == KeyCodes.KEY_ENTER) {
							if (event.getSource() instanceof TextBox) {
								TextBox textBox = (TextBox) event.getSource();
								if ((textBox.getText() != null && textBox.getText().length() < minLength)) {
									ClientApplicationContext.getInstance().log("Textbox minimum length check", "The length of this field [" + prompt + "] must be larger than " + minLength, true);
								}
							}
						}

					}
				});

				((LabeledTextFieldWidget) uiObject).addBlurHandler(new BlurHandler() {

					public void onBlur(BlurEvent event) {
						if (event.getSource() instanceof LabeledTextFieldWidget) {
							TextBox textBox = (TextBox) event.getSource();
							if ((textBox.getText() != null && textBox.getText().length() < minLength)) {
								ClientApplicationContext.getInstance().log("Textbox minimum length check", "The length of this field [" + prompt + "] must be larger than " + minLength, true);
							}
						}

					}
				});
			}
		}

	}

	private void handleMaxLengthAttribute(final TextFieldGVO gvo, UIObject uiObject) {
		if (gvo.getMaxLength() != null) {
			final int maxLength = gvo.getMaxLength().intValue();
			if (uiObject instanceof TextBox) {
				((TextBox) uiObject).setMaxLength(gvo.getMaxLength().intValue());
				((TextBox) uiObject).addKeyPressHandler(new KeyPressHandler() {

					public void onKeyPress(KeyPressEvent event) {
						if (event.getCharCode() == KeyCodes.KEY_TAB || event.getCharCode() == KeyCodes.KEY_ENTER) {
							if (event.getSource() instanceof TextBox) {
								TextBox textBox = (TextBox) event.getSource();
								if ((textBox.getText() != null && textBox.getText().length() > maxLength)) {
									ClientApplicationContext.getInstance().log("Textbox maximum length check", "The length of this field [" + gvo.getId() + "] must be smaller than " + maxLength, true);
								}
							}
						}

					}
				});

				((TextBox) uiObject).addBlurHandler(new BlurHandler() {

					public void onBlur(BlurEvent event) {
						if (event.getSource() instanceof TextBox) {
							TextBox textBox = (TextBox) event.getSource();
							if ((textBox.getText() != null && textBox.getText().length() > maxLength)) {
								ClientApplicationContext.getInstance().log("Textbox maximum length check", "The length of this field [" + gvo.getId() + "] must be smaller than " + maxLength, true);
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
							if (event.getSource() instanceof TextBox) {
								TextBox textBox = (TextBox) event.getSource();
								if ((textBox.getText() != null && textBox.getText().length() > maxLength)) {
									ClientApplicationContext.getInstance().log("Textbox maximum length check", "The length of this field [" + prompt + "] must be smaller than " + maxLength, true);
								}
							}
						}

					}
				});

				((LabeledTextFieldWidget) uiObject).addBlurHandler(new BlurHandler() {

					public void onBlur(BlurEvent event) {
						if (event.getSource() instanceof LabeledTextFieldWidget) {
							TextBox textBox = (TextBox) event.getSource();
							if ((textBox.getText() != null && textBox.getText().length() > maxLength)) {
								ClientApplicationContext.getInstance().log("Textbox maximum length check", "The length of this field [" + prompt + "] must be smaller than " + maxLength, true);
							}
						}

					}
				});
			}
		}

	}

	private void registerDataChange(final TextFieldGVO gvo, UIObject uiObject) {
		if (uiObject instanceof HasDataChangeHandlers) {
			((HasDataChangeHandlers)uiObject).addDataChangeHandler(new DataChangeHandler() {

				public void onDataChange(UIObject uiObject, Object oldValue, Object newValue) {
					doDataChange(gvo, uiObject, oldValue, newValue);

				}
			});
		}
	}

	private void doDataChange(EditableComponentGVO component, UIObject uiObject, Object oldValue, Object newValue) {
		ComponentRendererHelper.handleDataChange(component, uiObject, oldValue, newValue);
	}

	private void handleRequiredValidationMessageAttribute(TextFieldGVO gvo, UIObject uiObject) {
		if (uiObject instanceof HasRequiredValidationMessage) {
			HasRequiredValidationMessage hasRequiredValidationMessage = (HasRequiredValidationMessage)uiObject;
			hasRequiredValidationMessage.setRequiredValidationMessage(gvo.getRequiredValidationMessage());
			hasRequiredValidationMessage.setRequiredValidationTitle(gvo.getRequiredValidationTitle());
		}
	}
}
