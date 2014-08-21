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
package com.qualogy.qafe.gwt.client.vo.handlers;

import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;
import com.qualogy.qafe.gwt.client.ui.renderer.AnyComponentRenderer;
import com.qualogy.qafe.gwt.client.ui.renderer.RendererHelper;
import com.qualogy.qafe.gwt.client.util.ComponentRepository;
import com.qualogy.qafe.gwt.client.vo.functions.BuiltInFunctionGVO;
import com.qualogy.qafe.gwt.client.vo.functions.SetPanelGVO;
import com.qualogy.qafe.gwt.client.vo.ui.ComponentGVO;

public class SetPanelHandler extends AbstractBuiltInHandler {

	public boolean handleBuiltIn(UIObject sender, String listenerType, Map<String, String> mouseInfo, BuiltInFunctionGVO builtInFunctionGVO, String appId, String windowId, String eventSessionId) {
		SetPanelGVO setPanelGVO = (SetPanelGVO) builtInFunctionGVO;
		setPanel(setPanelGVO, appId, windowId, eventSessionId);
        return false;
	}
	
	private void setPanel(SetPanelGVO setPanelGVO, String appId, String windowId, String eventSessionId) {
		String panelRefId = setPanelGVO.getTarget();
        String key = RendererHelper.generateId(panelRefId, windowId, appId);
        List<UIObject> uiObjects = ComponentRepository.getInstance().getComponent(key);
        if ((uiObjects != null) && (uiObjects.size() > 0)) {
        	UIObject uiObject = uiObjects.get(0);
        	if (uiObject instanceof SimplePanel) {
				SimplePanel simplePanel = (SimplePanel) uiObject;
				Widget innerComponent = simplePanel.getWidget();
				if (innerComponent != null) {
					ComponentRepository.getInstance().clearContainerComponent(innerComponent);
				}
				ComponentGVO panelDefinitionGVO = setPanelGVO.getSrc();
				UIObject panelDefinition = AnyComponentRenderer.getInstance().render(panelDefinitionGVO, eventSessionId, windowId, appId);
				if (panelDefinition instanceof Widget) {
					simplePanel.setWidget((Widget)panelDefinition);
				}
			}
        }
	}
}
