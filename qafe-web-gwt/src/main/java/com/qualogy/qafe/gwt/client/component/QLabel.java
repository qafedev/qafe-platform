/**
 * Copyright 2008-2017 Qualogy Solutions B.V.
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

import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.HasEnabled;
import com.google.gwt.user.client.ui.Label;

public class QLabel extends Label implements HasEnabled {
	
	public static final String DISABLED_STYLE_NAME = "qafe_item_disabled";
	
	public QLabel(String name) {
		super(name, false);
	}

	private boolean enabled;

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
		if (enabled) {			
			this.removeStyleName(DISABLED_STYLE_NAME);
		}else {
			this.setStyleName(DISABLED_STYLE_NAME);
		}
	}
	
	@Override
	public void onBrowserEvent(Event event) {
		if (isEnabled()){
			super.onBrowserEvent(event);
		}
	}

	@Override
	protected void setElement(Element elem) {
		super.setElement(elem);
		sinkEvents(Event.ONCONTEXTMENU);
	}
	
}
