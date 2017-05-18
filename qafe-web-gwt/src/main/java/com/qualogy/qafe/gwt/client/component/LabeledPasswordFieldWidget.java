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
package com.qualogy.qafe.gwt.client.component;

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
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.HasName;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * This class is a SimplePanel, but implements all the interfaces of the 
 * TextBox class,so that it can be used a special kind of textbox.
 * @author rjankie
 *
 */
public class LabeledPasswordFieldWidget extends SimplePanel implements TitledComponent,HasPrompt, EventListener, Focusable, HasName, HasText, HasEditable, HasValueChangeHandlers, HasClickHandlers, HasAllFocusHandlers, HasAllKeyHandlers {

	private Label label = new Label();
	private QPasswordTextBox textbox ;
	private final static String ORIENTATION_UPDOWN="updown";
	private final static String ORIENTATION_LEFTRIGHT="leftright";

	public LabeledPasswordFieldWidget(String labelText, String orientation){
		textbox = new QPasswordTextBox();				
		label.setText(labelText);		
		label.setStylePrimaryName("label");
		if (ORIENTATION_LEFTRIGHT.equals(orientation)) {
			HorizontalPanel horizontalPanel = new HorizontalPanel();		
			horizontalPanel.add(label);
			horizontalPanel.add(textbox);
			add(horizontalPanel);
		} else if (ORIENTATION_UPDOWN.equals(orientation)) {
			VerticalPanel verticalPanel = new VerticalPanel();		
			verticalPanel.add(label);
			verticalPanel.add(textbox);
			add(verticalPanel);
		}
	}
	
	public String getText() {
		return textbox.getText();
	}

	public void setText(String text) {
		textbox.setText(text);		
	}

	public Label getLabel() {
		return label;
	}

	public void setLabel(Label label) {
		this.label = label;
	}

	public int getTabIndex() {
		return textbox.getTabIndex();
	}
	
	public void setTabIndex(int arg0) {
		textbox.setTabIndex(arg0);
	}
	
	public void setAccessKey(char arg0) {
		textbox.setAccessKey(arg0);
	}

	public void setFocus(boolean arg0) {
		textbox.setFocus(arg0);
	}

	public String getName() {
		return textbox.getName();
	}

	public void setName(String arg0) {
		textbox.setName(arg0);
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
	
	public String getPrompt() {
		return label.getText();
	}

	public void setPrompt(String prompt) {
		label.setText(prompt);
	}

	public UIObject getDataComponent() {
		return textbox;
	}

	public UIObject getTitleComponent() {
		return label;
	}
	
	public HandlerRegistration addClickHandler(ClickHandler handler) {
		return textbox.addClickHandler(handler);
	}

	public HandlerRegistration addValueChangeHandler(ValueChangeHandler handler) {
		return textbox.addValueChangeHandler(handler);
	}

	public HandlerRegistration addFocusHandler(FocusHandler handler) {
		return textbox.addFocusHandler(handler);
	}

	public HandlerRegistration addBlurHandler(BlurHandler handler) {
		return textbox.addBlurHandler(handler);
	}

	public HandlerRegistration addKeyUpHandler(KeyUpHandler handler) {
		return textbox.addKeyUpHandler(handler);
	}

	public HandlerRegistration addKeyDownHandler(KeyDownHandler handler) {
		return textbox.addKeyDownHandler(handler);
	}

	public HandlerRegistration addKeyPressHandler(KeyPressHandler handler) {
		return textbox.addKeyPressHandler(handler);
	}
	
	@Override
	public void addStyleName(String style) {
		getDataComponent().setStyleName(getDataComponent().getElement(), style, true);
	}
	
	@Override
	public void removeStyleName(String style) {
		getDataComponent().setStyleName(getDataComponent().getElement(), style, false);
	}
	
	@Override
	public void setStylePrimaryName(String style) {
		getDataComponent().setStylePrimaryName(getDataComponent().getElement(), style);
	}
}