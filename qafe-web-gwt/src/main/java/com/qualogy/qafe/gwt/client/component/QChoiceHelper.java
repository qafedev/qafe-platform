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
package com.qualogy.qafe.gwt.client.component;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.HasEnabled;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

public class QChoiceHelper {
	
	private UIObject uiObject;
	private List<DataChangeHandler> dataChangeHandlers = new ArrayList<DataChangeHandler>();
	private boolean enabled;

	public QChoiceHelper(UIObject uiObject) {
		this.uiObject = uiObject;
	}
	
	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
		List<Widget> items = getItems();
		if (items == null) {
			return;
		}
		for (Widget item : items) {
			if (item instanceof HasEnabled) {
				HasEnabled hasEnabled = (HasEnabled)item;
				hasEnabled.setEnabled(enabled);
			}
		}
	}
	
	public Object getData() {
		List<Widget> items = getItems();
		if (items != null) {
			for (Widget item: items) {
				if (item instanceof QRadioButton) {
					QRadioButton radioButton = (QRadioButton)item;
					if (radioButton.getValue()) {
						return radioButton.getText();
					}
				}
			}
		}
		return null;
	}
	
	public void addDataChangeHandler(DataChangeHandler handler) {
		if (handler instanceof DataChangeHandler) {
			dataChangeHandlers.add(handler);
		}
	}

	public boolean removeDataChangeHandler(DataChangeHandler handler) {
		return dataChangeHandlers.remove(handler);
	}
	
	public void registerItemValueChangeHandler(final Widget item) {
		if (item instanceof HasValueChangeHandlers) {
			HasValueChangeHandlers<Boolean> hasValueChangeHandlers = (HasValueChangeHandlers<Boolean>)item;
			hasValueChangeHandlers.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
				public void onValueChange(ValueChangeEvent<Boolean> event) {
					doItemValueChange(event);
				}
			});
		}
	}
	
	private void doItemValueChange(ValueChangeEvent<Boolean> event) {
	    Object value = event.getValue();
	    UIObject source = (UIObject)event.getSource();
	    if (source instanceof HasText) {
	        value = ((HasText)source).getText();
	    }
		fireDataChange(uiObject, null, value);
	}
	
	private void fireDataChange(UIObject ui, Object oldValue, Object newValue) {
		for (DataChangeHandler handler: dataChangeHandlers) {
			handler.onDataChange(ui, oldValue, newValue);
		}
	}
	
	private List<Widget> getItems() {
		List<Widget> items = new ArrayList<Widget>();
		if (uiObject instanceof ComplexPanel) {
			ComplexPanel complexPanel = (ComplexPanel)uiObject;
			int numItems = complexPanel.getWidgetCount();
			for (int i=0; i<numItems; i++) {
				Widget item = complexPanel.getWidget(i);
				items.add(item);
			}
		}
		return items;
	}
}