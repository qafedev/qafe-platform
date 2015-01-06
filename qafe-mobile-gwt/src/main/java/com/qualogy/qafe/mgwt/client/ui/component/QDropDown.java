/**
 * Copyright 2008-2015 Qualogy Solutions B.V.
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.googlecode.mgwt.ui.client.widget.MListBox;
import com.qualogy.qafe.mgwt.client.ui.events.DataChangeHandler;
import com.qualogy.qafe.mgwt.shared.QAMLConstants;

public class QDropDown extends MListBox implements IsEditable, HasSelection {

	private static final int EMPTY_ITEM_INDEX		= 0;
	
	private Map<String,Integer> items = new HashMap<String,Integer>();
	private Map<String,Integer> values = new HashMap<String,Integer>();
	private String emptyItem;
	private String emptyItemValue;
	private String dataName;
	
	public QDropDown() {
		initialize();
	}
	
	private void initialize() {
		addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				Object newData = getData();
				ComponentHelper.fireDataChange(QDropDown.this, newData, null);
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
		Object oldData = getData();
		if (data == null) {
			handleNoSelection();
			Object newData = getData();
			ComponentHelper.handleDataChange(this, newData, oldData);
		} else if (data instanceof Map) {
			// TODO
		} else if (data instanceof List) {
			handleInsertion((List)data);
			ComponentHelper.fireDataChange(this, data, oldData);
		} else {
			handleSelection(data.toString());
			Object newData = getData();
			ComponentHelper.handleDataChange(this, newData, oldData);
		}
	}

	@Override
	public Object getData() {
		int selectedIndex = getSelectedIndex();
		if (selectedIndex > -1) {
			String selectedValue = getValue(selectedIndex);
			if (values.containsKey(selectedValue)) {
				return selectedValue;
			}
			String selectedItem = getItemText(selectedIndex);
			if (items.containsKey(selectedItem)) {
				return selectedItem;
			}	
		}
		return null;
	}

	@Override
	public Object getDataModel() {
		int selectedIndex = getSelectedIndex();
		Object dataModel = getDataModel(selectedIndex);
		return dataModel;
	}

	@Override
	public Object getModel() {
		List model = null;
		int numItems = getItemCount();
		if (numItems > 0) {
			model = new ArrayList();
			for (int i=0; i<numItems; i++) {
				Object dataModel = getDataModel(i);
				if (dataModel != null) {
					model.add(dataModel);
				}
			}
		}
		return model;
	}

	private Object getDataModel(int index) {
		Map dataModel = null;
		if (index > -1) {
			dataModel = new HashMap();
			String selectedValue = getValue(index);
			if (values.containsKey(selectedValue)) {
				dataModel.put(QAMLConstants.INTERNAL_ITEM_VALUE, selectedValue);
			}
			String selectedItem = getItemText(index);
			if (items.containsKey(selectedItem)) {
				dataModel.put(QAMLConstants.INTERNAL_ITEM_LABEL, selectedItem);
			}	
		}
		return dataModel;
	}
	
	private void handleNoSelection() {
		int selectedIndex = -1;
		if (hasEmptyItem()) {
			selectedIndex = EMPTY_ITEM_INDEX;
		}
		setSelectedIndex(selectedIndex);
	}
	
	private void handleSelection(String value) {
		int selectedIndex = -1;
		if (values.containsKey(value)) {
			selectedIndex = values.get(value);
		} else if (items.containsKey(value)) {
			selectedIndex = items.get(value);
		}
		setSelectedIndex(selectedIndex);
	}
	
	private void handleInsertion(List values) {
		clear();
		setEmptyItem(emptyItem, emptyItemValue);
		for (int i=0; i<values.size(); i++) {
			Object value = values.get(i);
			String itemLabel = null;
			String itemValue = null;
			if (value == null) {
				continue;
			}
			if (value instanceof Map) {
				Map map = (Map)value;
				Object item = map.get(QAMLConstants.INTERNAL_ITEM_LABEL);
				if (item != null) {
					itemLabel = item.toString();
				}
				item = map.get(QAMLConstants.INTERNAL_ITEM_VALUE);
				if (item != null) {
					itemValue = item.toString();
				}
			} else {
				itemLabel = value.toString();
				itemValue = itemLabel;
			}
			if ((itemLabel != null) && (itemValue != null)) {
				addItem(itemLabel, itemValue);	
			}
		}
	}

	private boolean hasEmptyItem() {
		return (emptyItem != null) || (emptyItemValue != null);
	}
	
	@Override
	public void clear() {
		items.clear();
		values.clear();
		super.clear();
	}
	
	public void setEmptyItem(String item, String value) {
		if ((item == null) && (value == null)) {
			setSelectedIndex(-1);
			return;
		}
		this.emptyItem = item;
		this.emptyItemValue = value;
		insertItem(this.emptyItem, this.emptyItemValue, EMPTY_ITEM_INDEX);
	}
	
	@Override
	public void insertItem(String item, String value, int index) {
		int itemCount = getItemCount();
	    if ((index < 0) || (index > itemCount)) {
	      index = itemCount;
	    }
	    super.insertItem(item, value, index);
		items.put(item, index);
		values.put(value, index);
	}
	
	@Override
	public void setEditable(boolean editable) {
		setEnabled(editable);
	}

	@Override
	public void setSelected(Object value) {
		setData(value);
	}
}