/**
 * Copyright 2008-2015 Qualogy Solutions B.V.
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

public class PasswordTextFieldGVO extends TextFieldGVO{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1364273197572324327L;
	/**
	 * 
	 */
	protected String passwordMask;

	/**
	 * @return
	 */
	public String getPasswordMask() {
		return passwordMask;
	}

	/**
	 * @param passwordMask
	 */
	public void setPasswordMask(String passwordMask) {
		this.passwordMask = passwordMask;
	}
	
	public String getClassName() {
		return "com.qualogy.qafe.gwt.client.vo.ui.PasswordTextFieldGVO";
	}
	public String getStyleName() {
		return "password";
	}
}
