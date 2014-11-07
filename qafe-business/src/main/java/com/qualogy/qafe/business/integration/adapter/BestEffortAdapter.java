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
package com.qualogy.qafe.business.integration.adapter;

import java.util.Map;

import com.qualogy.qafe.bind.commons.type.TypeDefinition;

public class BestEffortAdapter{
	/**
	 * method knows toBeMapped is not an instance of any type of Collection or is an array!
	 * either creates mapping based upon typedefinition otherwise see if we have some conversion todo 
	 */
	public static Object adapt(Object toAdapt, TypeDefinition type) {
		Object adaptedResult = ObjectMapConverter.convert(toAdapt);
		if(type!=null)
			adaptedResult = convertItems(adaptedResult, type);
			
		return adaptedResult;
	}
	
	private static Object convertItems(Object objectToAdapt, TypeDefinition type){
		//TODO: convert items in map??
		//TODO: nested
		//TODO: known types conversion
		//Map result = null;
		
//		if(objectToAdapt!=null){
//			for (Iterator iter = type.getAttributes().iterator(); iter.hasNext();) {
//				TypeAttributeMapping mapping = (TypeAttributeMapping) iter.next();
//				objectToAdapt.get(mapping.getName())
//				mapping.getType()
//			}
//		}
		
		
		return objectToAdapt;
	}
}
