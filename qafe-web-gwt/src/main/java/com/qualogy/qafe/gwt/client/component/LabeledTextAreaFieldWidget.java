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
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.HasName;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.qualogy.qafe.gwt.client.ui.renderer.RendererHelper;
import com.qualogy.qafe.gwt.client.ui.widget.RichTextToolbar;
import com.qualogy.qafe.gwt.client.vo.ui.TextAreaGVO;

/**
 * This class is a SimplePanel, but implements all the interfaces of the 
 * TextBox class,so that it can be used a special kind of textbox.
 * @author rjankie
 *
 */
public class LabeledTextAreaFieldWidget extends SimplePanel implements TitledComponent, HasPrompt, EventListener, Focusable, HasName, HasText, HasEditable, HasValueChangeHandlers, HasClickHandlers, HasAllFocusHandlers, HasAllKeyHandlers {

	private Label label = new Label();
	private Widget content;
	private Widget uiObject ;
	private final static String ORIENTATION_UPDOWN="updown";
	private final static String ORIENTATION_LEFTRIGHT="leftright";

	public LabeledTextAreaFieldWidget(String labelText, String orientation,TextAreaGVO component,String uuid,String parent){
		if (component.getRich().booleanValue()){
			// This sample is taken from the KitchenSick demo 
		    content = new QRichTextArea();
		    RichTextToolbar tb = new RichTextToolbar((QRichTextArea)content);

		    VerticalPanel p = new VerticalPanel();
		    p.add(tb);
		    p.add(content);
		    content.setHeight("14em");
		    content.setWidth("100%");
		    tb.setWidth("100%");
		    p.setWidth("100%");
		    //p.setStyleName("qafe_rich_textarea");
		    DOM.setStyleAttribute(p.getElement(), "marginRight", "4px");
			
			RendererHelper.fillIn(component, content, uuid,parent, component.getContext());
			RendererHelper.fillIn(component, p,uuid,parent, component.getContext());
			((QRichTextArea)content).setText(component.getValue());
			
			uiObject = p;
			if (component.getRequired()!=null  && component.getRequired().booleanValue()){					
				DOM.setElementProperty(uiObject.getElement(), "required", "true");
				RendererHelper.setStyleForElement(uiObject.getElement(), "background","red");
			}
		} else {
			content = new QTextArea();
			uiObject = content;
			QTextArea ta =(QTextArea)uiObject;
			RendererHelper.fillIn(component, uiObject,uuid,parent, component.getContext());
			if (component.getMaxLength()!=null){
				if (component.getMaxLength().intValue()>0){
					ta.setCharacterWidth(component.getMaxLength().intValue());
				} else {
					ta.setCharacterWidth(80);
				}
			} else {
				ta.setCharacterWidth(80);
			}
			ta.setVisibleLines(component.getRows());
			//ta.setStylePrimaryName("qafe_textarea");
			ta.setText(component.getValue());
			ta.setEnabled(!component.getDisabled().booleanValue());
			ta.setReadOnly(!component.getEditable().booleanValue());
			if (component.getRequired()!=null  && component.getRequired().booleanValue()){					
				DOM.setElementProperty(uiObject.getElement(), "required", "true");
				uiObject.addStyleName("qafe_invalid_field");
				((QTextArea)uiObject).addValueChangeHandler(new ValueChangeHandler<String>(){
					public void onValueChange(ValueChangeEvent<String> event) {
						if (event.getSource() instanceof TextBox){
							TextBox tb= (TextBox)event.getSource();
							String value =tb.getText(); 
							if (value!=null && value.length()>0 ){
								tb.removeStyleName("qafe_invalid_field");
							} else {
								tb.addStyleName("qafe_invalid_field");
							}
						}
					}
				});
			}
		}
		
		label.setText(labelText);
		
		//label.setStylePrimaryName("qafe_label");
		
		if (ORIENTATION_LEFTRIGHT.equals(orientation)){
			HorizontalPanel horizontalPanel = new HorizontalPanel();		
			horizontalPanel.add(label);
			horizontalPanel.add(uiObject);
			add(horizontalPanel);
		} else if (ORIENTATION_UPDOWN.equals(orientation)){
			VerticalPanel verticalPanel = new VerticalPanel();		
			verticalPanel.add(label);
			verticalPanel.add(uiObject);
			add(verticalPanel);
		}
	}
	
