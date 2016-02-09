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
import com.qualogy.qafe.gwt.client.component.ShowPanelComponent;
import com.qualogy.qafe.gwt.client.util.ComponentRepository;
import com.qualogy.qafe.gwt.client.vo.functions.BuiltInFunctionGVO;
import com.qualogy.qafe.gwt.client.vo.functions.ClosePanelGVO;

public class ClosePanelExecute implements ExecuteCommand {

	public void execute(BuiltInFunctionGVO builtInFunction) {
		if (builtInFunction instanceof ClosePanelGVO) {
			ClosePanelGVO closePanelGVO = (ClosePanelGVO) builtInFunction;
			String id = "showPanel_" + closePanelGVO.getBuiltInComponentGVO().getComponentIdUUID();
			List<UIObject> uiObjects = ComponentRepository.getInstance().getComponent(id);
			if(uiObjects != null){
				UIObject uiObject = uiObjects.iterator().next();
				if (uiObject instanceof ShowPanelComponent) {
					ComponentRepository.getInstance().remove("showPanel_"+closePanelGVO.getRef());	
					ShowPanelComponent showPanel = (ShowPanelComponent)uiObject;
					showPanel.hide();//This will call the onDetach of the component.					
				}
			}
		}
	}

}
