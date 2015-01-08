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
package com.qualogy.qafe.core.errorhandling;

/**
 * Exception that can be thrown from any external (user defined) resource.
 * Errorhandling mechnism acts upon this exception.
 * 
 * @author 
 */
public class ExternalException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7455149670874287725L;
	
	private String errorMessage;

	public ExternalException(Throwable cause) {
		this(null, null, cause);
	}
	
	public ExternalException(String message, Throwable cause) {
		this(message, null, cause);
	}
	
	public ExternalException(String message, String errorMessage, Throwable cause) {
		super(message, cause);
		this.errorMessage = errorMessage;
	}

	public String getErrorMessage() {
		return errorMessage;
	}
}
