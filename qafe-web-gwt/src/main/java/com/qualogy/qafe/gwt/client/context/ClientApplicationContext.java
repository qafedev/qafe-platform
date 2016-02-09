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
package com.qualogy.qafe.gwt.client.context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.gwt.mosaic.ui.client.WindowPanel;

import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.logging.client.ConsoleLogHandler;
import com.google.gwt.logging.client.SystemLogHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;
import com.qualogy.qafe.gwt.client.component.DesktopIcon;
import com.qualogy.qafe.gwt.client.component.DialogComponent;
import com.qualogy.qafe.gwt.client.component.HasResultHandlers;
import com.qualogy.qafe.gwt.client.component.ProgressIndicator;
import com.qualogy.qafe.gwt.client.component.QMenuBar;
import com.qualogy.qafe.gwt.client.component.QWindowPanel;
import com.qualogy.qafe.gwt.client.component.ResultHandler;
import com.qualogy.qafe.gwt.client.component.ShowPanelComponent;
import com.qualogy.qafe.gwt.client.component.html5.notifications.Notification;
import com.qualogy.qafe.gwt.client.exception.GWTServiceException;
import com.qualogy.qafe.gwt.client.factory.MainFactoryActions;
import com.qualogy.qafe.gwt.client.factory.WindowFactory;
import com.qualogy.qafe.gwt.client.storage.DataStorage;
import com.qualogy.qafe.gwt.client.storage.impl.LocalDataStorage;
import com.qualogy.qafe.gwt.client.ui.images.DataGridImageBundle;
import com.qualogy.qafe.gwt.client.ui.images.MainPanelImageBundle;
import com.qualogy.qafe.gwt.client.ui.renderer.events.WindowCloseHandler;
import com.qualogy.qafe.gwt.client.util.ComponentRepository;
import com.qualogy.qafe.gwt.client.util.ExceptionUtil;
import com.qualogy.qafe.gwt.client.util.QEffects;
import com.qualogy.qafe.gwt.client.vo.data.GDataObject;
import com.qualogy.qafe.gwt.client.vo.functions.dialog.GenericDialogGVO;
import com.qualogy.qafe.gwt.client.vo.ui.UIGVO;
import com.qualogy.qafe.gwt.client.vo.ui.UIVOCluster;
import com.qualogy.qafe.gwt.client.vo.ui.WindowGVO;


public class ClientApplicationContext implements HasResultHandlers {

	Logger logger = Logger.getLogger("ClientApplicationContext");
	private String appUUID = null;
	private String windowSession = null;
	private String globalDateFormat = null;

	private int desktopIconHGap = 15;
	private int desktopIconVGap = 15;
	private final static int INIT_START_X_POSITION = 65;
	private final static int INIT_START_Y_POSITION = 60;
	private final static int STEP_SIZE_X = 10;
	private final static int STEP_SIZE_Y = 10;
	private final static int MARGIN_X = 100;
	private final static int MARGIN_Y = 100;
	private int startXPosition = INIT_START_X_POSITION;
	private int startYPosition = INIT_START_Y_POSITION;

	private Boolean debugMode = Boolean.FALSE;
	private Boolean reloadable = Boolean.FALSE;
	private Boolean showLog = Boolean.FALSE;
	private boolean dektopNotificationDesired;


	private MenuBar mainMenu = null;
	private MenuBar applicationsMenu = null;
	private MenuBar tryMeMenu = null;
	private PickupDragController dragController = null;
	private SimplePanel mainPanel = null;

	private MenuBar labsMenu;
	private Element styleElement;
	private Panel dockPanel;

