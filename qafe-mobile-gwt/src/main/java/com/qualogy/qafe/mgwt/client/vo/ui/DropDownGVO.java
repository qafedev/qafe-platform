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
/**
 * 
 */
package com.qualogy.qafe.mgwt.client.vo.ui;

/**
 * @author rjankie
 */

public class DropDownGVO extends EditableComponentGVO implements HasRequiredClass {

	private static final long serialVersionUID = 4225013574017207708L;
	private Boolean required = Boolean.FALSE;
	private String requiredStyleClassName;
	//private String styleClassName = "qafe_dropdown";
	protected DropDownItemGVO[] dropDownItems = null;
	private String emptyItemDisplayName;
	private String emptyItemValue;
	private String emptyItemMessageKey;

	public String getEmptyItemDisplayName() {
		return emptyItemDisplayName;
	}

	public void setEmptyItemDisplayName(String emptyItemDisplayName) {
		this.emptyItemDisplayName = emptyItemDisplayName;
	}

	public String getEmptyItemValue() {
		return emptyItemValue;
	}

	public void setEmptyItemValue(String emptyItemValue) {
		this.emptyItemValue = emptyItemValue;
	}

	public String getEmptyItemMessageKey() {
		return emptyItemMessageKey;
	}

	public void setEmptyItemMessageKey(String emptyItemMessageKey) {
		this.emptyItemMessageKey = emptyItemMessageKey;
	}

	public Boolean getRequired() {
		return required;
	}

	public void setRequired(Boolean required) {
		this.required = required;
	}

	public String getRequiredStyleClassName() {
		return requiredStyleClassName;
	}

	public void setRequiredStyleClassName(String requiredStyleClassName) {
		this.requiredStyleClassName = requiredStyleClassName;
	}

	public DropDownItemGVO[] getDropDownItems() {
		return dropDownItems;
	}

	public void setDropDownItems(DropDownItemGVO[] dropDownItems) {
		this.dropDownItems = dropDownItems;
	}

	public String getClassName() {
		return "com.qualogy.qafe.gwt.client.vo.ui.DropDownGVO";
	}

	/*public String getStyleClassName() {
		return styleClassName;
	}

	public void setStyleClassName(String styleClassName) {
		this.styleClassName = styleClassName;
	}*/

}
