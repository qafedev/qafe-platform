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
package com.qualogy.qafe.mgwt.client.ui.renderer;

import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;
import com.qualogy.qafe.mgwt.client.activities.AbstractActivity;
import com.qualogy.qafe.mgwt.client.ui.component.QChoice;
import com.qualogy.qafe.mgwt.client.vo.ui.ChoiceGVO;
import com.qualogy.qafe.mgwt.client.vo.ui.ChoiceItemGVO;
import com.qualogy.qafe.mgwt.client.vo.ui.ComponentGVO;

public class ChoiceRenderer extends AbstractComponentRenderer {

	public UIObject render(ComponentGVO component, String owner, String uuid, String parent, String context, AbstractActivity activity) {
		UIObject widget = null;
		if (component instanceof ChoiceGVO) {
			ChoiceGVO choiceGVO = (ChoiceGVO)component;
			QChoice choice = new QChoice();			
			init(choiceGVO, choice, owner, uuid, parent, context, activity);
			widget = choice;
		}
		registerComponent(component, widget, owner, parent, context);
		return widget;
	}
	
	@Override
	protected void init(ComponentGVO component, UIObject widget, String owner, String uuid, String parent, String context, AbstractActivity activity) {
		super.init(component, widget, owner, uuid, parent, context, activity);
		ChoiceGVO choiceGVO = (ChoiceGVO)component;
		QChoice choice = (QChoice)widget;			
		renderChildren(choiceGVO, choice, uuid, parent, context, activity);
	}
	
	private void renderChildren(ChoiceGVO component, QChoice widget, String uuid, String parent, String context, AbstractActivity activity) {
		ChoiceItemGVO[] choiceItems = component.getChoiceItems();
		if (choiceItems != null) {
			for (int i=0; i<choiceItems.length; i++) {
				ChoiceItemGVO choiceItemGVO = choiceItems[i];
				UIObject choiceItem = renderChildComponent(choiceItemGVO, component.getId(), uuid, parent, context, activity);
				if (choiceItem instanceof Widget) {
					widget.addItem(( Widget)choiceItem);
				}
			}
		}
	}
}
