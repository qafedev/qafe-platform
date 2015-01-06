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

import com.qualogy.qafe.bind.core.application.ApplicationContext;
import com.qualogy.qafe.bind.domain.ApplicationMapping;
import com.qualogy.qafe.bind.presentation.component.Area;
import com.qualogy.qafe.bind.presentation.component.Component;
import com.qualogy.qafe.bind.presentation.component.MapComponent;
import com.qualogy.qafe.bind.presentation.component.Window;
import com.qualogy.qafe.gwt.client.vo.ui.AreaGVO;
import com.qualogy.qafe.gwt.client.vo.ui.ComponentGVO;
import com.qualogy.qafe.gwt.client.vo.ui.MapGVO;
import com.qualogy.qafe.gwt.server.helper.UIAssemblerHelper;
import com.qualogy.qafe.web.util.SessionContainer;

public class MapUIAssembler implements UIAssembler {

	public MapUIAssembler() {
	}

	public ComponentGVO convert(Component object, Window currentWindow,ApplicationMapping applicationMapping, ApplicationContext context, SessionContainer ss) {
		ComponentGVO vo = null;
		if (object != null) {
			if (object instanceof MapComponent) {
				MapComponent map = (MapComponent)object;
				MapGVO voTemp  = new MapGVO();
				UIAssemblerHelper.copyFields(map, currentWindow,voTemp,applicationMapping, context, ss);		
				
				
				if (map.getAreas()!=null){
					AreaGVO[] areas = new AreaGVO[map.getAreas().size()];
					int index=0;
					for (Area item : map.getAreas()) {											
						areas[index] = (AreaGVO)ComponentUIAssembler.convert(item,currentWindow,applicationMapping,context, ss);
						index++;
					}
					voTemp.setAreas(areas);
					voTemp.setImageUrl(map.getImageUrl());
					voTemp.setName(map.getName());
					
				}
				vo =voTemp;
			}
		}
		return vo;
	}

	public String getStaticStyleName() {
		return "map"; 
	}
}
