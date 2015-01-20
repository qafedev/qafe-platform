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
package com.qualogy.qafe.gwt.client.vo.handlers;

import java.util.Collection;
import java.util.Map;
import java.util.Queue;

import com.google.gwt.user.client.ui.UIObject;
import com.qualogy.qafe.gwt.client.vo.functions.BuiltInFunctionGVO;
import com.qualogy.qafe.gwt.client.vo.functions.IterationGVO;
import com.qualogy.qafe.gwt.client.vo.ui.event.ParameterGVO;

public class IterationHandler extends AbstractBuiltInHandler {

	@Override
	protected BuiltInState executeBuiltIn(UIObject sender, String listenerType
			, Map<String, String> mouseInfo, BuiltInFunctionGVO builtInGVO
			, String appId, String windowId, String eventSessionId, Queue derivedBuiltIns) {
		IterationGVO iterationGVO = (IterationGVO) builtInGVO;
		handleIteration(iterationGVO, sender, appId, windowId, eventSessionId, derivedBuiltIns);
		if (derivedBuiltIns.isEmpty()) {
			return BuiltInState.EXECUTED;
		}
		return BuiltInState.REPEAT;
	}
	
	private void handleIteration(IterationGVO iterationGVO, UIObject sender, String appId
			, String windowId, String eventSessionId, Queue derivedBuiltIns) {
		ParameterGVO referenceGVO = iterationGVO.getReference();
		Object value = getValue(sender, referenceGVO, appId, windowId, eventSessionId);
		Object[] array = resolveArray(value);
		if (array == null) {
			return;
		}		
		int varIndexValue = resolveVarIndexValue(iterationGVO, eventSessionId);
		if (varIndexValue < 0) {
			return;
		}
		int endIndex = iterationGVO.getEnd();
		if (endIndex < 0) {
			endIndex = array.length - 1;
		} else {
			endIndex = Math.min(endIndex, array.length - 1);
		}
		if (varIndexValue > endIndex) {
			return;
		}
		
		Collection<BuiltInFunctionGVO> eventItems = iterationGVO.getEventItems();
		if (eventItems != null) {
			derivedBuiltIns.addAll(eventItems);
		}
		store(iterationGVO, array, varIndexValue, eventSessionId);
	}
	
	private Object[] resolveArray(Object value) {
		if (value instanceof Object[]) {
			return (Object[])value;
		}
		if (value instanceof Collection) {
			return ((Collection<?>)value).toArray();
		}
		return null;
	}
	
	private String resolveVarIndex(IterationGVO iterationGVO) {
		String varIndex = iterationGVO.getVarIndex();
		if ((varIndex == null) || (varIndex.length() == 0)) {
			String builtInShortenName = getShortenName(iterationGVO);
			varIndex = "indexOf" + builtInShortenName;
		}
		return varIndex;
	}
	
	private int resolveVarIndexValue(IterationGVO iterationGVO, String eventSessionId) {
		String varIndex = resolveVarIndex(iterationGVO);
		Object varIndexValue = getData(eventSessionId, varIndex);
		if (varIndexValue == null) {
			int beginIndex = iterationGVO.getBegin();
			if (beginIndex < 0) {
				beginIndex = 0;
			}
			return beginIndex;
		}
		if (varIndexValue instanceof Integer) {
			int increment = iterationGVO.getIncrement();
			if (increment <= 0) {
				increment = 1;
			}
			return (Integer) varIndexValue + increment;
		}		
		return -1;
	}

	private void store(IterationGVO iterationGVO, Object[] array, int varIndexValue, String eventSessionId) {
		String var = iterationGVO.getVar();
		if (var != null) {
			Object varValue = array[varIndexValue];
			storeData(eventSessionId, var, varValue);
		}
		
		String varIndex = resolveVarIndex(iterationGVO);
		storeData(eventSessionId, varIndex, varIndexValue);
	}
}