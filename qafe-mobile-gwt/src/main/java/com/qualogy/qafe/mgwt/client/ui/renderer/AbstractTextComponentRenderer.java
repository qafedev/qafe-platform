/**
 * Copyright 2008-2017 Qualogy Solutions B.V.
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
package com.qualogy.qafe.mgwt.client.ui.renderer;

import com.google.gwt.user.client.ui.UIObject;
import com.qualogy.qafe.mgwt.client.activities.AbstractActivity;
import com.qualogy.qafe.mgwt.client.vo.ui.ComponentGVO;

public abstract class AbstractTextComponentRenderer extends AbstractEditableComponentRenderer {

	protected void preInit(ComponentGVO component, UIObject widget, String uuid, String parent, String context, AbstractActivity activity) {
		if (widget == null) {
			return;
		}
		// TODO use css
		widget.getElement().setAttribute("style", "margin: 5px; padding: 1px; border-radius: 4px; border: 1px solid #ABADB0");
	}

	@Override
	protected void init(ComponentGVO component, UIObject widget, String owner, String uuid, String parent, String context, AbstractActivity activity) {
		preInit(component, widget, uuid, parent, context, activity);
		super.init(component, widget, owner, uuid, parent, context, activity);
	}
}
