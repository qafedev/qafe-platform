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
package com.qualogy.qafe.gwt.client.vo.functions.execute;

import java.util.List;

import com.google.gwt.user.client.ui.UIObject;
import com.qualogy.qafe.gwt.client.ui.renderer.RendererHelper;
import com.qualogy.qafe.gwt.client.vo.functions.BuiltInComponentGVO;
import com.qualogy.qafe.gwt.client.vo.functions.BuiltInFunctionGVO;
import com.qualogy.qafe.gwt.client.vo.functions.ToggleGVO;

public class ToggleExecute implements ExecuteCommand {

	public void execute(BuiltInFunctionGVO builtInFunction) {
		if (builtInFunction instanceof ToggleGVO) {
			

			if (builtInFunction.getComponents() != null) {
				for (BuiltInComponentGVO builtInComponentGVO : builtInFunction.getComponents()) {
					
			
					String component = builtInComponentGVO.getComponentIdUUID();
					List<UIObject> uiObjects = null;
					if (component!=null){ 
							uiObjects = RendererHelper.getComponent(component);							
					} else {
						uiObjects = RendererHelper.getNamedComponent(builtInComponentGVO.getComponentName());
					}
					if (uiObjects !=null){
						for (UIObject uiObject : uiObjects) {
							if (uiObject.isVisible()){
								uiObject.setVisible(false);
							} else {
								uiObject.setVisible(true);
							}	
						}
						

					}
				}
			}
		}
		FunctionsExecutor.setProcessedBuiltIn(true);

	}

}
