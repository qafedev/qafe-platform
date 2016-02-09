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
package com.qualogy.qafe.mgwt.client.vo.ui;


public class ToolbarGVO extends EditableComponentGVO{


	private ToolbarItemGVO[] toolbarItems = null;
	private String itemWidth="16px";
	private String itemHeight="16px";
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

	
	


	public ToolbarItemGVO[] getToolbarItems() {
		return toolbarItems;
	}

	public void setToolbarItems(ToolbarItemGVO[] toolbarItems) {
		this.toolbarItems = toolbarItems;
	}

	public String getClassName() {
		return "com.qualogy.qafe.gwt.client.vo.ui.Toolbar";
	}


	
	
}
