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
package com.qualogy.qafe.core.security;

import com.qualogy.qafe.bind.business.action.BusinessAction;
import com.qualogy.qafe.bind.core.application.ApplicationContext;
import com.qualogy.qafe.bind.core.security.AuthorizationControlled;
import com.qualogy.qafe.bind.presentation.component.Component;
import com.qualogy.qafe.core.datastore.DataIdentifier;
import com.qualogy.qafe.core.datastore.DataStore;
import com.qualogy.qafe.core.errorhandling.ExternalException;

public class SecurityManager {

	/**
	 * Method to authenticate a user.
	 * @param context
	 * @param dataId
	 * @param action
	 * @return
	 * @throws ExternalException
	 */
	public static UserIdentifier authenticate(ApplicationContext context, DataIdentifier dataId, BusinessAction action) throws ExternalException{
		context.getBusinessManager().manage(context, dataId, action);
		
		Roles roles = (Roles)DataStore.getValue(dataId, "");
		
		UserIdentifier userid = SecurityVault.create(context, roles);
		
		return userid;
	}
	
	/**
	 * Method returns style info on negative decision (f.i. 'visibility:hidden;')and null on positive, when
	 * given object is instanceof AuthorizationControlled. When object is no AuthorizationControlled null
	 * is returned.
	 * @param userId
	 * @param context
	 * @param objectToDecideOver
	 * @return String style info, null when negative
	 */
	public static String decide(UserIdentifier userId, ApplicationContext context, Component objectToDecideOver){
		boolean decision = AccessDecisionManager.decide(userId, context, objectToDecideOver);
		
		String style = null;
		if(!decision && objectToDecideOver instanceof AuthorizationControlled){
			if(((AuthorizationControlled)objectToDecideOver).getControllingAuthorizationRule()!=null)
				style = ((AuthorizationControlled)objectToDecideOver).getControllingAuthorizationRule().getWhenNoAccess();
		}
		return style;
	}
	
	/**
	 * Method returns boolean indicating pass or no pass for this user on this businessaction. True indicating
	 * pass, false no pass
	 * @param userId
	 * @param context
	 * @param objectToDecideOver
	 * @return boolean
	 */
	public static boolean decide(UserIdentifier userId, ApplicationContext context, BusinessAction objectToDecideOver){
		return AccessDecisionManager.decide(userId, context, objectToDecideOver);
	}
}
