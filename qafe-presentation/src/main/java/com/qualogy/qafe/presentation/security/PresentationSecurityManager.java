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
package com.qualogy.qafe.presentation.security;

import com.qualogy.qafe.bind.core.application.ApplicationContext;
import com.qualogy.qafe.bind.core.security.AuthenticationControlled;
import com.qualogy.qafe.bind.core.security.AuthenticationModule;
import com.qualogy.qafe.bind.core.security.AuthorizationControlled;
import com.qualogy.qafe.bind.presentation.component.Component;
import com.qualogy.qafe.core.datastore.ApplicationLocalStore;
import com.qualogy.qafe.core.security.SecurityManager;
import com.qualogy.qafe.core.security.UserIdentifier;


/**
 * This should be a generic class somewhere in presentation
 *
 */
public class PresentationSecurityManager {


	/**
	 * this method should be performed before every application and window load  
	 * @param o
	 */
	public static void checkAuthentication(ApplicationContext context, String uuid, AuthenticationControlled controlledObject){
		if(controlledObject.isAuthenticationRequired()){
			if(!ApplicationLocalStore.getInstance().contains(uuid, ApplicationLocalStore.KEY_USER_ID)){
				//show authentication window
				AuthenticationModule module = (controlledObject).getAuthenticationModule();
				//show this window
				String windowId = module.getWindowRef();
				
				//call the action below from the authenticate event, in the eventhandler(!)
			//TODO	UserIdentifier userId = SecurityManager.authenticate(context, dataId, action);
				
				//and store the retrieved userId in the localstore
				//TODO ApplicationLocalStore.getInstance().store(uuid, ApplicationLocalStore.KEY_USER_ID, userId);
				
				//security manager takes care of storing the userprofile
			}
		}
	}
	
	/**
	 * Method checks user against authorization. If authorized or no authorization has been specified no extra style should
	 * be applied and null is returned. If user is not authorized style will be returned and should be applied to the user  
	 * @param context
	 * @param userId
	 * @param component
	 * @return String style
	 */
	public String checkAuthorisation(ApplicationContext context, UserIdentifier userId, Component component){
		String style = null;
		if(((AuthorizationControlled)component).isAuthorizationControlled()){
			style = SecurityManager.decide(userId, context, component);
		}
		return style;
	}

}
