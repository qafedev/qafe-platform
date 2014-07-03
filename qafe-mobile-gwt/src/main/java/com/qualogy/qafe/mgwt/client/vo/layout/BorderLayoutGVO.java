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
package com.qualogy.qafe.mgwt.client.vo.layout;

import com.qualogy.qafe.mgwt.client.vo.ui.ComponentGVO;


public class BorderLayoutGVO extends LayoutGVO{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3985495297701052727L;
	
	private ComponentGVO south;
	private ComponentGVO north;
	private ComponentGVO center;
	private ComponentGVO west;
	private ComponentGVO east;
	public ComponentGVO getCenter() {
		return center;
	}
	public void setCenter(ComponentGVO center) {
		this.center = center;
	}
	public ComponentGVO getEast() {
		return east;
	}
	public void setEast(ComponentGVO east) {
		this.east = east;
	}
	public ComponentGVO getNorth() {
		return north;
	}
	public void setNorth(ComponentGVO north) {
		this.north = north;
	}
	public ComponentGVO getSouth() {
		return south;
	}
	public void setSouth(ComponentGVO south) {
		this.south = south;
	}
	public ComponentGVO getWest() {
		return west;
	}
	public void setWest(ComponentGVO west) {
		this.west = west;
	}
	
}
