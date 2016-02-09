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

import com.qualogy.qafe.mgwt.client.activities.AbstractActivity;
import com.qualogy.qafe.mgwt.client.activities.WindowActivity;
import com.qualogy.qafe.mgwt.client.places.WindowPlace;
import com.qualogy.qafe.mgwt.client.vo.functions.BuiltInFunctionGVO;
import com.qualogy.qafe.mgwt.client.vo.functions.CloseWindowGVO;

public class CloseWindowExecute extends BuiltInExecute {

	public void execute(BuiltInFunctionGVO builtInFunctionGVO, AbstractActivity activity) {
		if (builtInFunctionGVO instanceof CloseWindowGVO) {
			CloseWindowGVO closeWindowGVO = (CloseWindowGVO)builtInFunctionGVO;
			if (activity instanceof WindowActivity) {
				WindowActivity windowActivity = (WindowActivity)activity;
				closeWindow(closeWindowGVO, windowActivity);
			}
		}
		FunctionsExecutor.setProcessedBuiltIn(true);
	}
	
	private void closeWindow(CloseWindowGVO closeWindowGVO, WindowActivity activity) {
		String windowId = closeWindowGVO.getWindow();
		String context = closeWindowGVO.getUuid();
		WindowPlace windowPlace = activity.getPlace();
		if (!windowPlace.getId().equals(windowId)) {
			return;
		}
		if (!windowPlace.getContext().equals(context)) {
			return;
		}
		activity.goBack();
	}
}
