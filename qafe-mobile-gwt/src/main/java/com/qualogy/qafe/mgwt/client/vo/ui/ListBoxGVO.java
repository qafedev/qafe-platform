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
/**
 * 
 */
package com.qualogy.qafe.mgwt.client.vo.ui;

/**
 * @author rjankie
 */

public class ListBoxGVO extends DropDownGVO {

	private Integer nrOfVisibleItems = null;
	//private String styleClassName = "qafe_listbox";
	private Boolean multipleSelect = Boolean.FALSE;
	private static final long serialVersionUID = -1062972016925601394L;

	public Integer getNrOfVisibleItems() {
		return nrOfVisibleItems;
	}

	public void setNrOfVisibleItems(Integer nrOfVisibleItems) {
		this.nrOfVisibleItems = nrOfVisibleItems;
	}

	public Boolean getMultipleSelect() {
		return multipleSelect;
	}

	public void setMultipleSelect(Boolean multipleSelect) {
		this.multipleSelect = multipleSelect;
	}

	public String getClassName() {
		return "com.qualogy.qafe.gwt.client.vo.ui.ListBoxGVO";
	}

	/*public String getStyleClassName() {
		return styleClassName;
	}

	public void setStyleClassName(String styleClassName) {
		this.styleClassName = styleClassName;
	}*/
}
