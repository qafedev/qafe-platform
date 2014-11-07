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
package com.qualogy.qafe.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import com.qualogy.qafe.bind.core.application.ApplicationContext;
import com.qualogy.qafe.bind.core.application.Configuration;
import com.qualogy.qafe.bind.presentation.component.Window;
import com.qualogy.qafe.core.application.ApplicationCluster;
import com.qualogy.qafe.web.util.SessionContainer;

public class ContextLoaderHelper {

	/**
	 * For now only used by GWT, but any other could easily follow
	 * @param request
	 * @return
	 */
	public static boolean isReloadable(HttpServletRequest request){
		String reloadable=  request.getSession().getServletContext().getInitParameter(ContextLoader.RELOADABLE_PARAM);
		boolean returnValue=false;
		if (reloadable!=null && "true".equalsIgnoreCase(reloadable.trim())){
			returnValue=true;
		}
		return returnValue;
	}

	public static boolean isMDIMode() {
		return checkValueOfContextParameter(Configuration.MDI_MODE);
	}

	public static boolean isDebugMode() {
		return checkValueOfContextParameter(Configuration.DEVELOPMENT_MODE);
	}

	public static boolean showLog() {
		return checkValueOfContextParameter(Configuration.SHOW_LOG);
	}
	public static boolean isDockMode() {
		return checkValueOfContextParameter(Configuration.DOCKING_PANEL_ENABLED);
	}


	private static boolean checkValueOfContextParameter(String field) {
		if (ApplicationCluster.getInstance().getConfigurationItem(field) != null) {
			if (Boolean.valueOf(ApplicationCluster.getInstance().getConfigurationItem(field))) {
				return true;
			} else {
				return false;
			}
		} else {
			return true;
		}
	}

    // CHECKSTYLE.OFF: CyclomaticComplexity
	public static Boolean loadOnStartup(String windowId, ApplicationContext context, SessionContainer sessionContainer) {
		boolean loadOnStartup = false;
		if (context != null && windowId != null) {
			// default behavior from the context
			if (ApplicationCluster.getInstance().getConfigurationItem(Configuration.LOAD_ON_STARTUP) != null) {
				String absoluteWindowId = context.getId() + "." + windowId;
				if (absoluteWindowId != null) {
					String[] loadOnStartupWindows = StringUtils.split(ApplicationCluster.getInstance().getConfigurationItem(Configuration.LOAD_ON_STARTUP), ",");
					if (loadOnStartupWindows != null) {
						for (int i = 0; i < loadOnStartupWindows.length && !loadOnStartup; i++) {
							if (absoluteWindowId.equals(loadOnStartupWindows[i])) {
								loadOnStartup = true;
							}

						}
					}
				}
			}
			// behavior from the paramater
			Map<String, String> parameters = sessionContainer.getParameters();
			if (parameters != null) {
				if (parameters.containsKey(Configuration.LOAD_ON_STARTUP)) {
					String absoluteWindowId = context.getId() + "." + windowId;
					String window = parameters.get(Configuration.LOAD_ON_STARTUP);
					String[] loadOnStartupWindows = StringUtils.split(window, ",");
					if (window != null && window.length() > 0) {
						if (window.contains(".")) {
							for (int i = 0; i < loadOnStartupWindows.length && !loadOnStartup; i++) {
								if (absoluteWindowId.equals(loadOnStartupWindows[i])) {
									loadOnStartup = true;
								}
							}
						} else {
							if (context.getId().toString().equals(window)) {
								if (context.getApplicationMapping() != null && context.getApplicationMapping().getPresentationTier() != null && context.getApplicationMapping().getPresentationTier().getView() != null && context.getApplicationMapping().getPresentationTier().getView().getWindows() != null) {
									List<Window> windows = context.getApplicationMapping().getPresentationTier().getView().getWindows();
									// The first window is always the authentication window,
									// so the size must be greater than 1
									if (windows.size() > 1) {
										Window w = windows.get(1);
										if (w.getId().equals(windowId)) {
											loadOnStartup = true;
										}
									}

								}
							}

						}
					}
				}
			}
		}
		return loadOnStartup;
	}
	// CHECKSTYLE.ON: CyclomaticComplexity

	public static List<String> getLoadOnStartupWindowsFromParams(SessionContainer sessionContainer) {
		List<String> loadOnStartupWindowList = new ArrayList<String>();
		Map<String, String> parameters = sessionContainer.getParameters();
		if ((parameters != null) && parameters.containsKey(Configuration.LOAD_ON_STARTUP)) {
			String[] paramStartupWindows = StringUtils.split(parameters.get(Configuration.LOAD_ON_STARTUP), ",");
			if (paramStartupWindows != null) {
				for (int i=0; i<paramStartupWindows.length; i++) {
					String paramStartupWindow = paramStartupWindows[i];
					if (!paramStartupWindow.contains(".")) {
						ApplicationContext appContext = ApplicationCluster.getInstance().getApplicationContext(paramStartupWindow);
						if (appContext != null) {
							if ((appContext.getApplicationMapping() != null) && (appContext.getApplicationMapping().getPresentationTier() != null) && (appContext.getApplicationMapping().getPresentationTier().getView() != null) && (appContext.getApplicationMapping().getPresentationTier().getView().getWindows() != null)) {
								List<Window> windowList = appContext.getApplicationMapping().getPresentationTier().getView().getWindows();
								// The first window is always the authentication window,
								// so the size must be greater than 1
								if (windowList.size() > 1) {
									Window window = windowList.get(1);
									paramStartupWindow = paramStartupWindow + "." + window.getId();
									loadOnStartupWindowList.add(paramStartupWindow);
								}
							}
						}
					} else {
						loadOnStartupWindowList.add(paramStartupWindow);
					}
				}
			}
		}
		return loadOnStartupWindowList;
	}
}
