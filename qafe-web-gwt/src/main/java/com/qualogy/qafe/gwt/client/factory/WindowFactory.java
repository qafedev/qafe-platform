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
package com.qualogy.qafe.gwt.client.factory;

import org.gwt.mosaic.core.client.Dimension;
import org.gwt.mosaic.ui.client.WindowPanel;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Hidden;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.MenuItemSeparator;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.qualogy.qafe.gwt.client.component.QRootPanel;
import com.qualogy.qafe.gwt.client.component.QWindowPanel;
import com.qualogy.qafe.gwt.client.context.ClientApplicationContext;
import com.qualogy.qafe.gwt.client.ui.GWTUIGenerator;
import com.qualogy.qafe.gwt.client.ui.renderer.RendererHelper;
import com.qualogy.qafe.gwt.client.ui.renderer.events.EventFactory;
import com.qualogy.qafe.gwt.client.vo.ui.MenuItemGVO;
import com.qualogy.qafe.gwt.client.vo.ui.MenuItemSeparatorGVO;
import com.qualogy.qafe.gwt.client.vo.ui.QAFEKeywordsGVO;
import com.qualogy.qafe.gwt.client.vo.ui.UIGVO;
import com.qualogy.qafe.gwt.client.vo.ui.WindowGVO;
import com.qualogy.qafe.gwt.client.vo.ui.event.EventListenerGVO;

public class WindowFactory {

	private static final String LOG_TITLE = "Execution log window";
	private static String currentWindowTitle;

	private WindowFactory() {
	};