	public String getText() {
		if (content instanceof HasText){
			return ((HasText)content).getText();
		}
		return null;
	}

	public void setText(String text) {
		if (content instanceof HasText){
			((HasText)content).setText(text);
		}
	}

	public Label getLabel() {
		return label;
	}

	public void setLabel(Label label) {
		this.label = label;
	}

	public int getTabIndex() {
		if (content instanceof Focusable){
			return ((Focusable)content).getTabIndex();
		}
		return -1;
	}

	public void setTabIndex(int arg0) {
		if (content instanceof Focusable){
			((Focusable)content).setTabIndex(arg0);
		}
	}
	
	public void setAccessKey(char arg0) {
		if (content instanceof Focusable){
			((Focusable)content).setAccessKey(arg0);
		}
	}

	public void setFocus(boolean arg0) {
		if (content instanceof Focusable){
			((Focusable)content).setFocus(arg0);
		}
	}

	public String getName() {
		if (uiObject instanceof HasName){
			return ((HasName)uiObject).getName();
		}
		return null;
	}

	public void setName(String arg0) {
		if (uiObject instanceof HasName){
			((HasName)uiObject).setName(arg0);
		}
	}

	public String getPrompt() {
		return label.getText();
	}

	public void setPrompt(String prompt) {
		label.setText(prompt);
	}

	public UIObject getDataComponent() {
		return uiObject;
	}

	public UIObject getTitleComponent() {
		return label;
	}

	public HandlerRegistration addValueChangeHandler(ValueChangeHandler handler) {
		if (uiObject instanceof HasValueChangeHandlers) {
			return ((HasValueChangeHandlers)uiObject).addValueChangeHandler(handler);
		}
		return null;
	}

	public HandlerRegistration addClickHandler(ClickHandler handler) {
		if (uiObject instanceof HasClickHandlers) {
			return ((HasClickHandlers)uiObject).addClickHandler(handler);
		}
		return null;
	}

	public HandlerRegistration addFocusHandler(FocusHandler handler) {
		if (uiObject instanceof HasAllFocusHandlers) {
			return ((HasAllFocusHandlers)uiObject).addFocusHandler(handler);
		}
		return null;
	}

	public HandlerRegistration addBlurHandler(BlurHandler handler) {
		if (uiObject instanceof HasAllFocusHandlers) {
			return ((HasAllFocusHandlers)uiObject).addBlurHandler(handler);
		}
		return null;
	}

	public HandlerRegistration addKeyUpHandler(KeyUpHandler handler) {
		if (uiObject instanceof HasAllKeyHandlers) {
			return ((HasAllKeyHandlers)uiObject).addKeyUpHandler(handler);
		}
		return null;
	}

	public HandlerRegistration addKeyDownHandler(KeyDownHandler handler) {
		if (uiObject instanceof HasAllKeyHandlers) {
			return ((HasAllKeyHandlers)uiObject).addKeyDownHandler(handler);
		}
		return null;
	}

	public HandlerRegistration addKeyPressHandler(KeyPressHandler handler) {
		if (uiObject instanceof HasAllKeyHandlers) {
			return ((HasAllKeyHandlers)uiObject).addKeyPressHandler(handler);
		}
		return null;
	}

	public boolean isEditable() {
		if (content instanceof HasEditable) {
			HasEditable hasEditable = (HasEditable)content;
			return hasEditable.isEditable();
		}
		return false;
	}

	public void setEditable(boolean editable) {
		if (content instanceof HasEditable) {
			HasEditable hasEditable = (HasEditable)content;
			hasEditable.setEditable(editable);
		}
	}
}