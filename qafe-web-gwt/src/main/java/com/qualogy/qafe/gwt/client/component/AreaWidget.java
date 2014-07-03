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
package com.qualogy.qafe.gwt.client.component;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.UIObject;

public class AreaWidget extends UIObject {
	private Command command;

	public AreaWidget() {
		setElement(DOM.createElement("area"));
		DOM.setElementAttribute(getElement(), "href", "#");
	}

	public AreaWidget(String shape, String coords, String alt, Command command) {
		this();
		setShape(shape);
		setAlt(alt);
		setCoords(coords);
		setTitle(alt);
		this.command = command;
	}

	Command getCommand() {
		return command;
	}

	void setAlt(String alt) {
		DOM.setElementProperty(getElement(), "alt", (alt == null) ? "area"
				: alt);
	}

	void setCoords(String coords) {
		DOM.setElementProperty(getElement(), "coords", (coords == null) ? ""
				: coords);
	}

	void setShape(String shape) {
		DOM.setElementProperty(getElement(), "shape", (shape == null) ? ""
				: shape);
	}



	public void setCommand(Command command) {
		this.command = command;
	}

}