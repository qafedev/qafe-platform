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
 * The Panel class is the container 
 */
public class SplitPanel extends Component implements HasComponents{

	public SplitPanel() {
		components.add(first);
		components.add(second);
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 2311224532257132139L;
	
	public Component getFirst() {
		return first;
	}
	public void setFirst(Component first) {
		this.first = first;
	}
	public Component getSecond() {
		return second;
	}
	public void setSecond(Component second) {
		this.second = second;
	}
	protected Component first;
	protected Component second;
	protected String position = "50%";
	protected List<Component> components = new ArrayList<Component>();
	
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	protected Boolean horizontalOrientation=Boolean.TRUE;
	

	public Boolean getHorizontalOrientation() {
		return horizontalOrientation;
	}
	public void setHorizontalOrientation(Boolean horizontalOrientation) {
		this.horizontalOrientation = horizontalOrientation;
	}
	public List<? extends Component> getComponents() {
		return components; 
	}
	


	
}
