/**
 * Copyright 2008-2016 Qualogy Solutions B.V.
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
package com.qualogy.qafe.bind.resource;

import com.qualogy.qafe.bind.domain.BindBase;
/**
 * Holder for a bundle of resourceproperties. A bundle of resourceproperties is needed
 * to initialize the actual resource.
 * 
 * @author 
 *
 */
public abstract class BindResource extends BindBase{
	
	public final static String PLACEHOLDER_$_SECURITY_RESOURCE = "$_REPLACE_THIS_SECURITY_RESOURCE";
	
//	public final static String PLACEHOLDER_$_AUTHENTICATION_RESOURCE = "$_REPLACE_THIS_AUTHENTICATION_RESOURCE";
//	public final static String PLACEHOLDER_$_AUTHORISATION_RESOURCE = "$_REPLACE_THIS_AUTHORISATION_RESOURCE";
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2688467715362650905L;

	/**
	 * userDefinedId is a holder for a user defined id
	 */
	protected String id;
	
	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}
}
