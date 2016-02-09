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
package com.qualogy.qafe.gwt.client.vo.functions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.qualogy.qafe.gwt.client.vo.ui.event.ParameterGVO;

public abstract class AbstractSelectionItemGVO extends BuiltInFunctionGVO {

	private Map<Object, List<BuiltInFunctionGVO>> selectionItems = new HashMap<Object, List<BuiltInFunctionGVO>>();
	private ParameterGVO expression;
	
	/**
	 * 
	 * @param selectionCase
	 * @return
	 */
	public List<BuiltInFunctionGVO> getEventItems(Object selectionCase) {
		return selectionItems.get(selectionCase);
	}
	
	/**
	 * 
	 * @param selectionCase
	 * @param eventItem
	 */
	public void addEventItem(Object selectionCase, BuiltInFunctionGVO eventItem) {
		if (selectionItems.get(selectionCase) == null) {
			selectionItems.put(selectionCase, new ArrayList<BuiltInFunctionGVO>());
		}
		selectionItems.get(selectionCase).add(eventItem);
	}
	
	public Set<Object> getSelectionCases() {
		return selectionItems.keySet();
	}

	/**
	 * @return the expression
	 */
	public ParameterGVO getExpression() {
		return expression;
	}

	/**
	 * @param expression the expression to set
	 */
	public void setExpression(ParameterGVO expression) {
		this.expression = expression;
	}
}