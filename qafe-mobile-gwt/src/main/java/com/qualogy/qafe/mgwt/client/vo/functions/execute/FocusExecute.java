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

import java.util.List;

import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;
import com.qualogy.qafe.mgwt.client.activities.AbstractActivity;
import com.qualogy.qafe.mgwt.client.activities.WindowActivity;
import com.qualogy.qafe.mgwt.client.ui.component.IsFocusable;
import com.qualogy.qafe.mgwt.client.ui.component.IsStackable;
import com.qualogy.qafe.mgwt.client.vo.functions.BuiltInComponentGVO;
import com.qualogy.qafe.mgwt.client.vo.functions.BuiltInFunctionGVO;
import com.qualogy.qafe.mgwt.client.vo.functions.FocusGVO;
import com.qualogy.qafe.mgwt.shared.QAMLUtil;

public class FocusExecute extends BuiltInExecute implements ExecuteCommand {

	public void execute(BuiltInFunctionGVO builtInFunctionGVO, AbstractActivity activity) {
		if (builtInFunctionGVO instanceof FocusGVO) {
			FocusGVO focusGVO = (FocusGVO)builtInFunctionGVO;
			if (activity instanceof WindowActivity) {
				WindowActivity windowActivity = (WindowActivity)activity;
				focus(focusGVO, windowActivity);
			}
		}
		FunctionsExecutor.setProcessedBuiltIn(true);
	}
	
	private void focus(FocusGVO focusGVO, WindowActivity activity) {
		BuiltInComponentGVO builtInComponentGVO = focusGVO.getBuiltInComponentGVO();
		List<UIObject> widgets = getWidgets(builtInComponentGVO, activity);
		if (QAMLUtil.isEmpty(widgets)) {
			return;
		}
		for (UIObject widget : widgets) {
			if (widget instanceof IsFocusable) {
				handleParentVisibility(((Widget)widget).getParent());
				IsFocusable focusable = (IsFocusable)widget;
				focusable.setFocus(true);
			}
		}
	}
	
	private void handleParentVisibility(Widget widget) {
		if (widget instanceof IsStackable) {
			IsStackable stackable = (IsStackable)widget;
			stackable.setSelected();
		} else if (widget != null) {
			handleParentVisibility(widget.getParent());
		}
	}
}