	private List<MenuBar> defaultMenus = new ArrayList<MenuBar>();
	private MainPanelImageBundle mainPanelImageBundle = (MainPanelImageBundle) GWT.create(MainPanelImageBundle.class);
	private DataGridImageBundle datagridImageBundle = (DataGridImageBundle) GWT.create(DataGridImageBundle.class);
	private ProgressIndicator pi = GWT.create(ProgressIndicator.class);
	private NotificationBox notificationBox = new NotificationBox();
	// After Callback if anything is to be done that should be registered here to do the call back onResult later.
	private HashMap<String, ResultHandler> resultHandlers = new HashMap<String, ResultHandler>();
	private Map<String, Map<String,WindowCloseHandler>> windowCloseHandlers = new HashMap<String, Map<String, WindowCloseHandler>>();
	private Map<String, Map<String, WindowPanel>> mapOfWindows = new HashMap<String, Map<String, WindowPanel>>();
	private List<WindowPanel> windows = new ArrayList<WindowPanel>();
	private List<DesktopIcon> desktopIcons = new ArrayList<DesktopIcon>();
	private Map<String, String> globalConfigurations = new HashMap<String, String>();
	private static ClientApplicationContext singleton = new ClientApplicationContext();
	private Map<String, List<String>> panelDefinitionsOpened = new HashMap<String, List<String>>();
	private Map<String, UIGVO> applications = new HashMap<String, UIGVO>();
	private Boolean clientSideEventEnabled = Boolean.FALSE;
	
	private DataStorage dataStorage;

	public int externalWindowCount = 0;
	public int internalWindowCount = 0;
	public static final int MDI = 1;
	public static final int SDI = 2;
	public int mode = MDI;
	public Boolean dockMode = Boolean.FALSE;
	public MenuBar bottomMenuBar = new QMenuBar();
	public HorizontalPanel horizontalPanel = new HorizontalPanel();

	public void addResultHandler(String senderId, String listenerType, ResultHandler handler) {
		if (handler != null) {
			String key = senderId + listenerType;
			resultHandlers.put(key, handler);
		}
	}

	public boolean removeResultHandler(String senderId, String listenerType) {
		String key = senderId + listenerType;
		if (resultHandlers.containsKey(key)) {
			resultHandlers.remove(key);
			return true;
		}
		return false;
	}

	public void fireResult(GDataObject resultObject) {
		if(resultObject != null) {
			String senderId = resultObject.getSenderId();
			String listenerType = resultObject.getListenerType();
			String key = senderId + listenerType;
			if (resultHandlers.containsKey(key)) {
				ResultHandler resultHandler = resultHandlers.get(key);
				resultHandler.onResult(senderId, listenerType);
			}
		}
	}

	public void addWindowCloseHandler(String appId, String windowId, String eventId, WindowCloseHandler handler) {
		String key = appId + windowId;
		Map<String, WindowCloseHandler> eventToHandlerMap = windowCloseHandlers.get(key);
		if (eventToHandlerMap == null) {
			eventToHandlerMap = new HashMap<String, WindowCloseHandler>();
			windowCloseHandlers.put(key, eventToHandlerMap);
		}
		eventToHandlerMap.put(eventId, handler);
	}

	public boolean removeWindowCloseHandlers(String appId, String windowId) {
		String key = appId + windowId;
		if (windowCloseHandlers.containsKey(key)) {
			windowCloseHandlers.remove(key);
			return true;
		}
		return false;
	}

	public void fireWindowClose(String appId, String windowId) {
		String key = appId + windowId;
		Map<String, WindowCloseHandler> eventToHandlerMap = windowCloseHandlers.get(key);
		if (eventToHandlerMap != null) {
			for (WindowCloseHandler handler: eventToHandlerMap.values()){
				handler.onWindowClose(appId, windowId);
			}
			removeWindowCloseHandlers(appId, windowId);
		}
	}

	public boolean isTimerScheduledForEvent(String appId, String windowId, String eventId){
		String key = appId + windowId;
		Map<String, WindowCloseHandler> eventToHandlerMap = windowCloseHandlers.get(key);
		if (eventToHandlerMap != null) {
			return eventToHandlerMap.containsKey(eventId);
		}
		return false;
	}

	public ProgressIndicator getPi() {
		return pi;
	}

	public void setPi(ProgressIndicator pi) {
		this.pi = pi;
	}

