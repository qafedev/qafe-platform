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
package com.qualogy.qafe.gwt.server.event.assembler;

import com.qualogy.qafe.bind.core.application.ApplicationContext;
import com.qualogy.qafe.bind.presentation.event.Event;
import com.qualogy.qafe.bind.presentation.event.EventItem;
import com.qualogy.qafe.bind.presentation.event.function.LocalDelete;
import com.qualogy.qafe.gwt.client.vo.functions.BuiltInFunctionGVO;
import com.qualogy.qafe.gwt.client.vo.functions.StoreClearGVO;

public class StoreClearAssembler extends AbstractEventItemAssembler {

    public final BuiltInFunctionGVO assemble(EventItem eventItem, Event event, ApplicationContext applicationContext) {
        StoreClearGVO eventItemGVO = null;
        if (eventItem instanceof LocalDelete) {
            eventItemGVO = new StoreClearGVO();
            assembleAttributes(eventItemGVO, (LocalDelete)eventItem, event, applicationContext);
        }
        return eventItemGVO;
    }

    private final void assembleAttributes(StoreClearGVO eventItemGVO, LocalDelete eventItem, Event event,
            ApplicationContext applicationContext) {
        eventItemGVO.setName(eventItem.getName());
        eventItemGVO.setTarget(eventItem.getTarget());
    }
}
