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
package com.qualogy.qafe.business.integration.builder;

public class UnableToBuildException extends RuntimeException {

	public UnableToBuildException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public UnableToBuildException(String arg0) {
		super(arg0);
	}

	public UnableToBuildException(Throwable arg0) {
		super(arg0);

	}

	private static final long serialVersionUID = -2310250455083999926L;

}
