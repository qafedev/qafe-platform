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
import com.qualogy.qafe.bind.presentation.event.Event;
import com.qualogy.qafe.bind.presentation.event.EventItem;
import com.qualogy.qafe.bind.presentation.event.function.CallScript;
import com.qualogy.qafe.gwt.client.vo.functions.BuiltInFunctionGVO;
import com.qualogy.qafe.gwt.client.vo.functions.CallScriptGVO;
import com.qualogy.qafe.gwt.client.vo.ui.event.ParameterGVO;


/**
 * 
 * @author jroosing
 *
 */
public class CallScriptAssembler extends AbstractEventItemAssembler {

    public final BuiltInFunctionGVO assemble(final EventItem eventItem,
            final Event event, final ApplicationContext applicationContext) {
        CallScriptGVO eventItemGVO = null;
        if (eventItem instanceof CallScript) {
            eventItemGVO = new CallScriptGVO();
            assembleAttributes(eventItemGVO, (CallScript) eventItem);
            assembleChildren(eventItemGVO, (CallScript) eventItem);
        }
        return eventItemGVO;
    }
    
    private void assembleAttributes(final CallScriptGVO eventItemGVO, final CallScript eventItem) {
        eventItemGVO.setType(eventItem.getType());
        eventItemGVO.setFunctionName(eventItem.getFunctionName());
    }
    
    private void assembleChildren(final CallScriptGVO eventItemGVO, final CallScript eventItem) {       
        final List<Parameter> params = eventItem.getParams();
        if (params != null) {
            for (Parameter param : params) {
                final ParameterGVO paramGVO = assembleParameter(param);
                eventItemGVO.addParam(paramGVO);
            }
        }
    }
}
