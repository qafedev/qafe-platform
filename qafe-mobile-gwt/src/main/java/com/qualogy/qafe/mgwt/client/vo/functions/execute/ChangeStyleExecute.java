/**
 * Copyright 2008-2017 Qualogy Solutions B.V.
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

import com.google.gwt.user.client.ui.UIObject;
import com.qualogy.qafe.mgwt.client.activities.AbstractActivity;
import com.qualogy.qafe.mgwt.client.activities.WindowActivity;
import com.qualogy.qafe.mgwt.client.ui.component.ComponentHelper;
import com.qualogy.qafe.mgwt.client.vo.functions.BuiltInComponentGVO;
import com.qualogy.qafe.mgwt.client.vo.functions.BuiltInFunctionGVO;
import com.qualogy.qafe.mgwt.client.vo.functions.ChangeStyleActionGVO;
import com.qualogy.qafe.mgwt.client.vo.functions.ChangeStyleGVO;
import com.qualogy.qafe.mgwt.shared.QAMLConstants;
import com.qualogy.qafe.mgwt.shared.QAMLUtil;

public class ChangeStyleExecute extends BuiltInExecute {

	public void execute(BuiltInFunctionGVO builtInFunctionGVO, AbstractActivity activity) {
		if (builtInFunctionGVO instanceof ChangeStyleGVO) {
			ChangeStyleGVO changeStyleGVO = (ChangeStyleGVO)builtInFunctionGVO;
			if (activity instanceof WindowActivity) {
				WindowActivity windowActivity = (WindowActivity)activity;
				changeStyle(changeStyleGVO, windowActivity);
			}
		}
		FunctionsExecutor.setProcessedBuiltIn(true);
	}
	
	private void changeStyle(ChangeStyleGVO changeStyleGVO, WindowActivity activity) {
		List<BuiltInComponentGVO> builtInComponents = changeStyleGVO.getComponents();
		if (builtInComponents == null) {
			return;
		}
		List<ChangeStyleActionGVO> changeStyleActions = changeStyleGVO.getActions();
		if (changeStyleActions == null) {
			return;
		}
		for (BuiltInComponentGVO builtInComponentGVO : builtInComponents) {
			List<UIObject> widgets = getWidgets(builtInComponentGVO, activity);			
			if (QAMLUtil.isEmpty(widgets)) {
				continue;
			}
			for (UIObject widget : widgets) {
				for (ChangeStyleActionGVO changeStyleAction : changeStyleActions) {
					String action = changeStyleAction.getAction();
					String attribute = changeStyleAction.getKey();
					String attributeValue = changeStyleAction.getValue();
					String styleClass = changeStyleAction.getStyle();
					if (!QAMLUtil.isEmpty(attribute)) {
						if (QAMLConstants.STYLE_REMOVE.equalsIgnoreCase(action)) {
							attributeValue = null;
						}
						ComponentHelper.setStyle(widget, attribute, attributeValue);
					}
					if (!QAMLUtil.isEmpty(styleClass)) {
						if (QAMLConstants.STYLE_REMOVE.equalsIgnoreCase(action)) {
							ComponentHelper.removeStyle(widget, styleClass);
						} else if (QAMLConstants.STYLE_SET.equalsIgnoreCase(action)) {
							ComponentHelper.addStyle(widget, styleClass);
						}
					}
				}
			}
		}
	}
}