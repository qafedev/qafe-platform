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

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.HandlesAllKeyEvents;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.user.client.ui.TextBox;
import com.qualogy.qafe.gwt.client.vo.ui.TextFieldGVO;

public class QTextField extends TextBox implements HasData, HasDataChangeHandlers, HasEditable, HasRequiredValidationMessage {

	public static final String EMPTY_VALUE = "";
	
	public static final int ASCII_SPECIAL_CHARS_THRESHOLD = 40;
	public static final String RESTRICTION_REGEXP_TYPE_INTEGER_VALUE= "^[-]?[0-9]+$";
	public static final String RESTRICTION_REGEXP_TYPE_SIGNED_INTEGER_VALUE="[0-9]+$";
	public static final String RESTRICTION_REGEXP_TYPE_DOUBLE_VALUE= "^[-]?[0-9]*[\\.]?[0-9]*$";
	public static final String RESTRICTION_REGEXP_TYPE_CHARACTERS_VALUE="^[a-zA-Z]+$";
	public static final String RESTRICTION_REGEXP_TYPE_EMAIL_VALUE="[0-9a-zA-Z-,\\._@]*";

	private String oldValue = EMPTY_VALUE;
	private List<DataChangeHandler> dataChangeHandlers = new ArrayList<DataChangeHandler>();
	private String keyRestrictionRegExp = null;
	private String requiredValidationMessage;
	private String requiredValidationTitle;
	
	public QTextField(TextFieldGVO gvo) {
		init(gvo);
		registerKeyEvents();
	}
	
	private void init(TextFieldGVO gvo) {
		if (TextFieldGVO.TYPE_CHARACTERS.equals(gvo.getType())){
			keyRestrictionRegExp = RESTRICTION_REGEXP_TYPE_CHARACTERS_VALUE;
		} else if (TextFieldGVO.TYPE_DOUBLE.equals(gvo.getType())){
			keyRestrictionRegExp = RESTRICTION_REGEXP_TYPE_DOUBLE_VALUE;
		} else if (TextFieldGVO.TYPE_EMAIL.equals(gvo.getType())){
			keyRestrictionRegExp = RESTRICTION_REGEXP_TYPE_EMAIL_VALUE;
		} else if (TextFieldGVO.TYPE_INTEGER.equals(gvo.getType())){
			keyRestrictionRegExp = RESTRICTION_REGEXP_TYPE_INTEGER_VALUE;
		} else if (TextFieldGVO.TYPE_SIGNED_INTEGER.equals(gvo.getType())){
			keyRestrictionRegExp = RESTRICTION_REGEXP_TYPE_SIGNED_INTEGER_VALUE;
		}		
	}
	
	public Object getData() {
		return getText();
	}

	public void setData(Object data, String action, Object mapping) {
		String text = (String)data;
		setText(text);
	}

	public void setText(String text) {
		rememberValue();
		super.setText(validateType(text));
		handleDataChange();
	}

	private String validateType(String text) {
		String textToSet = text;
		String specifiedType = "";
		if(text !=null && text.length() > 0 && keyRestrictionRegExp != null){
			try{
				if(keyRestrictionRegExp.equals(RESTRICTION_REGEXP_TYPE_INTEGER_VALUE)) {
					specifiedType = "int";
					double doubleValue = Double.parseDouble(text);
					textToSet = (int)doubleValue + "";
				} else if(keyRestrictionRegExp.equals(RESTRICTION_REGEXP_TYPE_DOUBLE_VALUE)) {
					specifiedType = "double";
					double doubleValue = Double.parseDouble(text);
					textToSet = doubleValue + "";
				} else if(keyRestrictionRegExp.equals(RESTRICTION_REGEXP_TYPE_EMAIL_VALUE)) {
					specifiedType = "email";
					if(text.matches(RESTRICTION_REGEXP_TYPE_EMAIL_VALUE)){
						textToSet = text;
					} else {
						throw new RuntimeException();
					}
				}
			} catch(Exception e){
				throw new RuntimeException("Mismatch in datatype. Setting invalid data - \""+ text +"\" to a text field with type = \"" + specifiedType + "\"");
			}
		}
		return textToSet;
	}

	private void registerKeyEvents() {
		HandlesAllKeyEvents keyEvents = new HandlesAllKeyEvents() {

			public void onKeyDown(KeyDownEvent event) {
				// TODO Auto-generated method stub					
			}

			public void onKeyUp(KeyUpEvent event) {
				handleDataChange();
			}

			public void onKeyPress(KeyPressEvent event) {
				handleKeyRestriction(event, event.getCharCode());
			}
		};
	    keyEvents.addKeyHandlersTo((com.google.gwt.event.dom.client.HasAllKeyHandlers)this);
	}
	
	private boolean isSpecialKeyPressed(KeyPressEvent event) {
		if (event.isAnyModifierKeyDown()) {
			return true;
		}
		return (event.getCharCode() < ASCII_SPECIAL_CHARS_THRESHOLD);
	}
	
	private void handleKeyRestriction(KeyPressEvent event, char keyPress) {
		if (isSpecialKeyPressed(event)) {
			return;
		}
		
		String newValue = constructNewValue(keyPress);
		
		if ((newValue != null) && (keyRestrictionRegExp != null)) {
			if (!isNegativeOnlyValueAllowed(newValue) && newValue.replaceFirst(keyRestrictionRegExp, "").length() > 0) {
				event.preventDefault();
			}
		}
		
		
	}
	
	private String constructNewValue(char keyPress) {
		String previousValue = getText();
		String newValue = "";
		int cursorPos = getCursorPos();
		newValue = previousValue.substring(0, cursorPos) + keyPress + previousValue.substring(cursorPos + getSelectionLength(), previousValue.length());
		return newValue;
	}
	
	private boolean isNegativeOnlyValueAllowed(String newValue) {
		return (("-".equals(newValue)) && (keyRestrictionRegExp.equals(RESTRICTION_REGEXP_TYPE_INTEGER_VALUE) || keyRestrictionRegExp.equals(RESTRICTION_REGEXP_TYPE_DOUBLE_VALUE)));
	}
	
	private void handleDataChange() {
		if (isDataChanged()) {
			doDataChange();
		}
	}
	
	private boolean isDataChanged() {
		String newValue = getText();
		return !getRememberedValue().equals(newValue);
	}
	
	private void rememberValue() {
		oldValue = getText();
	}
	
	private String getRememberedValue() {
		return oldValue;
	}
	
	private void doDataChange() {
		fireDataChange(getRememberedValue(), getText());
		rememberValue();
	}
	
	private void fireDataChange(String oldValue, String newValue) {
		for (int i=0; i<dataChangeHandlers.size(); i++) {
			DataChangeHandler handler = dataChangeHandlers.get(i);
			handler.onDataChange(this, oldValue, newValue);
		}
	}
	
	public void addDataChangeHandler(DataChangeHandler handler) {
		if (handler != null) {
			dataChangeHandlers.add(handler);
		}
	}

	public boolean removeDataChangeHandler(DataChangeHandler handler) {
		return dataChangeHandlers.remove(handler);
	}

	public boolean isEditable() {
		return !isReadOnly();
	}

	public void setEditable(boolean editable) {
		setReadOnly(!editable);
	}

	public String getRequiredValidationMessage() {
		return requiredValidationMessage;
	}

	public String getRequiredValidationTitle() {
		return requiredValidationTitle;
	}

	public void setRequiredValidationMessage(String message) {
		this.requiredValidationMessage = message;
	}

	public void setRequiredValidationTitle(String title) {
		this.requiredValidationTitle = title;
	}
}