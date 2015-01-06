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

public class RestrictionRule extends Rule implements Restriction {
	
	private Role role = new Role();
	private String applicationId;
	private String windowId;
	private String componentId;
	
	public String getRoleName() {
		return role.getRoleName();
	}
	public void setRoleName(String roleName) {
		role.setRoleName(roleName);
	}
	
	public Integer getRoleOrder() {
		return role.getRoleOrder();
	}
	public void setRoleOrder(Integer roleOrder) {
		role.setRoleOrder(roleOrder);
	}
	
	public String getApplicationId() {
		return applicationId;
	}
	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}
	
	public String getWindowId() {
		return windowId;
	}
	public void setWindowId(String windowId) {
		this.windowId = windowId;
	}
	
	public String getComponentId() {
		return componentId;
	}
	public void setComponentId(String componentId) {
		this.componentId = componentId;
	}
	
	public RestrictionRule clone() {
		RestrictionRule restrictionRule = new RestrictionRule();
		restrictionRule.setRoleName(getRoleName());
		restrictionRule.setRoleOrder(getRoleOrder());
		restrictionRule.setApplicationId(getApplicationId());
		restrictionRule.setWindowId(getWindowId());
		restrictionRule.setComponentId(getComponentId());
		restrictionRule.setNoAccess(getNoAccess());
		return restrictionRule;
	}
}
