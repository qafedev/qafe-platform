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
package com.qualogy.qafe.gwt.client.component;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.HasAllFocusHandlers;
import com.google.gwt.event.dom.client.HasAllKeyHandlers;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.HasName;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.TextBoxBase;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;
import com.qualogy.qafe.gwt.client.context.ClientApplicationContext;
import com.qualogy.qafe.gwt.client.ui.renderer.RendererHelper;
import com.qualogy.qafe.gwt.client.vo.ui.TextFieldGVO;

/**
 * This class is a SimplePanel, but implements all the interfaces of the 
 * TextBox class,so that it can be used a special kind of textbox.
 * @author rjankie
 *
 */
public class LabeledTextFieldWidget extends SimplePanel implements HasPrompt, TitledComponent, EventListener, Focusable, HasName, HasText, HasEditable, HasValueChangeHandlers, HasClickHandlers, HasAllFocusHandlers, HasAllKeyHandlers, HasRequiredValidationMessage {

	private Label label = new Label();
	private UIObject textbox ;
	private final static String ORIENTATION_UPDOWN="updown";
	private final static String ORIENTATION_LEFTRIGHT="leftright";

	public LabeledTextFieldWidget(TextFieldGVO gvo, String labelText, String orientation,String type,String format){
		if (type != null) {
			if (TextFieldGVO.TYPE_DATE.equals(type)) {
				textbox = new QDatePicker(format);
			} else if(TextFieldGVO.TYPE_SPINNER.equals(type)){
				int minValue = 0;
				int maxValue = 20;
				if (gvo.getMinValue() != null) {
					minValue = Integer.parseInt(gvo.getMinValue());
				}	
				if (gvo.getMaxValue() != null) {
					maxValue = Integer.parseInt(gvo.getMaxValue());
				}	
				long initialValue = Long.parseLong(minValue + "");
				textbox = new QValueSpinner(initialValue, minValue, maxValue);
			} else {
				textbox = new QTextField(gvo);
				handleTypeAttribute(textbox,gvo.getRegExp(),gvo.getValidationMessage(), gvo.getValidationTitle());
			}
		} else {
			textbox = new QTextField(gvo);
		}
		label.setText(labelText);
		String height = TextFieldGVO.DEFAULT_HEIGHT;
		String width = TextFieldGVO.DEFAULT_WIDTH;
		if (gvo.getHeight()!=null) {
			height = gvo.getHeight();
		}	
		if (gvo.getWidth()!=null) {
			width = gvo.getWidth();
		}	

		FlexTable flexTable = new FlexTable();
		RendererHelper.setStyleForElement(textbox.getElement(),"width", width);
		RendererHelper.setStyleForElement(textbox.getElement(),"height", height);
		RendererHelper.setStyleForElement(label.getElement(),"width", "auto");
		
		if (textbox instanceof QValueSpinner) {
			TextBox spinnerTextBox = ((QValueSpinner)textbox).getTextBox();
			//spinnerTextBox.setStylePrimaryName(gvo.getStyleClassName());
			flexTable.setWidget(0, 1, (Widget)textbox);
		} else {
			if (ORIENTATION_LEFTRIGHT.equals(orientation)) {
				RendererHelper.setStyleForElement(textbox.getElement(),"marginRight", "20px");
				flexTable.setWidget(0, 1, (Widget)textbox);				
			} else if (ORIENTATION_UPDOWN.equals(orientation)) {
				RendererHelper.setStyleForElement(textbox.getElement(),"marginTop", "20px");
				flexTable.setWidget(1, 0, (Widget)textbox);				
			}
		}
		flexTable.setWidget(0, 0, label);
		add(flexTable);
	}
	
	protected void handleTypeAttribute(UIObject uiObject, final String regExp, final String validationMessage, final String validationTitle) {
		if (uiObject instanceof TextBox) {
			TextBox textBox = (TextBox)uiObject;
			textBox.addBlurHandler(new BlurHandler(){
				public void onBlur(BlurEvent event) {
					String textValue = ((TextBoxBase)event.getSource()).getText();
					if ((textValue != null) && (regExp != null)) {											
						if (textValue.replaceFirst(regExp, "").length() > 0) {							
							if (validationTitle != null) {
								ClientApplicationContext.getInstance().log(validationTitle, validationMessage,true);
							} else {
								ClientApplicationContext.getInstance().log("Validation error", validationMessage,true);
							}
						}						
					}					
				}
			});
		}		
	}
	
	public String getText() {
		if (textbox instanceof HasText) {
			return ((HasText)textbox).getText();
		} 
		if (textbox instanceof QValueSpinner) {
			return ((QValueSpinner)textbox).getTextBox().getText();
		}
		return null;
	}

	public void setText(String text) {
		if (textbox instanceof HasText) {
			((HasText)textbox).setText(text);
		}
	}

	public Label getLabel() {
		return label;
	}

	public void setLabel(Label label) {
		this.label = label;
	}

