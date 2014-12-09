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
package com.qualogy.qafe.mgwt.client.ui.renderer.events.exception;

public class RequiredFieldException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4499642624084707369L;
	private String title;

	public RequiredFieldException() {
	}
	
	public RequiredFieldException(String message) {
		this(null, message, null);
	}
	
	public RequiredFieldException(Throwable cause) {
		this(null, null, cause);
	}

	public RequiredFieldException(String message, Throwable cause) {
		this(null, message, cause);
	}
	
	public RequiredFieldException(String title, String message) {
		this(title, message, null);
	}
	
	public RequiredFieldException(String title, String message, Throwable cause) {
		super(message, cause);
		this.title = title;
	}

	public String getTitle() {
		return title;
	}

}
