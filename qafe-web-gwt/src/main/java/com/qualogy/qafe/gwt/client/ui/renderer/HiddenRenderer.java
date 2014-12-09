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

import com.google.gwt.user.client.ui.Hidden;
import com.google.gwt.user.client.ui.UIObject;
import com.qualogy.qafe.gwt.client.component.QHidden;
import com.qualogy.qafe.gwt.client.vo.ui.ComponentGVO;
import com.qualogy.qafe.gwt.client.vo.ui.HiddenGVO;

public class HiddenRenderer extends AbstractComponentRenderer {

	public UIObject render(ComponentGVO component, String uuid,String parent, String context) {
		Hidden uiObject = null;
		if (component != null) {
			if (component instanceof HiddenGVO) {
				HiddenGVO gvo = (HiddenGVO) component;
				uiObject = new QHidden();
				uiObject.setValue(gvo.getValue());
				RendererHelper.fillIn(component, uiObject, uuid,parent, context);	
			}
		}

		return uiObject;
	}
}
