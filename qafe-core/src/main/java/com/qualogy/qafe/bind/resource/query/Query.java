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
package com.qualogy.qafe.bind.resource.query;

import java.io.Serializable;

/**
 * 
 * @author 
 *
 */

public abstract class Query implements Serializable{
	
	protected String id;
	
	protected String ref;
	private Query reference;//will not be set by JIBX
	
	protected Integer order;
	/**
	 * if a query contains errors, f.i. because it could not be generated
	 * this flag can be set to true
	 */
	protected boolean containsErrors;
	
	public boolean containsErrors(){
		return containsErrors;
	}
	
	public void setContainsErrors(boolean flag){
		containsErrors = flag;
	}
	
	public Integer getOrder() {
		return order;
	}
	public String getId() {
		return id;
	}
	
	/**
	 * this method should result in setting the containserrors flag 
	 */
	public abstract void validate();

	public String getRef() {
		return ref;
	}

	public Query getReference() {
		return reference;
	}

	public void setReference(Query reference) {
		this.reference = reference;
	}

	public void setId(String id) {
		this.id = id;
	}
}
