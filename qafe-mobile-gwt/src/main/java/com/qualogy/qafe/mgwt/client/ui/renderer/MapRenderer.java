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
package com.qualogy.qafe.mgwt.client.ui.renderer;

import com.google.gwt.user.client.ui.UIObject;
import com.qualogy.qafe.mgwt.client.activities.AbstractActivity;
import com.qualogy.qafe.mgwt.client.ui.component.QMap;
import com.qualogy.qafe.mgwt.client.ui.component.QMapArea;
import com.qualogy.qafe.mgwt.client.vo.ui.AreaGVO;
import com.qualogy.qafe.mgwt.client.vo.ui.ComponentGVO;
import com.qualogy.qafe.mgwt.client.vo.ui.MapGVO;

public class MapRenderer extends AbstractComponentRenderer {

	public UIObject render(ComponentGVO component, String owner, String uuid, String parent, String context, AbstractActivity activity) {
		UIObject widget = null;
		if (component instanceof MapGVO) {
			MapGVO mapGVO = (MapGVO)component;
			QMap map = new QMap(mapGVO.getImageUrl());
			init(mapGVO, map, owner, uuid, parent, context, activity);
			widget = map;
		}
		registerComponent(component, widget, owner, parent, context);
		return widget;
	}
	
	@Override
	protected void init(ComponentGVO component, UIObject widget, String owner, String uuid, String parent, String context, AbstractActivity activity) {
		super.init(component, widget, owner, uuid, parent, context, activity);
		MapGVO mapGVO = (MapGVO)component;
		QMap map = (QMap)widget;
		String name = mapGVO.getName();
		map.setName(name);
		renderChildren(mapGVO, map, uuid, parent, context, activity);
	}
	
	private void renderChildren(MapGVO component, QMap widget, String uuid, String parent, String context, AbstractActivity activity) {
		AreaGVO[] areas = component.getAreas();
		if (areas != null) {
			QMapArea[] areaWidgets = new QMapArea[areas.length];
			for (int i=0; i<areas.length; i++) {
				AreaGVO areaGVO = areas[i];
				String shape = areaGVO.getShape();
				String coords = areaGVO.getCoords();
				String alt = areaGVO.getAlt();
				QMapArea area = new QMapArea();
				area.setShape(shape);
				area.setCoords(coords);
				area.setAlt(alt);
				areaWidgets[i] = area;
			}
			widget.setAreas(areaWidgets);
		}
	}
}
