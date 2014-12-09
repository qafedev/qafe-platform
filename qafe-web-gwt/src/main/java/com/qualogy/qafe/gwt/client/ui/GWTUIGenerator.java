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
package com.qualogy.qafe.gwt.client.ui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.gwt.mosaic.ui.client.WindowPanel;
import org.gwt.mosaic.ui.client.WindowPanel.WindowState;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;
import com.qualogy.qafe.gwt.client.component.DesktopIcon;
import com.qualogy.qafe.gwt.client.component.html5.notifications.Notification;
import com.qualogy.qafe.gwt.client.context.ClientApplicationContext;
import com.qualogy.qafe.gwt.client.factory.MainFactoryActions;
import com.qualogy.qafe.gwt.client.ui.renderer.RendererHelper;
import com.qualogy.qafe.gwt.client.ui.renderer.WindowRenderer;
import com.qualogy.qafe.gwt.client.ui.renderer.events.CallbackHandler;
import com.qualogy.qafe.gwt.client.ui.renderer.events.EventFactory;
import com.qualogy.qafe.gwt.client.util.QAMLConstants;
import com.qualogy.qafe.gwt.client.vo.ui.ComponentGVO;
import com.qualogy.qafe.gwt.client.vo.ui.MenuItemGVO;
import com.qualogy.qafe.gwt.client.vo.ui.UIGVO;
import com.qualogy.qafe.gwt.client.vo.ui.WindowGVO;
import com.qualogy.qafe.gwt.client.vo.ui.event.EventListenerGVO;
import com.qualogy.qafe.gwt.client.vo.ui.event.InputVariableGVO;

public class GWTUIGenerator {

	public static final Panel createView(UIGVO ui) {
		Panel view = null;
		if (ui != null) {
			String uuid = ui.getUuid();

			WindowGVO[] windows = ui.getWindows();
			Widget[] widgets = new Widget[windows.length];

			/*
			 * for (int i = 0; i < windows.length; i++) {
			 *
			 * Widget w = (Widget) new
			 * WindowRenderer().render(windows[i],windows[i].getId() +"|"+uuid
			 * ); widgets[i] = w; }
			 */
			buildMenu(widgets, windows, uuid, ui);

		}

		return view;
	}

	public static final Widget createView(UIGVO ui, String windowId) {
		Widget view = null;
		if (ui != null) {
			String uuid = ui.getUuid();

			WindowGVO[] windows = ui.getWindows();
			for (int i = 0; i < windows.length; i++) {
				if (windowId != null && windowId.equals(windows[i].getId())) {
					view = (Widget) new WindowRenderer().render(windows[i], uuid, windowId, ui.getAppId());
					EventFactory.processOnLoadEvent(windows[i], view);
					break;

				}
				// check if there onload events for this.

			}

		}

		return view;
	}

    // CHECKSTYLE.OFF: CyclomaticComplexity
	private static void buildMenu(Widget[] widgets, WindowGVO[] windows, final String uuid, UIGVO ui) {
		if (widgets != null && windows != null) {
			MenuBar menuBar = null;
			String menuTitle = "";
			boolean tryMeFeature = false;
			if (ui.getTitle() != null) {
				menuTitle = ui.getTitle();
				// TODO: is it needed to call this method?
				ClientApplicationContext.getInstance().removeWindow(menuTitle, ui.getAppId(), uuid);

				menuBar = new MenuBar(true);
				MenuItem menuItem = createMenuItem(ui.getAppId(), menuTitle, uuid, null, menuBar);
				if (ui.getRootMenu()==null){
					if (ClientApplicationContext.getInstance().getApplicationsMenu() != null) {
						ClientApplicationContext.getInstance().getApplicationsMenu().addItem(menuItem);
					}
				} else {
					// get the rootMenu and add the item to that menu
					MenuBar  targetMenuBar = ClientApplicationContext.getInstance().getMenuBars().get(ui.getRootMenu());
					if (targetMenuBar!=null){
						targetMenuBar.addItem(menuItem);
					}

				}
			} else {
				menuTitle = "Try me!";
				if (ClientApplicationContext.getInstance().getTryMeMenu() == null) {
					menuBar = new MenuBar(true);
					ClientApplicationContext.getInstance().setTryMeMenu(menuBar);
					ClientApplicationContext.getInstance().getMainMenu().addItem(menuTitle, menuBar);
				}
				tryMeFeature = true;
				menuBar = ClientApplicationContext.getInstance().getTryMeMenu();
				ClientApplicationContext.getInstance().log("Try me menu", "Check Try Me! menu for updated application", false, true, null);

			}

			for (int i = 0; i < widgets.length; i++) {
				String title = windows[i].getTitle();
				if (title == null || title.length() == 0) {
					title = windows[i].getId();
				}

				if (windows[i].getIsparent() != null && windows[i].getIsparent().booleanValue()) {
					menuBar.addItem(createMenuItem(title, uuid, windows[i].getId(), ui, tryMeFeature));

				}

				if (ClientApplicationContext.getInstance().isMDI()) {

					if (ClientApplicationContext.getInstance().getDockMode()) {
						if (windows[i].getInDock() != null && windows[i].getInDock().booleanValue()) {
							addToDockPanel(windows[i], uuid, title, ui.getAppId());
						} else {
							createDesktopIcon(windows[i], uuid, title, ui.getAppId());
						}
					} else {
						createDesktopIcon(windows[i], uuid, title, ui.getAppId());

					}
				}

			}
			if (ClientApplicationContext.getInstance().isMDI()) {
				ClientApplicationContext.getInstance().updatePosition();
			}

		}
	}
    // CHECKSTYLE.ON: CyclomaticComplexity

