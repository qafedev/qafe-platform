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
import com.qualogy.qafe.mgwt.client.ui.component.HasData;
import com.qualogy.qafe.mgwt.client.vo.functions.BuiltInComponentGVO;
import com.qualogy.qafe.mgwt.client.vo.functions.BuiltInFunctionGVO;
import com.qualogy.qafe.mgwt.client.vo.functions.CopyGVO;
import com.qualogy.qafe.mgwt.shared.QAMLUtil;

public class CopyExecute extends BuiltInExecute {

	public void execute(BuiltInFunctionGVO builtInFunctionGVO, AbstractActivity activity) {
		if (builtInFunctionGVO instanceof CopyGVO) {
			CopyGVO copyGVO = (CopyGVO)builtInFunctionGVO;
			if (activity instanceof WindowActivity) {
				WindowActivity windowActivity = (WindowActivity)activity;
				copy(copyGVO, windowActivity);
			}
		}
		FunctionsExecutor.setProcessedBuiltIn(true);
	}
	
	private void copy(CopyGVO copyGVO, WindowActivity activity) {
		BuiltInComponentGVO fromComponentGVO = copyGVO.getFromGVO();
		List<UIObject> fromWidgets = getWidgets(fromComponentGVO, activity);
		if (QAMLUtil.isEmpty(fromWidgets)) {
			return;
		}
		BuiltInComponentGVO toComponentGVO = copyGVO.getToGVO();
		List<UIObject> toWidgets = getWidgets(toComponentGVO, activity);
		if (QAMLUtil.isEmpty(toWidgets)) {
			return;
		}
		if (fromWidgets.size() != toWidgets.size()) {
			return;
		}
		for (int i=0; i<fromWidgets.size(); i++) {
			UIObject fromWidget = fromWidgets.get(i);
			UIObject toWidget = toWidgets.get(i);
			if (!(fromWidget instanceof HasData)) {
				continue;
			}
			if (!(toWidget instanceof HasData)) {
				continue;
			}
			Object value = ((HasData)fromWidget).getData();
			((HasData)toWidget).setData(value);
		}
	}
}
