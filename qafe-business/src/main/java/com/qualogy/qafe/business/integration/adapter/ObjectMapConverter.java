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
package com.qualogy.qafe.business.integration.adapter;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.qualogy.qafe.business.integration.builder.PredefinedClassTypeConverter;
import com.qualogy.qafe.core.application.MappingError;
import com.qualogy.qafe.core.datastore.DataMap;

/**
 * Utility class to convert a object into a Map. 
 * @author mvanderwurff
 *
 */
public class ObjectMapConverter {
	
	/**
	 * the default class up to where fields can be read from, convert will not include
	 * the up to class.
	 */
	public final static Class OBJECT_CLASS = Object.class;
	
	/**
	 * the defaults for excluding packages to read fields from when doing an
	 * intrusive field conversion (including a null package)
	 */
	public final static String[] DEFAULT_EXCLUDE_PCK_NAMES = {"java", "javax"};
	
	public static final String KEYWORD_FQN = "fqn";
	
	/**
	 * Method to convert a given object into a map. The given object
	 * cannot be of type Collection or an Array of objects. 
	 * 
	 * 1) If the given object is a Map or is not null and not predefined
	 * the object will be converted to Map and the objects within will
	 * be converted to maps as well (when not predefinedtypes and not null, nut including 
	 * Collections and Arrays).
	 * 2) In any other case the object is returned
	 *   
	 * @param object - the object to convert
	 * @throws IllegalArgumentException - when the object arg is of type Collection or an 
	 * 			array of objects
	 * @return map - the object converted to a Map
	 */
	public static Object convert(Object object){
		Object converted = object;
		if(object instanceof Map){
			converted = convertMap((Map)object, new HashSet());
		}else if(object instanceof Collection || object instanceof Object[]){
			converted =  convertObject(object,new HashSet());
		}else if(object != null && !PredefinedClassTypeConverter.isPredefined(object.getClass())){
			converted = (Map)convertObject(object, new HashSet());
		}
		return converted;
	}
	
	public static Object convert(String className, Map map){
		Object result = null;
		try {
			result = convert(Class.forName(className), map);
		} catch (ClassNotFoundException e) {
			throw new UnableToAdaptException(e);
		}
		return result;
	}
	
	private static Object convert(Class clazz, Map map){
		Object result = createServiceObj(clazz);
		for (Iterator iter = map.keySet().iterator(); iter.hasNext();) {
			String key = (String) iter.next();
			Object param = map.get(key);
			Field field;
			try {
				field = result.getClass().getDeclaredField(key);
				if(param instanceof Map){
					param = convert(field.getClass(), (Map)param);
				}else if(param instanceof List || param instanceof Object[]){
					if(param instanceof Object[])param = Arrays.asList((Object[])param);
					for (int i = 0; i<((List)param).size(); i++) {
						if(param!=null && !PredefinedClassTypeConverter.isPredefined(param.getClass()))
							throw new UnableToAdaptException("cannot adapt nested complex objects to an instance without adapter");
					}
				}
				field.setAccessible(true);
				field.set(result, param);
			} catch (NoSuchFieldException e) {
				Logger.getLogger(ObjectMapConverter.class.getName()).log(Level.WARNING, "field " + key + " does not exist on object " + clazz, e);//throw new MappingError("field " + key + " does not exist on object " + clazz, e);
			} catch (Exception e) {
				throw new UnableToAdaptException("field [" + key + "] param [" + param + "]", e);
			} 
		}
		return result;
		
	}
	
	private static Object createServiceObj(Class clazz){
		Object result = null;
		try {
			if(clazz==null)
				throw new UnableToAdaptException("Adapter states null outputclass");
			
			Constructor c = clazz.getConstructor(new Class[0]);
			c.setAccessible(true);
			result = c.newInstance(new Object[0]);
		} catch (InstantiationException e) {
			throw new MappingError("Cannot instantiate ["+clazz+"], hint: define a default constructor", e);
		} catch (Exception e) {
			throw new UnableToAdaptException(e);
		} 
		return result;
	}
	