	// CHECKSTYLE.OFF: CyclomaticComplexity
	public static void createWindow(UIGVO ui, String windowId) {
		Widget w= GWTUIGenerator.createView(ui, windowId);

		if (ClientApplicationContext.getInstance().isMDI()) {
			WindowGVO windowGVO = getWindow(ui, windowId);
			String windowTitle = getWindowTitle(ui, windowId);

			if (windowGVO != null) {
				windowTitle = windowGVO.getTitle();
			}

			if (w != null) {

				WindowPanel window = new QWindowPanel(windowTitle);
				window.getHeader().setText(windowTitle);

				if(w instanceof QRootPanel) {
					((QWindowPanel)window).setQRootPanel((QRootPanel)w);
				}

				window.setAnimationEnabled(true);
				((QWindowPanel) window).setManaged(true);

				// Do not pack the windows if the "width" or "height" attributes
				// are on
				boolean bPack = true;

				// window.updateSize();
				if (windowGVO.getHeight() != null && windowGVO.getHeight().length() > 0) {
					try {
						int height = Integer.parseInt(windowGVO.getHeight());
						// Test if the height is not negative
						if (height < 0)
							throw new NumberFormatException();
						bPack = false;
						window.setHeight(height + "px");
					} catch (NumberFormatException e) {
						ClientApplicationContext.getInstance().log("could not set height on Window (it's not a valid integer):" + windowGVO.getHeight());
					}
				}
				if (windowGVO.getWidth() != null && windowGVO.getWidth().length() > 0) {
					try {
						int width = Integer.parseInt(windowGVO.getWidth());
						// Test if the width is not negative
						if (width < 0)
							throw new NumberFormatException();
						bPack = false;
						window.setWidth(width + "px");
					} catch (NumberFormatException e) {
						ClientApplicationContext.getInstance().log("could not set width on Window (it's not a valid integer):" + windowGVO.getWidth());
					}
				}

				if (currentWindowTitle == null)
					currentWindowTitle = windowGVO.getTitle();
				if (!windowTitle.equalsIgnoreCase(currentWindowTitle)) {
					ClientApplicationContext.getInstance().updatePosition();
				}
				currentWindowTitle = windowTitle;
				int left = ClientApplicationContext.getInstance().getStartXPosition();
				int top = ClientApplicationContext.getInstance().getStartYPosition();
				if (windowGVO.getLeft() != null) {
					left = windowGVO.getLeft();
				}
				if (windowGVO.getTop() != null) {
					top = windowGVO.getTop();
				}
				window.setPopupPosition(left, top);

				ClientApplicationContext.getInstance().addWindows(ui, window, windowId);
				if (Cookies.getCookie(ui.getUuid() + "-top-" + windowId) != null && Cookies.getCookie(ui.getUuid() + "-left-" + windowId) != null) {
					// There is a known bug that causes the parsing of the position stored as string in the cookie to be
					// returned as a double. 
					double leftPos = Double.parseDouble(Cookies.getCookie(ui.getUuid() + "-left-" + windowId));
					double rightPos = Double.parseDouble(Cookies.getCookie(ui.getUuid() + "-top-" + windowId));
					int roundedLeftPos = (int)Math.round(leftPos);
					int roundedRightPos = (int)Math.round(rightPos);
					window.setPopupPosition(roundedLeftPos, roundedRightPos);

				}
				window.setWidget(w);
				// If the width or the height attributes were set then we don't
				// pack the window
				// Use the default width and height that you got in the ClientApplicationContext.
				if(ClientApplicationContext.getInstance().getGlobalConfigurations().get("window.default.height") != null && windowGVO.getHeight() == null){
					window.setHeight(ClientApplicationContext.getInstance().getGlobalConfigurations().get("window.default.height"));
					bPack = false;
				}
				if(ClientApplicationContext.getInstance().getGlobalConfigurations().get("window.default.width") != null && windowGVO.getWidth() == null){
					window.setWidth(ClientApplicationContext.getInstance().getGlobalConfigurations().get("window.default.width"));
					bPack = false;
				}

				if (bPack) {
					window.pack();
					if (window.getContentHeight() > (int)(Window.getClientHeight()*0.8)){
						Dimension d =window.getContentSize();
						Dimension newD = new Dimension(d.getWidth(),Window.getClientHeight()/2);
						window.setContentSize(newD);
					}
					if (window.getContentWidth() > (int)(Window.getClientWidth()*0.8)){
						Dimension d =window.getContentSize();
						Dimension newD = new Dimension(Window.getClientWidth()/2,d.getHeight());
						window.setContentSize(newD);
					}
				}

				window.show();
				modifyWindowSize(w, window);

				EventFactory.createWindowSizeEvent(ui.getUuid(), windowId, window);
				if (windowGVO != null) {
					RendererHelper.addStyle(windowGVO.getRootPanel(), ((QWindowPanel)window).getQRootPanel());
					if (windowGVO.getClosable() != null) {
						((QWindowPanel) window).setClosable(windowGVO.getClosable().booleanValue());
					}
					if (windowGVO.getMaximizable() != null && windowGVO.getMaximizable().booleanValue()) {
						((QWindowPanel) window).addMaximizeButton();// setMaximizable(windowGVO.getMaximizable().booleanValue());
					}
					if (windowGVO.getMinimizable() != null && windowGVO.getMinimizable().booleanValue()) {
						((QWindowPanel) window).addMinimizeButton();
					}
					if (windowGVO.getResizable() != null) {
						window.setResizable(windowGVO.getResizable().booleanValue());
					}

					// The id of the ui-window is winId|winId|appId (<id>|<parent>|<context>),
					// while the id of ui-components within a window is componentId|winId|appId
					RendererHelper.addId(windowGVO, window, ui.getUuid(), windowGVO.getId(), windowGVO.getContext(), true);
					RendererHelper.addUUID(windowGVO, window, ui.getUuid());
					// /// TODO Check the unload and window actions...
					EventFactory.createDefaultCloseEvent(windowGVO, window, ClientApplicationContext.getInstance().getWindowSession(),ui.getUuid());
					EventFactory.createWindowStateChangeListener(window, windowTitle, ui.getUuid(), windowId);
				}
			} else {
				ClientApplicationContext.getInstance().log("Opening Window failed", "Window with id '" + windowId + "' could not be opened.\n This Window might not exist in this application context", true);
			}
		} else {
			WindowGVO windowGVO = getWindow(ui, windowId);

			setWidgetToMainPanel(w, windowGVO);
		}
	}
    // CHECKSTYLE.ON: CyclomaticComplexity

