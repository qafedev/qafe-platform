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

import com.googlecode.mgwt.ui.client.widget.LayoutPanel;

public class QPanel extends LayoutPanel implements HasDisplayname {

	public QPanel() {
		// Fix for scrolling in a TabPanel 
		getElement().getStyle().setProperty("WebkitBoxFlex", "1");
		getElement().getStyle().setProperty("display", "-webkit-box");
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
}
