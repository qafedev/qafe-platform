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
package com.qualogy.qafe.gwt.client.ui.renderer;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.qualogy.qafe.gwt.client.component.QRootPanel;
import com.qualogy.qafe.gwt.client.context.ClientApplicationContext;
import com.qualogy.qafe.gwt.client.vo.ui.ComponentGVO;
import com.qualogy.qafe.gwt.client.vo.ui.MenuItemGVO;
import com.qualogy.qafe.gwt.client.vo.ui.MenuItemSeparatorGVO;
import com.qualogy.qafe.gwt.client.vo.ui.RootPanelGVO;
import com.qualogy.qafe.gwt.client.vo.ui.ToolbarGVO;
import com.qualogy.qafe.gwt.client.vo.ui.WindowGVO;

public class WindowRenderer extends AbstractComponentRenderer {

	private String toolbarHeight;
	
	public UIObject render(ComponentGVO component, String uuid, String parent, String context) {		
		QRootPanel qRootPanel = null;
		if (component != null) {
			if (component instanceof WindowGVO) {
				WindowGVO gvo = (WindowGVO) component;
				qRootPanel = new QRootPanel();
				RendererHelper.addId(component, qRootPanel,uuid,parent, context, false);
				RendererHelper.addUUID(component, qRootPanel, uuid);
				RendererHelper.addEvents(component, qRootPanel, uuid);
				
				Widget rootPanel = (Widget)super.renderChildComponent(gvo.getRootPanel(), uuid,gvo.getId(), context);
				Widget menuAndToolbar = createMenuAndToolbar(qRootPanel,gvo.getRootPanel(), uuid, parent);				
				Widget messageBox = new MessageBox();
				
				ScrollPanel sp = new ScrollPanel();
				if (ClientApplicationContext.getInstance().isMDI()){
					sp.setHeight(gvo.getHeight());
					sp.setWidth(gvo.getWidth());
				} else {
					sp.setHeight(Window.getClientHeight()+"px");
					sp.setWidth(Window.getClientWidth()+"px");
				}
				sp.add(rootPanel);				
				
				qRootPanel.setMenuAndToolBar(menuAndToolbar);
				qRootPanel.setRootPanel(sp);
				qRootPanel.setMessageBox(messageBox);
				qRootPanel.add(menuAndToolbar,0,0);
				int yPosition = toolbarHeight != null ? Integer.parseInt(toolbarHeight)+ 20 : 0;
				if(qRootPanel.getMenuBar() != null){
					yPosition+=22;
				}
				qRootPanel.add(sp,0,yPosition);
				
				qRootPanel.add(messageBox,200,0);
			}
		}
		return qRootPanel;
	}
	
	/*private void inheritStyle(UIObject source, UIObject target) {
		if ((source.getStyleName() != null) && (source.getStyleName().length() > 0)) {
			target.addStyleName(source.getStyleName());	
		}
		if ((source.getElement().getStyle() != null) && (source.getElement().getStyle().toString().length() > 0)) {
			String inlineStyle = DOM.getElementAttribute(source.getElement(), "style");
			target.getElement().setAttribute("style", inlineStyle);
		}
	}*/

	private Widget createMenuAndToolbar(QRootPanel uiObject,ComponentGVO component,	String uuid,String parent) {

		VerticalPanel vp = new VerticalPanel();
		//vp.setSpacing(2);
		vp.setWidth("100%");
		//int totalMargin = 0;
		
		MenuBar menu = createMenu(component, uuid,parent);
		if (menu != null) {
			vp.add(menu);
			//totalMargin=totalMargin+28;
			uiObject.setMenuBar(menu);
		}

		if (component instanceof RootPanelGVO) {	
			RootPanelGVO rootPanelGVO = (RootPanelGVO) component;
			if (rootPanelGVO.getToolbarGVO() != null) {
				Widget toolbar = createToolBar((ToolbarGVO) rootPanelGVO.getToolbarGVO(), uuid,parent) ;
				vp.add(toolbar);
				uiObject.setToolbar(toolbar);
				//totalMargin=totalMargin+28;
			}
			if (rootPanelGVO.getTitle()!=null && rootPanelGVO.getTitle().length()>0){
				vp.add(new HTML(rootPanelGVO.getTitle()));
			}
		}
		//DOM.setElementAttribute(vp.getElement(), "totalMargin", ""+totalMargin);
		return vp;
	}

