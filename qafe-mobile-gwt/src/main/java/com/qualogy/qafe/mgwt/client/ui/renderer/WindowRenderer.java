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
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.mgwt.ui.client.widget.buttonbar.ButtonBar;
import com.qualogy.qafe.mgwt.client.activities.AbstractActivity;
import com.qualogy.qafe.mgwt.client.activities.WindowActivity;
import com.qualogy.qafe.mgwt.client.util.ComponentRepository;
import com.qualogy.qafe.mgwt.client.views.WindowView;
import com.qualogy.qafe.mgwt.client.views.impl.WindowViewImpl;
import com.qualogy.qafe.mgwt.client.vo.ui.ComponentGVO;
import com.qualogy.qafe.mgwt.client.vo.ui.RootPanelGVO;
import com.qualogy.qafe.mgwt.client.vo.ui.ToolbarGVO;
import com.qualogy.qafe.mgwt.client.vo.ui.WindowGVO;

public class WindowRenderer extends AbstractComponentRenderer {

	public UIObject render(ComponentGVO component, String owner, String uuid, String parent, String context, AbstractActivity activity) {		
		UIObject widget = null;
		if (component instanceof WindowGVO) {
			boolean showBackButton = true;
			boolean subWindow = false;
			if (activity instanceof WindowActivity) {
				WindowActivity windowActivity = (WindowActivity)activity;
				showBackButton = windowActivity.isShowBackButton();
				subWindow = windowActivity.isSubWindow();
			}
			WindowGVO windowGVO = (WindowGVO)component;
			WindowView window = new WindowViewImpl(showBackButton, subWindow);
			init(windowGVO, (UIObject)window, owner, uuid, parent, context, activity);
			registerView(windowGVO, window, parent, context);
			widget = (UIObject)window;
		}
		return widget;
	}
	
	@Override
	protected void init(ComponentGVO component, UIObject widget, String owner, String uuid, String parent, String context, AbstractActivity activity) {
		super.init(component, widget, owner, uuid, parent, context, activity);
		WindowGVO windowGVO = (WindowGVO)component;
		WindowView window = (WindowView)widget;
		renderChildren(windowGVO, window, uuid, parent, context, activity);
	}
	
	private void renderChildren(WindowGVO component, WindowView widget, String uuid, String parent, String context, AbstractActivity activity) {
		RootPanelGVO rootPanelGVO = component.getRootPanel();
		UIObject rootPanel = renderChildComponent(rootPanelGVO, component.getId(), uuid, component.getId(), context, activity);
		if (rootPanel instanceof Widget) {
			widget.setContent((Widget)rootPanel);	
		}
		
		ToolbarGVO toolbarGVO = rootPanelGVO.getToolbarGVO();
		UIObject toolbar = renderChildComponent(toolbarGVO, rootPanelGVO.getId(), uuid, component.getId(), context, activity);
		if (toolbar instanceof ButtonBar) {
			widget.setToolbar((ButtonBar)toolbar);	
		}
	}
	
	@Override
	protected void initSize(ComponentGVO component, UIObject widget, String uuid, String parent, String context) {
		// Do nothing
	}
	
	private void registerView(WindowGVO component, WindowView view, String parent, String context) {
		ComponentRepository.getInstance().addView(parent, context, view, component);		
	}
}
