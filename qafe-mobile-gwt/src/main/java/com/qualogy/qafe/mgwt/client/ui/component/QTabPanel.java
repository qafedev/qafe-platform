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
package com.qualogy.qafe.mgwt.client.ui.component;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.Widget;
import com.googlecode.mgwt.ui.client.widget.tabbar.TabPanel;

public class QTabPanel extends TabPanel implements HasStacks {

	private List<IsStackable> stacks = new ArrayList<IsStackable>();

	public void add(String text, Widget child) {
		addStack(text, (IsStackable)child);
	}

	@Override
	public void addStack(String label, IsStackable stack) {
		if (stack == null) {
			return;
		}
		QTabBarButton tabBarButton = new QTabBarButton(label);
		super.add(tabBarButton, (Widget)stack);
		stacks.add(stack);
		stack.setContainer(this);
		stack.setStackButton(tabBarButton);
	}

	@Override
	public void selectStack(IsStackable stack) {
		int stackIndex = stacks.indexOf(stack);
		if (stackIndex > -1) {
			super.setSelectedChild(stackIndex);
		}
	}

	@Override
	public void update() {
		for (IsStackable stack : stacks) {
			boolean visible = stack.isVisible();
			stack.getStackButton().setVisible(visible);
		}
	}
}
