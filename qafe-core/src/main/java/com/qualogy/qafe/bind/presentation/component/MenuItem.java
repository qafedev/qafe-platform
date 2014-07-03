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
package com.qualogy.qafe.bind.presentation.component;

import java.util.ArrayList;
import java.util.List;


/**
 * @author rjankie 
 */

public  class MenuItem extends EditableComponent implements HasVisibleText, HasComponents{

	/**
	 * 
	 */
	
	private static final long serialVersionUID = 7873512492649230570L;

	/**
	 * 
	 */
	protected String shortcut;

	
	/**
	 * 
	 */
	protected String displayname;
	
	protected String messageKey;
	
	/**
	 * 
	 */
	protected List<MenuItem> subMenus = new ArrayList<MenuItem>();


	

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


	
	/**
	 * @param menuItem
	 */
	void add(MenuItem menuItem){
		if (menuItem!=null){
			getSubMenus().add(menuItem);
		} else {
			// throw NullPointerException
		}
	}
	
	/**
	 * @param menuItem
	 */
	void remove(MenuItem menuItem){
		if (menuItem!=null){
			getSubMenus().remove(menuItem);
		} else {
			// throw NullPointerException
		}
	}

	public List<MenuItem> getSubMenus() {
		return subMenus;
	}

	public void setSubMenus(List<MenuItem> subMenus) {
		this.subMenus = subMenus;
	}

	public String getDisplayname() {
		return displayname;
	}

	public void setDisplayname(String displayname) {
		this.displayname = displayname;
	}

	public String getMessageKey() {
		return messageKey;
	}

	public void setMessageKey(String messageKey) {
		this.messageKey = messageKey;
	}

	public List<? extends Component> getComponents() {
		return getSubMenus();
	}
	
}