	private static void addToDockPanel(final WindowGVO windowGVO, final String uuid, final String title, final String appId) {
		if (windowGVO != null && windowGVO.getIcon() != null && windowGVO.getIcon().length() > 0) {
			final String windowId = windowGVO.getId();
			final Image dockImage = new Image(windowGVO.getIcon());
			dockImage.setTitle(title);
			//dockImage.setHeight(60 + "px");
			//dockImage.setWidth(40 + "px");
			dockImage.setStylePrimaryName("dockItem");
			fillInMandatoryOnly(windowGVO, dockImage, uuid, windowId, appId);

			dockImage.addClickHandler(new ClickHandler() {

				public void onClick(ClickEvent event) {
					WindowPanel existingWindowPanel = ClientApplicationContext.getInstance().windowIdExists(windowId, uuid);
					if (existingWindowPanel == null) {
						EventListenerGVO[] events = windowGVO.getEvents();
						if (events != null) {
							String eventType = QAMLConstants.EVENT_ONCLICK;
							for (EventListenerGVO eventGVO : events) {
								if (eventGVO.getEventComponentId().equals(windowId) && eventGVO.getEventListenerType().equals(eventType)) {
									createCallBack4OpenWindow(dockImage, eventType, eventGVO);
									break;
								}
							}
						}
//						MainFactoryActions.getUIByUUID(uuid, windowId);
					} else {
						Iterator itr = ClientApplicationContext.getInstance().getHorizontalPanel().iterator();
						while (itr.hasNext()) {
							Widget w = (Widget) itr.next();
							if (w.getTitle().equals(title)) {
								existingWindowPanel.setWindowState(WindowState.NORMAL);
								existingWindowPanel.show();
								ClientApplicationContext.getInstance().getHorizontalPanel().remove(w);
							}
						}
						existingWindowPanel.toFront();
					}
				}
			});

			if (ClientApplicationContext.getInstance().getDockPanel() != null) {
				ClientApplicationContext.getInstance().getDockPanel().add(dockImage);
				int dockWidth = ClientApplicationContext.getInstance().getDockPanel().getOffsetWidth();
				DOM.setStyleAttribute(ClientApplicationContext.getInstance().getDockPanel().getElement(), "left", Window.getClientWidth() / 2 - (dockWidth / 2) + "");
			}
		}
	}

