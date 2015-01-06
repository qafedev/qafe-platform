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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.qualogy.qafe.core.application.UserInfo;

public class SecurityInfo {
	
	public static final String METHOD_AUTHENTICATE			= "authenticate";
	public static final String METHOD_GET_RESTRICTIONS		= "getRestrictions";
	
	private static final String DELIMITER = ".";
	
	private static SecurityInfo instance = null;	
		
	private Map<String,UserInfo> users;
	private Map<String,Map> roles;

	public static SecurityInfo getInstance(){
		if (instance == null) {
			instance = new SecurityInfo();
		}
		return instance;
	}
	
	private SecurityInfo() {
		users = new HashMap<String,UserInfo>();
		roles = new HashMap<String,Map>();
	}
	
	public void addUserInfo(UserInfo userInfo) {
		users.put(userInfo.getUserUID(), userInfo);
	}
	
	public UserInfo getUserInfo(String userUID) {
		if (users.containsKey(userUID)) {
			return users.get(userUID);
		}
		return null;
	}
	
	public void removeUserInfo(String userUID) {
		if (users.containsKey(userUID)) {
			users.remove(userUID);
		}
	}
	
	public void addRestriction(Restriction restriction) {
		if (restriction != null) {
			String roleNameOrder = restriction.getRoleName() + DELIMITER + restriction.getRoleOrder();
			if (!roles.containsKey(roleNameOrder)) {
				Map<String, Map> applications = new HashMap<String, Map>();
				roles.put(roleNameOrder, applications);
			}
			
			Map<String, Map> applications = roles.get(roleNameOrder);
			if (!applications.containsKey(restriction.getApplicationId())) {
				Map<String, Map> windows = new HashMap<String, Map>();
				applications.put(restriction.getApplicationId(), windows);
			}

			Map<String, Map> windows = applications.get(restriction.getApplicationId());
			if (!windows.containsKey(restriction.getWindowId())) {
				Map<String, String> components = new HashMap<String, String>();
				windows.put(restriction.getWindowId(), components);
			}
			
			Map<String, String> components = windows.get(restriction.getWindowId());
			if (restriction.getComponentId() != null) {
				components.put(restriction.getComponentId(), restriction.getNoAccess());
			} else {
				components.put("null", restriction.getNoAccess());
			}
		}
	}
	
	public void addRestrictions(List<Restriction> restrictionList) {
		if (restrictionList != null) {
			for (int i=0; i<restrictionList.size(); i++) {
				addRestriction(restrictionList.get(i));
			}	
		}
	}
	
	public List<Restriction> getRestrictions(Role role) {
		return getRestrictions(role, null, null);
	}
	
	public List<Restriction> getRestrictions(Role role, String applicationId, String windowId) {
		List<Restriction> restrictionList = null;
		if (role != null) {
			String roleNameOrder = role.getRoleName() + DELIMITER + role.getRoleOrder();		
			if (roles.containsKey(roleNameOrder)) {
				restrictionList = new ArrayList<Restriction>();
				Map<String, Map> applications = roles.get(roleNameOrder);
				Iterator<String> itr = applications.keySet().iterator();
				while (itr.hasNext()) {
					String appId = itr.next();
					if ((applicationId == null) || appId.equals(applicationId)) {
						Map<String, Map> windows = applications.get(appId);
						Iterator<String> itr2 = windows.keySet().iterator();
						while (itr2.hasNext()) {
							String winId = itr2.next();
							if ((windowId == null) || winId.equals(windowId)) {
								Map<String, String> components = windows.get(winId);
								Iterator<String> itr3 = components.keySet().iterator();
								while (itr3.hasNext()) {
									String compId = itr3.next();
									String noAccess = components.get(compId);
									if (((windowId == null) && compId.equals("null")) || ((windowId != null) && !compId.equals("null"))) {
										if (compId.equals("null")) {
											compId = null;
										}
										RestrictionRule restriction = new RestrictionRule();
										restriction.setRoleName(role.getRoleName());
										restriction.setRoleOrder(role.getRoleOrder());
										restriction.setApplicationId(appId);
										restriction.setWindowId(winId);
										restriction.setComponentId(compId);
										restriction.setNoAccess(noAccess);
										restrictionList.add(restriction);	
									}
								}
							}	
						}
					}
				}
			}
		}	
		return restrictionList;
	}	
}
