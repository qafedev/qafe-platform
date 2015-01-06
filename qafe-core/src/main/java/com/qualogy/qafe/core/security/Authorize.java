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

import java.util.List;

import com.qualogy.qafe.bind.presentation.event.function.BuiltInFunction;
import com.qualogy.qafe.core.application.UserInfo;

public class Authorize extends BuiltInFunction {
	
	private static final long serialVersionUID = 1L;
	
	private UserInfo userInfo;	
	private List<Restriction> restrictions;
	private String resourceAppId;
	private String resourceId;

	public Authorize(String userUID, String resourceAppId, String resourceId) {
		this.userInfo = SecurityInfo.getInstance().getUserInfo(userUID);
		this.resourceAppId = resourceAppId;
		this.resourceId = resourceId;
	}
	
	public UserInfo getUserInfo() {
		return userInfo;
	}
	
	public Boolean hasToOpenWindows() {
		if (getUserInfo() != null) {
			return getUserInfo().hasToOpenWindows(resourceAppId, resourceId);
		}
		return false;
	}
	
	public List<String> getToOpenWindows() {
		if (getUserInfo() != null) {
			return getUserInfo().getToOpenWindows(resourceAppId, resourceId);
		}
		return null;
	}
	
	public Boolean hasRestrictions() {
		return hasRestrictions(null, null);
	}
	
	public Boolean hasRestrictions(String applicationId, String windowId) {
		if (getUserInfo() != null) {
			Role userRole = getUserInfo().getUserRole(applicationId, windowId);
			restrictions = SecurityInfo.getInstance().getRestrictions(userRole, applicationId, windowId);
			return ((restrictions != null) && (restrictions.size() > 0));
		}
		return false;
	}
	
	public List<Restriction> getRestrictions() {
		return restrictions;
	}
}
