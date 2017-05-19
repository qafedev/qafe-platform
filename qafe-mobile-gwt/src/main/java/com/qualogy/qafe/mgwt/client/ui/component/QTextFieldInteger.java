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
package com.qualogy.qafe.mgwt.client.ui.component;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.UIObject;
import com.googlecode.mgwt.ui.client.widget.MIntegerBox;
import com.qualogy.qafe.mgwt.client.ui.events.DataChangeHandler;

public class QTextFieldInteger extends MIntegerBox implements IsTextField {

	private String dataName;
	private Boolean required;
	private String requiredValidationMessage;
	private String requiredValidationTitle;
	
	public QTextFieldInteger(String text) {
		setPlaceHolder(text);
		initialize();
	}
	
	protected void initialize() {
		addValueChangeHandler(new ValueChangeHandler<Integer>() {
			@Override
			public void onValueChange(ValueChangeEvent<Integer> event) {
				Object newData = getData();
				ComponentHelper.fireDataChange(QTextFieldInteger.this, newData, null);
			}
		});
		addKeyPressHandler(new KeyPressHandler() {
			@Override
			public void onKeyPress(KeyPressEvent event) {
				ComponentHelper.handleKeyConstraints(QTextFieldInteger.this, getText(), event, event.getCharCode());
			}
		});
	}
	
	@Override
	public HandlerRegistration addDataChangeHandler(DataChangeHandler handler) {
		return ComponentHelper.addDataChangeHandler(this, handler);
	}

	@Override
	public void setDataName(String dataName) {
		this.dataName = dataName;
	}

	@Override
	public String getDataName() {
		return dataName;
	}
	
	@Override
	public void setData(Object data) {
		Integer value = ComponentHelper.toInteger(data, null);
		Object oldData = getData();
		super.setValue(value);
		Object newData = getData();
		ComponentHelper.handleDataChange(this, newData, oldData);
	}

	@Override
	public Object getData() {
		Object data = super.getValue();
		ComponentHelper.checkRequired(this, data);
		return data;
	}
	
	@Override
	public Object getDataModel() {
		return getData();
	}

	@Override
	public Object getModel() {
		return getData();
	}
	
	@Override
	public void setEditable(boolean editable) {
		setReadOnly(!editable);
	}
	
	@Override
	public String getDisplayname() {
		return getPlaceHolder();
	}

	@Override
	public void setDisplayname(String displayname) {
		setPlaceHolder(displayname);
	}

	@Override
	public List<String> getKeyConstraints() {
		List<String> constraints = new ArrayList<String>();
		constraints.add(TYPE_INTEGER);
		return constraints;
	}
	
	@Override
	public List<UIObject> getStyleWidgets() {
		List<UIObject> styleWidgets = new ArrayList<UIObject>();
		styleWidgets.add(this);
		styleWidgets.add(box);
		return styleWidgets;
	}

	public Boolean getRequired() {
		return required;
	}

	public void setRequired(Boolean required) {
		this.required = required;
	}

	public String getRequiredValidationMessage() {
		return requiredValidationMessage;
	}

	public void setRequiredValidationMessage(String requiredValidationMessage) {
		this.requiredValidationMessage = requiredValidationMessage;
	}

	public String getRequiredValidationTitle() {
		return requiredValidationTitle;
	}

	public void setRequiredValidationTitle(String requiredValidationTitle) {
		this.requiredValidationTitle = requiredValidationTitle;
	}
}