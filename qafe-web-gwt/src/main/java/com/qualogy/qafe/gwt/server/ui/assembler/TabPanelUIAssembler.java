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
package com.qualogy.qafe.gwt.server.ui.assembler;

import com.qualogy.qafe.bind.core.application.ApplicationContext;
import com.qualogy.qafe.bind.core.application.Configuration;
import com.qualogy.qafe.bind.domain.ApplicationMapping;
import com.qualogy.qafe.bind.presentation.component.Component;
import com.qualogy.qafe.bind.presentation.component.TabPanel;
import com.qualogy.qafe.bind.presentation.component.Window;
import com.qualogy.qafe.core.application.ApplicationCluster;
import com.qualogy.qafe.gwt.client.vo.ui.ComponentGVO;
import com.qualogy.qafe.gwt.client.vo.ui.TabGVO;
import com.qualogy.qafe.gwt.client.vo.ui.TabPanelGVO;
import com.qualogy.qafe.gwt.server.helper.UIAssemblerHelper;
import com.qualogy.qafe.web.util.SessionContainer;

public class TabPanelUIAssembler implements UIAssembler {

	public TabPanelUIAssembler() {
	}

	public ComponentGVO convert(Component object, Window currentWindow,ApplicationMapping applicationMapping, ApplicationContext context, SessionContainer ss) {
		ComponentGVO vo = null;
		if (object != null) {
			if (object instanceof TabPanel) {
				TabPanel panel = (TabPanel)object;
				TabPanelGVO voTemp  = new TabPanelGVO();
				init(panel, voTemp, currentWindow, applicationMapping, context, ss);
				if (panel.getTabs() != null) {
					TabGVO[] tabs = new TabGVO[panel.getTabs().size()];
					int index = 0;
					for (Component tab : panel.getTabs()) {						
						tabs[index] =(TabGVO) ComponentUIAssembler.convert(tab, currentWindow,applicationMapping,context, ss);
						tabs[index].setContainerName(voTemp.getId());
						index++;
					}
					voTemp.setTabs(tabs);
				}
				vo =voTemp;
			}
		}
		return vo;
	}

	public String getStaticStyleName() {
		return "tabs";
	}
	
	private void init(TabPanel tabPanel, TabPanelGVO tabPanelGVO, Window currentWindow, ApplicationMapping applicationMapping, ApplicationContext context, SessionContainer ss) {
		String animationEnabled = ApplicationCluster.getInstance().getConfigurationItem(Configuration.ANIMATION_ENABLED);
		if (animationEnabled != null) {
			try {
				tabPanelGVO.setAnimationEnabled(Boolean.parseBoolean(animationEnabled));	
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
		UIAssemblerHelper.copyFields(tabPanel, currentWindow, tabPanelGVO, applicationMapping, context, ss);
	}
}