/**
 * Copyright 2008-2014 Qualogy Solutions B.V.
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

import com.google.gwt.user.client.ui.UIObject;
import com.googlecode.mgwt.ui.client.widget.ScrollPanel;

public class QStack extends ScrollPanel implements IsStackable {

	private HasStacks container = null;
	
	@Override
	public void setSelected() {
		getContainer().selectStack(this);
	}

	@Override
	public void setContainer(HasStacks container) {
		this.container = container;
	}

	@Override
	public HasStacks getContainer() {
		return container;
	}
	
	@Override
	public void setStackButton(UIObject stackButton) {
		// Do nothing
	}
	
	@Override
	public UIObject getStackButton() {
		return null;
	}
	
	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		getContainer().update();
	}
}