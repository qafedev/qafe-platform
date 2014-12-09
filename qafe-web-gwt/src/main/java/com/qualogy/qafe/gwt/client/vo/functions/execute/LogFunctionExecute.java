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
package com.qualogy.qafe.gwt.client.vo.functions.execute;

import org.gwt.mosaic.ui.client.WindowPanel;

import com.qualogy.qafe.gwt.client.component.QWindowPanel;
import com.qualogy.qafe.gwt.client.context.ClientApplicationContext;
import com.qualogy.qafe.gwt.client.vo.functions.BuiltInFunctionGVO;
import com.qualogy.qafe.gwt.client.vo.functions.LogFunctionGVO;

public class LogFunctionExecute implements ExecuteCommand {

	public void execute(BuiltInFunctionGVO builtInFunction) {
		if (builtInFunction instanceof LogFunctionGVO) {
			LogFunctionGVO logFunctionGVO = (LogFunctionGVO)builtInFunction;
			ClientApplicationContext.getInstance().log(logFunctionGVO.getMessage());
			if (!logFunctionGVO.getDebug().booleanValue()){
				WindowPanel wp = ClientApplicationContext.getInstance().getWindow(logFunctionGVO.getUuid(), logFunctionGVO.getWindowId());
				if (wp instanceof QWindowPanel){
					QWindowPanel qwp = (QWindowPanel)wp;
					qwp.showMessage(logFunctionGVO.getMessage(),logFunctionGVO.getDelay(),logFunctionGVO.getStyleClass(),logFunctionGVO.getStyleProperties());
				}
			}
			
		}
		FunctionsExecutor.setProcessedBuiltIn(true);

	}

}
