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
package com.qualogy.qafe.bind.util;

import java.util.Iterator;
import java.util.List;

import com.qualogy.qafe.bind.Validatable;
import com.qualogy.qafe.bind.ValidationException;

public class Validator {

	public final static String BIND_PACKAGE_PRECEDER = "com.qualogy.qafe.bind";
	
	/**
	 * method which performs validation on the given
	 * object and its members recursivly (iow the members of the members, etc.)
	 * @param object
	 * @throws ValidationException - thrown by a validatable in the given GenesisFramework
	 * @throws IllegalArgumentException when object is null
	 */
	public static void validate(Object object) throws ValidationException{
		if (object == null) 
        	throw new IllegalArgumentException("obj is null");
        
		List validatables = InterfaceScanner.scan(object, Validatable.class, BIND_PACKAGE_PRECEDER);
		
		for (Iterator iter = validatables.iterator(); iter.hasNext();) {
			Validatable v = (Validatable) iter.next();
			v.validate();
		}
	}
}
