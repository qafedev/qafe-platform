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
package com.qualogy.qafe.presentation.handler.executors;

import java.util.Iterator;
import java.util.Map;

import com.qualogy.qafe.bind.commons.type.Parameter;
import com.qualogy.qafe.bind.commons.type.Reference;
import com.qualogy.qafe.bind.core.application.ApplicationContext;
import com.qualogy.qafe.bind.presentation.event.InputVariable;
import com.qualogy.qafe.core.datastore.ApplicationLocalStore;
import com.qualogy.qafe.core.datastore.DataIdentifier;
import com.qualogy.qafe.core.datastore.DataStore;
import com.qualogy.qafe.core.datastore.ParameterValueHandler;
import com.qualogy.qafe.core.framework.presentation.EventData;
import com.qualogy.qafe.core.framework.presentation.EventDataContainer;

public abstract class EventItemExecuteHelper {

	public static Object getValue(ApplicationContext context, DataIdentifier dataId, Parameter parameter, EventData eventData) {
		Object data = null;
		
		String windowId = eventData.getWindowId();
		Reference reference = parameter.getRef();
		if ((reference != null) && reference.isGlobalStoreReference()) {
			windowId = null;
		}
		String localStoreId = generateLocalStoreId(eventData.getWindowSession(), context, windowId);
		
		if ((reference != null) && reference.isComponentReference()) {
			data = getValueFromInput(reference.toString(), eventData, dataId);
			if (data == null) {
				data = ParameterValueHandler.get(context, eventData.getApplicationStoreIdentifier(), dataId, parameter,localStoreId);
			}											
		} else {
			data = ParameterValueHandler.get(context, eventData.getApplicationStoreIdentifier(), dataId, parameter,localStoreId);
		}
		return resolveLookupData(dataId, eventData, parameter, data);
	}
	
	public static String generateLocalStoreId(String sessionId, ApplicationContext context, String windowId) {
		return ParameterValueHandler.generateLocalStoreId(sessionId, context, windowId);
	}
	
	private static Object getValueFromInput(String referenceName, EventData eventData, DataIdentifier dataId) {
    	Object value = null;
    	StringBuffer buffer = new StringBuffer();
		Iterator<InputVariable> inputVariablesItr = eventData.getInputVariables().iterator();
		while (inputVariablesItr.hasNext()) {
			Object o = inputVariablesItr.next();
			if (o instanceof InputVariable) {
				InputVariable parameter = (InputVariable)o;
				buffer.append("inputvar: [name=" + parameter.getName() + ",reference=" + parameter.getReference() + ",value=" + parameter.getDefaultValue() + ",componentvalue=" + parameter.getComponentValue() + "]\n");
				if (parameter.getReference() != null) {
					if (parameter.getReference().toString().equals(referenceName)) {
						if (parameter.getDataObject() == null) {
							value = parameter.getComponentValue();
						} else {
							EventDataContainer eventDataContainer = (EventDataContainer)parameter.getDataObject();
							value = eventDataContainer.getData();
						}
					}
				}
			}
		}
    	return value;
	}
	
	private static Object resolveLookupData(DataIdentifier dataId, EventData eventData, Parameter parameter, Object data) {
		if (data instanceof Map) {
			Map dataMap = (Map)data;
			Iterator keyIterator = dataMap.keySet().iterator();
			while(keyIterator.hasNext()) {
				String key = (String)keyIterator.next();
				Object value = dataMap.get(key);
				value =	retrieveLookupData(dataId, eventData, parameter, value);
				dataMap.put(key, value);
			}			
		} else {
			data = retrieveLookupData(dataId, eventData, parameter, data);
		}
		return data;
	}
	
	private static Object retrieveLookupData(DataIdentifier dataId, EventData eventData, Parameter parameter, Object o) {
		Object result = o;
		if (isLookupData(o)) { 
			String lookupData = (String) o;
			String lookupKey = eventData.getWindowSession() + ApplicationLocalStore.OBJECT_DELIMITER + eventData.getWindowId();
			//String lookupKey = eventData.getApplicationIdentifier().stringValueOf() + ApplicationLocalStore.OBJECT_DELIMITER + eventData.getWindowId();
			if (ApplicationLocalStore.getInstance().contains(lookupKey, lookupData)) {
				result = ApplicationLocalStore.getInstance().retrieve(lookupKey, lookupData);
				ParameterValueHandler.store(dataId, parameter, result);
			}
		}
		return result;
	}
	
	private static Boolean isLookupData(Object data) {
		if (data instanceof String) {
			String lookupDataKey = DataStore.KEY_LOOKUP_DATA;
			String lookupData = (String) data;
			if (lookupData.indexOf(lookupDataKey) > -1) {
				return true;
			}
		}
		return false;
	}
}