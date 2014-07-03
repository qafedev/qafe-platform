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
package com.qualogy.qafe.mgwt.client.context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.i18n.client.Dictionary;
import com.google.gwt.logging.client.ConsoleLogHandler;
import com.google.gwt.logging.client.SystemLogHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.mgwt.ui.client.dialog.Dialogs;
import com.qualogy.qafe.mgwt.client.component.HasResultHandlers;
import com.qualogy.qafe.mgwt.client.component.ResultHandler;
import com.qualogy.qafe.mgwt.client.exception.GWTServiceException;
import com.qualogy.qafe.mgwt.client.factory.MainFactoryActions;
import com.qualogy.qafe.mgwt.client.vo.data.GDataObject;
import com.qualogy.qafe.mgwt.client.vo.functions.dialog.GenericDialogGVO;
import com.qualogy.qafe.mgwt.client.vo.ui.UIGVO;
import com.qualogy.qafe.mgwt.client.vo.ui.UIVOCluster;
import com.qualogy.qafe.mgwt.client.vo.ui.WindowGVO;


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
		private SimplePanel mainPanel = null;

	private MenuBar labsMenu;
	private Element styleElement;
	private Panel dockPanel;

	private List<MenuBar> defaultMenus = new ArrayList<MenuBar>();
	// After Callback if anything is to be done that should be registered here to do the call back onResult later.
	private HashMap<String, ResultHandler> resultHandlers = new HashMap<String, ResultHandler>();
	private Map<String, String> globalConfigurations = new HashMap<String, String>();
	private static ClientApplicationContext singleton = new ClientApplicationContext();

	public int externalWindowCount = 0;
	public int internalWindowCount = 0;
	public static final int MDI = 1;
	public static final int SDI = 2;
	public int mode = MDI;
	public Boolean dockMode = Boolean.FALSE;
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







	public Map<String, String> getGlobalConfigurations() {
		return globalConfigurations;
	}

	public void setGlobalConfigurations(Map<String, String> globalConfigurations) {
		this.globalConfigurations = globalConfigurations;
		setDektopNotificationDesired(Boolean.valueOf(globalConfigurations.get("dektop.notification.desired")));
	}



	public Boolean getReloadable() {
		return reloadable;
	}

	public void setReloadable(Boolean reloadable) {
		this.reloadable = reloadable;
	}



	public SimplePanel getMainPanel() {
		return mainPanel;
	}

	public void setMainPanel(SimplePanel mainPanel) {
		this.mainPanel = mainPanel;
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

			Window.setStatus("Processing....");
		} else {
			Window.setStatus("Finished processing....");
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
			if (e instanceof GWTServiceException) {
				GWTServiceException gwtException = (GWTServiceException) e;
				} else {
			}
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

	public void log(String title, String message, boolean alert) {
		log(title, message, alert, false, null);
	}

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

	}

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

    // CHECKSTYLE.OFF: CyclomaticComplexity
	public void postInitialization(UIVOCluster ui) {
		// TODO ???
		if (ui != null) {
			setDebugMode(ui.isDebugMode());
			setShowLog(ui.getShowLog());
			setReloadable(ui.getReloadable());
			setGlobalDateFormat(ui.getGlobalDateFormat());
			String loadMessages = null;
			for (String loadMessage : ui.getMessages()) {
				if (loadMessages == null) {
					loadMessages = "";
				}
				loadMessages += loadMessage + "\n";
			}
			if (loadMessages != null) {
				ClientApplicationContext.getInstance().log("There were load errors", loadMessages, true, true, null);
			}
			for (Map messageWithType : ui.getMessagesWithType()) {
				if (messageWithType != null) {
					Iterator itr = messageWithType.keySet().iterator();
					while (itr.hasNext()) {
						Object key = itr.next();
						if (key instanceof Integer) {
							Integer dialogType = (Integer)key;
							String message = (String)messageWithType.get(dialogType);
							ClientApplicationContext.getInstance().log("", message, String.valueOf(dialogType), true, true, null);
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



	public HorizontalPanel getHorizontalPanel() {
		return horizontalPanel;
	}

	public void setHorizontalPanel(HorizontalPanel horizontalPanel) {
		this.horizontalPanel = horizontalPanel;
	}










	public void setStyleElement(Element styleElement) {
		this.styleElement = styleElement;

	}



	public String getAppUUID() {
		if(appUUID == null) {
			Dictionary appDetails = Dictionary.getDictionary("AppDetails");
			appUUID = appDetails.get("appUUID");
			windowSession = appDetails.get("windowSession");
			logger.log(Level.ALL, "AppUUID=" +appUUID +"|| windowSession" + windowSession);
		}
		return appUUID;
	}

	public String getWindowSession() {
		if (windowSession == null) {
			Dictionary appDetails = Dictionary.getDictionary("AppDetails");
			appUUID = appDetails.get("appUUID");
			windowSession = appDetails.get("windowSession");
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
		Dialogs.alert(title, text, null);
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


}

