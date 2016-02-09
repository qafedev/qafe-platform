/**
 * Copyright 2008-2016 Qualogy Solutions B.V.
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
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.mgwt.ui.client.MGWTStyle;
import com.googlecode.mgwt.ui.client.widget.WidgetList;
import com.qualogy.qafe.mgwt.client.ui.events.DataChangeHandler;
import com.qualogy.qafe.mgwt.shared.QAMLConstants;

public class QListBox extends FlowPanel implements IsEditable, HasSelection {
	
	private HTML header;
	private WidgetList widgets;
	private boolean multipleSelect;
	private String uniqueId;
	private String dataName;
	
	public QListBox(boolean multipleSelect, String uniqueId) {
		header = new HTML();
		header.addStyleName(MGWTStyle.getTheme().getMGWTClientBundle().getListCss().listHeader());
		super.add(header);
		widgets = new WidgetList();
		super.add(widgets);
		this.multipleSelect = multipleSelect;
		this.uniqueId = uniqueId;
	}

	public boolean isMultipleSelect() {
		return multipleSelect;
	}

	public void addItem(String item, String value) {
		String name= uniqueId;
		if (isMultipleSelect()) {
			name = null;
		}
		QListBoxItem widget = new QListBoxItem(name);
		widget.setDisplayname(item);
		widget.setFormValue(value);
		addItem(widget);
	}
	
	public void addItem(Widget widget) {
		if (widget instanceof QListBoxItem) {
			QListBoxItem listBoxItem = (QListBoxItem)widget;
			listBoxItem.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
				@Override
				public void onValueChange(ValueChangeEvent<Boolean> event) {
					Object newData = getData();
					ComponentHelper.fireDataChange(QListBox.this, newData, null);
				}
			});
			widgets.add(widget);
		}
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
		Iterator<Widget> itrWidget = widgets.iterator();
		while (itrWidget.hasNext()) {
			Widget widget = itrWidget.next();
			if (widget instanceof QListBoxItem) {
				QListBoxItem listBoxItem = (QListBoxItem)widget;
				if (listBoxItem.getValue()) {
					return listBoxItem.getFormValue();
				}
			}
		}
		return null;
	}

	@Override
	public Object getDataModel() {
		List dataModel = new ArrayList();
		Iterator<Widget> itrWidget = widgets.iterator();
		while (itrWidget.hasNext()) {
			Widget widget = itrWidget.next();
			if (widget instanceof QListBoxItem) {
				QListBoxItem listBoxItem = (QListBoxItem)widget;
				if (listBoxItem.getValue()) {
					Object object = getDataModel(listBoxItem);
					if (object != null) {
						dataModel.add(object);	
					}
				}
			}
		}
		if (dataModel.isEmpty()) {
			return null;
		}
		if (isMultipleSelect()) {
			return dataModel;
		}
		return dataModel.get(0);
	}

	@Override
	public Object getModel() {
		List model = new ArrayList();
		Iterator<Widget> itrWidget = widgets.iterator();
		while (itrWidget.hasNext()) {
			Widget widget = itrWidget.next();
			if (widget instanceof QListBoxItem) {
				QListBoxItem listBoxItem = (QListBoxItem)widget;
				Object object = getDataModel(listBoxItem);
				if (object != null) {
					model.add(object);	
				}
			}
		}
		if (model.isEmpty()) {
			return null;
		}
		return model;
	}

	private Object getDataModel(Widget widget) {
		Map dataModel = null;
		if (widget instanceof QListBoxItem) {
			QListBoxItem listBoxItem = (QListBoxItem)widget;
			dataModel = new HashMap();
			String selectedValue = listBoxItem.getFormValue();
			dataModel.put(QAMLConstants.INTERNAL_ITEM_VALUE, selectedValue);
			String selectedItem = listBoxItem.getText();
			dataModel.put(QAMLConstants.INTERNAL_ITEM_LABEL, selectedItem);
		}	
		return dataModel;
	}
	
	private void handleNoSelection() {
		handleSelection(null);
	}
	
	private void handleSelection(String value) {
		Iterator<Widget> itrWidget = widgets.iterator();
		while (itrWidget.hasNext()) {
			Widget widget = itrWidget.next();
			if (widget instanceof QListBoxItem) {
				QListBoxItem listBoxItem = (QListBoxItem)widget;
				boolean selected = false;
				if (value != null) {
					selected = value.equals(listBoxItem.getFormValue()) || value.equals(listBoxItem.getText()); 
				}
				listBoxItem.setValue(selected);
			}
		}
	}
	
	private void handleInsertion(List values) {
		widgets.clear();
		for (int i=0; i<values.size(); i++) {
			Object value = values.get(i);
			if (value == null) {
				continue;
			}
			String itemLabel = null;
			String itemValue = null;
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
	
	@Override
	public void setEditable(boolean editable) {
		// TODO
	}

	@Override
	public void setSelected(Object value) {
		setData(value);
	}
}

