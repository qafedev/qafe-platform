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
package com.qualogy.qafe.mgwt.client.vo.functions.execute;

import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.mgwt.ui.client.widget.ScrollPanel;
import com.qualogy.qafe.mgwt.client.activities.AbstractActivity;
import com.qualogy.qafe.mgwt.client.activities.WindowActivity;
import com.qualogy.qafe.mgwt.client.places.WindowPlace;
import com.qualogy.qafe.mgwt.client.ui.renderer.AnyComponentRenderer;
import com.qualogy.qafe.mgwt.client.vo.functions.BuiltInFunctionGVO;
import com.qualogy.qafe.mgwt.client.vo.functions.SetPanelGVO;
import com.qualogy.qafe.mgwt.client.vo.ui.ComponentGVO;

public class SetPanelExecute extends BuiltInExecute {

	public void execute(BuiltInFunctionGVO builtInFunctionGVO, AbstractActivity activity) {
		if (builtInFunctionGVO instanceof SetPanelGVO) {
			SetPanelGVO setPanelGVO = (SetPanelGVO)builtInFunctionGVO;
			if (activity instanceof WindowActivity) {
				WindowActivity windowActivity = (WindowActivity)activity;
				setPanel(setPanelGVO, windowActivity);
			}
		}
		FunctionsExecutor.setProcessedBuiltIn(true);
	}
	
	private void setPanel(SetPanelGVO setPanelGVO, WindowActivity activity) {
		WindowPlace windowPlace = activity.getPlace();
		String context = windowPlace.getContext();
		String windowId = windowPlace.getId();
		String panelRefId = setPanelGVO.getTarget();
		String panelRefKey = activity.getClientFactory().generateComponentKey(panelRefId, windowId, context);
		List<UIObject> widgets = activity.getClientFactory().getComponentById(panelRefKey);
		if ((widgets == null) || (widgets.size() == 0)) {
			return;
		}
		UIObject widget = widgets.get(0);
		if (!(widget instanceof HasWidgets)) {
			return;
		}
		HasWidgets panelRef = (HasWidgets)widget;
		
		ComponentGVO definitionComponentGVO = setPanelGVO.getSrc();
		String viewKey = activity.getClientFactory().generateViewKey(windowId, context);
		activity.getClientFactory().removeComponents(viewKey, panelRefId);
		UIObject definitionComponent = AnyComponentRenderer.getInstance().render(definitionComponentGVO, panelRefId, context, windowId, context, activity);
		if (definitionComponent instanceof Widget) {
			panelRef.clear();
			panelRef.add((Widget)definitionComponent);
		
			if (panelRef instanceof Widget) {
				Widget parentWidget = ((Widget)panelRef).getParent();
				while (parentWidget != null) {
					if (parentWidget instanceof ScrollPanel) {
						ScrollPanel scrollPanel = (ScrollPanel)parentWidget;
						scrollPanel.refresh();
						break;
					}
					parentWidget = parentWidget.getParent();
				}
			}
			
			// Register the events if it is not coming from the rendering process (PanelRefRenderer),
			// because the events will be registered when the rendering process is done
			if (setPanelGVO.getSenderId() != null) {
				Map<UIObject,ComponentGVO> ownerComponents = activity.getClientFactory().getComponents(viewKey, panelRefId);
				registerEvents(ownerComponents, windowId, context, activity);	
			}
		}
	}
}
