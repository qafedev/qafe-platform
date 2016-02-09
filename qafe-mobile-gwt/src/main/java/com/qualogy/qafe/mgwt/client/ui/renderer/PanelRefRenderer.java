/**
 * Copyright 2008-2016 Qualogy Solutions B.V.
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
import com.qualogy.qafe.mgwt.client.ui.component.QPanelRef;
import com.qualogy.qafe.mgwt.client.vo.functions.BuiltInComponentGVO;
import com.qualogy.qafe.mgwt.client.vo.functions.SetPanelGVO;
import com.qualogy.qafe.mgwt.client.vo.functions.execute.FunctionsExecutor;
import com.qualogy.qafe.mgwt.client.vo.ui.ComponentGVO;
import com.qualogy.qafe.mgwt.client.vo.ui.PanelRefGVO;

public class PanelRefRenderer extends AbstractComponentRenderer {

	public UIObject render(ComponentGVO component, String owner, String uuid, String parent, String context, AbstractActivity activity) {
		UIObject widget = null;
		if (component instanceof PanelRefGVO) {
			PanelRefGVO panelRefGVO = (PanelRefGVO)component;
			QPanelRef panelRef = new QPanelRef();
			registerComponent(panelRefGVO, panelRef, owner, parent, context);
			init(panelRefGVO, panelRef, owner, uuid, parent, context, activity);
			widget = panelRef;
		}
		return widget;
	}
	
	@Override
	protected void init(ComponentGVO component, UIObject widget, String owner, String uuid, String parent, String context, AbstractActivity activity) {
		super.init(component, widget, owner, uuid, parent, context, activity);
		PanelRefGVO panelRefGVO = (PanelRefGVO)component;
		QPanelRef panelRef = (QPanelRef)widget;
		renderChildren(panelRefGVO, panelRef, uuid, parent, context, activity);
	}
	
	private void renderChildren(PanelRefGVO component, QPanelRef widget, String uuid, String parent, String context, AbstractActivity activity) {
		// Use SetPanelGVO to render the PanelDefinition,
		// so that components of the PanelDefinition can be traced
		String panelRefId = component.getId();
		String panelRefKey = activity.getClientFactory().generateComponentKey(panelRefId, parent, context);
		SetPanelGVO setPanelGVO = new SetPanelGVO();
		setPanelGVO.setTarget(panelRefId);
		setPanelGVO.setSrc(component.getRef());
		BuiltInComponentGVO builtInComponentGVO = new BuiltInComponentGVO();
		builtInComponentGVO.setComponentId(panelRefId);
		builtInComponentGVO.setComponentIdUUID(panelRefKey);
		builtInComponentGVO.setUUID(uuid);
		builtInComponentGVO.setWindowId(parent);
		setPanelGVO.setBuiltInComponentGVO(builtInComponentGVO);
		setPanelGVO.setUuid(uuid);
		setPanelGVO.setContext(context);
		FunctionsExecutor.getInstance().execute(setPanelGVO, activity);
	}
}