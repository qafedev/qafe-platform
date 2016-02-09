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

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;
import com.qualogy.qafe.mgwt.client.activities.AbstractActivity;
import com.qualogy.qafe.mgwt.client.context.ClientApplicationContext;
import com.qualogy.qafe.mgwt.client.vo.functions.BuiltInFunctionGVO;

public class FunctionsExecutor {
	
	private static FunctionsExecutor singleton = null;
	private static boolean processedBuiltIn = false;
	private final Map<String,ExecuteCommand> EXECUTOR_MAP = new HashMap<String,ExecuteCommand>();

	private FunctionsExecutor() {
		EXECUTOR_MAP.put("com.qualogy.qafe.gwt.client.vo.functions.ChangeStyleGVO", new ChangeStyleExecute());
		EXECUTOR_MAP.put("com.qualogy.qafe.gwt.client.vo.functions.RegExpValidateGVO", new RegExpValidateExecute());
		EXECUTOR_MAP.put("com.qualogy.qafe.gwt.client.vo.functions.SetValueGVO", new SetValueExecute());
		EXECUTOR_MAP.put("com.qualogy.qafe.gwt.client.vo.functions.SetPanelGVO", new SetPanelExecute());
		EXECUTOR_MAP.put("com.qualogy.qafe.gwt.client.vo.functions.ClearGVO", new ClearExecute());
		EXECUTOR_MAP.put("com.qualogy.qafe.gwt.client.vo.functions.FocusGVO", new FocusExecute());
		EXECUTOR_MAP.put("com.qualogy.qafe.gwt.client.vo.functions.CopyGVO", new CopyExecute());
		EXECUTOR_MAP.put("com.qualogy.qafe.gwt.client.vo.functions.ToggleGVO", new ToggleExecute());
		EXECUTOR_MAP.put("com.qualogy.qafe.gwt.client.vo.functions.SetPropertyGVO", new SetPropertyExecute());		
		EXECUTOR_MAP.put("com.qualogy.qafe.gwt.client.vo.functions.OpenWindowGVO", new OpenWindowExecute());
		EXECUTOR_MAP.put("com.qualogy.qafe.gwt.client.vo.functions.CloseWindowGVO", new CloseWindowExecute());
		EXECUTOR_MAP.put("com.qualogy.qafe.gwt.client.vo.functions.dialog.GenericDialogGVO", new GenericDialogExecute());
		EXECUTOR_MAP.put("com.qualogy.qafe.gwt.client.vo.functions.LogFunctionGVO", new LogFunctionExecute());
		EXECUTOR_MAP.put("com.qualogy.qafe.gwt.client.vo.functions.ShowPanelGVO", new ShowPanelExecute());
		EXECUTOR_MAP.put("com.qualogy.qafe.gwt.client.vo.functions.ClosePanelGVO", new ClosePanelExecute());
	};

	public static FunctionsExecutor getInstance() {
		if (singleton == null) {
			singleton = new FunctionsExecutor();
		}
		return singleton;
	}

	public void execute(BuiltInFunctionGVO builtInFunctionGVO, AbstractActivity activity) {
		if (builtInFunctionGVO == null) {
			return;
		}
		if (activity == null) {
			return;
		}
		try {
			ExecuteCommand command = EXECUTOR_MAP.get(builtInFunctionGVO.getClassName());
			if (command != null) {
				GWT.log("Function Execute: " + command, null);
				processedBuiltIn = false;
				command.execute(builtInFunctionGVO, activity);
				if (!processedBuiltIn) {
					Timer timer = new Timer() {
						public void run() {
							if (processedBuiltIn) {
								cancel();
							}
						}
					};
					timer.run();
					timer.scheduleRepeating(100);
				}
			} else {
				ClientApplicationContext.getInstance().log("Unable to find executor for class " + builtInFunctionGVO.getClassName(), null);
			}
		} catch (Exception e) {
			ClientApplicationContext.getInstance().log("FunctionsExecutor:execute  failed " + builtInFunctionGVO.getClassName(), "The error that occured: " + e.getMessage(), true, false, e);			
		}
	}

	public static boolean isProcessedBuiltIn() {
		return FunctionsExecutor.processedBuiltIn;
	}

	public static void setProcessedBuiltIn(boolean processedBuiltIn) {
		FunctionsExecutor.processedBuiltIn = processedBuiltIn;
	}
}
