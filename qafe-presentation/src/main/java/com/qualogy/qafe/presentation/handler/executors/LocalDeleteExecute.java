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

import java.util.Collection;

import com.qualogy.qafe.bind.commons.type.Reference;
import com.qualogy.qafe.bind.core.application.ApplicationContext;
import com.qualogy.qafe.bind.presentation.event.Event;
import com.qualogy.qafe.bind.presentation.event.EventItem;
import com.qualogy.qafe.bind.presentation.event.function.BuiltInFunction;
import com.qualogy.qafe.bind.presentation.event.function.LocalDelete;
import com.qualogy.qafe.core.datastore.ApplicationLocalStore;
import com.qualogy.qafe.core.datastore.DataIdentifier;
import com.qualogy.qafe.core.datastore.DataStore;
import com.qualogy.qafe.core.framework.presentation.EventData;
import com.qualogy.qafe.presentation.EventHandlerImpl;
import com.qualogy.qafe.presentation.handler.ExecuteEventItem;

public class LocalDeleteExecute extends AbstractEventItemExecute implements
		ExecuteEventItem {

	public boolean execute(EventItem eventItem, ApplicationContext context, Event event, EventData eventData, Collection<BuiltInFunction> listToExecute, EventHandlerImpl eventHandler, DataIdentifier dataId) {
		LocalDelete localDelete = (LocalDelete)eventItem;
		
		String localDeleteName = localDelete.getName();
		String localDeleteTarget = localDelete.getTarget();
		if (Reference.SOURCE_DATASTORE_ID.equals(localDeleteTarget)) {
			// target = "pipe"
			DataStore.getDataStore(dataId).remove(localDeleteName);
		} else {
			String storeId = null;			
			if (Reference.SOURCE_APP_LOCAL_STORE_ID.equals(localDeleteTarget)) {
				// target = "user"
			    storeId = generateLocalStoreId(eventData.getWindowSession(), context, eventData.getWindowId());
			} else if (Reference.SOURCE_APP_GLOBAL_STORE_ID.equals(localDeleteTarget)) {
				// target = "global"
				storeId = generateGlocalStoreId(eventData.getWindowSession(), context);
			}
			
 			if ((localDeleteName != null) && (localDeleteName.length() > 0)) {
				ApplicationLocalStore.getInstance().delete(storeId, localDeleteName);
			} else {			
				ApplicationLocalStore.getInstance().deleteAll(storeId);				
			}
		}
		return false;
	}
}
