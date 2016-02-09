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
package com.qualogy.qafe.gwt.client.vo.ui;



/**
 * @author rjankie 
 */

public  class MenuItemGVO extends EditableComponentGVO  implements HasParent, HasVisibleTextI{

	/**
	 * 
	 */
	
	private static final long serialVersionUID = 7873512492649230570L;

	/**
	 * 
	 */
	private String shortcut;

	/**
	 * 
	 */
	private String displayname;
	
	private String parent=null;
	/**
	 * 
	 */
	private MenuItemGVO[] subMenus =null;
	
	private String[] modifiers;
	
	private String   key;
	
	

	

	/**
	 * @return
	 */
	public String getShortcut() {
		return shortcut;
	}

	/**
	 * @param shortcut
	 */
	public void setShortcut(String shortcut) {
		this.shortcut = shortcut;
	}

	public MenuItemGVO[] getSubMenus() {
		return subMenus;
	}

	public void setSubMenus(MenuItemGVO[] subMenus) {
		this.subMenus = subMenus;
	}

	public String getClassName() {
		return "com.qualogy.qafe.gwt.client.vo.ui.MenuItemGVO";
	}

	

	public String getParent() {
		return parent;
	}

	public boolean hasParent() {
		return (parent!=null);
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String[] getModifiers() {
		return modifiers;
	}

	public void setModifiers(String[] modifiers) {
		this.modifiers = modifiers;
	}

	public String getDisplayname() {
		return displayname;
	}

	public void setDisplayname(String displayname) {
		this.displayname = displayname;
	}
}
