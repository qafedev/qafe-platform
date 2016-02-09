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
package com.qualogy.qafe.gwt.server.event.assembler;

import java.util.List;

import com.qualogy.qafe.bind.commons.type.Parameter;
import com.qualogy.qafe.bind.core.application.ApplicationContext;
import com.qualogy.qafe.bind.core.statement.ResultItem;
import com.qualogy.qafe.bind.core.statement.SwitchResult;
import com.qualogy.qafe.bind.core.statement.SwitchResults;
import com.qualogy.qafe.bind.core.statement.SwitchStatement;
import com.qualogy.qafe.bind.presentation.event.Event;
import com.qualogy.qafe.bind.presentation.event.EventItem;
import com.qualogy.qafe.gwt.client.vo.functions.BuiltInFunctionGVO;
import com.qualogy.qafe.gwt.client.vo.functions.SwitchGVO;
import com.qualogy.qafe.gwt.client.vo.ui.event.ParameterGVO;

public class SwitchAssembler extends AbstractEventItemAssembler {

	/**
	 * 
	 */
	@Override
	public BuiltInFunctionGVO assemble(EventItem eventItem, Event event, ApplicationContext applicationContext) {
		SwitchGVO eventItemGVO = null;
		if(eventItem instanceof SwitchStatement) {
			eventItemGVO = new SwitchGVO();
			assembleAttributes(eventItemGVO, (SwitchStatement)eventItem, event, applicationContext);
		}
		return eventItemGVO;
	}
	
	/**
	 * 
	 * @param eventItemGVO
	 * @param eventItem
	 * @param event
	 * @param applicationContext
	 */
	private void assembleAttributes(SwitchGVO eventItemGVO, SwitchStatement eventItem, Event event, ApplicationContext applicationContext) {
		ParameterGVO expressionGVO = assembleParameter(eventItem.getExpression());
		eventItemGVO.setExpression(expressionGVO);
		SwitchResults results = eventItem.getResults();
		if (results == null) {
		    return;
		}
		
		List<SwitchResult> switchResults = results.getResults();
		if (switchResults != null) {
		    for (SwitchResult result : switchResults) {
	            assembleChildren(eventItemGVO, result.getValue(), result.getResultItems(), event, applicationContext);
	        }    
		}
		
		List<ResultItem> defaultResultItems = results.getDefaultResult();
		assembleChildren(eventItemGVO, SwitchGVO.DEFAULT_SELECTION, defaultResultItems, event, applicationContext);
    }
	
	/**
	 * 
	 * @param eventItemGVO
	 * @param selectionCase
	 * @param result
	 * @param event
	 * @param applicationContext
	 */
	private void assembleChildren(SwitchGVO eventItemGVO, Parameter selectionCase, List<ResultItem> resultItems, Event event, ApplicationContext applicationContext) {
		ParameterGVO selectionCaseGVO = assembleParameter(selectionCase);
		assembleChildren(eventItemGVO, selectionCaseGVO, resultItems, event, applicationContext);
	}
	
	/**
	 * 
	 * @param eventItemGVO
	 * @param selectionCase
	 * @param result
	 * @param event
	 * @param applicationContext
	 */
	private void assembleChildren(SwitchGVO eventItemGVO, Object selectionCase, List<ResultItem> resultItems, Event event, ApplicationContext applicationContext) {
		if (resultItems == null) {
			return;
		}
		for (ResultItem resultItem : resultItems) {
			if (!(resultItem instanceof EventItem)) {
				continue;
			}
			BuiltInFunctionGVO subEventItemGVO = AnyEventAssembler.assemble((EventItem) resultItem, event, applicationContext);
			if (subEventItemGVO != null) {
				eventItemGVO.addEventItem(selectionCase, subEventItemGVO);
			}
		}
	}
}
