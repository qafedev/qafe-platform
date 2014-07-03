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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import com.qualogy.qafe.bind.commons.type.Parameter;
import com.qualogy.qafe.bind.commons.type.Reference;
import com.qualogy.qafe.bind.core.application.ApplicationContext;
import com.qualogy.qafe.bind.core.messages.PlaceHolder;
import com.qualogy.qafe.bind.presentation.event.Event;
import com.qualogy.qafe.bind.presentation.event.EventItem;
import com.qualogy.qafe.bind.presentation.event.function.BuiltInFunction;
import com.qualogy.qafe.bind.presentation.event.function.SetValue;
import com.qualogy.qafe.bind.presentation.event.function.SetValueMapping;
import com.qualogy.qafe.core.datastore.ApplicationStoreIdentifier;
import com.qualogy.qafe.core.datastore.DataIdentifier;
import com.qualogy.qafe.core.datastore.DataStore;
import com.qualogy.qafe.core.framework.presentation.EventData;
import com.qualogy.qafe.core.placeholder.PlaceHolderResolver;
import com.qualogy.qafe.presentation.EventHandlerImpl;
import com.qualogy.qafe.presentation.handler.ExecuteEventItem;

@Deprecated
public class SetValueExecute extends AbstractEventItemExecute implements
		ExecuteEventItem {

	public boolean execute(EventItem eventItem, ApplicationContext context,
			Event event, EventData eventData, Collection<BuiltInFunction> listToExecute,
			EventHandlerImpl eventHandler, DataIdentifier dataId) {
		
		
		SetValue origSetValue = (SetValue) eventItem;
		Parameter origParam = origSetValue.getParameter();
		if(origParam==null) {
			throw new IllegalArgumentException("At least one of the parameter fields is mandatory");
		}	
		
		// do not change the object from the applicationmapping.
		// Make a copy instead, filled in with the correct data
		SetValue copySetValue = new SetValue();
		copySetValue.setComponentId(origSetValue.getComponentId());
		copySetValue.setAction(origSetValue.getAction());
		copySetValue.setGroup(origSetValue.getGroup());
		// clone mapping
		if (origSetValue.getMapping()!=null){
			copySetValue.setMapping(new ArrayList<SetValueMapping>());
			for ( SetValueMapping svm: origSetValue.getMapping()) {
				copySetValue.getMapping().add(svm.clone());
			}
		}
		
		Parameter copyParam = null;
		try {
			copyParam = (Parameter)origParam.clone();
			copySetValue.setParameter(copyParam);
			
			/**
			 * set the placeholders value (if src="component") to the default of pipe);
			 */
			if (copyParam.hasPlaceHolders()){
				Iterator<PlaceHolder> itr = copyParam.getPlaceHolders().iterator();
				while (itr.hasNext()){
					PlaceHolder  placeHolder = (PlaceHolder)itr.next();
					if (placeHolder.getRef()!=null){
						if (Reference.SOURCE_COMPONENT_ID.equals(placeHolder.getRef().getSource())){
							// put the value in the datastore
						    Object o = getValue(context, dataId, placeHolder, eventData);
							DataStore.store(dataId, placeHolder.getRef().getRootRef(), o);							
							// modify the src to get the data in the datastore 
							placeHolder.getRef().setSource(Reference.SOURCE_DATASTORE_ID);
						}
					}
				}
			}
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException("Cloneable interface on Parameter has changed", e);
		}
		
		// Try to resolve variables defined in component-id attribute
		String componentId = copySetValue.getComponentId();
		if (componentId != null) {
			ApplicationStoreIdentifier appStoreId = eventData.getApplicationStoreIdentifier();
			componentId = (String)PlaceHolderResolver.resolve(context, appStoreId, dataId, componentId, copyParam, null);
			copySetValue.setComponentId(componentId);
		}
		copySetValue.setDataObject(getValue(context, dataId, copyParam, eventData));	
		listToExecute.add(copySetValue);
		return false;
	}
}
