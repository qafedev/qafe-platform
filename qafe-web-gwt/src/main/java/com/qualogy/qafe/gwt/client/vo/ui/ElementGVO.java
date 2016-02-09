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
package com.qualogy.qafe.gwt.client.vo.ui;

import com.google.gwt.user.client.rpc.IsSerializable;



public class ElementGVO implements IsSerializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7386796024087034750L;
	private int x;
	private int y;
	private int gridwidth;
	private int gridheight;
	private String styleClass;
	public String getStyleClass() {
		return styleClass;
	}
	public void setStyleClass(String styleClass) {
		this.styleClass = styleClass;
	}
	public String getStyle() {
		return style;
	}
	public void setStyle(String style) {
		this.style = style;
	}
	private String style;
	
	private String[][] styleProperties;
	
	public String[][] getStyleProperties() {
		return styleProperties;
	}
	public void setStyleProperties(String[][] styleProperties) {
		this.styleProperties = styleProperties;
	}
	protected ComponentGVO component;
	
	public ComponentGVO getComponent() {
		return component;
	}
	public void setComponent(ComponentGVO component) {
		this.component = component;
	}
	public int getGridheight() {
		return gridheight;
	}
	public void setGridheight(int gridheight) {
		this.gridheight = gridheight;
	}
	public int getGridwidth() {
		return gridwidth;
	}
	public void setGridwidth(int gridwidth) {
		this.gridwidth = gridwidth;
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	
	
}
