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

import com.google.gwt.user.client.ui.UIObject;
import com.qualogy.qafe.mgwt.client.activities.AbstractActivity;
import com.qualogy.qafe.mgwt.client.activities.WindowActivity;
import com.qualogy.qafe.mgwt.client.ui.component.IsTextField;
import com.qualogy.qafe.mgwt.client.views.AbstractView;
import com.qualogy.qafe.mgwt.client.vo.functions.BuiltInComponentGVO;
import com.qualogy.qafe.mgwt.client.vo.functions.BuiltInFunctionGVO;
import com.qualogy.qafe.mgwt.client.vo.functions.RegExpValidateGVO;
import com.qualogy.qafe.mgwt.shared.QAMLUtil;

public class RegExpValidateExecute extends BuiltInExecute {

	public void execute(BuiltInFunctionGVO builtInFunctionGVO, AbstractActivity activity) {
		if (builtInFunctionGVO instanceof RegExpValidateGVO) {
			RegExpValidateGVO regExpValidateGVO = (RegExpValidateGVO)builtInFunctionGVO;
			if (activity instanceof WindowActivity) {
				WindowActivity windowActivity = (WindowActivity)activity;
				validateRegExp(regExpValidateGVO, windowActivity);
			}
		}
		FunctionsExecutor.setProcessedBuiltIn(true);
	}
	
	private void validateRegExp(RegExpValidateGVO regExpValidateGVO, WindowActivity activity) {
		String regExp = regExpValidateGVO.getRegExp();
		if (regExp == null) {
			return;
		}
		List<BuiltInComponentGVO> builtInComponents = regExpValidateGVO.getComponents();
		if (builtInComponents == null) {
			return;
		}
		for (BuiltInComponentGVO builtInComponentGVO : builtInComponents) {
			List<UIObject> widgets = getWidgets(builtInComponentGVO, activity);			
			if (QAMLUtil.isEmpty(widgets)) {
				continue;
			}
			for (UIObject widget : widgets) {
				if (widget instanceof IsTextField) {
					IsTextField isTextField = (IsTextField)widget;
					Object data = isTextField.getData();
					if (data instanceof String) {
						String value = (String)data;
						if (value.matches(regExp)) {
							continue;
						}
						String regExpMessage = regExpValidateGVO.getMessage();
						AbstractView view = activity.getView();
						view.alert("", regExpMessage + ": '" + value + "'");
					}
				}
			}
		}
	}
}