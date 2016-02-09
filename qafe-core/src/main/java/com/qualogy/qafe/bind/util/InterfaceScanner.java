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

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Utility class to scan an object and all its members
 * for a specified interface.
 * @author 
 *
 */
public class InterfaceScanner {

	/**
	 * @see #scan(Object, Class, String)
	 * @pre Assuming object to be not null and having fields to scan.
	 * @param theObject - Object to scan
	 * @param anInterface - 
	 * @return objects - the objects that implement the given interface
	 */
	public static List scan(Object theObject, Class anInterface){
		return scan(theObject, anInterface, null, null);					
	}
	
	/**
	 * @see #scan(Object, Class, String)
	 * @pre Assuming object to be not null and having fields to scan.
	 * @param theObject - Object to scan
	 * @param anInterface - 
	 * @return objects - the objects that implement the given interface
	 */
	public static List scan(Object theObject, Class anInterface, Class[] excludeClasses){
		return scan(theObject, anInterface, null, excludeClasses);					
	}
	
	/**
	 * This method scans the given object and its members recursivly 
	 * (iow the members of the members, etc.)for the given interface 
	 * returning the object references to the members (and/or the object
	 * itself) implementing this interface.
	 * Note: the scan is bound to the package and subpackages of the given object
	 * to scan.
	 * @pre Assuming object to be not null and having fields to scan.
	 * @param theObject - Object to scan
	 * @param anInterface - 
	 * @return objects - the objects that implement the given interface
	 */
	public static List scan(Object theObject, Class anInterface, String packageBoundary){
		return scan(theObject, anInterface, packageBoundary, null);							
	}
	
	public static List scan(Object theObject, Class anInterface, String packageBoundary, Class[] excludeClasses){
		if(theObject==null)
			throw new IllegalArgumentException("theObject cannot be null");
		if(anInterface==null)
			throw new IllegalArgumentException("anInterface cannot be null");
		
		if(packageBoundary==null)
			packageBoundary = theObject.getClass().getPackage().getName();
		
		Set excluding = null; 
		if(excludeClasses!=null)
			excluding = new HashSet<Class>(Arrays.asList(excludeClasses));
		
		return scan(new ArrayList(), new HashSet<Object>(), theObject, anInterface, packageBoundary, excluding);					
	}
	
	/**
	 * for general use see #scan(Object, Class, String)
	 * 
	 * additionally this method has a list and map param. the list keeps track
	 * of objects that implemented the wanted interface. the map keeps track of processed 
	 * members, because of recursive members that point to themselves.
	 * @param objects
	 * @param processed
	 * @param theObject
	 * @param anInterface
	 * @param packageBoundary
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private static List scan(List objects, Set<Object> processed, Object theObject, Class anInterface, String packageBoundary, Set excludeClasses){
		if(theObject instanceof List){
			for (Iterator iter = ((List)theObject).iterator(); iter.hasNext();) {
				objects = scan(objects, processed, iter.next(), anInterface, packageBoundary, excludeClasses);
			}
		}
		
		if(hasCorrectPackage(theObject, packageBoundary)){
			if(isWantedImplementor(theObject, anInterface, excludeClasses)){
				objects.add(theObject);
			}
			
			if(processed.contains(theObject))
				return objects;
			processed.add(theObject);
			
			List fields = getFields(theObject.getClass(), new ArrayList());
			for (Iterator iter = fields.iterator(); iter.hasNext();) {
				Field field = (Field) iter.next();
				field.setAccessible(true);
				Object result = null;
				try {
					result = field.get(theObject);
				} catch (IllegalArgumentException e) {
					throw new UnableToScanException(e);
				} catch (IllegalAccessException e) {
					throw new UnableToScanException(e);
				}
				if(result!=null && result.getClass().getDeclaredFields().length>0)
					objects = scan(objects, processed, result, anInterface, packageBoundary, excludeClasses);
			}
		}
		return objects;
	}
	
	/**
	 * @pre theObject is not null
	 * @param theObject
	 * @param anInterface
	 * @param excludeClasses
	 * @return
	 */
	private static boolean isWantedImplementor(Object theObject, Class anInterface, Set excludeClasses) {
		boolean wanted = false;
		if(anInterface.isInstance(theObject)){
			wanted = (excludeClasses==null) || (excludeClasses!=null && !excludeClasses.contains(theObject.getClass()));
		}
		return wanted;
	}

	/**
	 * check if this class origins in the BIND_PACKAGE_PRECEDER package
	 * @param object
	 * @return
	 */
	private static boolean hasCorrectPackage(Object object, String packageBoundary){
		
		if (object!=null  && object.getClass().getPackage()!=null){
			return object!=null && object.getClass().getPackage().getName().startsWith(packageBoundary); 
			
		} else {
			return false;
		}
		
	}
	
	/**
	 * get all declared fields, also from superclass
	 * @param clas
	 * @param fields
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private static List getFields(Class clas, List fields){
		
		if(clas.getSuperclass()!=null && !clas.getSuperclass().equals(Object.class)){
			getFields(clas.getSuperclass(), fields);
		}
		
		List somemore = Arrays.asList(clas.getDeclaredFields());
		fields.addAll(somemore);
		return fields;
	}
	
}
