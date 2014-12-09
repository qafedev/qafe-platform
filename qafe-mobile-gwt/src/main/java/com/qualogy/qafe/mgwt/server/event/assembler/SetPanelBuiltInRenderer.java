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

import java.util.Iterator;
import java.util.List;

import com.qualogy.qafe.bind.core.application.ApplicationContext;
import com.qualogy.qafe.bind.presentation.component.PanelDefinition;
import com.qualogy.qafe.bind.presentation.event.Event;
import com.qualogy.qafe.bind.presentation.event.EventItem;
import com.qualogy.qafe.bind.presentation.event.function.SetPanel;
import com.qualogy.qafe.mgwt.client.vo.data.EventDataGVO;
import com.qualogy.qafe.mgwt.client.vo.functions.BuiltInFunctionGVO;
import com.qualogy.qafe.mgwt.client.vo.functions.SetPanelGVO;
import com.qualogy.qafe.mgwt.client.vo.ui.ComponentGVO;
import com.qualogy.qafe.mgwt.server.helper.ApplicationAssembler;
import com.qualogy.qafe.mgwt.server.ui.assembler.ComponentUIAssembler;
import com.qualogy.qafe.web.util.SessionContainer;

public class SetPanelBuiltInRenderer extends AbstractEventRenderer implements EventAssembler {

	public BuiltInFunctionGVO convert(EventItem eventItem, EventDataGVO eventData,ApplicationContext context, SessionContainer sc) {
		BuiltInFunctionGVO bif = null;
		if (eventItem instanceof SetPanel) {
			SetPanelGVO setPanel = new SetPanelGVO();

			bif = setPanel;
			bif.setUuid(eventData.getUuid());
			SetPanel in = (SetPanel) eventItem;
			setPanel.setTarget(in.getTarget());
			setPanel.setBuiltInComponentGVO(getBuiltInComponentGVO(in.getTarget(), eventData));
			
			// since parent is unknown for the target, we have to set it.
			
			setPanel.getBuiltInComponentGVO().setWindowId(eventData.getParent());
			
			if (context.getApplicationMapping()!=null &&
					context.getApplicationMapping().getPresentationTier()!=null &&
					context.getApplicationMapping().getPresentationTier().getView() !=null &&
					context.getApplicationMapping().getPresentationTier().getView().getPanelDefinitions()!=null){
				Iterator<?> itr = context.getApplicationMapping().getPresentationTier().getView().getPanelDefinitions().iterator();
				while(itr.hasNext()){
					PanelDefinition pd =(PanelDefinition) itr.next();
					if (pd.getId().equals(in.getSrc())){
						ComponentGVO cGvo =ComponentUIAssembler.convert(pd, null,context.getApplicationMapping(),context, sc);
						List<Event> events = ApplicationAssembler.mergeEvents(eventData.getParent(), context.getApplicationMapping());
						ApplicationAssembler.processComponents(cGvo, context.getApplicationMapping(), null, context.getApplicationMapping().getPresentationTier().getStyles(), events);
						setPanel.setSrc(cGvo);
					}
				}
			}
			
			
		}
		return bif;
	}

	
}
