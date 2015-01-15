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
package com.qualogy.qafe.presentation.handler.executors;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.qualogy.qafe.bind.commons.type.Parameter;
import com.qualogy.qafe.bind.commons.type.Reference;
import com.qualogy.qafe.bind.core.application.ApplicationContext;
import com.qualogy.qafe.bind.presentation.event.Event;
import com.qualogy.qafe.bind.presentation.event.EventItem;
import com.qualogy.qafe.bind.presentation.event.function.BuiltInFunction;
import com.qualogy.qafe.bind.presentation.event.function.LocalStore;
import com.qualogy.qafe.core.datastore.ApplicationLocalStore;
import com.qualogy.qafe.core.datastore.DataIdentifier;
import com.qualogy.qafe.core.datastore.DataStore;
import com.qualogy.qafe.core.framework.presentation.EventData;
import com.qualogy.qafe.presentation.EventHandlerImpl;
import com.qualogy.qafe.presentation.handler.ExecuteEventItem;

public class LocalStoreExecute extends AbstractEventItemExecute implements ExecuteEventItem {
	public final static Logger logger = Logger.getLogger(LocalStoreExecute.class.getName());

    // CHECKSTYLE.OFF: CyclomaticComplexity
	public boolean execute(EventItem eventItem, ApplicationContext context, Event event, EventData eventData, Collection<BuiltInFunction> listToExecute, EventHandlerImpl eventHandler, DataIdentifier dataId) {

		LocalStore origStore = (LocalStore) eventItem;
		Parameter origParam = origStore.getParameter();
		if (origParam == null)
			throw new IllegalArgumentException("At least one of the parameter fields is mandatory");

		// do not change the object from the applicationmapping.
		// Make a copy instead, filled in with the correct data
		LocalStore copyStore = new LocalStore();
		Parameter copyParam = null;
		try {
			copyParam = (Parameter) origParam.clone();
			copyStore.setParameter(copyParam);
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException("Cloneable interface on Parameter has changed", e);
		}
		copyStore.setTarget(origStore.getTarget());
		copyStore.setAction(origStore.getAction());
		copyStore.setField(origStore.getField());

		// Before the method getValue() gets executed, the placeholders, if any, should be resolved (storing in the pipe),
		// because placeholders can refer to an input variable (component) which is not accessible when further processing
		if (copyParam.getPlaceHolders() != null) {
			resolveParameters(context, dataId, copyParam.getPlaceHolders().iterator(), eventData);
		}
		copyStore.setDataObject(getValue(context, dataId, copyParam, eventData));
		listToExecute.add(copyStore);


		String identifier = null;
		if (Reference.SOURCE_APP_LOCAL_STORE_ID.equalsIgnoreCase(copyStore.getTarget())) { 
			// target = "user"
			identifier = generateLocalStoreId(eventData.getWindowSession(), context, eventData.getWindowId());
		} else if (Reference.SOURCE_APP_GLOBAL_STORE_ID.equalsIgnoreCase(copyStore.getTarget())) { 
			// target = "global"
			identifier = generateGlocalStoreId(eventData.getWindowSession(), context);
		}

		if (copyStore.getAction() == null || copyStore.getAction().length() == 0 || LocalStore.ACTION_SET.equals(copyStore.getAction())) {
			if (Reference.SOURCE_DATASTORE_ID.equalsIgnoreCase(copyStore.getTarget())) {
				DataStore.store(dataId, copyStore.getParameter().getName(), copyStore.getDataObject());
			} else {
				ApplicationLocalStore.getInstance().store(identifier, copyStore.getParameter().getName(), copyStore.getDataObject());
			}
		} else if (LocalStore.ACTION_ADD.equals(copyStore.getAction())) {
			Object o = null;
			if (Reference.SOURCE_DATASTORE_ID.equalsIgnoreCase(copyStore.getTarget())) {
				if (DataStore.findValue(dataId, copyStore.getParameter().getName()) == null) {
					DataStore.store(dataId, copyStore.getParameter().getName(), new ArrayList());
				}
				o = DataStore.getValue(dataId, copyStore.getParameter().getName());
			} else {
				if (!ApplicationLocalStore.getInstance().contains(identifier, copyStore.getParameter().getName())) {
					ApplicationLocalStore.getInstance().store(identifier, copyStore.getParameter().getName(), new ArrayList());
				}
				o = ApplicationLocalStore.getInstance().retrieve(identifier, copyStore.getParameter().getName());
			}

			if (o instanceof List) {
				if (copyStore.getDataObject() != null) {
					((List) o).add(copyStore.getDataObject());
				}
			}

		} else if (LocalStore.ACTION_DELETE.equals(copyStore.getAction())) {
			if (copyStore.getDataObject() != null) {
				Object o = null;
				if (Reference.SOURCE_DATASTORE_ID.equalsIgnoreCase(copyStore.getTarget())) {
					o = DataStore.getValue(dataId, copyStore.getParameter().getName());
				} else {
					o = ApplicationLocalStore.getInstance().retrieve(identifier, copyStore.getParameter().getName());
				}

				if (copyStore.getDataObject() != null) {
					if (o instanceof List) {
						Iterator itr = ((List) o).iterator();
						List<Integer> matches = new ArrayList<Integer>();
						int index = 0;

						while (itr.hasNext()) {
							Object itrObject = itr.next();
							if (itrObject instanceof Map) {
								Object fieldData = ((Map) itrObject).get(copyStore.getField());
								if (fieldData!=null){
									if (copyStore.getDataObject().toString().equals(fieldData.toString())) {
										matches.add(index);
									}
								}

							} else {
								if (copyStore.getDataObject().toString().equals(itrObject.toString())){
									matches.add(index);
								}
							}
							index++;

						}
						for (Integer integer : matches) {
							((List) o).remove(integer);
						}
					}
				}
			}
		}
		logger.fine("LocalStore after <store>\n"+ApplicationLocalStore.getInstance().toLogString(identifier));
		logger.fine("DataStore after <store>\n"+DataStore.toLogString(dataId));
		return false;
	}
	// CHECKSTYLE.ON: CyclomaticComplexity
}