	/**
	 * Method to convert an object to a Map. 
	 * @param object
	 * @param entries
	 * @return
	 */
	private static Object convertObject(Object object, Set entries){
		Object converted = null;
		if (object instanceof Object[]) {
			object = Arrays.asList((Object[])object);
		}
		if(object instanceof Map){
			converted = convertMap((Map)object, entries);
		} else if (object instanceof Collection){
			converted = object;
			List convertedItems = new ArrayList();
			for (Iterator iter = ((Collection)converted).iterator(); iter.hasNext();) {
				Object convertedItem = convertObject((Object) iter.next(), entries);
				if (convertedItem != null) {
					convertedItems.add(convertedItem);
				}
			}
			converted = convertedItems;
		} else if (object != null){
			converted = getFieldsFromObject(new DataMap(), entries, object.getClass(), object);
		}
		return converted;
	}
	
	/**
	 * Method gets all the fields from an object including the private and 
	 * protected ones and returns them as a Map, where the key will be the field name
	 * and the value the value of the field. If a field is a complex object (not predefined)
	 * as well, it will be converted as well.
	 * 
	 * Note: Ignores fields from java.lang
	 */
	private static Map<String, Object> getFieldsFromObject(Map<String, Object> converted, Set<Object> entries, Class clazz, Object object){
		if(OBJECT_CLASS.equals(object.getClass()))
			return converted;
		
		if(clazz.getSuperclass() != null && clazz.getSuperclass() != OBJECT_CLASS) {
			converted = getFieldsFromObject(converted, entries, clazz.getSuperclass(), object);
		}
		
		Field[] fields = clazz.getDeclaredFields();
		AccessibleObject.setAccessible(fields, true);
		for (int i = 0; i < fields.length; i++) {
			Object value = null;
			try {
				//Warning: Field.get(Object) creates wrappers objects for primitive types.
				value = fields[i].get(object);
			} catch (IllegalArgumentException e) {
				//leave
			} catch (IllegalAccessException e) {
				//this can't happen. Would get a Security exception instead throw a runtime exception in case the impossible happens.
                throw new InternalError("Unexpected IllegalAccessException: " + e.getMessage());
			}
			String uniqueEntryIdentifier = fields[i].getDeclaringClass().getName() + ":" + fields[i].getName();
			if(entries.add(uniqueEntryIdentifier)){// ???? not already contains, to prevent from statics to be included more than once, raising a stackoverflow
				// 	QAFEPLATFORM-88 - java.util collections were also excluded previously.- now added check for Collection
				if ((value instanceof Collection) || ((value != null) && !PredefinedClassTypeConverter.isPredefined(value.getClass()) && !hasExcludedPackage(value.getClass()))) {
					value = convertObject(value, entries);
				} else if (value instanceof Map) {
					value = convertMap((Map)value, entries);
				}
			}
			converted.put(fields[i].getName(), value);
		}
		converted.put(KEYWORD_FQN, clazz.getName());
		return converted;
	}
	
	private static boolean hasExcludedPackage(Class clazz){
		boolean isExcluded = clazz.getPackage()==null;
		
		for (int i = 0; !isExcluded && i < DEFAULT_EXCLUDE_PCK_NAMES.length; i++) {
			isExcluded = clazz.getPackage().getName().startsWith(DEFAULT_EXCLUDE_PCK_NAMES[i]);
		}
		return isExcluded;
	}
	
	/**
	 * Method to convert nested objects within a Map.
	 * TODO: necessary?
	 * @param object
	 * @return
	 */	
	private static Map<String, Object> convertMap(Map object, Set entries){
		Set keys = object.keySet();
		for (Iterator iter = keys.iterator(); iter.hasNext();) {
			Object tempKey = iter.next();
			if (tempKey != null){
				String key = tempKey.toString();
				Object value = object.get(key);
				if(value != null && !PredefinedClassTypeConverter.isPredefined(value.getClass()) && !hasExcludedPackage(value.getClass())){
					value = convertObject(value, entries);
				}
				object.put(key, value);
			}
		}
		return object;
	}
}