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
package com.qualogy.qafe.mgwt.client.vo.ui.event;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.qualogy.qafe.mgwt.client.vo.functions.DataContainerGVO;

public class InputVariableGVO implements IsSerializable {

	private String name;
	private String reference;
	private String defaultValue;
	private String componentValue;
	//private Boolean isDataGrid=Boolean.FALSE;
	private DataContainerGVO dataContainerObject= null;
	
	
	public InputVariableGVO(){}
	
	public InputVariableGVO(String name, String reference, String defaultValue) {
		setName(name);
		setReference(reference);
		setDefaultValue(defaultValue);
		
	}
	
	public InputVariableGVO(String name, String reference, String defaultValue,String componentValue,DataContainerGVO dataContainerGVO) {
		this(name,reference,defaultValue);
		setComponentValue(componentValue);
		setDataContainerObject(dataContainerGVO);
		
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

//	public Boolean getIsDataGrid() {
//		return isDataGrid;
//	}
//
//	public void setIsDataGrid(Boolean isDataGrid) {
//		this.isDataGrid = isDataGrid;
//	}

	public InputVariableGVO getCopy(){
		InputVariableGVO inputVariableGVO = new InputVariableGVO();
		inputVariableGVO.setComponentValue(componentValue);
		inputVariableGVO.setDefaultValue(defaultValue);
	//	inputVariableGVO.setIsDataGrid(isDataGrid);
		inputVariableGVO.setName(name);
		inputVariableGVO.setReference(reference);
		return inputVariableGVO;
	}

	public DataContainerGVO getDataContainerObject() {
		return dataContainerObject;
	}

	public void setDataContainerObject(DataContainerGVO dataContainerObject) {
		this.dataContainerObject = dataContainerObject;
	}
}
