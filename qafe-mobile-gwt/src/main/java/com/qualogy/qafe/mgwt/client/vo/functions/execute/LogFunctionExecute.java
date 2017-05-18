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

import com.qualogy.qafe.mgwt.client.activities.AbstractActivity;
import com.qualogy.qafe.mgwt.client.activities.WindowActivity;
import com.qualogy.qafe.mgwt.client.views.AbstractView;
import com.qualogy.qafe.mgwt.client.vo.functions.BuiltInFunctionGVO;
import com.qualogy.qafe.mgwt.client.vo.functions.LogFunctionGVO;

public class LogFunctionExecute extends BuiltInExecute {

	public void execute(BuiltInFunctionGVO builtInFunctionGVO, AbstractActivity activity) {
		if (builtInFunctionGVO instanceof LogFunctionGVO) {
			LogFunctionGVO logFunctionGVO = (LogFunctionGVO)builtInFunctionGVO;
			if (activity instanceof WindowActivity) {
				WindowActivity windowActivity = (WindowActivity)activity;
				log(logFunctionGVO, windowActivity);
			}
		}
		FunctionsExecutor.setProcessedBuiltIn(true);
	}
	
	private void log(LogFunctionGVO logFunctionGVO, WindowActivity activity) {
		if (logFunctionGVO.getDebug().booleanValue()) {
			return;
		}
		String logMessage = logFunctionGVO.getMessage();
		int logDelay = logFunctionGVO.getDelay();
		String styleClass = logFunctionGVO.getStyleClass();
		String[][] inlineStyles = logFunctionGVO.getStyleProperties();
		AbstractView view = activity.getView();
		view.notify(logMessage, logDelay, styleClass, inlineStyles);
	}
}