	public DataGridImageBundle getDatagridImageBundle() {
		return datagridImageBundle;
	}

	public Map<String, String> getGlobalConfigurations() {
		return globalConfigurations;
	}

	public void setGlobalConfigurations(Map<String, String> globalConfigurations) {
		this.globalConfigurations = globalConfigurations;
		setDektopNotificationDesired(Boolean.valueOf(globalConfigurations.get("dektop.notification.desired")));
	}

	public List<DesktopIcon> getDesktopIcons() {
		return desktopIcons;
	}

	public List<WindowPanel> getWindows() {
		return windows;
	}

	public void setWindows(List<WindowPanel> windows) {
		this.windows = windows;
	}

	public Boolean getReloadable() {
		return reloadable;
	}

	public void setReloadable(Boolean reloadable) {
		this.reloadable = reloadable;
	}

	public MainPanelImageBundle getMainPanelImageBundle() {
		return mainPanelImageBundle;
	}

	public SimplePanel getMainPanel() {
		return mainPanel;
	}

	public void setMainPanel(SimplePanel mainPanel) {
		this.mainPanel = mainPanel;
	}

	public PickupDragController getDragController() {
		return dragController;
	}

	public MenuBar getTryMeMenu() {
		return tryMeMenu;
	}

	public void setTryMeMenu(MenuBar tryMeMenu) {
		this.tryMeMenu = tryMeMenu;
	}

	public Element getStyleElement() {
		return styleElement;
	}

	public Boolean getShowLog() {
		return showLog;
	}

	public MenuBar getApplicationsMenu() {
		return applicationsMenu;
	}

	public void setApplicationsMenu(MenuBar applicationsMenu) {
		this.applicationsMenu = applicationsMenu;
	}

	private ClientApplicationContext() {
		// create a DragController to manage drag-n-drop actions
		// note: This creates an implicit DropController for the boundary panel
		dragController = new PickupDragController(RootPanel.get(), true);
		logger.addHandler(new ConsoleLogHandler());
		logger.addHandler(new SystemLogHandler ());

		// Request parameters
		Map<String,List<String>> parameters = Window.Location.getParameterMap();
		map = new HashMap<String,String>();
		for (String key: parameters.keySet()){
			map.put(key,Window.Location.getParameter(key));
		}
	};

	public static ClientApplicationContext getInstance() {
		return singleton;
	}





	public void setBusy(boolean busy) {
		if (busy) {
			pi.start();
			Window.setStatus("Processing....");
		} else {
			Window.setStatus("Finished processing....");
			pi.stop();
		}
	}

	public void setLogText(String title, String text) {
		if (isMDI()) {

		} else {
			if (getShowLog()) {
				notify(title, text);
			}
		}
		Window.setStatus(title + ": " + text);
	}

	public void setLogText(String text) {
		setLogText("Log message", text);
	}



	public int getStartXPosition() {
		return startXPosition;
	}

	public int getStartYPosition() {
		return startYPosition;
	}

	public void updatePosition() {
		if (getWindows().size() > 1) {
			if (Window.getClientWidth() > (getStartXPosition() + STEP_SIZE_X + MARGIN_X)) {
				startXPosition += STEP_SIZE_X;
			} else {
				startXPosition = INIT_START_X_POSITION;
				startYPosition = INIT_START_Y_POSITION + STEP_SIZE_Y;
			}

			if (Window.getClientHeight() > (getStartYPosition() + STEP_SIZE_Y + MARGIN_Y)) {
				startYPosition += STEP_SIZE_Y;
			} else {
				startXPosition = INIT_START_X_POSITION + STEP_SIZE_X;
				startYPosition = INIT_START_Y_POSITION;
			}
		} else {
			startXPosition = INIT_START_X_POSITION;
			startYPosition = INIT_START_Y_POSITION;
		}

	}

	public void log(String message, Throwable e) {
		log("log", message, false, false, e);
	}

