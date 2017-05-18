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
package com.qualogy.qafe.gwt.server.event.assembler;

import java.util.List;

import com.qualogy.qafe.bind.commons.type.Reference;
import com.qualogy.qafe.bind.core.application.ApplicationContext;
import com.qualogy.qafe.bind.core.statement.Iteration;
import com.qualogy.qafe.bind.core.statement.ResultItem;
import com.qualogy.qafe.bind.presentation.event.Event;
import com.qualogy.qafe.bind.presentation.event.EventItem;
import com.qualogy.qafe.gwt.client.vo.functions.BuiltInFunctionGVO;
import com.qualogy.qafe.gwt.client.vo.functions.IterationGVO;
import com.qualogy.qafe.gwt.client.vo.ui.event.ParameterGVO;

public class IterationAssembler extends AbstractEventItemAssembler {

	@Override
	public BuiltInFunctionGVO assemble(EventItem eventItem, Event event, ApplicationContext applicationContext) {
		IterationGVO eventItemGVO = null;
		if(eventItem instanceof Iteration) {
			eventItemGVO = new IterationGVO();
			assembleAttributes(eventItemGVO, (Iteration)eventItem, event, applicationContext);
			assembleChildren(eventItemGVO, (Iteration)eventItem, event, applicationContext);
		}
		return eventItemGVO;
	}
	
	private void assembleAttributes(IterationGVO eventItemGVO, Iteration eventItem, Event event
			, ApplicationContext applicationContext) {
		ParameterGVO referenceGVO = assembleReference(eventItem.getReference());
		eventItemGVO.setReference(referenceGVO);
		eventItemGVO.setBegin(eventItem.getBegin());
		eventItemGVO.setEnd(eventItem.getEnd());
		eventItemGVO.setIncrement(eventItem.getIncrement());
		eventItemGVO.setItemCount(eventItem.getItemCount());
		eventItemGVO.setOrder(eventItem.getOrder());
		eventItemGVO.setVar(eventItem.getVar());
		eventItemGVO.setVarIndex(eventItem.getVarIndex());
    }
	
	private ParameterGVO assembleReference(Reference reference) {
		String ref = reference.getRootRef();
		String source = reference.getSource();
		ParameterGVO referenceGVO = new ParameterGVO(null, ref, source);
		return referenceGVO;
	}

	private void assembleChildren(IterationGVO eventItemGVO, Iteration eventItem, Event event
			, ApplicationContext applicationContext) {
		List<ResultItem> resultItems = eventItem.getResultItems();
		if (resultItems == null) {
			return;
		}
		for (ResultItem resultItem : resultItems) {
			if (!(resultItem instanceof EventItem)) {
				continue;
			}
			BuiltInFunctionGVO subEventItemGVO = AnyEventAssembler.assemble((EventItem) resultItem, event, applicationContext);
			if (subEventItemGVO != null) {
				eventItemGVO.addEventItem(subEventItemGVO);
			}
		}
	}
}