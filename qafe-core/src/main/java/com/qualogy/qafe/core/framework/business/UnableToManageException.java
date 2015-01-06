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
package com.qualogy.qafe.core.framework.business;

/**
 * Exception thrown when due to a technical reason in Qafe or a
 * mapping file an action cannot be processed.
 * @author Marc van der Wurff
 *
 */
public class UnableToManageException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1432412937874810646L;

	private UnableToManageException() {
	}

	public UnableToManageException(String message) {
		super(message);
	}

	public UnableToManageException(Throwable cause) {
		super(cause);
	}

	public UnableToManageException(String message, Throwable cause) {
		super(message, cause);
	}

}
