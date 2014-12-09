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

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.UmbrellaException;
import com.google.gwt.user.client.Timer;
import com.qualogy.qafe.gwt.client.context.ClientApplicationContext;
import com.qualogy.qafe.gwt.client.vo.functions.BuiltInFunctionGVO;

public class FunctionsExecutor {
	private static FunctionsExecutor singleton = null;

	private final Map<String,ExecuteCommand> EXECUTOR_MAP = new HashMap<String,ExecuteCommand>();

	private static boolean processedBuiltIn = false;

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
		EXECUTOR_MAP.put("com.qualogy.qafe.gwt.client.vo.functions.SetRestrictionGVO", new SetRestrictionExecute());
		EXECUTOR_MAP.put("com.qualogy.qafe.gwt.client.vo.functions.ShowPanelGVO", new ShowPanelExecute());
		EXECUTOR_MAP.put("com.qualogy.qafe.gwt.client.vo.functions.ClosePanelGVO", new ClosePanelExecute());
		
		EXECUTOR_MAP.put("com.qualogy.qafe.gwt.client.vo.functions.UpdateModelGVO", new UpdateModelExecute());
	};

	public static FunctionsExecutor getInstance() {
		if (singleton == null) {
			singleton = new FunctionsExecutor();
		}
		return singleton;
	}

	public void execute(BuiltInFunctionGVO builtInFunction) {

		if (builtInFunction != null) {
			try {
				ExecuteCommand command = (ExecuteCommand) EXECUTOR_MAP.get(builtInFunction.getClassName());
				if (command != null) {
					GWT.log("Function Execute:" +command, null);
					processedBuiltIn = false;
					command.execute(builtInFunction);
					if (!processedBuiltIn) {
						Timer t = new Timer() {
							public void run() {

								if (processedBuiltIn) {
									cancel();
								}

							}
						};
						t.run();
						t.scheduleRepeating(100);
					}
				} else {
					ClientApplicationContext.getInstance().log("Unable to find executor for class " + builtInFunction.getClassName(), null);
				}
			} catch (UmbrellaException e) {
				ClientApplicationContext.getInstance().log("FunctionsExecutor:execute failed " + builtInFunction.getClassName(), "Cause: " + e.getCauses(), true, false, e);
			} catch (Exception e) {
				ClientApplicationContext.getInstance().log("FunctionsExecutor:execute failed " + builtInFunction.getClassName(), "The error that occured:  " + e.getMessage(), true, false, e);			
			}
		}
	}

	public static boolean isProcessedBuiltIn() {
		return processedBuiltIn;
	}

	public static void setProcessedBuiltIn(boolean processedBuiltIn) {
		FunctionsExecutor.processedBuiltIn = processedBuiltIn;
	}

}