	public void log(String title, String message, boolean alert, boolean showInfo, Throwable e) {
		log(title, message, GenericDialogGVO.TYPE_ERROR, alert, showInfo, e);
	}

	public void log(String title, String message, String dialogType, boolean alert, boolean showInfo, Throwable e) {
		logger.log(Level.INFO, title +" ||\n " + message);
		if (title != null) {
			GWT.log(title + ":" + message, e);
			if (e != null) {
				String stackTrace = getStackTrace(e);
				setLogText(title, message + " (" + e.getMessage() + " -- " + stackTrace + ")");
			} else {
				setLogText(title, message);
			}
		} else {
			logger.log(Level.SEVERE, message,  e);
			if (e != null) {
				String stackTrace = getStackTrace(e);
				setLogText(message + " (" + e.getMessage() + " -- " + stackTrace + ")");

			} else {
				setLogText(message);
			}
			setLogText(message + " (" + e.getMessage() + ")");
		}
		if (e != null && isDebugMode()) {
			notify(title, e.toString());
		}
		if (showInfo) {
			if (e != null) {
				notify(title, message + " (" + e.getMessage() + ")");
			} else {
				notify(title, message);
			}
		}
		if (alert) {
			if ((title == null)) {
				title = ExceptionUtil.messageTitle;
			}
			showMessage(title, ExceptionUtil.getMessage(e, message), dialogType, e);
		}
	}

	private void showMessage(String title, String message, String dialogType, Throwable e) {
		if (e instanceof GWTServiceException) {
			GWTServiceException gwtException = (GWTServiceException) e;
			DialogComponent.showDialog(title, message, GenericDialogGVO.TYPE_ERROR, gwtException.getDetailedMessage(), 0, 0);
		} else {
			DialogComponent.showDialog(title, message, dialogType, null, 0, 0);
		}
	}

	private String getStackTrace(Throwable e) {
		if (e != null) {
			StackTraceElement[] ste = e.getStackTrace();
			StringBuffer s = new StringBuffer( "Trace: "+ e.getMessage());
			for (int i = 0; i < ste.length; i++) {
				s.append("\r" + ste[i].toString());
			}
			return s.toString();
		} else {
			return "(no stack)";
		}
	}

	public void log(String message) {
		log("log", message, false);
	}
	
	public void log(String message, boolean alert) {
	    log("log", message, alert);
	}

	public void log(String title, String message, boolean alert) {
		log(title, message, alert, false, null);
	}

    // CHECKSTYLE.OFF: CyclomaticComplexity
	public void clearAll() {
		if (ClientApplicationContext.getInstance().getApplicationsMenu() != null) {
			ClientApplicationContext.getInstance().getApplicationsMenu().clearItems();
		}
		if (ClientApplicationContext.getInstance().getMainMenu() != null) {
			ClientApplicationContext.getInstance().getMainMenu().clearItems();
		}
		if (ClientApplicationContext.getInstance().getTryMeMenu() != null) {
			ClientApplicationContext.getInstance().getTryMeMenu().clearItems();
			ClientApplicationContext.getInstance().setTryMeMenu(null);
		}
		// Close the windows
		if (isMDI()) {
			for (int i = 0; i < windows.size(); i++) {
				WindowPanel wp = windows.get(i);
				if (wp instanceof QWindowPanel) {
					if (((QWindowPanel) wp).isManaged()) {
						wp.hide();
						i--;
					}
				}
			}
			if (ClientApplicationContext.getInstance().getHorizontalPanel().getWidgetCount() > 0) {
				int minimisedWindowCount = ClientApplicationContext.getInstance().getHorizontalPanel().getWidgetCount();
				for (int index = 0; index < minimisedWindowCount; index++) {
					ClientApplicationContext.getInstance().getHorizontalPanel().remove(index);
				}
			}

			if (ClientApplicationContext.getInstance().getDockPanel() != null) {
				ClientApplicationContext.getInstance().getDockPanel().clear();
			}
		} else {
			WindowFactory.clearWidgetFromMainPanel();
		}

		// Collect and close the panels, triggered by show-panel built-in
		List<ShowPanelComponent> showPanelComponents = new ArrayList<ShowPanelComponent>();
		Map<String, List<UIObject>> components = ComponentRepository.getInstance().getComponentMap();
		Iterator<String> componentIds = components.keySet().iterator();
		while (componentIds.hasNext()) {
			String componentId = componentIds.next();
			List<UIObject> uiObjects = components.get(componentId);
			for (UIObject uiObject : uiObjects) {
				if (uiObject instanceof ShowPanelComponent) {
					showPanelComponents.add((ShowPanelComponent)uiObject);
				}
			}
		}
		while (!showPanelComponents.isEmpty()) {
			ShowPanelComponent showPanelComponent = showPanelComponents.remove(0);

			// This will call showPanelComponent.onDetach()
			showPanelComponent.hide();
		}

		ComponentRepository.getInstance().clearAll();
		for (DesktopIcon di : desktopIcons) {
			di.removeFromParent();
		}
		desktopIcons.clear();
	}
    // CHECKSTYLE.ON: CyclomaticComplexity

