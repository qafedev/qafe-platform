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
package com.qualogy.qafe.bind.presentation.layout;

import com.qualogy.qafe.bind.presentation.component.Component;


public class BorderLayout extends Layout{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4342691266069499979L;
	protected Component center;
	protected Component north;
	protected Component south;
	protected Component east;
	protected Component west;
	public Component getCenter() {
		return center;
	}
	public void setCenter(Component center) {
		this.center = center;
	}
	public Component getEast() {
		return east;
	}
	public void setEast(Component east) {
		this.east = east;
	}
	public Component getNorth() {
		return north;
	}
	public void setNorth(Component north) {
		this.north = north;
	}
	public Component getSouth() {
		return south;
	}
	public void setSouth(Component south) {
		this.south = south;
	}
	public Component getWest() {
		return west;
	}
	public void setWest(Component west) {
		this.west = west;
	}
	
	
}
