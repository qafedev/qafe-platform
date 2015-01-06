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
package com.qualogy.qafe.bind.commons.type;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;
/**
 * Value description, sets the outline of a value type
 * @author 
 *
 */
public class Value implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1566377637193489873L;
	protected TypeDefinition type;
	/**
	 * fully qualified class name
	 */
	protected String outputClass;
    
    protected String staticValue;
	
    public Value(TypeDefinition type, String staticValue, String outputClass) {
		super();
		this.type = type;
		this.staticValue = staticValue;
		this.outputClass = outputClass;
	}
    
	public Value(TypeDefinition type, String staticValue) {
		super();
		this.type = type;
		this.staticValue = staticValue;
	}
	
	public Value(String staticValue) {
		super();
		this.staticValue = staticValue;
	}
	public Value() {
		super();
	}
	protected String getOutputClass() {
		return outputClass;
	}
	public TypeDefinition getType() {
		return type;
	}
	public void setType(TypeDefinition type) {
		this.type = type;
	}
	public String toString(){
		return ToStringBuilder.reflectionToString(this);
	}
	public String getStaticValue() {
		return staticValue;
	}
	public void setStaticValue(String staticValue) {
		this.staticValue = staticValue;
	}
}
