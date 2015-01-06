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
/**
 * 
 */
package com.qualogy.qafe.gwt.client.vo.ui;

/**
 * @author rjankie
 */

public class DropDownItemGVO extends ComponentGVO implements HasVisibleTextI{


	/**
	 * 
	 */
	private static final long serialVersionUID = -6800035872162120639L;

	/**
	 * 
	 */
	protected String value;

	/**
	 * 
	 */
	protected String displayname;
	
	protected Boolean selected = Boolean.FALSE;

	



	/**
	 * @return
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param value
	 */
	public void setValue(String value) {
		this.value = value;
	}
	public String getClassName() {
		return "com.qualogy.qafe.gwt.client.vo.ui.DropDownItemGVO";
	}

	public Boolean getSelected() {
		return selected;
	}

	public void setSelected(Boolean selected) {
		this.selected = selected;
	}



	public String getDisplayname() {
		return displayname;
	}

	public void setDisplayname(String displayname) {
		this.displayname = displayname;
	}
}
