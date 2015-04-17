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

import java.util.ArrayList;

import com.google.gwt.user.client.ui.DecoratedStackPanel;
import com.google.gwt.user.client.ui.Widget;

public class QStackPanel extends DecoratedStackPanel {
	private Widget selectedStack;

	public Widget getSelectedStack() {
		return selectedStack;
	}

	public void setSelectedStack(Widget selectedStack) {
		this.selectedStack = selectedStack;
	}

	public void storeSelectedWidget() {
		int currentSelectedIndex = getSelectedIndex();
		if (currentSelectedIndex > -1) {
			selectedStack = getWidget(currentSelectedIndex);
		}
	}
	
	public void showStack(Widget stack) {
	    setSelectedStack(stack);
	    showStack();
    }


	public void processVisible(QStack stackToProcess, boolean visible) {
		storeSelectedWidget();
		if (visible) {
			if (stackToProcess.isAttached()) {
				return;
			}
			ArrayList<QStack> stacks = makeCopyOfStacks();
			//remove all items and add it again to avoid selection problems.
			for (QStack qStack : stacks) {
				remove(qStack);
			}

			addStacksInOrder(stackToProcess, stacks);

		} else if (stackToProcess.isAttached()) {
			remove(stackToProcess);
		}
		showStack();
	}

	private void addStacksInOrder(QStack stackToProcess,
			ArrayList<QStack> stacks) {
		boolean added = false;
		for (int i = 0; i < stacks.size(); i++) {
			QStack qStack = stacks.get(i);
			if (!added && qStack.getStackIndex() > stackToProcess.getStackIndex()) {
				add(stackToProcess, stackToProcess.getStackText());
				added = true;
			}
			add(qStack, qStack.getStackText());
		}
		if (!added) {
			add(stackToProcess, stackToProcess.getStackText());
		}
	}

	private void showStack() {
		int currentSelectedIndex = getWidgetIndex(selectedStack);
		currentSelectedIndex = (currentSelectedIndex > -1) ? currentSelectedIndex
				: 0;
		showStack(currentSelectedIndex);
	}

	private ArrayList<QStack> makeCopyOfStacks() {
		ArrayList<QStack> stacks = new ArrayList<QStack>();
		for (int i = 0; i < getWidgetCount(); i++) {
			if (getWidget(i) instanceof QStack) {
				QStack stack = (QStack) getWidget(i);
				stacks.add(stack);
			}
		}
		return stacks;
	}
}
