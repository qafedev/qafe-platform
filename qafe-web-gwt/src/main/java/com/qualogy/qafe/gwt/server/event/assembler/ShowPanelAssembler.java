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
import com.qualogy.qafe.bind.presentation.event.function.ShowPanel;
import com.qualogy.qafe.gwt.client.vo.functions.BuiltInFunctionGVO;
import com.qualogy.qafe.gwt.client.vo.functions.ShowPanelGVO;
import com.qualogy.qafe.gwt.client.vo.ui.PanelGVO;
import com.qualogy.qafe.gwt.server.helper.ApplicationAssembler;
import com.qualogy.qafe.gwt.server.ui.assembler.ComponentUIAssembler;

public class ShowPanelAssembler extends AbstractEventItemAssembler {

    public BuiltInFunctionGVO assemble(EventItem eventItem, Event event, ApplicationContext applicationContext) {
        ShowPanelGVO eventItemGVO = null;
        if (eventItem instanceof ShowPanel) {
            eventItemGVO = new ShowPanelGVO();
            String windowId = event.getWindowId();
            assembleAttributes(eventItemGVO, (ShowPanel)eventItem, event, windowId, applicationContext);
        }
        return eventItemGVO;
    }

    private void assembleAttributes(ShowPanelGVO eventItemGVO, ShowPanel eventItem, Event event, String windowId, ApplicationContext applicationContext) {
    	eventItemGVO.setAutoHide(eventItem.isAutoHide());
    	eventItemGVO.setModal(eventItem.isModal());
    	eventItemGVO.setLeft(eventItem.getLeft());
    	eventItemGVO.setTop(eventItem.getTop());
    	eventItemGVO.setPosition(eventItem.getPosition());
    	
		PanelGVO panelGVO = getPanelDefinition(eventItem.getSrc(), windowId, applicationContext);
		eventItemGVO.setSrc(panelGVO);
    }
    
    private PanelGVO getPanelDefinition(String panelDefinitionId, String windowId, ApplicationContext applicationContext) {
    	ApplicationMapping applicationMapping = applicationContext.getApplicationMapping();
    	if (applicationMapping == null) {
    		return null;
    	}
    	
    	PresentationTier presentationTier = applicationMapping.getPresentationTier();
    	if (presentationTier == null) {
    		return null;
    	}
    	
    	View view = presentationTier.getView();
    	if (view == null) {
    		return null;
    	}

    	List<PanelDefinition> panelDefinitions = view.getPanelDefinitions();
    	if (panelDefinitions == null) {
    		return null;
    	}
    	
    	for (PanelDefinition panelDefinition : panelDefinitions) {
			if (panelDefinition.getId().equals(panelDefinitionId)) {
				PanelGVO panelDefinitionGVO = (PanelGVO)ComponentUIAssembler.convert(panelDefinition, null, applicationMapping, applicationContext, null);
				List<Event> events = ApplicationAssembler.mergeEvents(windowId, applicationMapping);
				ApplicationAssembler.processComponents(panelDefinitionGVO, applicationMapping, null, presentationTier.getStyles(), events);
				return panelDefinitionGVO;
			}    		
    	}
    	return null;
    }
}
