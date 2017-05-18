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

import com.qualogy.qafe.bind.business.action.BusinessActionRef;
import com.qualogy.qafe.bind.commons.type.Parameter;
import com.qualogy.qafe.bind.core.application.ApplicationContext;
import com.qualogy.qafe.bind.presentation.event.Event;
import com.qualogy.qafe.bind.presentation.event.EventItem;
import com.qualogy.qafe.gwt.client.vo.functions.BuiltInFunctionGVO;
import com.qualogy.qafe.gwt.client.vo.functions.BusinessActionRefGVO;
import com.qualogy.qafe.gwt.client.vo.ui.event.ParameterGVO;

/**
 * 
 * 
 *
 */
public final class BusinessActionRefAssembler extends AbstractEventItemAssembler {

    public BuiltInFunctionGVO assemble(final EventItem eventItem, final Event event,
            final ApplicationContext applicationContext) {
        BusinessActionRefGVO eventItemGVO = null;
        final BusinessActionRef businessActionRef = (BusinessActionRef) eventItem;
        eventItemGVO = new BusinessActionRefGVO();
        eventItemGVO.setBusinessActionId(businessActionRef.getRef().getId());
        collectInputVariables(eventItemGVO, businessActionRef);
        collectOutputVariables(eventItemGVO, businessActionRef);
        return eventItemGVO;
    }

    private void collectInputVariables(final BusinessActionRefGVO eventItemGVO,
            final BusinessActionRef businessActionRef) {
        final List<Parameter> inputParams = businessActionRef.getInput();
        if (inputParams == null) {
            return;
        }
        for (Parameter parameter : inputParams) {
            final ParameterGVO parameterGVO = assembleParameter(parameter);
            eventItemGVO.getInputParameters().add(parameterGVO);
        }
    }

    private void collectOutputVariables(final BusinessActionRefGVO eventItemGVO,
            final BusinessActionRef businessActionRef) {
        final List<Parameter> ouptputParams = businessActionRef.getOutput();
        if (ouptputParams == null) {
            return;
        }
        for (Parameter parameter : ouptputParams) {
            final ParameterGVO parameterGVO = assembleParameter(parameter);
            eventItemGVO.getOutputParameters().add(parameterGVO);
        }
    }

}