	public MenuBar getMainMenu() {
		return mainMenu;
	}

	public void setMainMenu(MenuBar mainMenu) {
		this.mainMenu = mainMenu;
	}

	public void addDefaultMenu(MenuBar menu) {
		defaultMenus.add(menu);
	}

	public List<MenuBar> getDefaultMenus() {
		return defaultMenus;
	}

	public void setLabsMode(Boolean labs) {
		if (labs != null) {
			if (labsMenu != null) {
				labsMenu.setVisible(labs.booleanValue());
				if (!labs.booleanValue()) {
					if (mainMenu instanceof QMenuBar) {
						((QMenuBar) mainMenu).removeMenu(labsMenu);
					}
				}
			}
		}
	}

	public void setLabsMenu(MenuBar labsMenu) {
		this.labsMenu = labsMenu;
	}

    // CHECKSTYLE.OFF: CyclomaticComplexity
	public void postInitialization(UIVOCluster ui) {
		if (ui != null) {
			setLabsMode(ui.isDebugMode());
			setDebugMode(ui.isDebugMode());
			setShowLog(ui.getShowLog());
			setReloadable(ui.getReloadable());
			setGlobalDateFormat(ui.getGlobalDateFormat());
			setClientSideEventEnabled(ui.isClientSideEventEnabled());
			setApplications(ui.getVos());
			String loadMessages = null;
			for (String loadMessage : ui.getMessages()) {
				if (loadMessages == null) {
					loadMessages = "";
				}
				loadMessages += loadMessage + "<br>";
			}
			if (loadMessages != null) {
				ClientApplicationContext.getInstance().log("There were load errors", loadMessages, true, true, null);
			}
			for (Map messageWithType : ui.getMessagesWithType()) {
				if (messageWithType != null) {
					Iterator itr = messageWithType.keySet().iterator();
					while (itr.hasNext()) {
						Object key = itr.next();
						if (key instanceof String) {
							String dialogType = (String)key;
							String message = (String)messageWithType.get(dialogType);
							ClientApplicationContext.getInstance().log("", message, dialogType, true, true, null);
						}
					}
				}
			}
			if (ui.getVos() != null) {
				for (int i = 0; i < ui.getVos().length; i++) {

					if (ui.getVos()[i].getWindows() != null) {
						WindowGVO[] windows = ui.getVos()[i].getWindows();
						for (int j = 0; j < windows.length; j++) {
							if (windows[j] != null) {
								if (windows[j].getLoadOnStartup()) {
									MainFactoryActions.getUIByUUID(ui.getVos()[i].getUuid(), windows[j].getId());
								}
							}
						}
					}
				}
			}
			// Maybe it was a system application, then another attribute of he
			// cluster should be checked.
			if (ui.getSystemMenuApplication() != null) {

				WindowGVO[] windows = ui.getSystemMenuApplication().getWindows();
				for (int j = 0; j < windows.length; j++) {
					if (windows[j] != null) {
						if (windows[j].getLoadOnStartup()) {
							MainFactoryActions.getUIByUUID(ui.getSystemMenuApplication().getUuid(), windows[j].getId());
						}
					}
				}
			}
		}

	}
    // CHECKSTYLE.ON: CyclomaticComplexity

