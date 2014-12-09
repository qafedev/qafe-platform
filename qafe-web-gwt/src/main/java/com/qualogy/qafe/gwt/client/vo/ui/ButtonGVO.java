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
package com.qualogy.qafe.gwt.client.vo.ui;

/**
 * @author rjankie
 */

public class ButtonGVO extends ComponentGVO implements HasVisibleTextI {

	private static final long serialVersionUID = -5332198195301252433L;
	protected String label;
	protected String imageLocation;
	protected String displayname;


	public String getImageLocation() {
		return imageLocation;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getClassName() {
		return "com.qualogy.qafe.gwt.client.vo.ui.ButtonGVO";
	}

	public void setImageLocation(String imageLocation) {
		this.imageLocation = imageLocation;
		
	}

	public String getDisplayname() {
		return displayname;
	}

	public void setDisplayname(String displayname) {
		this.displayname = displayname;
	}
	
	
}
