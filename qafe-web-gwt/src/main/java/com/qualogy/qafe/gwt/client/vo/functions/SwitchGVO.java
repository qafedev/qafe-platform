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
package com.qualogy.qafe.gwt.client.vo.functions;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class SwitchGVO extends AbstractSelectionItemGVO {

	public final static String CLASS_NAME = "com.qualogy.qafe.gwt.client.vo.functions.SwitchGVO";
	public final static String DEFAULT_SELECTION = "defaultSelection";
		
	@Override
	public Set<Object> getSelectionCases() {
		Set<Object> selectionCases = new HashSet<Object>(super.getSelectionCases());
		if (selectionCases.contains(DEFAULT_SELECTION)) {
			selectionCases.remove(DEFAULT_SELECTION);
		}
		return selectionCases;
	}
	
	public List<BuiltInFunctionGVO> getDefaultEventItems() {
		return getEventItems(DEFAULT_SELECTION);
	}
	
	/**
	 * 
	 */
	@Override
	public String getClassName() {
		return CLASS_NAME;
	}

}
