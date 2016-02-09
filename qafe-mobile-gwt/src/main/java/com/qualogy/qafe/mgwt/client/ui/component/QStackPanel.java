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

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.Widget;
import com.googlecode.mgwt.ui.client.widget.Carousel;

public class QStackPanel extends Carousel implements HasStacks {

	private List<IsStackable> stacks = new ArrayList<IsStackable>();
	
	@Override
	public void add(Widget widget) {
		addStack(null, (IsStackable)widget);
	}
	
	@Override
	public void addStack(String label, IsStackable stack) {
		if (stack == null) {
			return;
		}
		stack.setContainer(this);
		super.add((Widget)stack);
		stacks.add(stack);
	}

	@Override
	public void selectStack(IsStackable stack) {
		int stackIndex = stacks.indexOf(stack);
		if (stackIndex > -1) {
			// This method does not work properly in MGWT 1.1.1,
			// when switching to the second page while it has 3 pages
			setSelectedPage(stackIndex);
		}
	}
	
	@Override
	public void update() {
		clear();
		for (IsStackable stack : stacks) {
			if (stack.isVisible()) {
				super.add((Widget)stack);	
			}
		}
		refresh();
	}
}
