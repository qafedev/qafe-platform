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
package com.qualogy.qafe.bind.presentation.component;


/**
 * @author rjankie
 * The Panel class is the container 
 */
public class DatagridControlBar extends Panel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2478563698399351531L;
	
	public static final String PLACEMENT_TOP = "top";
	public static final String PLACEMENT_LEFT = "left";
	public static final String PLACEMENT_BOTTOM = "bottom";
	public static final String PLACEMENT_RIGHT = "right";
	
	protected String placement = PLACEMENT_BOTTOM;

	public String getPlacement() {
		return placement;
	}

	public void setPlacement(String placement) {
		this.placement = placement;
	}
}
