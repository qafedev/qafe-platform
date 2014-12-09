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

import com.google.gwt.event.dom.client.HasBlurHandlers;
import com.google.gwt.event.dom.client.HasFocusHandlers;
import com.qualogy.qafe.mgwt.client.vo.ui.HasRequiredValidationMessage;

public interface IsTextField extends IsEditable, IsFocusable, HasDisplayname, HasConstraints, HasBlurHandlers, HasFocusHandlers, HasStyle, HasRequiredValidationMessage {
	
	public static final String TYPE_INTEGER					= "[0-9]+$";
	public static final String TYPE_SIGNED_INTEGER			= "[0-9]+$";
	public static final String TYPE_DOUBLE					= "^[-]?[0-9]*[\\.]?[0-9]*$";
	public static final String TYPE_CHARS					= "^[a-zA-Z]+$";
}
