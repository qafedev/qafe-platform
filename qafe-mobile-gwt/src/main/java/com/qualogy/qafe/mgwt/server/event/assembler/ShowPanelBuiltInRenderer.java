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
package com.qualogy.qafe.mgwt.server.event.assembler;

import java.util.Iterator;
import java.util.List;

import com.qualogy.qafe.bind.core.application.ApplicationContext;
import com.qualogy.qafe.bind.presentation.component.PanelDefinition;
import com.qualogy.qafe.bind.presentation.event.Event;
import com.qualogy.qafe.bind.presentation.event.EventItem;
import com.qualogy.qafe.bind.presentation.event.function.ShowPanel;
import com.qualogy.qafe.mgwt.client.vo.data.EventDataGVO;
import com.qualogy.qafe.mgwt.client.vo.data.EventDataI;
import com.qualogy.qafe.mgwt.client.vo.functions.BuiltInFunctionGVO;
import com.qualogy.qafe.mgwt.client.vo.functions.ShowPanelGVO;
import com.qualogy.qafe.mgwt.client.vo.ui.ComponentGVO;
import com.qualogy.qafe.mgwt.server.helper.ApplicationAssembler;
import com.qualogy.qafe.mgwt.server.ui.assembler.ComponentUIAssembler;
import com.qualogy.qafe.web.util.SessionContainer;

public class ShowPanelBuiltInRenderer extends AbstractEventRenderer implements EventAssembler {

	public BuiltInFunctionGVO convert(EventItem eventItem, EventDataGVO eventData, ApplicationContext context, SessionContainer sc) {
		BuiltInFunctionGVO builtInFunctionGVO = null;
		if (eventItem instanceof ShowPanel) {
			ShowPanelGVO showPanelGVO = new ShowPanelGVO();

			builtInFunctionGVO = showPanelGVO;
			builtInFunctionGVO.setUuid(eventData.getUuid());
			ShowPanel showPanel = (ShowPanel) eventItem;
			showPanelGVO.setAutoHide(showPanel.isAutoHide());
			showPanelGVO.setModal(showPanel.isModal());
			if(showPanel.getLeft() > 0 && showPanel.getTop() > 0){
				showPanelGVO.setLeft(showPanel.getLeft());
				showPanelGVO.setTop(showPanel.getTop());
			} else if(showPanel.getLeft() == 0 && showPanel.getTop() == 0 && showPanel.getPosition() == null){
				showPanelGVO.setLeft(Integer.parseInt(eventData.getMouse().get(EventDataI.MOUSE_X)));
				showPanelGVO.setTop(Integer.parseInt(eventData.getMouse().get(EventDataI.MOUSE_Y)));
			} else {
				showPanelGVO.setPosition(showPanel.getPosition());
			}
			
			if (context.getApplicationMapping()!=null &&
					context.getApplicationMapping().getPresentationTier()!=null &&
					context.getApplicationMapping().getPresentationTier().getView() !=null &&
					context.getApplicationMapping().getPresentationTier().getView().getPanelDefinitions()!=null){
				Iterator<?> itr = context.getApplicationMapping().getPresentationTier().getView().getPanelDefinitions().iterator();
				while(itr.hasNext()){
					PanelDefinition pd =(PanelDefinition) itr.next();
					if (pd.getId().equals(showPanel.getSrc())){
						ComponentGVO cGvo = ComponentUIAssembler.convert(pd, null,context.getApplicationMapping(),context, sc);
						List<Event> events = ApplicationAssembler.mergeEvents(eventData.getParent(), context.getApplicationMapping());
						ApplicationAssembler.processComponents(cGvo, context.getApplicationMapping(), null, context.getApplicationMapping().getPresentationTier().getStyles(), events);
						showPanelGVO.setSrc(cGvo);
						showPanelGVO.setBuiltInComponentGVO(getBuiltInComponentGVO(pd.getId(), eventData));
						showPanelGVO.getBuiltInComponentGVO().setWindowId(eventData.getParent());
					}
				}
			}
			
			
		}
		return builtInFunctionGVO;
	}

}
