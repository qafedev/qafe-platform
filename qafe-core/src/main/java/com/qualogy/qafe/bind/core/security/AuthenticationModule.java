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
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.qualogy.qafe.bind.core.application.ApplicationIdentifier;

public class AuthenticationModule implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -9039384715315212544L;

	/**
	 * reference string to the authentication window 
	 */
	protected String windowRef;
	
	/**
	 * reference string to the authentication resource
	 */
	protected String resourceRef;
	
	protected String applicationRef;
	
	private Set<ApplicationIdentifier> ids = null;
	
	public Set<ApplicationIdentifier> getIds() {
		return ids;
	}
	public String getResourceRef() {
		return resourceRef;
	}
	public String getWindowRef() {
		return windowRef;
	}
	public void setWindowRef(String windowRef) {
		this.windowRef = windowRef;
	}
	public String getApplicationRef() {
		return applicationRef;
	}
	public void setApplicationRef(String applicationRef) {
		this.applicationRef = applicationRef;
	}
	public void add(ApplicationIdentifier id) {
		if(ids==null)
			ids = new HashSet<ApplicationIdentifier>();
		ids.add(id);
	}
	public AuthenticationModule clone() {
		AuthenticationModule newAuthenticationModule = new AuthenticationModule();
		if (applicationRef != null) {
			newAuthenticationModule.applicationRef = new String(applicationRef);	
		}
		if (windowRef != null) {
			newAuthenticationModule.windowRef = new String(windowRef);	
		}
		if (resourceRef != null) {
			newAuthenticationModule.resourceRef = new String(resourceRef);	
		} 
		if (ids != null) {
			Iterator<ApplicationIdentifier> itr = ids.iterator();
			while (itr.hasNext()) {
				ApplicationIdentifier appIdentifier = itr.next();
				newAuthenticationModule.add(appIdentifier);
			}
		}
		return newAuthenticationModule;
	}
}
