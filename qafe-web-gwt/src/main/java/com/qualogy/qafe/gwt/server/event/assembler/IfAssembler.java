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
package com.qualogy.qafe.gwt.server.event.assembler;

import java.util.List;

import com.qualogy.qafe.bind.core.application.ApplicationContext;
import com.qualogy.qafe.bind.core.statement.IfResult;
import com.qualogy.qafe.bind.core.statement.IfStatement;
import com.qualogy.qafe.bind.core.statement.ResultItem;
import com.qualogy.qafe.bind.presentation.event.Event;
import com.qualogy.qafe.bind.presentation.event.EventItem;
import com.qualogy.qafe.gwt.client.vo.functions.BuiltInFunctionGVO;
import com.qualogy.qafe.gwt.client.vo.functions.IfGVO;
import com.qualogy.qafe.gwt.client.vo.ui.event.ParameterGVO;

public class IfAssembler extends AbstractEventItemAssembler {

	/**
	 * 
	 */
	@Override
	public BuiltInFunctionGVO assemble(EventItem eventItem, Event event, ApplicationContext applicationContext) {
		IfGVO eventItemGVO = null;
		if(eventItem instanceof IfStatement) {
			eventItemGVO = new IfGVO();
			assembleAttributes(eventItemGVO, (IfStatement)eventItem, event, applicationContext);
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
	private void assembleAttributes(IfGVO eventItemGVO, IfStatement eventItem, Event event, ApplicationContext applicationContext) {
		ParameterGVO expressionGVO = assembleParameter(eventItem.getExpression());
		eventItemGVO.setExpression(expressionGVO);
		for (IfResult result: eventItem.getResults()) {
			assembleChildren(eventItemGVO, result.getValue(), result, event, applicationContext);
		}
    }
	
	/**
	 * 
	 * @param eventItemGVO
	 * @param selectionCase
	 * @param result
	 * @param event
	 * @param applicationContext
	 */
	private void assembleChildren(IfGVO eventItemGVO, String selectionCase, IfResult result, Event event, ApplicationContext applicationContext) {
		List<ResultItem> resultItems = result.getResultItems();
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
