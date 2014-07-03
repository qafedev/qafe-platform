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

import com.qualogy.qafe.bind.business.action.BusinessAction;
import com.qualogy.qafe.bind.core.application.ApplicationContext;
import com.qualogy.qafe.bind.core.application.ApplicationStack;
import com.qualogy.qafe.bind.presentation.component.Component;
import com.qualogy.qafe.bind.presentation.component.Window;
import com.qualogy.qafe.bind.util.InterfaceScanner;

public class AuthenticationOverAuthorizationPostProcessorHelper {

	/**
	 * when authorization is set, all modules that have authorization
	 * enabled should also have authentication enabled
	 * @param stack
	 */
	@SuppressWarnings("unchecked")
	public static void postProcess(ApplicationStack stack) {
		for (Iterator iter = stack.getApplicationsIterator(); iter.hasNext();) {
			ApplicationContext context = (ApplicationContext) iter.next();
			if(!context.isAuthenticationRequired()) {//not set
				if(context.isAuthorizationControlled()){//authorization over context
					context.setAuthenticationRequired();
				}else{
					setAuthentication(context);
				}
			}
			
			setAuthenticationOnWindows(context);
		}
	}
	
	/**
	 * This method loops over all windows in given context and checks if authenticationrequired is set. If not set
	 * this method will determine authentication required or not based on authorization over the window or one of its components.
	 * This check is necessary since windows can be used cross application.
	 * 
	 * Method checks when all windows have authentication required set to true, the authenticationrequired
	 * check on the applicationcontext which the window belongs to will be set to true
 	 *
	 * @param context
	 */
	private static void setAuthenticationOnWindows(ApplicationContext context) {
		int windowAuthenticationCounter = 0;
		
		List<Window> windows = context.getApplicationMapping().getPresentationTier().getView().getWindows();
		for (Iterator windowsIter = windows.iterator(); windowsIter.hasNext();) {
			Window window = (Window) windowsIter.next();
			if(window.isAuthenticationRequired()){
				windowAuthenticationCounter++;
			}else{//not set
				if(window.isAuthorizationControlled() || hasAuthorizationOverComponents(window)){
					window.setAuthenticationRequired();
					windowAuthenticationCounter++;
				}
			}
		}
		
		if(windowAuthenticationCounter == windows.size()){
			context.setAuthenticationRequired();
		}
		
	}

	/**
	 * Method loops over businessactions, when one businessaction has authorization configured, authentication
	 * is set to required on the applicationcontext. 
	 * 
	 * @param context
	 */
	private static void setAuthentication(ApplicationContext context) {
		
		if(context.getApplicationMapping().getBusinessTier()!=null && context.getApplicationMapping().getBusinessTier().getBusinessActions()!=null){
			List<BusinessAction> actions = context.getApplicationMapping().getBusinessTier().getBusinessActions();
			for (Iterator iter = actions.iterator(); iter.hasNext();) {
				BusinessAction action = (BusinessAction) iter.next();
				if(action.isAuthorizationControlled()){
					context.setAuthenticationRequired();
					break;
				}
			}
		}
	}

	/**
	 * Method check if at least one of the windows components has an authorizationrule set.
	 * if one is found true is returned (otherwise false).  
	 * @param window
	 * @return boolean 
	 */
	@SuppressWarnings("unchecked")
	private static boolean hasAuthorizationOverComponents(Window window) {
		boolean hasAuthorization = false;
		
		List<Component> components = InterfaceScanner.scan(window, Component.class);
		for (Iterator iter = components.iterator(); iter.hasNext();) {
			Component component = (Component) iter.next();
			if(component.isAuthorizationControlled()){
				hasAuthorization = true;
				break;
			}
		}
		return hasAuthorization;		
	}
}
