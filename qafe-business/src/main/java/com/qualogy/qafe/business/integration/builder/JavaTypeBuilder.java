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
package com.qualogy.qafe.business.integration.builder;

import com.qualogy.qafe.business.integration.adapter.PredefinedAdapter;

public abstract class JavaTypeBuilder implements PredefinedAdapter{

	public abstract Class getToConvertToClass();
	
	public abstract Class getPrimitiveClass();
	
	/**
	 * @see PredefinedAdapter.convert(Object)
	 * uses the toString method of these objects since they all return the value as a String
	 * if object is null, null is returned
	 */
	public final Object convert(Object objectToConvert) {
		
		Object wanted = objectToConvert;
		if(objectToConvert!=null && !getToConvertToClass().isInstance(objectToConvert) && !objectToConvert.equals("")){
			try{
				wanted = getToConvertToClass().getConstructor(new Class[]{String.class}).newInstance(new Object[]{objectToConvert.toString()});
			}catch(Exception e){
				throw new UnableToBuildException(e);
			}
		}
		return wanted;
	}
	
}
