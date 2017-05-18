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
package com.qualogy.qafe.gwt.client.vo.functions;


public class FocusGVO extends BuiltInFunctionGVO {

	public final static String CLASS_NAME = "com.qualogy.qafe.gwt.client.vo.functions.FocusGVO";
	
	private String componentId;
	
	private BuiltInComponentGVO  builtInComponentGVO;

	public String getComponentId() {
		return componentId;
	}

	public void setComponentId(String componentId) {
		this.componentId = componentId;
	}

	public BuiltInComponentGVO getBuiltInComponentGVO() {
		return builtInComponentGVO;
	}

	public void setBuiltInComponentGVO(BuiltInComponentGVO builtInComponentGVO) {
		this.builtInComponentGVO = builtInComponentGVO;
	}

	@Override
	public String getClassName() {
		return CLASS_NAME;
	}
}