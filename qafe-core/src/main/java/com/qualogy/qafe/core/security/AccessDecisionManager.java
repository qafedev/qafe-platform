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

import org.apache.commons.lang.ArrayUtils;

import com.qualogy.qafe.bind.core.application.ApplicationContext;
import com.qualogy.qafe.bind.core.application.ApplicationIdentifier;
import com.qualogy.qafe.bind.core.security.AuthenticationControlled;
import com.qualogy.qafe.bind.core.security.AuthorizationControlled;
import com.qualogy.qafe.bind.core.security.AuthorizationRule;

public class AccessDecisionManager {
	
	/**
	 * 
	 * @param object - to decide over
	 * @param id
	 * @return
	 * @throws UserNotAuthenticatedException
	 */
	public static boolean decide(final UserIdentifier userId, final ApplicationContext context, final Object object) throws UserNotAuthenticatedException{
		
		if(object instanceof AuthenticationControlled){
			hasAuthenticated(userId, context.getId(), (AuthenticationControlled)object);
		}
		
		boolean decision = true;
		if(object instanceof AuthorizationControlled){
			decision = isAuthorized(userId, (AuthorizationControlled)object);
		}
				
		return decision;
	}

	
	
	private static boolean isAuthorized(UserIdentifier userId, AuthorizationControlled controlledObj) {
		boolean decision = false;
		
		Boolean cachedDecision = SecurityVault.getAuthorityDecisionFromCache(userId, controlledObj);
		if(cachedDecision!=null){
			decision = cachedDecision.booleanValue();
		}else{
			Roles authorities = SecurityVault.getAuthorities(userId);
			if(authorities==null)
				throw new UserNotAuthenticatedException("user with [" + userId + "] not found in security vault");
			
			AuthorizationRule authorizationRule = controlledObj.getControllingAuthorizationRule();
			
			String[] roles = authorizationRule.getAuthorizedRoles();
			if(!ArrayUtils.isEmpty(roles)){
				decision = isUserInRole(roles, authorities.get(), authorizationRule.getAccess());
			}
			SecurityVault.addDecision(userId, decision, controlledObj);
		}
		return decision;
	}



	private static void hasAuthenticated(UserIdentifier userId, ApplicationIdentifier appid, AuthenticationControlled controlled) {
		if(controlled.isAuthenticationRequired()){
			boolean authenticated = SecurityVault.hasAuthenticatedFor(userId, appid);
			if(!authenticated)
				throw new UserNotAuthenticatedException("User with [" + userId + "] not found in security vault");
		}
		
	}



	/**
	 * @param roles
	 * @param strings
	 * @return
	 */
	private static Boolean isUserInRole(String[] roles, String[] authorities, String access) {
		Boolean authorized = false;
		
		int match = 0;
		for (int i = 0; i < roles.length; i++) {
			for (int j = 0; j < authorities.length; j++) {
				if(roles[i].equals(authorities[j])){
					if(match==1 && AuthorizationRule.ACCESS_RULE_IF_ANY_GRANTED.equals(access)){
						authorized = true;
						break;
					}
					match++;	
					break;
				}
			}
		}
		if(!AuthorizationRule.ACCESS_RULE_IF_ANY_GRANTED.equals(access)){
			if(access.equals(AuthorizationRule.ACCESS_RULE_IF_ALL_GRANTED) && match == roles.length)
				authorized = true;
			else if(access.equals(AuthorizationRule.ACCESS_RULE_IF_NONE_GRANTED) && match == 0)
				authorized = true;
		}
		return authorized;
	}
	
}
