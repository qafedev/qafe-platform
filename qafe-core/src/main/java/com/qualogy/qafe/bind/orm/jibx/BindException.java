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
package com.qualogy.qafe.bind.orm.jibx;

/**
 * Exception thrown when postbinding fails, so after xsd validation and binding by the bindingframework
 * is done. BindException are thrown on documented xml rules.
 * @author 
 */
public class BindException extends RuntimeException {

	private static final long serialVersionUID = -4322004082879602506L;

	public BindException(String arg0) {
		super(arg0);
	}

	public BindException(Throwable arg0) {
		super(arg0);
	}

	public BindException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

}
