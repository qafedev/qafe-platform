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
package com.qualogy.qafe.mgwt.client.vo.functions;

import com.qualogy.qafe.mgwt.client.vo.ui.ComponentGVO;


public class SetPanelGVO extends BuiltInFunctionGVO {

	private BuiltInComponentGVO builtInComponentGVO;
	private String target;
	private ComponentGVO src;

	
	public BuiltInComponentGVO getBuiltInComponentGVO() {
		return builtInComponentGVO;
	}


	public void setBuiltInComponentGVO(BuiltInComponentGVO builtInComponentGVO) {
		this.builtInComponentGVO = builtInComponentGVO;
	}


	public String getTarget() {
		return target;
	}


	public void setTarget(String target) {
		this.target = target;
	}


	public ComponentGVO getSrc() {
		return src;
	}


	public void setSrc(ComponentGVO src) {
		this.src = src;
	}




	public String getClassName() {
		return "com.qualogy.qafe.gwt.client.vo.functions.SetPanelGVO";
	}

	

	


}
