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
/**
 * 
 */
package com.qualogy.qafe.mgwt.client.vo.ui;

/**
 * @author rjankie The RootPanel class is the container
 */
public class RootPanelGVO extends PanelGVO {

	private ToolbarGVO toolbarGVO;

	private String parent=null;;

	/**
	 * 
	 */
	private static final long serialVersionUID = 2329991094591222688L;

	public String getClassName() {
		return "com.qualogy.qafe.gwt.client.vo.ui.RootPanelGVO";
	}

	public String getStyleName() {
		return "rootPanel";
	}

	public ToolbarGVO getToolbarGVO() {
		return toolbarGVO;
	}

	public void setToolbarGVO(ToolbarGVO toolbarGVO) {
		this.toolbarGVO = toolbarGVO;
	}

	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

	public void updateToolbar() {
		if (parent!=null){
			if (getToolbarGVO() != null
					&& getToolbarGVO().getToolbarItems() != null) {
				for (int i = 0; i < getToolbarGVO().getToolbarItems().length; i++) {
					getToolbarGVO().getToolbarItems()[i].setParent(getParent());
				}
			}
		}
	}

	public void updateMenu() {
		updateMenu(getMenu());
		
	}
	private void updateMenu(MenuItemGVO menu){
		if (parent!=null){
			if (menu != null && menu.getSubMenus() != null) {
				for (int i = 0; i < menu.getSubMenus().length; i++) {
					menu.getSubMenus()[i].setParent(getParent());
					updateMenu(menu.getSubMenus()[i]);
				}
			}
		}
	}

}
