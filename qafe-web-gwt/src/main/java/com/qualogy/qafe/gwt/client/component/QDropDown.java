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

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.ListBox;

public class QDropDown extends ListBox implements HasEditable {

	private int selectedIndex;
	
	public QDropDown() {
		setVisibleItemCount(1);
		initSelectedIndex();
	}
	
	private void initSelectedIndex() {
		selectedIndex = 0;
	}
	
	@Override
	public void onBrowserEvent(Event event) {
		super.onBrowserEvent(event);
		if (isEditable()) {
			selectedIndex = super.getSelectedIndex();
			return;
		}
		undoChange();
	}
	
	private void undoChange() {
		super.setSelectedIndex(selectedIndex);
	}
	
	public boolean isEditable() {
		boolean readOnly = DOM.getElementPropertyBoolean(getElement(), "readOnly");
		return !readOnly;
	}

	public void setEditable(boolean editable) {
		boolean readOnly = !editable;
		DOM.setElementPropertyBoolean(getElement(), "readOnly", readOnly);
		String readOnlyStyle = "readonly";
		if (readOnly) {
			addStyleDependentName(readOnlyStyle);
		} else {
			removeStyleDependentName(readOnlyStyle);
		}
	}

	@Override
	public void clear() {
		super.clear();
		initSelectedIndex();
	}

	@Override
	public void setSelectedIndex(int index) {
		super.setSelectedIndex(index);
		selectedIndex = Math.max(index, 0);
	}
}