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
/**
 * 
 */
package com.qualogy.qafe.mgwt.client.vo.ui;

/**
 * @author rjankie
 */
public class TabPanelGVO extends ComponentGVO implements HasComponentsI {

	private static final long serialVersionUID = -7554429256802450967L;
	protected TabGVO[] tabs = null;
	//private String styleClassName = "qafe_tab";

	public TabGVO[] getTabs() {
		return tabs;
	}

	public void setTabs(TabGVO[] tabs) {
		this.tabs = tabs;
	}

	public String getClassName() {
		return "com.qualogy.qafe.gwt.client.vo.ui.TabPanelGVO";
	}

	public ComponentGVO[] getComponents() {
		return getTabs();
	}

	/*public String getStyleClassName() {
		return styleClassName;
	}

	public void setStyleClassName(String styleClassName) {
		this.styleClassName = styleClassName;
	}*/
}
