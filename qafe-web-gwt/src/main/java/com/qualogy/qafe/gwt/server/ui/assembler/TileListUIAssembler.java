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
import com.qualogy.qafe.bind.domain.ApplicationMapping;
import com.qualogy.qafe.bind.presentation.component.Component;
import com.qualogy.qafe.bind.presentation.component.TileList;
import com.qualogy.qafe.bind.presentation.component.Window;
import com.qualogy.qafe.gwt.client.vo.layout.BorderLayoutGVO;
import com.qualogy.qafe.gwt.client.vo.layout.HasElements;
import com.qualogy.qafe.gwt.client.vo.ui.ComponentGVO;
import com.qualogy.qafe.gwt.client.vo.ui.DataGridColumnGVO;
import com.qualogy.qafe.gwt.client.vo.ui.DataGridGVO;
import com.qualogy.qafe.gwt.client.vo.ui.ElementGVO;
import com.qualogy.qafe.gwt.client.vo.ui.HasComponentsI;
import com.qualogy.qafe.gwt.client.vo.ui.PanelGVO;
import com.qualogy.qafe.gwt.client.vo.ui.StackGVO;
import com.qualogy.qafe.gwt.client.vo.ui.TileListGVO;
import com.qualogy.qafe.gwt.client.vo.ui.WindowGVO;
import com.qualogy.qafe.gwt.server.helper.UIAssemblerHelper;
import com.qualogy.qafe.web.util.SessionContainer;

public class TileListUIAssembler implements UIAssembler {

	public TileListUIAssembler(){
	}

	public ComponentGVO convert(Component component, Window currentWindow, ApplicationMapping applicationMapping, ApplicationContext context, SessionContainer sc) {
		ComponentGVO vo = null;
		if (component != null) {
			if (component instanceof TileList) {
				TileList tileList = (TileList)component;
				TileListGVO voTemp  = new TileListGVO();
				UIAssemblerHelper.copyFields(tileList, currentWindow,voTemp,applicationMapping, context, sc);
				ComponentGVO componentGVO = (ComponentGVO)ComponentUIAssembler.convert(tileList.getComponent(), currentWindow, applicationMapping, context, sc);
				voTemp.setComponent(componentGVO);
				voTemp.setColumns(tileList.getColumns());
				if(voTemp.getComponents() != null) {
					for(ComponentGVO comp: voTemp.getComponents()) {
						assignParenttoInnerComponents(comp,voTemp.getId());
					}
				}
				vo = voTemp;
			}
		}
		return vo;
	}

    // CHECKSTYLE.OFF: CyclomaticComplexity
	private void assignParenttoInnerComponents(ComponentGVO component, String parent) {
		if (component!=null){
			component.setParent(parent);

			if (component instanceof PanelGVO){
				PanelGVO panel = (PanelGVO) component;
				ComponentGVO[] panelComponents = null;
				if (panel.getLayout() instanceof HasElements) {
					HasElements hasElements = (HasElements) panel.getLayout();
					ElementGVO[] elements = hasElements.getElements();
					if (elements != null) {
						for (int i = 0; i < elements.length; i++) {
							ComponentGVO c = elements[i].getComponent();
							assignParenttoInnerComponents(c,parent);
						}
					}
				} else if (panel.getLayout() instanceof BorderLayoutGVO) {
					BorderLayoutGVO borderLayoutGVO = (BorderLayoutGVO) (panel.getLayout());
					assignParenttoInnerComponents(borderLayoutGVO.getCenter(),parent);
					assignParenttoInnerComponents(borderLayoutGVO.getNorth(),parent);
					assignParenttoInnerComponents(borderLayoutGVO.getWest(),parent);
					assignParenttoInnerComponents(borderLayoutGVO.getEast(),parent);
					assignParenttoInnerComponents(borderLayoutGVO.getSouth(),parent);
				} else {
					panelComponents = panel.getLayout().getComponents();
					if (panelComponents != null) {
						for (int i = 0; i < panelComponents.length; i++) {
							assignParenttoInnerComponents(panelComponents[i],parent);
						}
					}
				}
				if (panel.getDataPanelControl() != null) {
					for (ComponentGVO componentGVO : panel.getDataPanelControl()) {
						assignParenttoInnerComponents(componentGVO,parent);
					}
				}
			}else if (component instanceof HasComponentsI) {
				HasComponentsI hasComponentsI = (HasComponentsI) component;
				ComponentGVO[] cs = hasComponentsI.getComponents();
				if (cs != null) {
					for (int i = 0; i < cs.length; i++) {
						assignParenttoInnerComponents(cs[i],parent);
					}
				}
			} else if (component instanceof StackGVO) {
				StackGVO stackGVO = (StackGVO) component;
				assignParenttoInnerComponents(stackGVO.getComponent(),parent);
			} else if (component instanceof DataGridGVO) {
				DataGridGVO dataGridGVO = (DataGridGVO) component;
				DataGridColumnGVO[] columns = dataGridGVO.getColumns();
				if (columns != null) {
					for (int i = 0; i < columns.length; i++) {
						assignParenttoInnerComponents(columns[i],parent);
					}
				}

				assignParenttoInnerComponents(dataGridGVO.getDeleteComponent(),parent);
				assignParenttoInnerComponents(dataGridGVO.getAddComponent(),parent);
				assignParenttoInnerComponents(dataGridGVO.getSaveComponent(),parent);
				assignParenttoInnerComponents(dataGridGVO.getPageSizeComponent(),parent);
				assignParenttoInnerComponents(dataGridGVO.getOffSetComponent(),parent);
				assignParenttoInnerComponents(dataGridGVO.getExportComponent(),parent);
				assignParenttoInnerComponents(dataGridGVO.getOverflow(),parent);
			} else if (component instanceof WindowGVO) {
				assignParenttoInnerComponents(((WindowGVO) component).getRootPanel(),parent);
			}
		}
	}
    // CHECKSTYLE.OFF: CyclomaticComplexity

	public String getStaticStyleName() {
		return "TileList";
	}

}
