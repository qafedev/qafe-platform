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

public class QTab extends QPanel implements IsStackable {

	private HasStacks container = null;
	private QTabBarButton stackButton = null;
	
	@Override
	public void setSelected() {
		if (hasContainer()) {
			getContainer().selectStack(this);
		}
	}

	@Override
	public void setContainer(HasStacks container) {
		this.container = container;
	}

	@Override
	public HasStacks getContainer() {
		return container;
	}
	
	private boolean hasContainer() {
		return (container != null);
	}
	
	@Override
	public void setStackButton(UIObject stackButton) {
		if (stackButton instanceof QTabBarButton) {
			this.stackButton = (QTabBarButton)stackButton;	
			this.stackButton.setVisible(isVisible());
		}
	}
	
	@Override
	public UIObject getStackButton() {
		return stackButton;
	}

	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		if (hasContainer()) {
			getContainer().update();	
		}
	}

	@Override
	public String getDisplayname() {
		if (stackButton != null) {
			return stackButton.getText();
		}
		return null;
	}

	@Override
	public void setDisplayname(String displayname) {
		if (stackButton != null) {
			stackButton.setText(displayname);
		}
	}
}