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

import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.UIObject;
import com.qualogy.qafe.gwt.client.util.ComponentRepository;

public class QRadioButton extends RadioButton implements ClickHandler, HasText {

	public QRadioButton(String name, String label, boolean asHTML) {
		super(name, label, asHTML);
		addClickHandler(this);
	}

	public QRadioButton(String name, String label) {
		super(name, label);
		addClickHandler(this);
	}

	public QRadioButton(String name, String label, String value) {
		super(name, label);
		DOM.setElementAttribute(getElement(), "value", value);
		DOM.setElementAttribute(getElement(), "isSelected","false");
		addClickHandler(this);
	}

	public QRadioButton(String name) {
		super(name);
		addClickHandler(this);
	}

	public String getText() {
		String returnValue=null;
		String key = DOM.getElementAttribute(getElement(), "id");
		List<UIObject> components = ComponentRepository.getInstance().getComponent(key);
		if (components!=null) {
			for (UIObject object : components) {
				String selected = DOM.getElementAttribute(object.getElement(), "isSelected");
				if ("true".equalsIgnoreCase(selected)){
					returnValue = 	DOM.getElementAttribute(object.getElement(), "value");
				}
			}
		}
		return returnValue;
	}

	@Override
	public void setValue(Boolean value) {
	    setValue(value, true);
	}

	
	public void setValue(String text) {
		String key = DOM.getElementAttribute(getElement(), "id");
		List<UIObject> components = ComponentRepository.getInstance().getComponent(key);
		if (components!=null){
			for (UIObject object : components) {
				if (text!=null){
					if (text.equals(DOM.getElementAttribute(object.getElement(), "value"))){
						DOM.setElementAttribute(object.getElement(), "isSelected","true");
						if (object instanceof QRadioButton){
							((QRadioButton)object).setValue(true);
						}
					}
				}
			}
		}
	}

	public void reset() {
		setValue(false);
		String key = DOM.getElementAttribute(getElement(), "id");
		List<UIObject> components = ComponentRepository.getInstance().getComponent(key);
		if(components == null) {
		    return;
		}
		for (UIObject object : components) {
			DOM.setElementAttribute(object.getElement(), "isSelected","false");
			if (object instanceof RadioButton){
				RadioButton rb = (RadioButton)object;
				rb.setChecked(false);
			}
		}
	}

	public void onClick(ClickEvent event) {
		// Save current instance - you could just save the text to save
		// memory, but you might well want to add getters for other
		// attributes of the current instance

		if (event.getSource() instanceof RadioButton) {
			RadioButton rb = (RadioButton) event.getSource();
			String key = DOM.getElementAttribute(rb.getElement(), "id");
			if(key != null && key.length() > 0) {
				List<UIObject> components = ComponentRepository.getInstance().getComponent(key);
				for (UIObject object : components) {
					if (object == rb) {
						DOM.setElementAttribute(object.getElement(), "isSelected", "true");
					} else {
						DOM.setElementAttribute(object.getElement(), "isSelected","false");
					}
				}					
			} 
		}
	}
}
