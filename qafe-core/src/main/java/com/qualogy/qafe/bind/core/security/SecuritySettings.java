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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.qualogy.qafe.bind.io.FileLocation;

public class SecuritySettings implements Serializable{
	
	public static final long serialVersionUID = 1295018813311074154L;
	
	public final static String AUTHENTICATE_WINDOW = "$_authenticate_window";
	public final static String SECURITY_SERVICE = "$_SecurityService";
	public final static String AUTHENTICATE_METHOD = "authenticate";
	public final static String GET_RESTRICTIONS_METHOD = "getRestrictions";
	public final static String METHOD_IN_USERNAME = "username";
	public final static String METHOD_IN_PASSWORD = "password";
	
	protected List<AuthorizationRule> authorizationRules;
	protected AuthenticationSettings authenticationSettings;
	
	protected List<FileLocation> settingsFileLocations;
	
	public AuthenticationSettings getAuthenticationSettings() {
		return authenticationSettings;
	}

	public List<AuthorizationRule> getAuthorizationRules() {
		return authorizationRules;
	}
	
	public void add(AuthorizationRule rule){
		if(rule==null)
			throw new IllegalArgumentException("cannot add null to authorizationRules");
		
		if(authorizationRules==null)
			authorizationRules = new ArrayList<AuthorizationRule>();
		
		authorizationRules.add(rule);
	}

	public List<FileLocation> getSettingsFileLocations() {
		return settingsFileLocations;
	}
	
	public void merge(SecuritySettings otherSecuritySettings){
		if(this.authorizationRules==null){
			this.authorizationRules = otherSecuritySettings.authorizationRules;
		}else if(CollectionUtils.isNotEmpty(otherSecuritySettings.authorizationRules)){
			this.authorizationRules.addAll(otherSecuritySettings.authorizationRules);
		}
		
		if(this.authenticationSettings==null){
			this.authenticationSettings = otherSecuritySettings.authenticationSettings;
		}else{
			this.authenticationSettings.merge(otherSecuritySettings.authenticationSettings);	
		}
	}
	
	public SecuritySettings clone() {
		SecuritySettings newSecuritySettings = new SecuritySettings();
		if (authorizationRules != null) {
			newSecuritySettings.authorizationRules = new ArrayList<AuthorizationRule>();
			newSecuritySettings.authorizationRules.addAll(authorizationRules);	
		}
		if (settingsFileLocations != null) {
			newSecuritySettings.settingsFileLocations = new ArrayList<FileLocation>();
			newSecuritySettings.settingsFileLocations.addAll(settingsFileLocations);	
		}
		if (authenticationSettings != null) {
			newSecuritySettings.authenticationSettings = authenticationSettings.clone();
		}
		return newSecuritySettings;
	}
}
