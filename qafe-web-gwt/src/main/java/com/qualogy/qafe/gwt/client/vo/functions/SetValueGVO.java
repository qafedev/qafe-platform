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
package com.qualogy.qafe.gwt.client.vo.functions;

import java.util.Map;

import com.qualogy.qafe.gwt.client.vo.ui.event.ParameterGVO;


public class SetValueGVO extends BuiltInFunctionGVO {

    public final static String CLASS_NAME = "com.qualogy.qafe.gwt.client.vo.functions.SetValueGVO";
    
    public final static String ACTION_SET = "set";
    public final static String ACTION_ADD = "add";

    private BuiltInComponentGVO builtInComponentGVO;
	private String componentId;
	private String group;
	private String namedComponentId;
	private DataContainerGVO dataContainer;
    private Map<String,String> mapping;
	private String data;
	private String value;
	private String action = ACTION_SET;
	private ParameterGVO parameter;
	
	public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}
	
	public Map<String,String> getMapping() {
		return mapping;
	}

	public void setMapping(Map<String,String> mapping) {
		this.mapping = mapping;
	}
	
	public String getComponentId() {
		return componentId;
	}

	public void setComponentId(String componentId) {
		this.componentId = componentId;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public BuiltInComponentGVO getBuiltInComponentGVO() {
		return builtInComponentGVO;
	}

	public void setBuiltInComponentGVO(BuiltInComponentGVO builtInComponentGVO) {
		this.builtInComponentGVO = builtInComponentGVO;
	}

	public String getNamedComponentId() {
		return namedComponentId;
	}

	public void setNamedComponentId(String namedComponentId) {
		this.namedComponentId = namedComponentId;
	}

	public DataContainerGVO getDataContainer() {
		return dataContainer;
	}

	public void setDataContainer(DataContainerGVO dataContainer) {
		this.dataContainer = dataContainer;
	}

	public ParameterGVO getParameter() {
        return parameter;
    }

    public void setParameter(ParameterGVO parameter) {
        this.parameter = parameter;
    }
    
    public String getClassName() {
        return CLASS_NAME;
    }
}