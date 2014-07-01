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
package com.qualogy.qafe.business.integration.adapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.AbstractHashedMap;
import org.apache.commons.collections.map.CaseInsensitiveMap;

import com.qualogy.qafe.bind.commons.type.AdapterMapping;
import com.qualogy.qafe.bind.commons.type.In;
import com.qualogy.qafe.bind.commons.type.Parameter;
import com.qualogy.qafe.bind.commons.type.Reference;
import com.qualogy.qafe.bind.commons.type.TypeDefinition;
import com.qualogy.qafe.bind.core.application.ApplicationContext;
import com.qualogy.qafe.business.integration.builder.PredefinedClassTypeConverter;
import com.qualogy.qafe.core.datastore.DataIdentifier;
import com.qualogy.qafe.core.datastore.DataStore;
import com.qualogy.qafe.core.datastore.ParameterValueHandler;
import com.qualogy.qafe.core.datastore.DataMap;

/**
 * Integration service adpater that can adapt parameters to and from a service call
 * to an actual servicecall, done by a processor
 * @author mvanderwurff
 *
 */
public class Adapter{

	/**
	 * Method to adapt an integration service result to one ore more business domain objects. If service result
	 * is a list, the list will be recursively adapted. 
	 * 
	 * This 'controlling' method for adapting the output controls in 2 steps:
	 *  1. map according to specified service attribute name (regarding the dot notation within the reference key)
	 *  2. map according to mapping or type	 
	 * 
	 * The order in which the mapping upon the outcome will be tried is:
	 *  1. MappingAdapter when mapping not null,
	 *  2. PredefinedMapper when type is a known (for this business project) type
	 *  3. BestEffortAdapter will try to adapt Type(business domain) rules to the given object
	 *  
	 *  Notes:
	 *  - Empty mapping leaves a null object
	 *  - A null paramname on an outputmapping doesn't leave an entry 
	 *   
	 * @param id
	 * @param serviceOutcome - the object from a service to adapt
	 * @param outputMapping - the mapping the object needs to be adapted to
	 */
	public static void adaptOut(DataIdentifier id, Object serviceOutcome, List outputMapping){
		
		if(serviceOutcome instanceof Object[]){
			serviceOutcome = Arrays.asList((Object[])serviceOutcome);
		}
		
		if(outputMapping!=null){
			for (Iterator iter1 = outputMapping.iterator(); iter1.hasNext();) {
				Parameter param = (Parameter) iter1.next();
				if(param.getName() == null)
					continue;
				Object dsResult = null;
				
				if(serviceOutcome instanceof List){
					dsResult = prepareList((List)serviceOutcome, param);
				}else{
					dsResult = prepare(serviceOutcome, param);
				}
				
				if(DataStore.findValue(id, DataStore.KEY_WORD_COUNT) != null){
					dsResult = ((Map)((ArrayList)dsResult).get(0)).get("count(*)");
				}
				
				DataStore.store(id, param.getName(), dsResult);
			}
		}
		
		retrieveQafeBuiltInList(id, serviceOutcome);
		
	}

	private static void retrieveQafeBuiltInList(DataIdentifier id,
			Object serviceOutcome) {
		// A call statement can return a variable containing the json representation of the builtins to execute.
		// This method is to collect that value to use it in business action without user mentioning it in the out param.
		if(serviceOutcome instanceof Map) { 
			// Call statement always returns a Map.(In case of java if the same functionality needed it should also return Map)
			// If we do not do this check excpetion can happen in case of simple value return from Java.
			Reference ref = new Reference(DataStore.KEY_WORD_QAFE_BUILT_IN_LIST, Reference.SOURCE_DATASTORE_ID);
			Parameter builtInParameter = new Parameter(ref);
			builtInParameter.setName(DataStore.KEY_WORD_QAFE_BUILT_IN_LIST);
			Object dsResult = prepare(serviceOutcome, builtInParameter);
			if(dsResult != null) {
				DataStore.store(id, builtInParameter.getName(), dsResult);
			}
		}
		
	}
	
	/**
	 * Method to prepare objects for adaption and the actual adapt action
	 * Method checks if a reference key is supplied to get a nested value from
	 * the serviceoutcome. 
	 * 
	 * The serviceOutcome must be of type Map, so will be converted to a Map if necessary.
	 * 
	 * @param serviceOutcome
	 * @param param
	 * @return
	 */
	private static Object prepare(Object serviceOutcome, Parameter param){
		Object toBeMapped = null;
		Object converted = ObjectMapConverter.convert(serviceOutcome);
		if(converted!=null)//if cannot be converted
			serviceOutcome = converted;
		
		if(param.getRef()==null || param.getRef().denotesRoot()){ //need root?
			toBeMapped = serviceOutcome;
		}else{ //serviceoutcome must be a map if not root is wanted
			if(!(serviceOutcome instanceof Map))
				throw new UnableToAdaptException("cannot get attributes like '" + param.getRef() + "' from a non-complex type (" + (serviceOutcome!=null?""+serviceOutcome.getClass():"null") + ")");
			
			toBeMapped = (serviceOutcome==null || ((Map)serviceOutcome).isEmpty())?null:((Map)serviceOutcome).get(param.getRef().toString());
		}
		return adaptFromService(toBeMapped, param.getAdapter(), param.getType());
	}
	