	public static void modifyWindowSize(Widget w, WindowPanel window) {
		int menuHeight = 0;
		int toolbarHeight = 0;
		int extra = 17;
		if(((QRootPanel)w).getMenuBar() != null && ((QRootPanel)w).getToolbar() == null){
			menuHeight = ((QRootPanel)w).getMenuBar().getOffsetHeight();
			extra = 25;
		}
		if(((QRootPanel)w).getToolbar() != null && ((QRootPanel)w).getMenuBar() == null){
			toolbarHeight = ((QRootPanel)w).getToolbar().getOffsetHeight();
			extra = 15;
		}
		if(((QRootPanel)w).getToolbar() != null && ((QRootPanel)w).getMenuBar() != null){
			extra = 74;
		}
		int headerHeight =  window.getHeader().getOffsetHeight();
		int modifiedHeight = window.getContentHeight()+menuHeight+toolbarHeight+headerHeight+extra;
		window.setHeight(modifiedHeight+"px");
		window.setWidth((window.getContentWidth()+26)+"px");
		window.show();
	}

	public static void setWidgetToMainPanel(Widget w, WindowGVO windowGVO) {
		if (w != null) {
			clearWidgetFromMainPanel();
			SimplePanel mainPanel = ClientApplicationContext.getInstance().getMainPanel();
			if (mainPanel==null){
				mainPanel = new SimplePanel();
				mainPanel.setWidth(Window.getClientWidth() + "px");
				mainPanel.setHeight(Window.getClientHeight() + "px");

				ClientApplicationContext.getInstance().setMainPanel(mainPanel);
				if(ClientApplicationContext.getInstance().getApplicationsMenu() != null){
					RootPanel.get().add((Widget) mainPanel,0,24);
				} else {
					RootPanel.get().add((Widget) mainPanel,0,0);
				}
			}

			w.setWidth(Window.getClientWidth() + "px");
			w.setHeight(Window.getClientHeight() + "px");
			mainPanel.setWidget(w);
			w.addStyleName("SDIPanel");
			if(windowGVO != null) {
				RendererHelper.addStyle(windowGVO.getRootPanel(), w);
			}
		}
	}

	public static void clearWidgetFromMainPanel() {


		SimplePanel mainPanel = ClientApplicationContext.getInstance().getMainPanel();
		if (mainPanel!=null){
			Hidden hidden = new Hidden();
			mainPanel.setWidget(hidden);
		}
	}

	private static WindowGVO getWindow(UIGVO ui, String windowId) {

		WindowGVO windowGVO = null;
		if (ui != null) {

			WindowGVO[] windows = ui.getWindows();
			for (int i = 0; i < windows.length; i++) {
				if (windowId != null && windowId.equals(windows[i].getId())) {
					windowGVO = windows[i];
					break;
				}
			}

		}
		return windowGVO;
	}

	private static String getWindowTitle(UIGVO ui, String windowId) {
		String name = null;
		WindowGVO window = getWindow(ui, windowId);
		if (window != null) {
			if (window.getTitle() != null) {
				name = window.getTitle();
			}
		}

		// if (ui != null) {
		//
		// WindowGVO[] windows = ui.getWindows();
		// for (int i = 0; i < windows.length; i++) {
		// if (windowId != null && windowId.equals(windows[i].getId())) {
		// if (windows[i].getTitle() != null) {
		// name = windows[i].getTitle();
		// }
		// break;
		// }
		// }
		//
		// }
		return name;
	}

	public static Widget createWindow(UIGVO ui) {
		return GWTUIGenerator.createView(ui);
	}





	public static void createMenu(UIGVO systemMenuApp) {
		if (systemMenuApp != null) {
			MenuItemGVO systemMenu = systemMenuApp.getMenus();
			ClientApplicationContext.getInstance().addMenu(systemMenu.getId(),ClientApplicationContext.getInstance().getMainMenu());
			if (systemMenu.getSubMenus() != null) {
				for (int i = 0; i < systemMenu.getSubMenus().length; i++) {
					MenuItemGVO root = systemMenu.getSubMenus()[i];
					MenuBar menuBar = ClientApplicationContext.getInstance().getMainMenu();
					processMenu(systemMenu.getSubMenus()[i], menuBar, systemMenuApp.getUuid());
				}
			}
		}

	}

