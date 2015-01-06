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
package com.qualogy.qafe.business.integration.adapter;

public class AdaptedToService {
	private Class<?> clazz;
	
	
	private Object value;
	
	/**
	 * boolean is set to false if this adapted object has no value,
	 * no value means the requested object was never set onto the datastore 
	 * and has no default value 
	 */
	private boolean hasValue;
	
	

	private AdaptedToService(){
		//no impl
	}
	
	public static AdaptedToService create(String className){
		if(className==null)
			throw new IllegalArgumentException("cannot create with null className");
		
		Class<?> clazz = null;
		try {
			clazz = Class.forName(className);
		} catch (ClassNotFoundException e) {
			throw new UnableToAdaptException(e);
		}
		
		
		AdaptedToService adapted = new AdaptedToService();
		adapted.clazz = clazz;
		adapted.value = null;
		adapted.hasValue = false;
		return adapted;
	}
	
	public static AdaptedToService create(Object value){
		if(value==null)
			throw new IllegalArgumentException("AdaptedToService: cannot create with null value");
		
		AdaptedToService adapted = new AdaptedToService();
		adapted.value = value;
		adapted.clazz = value.getClass();
		adapted.hasValue = true;
		return adapted;
	}
	
	public boolean hasValue() {
		return hasValue;
	}
	
	public Object getValue() {
		return value;
	}
	public Class<?> getClazz() {
		return clazz;
	}

    @Override
    public String toString() {
        return "AdaptedToService{" +
                "value=" + value +
                ", hasValue=" + hasValue +
                '}';
    }
}
