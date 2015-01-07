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

import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;
import com.qualogy.qafe.gwt.client.ui.renderer.AnyComponentRenderer;
import com.qualogy.qafe.gwt.client.ui.renderer.RendererHelper;
import com.qualogy.qafe.gwt.client.util.ComponentRepository;
import com.qualogy.qafe.gwt.client.vo.functions.BuiltInFunctionGVO;
import com.qualogy.qafe.gwt.client.vo.functions.SetPanelGVO;

@Deprecated
public class SetPanelExecute implements ExecuteCommand {

	public void execute(BuiltInFunctionGVO builtInFunction) {
		if (builtInFunction instanceof SetPanelGVO) {
			SetPanelGVO setPanel = (SetPanelGVO) builtInFunction;
			List<UIObject> uiObjects = RendererHelper.getComponent(setPanel.getBuiltInComponentGVO().getComponentIdUUID());
			if (uiObjects!=null && uiObjects.iterator().hasNext()){
				UIObject uiObject = uiObjects.iterator().next();
			//for (UIObject uiObject : uiObjects) {
				// .getComponentId() );
				if (uiObject instanceof SimplePanel) {

					SimplePanel simplePanel = (SimplePanel) uiObject;

					Widget innerComponent = simplePanel.getWidget();
					if (innerComponent != null) {
						ComponentRepository.getInstance().clearContainerComponent(innerComponent);
					}
					UIObject ui = AnyComponentRenderer.getInstance().render(setPanel.getSrc(), setPanel.getUuid(), setPanel.getBuiltInComponentGVO().getWindowId(), setPanel.getSrc().getContext());

					if (ui instanceof Widget) {
						simplePanel.setWidget((Widget) ui);
					}

				}

			}
		}
	}

}
