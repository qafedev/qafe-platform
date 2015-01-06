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

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import com.qualogy.qafe.bind.commons.type.TypeDefinition;
import com.qualogy.qafe.business.integration.builder.BooleanBuilder;
import com.qualogy.qafe.business.integration.builder.CharacterBuilder;
import com.qualogy.qafe.business.integration.builder.DoubleBuilder;
import com.qualogy.qafe.business.integration.builder.IntegerBuilder;
import com.qualogy.qafe.business.integration.builder.LongBuilder;
import com.qualogy.qafe.business.integration.builder.PredefinedClassTypeConverter;
import com.qualogy.qafe.business.integration.builder.StringBuilder;
import com.qualogy.qafe.business.integration.builder.UnImplementedBuilderException;
/**
 * //TODO: dateconversion
 * @author mvanderwurff
 *
 */
public class PredefinedAdapterFactory {
	
	/**
	 * allows null values for type
	 * @param type
	 * @return
	 */
	private static boolean canCreate(TypeDefinition type){
		return PredefinedClassTypeConverter.isPredefined(type);
	}
	
	private static Map<Class, Class> converters = new HashMap<Class, Class>();
	static{
		converters.put(Integer.class, IntegerBuilder.class);
		converters.put(Long.class, LongBuilder.class);
		//converters.put(Short.class, ShortAdapter.class);
		//converters.put(Float.class, FloatAdapter.class);
		converters.put(Double.class, DoubleBuilder.class);
		converters.put(Character.class, CharacterBuilder.class);
		//converters.put(Byte.class, ByteAdapter.class);
		converters.put(Boolean.class, BooleanBuilder.class);
		converters.put(String.class, StringBuilder.class);
		//converters.put(Date.class, DateAdapter.class);
	}
	
	public static PredefinedAdapter create(TypeDefinition type){
		Class toClass = null;
		if(canCreate(type)){
			toClass = PredefinedClassTypeConverter.convert(type);
		}else{
			throw new UnImplementedBuilderException("className " + ((type!=null)?type:null) + " not implemented");
		}
		return create(toClass);
	}
			
	public static PredefinedAdapter create(Class toClass){
		 
		if(!converters.containsKey(toClass)){
			throw new UnImplementedBuilderException("Class " + toClass + " not implemented");
		}
		
		PredefinedAdapter converter = null;
		try{
			
			converter = (PredefinedAdapter)((Class)converters.get(toClass)).getConstructor(new Class[]{}).newInstance(new Object[]{});
			
		}catch(InvocationTargetException e){
			throw new UnImplementedBuilderException("Class " + toClass + " not implemented", e);
		}catch (SecurityException e) {
			throw new UnImplementedBuilderException("Class " + toClass + " not implemented", e);
		} catch (InstantiationException e) {
			throw new UnImplementedBuilderException("Class " + toClass + " not implemented", e);
		} catch (IllegalAccessException e) {
			throw new UnImplementedBuilderException("Class " + toClass + " not implemented", e);
		} catch (NoSuchMethodException e) {
			throw new UnImplementedBuilderException("Class " + toClass + " not implemented", e);
		}
		
		return converter;
	}
	
	/**
	 * method to check wheter the given typedefinition has a predeefined type
	 * and the given object is instance of one of the predefined types, so an
	 * actual adapter can convert the object. 
	 * @param type
	 * @return
	 */
	public static boolean canObjectBeConverted(TypeDefinition type, Object objectToConvert){
		return PredefinedClassTypeConverter.isPredefined(type) && objectToConvert!=null && PredefinedClassTypeConverter.isPredefined(objectToConvert.getClass());
	}
	
	public static boolean mustConvert(TypeDefinition type, Object o){
		boolean mustConvert = false;
		if(type!=null && type.getId()!=null && o!=null){
			String name = null;
			if(PredefinedClassTypeConverter.isPredefined(type))
				name = PredefinedClassTypeConverter.convert(type).getName();
			else
				name = type.getId();
			
			mustConvert = !o.getClass().getName().equals(name);
		}
		return  mustConvert;
	}
}