	public UIObject getTextbox() {
		return textbox;
	}

	public void setTextbox(TextBox textbox) {
		this.textbox = textbox;
	}

	public int getTabIndex() {
		if (textbox instanceof Focusable) {
			return ((Focusable)textbox).getTabIndex();
		} 
		return 0;
	}

	public void setAccessKey(char arg0) {
		if (textbox instanceof Focusable) {
			((Focusable)textbox).setAccessKey(arg0);
		}	
	}

	public void setFocus(boolean arg0) {
		if (textbox instanceof Focusable) {
			 ((Focusable)textbox).setFocus(arg0);
		}	 
	}

	public void setTabIndex(int arg0) {
		if (textbox instanceof Focusable) {
			 ((Focusable)textbox).setTabIndex(arg0);
		}	 
	}

	public String getName() {
		if (textbox instanceof HasName) {
			return ((HasName)textbox).getName();
		}	
		return null;
	}

	public void setName(String arg0) {
		if (textbox instanceof HasName) {
			((HasName)textbox).setName(arg0);
		}	  
	}

	public void setMaxLength(int length) {
		if (textbox instanceof TextBox){
			((TextBox)textbox).setMaxLength(length);
		}
	}

	public boolean isEditable() {
		if (textbox instanceof HasEditable) {
			HasEditable hasEditable = (HasEditable)textbox;
			return hasEditable.isEditable();
		}
		return false;
	}

	public void setEditable(boolean editable) {
		if (textbox instanceof HasEditable) {
			HasEditable hasEditable = (HasEditable)textbox;
			hasEditable.setEditable(editable);
		}
	}
	
	public UIObject getDataComponent() {
		return textbox;
	}

	public UIObject getTitleComponent() {
		return label;
	}

	public String getPrompt() {
		return label.getText();
	}

	public void setPrompt(String prompt) {
		label.setText(prompt);		
	}

	public HandlerRegistration addValueChangeHandler(ValueChangeHandler handler) {
		if (textbox instanceof HasValueChangeHandlers) {
			return ((HasValueChangeHandlers)textbox).addValueChangeHandler(handler);
		} 
		return null;
	}

	public HandlerRegistration addClickHandler(ClickHandler handler) {
		if (textbox instanceof HasClickHandlers) {
			return ((HasClickHandlers)textbox).addClickHandler(handler);
		} 
		return null;
	}

	public HandlerRegistration addFocusHandler(FocusHandler handler) {
		if (textbox instanceof HasAllFocusHandlers){
			return ((HasAllFocusHandlers)textbox).addFocusHandler(handler);
		}
		return null;
	}

	public HandlerRegistration addBlurHandler(BlurHandler handler) {
		if (textbox instanceof HasAllFocusHandlers){
			return ((HasAllFocusHandlers)textbox).addBlurHandler(handler);
		}
		return null;
	}

	public HandlerRegistration addKeyUpHandler(KeyUpHandler handler) {
		if (textbox instanceof HasAllKeyHandlers){
			return ((HasAllKeyHandlers)textbox).addKeyUpHandler(handler);
		}
		return null;
	}

	public HandlerRegistration addKeyDownHandler(KeyDownHandler handler) {
		if (textbox instanceof HasAllKeyHandlers){
			return ((HasAllKeyHandlers)textbox).addKeyDownHandler(handler);
		}
		return null;
	}

	public HandlerRegistration addKeyPressHandler(KeyPressHandler handler) {
		if (textbox instanceof HasAllKeyHandlers){
			return ((HasAllKeyHandlers)textbox).addKeyPressHandler(handler);
		}
		return null;
	}
	
	@Override
	public void addStyleName(String style) {
		if (getTextbox() != null) {
			getTextbox().setStyleName(getTextbox().getElement(), style, true);			
		} else {
			super.removeStyleName(style);
		}
	}
	
	@Override
	public void removeStyleName(String style) {
		if (getTextbox() != null) {
			getTextbox().setStyleName(getTextbox().getElement(), style, false);
		} else {
			super.removeStyleName(style);
		}
	}

	public String getRequiredValidationMessage() {
		if (textbox instanceof HasRequiredValidationMessage){
			return ((HasRequiredValidationMessage) textbox).getRequiredValidationMessage();
		}
		return null;
	}

	public void setRequiredValidationMessage(String message) {
		if (textbox instanceof HasRequiredValidationMessage){
			((HasRequiredValidationMessage) textbox).setRequiredValidationMessage(message);
		}
	}

	public String getRequiredValidationTitle() {
		if (textbox instanceof HasRequiredValidationMessage){
			return ((HasRequiredValidationMessage) textbox).getRequiredValidationTitle();
		}
		return null;
	}

	public void setRequiredValidationTitle(String title) {
		if (textbox instanceof HasRequiredValidationMessage){
			((HasRequiredValidationMessage) textbox).setRequiredValidationTitle(title);
		}
		
	}
}