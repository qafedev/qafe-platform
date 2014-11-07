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

import java.util.ArrayList;
import java.util.List;

import com.qualogy.qafe.bind.core.application.ApplicationContext;
import com.qualogy.qafe.bind.presentation.event.EventItem;
import com.qualogy.qafe.bind.presentation.event.function.ChangeStyle;
import com.qualogy.qafe.bind.presentation.event.function.ChangeStyleAction;
import com.qualogy.qafe.gwt.client.vo.data.EventDataGVO;
import com.qualogy.qafe.gwt.client.vo.functions.BuiltInFunctionGVO;
import com.qualogy.qafe.gwt.client.vo.functions.ChangeStyleActionGVO;
import com.qualogy.qafe.gwt.client.vo.functions.ChangeStyleGVO;
import com.qualogy.qafe.util.StyleDomUtil;
import com.qualogy.qafe.web.util.SessionContainer;



public class ChangeStyleEventRenderer extends AbstractEventRenderer implements EventAssembler {

	public BuiltInFunctionGVO convert(EventItem eventItem,EventDataGVO eventData,ApplicationContext context,SessionContainer sc) {
		BuiltInFunctionGVO bif = null;
		if (eventItem instanceof ChangeStyle) {
			ChangeStyleGVO changeStyle = new ChangeStyleGVO();
		
			bif = changeStyle;
			fillIn(eventItem, changeStyle, eventData);
		
			// Change Style Actions processing
			ChangeStyle in = (ChangeStyle)eventItem;
			
			List<ChangeStyleActionGVO> outActions = new ArrayList<ChangeStyleActionGVO>();
			if (in.getActions()!=null){
			for (ChangeStyleAction changeStyleAction : in.getActions()) {
				
			
					 ChangeStyleActionGVO changeStyleAction2 = new ChangeStyleActionGVO();
					 changeStyleAction2.setAction(changeStyleAction.getAction());
					 changeStyleAction2.setKey(StyleDomUtil.initCapitalize(changeStyleAction.getKey()));
					 changeStyleAction2.setValue(changeStyleAction.getValue());
					 changeStyleAction2.setStyle(changeStyleAction.getStyle());
					 outActions.add(changeStyleAction2);
				}
			}
			
			changeStyle.setActions(outActions);
			}
				
		return bif;
	}

}
