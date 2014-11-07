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
package com.qualogy.qafe.mgwt.client.vo.functions;

import java.util.List;

public class ChangeStyleGVO extends BuiltInFunctionGVO {

	
	private List<ChangeStyleActionGVO> actions;

	

	public String getClassName() {
		return "com.qualogy.qafe.gwt.client.vo.functions.ChangeStyleGVO";
	}

	public List<ChangeStyleActionGVO> getActions() {
		return actions;
	}

	public void setActions(List<ChangeStyleActionGVO> actions) {
		this.actions = actions;
	}

}
