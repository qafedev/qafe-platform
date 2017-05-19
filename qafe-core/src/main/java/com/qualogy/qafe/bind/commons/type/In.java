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


public class In extends Parameter{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2639513404573618007L;

	public In(In otherParam) {
		super(otherParam);
	}

	public In(Reference ref) {
		super(ref);
	}

	public In(String name, Reference ref) {
		super(name, ref);
	}

	public In(String name, Value value) {
		super(name, value);
	}

	public In(String staticValue) {
		super(staticValue);
	}

	public In() {
		super();
	}

	public In(String name, Reference ref, Value value, AdapterMapping adapter) {
		super(name, ref, value, adapter);
	}

	/**
	 * Convinience method to get a reference key to the repository data
	 * for this parameter. In this case, if the actual ref is not set on
	 * the in object, than the name value is used to return. 
	 * @return
	 */
	public String stringValueOfReference() {
		return (this.getRef()!=null && this.getRef().toString()!=null)?this.getRef().toString():this.getName();
    }
    
	
}
