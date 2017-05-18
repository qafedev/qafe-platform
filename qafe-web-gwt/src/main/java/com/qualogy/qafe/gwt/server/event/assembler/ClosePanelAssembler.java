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
import com.qualogy.qafe.bind.presentation.event.function.ClosePanel;
import com.qualogy.qafe.gwt.client.vo.functions.BuiltInFunctionGVO;
import com.qualogy.qafe.gwt.client.vo.functions.ClosePanelGVO;

public class ClosePanelAssembler extends AbstractEventItemAssembler {

    public BuiltInFunctionGVO assemble(EventItem eventItem, Event event, ApplicationContext applicationContext) {
        ClosePanelGVO eventItemGVO = null;
        if (eventItem instanceof ClosePanel) {
            eventItemGVO = new ClosePanelGVO();
            String windowId = event.getWindowId();
            assembleAttributes(eventItemGVO, (ClosePanel)eventItem, event, windowId, applicationContext);
        }
        return eventItemGVO;
    }

    private void assembleAttributes(ClosePanelGVO eventItemGVO, ClosePanel eventItem, Event event, String windowId, ApplicationContext applicationContext) {
    	eventItemGVO.setRef(eventItem.getRef());
    }
}
