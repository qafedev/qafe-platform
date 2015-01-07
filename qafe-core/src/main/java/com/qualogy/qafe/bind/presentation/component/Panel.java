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
package com.qualogy.qafe.bind.presentation.component;

import java.util.ArrayList;
import java.util.List;

import com.qualogy.qafe.bind.presentation.layout.Layout;

/**
 * @author rjankie
 * The Panel class is the container 
 */
public class Panel extends Component implements HasComponents, HasLayout, HasVisibleText {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2191873699672485876L;
	
	protected Boolean showdatacontrol = Boolean.FALSE;
	protected Boolean disclosure = Boolean.FALSE;
	
	
	public Boolean getDisclosure() {
		return disclosure;
	}

	public void setDisclosure(Boolean disclosure) {
		this.disclosure = disclosure;
	}

	protected String displayname;
	protected String messageKey;
	
	/**
	 * 
	 */
	protected Layout layout;
	
	/**
	 * 
	 */
	protected List<Component> components;

	public Panel(Layout layout) {
		super();
		this.layout = layout;
	}

	public Panel() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return
	 */
	public List <Component>getComponents() {
		return components;
	}

	public void add(Component component){
		if(component==null)
			throw new IllegalArgumentException("component cannot be null");
		if(components==null)
			components = new ArrayList<Component>();
		components.add(component);
	}
	
	/**
	 * @deprecated use add
	 * @param components
	 */
	public void setComponents(List<Component> components) {
		this.components = components;
	}

	/**
	 * @return
	 */
	public Layout getLayout() {
		return layout;
	}

	/**
	 * @param layout
	 */
	public void setLayout(Layout layout) {
		this.layout = layout;
	}

	public Boolean getShowdatacontrol() {
		return showdatacontrol;
	}

	public void setShowdatacontrol(Boolean showdatacontrol) {
		this.showdatacontrol = showdatacontrol;
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
