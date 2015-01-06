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
package com.qualogy.qafe.gwt.server.ui.assembler;

import java.util.ArrayList;

import com.qualogy.qafe.bind.core.application.ApplicationContext;
import com.qualogy.qafe.bind.domain.ApplicationMapping;
import com.qualogy.qafe.bind.presentation.component.Component;
import com.qualogy.qafe.bind.presentation.component.Image;
import com.qualogy.qafe.bind.presentation.component.Panel;
import com.qualogy.qafe.bind.presentation.component.Window;
import com.qualogy.qafe.core.application.ApplicationCluster;
import com.qualogy.qafe.gwt.client.vo.ui.ComponentGVO;
import com.qualogy.qafe.gwt.client.vo.ui.PanelGVO;
import com.qualogy.qafe.gwt.server.helper.UIAssemblerHelper;
import com.qualogy.qafe.web.util.SessionContainer;

public class PanelUIAssembler implements UIAssembler {

	public PanelUIAssembler() {
	}

	public ComponentGVO convert(Component object, Window currentWindow,ApplicationMapping applicationMapping, ApplicationContext context, SessionContainer ss) {
		ComponentGVO vo = null;
		if (object != null) {
			if (object instanceof Panel) {
				Panel panel = (Panel)object;
				PanelGVO voTemp  = new PanelGVO();
				UIAssemblerHelper.copyFields(panel, currentWindow,voTemp,applicationMapping, context, ss);				
				voTemp.setTitle(panel.getDisplayname());
				voTemp.setShowdatacontrol(panel.getShowdatacontrol());
				if (panel.getFieldName()!=null && panel.getFieldName().length()>0){
					if (voTemp.getShowdatacontrol()!=null && voTemp.getShowdatacontrol().booleanValue()){
						voTemp.setDataPanelControl(new ArrayList<ComponentGVO>());
						voTemp.getDataPanelControl().add(ComponentUIAssembler.convert(createImage(panel.getFieldName(),".save","Save"), currentWindow,applicationMapping,context, ss));
						voTemp.getDataPanelControl().add(ComponentUIAssembler.convert(createImage(panel.getFieldName(),".previous","Previous"), currentWindow,applicationMapping,context, ss));
						voTemp.getDataPanelControl().add(ComponentUIAssembler.convert(createImage(panel.getFieldName(),".next","Next"), currentWindow,applicationMapping,context, ss));
						voTemp.getDataPanelControl().add(ComponentUIAssembler.convert(createImage(panel.getFieldName(),".refresh","Refresh"), currentWindow,applicationMapping,context, ss));
					}
				}
				voTemp.setDisclosure(panel.getDisclosure());
				voTemp.setLayout(LayoutUIAssembler.convert(panel.getLayout(),currentWindow,applicationMapping,context,ss));			
				voTemp.setConcurrentModificationEnabled(ApplicationCluster.getInstance().isConcurrentModificationEnabled());
				
				vo = voTemp;
			}
		}
		return vo;
	}

	public String getStaticStyleName() {
		return "panel";
	}
	
	private Component createImage(String fieldName,String suffix,String tooltip){
		Image image = new Image();
		image.setId(fieldName+suffix);
		image.setTooltip(tooltip);	
		return image;
	}
	
}