	private static void processMenu(MenuItemGVO root, MenuBar menuBar, final String uuid) {
		MenuBar subMenuBar = null;
		if (QAFEKeywordsGVO.SYSTEM_MENU_APPLICATIONS.equals(root.getId())) {
			MenuBar applicationsMenu = new MenuBar(true);
			applicationsMenu.setAutoOpen(true);
			ClientApplicationContext.getInstance().setApplicationsMenu(applicationsMenu);

			subMenuBar = ClientApplicationContext.getInstance().getApplicationsMenu();
			subMenuBar.setAnimationEnabled(true);
			subMenuBar.setAutoOpen(true);
			MenuItem menuItem = GWTUIGenerator.createMenuItem(root.getId(), root.getDisplayname(), uuid, root.getContext(), subMenuBar);
			menuBar.addItem(menuItem);
		}


		if (root.getSubMenus() != null) {
			if (subMenuBar==null){
					subMenuBar = new MenuBar(true);
					subMenuBar.setAnimationEnabled(true);
					subMenuBar.setAutoOpen(true);
					MenuItem menuItem = GWTUIGenerator.createMenuItem(root.getId(), root.getDisplayname(), uuid, root.getContext(), subMenuBar);
					menuBar.addItem(menuItem);
			}

			for (int i = 0; i < root.getSubMenus().length; i++) {
				processMenu(root.getSubMenus()[i], subMenuBar, uuid);
			}
			ClientApplicationContext.getInstance().addMenu(root.getId(),subMenuBar);

		} else {
			if (!QAFEKeywordsGVO.SYSTEM_MENU_APPLICATIONS.equals(root.getId())&& root.getDisplayname()!=null &&
					!root.getDisplayname().equals(menuBar.getTitle())) {
				MenuItem menuItem = new MenuItem(root.getDisplayname(), (Command) null);
				RendererHelper.fillIn(root, menuItem, uuid, null, root.getContext());
				// DOM.setElementAttribute(menuItem.getElement(), "id",
				// root.getId());
				Command cmd = null;
				if (QAFEKeywordsGVO.SYSTEM_MENUITEM_LOGGING.equals(root.getId())) {
					cmd = new Command() {
						public void execute() {

						}
					};
				} else if (QAFEKeywordsGVO.SYSTEM_MENUITEM_RELOAD.equals(root.getId())) {
					cmd = new Command() {

						public void execute() {
							if (ClientApplicationContext.getInstance().getReloadable()) {
								MainFactoryActions.processReloadUIFromApplicationContext();
							} else {
								ClientApplicationContext.getInstance().log("Not available", "Reloading of application is only available in debug mode", true);
							}
						}
					};
				} else if (QAFEKeywordsGVO.SYSTEM_MENUITEM_TRYME.equals(root.getId())) {
					cmd = new Command() {

						public void execute() {
							if (ClientApplicationContext.getInstance().isDebugMode()) {
								MainFactory.createTryMeWindow(QAFEKeywordsGVO.SYSTEM_MENUITEM_TRYME);
							} else {
								ClientApplicationContext.getInstance().log("Not available", "Try me feature is only available in debug mode", true);
							}
						}
					};
				} else if (QAFEKeywordsGVO.SYSTEM_MENUITEM_TRYME_FORMS.equals(root.getId())) {
						cmd = new Command() {

							public void execute() {
								if (ClientApplicationContext.getInstance().isDebugMode()) {
									MainFactory.createTryMeWindow(QAFEKeywordsGVO.SYSTEM_MENUITEM_TRYME_FORMS);
								} else {
									ClientApplicationContext.getInstance().log("Not available", "Try me feature is only available in debug mode", true);
								}
							}
						};


				} else {
					if ((root.getEvents() != null) && (root.getEvents().length > 0)) {
						EventListenerGVO eventListenerGVO = root.getEvents()[0];
						cmd = EventFactory.createCommandListener(menuItem, eventListenerGVO, null);
					}
				}
				menuItem.setCommand(cmd);
				menuBar.addItem(menuItem);
			} else if(root instanceof MenuItemSeparatorGVO){
				MenuItemSeparator menuItemSeparator = new MenuItemSeparator();
				menuBar.addSeparator(menuItemSeparator);
				//menuBar.addItem((MenuItem)menuItemSeparator);
			}
		}
	}

}
