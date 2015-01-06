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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.qualogy.qafe.bind.ValidationException;
import com.qualogy.qafe.bind.commons.type.Parameter;
import com.qualogy.qafe.bind.core.application.ApplicationContext;
import com.qualogy.qafe.bind.io.FileLocation;
import com.qualogy.qafe.bind.resource.BindResource;
import com.qualogy.qafe.bind.resource.JavaResource;
import com.qualogy.qafe.business.resource.Resource;
import com.qualogy.qafe.business.resource.ResourceInitializationException;
import com.qualogy.qafe.core.io.jar.ExternalClassLoader;

public class JavaClass extends Resource{
	
	private Object instance = null;
	private ClassInfo info = null;
	private ClassLoader externalClassLoader = null;
	
	public JavaClass(BindResource resource) {
		super(resource);
	}
	public void init(ApplicationContext context) {
		
		FileLocation jarLocation = ((JavaResource)getBindResource()).getJarfileLocation();
		
		if(jarLocation!=null)
			externalClassLoader = ExternalClassLoader.create(jarLocation.toURI());
		
		this.instance = createInstance((JavaResource)getBindResource());
				
		this.info = ClassInfo.create(this.instance.getClass());
	}
	
	/**
	 * Create an instance of a class with the given classname, 
	 * @param string 
	 * @param clazz
	 * @throws thrown when anything goes wrong upon creation or finding the class an UnableToProcessExecption is thrown
	 * @return
	 */
	private Object createInstance(JavaResource resource){
		
		Class<?> clazz = null;
		try {
			clazz = createClass(resource.getClassName());
		} catch (ClassNotFoundException e) {
			throw new ResourceInitializationException(e);
		}
		
		return createInstance(clazz, resource);
	}
	
	private Object createInstance(Class<?> clazz, JavaResource resource) {
		Object instance = null;
		try{
//			List<Parameter> arguments = resource.getArguments();
//			if(arguments!=null){
//				Collections.sort(arguments);
//				for (Parameter argument : arguments) {
//					argument.get
//				}
//			}
			Constructor<?> constructor = clazz.getDeclaredConstructor(new Class[]{});
			constructor.setAccessible(true);
			instance = constructor.newInstance(new Object[]{});
		}catch(NoSuchMethodException e){
			throw new ResourceInitializationException("possible error; no default constructor", e);
		}catch(InvocationTargetException e){
			throw new ResourceInitializationException(e);
		}catch(IllegalAccessException e){
			throw new ResourceInitializationException(e);
		}catch(InstantiationException e){
			throw new ResourceInitializationException(e);
		}
		return instance;
	}
	private Class<?> createClass(String className) throws ClassNotFoundException {
		Class<?> clazz = null;
		
		if(externalClassLoader!=null){
			clazz = externalClassLoader.loadClass(className);
		}else{
			clazz = Class.forName(className);
		}
		return clazz;
	}
	public ClassLoader getExternalClassLoader() {
		return externalClassLoader;
	}
	public Object getInstance() {
		return instance;
	}
	
	public Method getMethod(String methodName, Class<?>[] parameterClasses) throws NoSuchMethodException{
		return info.getMethod(methodName, parameterClasses);
	}
	
	public String toLogString() {
		return ToStringBuilder.reflectionToString(instance);
	}
	public boolean isEqualTo(Resource otherResource) {
		return false;
	}
	public void destroy() {
	}
	public void validate() throws ValidationException {
		if(((JavaResource)getBindResource()).getClassName()==null)
			throw new ValidationException("classname must be set for resource type javaclass");
	}
	public void destroy(ApplicationContext context) {
	}
}
