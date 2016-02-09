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

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HasEnabled;
import com.googlecode.mgwt.ui.client.widget.Button;
import com.qualogy.qafe.mgwt.client.ui.events.DataChangeHandler;

public class QButton extends Button implements HasData, HasEnabled, HasDisplayname {

	private boolean enabled;
	private String dataName;
	
	public QButton(String text) {
		super(text);
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
		super.setText(value);
		Object newData = getData();
		ComponentHelper.handleDataChange(this, newData, oldData);
	}

	@Override
	public Object getData() {
		return super.getText();
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
	public void setText(String text) {
		setData(text);
	}
	
	@Override
	public boolean isEnabled() {
		return enabled;
	}

	@Override
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
		// TODO
	}

	@Override
	public String getDisplayname() {
		return getText();
	}

	@Override
	public void setDisplayname(String displayname) {
		setText(displayname);
	}
}