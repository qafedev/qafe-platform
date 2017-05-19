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

import com.qualogy.qafe.bind.commons.error.ErrorHandler;
import com.qualogy.qafe.bind.commons.error.ServiceError;
import com.qualogy.qafe.bind.commons.error.ServiceErrorRef;
import com.qualogy.qafe.bind.core.application.ApplicationContext;
import com.qualogy.qafe.bind.item.Item;
import com.qualogy.qafe.bind.presentation.event.Event;
import com.qualogy.qafe.bind.presentation.event.EventItem;
import com.qualogy.qafe.gwt.client.vo.functions.BuiltInFunctionGVO;
import com.qualogy.qafe.gwt.client.vo.functions.ErrorHandlerGVO;

public class ErrorHandlerAssembler extends AbstractEventItemAssembler {

    public BuiltInFunctionGVO assemble(EventItem eventItem, Event event, ApplicationContext applicationContext) {
        ErrorHandlerGVO eventItemGVO = null;
        if (eventItem instanceof ErrorHandler) {
            eventItemGVO = new ErrorHandlerGVO();
            assembleAttributes(eventItemGVO, (ErrorHandler)eventItem, event, applicationContext);
            assembleChildren(eventItemGVO, (ErrorHandler)eventItem, event, applicationContext);
        }
        return eventItemGVO;
    }

	private void assembleAttributes(ErrorHandlerGVO eventItemGVO, ErrorHandler eventItem, Event event
			, ApplicationContext applicationContext) {
    	eventItemGVO.setId(eventItem.getId());
    	eventItemGVO.setOrder(eventItem.getOrder());
    	eventItemGVO.setFinalAction(eventItem.getFinalAction());
    	assembleException(eventItemGVO, eventItem, event, applicationContext);
    }
	
	private void assembleException(ErrorHandlerGVO eventItemGVO, ErrorHandler eventItem, Event event
			, ApplicationContext applicationContext) {
    	ServiceErrorRef errorRef = eventItem.getErrorRef();
    	if (errorRef == null) {
    		return;
    	}
    	ServiceError serviceError = errorRef.getRef();
    	if (serviceError == null) {
    		return;
    	}    	
    	eventItemGVO.setException(serviceError.getException());
    }
	
	private void assembleChildren(ErrorHandlerGVO eventItemGVO, ErrorHandler eventItem, Event event
    		, ApplicationContext applicationContext) {
		final List<Item> items = eventItem.getResultItems();
        for (Item item : items) {
        	if (!(item instanceof EventItem)) {
        		continue;
        	}
        	EventItem childEventItem = (EventItem) item;
            final BuiltInFunctionGVO childEventItemGVO = AnyEventAssembler.assemble(childEventItem, event, applicationContext);                 
            if (childEventItemGVO != null) {
            	eventItemGVO.addEventItem(childEventItemGVO);
            }
        }
	}
}