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
package com.qualogy.qafe.mgwt.client.ui.component;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Widget;

public class QMapArea extends Widget {

	private static final String AREA 			= "area";
	private static final String AREA_ALT 		= "alt";
	private static final String AREA_COORDS 	= "coords";
	private static final String AREA_SHAPE 		= "shape";
	
	private String alt;
	private String coords;
	private String shape;
	
	public QMapArea() {
		setElement(DOM.createElement(AREA));
	}
	
	public String getAlt() {
		return alt;
	}
	
	public void setAlt(String alt) {
		this.alt = alt;
		if (alt == null) {
			alt = "";
		}
		DOM.setElementProperty(getElement(), AREA_ALT, alt);
	}

	public String getCoords() {
		return coords;
	}
	
	public void setCoords(String coords) {
		this.coords = coords;
		if (coords == null) {
			coords = "";
		}
		DOM.setElementProperty(getElement(), AREA_COORDS, coords);
	}

	public String getShape() {
		return shape;
	}
	
	public void setShape(String shape) {
		this.shape = shape;
		if (shape == null) {
			shape = "";
		}
		DOM.setElementProperty(getElement(), AREA_SHAPE, shape);
	}
}