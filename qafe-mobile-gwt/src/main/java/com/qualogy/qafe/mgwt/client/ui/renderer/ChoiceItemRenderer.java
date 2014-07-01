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
package com.qualogy.qafe.mgwt.client.ui.renderer;

import com.google.gwt.user.client.ui.UIObject;
import com.qualogy.qafe.mgwt.client.activities.AbstractActivity;
import com.qualogy.qafe.mgwt.client.ui.component.QChoiceItem;
import com.qualogy.qafe.mgwt.client.vo.ui.ChoiceItemGVO;
import com.qualogy.qafe.mgwt.client.vo.ui.ComponentGVO;

public class ChoiceItemRenderer extends AbstractComponentRenderer {

	public UIObject render(ComponentGVO component, String owner, String uuid, String parent, String context, AbstractActivity activity) {
		UIObject widget = null;
		if (component instanceof ChoiceItemGVO) {
			ChoiceItemGVO choiceItemGVO = (ChoiceItemGVO)component;
			QChoiceItem choiceItem = new QChoiceItem(owner);
			init(choiceItemGVO, choiceItem, owner, uuid, parent, context, activity);
			widget = choiceItem;
		}
		return widget;
	}
	
	@Override
	protected void init(ComponentGVO component, UIObject widget, String owner, String uuid, String parent, String context, AbstractActivity activity) {
		super.init(component, widget, owner, uuid, parent, context, activity);
		ChoiceItemGVO choiceItemGVO = (ChoiceItemGVO)component;
		QChoiceItem choiceItem = (QChoiceItem)widget;
		choiceItem.setText(choiceItemGVO.getDisplayname());
		if (choiceItemGVO.getSelected()) {
			choiceItem.setValue(true);
		}
		String value = choiceItemGVO.getValue();
		if (value != null) {
			choiceItem.setFormValue(value);
		}
	}
}
