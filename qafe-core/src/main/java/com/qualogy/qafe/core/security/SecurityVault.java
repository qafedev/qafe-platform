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
package com.qualogy.qafe.core.security;

import java.util.HashMap;
import java.util.Map;

import com.qualogy.qafe.bind.core.application.ApplicationContext;
import com.qualogy.qafe.bind.core.application.ApplicationIdentifier;
import com.qualogy.qafe.bind.core.security.AuthorizationControlled;

public class SecurityVault {

	private static Map<UserIdentifier, UserContext> vault = new HashMap<UserIdentifier, UserContext>();
	
	/**
	 * use this method to create the user context on first authentication
	 * @param appAuthenticatedOn
	 * @param authorities
	 * @return
	 */
	public static UserIdentifier create(ApplicationContext context, Roles roles) {
		UserIdentifier userId = new UserIdentifier();
		vault.put(userId, new UserContext());
		
		addAuthentication(userId, context, roles);
		
		return userId;
	}
	/**
	 * if a user has logged in to one or more other apps already on the applicationcluster use this method.
	 * @param id
	 * @param appAuthenticatedOn
	 * @param authorities
	 * @return
	 */
	public static void addAuthentication(UserIdentifier userId, ApplicationContext context, Roles roles) {
		
		UserContext userContext = getUserContext(userId);
		
		userContext.addApplicationsAuthenticatedFor(context.getAuthenticationModule().getIds());
	
		userContext.addRoles(roles);
		
		vault.put(userId, userContext);
	}
	
	public static Roles getAuthorities(UserIdentifier userId) {
		Roles authorities = null;
		
		UserContext data = getUserContext(userId);
		if(data!=null)
			authorities = data.getAuthorities();
		
		return authorities;
	}
	
	public static UserContext getUserContext(UserIdentifier userId) {
		return vault.get(userId);
	}
	
	public static void addDecision(UserIdentifier userId, Boolean decision, AuthorizationControlled controlledObj) {
		UserContext data = vault.get(userId);
		if(data==null)
			throw new IllegalArgumentException("data cannot be null for userid["+userId+"] while storing cache");
		
		data.addDecision(controlledObj, decision);
		
		vault.put(userId, data);
	}
	
	public static UserContext remove(UserIdentifier id){
		return vault.remove(id);
	}
	
	public static Boolean getAuthorityDecisionFromCache(UserIdentifier userId, AuthorizationControlled controlledObj){
		Boolean desicion = null;
		UserContext data = vault.get(userId);
		if(data!=null)
			desicion = data.isAuthorizedFor(controlledObj);
		return desicion;
	}
	
	public static boolean hasAuthenticatedFor(UserIdentifier userId, ApplicationIdentifier appId){
		UserContext data = vault.get(userId);
		return (data!=null && data.isAuthenticatedFor(appId));
	}
}
