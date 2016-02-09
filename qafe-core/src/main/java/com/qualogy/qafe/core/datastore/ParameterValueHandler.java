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
package com.qualogy.qafe.core.datastore;

import java.util.logging.Logger;

import org.apache.commons.lang.StringUtils;

import com.qualogy.qafe.bind.commons.type.Parameter;
import com.qualogy.qafe.bind.commons.type.Reference;
import com.qualogy.qafe.bind.commons.type.Value;
import com.qualogy.qafe.bind.core.application.ApplicationContext;
import com.qualogy.qafe.bind.core.application.ApplicationIdentifier;
import com.qualogy.qafe.bind.core.messages.Bundle;
import com.qualogy.qafe.bind.core.messages.PlaceHolder;
import com.qualogy.qafe.core.application.ApplicationCluster;
import com.qualogy.qafe.core.i18n.LocaleResolver;
import com.qualogy.qafe.core.placeholder.PlaceHolderResolver;
import com.qualogy.qafe.core.script.Script;
import com.qualogy.qafe.core.script.ScriptBuilder;
import com.qualogy.qafe.core.script.ScriptEngineManager;
/**
 * Helper class for retrieving data for a given key
 * @author 
 *
 */
public class ParameterValueHandler {
	
	public final static Logger logger = Logger.getLogger(ParameterValueHandler.class.getName());
	
	
	public static Object get(ApplicationContext context, DataIdentifier dataId, Reference reference){
		return get(context, null, dataId, reference);
	}
	public static Object get(ApplicationContext context, ApplicationStoreIdentifier storeId, DataIdentifier dataId, Reference reference){
		return get( context,storeId, dataId, reference,null);
	}
	
	/**
	 * Method gets a value for given reference. This method uses the references source
	 * to retrieve the value from the assigned source. In case of storeId, when not null
	 * or source set to local store reference, localstore always goes first.
	 * @param context
	 * @param appStoreId
	 * @param dataId
	 * @param reference
	 * @return
	 */
	public static Object get(ApplicationContext context, ApplicationStoreIdentifier appStoreId, DataIdentifier dataId, Reference reference,String storeId){	
		if(context==null)
			throw new IllegalArgumentException("context may not be null");
		
		Object value = null;
		if(reference != null){ //if ref is set try to retrieve a value
			String ref = reference.stringValueOf();
			if(ref.contains("[${")) {
				String varName = ref.substring(ref.indexOf("{")+1, ref.lastIndexOf("}"));
				Reference varRef = new Reference();
				varRef.setRef(varName);
				varRef.setSource(reference.getSource());
				Object obj = get(context, appStoreId, dataId, varRef, storeId);
				if(obj != null) {
					ref = ref.replace("${"+varName+"}", obj.toString());
				}
				
			}
			if(reference.isLocalStoreReference() || reference.isGlobalStoreReference()){
				if(storeId == null) {
					throw new IllegalArgumentException("store id cannot be null when using src=" + Reference.SOURCE_APP_LOCAL_STORE_ID);
				}
				value = getFromLocalStore(storeId, ref);
			} else if (reference.isMessageReference()){ //message resource
				String bundleId = (reference.hasRootRef())?reference.getRootRef():Bundle.DEFAULT_BUNDLE_ID;
				value = context.getMessages()!=null ? context.getMessages().get(bundleId, reference.getRefWithoutRoot(), LocaleResolver.resolve(dataId)) : null;
			} else if (reference.isDataStoreReference()){ //datastore value
				value = DataStore.findValue(dataId, ref);
			} 
		}
		return value;
	}
	
	public static Object get(ApplicationContext context, DataIdentifier dataId, Parameter parameter){
		return get(context, null, dataId, parameter);
	}
	public static Object get(ApplicationContext context, ApplicationStoreIdentifier storeId, DataIdentifier dataId, Parameter parameter){
		return get( context, storeId,dataId, parameter,null);
	}

