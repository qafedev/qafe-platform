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
package com.qualogy.qafe.business.resource;

import com.qualogy.qafe.bind.ValidationException;
import com.qualogy.qafe.bind.core.application.ApplicationContext;
import com.qualogy.qafe.bind.resource.BindResource;

/**
 * Superclass for any resource possible in the resourcetier.
 * This resource holds the resourceproperties for init and destroy purposes
 * and initializes and destorys function information.
 * @author mvanderwurff
 *
 */
public abstract class Resource {
	
	private BindResource bindResource;
	
	/**
	 * no args constructor made private
	 */
	private Resource(){
		super();
	}
	
	public Resource(BindResource bindResource){
		this();
		if(bindResource==null)
			throw new IllegalArgumentException("cannot instantiatie on null properties");
		
		this.bindResource = bindResource;
	}
	
	/**
	 * same as id from supplied properties
	 * @return
	 */
	public final String getName(){
		return bindResource.getId();
	}
	
	/** 
	 * method for initializing the resource
	 */ 
	public abstract void init(ApplicationContext context);
	
	/** 
	 * method for destroying the resource
	 */ 
	public abstract void destroy(ApplicationContext context);
	
	public abstract String toLogString();
	
	public abstract boolean isEqualTo(Resource otherResource);
	
	public abstract void validate() throws ValidationException;

	protected BindResource get() {
		return bindResource;
	}

	protected BindResource getBindResource() {
		return bindResource;
	}

	protected void setBindResource(BindResource bindResource) {
		this.bindResource = bindResource;
	}
}
