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

import com.google.gwt.user.client.ui.HasEnabled;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;

public class QTreeItem extends TreeItem implements HasEnabled {

	private Label label = null;
	
	public QTreeItem(String name) {
		this(name, null);
	}
	
	public QTreeItem(String name, Tree tree) {
		label = new QLabel(name);
		setWidget(label);
	}
		
	@Override
	public void addItem(TreeItem item) {
		super.addItem(item);
	}

	public boolean isEnabled() {
		return ((HasEnabled)label).isEnabled();
	}

	public void setEnabled(boolean enabled) {
		((HasEnabled)label).setEnabled(enabled);
	}

	public Label getLabel() {
		return label;
	}
}
