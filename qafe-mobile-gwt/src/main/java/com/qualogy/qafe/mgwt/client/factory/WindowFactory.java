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
package com.qualogy.qafe.mgwt.client.factory;


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
//import com.qualogy.qafe.mgwt.client.component.QRootPanel;
//import com.qualogy.qafe.mgwt.client.component.QWindowPanel;
import com.qualogy.qafe.mgwt.client.context.ClientApplicationContext;
//import com.qualogy.qafe.mgwt.client.ui.GWTUIGenerator;
import com.qualogy.qafe.mgwt.client.ui.renderer.RendererHelper;
import com.qualogy.qafe.mgwt.client.ui.renderer.events.EventFactory;
import com.qualogy.qafe.mgwt.client.vo.ui.MenuItemGVO;
import com.qualogy.qafe.mgwt.client.vo.ui.MenuItemSeparatorGVO;
import com.qualogy.qafe.mgwt.client.vo.ui.QAFEKeywordsGVO;
import com.qualogy.qafe.mgwt.client.vo.ui.UIGVO;
import com.qualogy.qafe.mgwt.client.vo.ui.WindowGVO;
import com.qualogy.qafe.mgwt.client.vo.ui.event.EventListenerGVO;

public class WindowFactory {

	private static final String LOG_TITLE = "Execution log window";
	private static String currentWindowTitle;

	private WindowFactory() {
	};

	
	public static void createWindow(UIGVO ui, String windowId) {

	}


	

	public static void setWidgetToMainPanel(Widget w, WindowGVO windowGVO) {
		
	}

	public static void clearWidgetFromMainPanel() {

	
	}

	private static WindowGVO getWindow(UIGVO ui, String windowId) {

		return null;
	}

	private static String getWindowTitle(UIGVO ui, String windowId) {
		
		return null;
	}

	public static Widget createWindow(UIGVO ui) {
		return null;
	}

	

	

	public static void createMenu(UIGVO systemMenuApp) {
	

	}

	private static void processMenu(MenuItemGVO root, MenuBar menuBar, final String uuid) {
	
	} 
	
}
