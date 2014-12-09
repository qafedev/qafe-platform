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
package com.qualogy.qafe.bind.core.security;

import java.util.Iterator;
import java.util.List;

import com.qualogy.qafe.bind.core.application.ApplicationContext;
import com.qualogy.qafe.bind.core.application.ApplicationStack;
import com.qualogy.qafe.bind.core.pattern.ApplicationIdPattern;
import com.qualogy.qafe.bind.core.pattern.IdPattern;
import com.qualogy.qafe.bind.core.pattern.WindowIdPattern;
import com.qualogy.qafe.bind.presentation.component.Window;

public class AuthenticationPostProcessHelper {


	/**
	 * This method validates the authentication settings. When valid
	 * this method sets the authenticationRequired setting on all applications
	 * and windows. after that it copies the authentication module when necessary.
	 *
	 */
	// CHECKSTYLE.OFF: CyclomaticComplexity
	public static void postProcess(ApplicationStack stack){

		if(stack==null)
			throw new IllegalArgumentException("stack cannot be null");

		if(stack.getSecuritySettings()!=null && stack.getSecuritySettings().getAuthenticationSettings()!=null){
			AuthenticationSettings settings = stack.getSecuritySettings().getAuthenticationSettings();
			List<IdPattern> patterns = settings.getRequiredOn().getIdPatterns();
			for (Iterator iter = patterns.iterator(); iter.hasNext();) {
				ApplicationIdPattern pattern = (ApplicationIdPattern) iter.next();
				for (Iterator iterator = stack.getApplicationsIterator(); iterator.hasNext();) {
					ApplicationContext context = (ApplicationContext) iterator.next();
					if(pattern.matches(context.getId().stringValueOf())){
//						if(context.getSecuritySettings()!=null && context.getSecuritySettings().getAuthenticationSettings()!=null){
//							//throw new BindException("Both <applications> and the <application> itself have authentication configured (application ["+context.getId().stringValueOf()+"])");
//							continue;
//						}
//						context.setSecuritySettings(stack.getSecuritySettings());
//						context.setAuthenticationRequired();
//						context.setAuthenticationModule(settings.getModule());
//						settings.getModule().add(context.getId());
						SecuritySettings contextSecuritySettings = null;
						if (context.getSecuritySettings() == null) {
							// Security on application-cluster level,
							// so copy the SecuritySettings to each application
							contextSecuritySettings = stack.getSecuritySettings().clone();
							context.setSecuritySettings(contextSecuritySettings);
							context.setAuthenticationRequired();
							context.setAuthenticationModule(contextSecuritySettings.getAuthenticationSettings().getModule());
							settings.getModule().add(context.getId());
						} else {
							// Security on application level,
							// so set the applicationRef for later use
							contextSecuritySettings = context.getSecuritySettings();
							contextSecuritySettings.getAuthenticationSettings().getModule().setApplicationRef(context.getId().toString());
						}
						String windowRef = contextSecuritySettings.getAuthenticationSettings().getModule().getWindowRef();
						if ((windowRef == null) || (windowRef.length() == 0)){
							contextSecuritySettings.getAuthenticationSettings().getModule().setWindowRef(SecuritySettings.AUTHENTICATE_WINDOW);
						}
					}
				}
			}
		}

		//check per app
		for (Iterator iterator = stack.getApplicationsIterator(); iterator.hasNext();) {
			ApplicationContext context = (ApplicationContext) iterator.next();
			if (context.getAuthenticationModule() == null){//no authentication set
				postProcess(context);
			} else {
				if ((context.getApplicationMapping() != null) && (context.getApplicationMapping().getPresentationTier() != null) && (context.getApplicationMapping().getPresentationTier().getView() != null)) {
					List<Window> windowList = context.getApplicationMapping().getPresentationTier().getView().getWindows();
					if (windowList != null) {
						for (int i=0; i<windowList.size(); i++) {
							postProcess(context, windowList.get(i));
						}
					}
				}
			}
		}
	}
    // CHECKSTYLE.ON: CyclomaticComplexity

	private static void postProcess(ApplicationContext context){

		if(context.getSecuritySettings()!=null && context.getSecuritySettings().getAuthenticationSettings()!=null){
			AuthenticationSettings settings = context.getSecuritySettings().getAuthenticationSettings();
			List<IdPattern> patterns = settings.getRequiredOn().getIdPatterns();
			for (Iterator iter = patterns.iterator(); iter.hasNext();) {
				WindowIdPattern pattern = (WindowIdPattern) iter.next();
				if(context.getApplicationMapping()==null || context.getApplicationMapping().getPresentationTier()==null || context.getApplicationMapping().getPresentationTier().getView()==null || context.getApplicationMapping().getPresentationTier().getView().getWindows()==null)
					throw new IllegalArgumentException("no windows defined, but authentication over windows is defined");
				List<Window> windows = context.getApplicationMapping().getPresentationTier().getView().getWindows();
				for (Iterator iterator2 = windows.iterator(); iterator2.hasNext();) {
					Window window = (Window) iterator2.next();
					if(pattern.matches(window.getId())){
//						window.setAuthenticationRequired();
//						window.setAuthenticationModule(settings.getModule());
//						settings.getModule().add(context.getId());
						postProcess(context, window);
					}
				}
			}
		}
	}

	private static void postProcess(ApplicationContext context, Window window) {
		if ((context.getSecuritySettings() != null) && (context.getSecuritySettings().getAuthenticationSettings() != null)) {
			AuthenticationSettings settings = context.getSecuritySettings().getAuthenticationSettings();
			window.setAuthenticationRequired();
			window.setAuthenticationModule(settings.getModule());
			settings.getModule().add(context.getId());
		}
	}
}
