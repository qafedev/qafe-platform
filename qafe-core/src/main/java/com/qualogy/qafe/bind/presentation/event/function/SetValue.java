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
package com.qualogy.qafe.bind.presentation.event.function;

import java.util.List;

import com.qualogy.qafe.bind.commons.type.Parameter;

/**
 * Builtinfunction to set value onto a component
 * @author rjankie
 */
public class SetValue extends BuiltInFunction{

	private static final long serialVersionUID = 3588738505465695150L;
	
	protected Parameter parameter;

	protected String componentId;
	
	protected String group;
	
	public String getGroup() {
		return group;
	}
	public void setGroup(String group) {
		this.group = group;
	}
	public final static String ACTION_SET="set";
	
	public final static String ACTION_ADD="add";
	
	
	protected String action=ACTION_SET;
	
	
	protected List<SetValueMapping> mapping; 
	
	private Object dataObject;
	
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public Object getDataObject() {
		return dataObject;
	}
	public void setDataObject(Object dataObject) {
		this.dataObject = dataObject;
	}
	public Parameter getParameter() {
		return parameter;
	}

	public String getComponentId() {
		return componentId;
	}
	public List<SetValueMapping> getMapping() {
		return mapping;
	}
	public void setMapping(List<SetValueMapping> mapping) {
		this.mapping = mapping;
	}
	public void setComponentId(String componentId) {
		this.componentId = componentId;
	}
	public void setParameter(Parameter parameter) {
		this.parameter = parameter;
	}
	
}
