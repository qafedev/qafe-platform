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
package com.qualogy.qafe.core.application;


public class LoadFailedException extends RuntimeException {
	private static final long serialVersionUID = -5200144596044005225L;

	public LoadFailedException(String applicationName, String message) {
		super(createMessage(applicationName, message));
	}

	public LoadFailedException(String applicationName, Throwable cause) {
		this(applicationName, null, cause);
	}

	public LoadFailedException(String applicationName, String message, Throwable cause) {
		super(createMessage(applicationName, message), cause);
	}
	
	public LoadFailedException(String s) {
		super(s);
	}

	private static String createMessage(String applicationName, String message){
		return "Application: " + (applicationName==null ? "noname" : applicationName) + (message!=null? " - " + message: "");
	}
}
