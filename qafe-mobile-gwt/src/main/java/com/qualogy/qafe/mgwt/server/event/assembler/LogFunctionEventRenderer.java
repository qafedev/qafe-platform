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
package com.qualogy.qafe.mgwt.server.event.assembler;

import org.apache.commons.lang.StringUtils;

import com.qualogy.qafe.bind.core.application.ApplicationContext;
import com.qualogy.qafe.bind.presentation.event.EventItem;
import com.qualogy.qafe.bind.presentation.event.function.LogFunction;
import com.qualogy.qafe.mgwt.client.vo.data.EventDataGVO;
import com.qualogy.qafe.mgwt.client.vo.functions.BuiltInFunctionGVO;
import com.qualogy.qafe.mgwt.client.vo.functions.LogFunctionGVO;
import com.qualogy.qafe.util.StyleDomUtil;
import com.qualogy.qafe.web.util.SessionContainer;

public class LogFunctionEventRenderer extends AbstractEventRenderer implements
		EventAssembler {

	public BuiltInFunctionGVO convert(EventItem eventItem,EventDataGVO eventData,ApplicationContext context, SessionContainer sc) {
		BuiltInFunctionGVO bif = null;
		if (eventItem instanceof LogFunction) {
			LogFunction logFunction = (LogFunction)eventItem;			
			LogFunctionGVO logFunctionGVO = new LogFunctionGVO();
			Object data = logFunction.getMessageData();
			logFunctionGVO.setMessage(data!=null ? data.toString(): "");
			logFunctionGVO.setWindowId(eventData.getWindowId());
			logFunctionGVO.setUuid(eventData.getUuid());
			logFunctionGVO.setDebug(logFunction.getDebug());
			logFunctionGVO.setDelay(logFunction.getDelay());
			
			String[] properties = StringUtils.split(logFunction.getStyle()==null ? "": logFunction.getStyle(), ';');
			String[][] styleProperties = new String[properties.length][2];
			for (int i=0;i<properties.length;i++){
				styleProperties[i]= StringUtils.split(properties[i],':');
			}			
			
			/*
			 * Modify the properties since this is DOM manipulation : font-size for css has to become fontSize for DOM
			 */
			for (int i=0;i<styleProperties.length;i++){
				
				styleProperties[i][0] =StyleDomUtil.initCapitalize(styleProperties[i][0]); 				
			}
			
			
			
			logFunctionGVO.setStyleProperties(styleProperties);
			logFunctionGVO.setStyleClass(logFunction.getStyleClass());
			bif=logFunctionGVO;
		}
		return bif;
	}

}
