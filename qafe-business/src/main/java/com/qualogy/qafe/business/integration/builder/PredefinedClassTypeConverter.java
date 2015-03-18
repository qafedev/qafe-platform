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
package com.qualogy.qafe.business.integration.builder;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.qualogy.qafe.bind.commons.type.TypeDefinition;

// TODO: dateconversion
/**
 * This class can build a Class for a given TypeDefinition,
 * the given def must be predefined in the SIMPLE_TYPES Map.
 */
public class PredefinedClassTypeConverter{
	
	/**
	 * map of predefined types with their
	 * class equivalent 
	 */
	public static Map<TypeDefinition, Class> SIMPLE_TYPES = new HashMap<TypeDefinition, Class>();
	static{
		SIMPLE_TYPES.put(new TypeDefinition("integer"), Integer.class);
		SIMPLE_TYPES.put(new TypeDefinition("int"), Integer.class);
		SIMPLE_TYPES.put(new TypeDefinition("long"), Long.class);
		//SIMPLE_TYPES.put(new TypeDefinition("short", Short.class);
		SIMPLE_TYPES.put(new TypeDefinition("float"), Float.class);
		SIMPLE_TYPES.put(new TypeDefinition("double"), Double.class);
		SIMPLE_TYPES.put(new TypeDefinition("character"), Character.class);
		SIMPLE_TYPES.put(new TypeDefinition("char"), Character.class);
		//SIMPLE_TYPES.put(new TypeDefinition("byte", Byte.class);
		SIMPLE_TYPES.put(new TypeDefinition("boolean"), Boolean.class);
		SIMPLE_TYPES.put(new TypeDefinition("string"), String.class);
		SIMPLE_TYPES.put(new TypeDefinition("date"), Date.class);
	}
	/**
	 * method to check wheter the given typedefinition has a predeefined Class type
	 * @param type
	 * @return
	 */
	public static boolean isPredefined(TypeDefinition type){
		return type!=null && SIMPLE_TYPES.containsKey(type);
	}
	
	/**
	 * method to check if the class given has a predefined TypeDefinition
	 * @param clazz
	 * @return
	 */
	public static boolean isPredefined(Class clazz){
		return clazz!=null && SIMPLE_TYPES.containsValue(clazz);
	}
	/**
	 * method to convert a TypeDefenition to a class definition
	 * @param type
	 * @return
	 */
	public static Class convert(TypeDefinition type){
		if(!isPredefined(type))
			throw new IllegalArgumentException("type ["+type+"] is unknown for simpletypeconverter");
		return SIMPLE_TYPES.get(type);
	}
	
}
