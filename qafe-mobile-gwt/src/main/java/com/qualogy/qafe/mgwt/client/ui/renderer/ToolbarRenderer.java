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
import com.googlecode.mgwt.ui.client.widget.buttonbar.ButtonBarSpacer;
import com.qualogy.qafe.mgwt.client.activities.AbstractActivity;
import com.qualogy.qafe.mgwt.client.ui.component.QToolbar;
import com.qualogy.qafe.mgwt.client.vo.ui.ComponentGVO;
import com.qualogy.qafe.mgwt.client.vo.ui.ToolbarGVO;
import com.qualogy.qafe.mgwt.client.vo.ui.ToolbarItemGVO;

public class ToolbarRenderer extends AbstractComponentRenderer {

	public UIObject render(ComponentGVO component, String owner, String uuid, String parent, String context, AbstractActivity activity) {
		UIObject widget = null;
		if (component instanceof ToolbarGVO) {
			ToolbarGVO toolbarGVO = (ToolbarGVO)component;
			QToolbar toolbar = new QToolbar();			
			init(toolbarGVO, toolbar, owner, uuid, parent, context, activity);
			widget = toolbar;
		}
		registerComponent(component, widget, owner, parent, context);
		return widget;
	}
	
	@Override
	protected void init(ComponentGVO component, UIObject widget, String owner, String uuid, String parent, String context, AbstractActivity activity) {
		super.init(component, widget, owner, uuid, parent, context, activity);
		ToolbarGVO toolbarGVO = (ToolbarGVO)component;
		QToolbar toolbar = (QToolbar)widget;			
		renderChildren(toolbarGVO, toolbar, uuid, parent, context, activity);
	}
	
	private void renderChildren(ToolbarGVO component, QToolbar widget, String uuid, String parent, String context, AbstractActivity activity) {
		ToolbarItemGVO[] toolbarItems = component.getToolbarItems();
		if (toolbarItems != null) {
			String toolbarItemWidth = component.getItemWidth();
			String toolbarItemHeight = component.getItemHeight();
			for (int i=0; i<toolbarItems.length; i++) {
				ToolbarItemGVO toolbarItemGVO = toolbarItems[i];
				UIObject toolbarItem = renderChildComponent(toolbarItemGVO, component.getId(), uuid, parent, context, activity);
				if (toolbarItem instanceof Widget) {
					initSize(toolbarItem, toolbarItemWidth, toolbarItemHeight, uuid, parent, context);
					widget.add((Widget)toolbarItem);
					if (i != (toolbarItems.length - 1)) {
						widget.add(new ButtonBarSpacer());
					}
				}
			}
		}
	}
}
