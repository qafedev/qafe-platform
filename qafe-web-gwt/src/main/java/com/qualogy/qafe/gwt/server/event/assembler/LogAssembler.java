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

import org.apache.commons.lang.StringUtils;

import com.qualogy.qafe.bind.commons.type.Parameter;
import com.qualogy.qafe.bind.core.application.ApplicationContext;
import com.qualogy.qafe.bind.presentation.event.Event;
import com.qualogy.qafe.bind.presentation.event.EventItem;
import com.qualogy.qafe.bind.presentation.event.function.LogFunction;
import com.qualogy.qafe.gwt.client.vo.functions.BuiltInFunctionGVO;
import com.qualogy.qafe.gwt.client.vo.functions.LogFunctionGVO;
import com.qualogy.qafe.gwt.client.vo.ui.event.ParameterGVO;
import com.qualogy.qafe.util.StyleDomUtil;

public class LogAssembler extends AbstractEventItemAssembler {

    public BuiltInFunctionGVO assemble(EventItem eventItem, Event event, ApplicationContext applicationContext) {
        LogFunctionGVO eventItemGVO = null;
        if (eventItem instanceof LogFunction) {
            eventItemGVO = new LogFunctionGVO();
            assembleAttributes(eventItemGVO, (LogFunction)eventItem, event, applicationContext);
        }
        return eventItemGVO;
    }

    private void assembleAttributes(LogFunctionGVO eventItemGVO, LogFunction eventItem, Event event, ApplicationContext applicationContext) {
    	Parameter messageParameters = eventItem.getMessage();
    	ParameterGVO messageGVOParameters = assembleParameter(messageParameters);
    	eventItemGVO.setMessageGVO(messageGVOParameters);
    	eventItemGVO.setDebug(Boolean.FALSE);
		eventItemGVO.setDelay(eventItem.getDelay());
		
		String[] properties = StringUtils.split(eventItem.getStyle()==null ? "": eventItem.getStyle(), ';');
		String[][] styleProperties = new String[properties.length][2];
		for (int i=0;i<properties.length;i++){
			styleProperties[i]= StringUtils.split(properties[i],':');
		}			
		
		/*
		 * Modify the properties since this is DOM manipulation : font-size for css has to become fontSize for DOM
		 */
		for (int i=0;i<styleProperties.length;i++){
			styleProperties[i][0] = StyleDomUtil.initCapitalize(styleProperties[i][0]); 				
		}
				
		eventItemGVO.setStyleProperties(styleProperties);
		eventItemGVO.setStyleClass(eventItem.getStyleClass());
    }
}
