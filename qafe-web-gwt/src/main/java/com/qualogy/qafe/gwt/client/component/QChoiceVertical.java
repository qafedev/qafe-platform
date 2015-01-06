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
package com.qualogy.qafe.gwt.client.component;

import com.google.gwt.user.client.ui.HasEnabled;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class QChoiceVertical extends VerticalPanel implements HasChoice, HasEnabled {

	private QChoiceHelper choiceHelper = null;

	public QChoiceVertical() {
		choiceHelper = new QChoiceHelper(this);
	}

	public Object getData() {
		return choiceHelper.getData();
	}

	public void setData(Object data, String action, Object mapping) {
		// TODO Auto-generated method stub
	}

	public void addDataChangeHandler(DataChangeHandler handler) {
		choiceHelper.addDataChangeHandler(handler);		
	}

	public boolean removeDataChangeHandler(DataChangeHandler handler) {
		return choiceHelper.removeDataChangeHandler(handler);
	}

	@Override
	public void add(Widget widget) {
		super.add(widget);
		choiceHelper.registerItemValueChangeHandler(widget);
	}
	
	public boolean isEnabled() {
		return choiceHelper.isEnabled();
	}

	public void setEnabled(boolean enabled) {
		choiceHelper.setEnabled(enabled);
	}
}
