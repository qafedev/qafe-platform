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

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang.StringUtils;

import com.qualogy.qafe.bind.commons.type.AdapterAttribute;
import com.qualogy.qafe.bind.commons.type.AdapterMapping;
import com.qualogy.qafe.bind.commons.type.AttributeMapping;
import com.qualogy.qafe.bind.commons.type.In;
import com.qualogy.qafe.business.integration.builder.PredefinedClassTypeConverter;
import com.qualogy.qafe.core.datastore.DataIdentifier;
import com.qualogy.qafe.core.datastore.DataMap;
import com.qualogy.qafe.core.datastore.DataStore;
/**
 * Adapter for adapting an object (map or servicedefined outcome) to a complex type, defined within the type-definition tag.
 * 
 * @author mvanderwurff
 */
public class MappingAdapter{
	
	private static final Logger logger = Logger.getLogger(MappingAdapter.class.getName());

	//TODO: check if correct method is used
	//TODO: implement adapt all
	//TODO: deal with conversion inside the adapters ==> string to int
	/**
	 * method knows toBeMapped is not an instance of any type of Collection or is an array!
	 * @param toBeMapped
	 * @param mapping
	 * @param result
	 * @return
	 */
	protected static Object adaptFromService(Object toBeMapped, AdapterMapping mapping) {
		Object result = null;
		//TODO: check 3 nested parents...since object is null every time
		//its cheaper to convert all than doing a nested get on each and every attribute of the mapping
		toBeMapped = ObjectMapConverter.convert(toBeMapped);
		
		if(mapping.hasParent()){
			result = adaptFromService(toBeMapped, mapping.getParent());
		}
		
		List<AdapterAttribute> attributes = mapping.getAdapterAttributes();
		if(attributes!=null){
			if(result==null){
//				result = new CaseInsensitiveMap();
				result = new DataMap();
			}
			
			for (Iterator<AdapterAttribute> iter = attributes.iterator(); iter.hasNext();) {
				AdapterAttribute attribute = (AdapterAttribute) iter.next();
				Object value = null;
				if(attribute instanceof AttributeMapping){//simple attribute or complex attribute with adapter
					AttributeMapping attrmapping = (AttributeMapping)attribute;
					if(attrmapping.getAdapter()!=null){//attribute has an adapter
						value = adaptFromService(((Map)toBeMapped).remove(attrmapping.getName()),attrmapping.getAdapter());
					}else{//simple attribute
						value = ((Map)toBeMapped).remove(attrmapping.getName());//TODO:simple conversion
					}
				}else if(attribute instanceof AdapterMapping){//internal adapter
					AdapterMapping adaptermapping = (AdapterMapping)attribute;
					value = adaptFromService(toBeMapped,adaptermapping);
				}
				((Map)result).put(attribute.getName(), value);
			}
		}
		if(mapping.adaptAll() && (
				(result==null && toBeMapped!=null && !((Map)toBeMapped).isEmpty())
				|| result instanceof Map
				)){ //adaptall left (removed adapted ones)
			if(result==null){
//				result = new CaseInsensitiveMap();
				result = new DataMap();
			}
			((Map)result).putAll((Map)toBeMapped);
		}
			
		
		return result;
	}
	/**
	 * Adapts data in the datastore for key name from the in parameter to a
	 * value form specified in the in's parameter adaptermapping. 
	 * @param id
	 * @param in
	 * @param jarFileLocation
	 * @return
	 */
	protected static Object adaptToService(ClassLoader classLoader, DataIdentifier id, In in) {
		return adaptToService(id, in, in.getAdapter(), null, classLoader);
	}
	
	private static Object createServiceObj(String className, ClassLoader classLoader){
		Object result = null;
		try {
			if(className==null)
				throw new UnableToAdaptException("Adapter states null outputclass");
			
			Class<?> clazz = classLoader.loadClass(className);
						
			Constructor<?> c = clazz.getConstructor(new Class[0]);
			c.setAccessible(true);
			result = c.newInstance(new Object[0]);
		} catch (InstantiationException e) {
			throw new UnableToAdaptException("Cannot instantiate ["+className+"], hint: define a default constructor", e);
		} catch (IllegalAccessException e) {
			throw new UnableToAdaptException(e);
		} catch (Exception e) {
			throw new UnableToAdaptException(e);
		}
		return result;
	}
	
