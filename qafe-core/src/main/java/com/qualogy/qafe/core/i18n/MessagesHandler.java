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
package com.qualogy.qafe.core.i18n;

import java.util.HashMap;
import java.util.Map;

import com.qualogy.qafe.bind.commons.type.Parameter;
import com.qualogy.qafe.bind.commons.type.Reference;
import com.qualogy.qafe.bind.commons.type.Value;
import com.qualogy.qafe.bind.core.application.ApplicationContext;
import com.qualogy.qafe.bind.domain.ApplicationMapping;
import com.qualogy.qafe.core.application.ApplicationCluster;
import com.qualogy.qafe.core.datastore.DataIdentifier;
import com.qualogy.qafe.core.datastore.ParameterValueHandler;

public class MessagesHandler {
	
	private static Map<ApplicationMapping,ApplicationContext> applicationMappingStore = new HashMap<ApplicationMapping,ApplicationContext>(17); 

	public static String getMessage(ApplicationContext applicationContext, DataIdentifier dataId, String messageKey,String staticValue){
		
		Parameter parameter = create(staticValue, messageKey);
		
		String messageValue = (String) ParameterValueHandler.get(applicationContext, dataId, parameter);
		
		return messageValue;
	}
	
	public static String getMessage(ApplicationContext applicationContext, DataIdentifier dataId, String messageKey){
		return getMessage(applicationContext, dataId, messageKey, null);
	}
	
	
	private static Parameter create(String staticValue, String messageKey){
		Value value = new Value(staticValue);
		
		Reference reference = new Reference(messageKey,Reference.SOURCE_MESSAGE_ID);
		
		return new Parameter(reference, value);
	}
	
	/**
	 * method to get an message for given messageKey from a context, which will be retrieved for applicationmapping.
	 * Applicationmapping - Context pairs are cached in static store.
	 * 
	 * @param applicationMapping
	 * @param dataId
	 * @param message
	 * @param staticValue
	 * @return
	 */
	public static String getMessage(ApplicationMapping applicationMapping, DataIdentifier dataId, String message,String staticValue){
		ApplicationContext context = null;
		if (applicationMappingStore.containsKey(applicationMapping)){
			context = applicationMappingStore.get(applicationMapping);
		} else {
			context = ApplicationCluster.getInstance().get(applicationMapping);
			applicationMappingStore.put(applicationMapping, context);
		}
		return getMessage(context, dataId, message, staticValue);
	}
	
	public static void clear(){
		applicationMappingStore.clear();
	}
}
