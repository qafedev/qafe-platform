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
package com.qualogy.qafe.bind.presentation.component;

import java.io.Serializable;

public class Element  implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7386796024087034750L;
	protected int x;
	protected int y;
	protected int gridwidth;
	protected int gridheight;
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
	protected String styleClass;
	
	protected String style;
	
	protected Component component;
	
	public Element(){}
	
	public Element (int x, int y){
		this.x=x;
		this.y=y;
	}
	
	public Component getComponent() {
		return component;
	}
	public void setComponent(Component component) {
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