	private void setApplications(UIGVO[] appGVOs) {
        if (appGVOs == null) {
            return;
        }
        for (UIGVO appGVO : appGVOs) {
            String appId = appGVO.getAppId();
            applications.put(appId, appGVO);
        }
    }

    private void setClientSideEventEnabled(Boolean clientSideEventEnabled) {
        this.clientSideEventEnabled = clientSideEventEnabled;
    }
	
	private void setGlobalDateFormat(String globalDateFormat) {
		this.globalDateFormat = globalDateFormat;
	}

	public String getGlobalDateFormat() {
		return globalDateFormat;
	}

	private void setShowLog(Boolean showLog) {
		this.showLog = showLog;
	}

	public void setMode(int mode) {
		this.mode = mode;
	}

	public boolean isMDI() {
		return this.mode == MDI;
	}

	public void setDebugMode(Boolean debugMode) {
		this.debugMode = debugMode;
	}

	public boolean isDebugMode() {
		return debugMode;
	}

	public MenuBar getBottomMenuBar() {
		return bottomMenuBar;
	}

	public void setBottomMenuBar(MenuBar bottomMenuBar) {
		this.bottomMenuBar = bottomMenuBar;
	}

	public HorizontalPanel getHorizontalPanel() {
		return horizontalPanel;
	}

	public void setHorizontalPanel(HorizontalPanel horizontalPanel) {
		this.horizontalPanel = horizontalPanel;
	}

	public void addWindows(UIGVO ui, WindowPanel window, String windowId) {
		getWindows().add(window);
		Map<String, WindowPanel> listOfWindows = null;
		if (mapOfWindows.containsKey(ui.getUuid())) {
			listOfWindows = mapOfWindows.get(ui.getUuid());
		} else {
			listOfWindows = new HashMap<String, WindowPanel>();
			mapOfWindows.put(ui.getUuid(), listOfWindows);
		}
		if (listOfWindows != null) {
			// check to close the old window
			WindowPanel wp = listOfWindows.get(windowId);
			if (wp != null) {
				try {
					wp.hide();
				} catch (Exception e) {
					ClientApplicationContext.getInstance().log("Exception when ClientApplicationContext::addWindows, continue execution -->" + e.getMessage(), null);
				}
			}
			listOfWindows.put(windowId, window);
		}
	}

	public WindowPanel getWindow(String uuid, String windowId) {
		Map<String, WindowPanel> listOfWindows = null;
		WindowPanel window = null;
		if (mapOfWindows.containsKey(uuid)) {
			listOfWindows = mapOfWindows.get(uuid);
		} else {
			listOfWindows = new HashMap<String, WindowPanel>();
			mapOfWindows.put(uuid, listOfWindows);
		}
		if (listOfWindows != null) {
			if (listOfWindows.containsKey(windowId)) {
				window = listOfWindows.get(windowId);

			}
		}
		return window;
	}

	public void closeAllWindowsForUUID(String uuid, String windowId) {
		Map<String, WindowPanel> listOfWindows = null;
		if (mapOfWindows.containsKey(uuid)) {
			listOfWindows = mapOfWindows.get(uuid);
		} else {
			listOfWindows = new HashMap<String, WindowPanel>();
			mapOfWindows.put(uuid, listOfWindows);
		}
		if (listOfWindows != null) {
			for (WindowPanel wp : listOfWindows.values()) {
				wp.hide(false);
			}
			listOfWindows.clear();
		}
	}

