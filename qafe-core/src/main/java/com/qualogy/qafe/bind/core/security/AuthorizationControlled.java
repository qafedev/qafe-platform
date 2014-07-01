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


/**
 * Interface for objects that are controlled through authorisationframework.
 * If an object is AuthorisationControlled it should specify if a user of what the 
 * object respresents should have authorisation over that object and if the role
 * set a user has applies.
 * @author 
 */
public interface AuthorizationControlled {
	/**
	 * method to get the authorizationrule that is controlling this object
	 * @return
	 */
	public AuthorizationRule getControllingAuthorizationRule();
	
	/**
	 * method to check if this object is controlled by authorizationrules
	 * @return
	 */
	public boolean isAuthorizationControlled();
	
	public void setControllingAuthorizationRule(AuthorizationRule rule);
	
	/**
	 * return the id as an object, the toString of the object will be used
	 * @return
	 */
	public Object getId();
}
