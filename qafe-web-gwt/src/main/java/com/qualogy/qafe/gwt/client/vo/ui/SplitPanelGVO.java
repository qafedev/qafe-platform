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
package com.qualogy.qafe.gwt.client.vo.ui;


/**
 * @author rjankie
 * The Panel class is the container 
 */
public class SplitPanelGVO extends ComponentGVO implements HasComponentsI{

		
	private ComponentGVO first;
	
	public ComponentGVO getFirst() {
		return first;
	}

	public void setFirst(ComponentGVO first) {
		this.first = first;
	}

	public ComponentGVO getSecond() {
		return second;
	}

	public void setSecond(ComponentGVO second) {
		this.second = second;
	}

	private ComponentGVO second;
	
	private Boolean horizontalOrientation=Boolean.TRUE;
	
	private String position;
	


	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	
	

	public Boolean getHorizontalOrientation() {
		return horizontalOrientation;
	}

	public void setHorizontalOrientation(Boolean horizontalOrientation) {
		this.horizontalOrientation = horizontalOrientation;
	}

	

	public String getClassName() {
		return "com.qualogy.qafe.gwt.client.vo.ui.SplitPanelGVO";
	}

	public ComponentGVO[] getComponents() {
		return new ComponentGVO[]{first,second};
	}




}
