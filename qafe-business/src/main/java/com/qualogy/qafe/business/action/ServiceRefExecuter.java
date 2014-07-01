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
package com.qualogy.qafe.business.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.qualogy.qafe.bind.commons.type.In;
import com.qualogy.qafe.bind.core.application.ApplicationContext;
import com.qualogy.qafe.bind.integration.service.ServiceRef;
import com.qualogy.qafe.bind.item.Item;
import com.qualogy.qafe.business.integration.IntegrationProcessorFactory;
import com.qualogy.qafe.core.datastore.DataIdentifier;
import com.qualogy.qafe.core.datastore.DataStore;
import com.qualogy.qafe.core.errorhandling.ExternalException;
import com.qualogy.qafe.core.framework.business.IntegrationProcessor;

public class ServiceRefExecuter extends ItemExecuter{
	
	public void execute(ApplicationContext context, DataIdentifier id, Item item)  throws ExternalException{
			
		ServiceRef serviceRef = (ServiceRef)item;
	
		if(serviceRef.getRef()==null)
			throw new NullPointerException("service isn't referencing to an item");
		
		
		processIn(context, id, serviceRef.getInput());
		
		IntegrationProcessor processor = IntegrationProcessorFactory.create(serviceRef.getRef().getResourceRef().getRef());
		processor.process(context, id, serviceRef);
		
		processOut(context, id, serviceRef.getOutput());
		
		collectUpdatedModels(id, serviceRef);
	}

	private void collectUpdatedModels(DataIdentifier id, ServiceRef serviceRef) {
		if (DataStore.contains(id, DataStore.KEY_SERVICE_MODIFY)) {
			DataStore.getDataStore(id).remove(DataStore.KEY_SERVICE_MODIFY);
			
			String methodId = serviceRef.getMethod().getId();
			List<String> modifiedModels = new ArrayList<String>();
			if (serviceRef.getInput() != null) {
				for (Object input : serviceRef.getInput()) {
					if (input instanceof In) {
						In in = (In)input;
						if (in.getRef() != null) {
							String model = in.getRef().getRefWithoutRoot();
							if (modifiedModels.contains(model)) {
								continue;
							}
							modifiedModels.add(model);	
						}
					}
				}
			}
			
			Map<String,List> serviceModifiedModels = null;
			if (!DataStore.contains(id, DataStore.KEY_SERVICE_MODIFIED_MODELS)) {
				serviceModifiedModels = new HashMap<String, List>();
				DataStore.store(id, DataStore.KEY_SERVICE_MODIFIED_MODELS, serviceModifiedModels);
			}
			serviceModifiedModels = (Map<String,List>)DataStore.getValue(id, DataStore.KEY_SERVICE_MODIFIED_MODELS);
			serviceModifiedModels.put(methodId, modifiedModels);
		}
	}
}