	private static Object adaptToService(DataIdentifier id, In in, AdapterMapping mapping, Object result, ClassLoader externalClassLoader) {
		if(result==null)
			result = createServiceObj(mapping.getOutputClass(), externalClassLoader);
		
		if(mapping.hasParent()){
			result = adaptToService(id, in, mapping.getParent(), result, externalClassLoader);
		}
		
		List<AdapterAttribute> attributes = mapping.getAdapterAttributes();
		
		if(attributes!=null){
			for (Iterator<AdapterAttribute> iter = attributes.iterator(); iter.hasNext();) {
				AdapterAttribute attribute = (AdapterAttribute) iter.next();
				Object value = null;
				
				String ref = ((in.getRef()==null || StringUtils.isBlank(in.getRef().stringValueOf())) ? "" : (in.getRef().toString() + ".")) + attribute.getName();
				if(attribute instanceof AttributeMapping){//simple attribute or complex attribute with adapter
					AttributeMapping attrmapping = (AttributeMapping)attribute;
					if(attrmapping.getAdapter()!=null){//attribute has an adapter
						value = adaptToService(id, in, attrmapping.getAdapter(), null, externalClassLoader);
					}else{//simple attribute
						value = DataStore.findValue(id, ref);//TODO:simple conversion
					}
				}else if(attribute instanceof AdapterMapping){//internal adapter
					AdapterMapping adaptermapping = (AdapterMapping)attribute;
					value = adaptToService(id, in, adaptermapping, null, externalClassLoader);
				}
				
				result = adaptToJavaObject(mapping.getId(), attribute.getName(), result, value, externalClassLoader);
				
			}
		}
		return result;
	}
	
	private static Object adaptToJavaObject(String mappingId, String attributeName, Object result, Object valueToAdapt, ClassLoader classLoader){
		try {
			if(valueToAdapt!=null){
				Class resultClazz = classLoader.loadClass(result.getClass().getName());
			
				Field field = resultClazz.getDeclaredField(attributeName);
				field.setAccessible(true);
				String fieldName = field.getType().getName();
				String valueName = valueToAdapt.getClass().getName(); 
				logger.info(field.getType().getName());
				
				if(!fieldName.equals(valueName)){
					if(PredefinedClassTypeConverter.isPredefined(field.getType()))
						valueToAdapt = PredefinedAdapterFactory.create(field.getType()).convert(valueToAdapt);
					else
						throw new IllegalArgumentException("Object passed ["+valueToAdapt+"] cannot be converted to type wanted["+field.getType()+"] for field["+field.getName()+"] in adapter["+mappingId+"]");
				}else{
					if(!PredefinedClassTypeConverter.isPredefined(field.getType())){
						Class paramClazz = classLoader.loadClass(fieldName);
						valueToAdapt = paramClazz.cast(valueToAdapt);
					}
				}
				field.set(result, valueToAdapt);
			}
		} catch (IllegalArgumentException e) {
			throw new UnableToAdaptException("arg ["+valueToAdapt+"] is illegal for field with name ["+attributeName+"]", e);
		} catch (SecurityException e) {
			throw new UnableToAdaptException(e);
		} catch (IllegalAccessException e) {
			throw new UnableToAdaptException("field ["+attributeName+"] does not accessible on class ["+result.getClass()+"]", e);
		} catch (NoSuchFieldException e) {
			logger.log(Level.WARNING, "field ["+attributeName+"] does not exist on class ["+result.getClass()+"]", e);
		} catch (ClassNotFoundException e) {
			throw new UnableToAdaptException("field ["+attributeName+"] class not found ["+result.getClass()+"]", e);
		}
		return result;
	}
}