	private static void createDesktopIcon(final WindowGVO windowGVO, final String uuid, final String title, final String appId) {
		if (windowGVO != null && windowGVO.getIcon() != null && windowGVO.getIcon().length() > 0) {
			final String windowId = windowGVO.getId();
			final DesktopIcon desktopIcon = new DesktopIcon(windowGVO.getIcon(), title);
			fillInMandatoryOnly(windowGVO, desktopIcon, uuid, windowId, appId);

			desktopIcon.addDoubleClickHandler(new DoubleClickHandler() {

				public void onDoubleClick(DoubleClickEvent event) {
					// ClientApplicationContext.getInstance().removeWindow(windowId,
					// uuid);
					WindowPanel existingWindowPanel = ClientApplicationContext.getInstance().windowIdExists(windowId, uuid);
					if (existingWindowPanel == null) {
						EventListenerGVO[] events = windowGVO.getEvents();
						if (events != null) {
							String eventType = QAMLConstants.EVENT_ONCLICK;
							for (EventListenerGVO eventGVO : events) {
								if (eventGVO.getEventComponentId().equals(windowId) && eventGVO.getEventListenerType().equals(eventType)) {
									createCallBack4OpenWindow(desktopIcon, eventType, eventGVO);
									break;
								}
							}
						}
//						MainFactoryActions.getUIByUUID(uuid, windowId);
					} else {
						Iterator itr = ClientApplicationContext.getInstance().getHorizontalPanel().iterator();
						while (itr.hasNext()) {
							Widget w = (Widget) itr.next();
							if (w.getTitle().equals(title)) {
								existingWindowPanel.setWindowState(WindowState.NORMAL);
								existingWindowPanel.show();
								ClientApplicationContext.getInstance().getHorizontalPanel().remove(w);
							}
						}
						existingWindowPanel.toFront();
					}

				}
			});
			ClientApplicationContext.getInstance().getDragController().makeDraggable(desktopIcon, desktopIcon.getTitleLabel());
			ClientApplicationContext.getInstance().addDesktopIcon(desktopIcon);
		}
	}

	public static MenuItem createMenuItem(String itemId, String itemLabel, String uuid, String context, MenuBar subMenu) {
		MenuItem menuItem = new MenuItem(itemLabel, subMenu);
		MenuItemGVO menuItemGVO = new MenuItemGVO();
		menuItemGVO.setId(itemId);
		RendererHelper.fillIn(menuItemGVO, menuItem, uuid, null, context);
		return menuItem;
	}

	private static MenuItem createMenuItem(final String title, final String uuid, final String windowId, final UIGVO uiGVO, final boolean tryMe) {
		final MenuItem menuItem = new MenuItem(title, (Command) null);
		MenuItemGVO menuItemGVO = new MenuItemGVO();
		menuItemGVO.setId(windowId);
		RendererHelper.fillIn(menuItemGVO, menuItem, uuid, null, uiGVO.getAppId());

		Command command = new Command() {
			public void execute() {

				if(ClientApplicationContext.getInstance().isDesktopNotificationPossible() && Notification.isNotificationNotAllowed()){
					Notification.requestPermission();
				}

				// ClientApplicationContext.getInstance().removeWindow(windowId,
				// uuid);
				WindowPanel existingWindowPanel = ClientApplicationContext.getInstance().windowIdExists(windowId, uuid);
				if (existingWindowPanel == null) {
					if (tryMe) {
						MainFactoryActions.getUIByUUID(uuid, windowId);
					} else {
						String eventType = QAMLConstants.EVENT_ONCLICK;
						EventListenerGVO eventGVO = uiGVO.getEventMap().get(windowId);
						createCallBack4OpenWindow(menuItem, eventType, eventGVO);
					}
					// MainFactoryActions.getUIByUUID(uuid, windowId);
				} else {
					Iterator itr = ClientApplicationContext.getInstance().getHorizontalPanel().iterator();
					while (itr.hasNext()) {
						Widget w = (Widget) itr.next();
						if (w.getTitle().equals(title)) {
							existingWindowPanel.setWindowState(WindowState.NORMAL);
							existingWindowPanel.show();
							ClientApplicationContext.getInstance().getHorizontalPanel().remove(w);
						}
					}
					existingWindowPanel.toFront();
				}
			}

		};
		menuItem.setCommand(command);

		return menuItem;
	}

	private static void createCallBack4OpenWindow(UIObject sender, String eventType, EventListenerGVO eventGVO) {
		List<InputVariableGVO> input = new ArrayList<InputVariableGVO>();
		CallbackHandler.createCallBack(sender, eventType, eventGVO, input);
	}

	private static void fillInMandatoryOnly(ComponentGVO componentGVO, UIObject uiObject, String uuid, String parent, String context) {
		RendererHelper.addId(componentGVO, uiObject, uuid, parent, context, false);
		RendererHelper.addUUID(componentGVO, uiObject, uuid);
	}
}
