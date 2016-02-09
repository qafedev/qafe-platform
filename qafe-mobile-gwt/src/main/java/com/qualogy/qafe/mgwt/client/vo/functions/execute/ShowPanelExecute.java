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
package com.qualogy.qafe.mgwt.client.vo.functions.execute;

import java.util.Map;

import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;
import com.qualogy.qafe.mgwt.client.activities.AbstractActivity;
import com.qualogy.qafe.mgwt.client.activities.WindowActivity;
import com.qualogy.qafe.mgwt.client.places.WindowPlace;
import com.qualogy.qafe.mgwt.client.ui.component.QShowPanel;
import com.qualogy.qafe.mgwt.client.ui.renderer.AnyComponentRenderer;
import com.qualogy.qafe.mgwt.client.vo.functions.BuiltInFunctionGVO;
import com.qualogy.qafe.mgwt.client.vo.functions.ShowPanelGVO;
import com.qualogy.qafe.mgwt.client.vo.ui.ComponentGVO;
import com.qualogy.qafe.mgwt.shared.QAMLConstants;

public class ShowPanelExecute extends BuiltInExecute {

	@Override
	public void execute(BuiltInFunctionGVO builtInFunctionGVO, AbstractActivity activity) {
		if (builtInFunctionGVO instanceof ShowPanelGVO) {
			ShowPanelGVO showPanelGVO = (ShowPanelGVO)builtInFunctionGVO;
			if (activity instanceof WindowActivity) {
				WindowActivity windowActivity = (WindowActivity)activity;
				showPanel(showPanelGVO, windowActivity);
			}
		}
		FunctionsExecutor.setProcessedBuiltIn(true);
	}
	
	private void showPanel(ShowPanelGVO showPanelGVO, WindowActivity activity) {
		ComponentGVO definitionComponentGVO = showPanelGVO.getSrc();
		if (definitionComponentGVO == null) {
			return;
		}
		WindowPlace windowPlace = activity.getPlace();
		String context = windowPlace.getContext();
		String windowId = windowPlace.getId();
		String viewKey = activity.getClientFactory().generateViewKey(windowId, context);
		String ownerId = showPanelGVO.getSrc().getId();
		UIObject ownerComponent = activity.getClientFactory().getOwner(viewKey, ownerId);
		if (ownerComponent == null) {
			UIObject definitionComponent = AnyComponentRenderer.getInstance().render(definitionComponentGVO, ownerId, context, windowId, context, activity);
			Map<UIObject,ComponentGVO> ownerComponents = activity.getClientFactory().getComponents(viewKey, ownerId);
			registerEvents(ownerComponents, windowId, context, activity);
			
			QShowPanel showPanel = new QShowPanel();
			if (definitionComponent instanceof Widget) {
				showPanel.setWidget((Widget)definitionComponent);
			}
			ownerComponent = showPanel;
			activity.getClientFactory().addOwner(viewKey, ownerId, ownerComponent, definitionComponentGVO);
		}
		
		if (ownerComponent instanceof QShowPanel) {
			QShowPanel showPanel = (QShowPanel)ownerComponent;
			boolean autoHide = showPanelGVO.isAutoHide();
			boolean modal = showPanelGVO.isModal();
			int posLeft = showPanelGVO.getLeft(); 
			int posTop = showPanelGVO.getTop();
			showPanel.setAutoHideEnabled(autoHide);
			showPanel.setModal(modal);
			showPanel.setPopupPosition(posLeft, posTop);
			String position = showPanelGVO.getPosition();
			if (QAMLConstants.INTERNAL_POSITION_CENTER.equals(position)) {
				showPanel.center();
			}
			showPanel.show();	
		}
	}
}