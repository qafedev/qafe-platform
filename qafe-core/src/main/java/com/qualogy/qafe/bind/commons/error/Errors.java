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
package com.qualogy.qafe.bind.commons.error;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Errors implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2478297044353799977L;
	protected List<ServiceError> errors;
	
	/**
     * method to add a Error to a Error list. The list will be
     * created when null 
     * @param action
     * @throws IllegalArgumentException - when object param passed is null
     */
    public void add(ServiceError error) {
    	if(error==null)
    		throw new IllegalArgumentException("error cannot be null");
    	if(errors==null)
    		errors = new ArrayList<ServiceError>();
    	
    	errors.add(error);
    }

    public void addAll(Errors otherErrors){
    	if(errors==null)
    		errors = new ArrayList<ServiceError>();
    		
    	errors.addAll(otherErrors.errors);
    	
    }
}
