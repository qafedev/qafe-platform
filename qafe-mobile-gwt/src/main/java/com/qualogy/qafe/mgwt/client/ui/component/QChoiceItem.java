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

import com.googlecode.mgwt.ui.client.widget.MRadioButton;

public class QChoiceItem extends MRadioButton implements HasDisplayname {

	public QChoiceItem(String name) {
		super(name);
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
