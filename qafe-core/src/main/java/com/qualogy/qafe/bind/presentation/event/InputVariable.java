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
package com.qualogy.qafe.bind.presentation.event;

import java.io.Serializable;

public class InputVariable implements Serializable {

	private String name;
	private String reference;
	private String defaultValue;
	private String componentValue;
	private Object dataObject;
	
	
	public InputVariable(String name, String reference, String defaultValue) {
		setName(name);
		setReference(reference);
		setDefaultValue(defaultValue);
		
	}

	public InputVariable(String name, String reference, String defaultValue,String componentValue) {
		this(name,reference,defaultValue);
		setComponentValue(componentValue);
		
	}
	
	public InputVariable(String name, String reference, String defaultValue,String componentValue,Object dataObject) {
		this(name,reference,defaultValue,componentValue);
		setDataObject(dataObject);
		
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 4524221411795992766L;

	public String getComponentValue() {
		return componentValue;
	}

	public void setComponentValue(String componentValue) {
		this.componentValue = componentValue;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public Object getDataObject() {
		return dataObject;
	}

	public void setDataObject(Object dataObject) {
		this.dataObject = dataObject;
	}

}
