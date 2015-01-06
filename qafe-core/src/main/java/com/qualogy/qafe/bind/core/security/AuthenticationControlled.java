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
package com.qualogy.qafe.bind.core.security;
/**
 * Interface for objects that are controlled through authenticationframework.
 * If an object is AuthenticationControlled it should specify if authentication is required
 * @author 
 *
 */
public interface AuthenticationControlled {
	
	/**
	 * method to get a boolean indicating if authentication is required over this object
	 */
	public boolean isAuthenticationRequired();
	
	/**
	 * method to set authenticationrequired to true
	 */
	public void setAuthenticationRequired();
	
	public void setAuthenticationModule(AuthenticationModule module);
	
	public AuthenticationModule getAuthenticationModule();
}
