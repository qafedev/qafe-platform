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

public  class TreeItemGVO extends ComponentGVO implements HasComponentsI, HasVisibleTextI{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7433436420915349822L;
	/**
	 * 
	 */
	protected String displayname;
	/**
	 * 
	 */
	protected String value;
	
	protected Boolean expand=Boolean.FALSE;

	protected ComponentGVO component;
	
	/**
	 * 
	 */
	protected TreeItemGVO[] children = null;

	


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
	
	

	/**
	 * @return
	 */

	public TreeItemGVO[] getChildren() {
		return children;
	}
	
	public ComponentGVO[] getComponents() {
		return getChildren();
	
	}

	public void setChildren(TreeItemGVO[] children) {
		this.children = children;
	}
	

	public String getClassName() {
		return "com.qualogy.qafe.gwt.client.vo.ui.TreeItemGVO";
	}



	public ComponentGVO getComponent() {
		return component;
	}

	public void setComponent(ComponentGVO component) {
		this.component = component;
	}

	public Boolean getExpand() {
		return expand;
	}

	public void setExpand(Boolean expand) {
		this.expand = expand;
	}

	public String getDisplayname() {
		return displayname;
	}

	public void setDisplayname(String displayname) {
		this.displayname = displayname;
	}


}
