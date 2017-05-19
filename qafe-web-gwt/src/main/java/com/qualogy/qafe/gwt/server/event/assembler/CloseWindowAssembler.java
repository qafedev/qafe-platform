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

import com.qualogy.qafe.bind.core.application.ApplicationContext;
import com.qualogy.qafe.bind.presentation.event.Event;
import com.qualogy.qafe.bind.presentation.event.EventItem;
import com.qualogy.qafe.bind.presentation.event.function.CloseWindow;
import com.qualogy.qafe.gwt.client.vo.functions.BuiltInFunctionGVO;
import com.qualogy.qafe.gwt.client.vo.functions.CloseWindowGVO;
import com.qualogy.qafe.gwt.client.vo.ui.event.ParameterGVO;

public class CloseWindowAssembler extends AbstractEventItemAssembler {

    public BuiltInFunctionGVO assemble(EventItem eventItem, Event event, ApplicationContext applicationContext) {
        CloseWindowGVO eventItemGVO = null;
        if (eventItem instanceof CloseWindow) {
            eventItemGVO = new CloseWindowGVO();
            assembleAttributes(eventItemGVO, (CloseWindow)eventItem, event, applicationContext);
        }
        return eventItemGVO;
    }

    private void assembleAttributes(CloseWindowGVO eventItemGVO, CloseWindow eventItem, Event event, ApplicationContext applicationContext) {
    	ParameterGVO windowGVO = assembleParameter(eventItem.getWindow());
    	eventItemGVO.setWindowGVO(windowGVO);
    }
}
