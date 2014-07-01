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

import java.util.Iterator;

import com.qualogy.qafe.bind.core.application.ApplicationContext;
import com.qualogy.qafe.bind.presentation.component.PanelDefinition;
import com.qualogy.qafe.bind.presentation.event.EventItem;
import com.qualogy.qafe.bind.presentation.event.function.ClosePanel;
import com.qualogy.qafe.gwt.client.vo.data.EventDataGVO;
import com.qualogy.qafe.gwt.client.vo.functions.BuiltInFunctionGVO;
import com.qualogy.qafe.gwt.client.vo.functions.ClosePanelGVO;
import com.qualogy.qafe.web.util.SessionContainer;

public class ClosePanelBuiltInRenderer extends AbstractEventRenderer implements EventAssembler {

	public BuiltInFunctionGVO convert(EventItem eventItem, EventDataGVO eventData, ApplicationContext context, SessionContainer sc) {
		BuiltInFunctionGVO builtInFunctionGVO = null;
		if (eventItem instanceof ClosePanel) {
			ClosePanelGVO closePanelGVO = new ClosePanelGVO();
			builtInFunctionGVO = closePanelGVO;
			builtInFunctionGVO.setUuid(eventData.getUuid());
			ClosePanel closePanel = (ClosePanel) eventItem;
			closePanelGVO.setRef(closePanel.getRef());
			
			if (context.getApplicationMapping()!=null &&
					context.getApplicationMapping().getPresentationTier()!=null &&
					context.getApplicationMapping().getPresentationTier().getView() !=null &&
					context.getApplicationMapping().getPresentationTier().getView().getPanelDefinitions()!=null){
				Iterator<?> itr = context.getApplicationMapping().getPresentationTier().getView().getPanelDefinitions().iterator();
				while(itr.hasNext()){
					PanelDefinition pd =(PanelDefinition) itr.next();
					if (pd.getId().equals(closePanel.getRef())){
						closePanelGVO.setBuiltInComponentGVO(getBuiltInComponentGVO(pd.getId(), eventData));
						closePanelGVO.getBuiltInComponentGVO().setWindowId(eventData.getParent());
						break;
					}
				}
			}
			
		}
		return builtInFunctionGVO;
	}

}