	public void removeWindow(String windowId, String context, String uuid) {
		ComponentRepository.getInstance().removeAllItemsForWindow(windowId, context);
		Map<String, WindowPanel> listOfWindows = null;
		WindowPanel window = null;
		if (mapOfWindows.containsKey(uuid)) {
			listOfWindows = mapOfWindows.get(uuid);
		} else {
			listOfWindows = new HashMap<String, WindowPanel>();
			mapOfWindows.put(uuid, listOfWindows);
		}
		if (listOfWindows != null) {
			if (listOfWindows.containsKey(windowId)) {
				window = listOfWindows.get(windowId);
				if (window instanceof QWindowPanel && ((QWindowPanel) window).isManaged()) {
					listOfWindows.remove(windowId);
					window.hide();
				}
			}
		}
	}

	/**
	 * This method is used to check weather a window panel already exists. The
	 * check is done while trying to invoke the panel when there is a chance
	 * that the same panel could be in the minimized state.
	 *
	 * @param String
	 *            windowId
	 * @param String
	 *            uuId
	 * @return WindowPanel
	 * */
	public WindowPanel windowIdExists(String windowId, String uuId) {
		WindowPanel window = null;
		if (mapOfWindows != null) {
			Map<String, WindowPanel> listOfWindows = (Map) mapOfWindows.get(uuId);
			if (listOfWindows != null) {
				window = listOfWindows.get(windowId);
			}
		}
		return window;
	}

	/**
	 * This method is used to remove the window panel from the Map object. This
	 * method is invoked when window panel is closed.
	 * */
	public void removeFromMapOfWindows(String uuId, String windowId) {
		if (mapOfWindows.containsKey(uuId)) {
			Map<String, WindowPanel> uuidWindows = mapOfWindows.get(uuId);
			if(uuidWindows != null && uuidWindows.containsKey(windowId)) {
				uuidWindows.remove(windowId);
				if(uuidWindows.isEmpty()) {
					mapOfWindows.remove(uuId);
				}
			}

		}
	}

	public void setStyleElement(Element styleElement) {
		this.styleElement = styleElement;

	}

	public List<String> getWindowUUIDs() {
		Iterator<String> keys = mapOfWindows.keySet().iterator();
		List<String> keysOfUUIDs = new ArrayList<String>();
		while (keys.hasNext()) {
			keysOfUUIDs.add(keys.next());
		}
		return keysOfUUIDs;
	}

	public void addDesktopIcon(DesktopIcon desktopIcon) {
		if (desktopIcon != null) {
			desktopIcons.add(desktopIcon);
			int rowCount = desktopIcons.size();
			int columnCount = 0;
			int posX = 0;
			int posY = desktopIconVGap + (rowCount * desktopIconVGap) + (rowCount * DesktopIcon.HEIGHT);
			if (posY + DesktopIcon.HEIGHT > Window.getClientHeight()) {
				columnCount = columnCount + 1;
				rowCount = 0;
			}
			posX = desktopIconHGap + (columnCount * desktopIconHGap) + (columnCount * DesktopIcon.WIDTH);
			posY = desktopIconVGap + (rowCount * desktopIconVGap) + (rowCount * DesktopIcon.HEIGHT);
			RootPanel.get().add(desktopIcon, posX, posY);
		}
	}

	public String getAppUUID() {
		if(appUUID == null) {
			appUUID = DOM.getElementById("appId").getAttribute("value");
			windowSession = DOM.getElementById("winId").getAttribute("value");
			logger.log(Level.ALL, "AppUUID=" +appUUID +"|| windowSession" + windowSession);
		}
		return appUUID;
	}

	public String getWindowSession() {
		if (windowSession == null) {
			appUUID = DOM.getElementById("appId").getAttribute("value");
			windowSession = DOM.getElementById("winId").getAttribute("value");
			logger.log(Level.ALL, "AppUUID=" +appUUID +"|| windowSession" + windowSession);
		}
		return windowSession;
	}

	public void setDockMode(Boolean dockMode) {
		this.dockMode = dockMode;
	}

	public Boolean getDockMode() {
		return dockMode;
	}

