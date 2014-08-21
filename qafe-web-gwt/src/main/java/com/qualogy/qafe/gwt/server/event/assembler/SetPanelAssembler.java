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

import java.util.List;

import com.qualogy.qafe.bind.core.application.ApplicationContext;
import com.qualogy.qafe.bind.domain.ApplicationMapping;
import com.qualogy.qafe.bind.domain.PresentationTier;
import com.qualogy.qafe.bind.presentation.component.PanelDefinition;
import com.qualogy.qafe.bind.presentation.component.View;
import com.qualogy.qafe.bind.presentation.event.Event;
import com.qualogy.qafe.bind.presentation.event.EventItem;
import com.qualogy.qafe.bind.presentation.event.function.SetPanel;
import com.qualogy.qafe.gwt.client.vo.functions.BuiltInFunctionGVO;
import com.qualogy.qafe.gwt.client.vo.functions.SetPanelGVO;
import com.qualogy.qafe.gwt.client.vo.ui.ComponentGVO;
import com.qualogy.qafe.gwt.server.helper.ApplicationAssembler;
import com.qualogy.qafe.gwt.server.ui.assembler.ComponentUIAssembler;

public class SetPanelAssembler extends AbstractEventItemAssembler {

	public BuiltInFunctionGVO assemble(EventItem eventItem, Event event, ApplicationContext applicationContext) {
		SetPanelGVO eventItemGVO = null;
        if (eventItem instanceof SetPanel) {
            eventItemGVO = new SetPanelGVO();
            String windowId = event.getWindowId();
            assembleAttributes(eventItemGVO, (SetPanel)eventItem, event, windowId, applicationContext);
        }
        return eventItemGVO;
	}
	
	private void assembleAttributes(SetPanelGVO eventItemGVO, SetPanel eventItem, Event event, String windowId, ApplicationContext applicationContext) {
		eventItemGVO.setTarget(eventItem.getTarget());
        
		ApplicationMapping applicationMapping = applicationContext.getApplicationMapping();
		if (applicationMapping == null) {
			return;
		}
		PresentationTier presentationTier = applicationMapping.getPresentationTier();
		if (presentationTier == null) {
			return;
		}
		View view = presentationTier.getView();
		if (view == null) {
			return;
		}
		List<PanelDefinition> panelDefinitions = view.getPanelDefinitions();
		if (panelDefinitions == null) {
			return;
		}
		for (PanelDefinition panelDefinition : panelDefinitions) {
			String panelDefinitionId = panelDefinition.getId();
			if (panelDefinitionId.equals(eventItem.getSrc())) {
				ComponentGVO panelDefinitionGVO = ComponentUIAssembler.convert(panelDefinition, null, applicationMapping, applicationContext, null);
				List<Event> events = ApplicationAssembler.mergeEvents(windowId, applicationMapping);
				ApplicationAssembler.processComponents(panelDefinitionGVO, applicationMapping, null, presentationTier.getStyles(), events);
				eventItemGVO.setSrc(panelDefinitionGVO);
				break;
			}
		}
    }
}