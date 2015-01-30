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
package com.qualogy.qafe.gwt.server.processor.impl;

import java.util.Map;

import com.qualogy.qafe.core.errorhandling.ExternalException;
import com.qualogy.qafe.gwt.client.vo.data.EventItemDataGVO;
import com.qualogy.qafe.gwt.client.vo.functions.BusinessActionRefGVO;
import com.qualogy.qafe.gwt.server.processor.EventItemProcessor;
import com.qualogy.qafe.presentation.BusinessActionItemDataObject;
import com.qualogy.qafe.presentation.EventItemDataObject;
import com.qualogy.qafe.presentation.handler.EventItemHandler;

public class EventItemProcessorImpl implements EventItemProcessor {

    public Map<String, Object> execute(EventItemDataGVO eventItemData) throws ExternalException {
        EventItemDataObject eventItemDataObject = null;
        if (eventItemData.getBuiltInGVO() instanceof BusinessActionRefGVO) {
            BusinessActionRefGVO businessActionRefGVO =
                (BusinessActionRefGVO) eventItemData.getBuiltInGVO();
            eventItemDataObject =
                new BusinessActionItemDataObject(eventItemData.getSessionId(), eventItemData.getAppId(), eventItemData.getWindowId(), 
                        businessActionRefGVO.getBusinessActionId(), eventItemData.getInputValues(), eventItemData.getOutputVariables());
        }

        final EventItemHandler eventItemHandler = EventItemHandler.getInstance();
        Map<String, Object> outputValues = eventItemHandler.execute(eventItemDataObject);
        return outputValues;
    }

}
