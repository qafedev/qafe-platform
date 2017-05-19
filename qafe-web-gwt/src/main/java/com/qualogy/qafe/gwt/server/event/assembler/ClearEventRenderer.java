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
import com.qualogy.qafe.bind.presentation.event.EventItem;
import com.qualogy.qafe.bind.presentation.event.function.Clear;
import com.qualogy.qafe.gwt.client.vo.data.EventDataGVO;
import com.qualogy.qafe.gwt.client.vo.functions.BuiltInFunctionGVO;
import com.qualogy.qafe.gwt.client.vo.functions.ClearGVO;
import com.qualogy.qafe.web.util.SessionContainer;

@Deprecated
public class ClearEventRenderer extends AbstractEventRenderer implements EventAssembler{

	public BuiltInFunctionGVO convert(EventItem eventItem,EventDataGVO eventData,ApplicationContext context, SessionContainer sc) {
		BuiltInFunctionGVO bif = null;
		if (eventItem instanceof Clear) {
			ClearGVO clear = new ClearGVO();
			
			bif = clear;
			bif.setUuid(eventData.getUuid());
			Clear in = (Clear)eventItem;
			clear.setComponentId(in.getComponentId()+"|"+eventData.getUuid());
			clear.setBuiltInComponentGVO(getBuiltInComponentGVO(in.getComponentId(),eventData));	
		}
		return bif;
	}

}
