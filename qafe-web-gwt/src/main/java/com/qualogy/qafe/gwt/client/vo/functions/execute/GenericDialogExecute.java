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
import com.qualogy.qafe.gwt.client.component.DialogComponent;
import com.qualogy.qafe.gwt.client.component.QWindowPanel;
import com.qualogy.qafe.gwt.client.context.ClientApplicationContext;
import com.qualogy.qafe.gwt.client.util.ComponentRepository;
import com.qualogy.qafe.gwt.client.vo.functions.BuiltInFunctionGVO;
import com.qualogy.qafe.gwt.client.vo.functions.dialog.GenericDialogGVO;

public class GenericDialogExecute implements ExecuteCommand {	

	public void execute(BuiltInFunctionGVO builtInFunction) {
		if (builtInFunction instanceof GenericDialogGVO) {
			GenericDialogGVO dialog = (GenericDialogGVO)builtInFunction;
			
			// left and top is used to figure out the center point.
			int left = 0;
			int top = 0;
			List<UIObject> uiObjects = ComponentRepository.getInstance().getComponent(dialog.getParent());
			if (uiObjects!=null && uiObjects.iterator().hasNext()){
				UIObject uiObject = uiObjects.iterator().next();
				if(uiObject instanceof QWindowPanel){
					QWindowPanel qWindowPanel = (QWindowPanel)uiObject;
					int windowHeight = Integer.parseInt(qWindowPanel.getHeight().substring(0, qWindowPanel.getHeight().indexOf("px")));
					int windowWidth = Integer.parseInt(qWindowPanel.getWidth().substring(0, qWindowPanel.getWidth().indexOf("px")));
					int windowXCoordiniate = ClientApplicationContext.getInstance().getStartXPosition();
					int windowYCoordiniate = ClientApplicationContext.getInstance().getStartYPosition();
					
					left = (windowXCoordiniate + windowWidth)/2;
					top = (windowYCoordiniate + windowHeight)/2;
				}
			}
			DialogComponent.showDialog(dialog.getTitle(),dialog.getMessage(), dialog.getType(), new NullPointerException().getLocalizedMessage(), left, top);
			
		}
		FunctionsExecutor.setProcessedBuiltIn(true);
	}

}
