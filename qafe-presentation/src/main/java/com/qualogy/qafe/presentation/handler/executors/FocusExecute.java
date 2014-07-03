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

import com.qualogy.qafe.bind.core.application.ApplicationContext;
import com.qualogy.qafe.bind.presentation.event.Event;
import com.qualogy.qafe.bind.presentation.event.EventItem;
import com.qualogy.qafe.bind.presentation.event.function.BuiltInFunction;
import com.qualogy.qafe.bind.presentation.event.function.Focus;
import com.qualogy.qafe.core.datastore.ApplicationStoreIdentifier;
import com.qualogy.qafe.core.datastore.DataIdentifier;
import com.qualogy.qafe.core.framework.presentation.EventData;
import com.qualogy.qafe.core.placeholder.PlaceHolderResolver;
import com.qualogy.qafe.presentation.EventHandlerImpl;
import com.qualogy.qafe.presentation.handler.ExecuteEventItem;

public class FocusExecute extends AbstractEventItemExecute implements ExecuteEventItem {

	public boolean execute(EventItem eventItem, ApplicationContext context, Event event, EventData eventData, Collection<BuiltInFunction> listToExecute, EventHandlerImpl eventHandler, DataIdentifier dataId) {
		if (eventItem instanceof Focus) {
			try {
				Focus copyFocus = ((Focus)eventItem).clone();
				
				// Try to resolve variables defined in component-id attribute
				String componentId = copyFocus.getComponentId();
				if (componentId != null) {
					ApplicationStoreIdentifier appStoreId = eventData.getApplicationStoreIdentifier();
					componentId = (String)PlaceHolderResolver.resolve(context, appStoreId, dataId, componentId, null, null);
					copyFocus.setComponentId(componentId);
				}
				
				listToExecute.add(copyFocus);
			} catch (CloneNotSupportedException e) {
				throw new RuntimeException("Cloneable interface on Parameter has changed", e);
			}
		}
		return false;
	}	
}