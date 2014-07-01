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

import com.qualogy.qafe.bind.core.application.ApplicationContext;
import com.qualogy.qafe.bind.presentation.event.EventItem;
import com.qualogy.qafe.bind.presentation.event.function.CloseWindow;
import com.qualogy.qafe.mgwt.client.vo.data.EventDataGVO;
import com.qualogy.qafe.mgwt.client.vo.functions.BuiltInFunctionGVO;
import com.qualogy.qafe.mgwt.client.vo.functions.CloseWindowGVO;
import com.qualogy.qafe.web.util.SessionContainer;

public class CloseWindowEventRenderer extends AbstractEventRenderer implements EventAssembler{

	public BuiltInFunctionGVO convert(EventItem eventItem, EventDataGVO eventData,ApplicationContext context, SessionContainer sc) {
		BuiltInFunctionGVO bif = null;
		if (eventItem instanceof CloseWindow) {
			CloseWindowGVO closeWindow = new CloseWindowGVO();
			
			bif = closeWindow;
			bif.setUuid(eventData.getUuid().substring(eventData.getUuid().lastIndexOf('|')+1));
			CloseWindow in = (CloseWindow)eventItem;
			setComponents(closeWindow, in, eventData);
			
			if (in.getWindowData() != null) {
				closeWindow.setWindow(in.getWindowData());	
			} else if ((in.getWindow() != null) && (in.getWindow().getValue() != null)) {
				closeWindow.setWindow(in.getWindow().getValue().getStaticValue());
			}
		}
		return bif;
	}

}
