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
package com.qualogy.qafe.bind.core.messages;

import com.qualogy.qafe.bind.commons.type.AdapterMapping;
import com.qualogy.qafe.bind.commons.type.Parameter;
import com.qualogy.qafe.bind.commons.type.Reference;
import com.qualogy.qafe.bind.commons.type.Value;

public class PlaceHolder extends Parameter{
	
	private static final long serialVersionUID = 2494629486378153995L;
	public final static String PREFIX = "${";
	public final static String POSTFIX = "}";
	
	public PlaceHolder() {
		super();
	}

	public PlaceHolder(Parameter otherParam) {
		super(otherParam);
	}

	public PlaceHolder(Reference ref) {
		super(ref);
	}

	public PlaceHolder(String name, Reference ref, Value value, AdapterMapping adapter) {
		super(name, ref, value, adapter);
	}

	public PlaceHolder(String name, Reference ref) {
		super(name, ref);
	}

	public PlaceHolder(String name, Value value) {
		super(name, value);
	}

	public PlaceHolder(String staticValue) {
		super(staticValue);
	}

	public String getPlaceHolderKey(){
		return PREFIX + name + POSTFIX;
	}
	
	public Object clone() throws CloneNotSupportedException {
		 PlaceHolder clone = new PlaceHolder();
		 clone.adapter = this.adapter;			
		 clone.name = this.name;
		 clone.ref = (Reference) ((this.ref == null) ? null : this.ref.clone());
		 clone.value = this.value;		
		 clone.expression = this.expression;			
		 clone.nullable = this.nullable;
		 return clone;
	
	 }
}
