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
package com.qualogy.qafe.gwt.client.vo.functions.execute;

import java.util.List;

import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.UIObject;
import com.qualogy.qafe.gwt.client.ui.renderer.RendererHelper;
import com.qualogy.qafe.gwt.client.vo.functions.BuiltInFunctionGVO;
import com.qualogy.qafe.gwt.client.vo.functions.CopyGVO;

public class CopyExecute implements ExecuteCommand {

	public void execute(BuiltInFunctionGVO builtInFunction) {
		if (builtInFunction instanceof CopyGVO) {
			CopyGVO copy = (CopyGVO) builtInFunction;

			List<UIObject> uiObjectsFrom = null;
			if (copy.getFromGVO().getComponentIdUUID() != null) {
				uiObjectsFrom = RendererHelper.getComponent(copy.getFromGVO().getComponentIdUUID());
			} else {
				uiObjectsFrom = RendererHelper.getNamedComponent(copy.getFromGVO().getComponentName());
			}

			List<UIObject> uiObjectsTo = null;
			if (copy.getToGVO().getComponentIdUUID() != null) {
				uiObjectsTo = RendererHelper.getComponent(copy.getToGVO().getComponentIdUUID());
			} else {
				uiObjectsTo = RendererHelper.getNamedComponent(copy.getToGVO().getComponentName());
			}

			if (uiObjectsTo != null && uiObjectsFrom != null) {
				if (uiObjectsTo.size()==uiObjectsFrom.size()){
					for (int i=0;i<uiObjectsTo.size();i++){
						UIObject uiObjectFrom = uiObjectsFrom.get(i);
						UIObject uiObjectTo = uiObjectsTo.get(i);
						
						if (uiObjectFrom instanceof HasText) {
							HasText hasTextFrom = (HasText) uiObjectFrom;
							String fromValue = hasTextFrom.getText();
							if (uiObjectTo instanceof HasText) {
								HasText hasTextTo = (HasText) uiObjectTo;
								hasTextTo.setText(fromValue);
							}
						}
					
					}
				}
			}
		}
		FunctionsExecutor.setProcessedBuiltIn(true);
	}

}
