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
package com.qualogy.qafe.gwt.client;

/**
 *  $Id: QAFEGWTWeb.java 4152 2009-12-09 11:54:24Z rjankie $
 *	$URL$
 */

import java.util.Map;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.StyleInjector;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.qualogy.qafe.gwt.client.component.QMenuBar;
import com.qualogy.qafe.gwt.client.context.ClientApplicationContext;
import com.qualogy.qafe.gwt.client.css.Resources;
import com.qualogy.qafe.gwt.client.factory.MainFactoryActions;
import com.qualogy.qafe.gwt.client.service.RPCServiceAsync;
import com.qualogy.qafe.gwt.client.util.JSNIUtil;
import com.qualogy.qafe.gwt.client.vo.data.InitState;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class QAFEGWTWeb implements EntryPoint {
	
	public static final int MAIN_MENU_HEIGHT = 24;
	public static final String WINDOW_CLOSING_MESSAGE = "Do you really want to leave the Qafe application page?";
	private static MenuBar menuBar = null;
	
	private static String rootPanelValue = "";

	@Override
	public void onModuleLoad() {
		JSNIUtil.exportQafeFunctions();
		
		rootPanelValue = JSNIUtil.getScriptParameter(GWT.getModuleName(), JSNIUtil.ROOT_PANEL_PARAM);
		
		// inject default qafe-gwt.css
		Resources.INSTANCE.css().ensureInjected(); // This is same as StyleInjector.inject(Resources.INSTANCE.css().getText());
		// now inject the custom css.
		injectGeneratedCSS();
		
		buildUI();		
		Window.addResizeHandler(new ResizeHandler() {
			@Override
			public void onResize(ResizeEvent event) {
				moveWidgets();
			}
		});

		Window.addWindowClosingHandler(new Window.ClosingHandler() {
			@Override
			public void onWindowClosing(Window.ClosingEvent closingEvent) {
				closingEvent.setMessage(WINDOW_CLOSING_MESSAGE);
			}
		});

		// For reloading of the CSS
		Element styleElement = DOM.createElement("style");
		ClientApplicationContext.getInstance().setStyleElement(styleElement);
		
		// workaround for GWT issue 1813
		// http://code.google.com/p/google-web-toolkit/issues/detail?id=1813
		RootPanel.get(rootPanelValue).getElement().getStyle().setProperty("position", "relative");
		
		// set uncaught exception handler
		GWT.setUncaughtExceptionHandler(new GWT.UncaughtExceptionHandler() {

			@Override
			public void onUncaughtException(Throwable throwable) {
				Throwable tempThrowable = throwable;
				StringBuffer text = new StringBuffer("Uncaught exception: " +throwable.getMessage());
				while (tempThrowable != null) {
					StackTraceElement[] stackTraceElements = tempThrowable.getStackTrace();
					text.append(tempThrowable.toString() + "\n");
					for (StackTraceElement element : stackTraceElements) {
						text.append( "    at " + element + "\n");
					}
					tempThrowable = tempThrowable.getCause();
					if (tempThrowable != null) {
						text.append( "Caused by: " + tempThrowable.getCause());
					}
				}
				ClientApplicationContext.getInstance().log("Uncaught exception: "+throwable.getMessage(), text.toString(), true, true, throwable);
			}
		});
	}

	protected void moveWidgets() {
		try {
			DOM.setStyleAttribute(ClientApplicationContext.getInstance().getPi().getElement(), "left", "" + (Window.getClientWidth() - ClientApplicationContext.getInstance().getPi().getWidth()));
			if (ClientApplicationContext.getInstance().isMDI()){
				if (ClientApplicationContext.getInstance().getDockMode().booleanValue()){
					int dockWidth= ClientApplicationContext.getInstance().getDockPanel().getOffsetWidth();
					DOM.setStyleAttribute( ClientApplicationContext.getInstance().getDockPanel().getElement(), "left",  Window.getClientWidth()/2 - (dockWidth/2)+"");
					DOM.setStyleAttribute( ClientApplicationContext.getInstance().getDockPanel().getElement(), "top", "" + (Window.getClientHeight() - 80));
				} else {
					DOM.setStyleAttribute(ClientApplicationContext.getInstance().getBottomMenuBar().getElement(), "left", "" + 0);
					DOM.setStyleAttribute(ClientApplicationContext.getInstance().getBottomMenuBar().getElement(), "top", "" + (Window.getClientHeight() - 25));
					DOM.setStyleAttribute(ClientApplicationContext.getInstance().getHorizontalPanel().getElement(), "left", "" + 0);
					DOM.setStyleAttribute(ClientApplicationContext.getInstance().getHorizontalPanel().getElement(), "top", "" + (Window.getClientHeight() - 23));
				}
			}else {
				SimplePanel mainPanel = ClientApplicationContext.getInstance().getMainPanel();
				mainPanel.setWidth(Window.getClientWidth() + "px");
				mainPanel.setHeight(Window.getClientHeight() + "px");
			}
		} catch (Exception e) {}
	}

	private void buildUI() {
		RPCServiceAsync service = MainFactoryActions.createService();
		AsyncCallback<?> callback = createMDICallback();
		Map<String, String> parameters = ClientApplicationContext.getInstance().getParameters();
		service.getMDIState(parameters, callback);
	}

	private static AsyncCallback<?> createMDICallback() {
		return new AsyncCallback<Object>() {

			@Override
			public void onFailure(Throwable caught) {
			    ClientApplicationContext.getInstance().log("Setting up SDI/MDI mode  failed", caught.getMessage(), true, true, caught);				

			}

			@Override
			public void onSuccess(Object result) {
				if (result != null && result instanceof InitState) {
					InitState initState = (InitState)result;
					if (initState.getMdiMode().booleanValue()){
						setupMDI(initState.getDockMode());
					} else {
						setupSDI();
					}
				}else {
					ClientApplicationContext.getInstance().log("Something went wrong when requesting information from the server");
					setupMDI(Boolean.FALSE);
				}
			}
		};
	}

	protected static void setupMDI(Boolean dockMode) {
		Window.enableScrolling(false);
		ClientApplicationContext.getInstance().setMode(ClientApplicationContext.MDI);
		ClientApplicationContext.getInstance().setDockMode(dockMode);
		RootPanel.get(rootPanelValue).add(buildMenu(), 0, 0);
	
		MainFactoryActions.processUIFromApplicationContext();
		ClientApplicationContext.getInstance().setLogText("MDI Mode");
		if (dockMode.booleanValue()){
			 Panel dockPanel = new HorizontalPanel();
			 ClientApplicationContext.getInstance().setDockPanel(dockPanel);
			 RootPanel.get(rootPanelValue).add(dockPanel,Window.getClientWidth()/2,Window.getClientHeight()-80);
		} else {
			RootPanel.get(rootPanelValue).add(ClientApplicationContext.getInstance().getHorizontalPanel(), 0, Window.getClientHeight() - 23);
			ClientApplicationContext.getInstance().getBottomMenuBar().setWidth("100%");
			ClientApplicationContext.getInstance().getBottomMenuBar().addItem(" ", new Command() {
				@Override
				public void execute() {
				}
			});
			RootPanel.get(rootPanelValue).add(ClientApplicationContext.getInstance().getBottomMenuBar(), 0, Window.getClientHeight() - 25);
		}
		AbsolutePanel image = new AbsolutePanel();
		image.addStyleName("imglogo");
		RootPanel.get(rootPanelValue).add(image);
		RootPanel.get(rootPanelValue).add(ClientApplicationContext.getInstance().getPi(), Window.getClientWidth()-ClientApplicationContext.getInstance().getPi().getWidth(), 3);
	}

	protected static void setupSDI() {
		ClientApplicationContext.getInstance().setMode(ClientApplicationContext.SDI);
		// This will remove the horizontal scrollbar
		RootPanel.get(rootPanelValue).getElement().getStyle().setProperty("overflow", "hidden");
		
		RootPanel.get(rootPanelValue).add(buildMenu(),0,0);
		MainFactoryActions.processUIFromApplicationContext();
		ClientApplicationContext.getInstance().setLogText("SDI Mode");
		RootPanel.get(rootPanelValue).add(ClientApplicationContext.getInstance().getPi(), Window.getClientWidth()-ClientApplicationContext.getInstance().getPi().getWidth(), 3);
	}

	private static MenuBar buildMenu() {
		menuBar = new QMenuBar();
		menuBar.setAnimationEnabled(true);
		menuBar.setAutoOpen(true);
		menuBar.setWidth("100%");
		ClientApplicationContext.getInstance().setMainMenu(menuBar);
		return menuBar;
	}
	
	private void injectGeneratedCSS() {
		AsyncCallback<?> callback = createGenerateTypedCSSCallBack();
		RPCServiceAsync service = MainFactoryActions.createService();
		service.generateTypedCSS("gwt", null, callback);
	}
	
	private static AsyncCallback<?> createGenerateTypedCSSCallBack() {
		return new AsyncCallback<Object>() {
			@Override
			public void onFailure(Throwable caught) {
			    ClientApplicationContext.getInstance().log("Getting generated CSS Failed", caught.getMessage(), true, true, caught);				

			}
			@Override
			public void onSuccess(Object result) {
				if (result != null) {
					String generatedCss = (String) result;
					StyleInjector.injectAtEnd(generatedCss);
				}
			}
		};
	}
}
