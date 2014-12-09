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
package com.qualogy.qafe.bind.presentation.component;

import java.util.ArrayList;
import java.util.List;

public class Toolbar extends EditableComponent{

	/**
	 * 
	 */
	private static final long serialVersionUID = 798169605439000374L;

	protected List<ToolbarItem> toolbarItems = new ArrayList<ToolbarItem>();
	
	protected String itemWidth="16px";
	public String getItemWidth() {
		return itemWidth;
	}

	public void setItemWidth(String itemWidth) {
		this.itemWidth = itemWidth;
	}

	public String getItemHeight() {
		return itemHeight;
	}

	public void setItemHeight(String itemHeight) {
		this.itemHeight = itemHeight;
	}

	protected String itemHeight="16px";
	

	public List<ToolbarItem> getToolbarItems() {
		return toolbarItems;
	}

	public void setToolbarItems(List<ToolbarItem> toolbarItems) {
		this.toolbarItems = toolbarItems;
	}
	
	
}