	/**
	 * Method to get a value based on an in. This method
	 * - first checks the ref (specified in the parameter), saying
	 * it'll check either system or datastore. 
	 * - second it'll use the staticvalue of the object, or if the ref/ name ends up with a null
	 * value, the static(default)value (if set is used).
	 * - when no ref is set and no default value is supplied, null will be returned when nullable is true
	 * (otherwise an exception is thrown) 
	 * 
	 * NOTE: if ref or name is set that value will be retrieved, BUT
	 * if the result of retrieving the value is null and default value is set to overwrite, 
	 * that value will be used!!!
	 * 
	 * @param userDefinedId
	 * @param parameter
	 * @return
	 */
	public static Object get(ApplicationContext context, ApplicationStoreIdentifier storeId, DataIdentifier dataId, Parameter parameter,String localStoreId){
		if (parameter == null) {
			throw new IllegalArgumentException("parameter cannot be null");
		}	
		Object value = null;
		if (parameter.getExpression() != null) {
			String expression = parameter.getExpression();
			if (!StringUtils.isBlank(expression)) {
				Script script = ScriptBuilder.build(context, storeId, dataId, expression, parameter, localStoreId);
				value = ScriptEngineManager.getEngine(context).process(script);
			}
		} else {
			Reference objectOfReference = parameter.getRef();
			Value objectOfValue = parameter.getValue();
			if (objectOfReference != null) {
				// newlocalStoreId will be based on the objectOfReference, like placeholders, and not the passing in localStoreId 
				String newLocalStoreId = generateLocalStoreId(context, objectOfReference, localStoreId);
				
				value = get(context,storeId, dataId, objectOfReference, newLocalStoreId);
				if (value == null) {
					if (objectOfReference.isComponentReference() || (parameter instanceof PlaceHolder)) {
						// This is a fallback, all parameters with the reference (src="component") or placeholders
						// are stored in the datastore using the name of the parameter
						value = DataStore.findValue(dataId, parameter.getName());
					}
				}
			} else if (objectOfValue != null) {
				String staticValue = objectOfValue.getStaticValue();
				if (staticValue != null) {
					value = new String(staticValue);
				}
			} else {
				// When the @ref is not specified explicitly, the default will be ref=@name and src="pipe"
				// e.g. : <in name="myVar"/> <=> <in name="myVar" ref="myVar" src="pipe"/>
				value = DataStore.findValue(dataId, parameter.getName());
			}
			value = PlaceHolderResolver.resolve(context, storeId, dataId, value, parameter, localStoreId);
		}
		return value;
	}
	
	/**
	 * This method uses internal method to get context based upon identifier
	 * @see get(ApplicationContext context, DataIdentifier dataId, Parameter parameter)
	 * @param applicationId
	 * @param dataId
	 * @param reference
	 * @return
	 */
	public static Object get(ApplicationIdentifier applicationId, DataIdentifier dataId, Reference reference){
		return get(getContext(applicationId), dataId, reference);
	}
	
	/**
	 * @see ParameterValueHandler.get(ApplicationContext context, DataIdentifier dataId, Parameter parameter)
	 */
	public static Object get(ApplicationIdentifier applicationId, DataIdentifier dataId, Parameter parameter){
		return get(getContext(applicationId), dataId, parameter);
	}
	/**
	 * This method uses internal method to get context based upon identifier
	 * @see get(ApplicationContext context, DataIdentifier dataId, Parameter parameter)
	 * @param applicationId
	 * @param dataId
	 * @param reference
	 * @return
	 */
	public static Object get(ApplicationIdentifier applicationId, ApplicationStoreIdentifier storeId, DataIdentifier dataId, Reference reference){
		return get(getContext(applicationId), storeId, dataId, reference);
	}
	
	/**
	 * @see ParameterValueHandler.get(ApplicationContext context, DataIdentifier dataId, Parameter parameter)
	 */
	public static Object get(ApplicationIdentifier applicationId, ApplicationStoreIdentifier storeId, DataIdentifier dataId, Parameter parameter){
		return get(getContext(applicationId), storeId, dataId, parameter);
	}
	
	
	
	/**
	 * Convinience method to store a parameter and value in the datastore.
	 * - if name is set it'll be used for storage
	 * - if name not set reference will be used
	 * - if both not set the value will be stored under null key (check datastore
	 * impl if possible)
	 * @param id
	 * @param parameter
	 * @param value
	 */
	public static void store(DataIdentifier id, Parameter parameter, Object value){
		String key = null;
		if(parameter.getName()!=null){
			key = parameter.getName();
		}else if(parameter.getRef()!=null && parameter.getRef().toString()!=null){
			key = parameter.getRef().toString();
		}
		DataStore.store(id, key, value);
	}

	public static String generateLocalStoreId(ApplicationContext context, Reference reference, String localStoreId) {
		if (localStoreId == null) {
			return null;
		}				
		if ((reference != null) && reference.isGlobalStoreReference()) {
			// localStoreId contains <sessionId>.<contextId>|<windowId>
			int indexOfObjectDelim = localStoreId.indexOf(ApplicationLocalStore.OBJECT_DELIMITER);
			if (indexOfObjectDelim > -1) {
				String sessionId = localStoreId.substring(0, indexOfObjectDelim); 
				localStoreId = generateLocalStoreId(sessionId, context, null);	
			}			
		}
		return localStoreId;
	}
	
	public static String generateLocalStoreId(String sessionId, ApplicationContext context, String windowId) {
		String localStoreId = sessionId + ApplicationLocalStore.OBJECT_DELIMITER + context.getId().stringValueOf();
		if (windowId != null) {
			localStoreId += ApplicationLocalStore.CONTEXT_DELIMITER + windowId;
		}
		return localStoreId;
	}
	
	private static ApplicationContext getContext(ApplicationIdentifier applicationId){
		if(applicationId==null)
			throw new IllegalArgumentException("appid may not be null");
		return ApplicationCluster.getInstance().get(applicationId);
	}
	
	private static Object getFromLocalStore(String uuid, String key){
		return ApplicationLocalStore.getInstance().retrieve(uuid, key);
	}
}
