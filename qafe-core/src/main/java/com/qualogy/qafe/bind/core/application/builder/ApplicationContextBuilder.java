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
package com.qualogy.qafe.bind.core.application.builder;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import com.qualogy.qafe.bind.core.application.ApplicationContext;
import com.qualogy.qafe.bind.core.application.Configuration;
import com.qualogy.qafe.bind.domain.ApplicationMapping;

public class ApplicationContextBuilder {
	
	
	public static ApplicationContext build(ApplicationMapping mapping){
		
		ApplicationContext context = build();
		try {
			
			Field field = ApplicationContext.class.getDeclaredField("concreteApplicationMappings");
			field.setAccessible(true);
			List<ApplicationMapping> concreteApplicationMappings = new ArrayList<ApplicationMapping>();
			concreteApplicationMappings.add(mapping);
			field.set(context, concreteApplicationMappings);
		
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return context;
	}
	
	public static ApplicationContext build(Configuration config){
		ApplicationContext context = build();
		context = add(context, config);
		return context;
	}
	
	private static ApplicationContext add(ApplicationContext context, Configuration config){
		//context.setconfig;
		
		context.setDefaults();
		
		return context;
	}
	
	public static ApplicationContext build(){
		
		ApplicationContext context = null;
		try {
			
			Constructor<ApplicationContext> constructor = ApplicationContext.class.getDeclaredConstructor(new Class[]{});
			constructor.setAccessible(true);
			context = (ApplicationContext)constructor.newInstance(new Object[]{});

			context.setDefaults();
			
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return context;
	}
}