	private Widget createToolBarButton(ToolbarGVO toolbar, final String imageLocation, final String toolTip, ComponentGVO component, String uuid,String parent) {
		Widget w = null;
		
		Image toolbarImage = new Image(imageLocation);
		toolbarImage.setSize(toolbar.getItemWidth(),toolbar.getItemHeight());
		RendererHelper.addStyle(component, toolbarImage);
		
		PushButton toolbarButton = new PushButton(toolbarImage);
		toolbarButton.setTitle(component.getTooltip());
		RendererHelper.fillIn(component, toolbarButton, uuid,parent, component.getContext());
		w = toolbarButton;
		return w;		
	}

	private HorizontalPanel createToolBar(ToolbarGVO component, String uuid,String parent) {
		HorizontalPanel tb = new HorizontalPanel();
		tb.setSpacing(5);
		RendererHelper.fillIn(component, tb, uuid,parent, component.getContext());
		if (component != null) {
			if (component.getToolbarItems() != null) {
				for (int i = 0; i < component.getToolbarItems().length; i++) {
					tb.add(createToolBarButton(component,component.getToolbarItems()[i].getImageLocation(), component.getToolbarItems()[i].getTooltip(), component.getToolbarItems()[i], uuid,parent));
					toolbarHeight = (component.getItemHeight().replace("px", "")).trim();
				}
			}
		}		
		return tb;
	}

	private MenuBar createMenu(ComponentGVO component, String uuid, String parent) {

		Command cmd = new Command() {
			public void execute() {

			}
		};
		MenuBar mainMenu = null; 
		MenuItemGVO rootMenu = component.getMenu();

		if (rootMenu != null) {
			if (rootMenu.getSubMenus() != null) {
				mainMenu = new MenuBar();
				RendererHelper.fillIn(rootMenu,	mainMenu, uuid,parent, component.getContext());
				for (int i = 0; i < rootMenu.getSubMenus().length; i++) {
					if (rootMenu.getSubMenus()[i].getSubMenus() != null && rootMenu.getSubMenus()[i].getSubMenus().length > 0) {
						
						processMenu(mainMenu, rootMenu.getSubMenus()[i], rootMenu.getSubMenus()[i].getDisplayname(), uuid,parent);
					} else {
						MenuItem menuItem = new MenuItem(rootMenu.getSubMenus()[i].getDisplayname(), cmd);
						RendererHelper.fillIn(rootMenu.getSubMenus()[i], menuItem, uuid,parent, component.getContext());
						mainMenu.addItem(menuItem);
					}
				}
			}
			if(rootMenu.getStyleClass() != null) {
				mainMenu.setStylePrimaryName(rootMenu.getStyleClass());
			}
		}
		if (mainMenu!=null){
			mainMenu.setAutoOpen(true);
			mainMenu.setAnimationEnabled(true);
			
		}		
		return mainMenu;
	}

	private void processMenu(MenuBar menu, MenuItemGVO subMenuGVO, String name, String uuid, String parent) {
		MenuBar subMenu = new MenuBar(true);
		MenuItemGVO[] subMenus = subMenuGVO.getSubMenus();
		for (int j = 0; j < subMenus.length; j++) {
			if (subMenus[j].getSubMenus() != null && subMenus[j].getSubMenus().length > 0) {
				processMenu(subMenu, subMenus[j], subMenus[j].getDisplayname(), uuid,parent);
			} else {
				if (subMenus[j] instanceof MenuItemSeparatorGVO){					
					subMenu.addSeparator();
				} else {
					MenuItem menuItem = new MenuItem(subMenus[j].getDisplayname(),(Command) null);
					RendererHelper.fillIn(subMenus[j], menuItem, uuid,parent, subMenus[j].getContext());
					subMenu.addItem(menuItem);
				}
			}

		}
		MenuItem subMenuItem = new MenuItem(name, subMenu);
		RendererHelper.fillIn(subMenuGVO, subMenuItem, uuid,parent, subMenuGVO.getContext());
		menu.addItem(subMenuItem);
	}
}
