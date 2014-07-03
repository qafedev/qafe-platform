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
package com.qualogy.qafe.core.application;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.qualogy.qafe.core.id.UniqueIdentifier;
import com.qualogy.qafe.core.security.Restriction;
import com.qualogy.qafe.core.security.Role;


public class UserInfo {
	
	private static final String DELIMITER = ".";
	
	private Boolean authenticated = false;
	private String userUID = null;
	private Map<String, Map> roles = new HashMap<String, Map>();
	private Map<String, List<String>> openWindows = new HashMap<String, List<String>>();	
	private Map<String, Boolean> usedAuthenticationResources = new HashMap<String, Boolean>();
	private Map<String, Boolean> loggedOutApps = new HashMap<String, Boolean>();
	
	public UserInfo() {
		this(null);
	}
	
	public UserInfo(String uuid) {
		if (StringUtils.isEmpty(uuid)) {
			uuid = UniqueIdentifier.nextSeed().toString();
		}
		this.userUID = uuid;
	}
	
	public String getUserUID() {
		return userUID;
	}
	
	public void addLoggedOutApp(String appId) {
		if (appId != null) {
			loggedOutApps.put(appId, true);	
		}
	}
	
	public void removeLoggedOutApp(String appId) {
		if (loggedOutApps.get(appId) != null) {
			loggedOutApps.remove(appId);	
		}
	}
	
	public Boolean isLoggedOutApp(String appId) {
		return (loggedOutApps.get(appId) != null);
	}
	
	public Boolean isAuthenticated() {
		return authenticated;
	}

	public void setAuthenticated() {
		authenticated =  true;
	}
	
	public Boolean isUsedAuthenticationResource(String resourceAppId, String resourceId) {
		if ((resourceAppId != null) && (resourceId != null)) {
			String id = concatWithDelimiter(resourceAppId, resourceId);
			return usedAuthenticationResources.containsKey(id);
		}
		return false;
	}
	
	public void setUsedAuthenticationResource(String resourceAppId, String resourceId) {
		if ((resourceAppId != null) && (resourceId != null)) {
			String id = concatWithDelimiter(resourceAppId, resourceId);
			usedAuthenticationResources.put(id, true);
		}
	}
	
	private String concatWithDelimiter(String str1, String str2) {
		return str1 + DELIMITER + str2;
	}
	
	public Boolean hasToOpenWindows(String resourceAppId, String resourceId) {
		String resourceKey = concatWithDelimiter(resourceAppId, resourceId);
		List<String> list = openWindows.get(resourceKey);
		if ((list != null) && (list.size() > 0)) {
			return true;
		}
		return false;
	}
	
	public void addToOpenWindow(String resourceAppId, String resourceId, String applicationId, String windowId) {
		String resourceKey = concatWithDelimiter(resourceAppId, resourceId);
		List<String> windowList = openWindows.get(resourceKey);
		if (windowList == null) {
			windowList = new ArrayList<String>();
			openWindows.put(resourceKey, windowList);
		}
		windowList.add(windowId);
	}
	
	public List<String> getToOpenWindows(String resourceAppId, String resourceId) {	
		String resourceKey = concatWithDelimiter(resourceAppId, resourceId);
		List<String> windowList = openWindows.get(resourceKey);
		if (windowList != null) {
			List<String> copyWindowList = new ArrayList<String>();
			for (int i=0; i<windowList.size(); i++) {
				copyWindowList.add(windowList.get(i));
			}
			windowList.clear();
			return copyWindowList;
		}
		return null;
	}
	
	public void addRestriction(Restriction restriction) {
		if (restriction != null) {
			String roleNameOrder = concatWithDelimiter(restriction.getRoleName(), restriction.getRoleOrder().toString());
			if (!roles.containsKey(roleNameOrder)) {
				Map<String, Map> applications = new HashMap<String, Map>();
				roles.put(roleNameOrder, applications);
			}
			
			Map<String, Map> applications = roles.get(roleNameOrder);
			if (!applications.containsKey(restriction.getApplicationId())) {
				Map<String, Map> windows = new HashMap<String, Map>();
				applications.put(restriction.getApplicationId(), windows);
			}

			Map<String, String> windows = applications.get(restriction.getApplicationId());
			windows.put(restriction.getWindowId(), null);
		}
	}
	
	public Role getUserRole(String applicationId, String windowId) {
		Role userRole = null;
		Iterator<String> itr = roles.keySet().iterator();
		while (itr.hasNext()) {
			String roleNameOrder = itr.next();
			String roleName = roleNameOrder.substring(0, roleNameOrder.indexOf(DELIMITER));
			Integer roleOrder = new Integer(roleNameOrder.substring(roleNameOrder.indexOf(DELIMITER) + DELIMITER.length()));
			Map<String, Map> applications = roles.get(roleNameOrder);
			Iterator<String> itr2 = applications.keySet().iterator();
			while (itr2.hasNext()) {
				String appId = itr2.next();
				if ((applicationId == null) || appId.equals(applicationId)) {
					Map<String, Map> windows = applications.get(appId);
					Iterator<String> itr3 = windows.keySet().iterator();
					while (itr3.hasNext()) {
						String winId = itr3.next();
						if ((windowId == null) || winId.equals(windowId)) {
							if ((userRole == null) || (userRole.getRoleOrder() > roleOrder)) {
								if (userRole == null) {
									userRole = new Role();
								}
								userRole.setRoleName(roleName);
								userRole.setRoleOrder(roleOrder);
							}
						}
					}
				}
			}	
		}
		return userRole;
	}
}
