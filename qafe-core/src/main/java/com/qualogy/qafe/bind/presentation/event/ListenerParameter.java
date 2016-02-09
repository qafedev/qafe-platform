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
package com.qualogy.qafe.bind.presentation.event;

import java.io.Serializable;

/**
 * @author rjankie
 * This class can be registered to any Component type which needs to
 * handle click events.
 */
public class ListenerParameter implements Serializable {

	public final static String NAME_KEY = "key";
	
	public final static String KEY_ALT = "KEY_ALT"; 
	public final static String KEY_BACKSPACE = "KEY_BACKSPACE";
	public final static String KEY_CTRL = "KEY_CTRL";
	public final static String KEY_DELETE = "KEY_DELETE";
	public final static String KEY_DOWN = "KEY_DOWN";
	public final static String KEY_END = "KEY_END";
	public final static String KEY_ENTER = "KEY_ENTER";
	public final static String KEY_ESCAPE = "KEY_ESCAPE";
	public final static String KEY_HOME = "KEY_HOME";
	public final static String KEY_LEFT = "KEY_LEFT";
	public final static String KEY_PAGEDOWN = "KEY_PAGEDOWN";
	public final static String KEY_PAGEUP = "KEY_PAGEUP";
	public final static String KEY_RIGHT = "KEY_RIGHT";
	public final static String KEY_SHIFT = "KEY_SHIFT";
	public final static String KEY_TAB = "KEY_TAB";
	public final static String KEY_UP = "KEY_UP";
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6904299604937748247L;
	
	protected String name;
	protected String value;
	
	public ListenerParameter() {
		super();
	}
	public ListenerParameter(String name, String value) {
		super();
		this.name = name;
		this.value = value;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
}
