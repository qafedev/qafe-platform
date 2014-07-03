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
package com.qualogy.qafe.bind.presentation.event;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author rjankie
 * This class can be registered to any Component type which needs to
 * handle click events.
 */
public class Listener implements Serializable {
	
	public final static String EVENT_ONENTER="onenter";
	/**
	 * On Exit event from Textfield
	 */
	public final static String EVENT_ONEXIT = "onexit";

	/**
	 * Loading of an image is interrupted
	 */
	public final static String EVENT_ONABORT = "onabort";

	/**
	 * An element loses focus
	 */
	public final static String EVENT_ONBLUR = "onblur";

	/**
	 * The user changes the content of a field
	 */
	public final static String EVENT_ONCHANGE = "onchange";

	/**
	 * Mouse clicks an object
	 */
	public final static String EVENT_ONCLICK = "onclick";

	/**
	 * Mouse double-clicks an object
	 */
	public final static String EVENT_ONDBLCLICK = "ondblclick";

	/**
	 * An error occurs when loading a document or an image
	 */
	public final static String EVENT_ONERROR = "onerror";
	
	public final static String EVENT_ONFINISH = "onfinish";

	/**
	 * An element gets focus
	 */
	public final static String EVENT_ONFOCUS = "onfocus";

	/**
	 * A keyboard key is pressed
	 */
	public final static String EVENT_ONKEYDOWN = "onkeydown";

	/**
	 * A keyboard key is pressed or held down
	 */
	public final static String EVENT_ONKEYPRESS = "onkeypress";

	/**
	 * A keyboard key is released
	 */
	public final static String EVENT_ONKEYUP = "onkeyup";

	/**
	 * A page or an image is finished loading
	 */

	public final static String EVENT_ONLOAD = "onload";

	/**
	 * A mouse button is pressed
	 */
	public final static String EVENT_ON_MOUSE_DOWN = "onmouse-down";

	/**
	 * The mouse is moved (ie 6)
	 */
	public final static String EVENT_ON_MOUSE_MOVE = "onmouse-move";

	/**
	 * The mouse is moved off an element
	 */
	public final static String EVENT_ON_MOUSE_EXIT = "onmouse-exit";

	/**
	 * The mouse is moved over an element
	 */
	public final static String EVENT_ON_MOUSE_ENTER = "onmouse-enter";

	/**
	 * A mouse button is released
	 */
	public final static String EVENT_ON_MOUSE_UP = "onmouse-up";

	/**
	 * The reset button is clicked
	 */
	public final static String EVENT_ONRESET = "onreset";

	/**
	 * A window or frame is resized
	 */

	public final static String EVENT_ONRESIZE = "onresize";

	/**
	 * Text is selected
	 */
	public final static String EVENT_ONSELECT = "onselect";
	
	/**
	 * on scheduled time
	 */
	public final static String EVENT_ONTIMER = "ontimer";

	
	/**
	 * The user exits the page
	 */

	public final static String EVENT_ONUNLOAD = "onunload";
	
	private static final long serialVersionUID = 561325110588595100L;
	
	public static final String EVENT_ONFETCHDATA = "onfetchdata";
	
	public static final String EVENT_ONSCROLL_TOP = "onscroll-top";
	
	public static final String EVENT_ONSCROLL_BOTTOM = "onscroll-bottom";

	protected String type;
	
	protected List<ListenerParameter> parameters = new ArrayList<ListenerParameter>();
	
	public List<ListenerParameter> getParameters() {
		return parameters;
	}

	public void setParameters(List<ListenerParameter> parameters) {
		this.parameters = parameters;
	}

	public Listener() {
	    super();
	}
	
	public Listener(final String type) {
	    this();
	    this.type = type;
	}
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	public void add(ListenerParameter param){
		if(param==null)
			throw new IllegalArgumentException("param cannot be null");
		if(parameters==null)
			parameters = new ArrayList<ListenerParameter>();
		
		parameters.add(param);
	}

}
