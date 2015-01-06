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
package com.qualogy.qafe.business.resource.java;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.qualogy.qafe.business.integration.adapter.PredefinedAdapterFactory;
import com.qualogy.qafe.business.integration.builder.JavaTypeBuilder;

public class ClassInfo {
	
	/**
	 * methods holds [key][values] :: [methodName+parameterLength][list of methods fitting that description]
	 */
	private Map<String, List<Method>> methods;
	
	private ClassInfo(){
		methods = new HashMap<String, List<Method>>();
	}
	
	public static ClassInfo create(Class<?> clazz){
		ClassInfo info = new ClassInfo();
		info.gatherMethods(clazz);
		return info;
	}
	
	private void gatherMethods(Class<?> clazz){
		Method[] classmethods = clazz.getMethods();
		for (int i = 0; i < classmethods.length; i++) {
			String methodName = classmethods[i].getName();
			methodName += classmethods[i].getParameterTypes().length;
			
			List<Method> methodsWithSameName = (List<Method>)methods.get(methodName);
			if (methodsWithSameName==null) {
				methodsWithSameName = new ArrayList<Method>();
			}
			
			methodsWithSameName.add(classmethods[i]);
			methods.put(methodName, methodsWithSameName);
		}
	}
	
	public Method getMethod(String methodName, Class<?>[] parameterClasses) throws NoSuchMethodException{
		if(methodName==null)
			throw new IllegalArgumentException("methodname cannot be null");
		if(parameterClasses==null)
			throw new IllegalArgumentException("parameterObjectClassPairs cannot be null");
		
		String key = methodName + parameterClasses.length;
		
		if(!methods.containsKey(key))
			throw new NoSuchMethodException("Method " + methodName + " does not exist with " + parameterClasses.length + " args");
		
		//narrow to find the correct method
		List<Method> themethods = new ArrayList<Method>((List<Method>)methods.get(methodName + parameterClasses.length));
		for (Iterator<Method> iter = themethods.iterator(); iter.hasNext();) {
			Method method = (Method) iter.next();
			Class<?>[] types = method.getParameterTypes();
			for (int i = 0; i < types.length; i++) {
				if(types[i].isPrimitive() && parameterClasses[i]!=null){
					Class<?> pClass = ((JavaTypeBuilder)PredefinedAdapterFactory.create(parameterClasses[i])).getPrimitiveClass();
					if(pClass!=types[i]){//not the same primitive equivalent
						iter.remove();
						break;
					}
				}else if(parameterClasses[i]!=null && !types[i].isAssignableFrom(parameterClasses[i])){
					iter.remove();//not the same class when not null
					break;
				}
			}
		}
		if(themethods.size()>1){
			throw new NoSuchMethodException("Cannot determine which of the overloaded methods with name " + methodName + " (and " + parameterClasses.length + " args) to use");
		}

		if(themethods.size()==0){
			String pClasses = "";
			for (int i = 0; i < parameterClasses.length; i++) {
				if(i!=0)
					pClasses += ", ";
				pClasses += parameterClasses[i];
			}
			throw new NoSuchMethodException("No method found for name " + methodName + " (and " + parameterClasses.length + " args ("+pClasses+")) to use");
		}

		
		return (Method)themethods.get(0);
	}
}
