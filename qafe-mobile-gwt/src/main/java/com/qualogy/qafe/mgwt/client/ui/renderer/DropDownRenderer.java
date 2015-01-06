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
package com.qualogy.qafe.mgwt.client.ui.renderer;

import com.google.gwt.user.client.ui.UIObject;
import com.qualogy.qafe.mgwt.client.activities.AbstractActivity;
import com.qualogy.qafe.mgwt.client.ui.component.QDropDown;
import com.qualogy.qafe.mgwt.client.vo.ui.ComponentGVO;
import com.qualogy.qafe.mgwt.client.vo.ui.DropDownGVO;
import com.qualogy.qafe.mgwt.client.vo.ui.DropDownItemGVO;

public class DropDownRenderer extends AbstractTextComponentRenderer {

	public UIObject render(ComponentGVO component, String owner, String uuid, String parent, String context, AbstractActivity activity) {
		UIObject widget = null;
		if (component instanceof DropDownGVO) {
			DropDownGVO dropDownGVO = (DropDownGVO)component;
			QDropDown dropDown = new QDropDown();
			init(dropDownGVO, dropDown, owner, uuid, parent, context, activity);
			widget = dropDown;
			registerComponent(dropDownGVO, dropDown, owner, parent, context);
		}
		return widget;	
	}
	
	@Override
	protected void init(ComponentGVO component, UIObject widget, String owner, String uuid, String parent, String context, AbstractActivity activity) {
		super.init(component, widget, owner, uuid, parent, context, activity);
		DropDownGVO dropDownGVO = (DropDownGVO)component;
		QDropDown dropDown = (QDropDown)widget;
		renderChildren(dropDownGVO, dropDown, uuid, parent, context);
	}
	
	private void renderChildren(DropDownGVO component, QDropDown widget, String uuid, String parent, String context) {
		String emptyItemDisplayname = component.getEmptyItemDisplayName();
		String emptyItemValue = component.getEmptyItemValue();
		if ((emptyItemDisplayname != null) || (emptyItemValue != null)) {
			String emptyItemMessageKey = component.getEmptyItemMessageKey();
			if (emptyItemMessageKey != null) {
				emptyItemDisplayname = emptyItemMessageKey;
			}
			emptyItemValue = (emptyItemValue != null) ? emptyItemValue : "";
			widget.setEmptyItem(emptyItemDisplayname, emptyItemValue);
		}
		DropDownItemGVO[] dropDownItems = component.getDropDownItems();
		if (dropDownItems != null) {
			for (int i=0; i<dropDownItems.length; i++) {
				DropDownItemGVO dropDownItemGVO = dropDownItems[i];
				if (dropDownItemGVO != null) {
					String displayname = dropDownItemGVO.getDisplayname();
					String value = dropDownItemGVO.getValue();
					widget.addItem(displayname, value);
					Boolean selected = dropDownItemGVO.getSelected();	
					if (selected) {
						widget.setSelected(value);
					}
				}	
			}
		}
	}
}