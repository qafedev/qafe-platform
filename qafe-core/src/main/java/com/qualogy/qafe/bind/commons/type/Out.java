/**
 * Copyright 2008-2017 Qualogy Solutions B.V.
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
package com.qualogy.qafe.bind.commons.type;


public class Out extends Parameter{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8765016087161997815L;

	public Out() {
		super();
	}

	public Out(Out otherOut) {
		super(otherOut);
	}
	
	public Out(String name, Reference ref, Value value, AdapterMapping adapter) {
		super(name, ref, value, adapter);
	}

}
