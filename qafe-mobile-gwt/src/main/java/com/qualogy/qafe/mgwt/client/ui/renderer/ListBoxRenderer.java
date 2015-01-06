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
import com.qualogy.qafe.mgwt.client.ui.component.QListBox;
import com.qualogy.qafe.mgwt.client.vo.ui.ComponentGVO;
import com.qualogy.qafe.mgwt.client.vo.ui.DropDownItemGVO;
import com.qualogy.qafe.mgwt.client.vo.ui.ListBoxGVO;

public class ListBoxRenderer extends AbstractComponentRenderer {

	public UIObject render(ComponentGVO component, String owner, String uuid, String parent, String context, AbstractActivity activity) {
		UIObject widget = null;
		if (component instanceof ListBoxGVO) {
			ListBoxGVO listBoxGVO = (ListBoxGVO)component;
			QListBox listBox = new QListBox(listBoxGVO.getMultipleSelect(), listBoxGVO.toString());
			init(listBoxGVO, listBox, owner, uuid, parent, context, activity);
			widget = listBox;
		}
		registerComponent(component, widget, owner, parent, context);
		return widget;
	}
	
	@Override
	protected void init(ComponentGVO component, UIObject widget, String owner, String uuid, String parent, String context, AbstractActivity activity) {
		super.init(component, widget, owner, uuid, parent, context, activity);
		ListBoxGVO listBoxGVO = (ListBoxGVO)component;
		QListBox listBox = (QListBox)widget;
		Integer numVisibleItems = listBoxGVO.getNrOfVisibleItems();
		if (numVisibleItems == null) {
			numVisibleItems = 1;
			DropDownItemGVO[] listBoxItems = listBoxGVO.getDropDownItems();
			if (listBoxItems != null) {
				numVisibleItems = listBoxItems.length;
			}
		} 
		// TODO
//		listBox.setVisibleItemCount(numVisibleItems);
		renderChildren(listBoxGVO, listBox, uuid, parent, context);
	}
	
	private void renderChildren(ListBoxGVO component, QListBox widget, String uuid, String parent, String context) {
		DropDownItemGVO[] listBoxItems = component.getDropDownItems();
		if (listBoxItems != null) {
			for (int i=0; i<listBoxItems.length; i++) {
				DropDownItemGVO listBoxItemGVO = listBoxItems[i]; 
				if (listBoxItemGVO != null) {
					String displayname = listBoxItemGVO.getDisplayname();
					String value = listBoxItemGVO.getValue();
					widget.addItem(displayname, value);
					if (listBoxItemGVO.getSelected()) {
						widget.setSelected(value);
					}
				}
			}
		}
	}
}
