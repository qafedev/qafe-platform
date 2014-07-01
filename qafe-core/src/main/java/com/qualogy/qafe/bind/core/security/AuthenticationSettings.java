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

public class AuthenticationSettings implements Serializable{
	
	private static final long serialVersionUID = 514514312192559216L;

	protected AuthenticationModule module;
	
	protected RequiredOn requiredOn; 
	
	public AuthenticationModule getModule() {
		return module;
	}

	public RequiredOn getRequiredOn() {
		return requiredOn;
	}
	
	public void merge(AuthenticationSettings otherAuthenticationSettings){
		if(otherAuthenticationSettings!=null){
			if(this.module!=null && otherAuthenticationSettings.module!=null){
				throw new IllegalArgumentException("cannot have more than one authentication module configured, please check your configuration");
			}
			if(this.module==null)
				this.module = otherAuthenticationSettings.module;
			
			if(this.requiredOn==null){
				this.requiredOn = otherAuthenticationSettings.requiredOn;
			}else{
				this.requiredOn.merge(otherAuthenticationSettings.requiredOn);
			}
		}
	}
	
	public AuthenticationSettings clone() {
		AuthenticationSettings newAuthenticationSettings = new AuthenticationSettings();
		if (module != null) {
			newAuthenticationSettings.module = module.clone();
		}
		if (requiredOn != null) {
			newAuthenticationSettings.requiredOn = requiredOn.clone();
		}
		return newAuthenticationSettings;
	}
}
