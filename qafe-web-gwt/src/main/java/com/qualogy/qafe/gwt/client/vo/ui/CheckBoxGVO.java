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
package com.qualogy.qafe.gwt.client.vo.ui;

/**
 * @author rjankie
 */

public class CheckBoxGVO extends EditableComponentGVO implements
		HasVisibleTextI {

	private static final long serialVersionUID = 5618002118711850699L;
	protected Boolean value;
	protected String displayname;
	//private String styleClassName = "qafe_checkbox";
	public final static String UNCHECKED_VALUE_ATTRIBUTE_TAG = "unchecked-value";
	public final static String CHECKED_VALUE_ATTRIBUTE_TAG = "checked-value";
	public final static String UNCHECKED_VALUE_DOMAIN_ATTRIBUTE_TAG = "unchecked-value-domain";
	public final static String CHECKED_VALUE_DOMAIN_ATTRIBUTE_TAG = "checked-value-domain";
	private String unCheckedValue;
	private String checkedValue;
	public String[] checkedValueDomain = null;
	public String[] unCheckedValueDomain = null;
	private String displaynamePosition;

	public Boolean getValue() {
		return value;
	}

	public void setValue(Boolean value) {
		this.value = value;
	}

	public String getClassName() {
		return "com.qualogy.qafe.gwt.client.vo.ui.CheckBoxGVO";
	}

	public String getCheckedValue() {
		return checkedValue;
	}

	public void setCheckedValue(String checkedValue) {
		this.checkedValue = checkedValue;
	}

	public String getUnCheckedValue() {
		return unCheckedValue;
	}

	public void setUnCheckedValue(String unCheckedValue) {
		this.unCheckedValue = unCheckedValue;
	}

	public String[] getCheckedValueDomain() {
		return checkedValueDomain;
	}

	public void setCheckedValueDomain(String[] checkedValueDomain) {
		this.checkedValueDomain = checkedValueDomain;
	}

	public String[] getUnCheckedValueDomain() {
		return unCheckedValueDomain;
	}

	public void setUnCheckedValueDomain(String[] unCheckedValueDomain) {
		this.unCheckedValueDomain = unCheckedValueDomain;
	}

	public String getDisplayname() {
		return displayname;
	}

	public void setDisplayname(String displayname) {
		this.displayname = displayname;
	}

	public String getDisplaynamePosition() {
		return displaynamePosition;
	}
	
	public void setDisplaynamePosition(String displaynamePosition) {
		this.displaynamePosition = displaynamePosition;
	}

	/*public String getStyleClassName() {
		return styleClassName;
	}

	public void setStyleClassName(String styleClassName) {
		this.styleClassName = styleClassName;
	}*/
}