	/**
	 * Method to prepare a list of objects for actual adaption to do's, 
	 * uses Adapter.prepare(Object, Parameter), so actually a convinience 
	 * method that loops through the list and calls prepare.
	 * 
	 * upon the objects
	 * @param serviceOutcome
	 * @param param
	 * @return
	 */
	private static Object prepareList(List serviceOutcome, Parameter param){
		List result = null;
		if(!serviceOutcome.isEmpty()){
			result = new ArrayList();
			for (Iterator iter = serviceOutcome.iterator(); iter.hasNext();) {
				result.add(prepare(iter.next(), param));
			}
		}
		return result;
	}
	
	/**
	 * Method to adapt a (part of a) serviceOutcome to a type or mapping specified in the configuration.
	 * 
	 * If List or array, this method is called recursively. 
	 *  
	 * @param toAdapt
	 * @param mapping
	 * @param type
	 * @return
	 */
	private static Object adaptFromService(Object toAdapt, AdapterMapping mapping, TypeDefinition type) {

		Object adapted = null;

		if(toAdapt instanceof Object[]){
			toAdapt = Arrays.asList((Object[]) toAdapt);
		}
		
		if(toAdapt instanceof Collection){
			List result = new ArrayList();
			for (Iterator iter = ((List)toAdapt).iterator(); iter.hasNext();){
				Object tmpResult = adaptFromService(iter.next(),mapping, type);
				result.add(tmpResult);
			}
			adapted = result;
		}else{
			if(toAdapt!=null){
				if(mapping!=null){//map according to adapter, it's always a map
					adapted = MappingAdapter.adaptFromService(toAdapt, mapping);
				}else if(PredefinedAdapterFactory.canObjectBeConverted(type, toAdapt)){//simple type
					adapted = PredefinedAdapterFactory.create(type).convert(toAdapt);
				}else if(type!=null){//either create mapping based upon typedefinition otherwise see if we have some conversion todo
					adapted = BestEffortAdapter.adapt((Map)toAdapt, type);
				}else{
					adapted = toAdapt;
				}
			}
		}
		return adapted;
	}
	
//	public static Map adaptIn(ApplicationContext context, DataIdentifier id, List inputMapping) {
//		return Adapter.adaptIn(context, id, inputMapping, null);
//	}
	
	/**
	 * Method adapts the data stored under the uniqueidentifier to the types and adapters
	 * supplied through the mapping. Afterwards the method wil create adapted objects
	 * for processing.
	 * 
	 * First there needs to be determined if the in parameter has an adaptermapping supplied.
	 * If so, the MappingAdapter will handle accordingly and adapts the object structure
	 * to the mapping. Else the single value is retrieved and converted if necessary.
	 * Secondly the data is prepared for the service call. 
	 * 	- If the adapted value is not null, this will be the adpated object
	 *  - Otherwise the type will be inspected and taken as being adapted
	 *  - If the type is also not supplied, the adapter is checked for an outputclass,
	 *  which will than be the adapted object
	 *  - If none of above applies the method will throw an exception because later on the 
	 *  method can not be determined because of the missing argument.
	 * 
	 * @param id
	 * @param inputMapping
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, AdaptedToService> adaptIn(ClassLoader classLoader, ApplicationContext context, DataIdentifier id, List<In> inputMapping) {
//		Map<String, AdaptedToService> adaptedMap = new CaseInsensitiveMap();
		Map<String, AdaptedToService> adaptedMap = new DataMap<String, AdaptedToService>();
		for (int i = 0; inputMapping!=null && i < inputMapping.size(); i++) {
			In in = (In)inputMapping.get(i);
			
			Object adapted = null;//conversion-/adapter-result
			if(in.getAdapter()!=null && in.getAdapter().getOutputClass()!=null){
				adapted = MappingAdapter.adaptToService(classLoader, id, in);
			}else{
				adapted = ParameterValueHandler.get(context, id, in);
				if(in.getOutputClass()!=null && adapted instanceof Map){
					adapted = ObjectMapConverter.convert(in.getOutputClass(), (Map)adapted);
				}else{
					if(in.getType()!=null && PredefinedAdapterFactory.canObjectBeConverted(in.getType(), adapted))
						adapted = PredefinedAdapterFactory.create(in.getType()).convert(adapted);
				}
			}
			
			if(adapted!=null || (in.getUseWhenNotSet()!=null && in.getUseWhenNotSet()==true)){
				adaptedMap = addAdaptedToServiceMap(adaptedMap, in, adapted);
			}
		}
		return adaptedMap;
	}

	private static Map<String, AdaptedToService> addAdaptedToServiceMap(Map<String, AdaptedToService> adaptedMap, In in, Object adapted) {
		AdaptedToService ats = null;
		if(adapted!=null){
			ats = AdaptedToService.create(adapted);
		}else if(in.getUseWhenNotSet()!=null && in.getUseWhenNotSet()==true){
			if(in.getType()!=null && PredefinedClassTypeConverter.isPredefined(in.getType())){//if value is null set type if not null, so method can be determined and called with null param
				ats = AdaptedToService.create(PredefinedClassTypeConverter.convert(in.getType()));
			}else if(in.getOutputClass()!=null){
				ats = AdaptedToService.create(in.getOutputClass());
			}else if(in.getAdapter()!=null && in.getAdapter().getOutputClass()!=null){
				ats = AdaptedToService.create(in.getAdapter().getOutputClass());
			}
		}
		adaptedMap.put(in.getName(), ats);
	
		
		return adaptedMap;
	}
}