	public void setDockPanel(Panel dockPanel) {
		this.dockPanel = dockPanel;
	}

	public Panel getDockPanel() {
		return dockPanel;
	}

	private Map<String, MenuBar> menuBars = new HashMap<String, MenuBar>(17);

	public Map<String, MenuBar> getMenuBars() {
		return menuBars;
	}

	public void addMenu(String id, MenuBar subMenuBar) {
		menuBars.put(id, subMenuBar);
	}

	public void notify(String title, String text) {
		if (isDesktopNotificationPossible() && Notification.isNotificationAllowed()){
			Notification.createIfSupported("http://www.qafe.com/wp-content/themes/qafe/img/logo_244x190.png", title, text);
		} else {
			notificationBox.setTitle(title);
			notificationBox.setText(text);
			notificationBox.setVisible(true);
			QEffects.fadeOut(notificationBox,2000, 50,false);
		}
	}


	public class NotificationBox extends FlowPanel {
		private Label title;

		public NotificationBox() {
			title = new Label();
			text = new Label();
			add(title);
			add(text);
			setStyleName("notificationBox");
			setVisible(false);
			RootPanel.get().add(this, Window.getClientWidth() - 200, Window.getClientHeight() - 100);
		}

		public void setTitle(String title) {
			this.title.setText(title);
		}

		public void setText(String text) {
			this.text.setText(text);
		}

		private Label text;
	}

	public boolean isDesktopNotificationPossible() {
		if(dektopNotificationDesired && Notification.isSupported()) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isDektopNotificationDesired() {
		return dektopNotificationDesired;
	}

	public void setDektopNotificationDesired(boolean dektopNotificationDesired) {
		this.dektopNotificationDesired = dektopNotificationDesired;
	}

	Map<String,String> map = null;

	public Map<String, String> getParameters() {
		return map;
	}

	//////////////////// PRERENDERING CACHING ///////////////
	Map<String, Widget> prerenderedWindow = new HashMap<String,Widget>();
	Map<String, UIGVO> prerenderedUIVO = new HashMap<String,UIGVO>();
	public Widget getPrerenderedWindow(String appId, String windowId) {
		String key = appId+"."+windowId;
		Widget w = prerenderedWindow.get(key);
		return w;
	}

	public void setPrerenderedWindow(String appId, String windowId, Widget w) {
		String key = appId+"."+windowId;
		prerenderedWindow.put(key,w);

	}
	public void clearPrerendering(){
		prerenderedWindow.clear();
		prerenderedUIVO.clear();
	}

	public UIGVO getPrerenderedUIVO(String appId){
		return prerenderedUIVO.get(appId);
	}
	public void setPrerenderingUIVO(String appId,UIGVO ui){
		prerenderedUIVO.put(appId, ui);
	}

	public Map<String, List<String>> getPanelDefinitionsOpened() {
		return panelDefinitionsOpened;
	}

	public void addPanelDefinitionsOpened(String windowId, String panelDefId) {
		if(panelDefinitionsOpened.get(windowId) == null) {
			List<String> panelDefinitions = new ArrayList<String>();
			panelDefinitionsOpened.put(windowId, panelDefinitions);
		}
		panelDefinitionsOpened.get(windowId).add(panelDefId);
	}

	public void removePanelDefinitionsOpened(String windowId, String panelDefId) {
		if(panelDefinitionsOpened.get(windowId) != null) {
			panelDefinitionsOpened.get(windowId).remove(panelDefId);
		}
	}

	public List<String> getPanelDefinitionsOpened(String windowId) {
		return panelDefinitionsOpened.get(windowId);
	}

	public UIGVO getApplication(String appId) {
	    return applications.get(appId);
	}
	
    public Boolean isClientSideEventEnabled() {
        return clientSideEventEnabled;
    }

    public DataStorage getDataStorage() {
        if (dataStorage == null) {
            dataStorage = new LocalDataStorage();
        }
        return dataStorage;
    }
}