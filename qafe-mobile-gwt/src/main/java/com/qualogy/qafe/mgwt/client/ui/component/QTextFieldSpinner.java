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
package com.qualogy.qafe.mgwt.client.ui.component;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.UIObject;
import com.googlecode.mgwt.ui.client.widget.MListBox;
import com.qualogy.qafe.mgwt.client.ui.events.DataChangeHandler;

public class QTextFieldSpinner extends MListBox implements IsTextField {
	
	private String dataName;
	private int minValue;
	private int maxValue;
	private Boolean required;
	private String requiredValidationMessage;
	private String requiredValidationTitle;
	
	public QTextFieldSpinner() {
		initialize();
	}
	
	private void initialize() {
		addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				Object newData = getData();
				ComponentHelper.fireDataChange(QTextFieldSpinner.this, newData, null);
			}
		});
	}

	public long getMinValue() {
		return minValue;
	}

	public void setMinValue(int minValue) {
		this.minValue = minValue;
		updateRange();
	}

	public long getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(int maxValue) {
		this.maxValue = maxValue;
		updateRange();
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
		String value = ComponentHelper.toString(data, null);
		Object oldData = getData();
		handleSelection(value);
		Object newData = getData();
		ComponentHelper.handleDataChange(this, newData, oldData);
	}

	@Override
	public Object getData() {
		int selectedIndex = getSelectedIndex();
		return getData(selectedIndex);
	}

	@Override
	public Object getDataModel() {
		return getData();
	}

	@Override
	public Object getModel() {
		return getData();
	}

	private Object getData(int index) {
		Object data = getItemText(index);
		ComponentHelper.checkRequired(this, data);
		return data;
	}
	
	private void handleSelection(String value) {
		int selectedIndex = 0;
		if (value != null) {
			int itemCount = getItemCount();
			for (int i=0; i<itemCount; i++) {
				Object data = getData(i);
				if (value.equals(data)) {
					selectedIndex = i;
					break;
				}
			}	
		}
		setSelectedIndex(selectedIndex);
	}

	private void updateRange() {
		clear();
		for (int i=minValue; i<=maxValue; i++) {
			addItem(String.valueOf(i));
		}
	}
	
	@Override
	public void setEditable(boolean editable) {
		setEnabled(editable);
	}

	@Override
	public String getDisplayname() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setDisplayname(String displayname) {
		// TODO Auto-generated method stub
	}

	@Override
	public List<String> getKeyConstraints() {
		return null;
	}
	
	@Override
	public List<UIObject> getStyleWidgets() {
		List<UIObject> styleWidgets = new ArrayList<UIObject>();
		styleWidgets.add(this);
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