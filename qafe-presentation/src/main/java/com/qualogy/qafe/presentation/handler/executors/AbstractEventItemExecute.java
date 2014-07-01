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

import com.qualogy.qafe.bind.commons.type.Parameter;
import com.qualogy.qafe.bind.core.application.ApplicationContext;
import com.qualogy.qafe.core.datastore.DataIdentifier;
import com.qualogy.qafe.core.datastore.DataStore;
import com.qualogy.qafe.core.framework.presentation.EventData;

public class AbstractEventItemExecute {

	public Object getValue(ApplicationContext context, DataIdentifier dataId, Parameter parameter, EventData eventData) {
		return EventItemExecuteHelper.getValue(context, dataId, parameter, eventData);
	}
	
	protected String generateLocalStoreId(String windowSession, ApplicationContext context, String windowId) {
		return EventItemExecuteHelper.generateLocalStoreId(windowSession, context, windowId);
	}

	protected String generateGlocalStoreId(String windowSession, ApplicationContext context) {
		return EventItemExecuteHelper.generateLocalStoreId(windowSession, context, null);
	}

	protected void resolveParameters(ApplicationContext context, DataIdentifier dataId, Iterator<? extends Parameter> parameters, EventData eventData) {
		if (parameters == null) {
			return;
		}
		while(parameters.hasNext()){
			Parameter parameter = parameters.next();
			Object value = getValue(context, dataId, parameter, eventData);
			DataStore.store(dataId, parameter.getName(), value);
		}
	}
}
