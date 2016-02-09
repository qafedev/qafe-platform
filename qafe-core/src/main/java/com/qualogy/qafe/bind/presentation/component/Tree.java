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
package com.qualogy.qafe.bind.presentation.component;

import java.util.ArrayList;
import java.util.List;


/**
 * @author rjankie 
 */

public  class Tree extends Component implements HasComponents, HasVisibleText{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6593012547710578746L;
	/**
	 * 
	 */
	protected String displayname;
	
	protected String messageKey;
	

	
	/**
	 * 
	 */
	protected List<TreeItem> children = new ArrayList<TreeItem>();

	/**
	 * @return
	 */
	public List<TreeItem> getChildren() {
		return children;
	}

	/**
	 * @param children
	 */
	public void setChildren(List<TreeItem> children) {
		this.children = children;
	}

	

	
	/**
	 * @param treeItem
	 */
	public void add(TreeItem treeItem){
		if (treeItem !=null){
			getChildren().add(treeItem);
		} else {
			// throw NullPointerException
		}
	}
	
	/**
	 * @param treeItem
	 */
	public void remove(TreeItem treeItem){
		if (treeItem!=null){
			getChildren().remove(treeItem);
		} else {
			// throw NullPointerException
		}
	}

	public List<TreeItem> getComponents() {
		return getChildren();
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
	
	
}
