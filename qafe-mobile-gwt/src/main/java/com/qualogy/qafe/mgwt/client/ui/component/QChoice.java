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

import java.util.Iterator;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.mgwt.ui.client.MGWTStyle;
import com.googlecode.mgwt.ui.client.widget.WidgetList;
import com.qualogy.qafe.mgwt.client.ui.events.DataChangeHandler;

public class QChoice extends FlowPanel implements IsEditable, HasSelection {

	private HTML header;
	private WidgetList widgets;
	private String dataName;
	
	public QChoice() {
		header = new HTML();
		header.addStyleName(MGWTStyle.getTheme().getMGWTClientBundle().getListCss().listHeader());
		super.add(header);
		widgets = new WidgetList();
		super.add(widgets);
	}

	public void addItem(Widget widget) {
		if (widget instanceof QChoiceItem) {
			QChoiceItem choiceItem = (QChoiceItem)widget;
			choiceItem.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
				@Override
				public void onValueChange(ValueChangeEvent<Boolean> event) {
					Object newData = getData();
					ComponentHelper.fireDataChange(QChoice.this, newData, null);
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
		String value = ComponentHelper.toString(data, "");
		Object oldData = getData();
		Iterator<Widget> itrWidget = widgets.iterator();
		while (itrWidget.hasNext()) {
			Widget widget = itrWidget.next();
			if (widget instanceof QChoiceItem) {
				QChoiceItem choiceItem = (QChoiceItem)widget;
				if (value.equals(choiceItem.getFormValue())) {
					choiceItem.setValue(true);
					break;
				}
			}
		}
		Object newData = getData();
		ComponentHelper.handleDataChange(this, newData, oldData);
	}

	@Override
	public Object getData() {
		Iterator<Widget> itrWidget = widgets.iterator();
		while (itrWidget.hasNext()) {
			Widget widget = itrWidget.next();
			if (widget instanceof QChoiceItem) {
				QChoiceItem choiceItem = (QChoiceItem)widget;
				if (choiceItem.getValue()) {
					return choiceItem.getFormValue();
				}
			}
		}
		return null;
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
		// TODO
	}

	@Override
	public void setSelected(Object value) {
		setData(value);
	}
}