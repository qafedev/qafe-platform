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
package com.qualogy.qafe.gwt.client.ui.renderer;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.UIObject;
import com.qualogy.qafe.gwt.client.context.ClientApplicationContext;
import com.qualogy.qafe.gwt.client.vo.ui.ComponentGVO;
import com.qualogy.qafe.gwt.client.vo.ui.MenuItemGVO;

public class MenuItemRenderer extends AbstractComponentRenderer {

	public UIObject render(ComponentGVO component, String uuid,String parent, String context) {
		UIObject uiObject = null;
		if (component != null) {
			if (component instanceof MenuItemGVO) {
				MenuItemGVO menuItemGVO = (MenuItemGVO) component;
//				if (menuItemGVO.getIsTopLevelMenu() != null
//						&& menuItemGVO.getIsTopLevelMenu().booleanValue()) {
//
					Command cmd = new Command() {
						public void execute() {
							ClientApplicationContext.getInstance().log("Default MenuItem command","You selected a menu item!",true);
						}
					};
					MenuItemGVO[] subMenus = menuItemGVO.getSubMenus();
					if (subMenus != null) {
						uiObject = new MenuBar();
						MenuBar menuBar = (MenuBar) uiObject;
						for (int i = 0; i < subMenus.length; i++) {
							UIObject renderedObject = render(subMenus[i], uuid,parent, context);
							if (renderedObject instanceof MenuBar) {
								menuBar.addItem(menuItemGVO.getDisplayname(),
										(MenuBar) renderedObject);
							} else if (renderedObject instanceof MenuItem) {
								menuBar.addItem((MenuItem) renderedObject);
							}
						}
					} else {
						uiObject = new MenuItem(menuItemGVO.getDisplayname(), cmd);
					}
//				} else {
//					Command cmd = new Command() {
//						public void execute() {
//								ClientApplicationContext.getInstance().log("Default menu item command","You selected a menu item!",true);
//						}
//					};
//					MenuItemGVO[] subMenus = menuItemGVO.getSubMenus();
//					if (subMenus != null) {
//						uiObject = new MenuBar(true);
//						MenuBar menuBar = (MenuBar) uiObject;
//						for (int i = 0; i < subMenus.length; i++) {
//							UIObject renderedObject = render(subMenus[i],uuid,parent);
//							if (renderedObject instanceof MenuBar) {
//								menuBar.addItem(menuItemGVO.getDisplayname(),
//										(MenuBar) renderedObject);
//							} else if (renderedObject instanceof MenuItem) {
//								menuBar.addItem((MenuItem) renderedObject);
//							}
//						}
//					} else {
//						uiObject = new MenuItem(menuItemGVO.getDisplayname(), cmd);
//					}
//				}

			}
		}
		


		return uiObject;
	}
}
