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
package com.qualogy.qafe.mgwt.client.vo.functions.execute;

import com.google.gwt.user.client.ui.UIObject;
import com.qualogy.qafe.mgwt.client.activities.AbstractActivity;
import com.qualogy.qafe.mgwt.client.activities.WindowActivity;
import com.qualogy.qafe.mgwt.client.places.WindowPlace;
import com.qualogy.qafe.mgwt.client.ui.component.QShowPanel;
import com.qualogy.qafe.mgwt.client.vo.functions.BuiltInFunctionGVO;
import com.qualogy.qafe.mgwt.client.vo.functions.ClosePanelGVO;

public class ClosePanelExecute implements ExecuteCommand {

	@Override
	public void execute(BuiltInFunctionGVO builtInFunctionGVO, AbstractActivity activity) {
		if (builtInFunctionGVO instanceof ClosePanelGVO) {
			ClosePanelGVO closePanelGVO = (ClosePanelGVO)builtInFunctionGVO;
			if (activity instanceof WindowActivity) {
				WindowActivity windowActivity = (WindowActivity)activity;
				closePanel(closePanelGVO, windowActivity);
			}
		}
		FunctionsExecutor.setProcessedBuiltIn(true);
	}
	
	private void closePanel(ClosePanelGVO closePanelGVO, WindowActivity activity) {
		WindowPlace windowPlace = activity.getPlace();
		String context = windowPlace.getContext();
		String windowId = windowPlace.getId();
		String viewKey = activity.getClientFactory().generateViewKey(windowId, context);
		String ownerId = closePanelGVO.getRef();
		UIObject ownerComponent = activity.getClientFactory().getOwner(viewKey, ownerId);
		if (ownerComponent instanceof QShowPanel) {
			((QShowPanel)ownerComponent).hide();
		}
	}
}
