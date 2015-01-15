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

import java.util.Collection;
import java.util.Iterator;

import com.qualogy.qafe.bind.commons.type.Parameter;
import com.qualogy.qafe.bind.commons.type.Reference;
import com.qualogy.qafe.bind.core.application.ApplicationContext;
import com.qualogy.qafe.bind.core.messages.PlaceHolder;
import com.qualogy.qafe.bind.presentation.event.Event;
import com.qualogy.qafe.bind.presentation.event.EventItem;
import com.qualogy.qafe.bind.presentation.event.function.BuiltInFunction;
import com.qualogy.qafe.bind.presentation.event.function.LogFunction;
import com.qualogy.qafe.core.datastore.DataIdentifier;
import com.qualogy.qafe.core.datastore.DataStore;
import com.qualogy.qafe.core.framework.presentation.EventData;
import com.qualogy.qafe.presentation.EventHandlerImpl;
import com.qualogy.qafe.presentation.handler.ExecuteEventItem;

public class LogFunctionExecute extends AbstractEventItemExecute implements
		ExecuteEventItem {

	public boolean execute(EventItem eventItem, ApplicationContext context,
			Event event, EventData eventData,
			Collection<BuiltInFunction> listToExecute,
			EventHandlerImpl eventHandler, DataIdentifier dataId) {

		LogFunction origLogFunction = (LogFunction) eventItem;

		LogFunction copyLogFunction = new LogFunction();
		Parameter copyParam = null;
		if (origLogFunction.getMessage() != null) {
			Parameter origParam = origLogFunction.getMessage();
			
			try {
				copyParam = (Parameter) origParam.clone();
				copyLogFunction.setMessage(copyParam);

				/**
				 * set the placeholders value (if src="component") to the
				 * default of pipe);
				 */

				if (copyParam.hasPlaceHolders()) {
					Iterator<PlaceHolder> itr = copyParam.getPlaceHolders()
							.iterator();
					while (itr.hasNext()) {
						PlaceHolder placeHolder = (PlaceHolder) itr.next();
						if (placeHolder.getRef() != null) {
							if (Reference.SOURCE_COMPONENT_ID
									.equals(placeHolder.getRef().getSource())) {
								// put the value in the datastore
								Object o = getValue(context, dataId,
										placeHolder, eventData);
								DataStore.store(dataId, placeHolder.getRef()
										.getRootRef(), o);
								// modify the src to get the data in the
								// datastore
								placeHolder.getRef().setSource(
										Reference.SOURCE_DATASTORE_ID);
							}
						}
					}
				}
			} catch (CloneNotSupportedException e) {
				throw new RuntimeException(
						"Cloneable interface on Parameter has changed", e);
			}
		}

		copyLogFunction.setMessageData(getValue(context, dataId, copyParam, eventData));
		copyLogFunction.setDebug(Boolean.FALSE);
		copyLogFunction.setDelay(origLogFunction.getDelay());
		copyLogFunction.setStyle(origLogFunction.getStyle());
		copyLogFunction.setStyleClass(origLogFunction.getStyleClass());
		listToExecute.add(copyLogFunction);
		return false;
	}
}
