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
package com.qualogy.qafe.gwt.client.ui.renderer;

import java.util.List;

import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;
import com.qualogy.qafe.gwt.client.util.ComponentRepository;
import com.qualogy.qafe.gwt.client.vo.ui.ComponentGVO;
import com.qualogy.qafe.gwt.client.vo.ui.PanelRefGVO;

public class PanelRefRenderer extends AbstractComponentRenderer {

	public UIObject render(ComponentGVO component, String uuid,String parent, String context) {
		SimplePanel uiObject = null;
		if (component != null) {
			if (component instanceof PanelRefGVO) {
				PanelRefGVO gvo = (PanelRefGVO) component;
				uiObject = new SimplePanel();
				RendererHelper.fillIn(component, uiObject, uuid,parent, context);	
				UIObject innerUIObject =renderChildComponent(gvo.getRef(), uuid, parent, context);
				//correction needs to be made because the same component is generated twice in the list (mem reference is the same, but two instances are stored)
				String key = RendererHelper.generateId(component.getId(), parent, context);
				List<UIObject> uiObjects =ComponentRepository.getInstance().getComponent(key);
				if (uiObjects!=null && uiObjects.size()>1){
					uiObjects.remove(uiObjects.size()-1);
				}
				
				List<UIObject> uiObjects2 =ComponentRepository.getInstance().getComponent(key);
				if (innerUIObject instanceof Widget){
					uiObject.setWidget((Widget)innerUIObject);
				}											
			}
		}

		return uiObject;
	}
}
