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
import java.util.List;
import java.util.logging.Logger;

import com.qualogy.qafe.bind.commons.type.Parameter;
import com.qualogy.qafe.bind.core.application.ApplicationContext;
import com.qualogy.qafe.bind.core.messages.PlaceHolder;
import com.qualogy.qafe.bind.core.statement.ResultItem;
import com.qualogy.qafe.bind.presentation.event.Event;
import com.qualogy.qafe.bind.presentation.event.EventItem;
import com.qualogy.qafe.bind.presentation.event.function.BuiltInFunction;
import com.qualogy.qafe.business.action.IfStatementExecuter;
import com.qualogy.qafe.core.datastore.ApplicationStoreIdentifier;
import com.qualogy.qafe.core.datastore.DataIdentifier;
import com.qualogy.qafe.core.datastore.DataStore;
import com.qualogy.qafe.core.errorhandling.ExternalException;
import com.qualogy.qafe.core.framework.presentation.EventData;
import com.qualogy.qafe.presentation.EventHandlerImpl;
import com.qualogy.qafe.presentation.handler.EventItemExecutor;
import com.qualogy.qafe.presentation.handler.ExecuteEventItem;

public class IfStatementExecute extends IfStatementExecuter implements ExecuteEventItem {
	
	public final static Logger logger = Logger.getLogger(IfStatementExecute.class.getName());

	private Event event;
	private EventData eventData;
	private EventHandlerImpl eventHandler;
	
	public boolean execute(EventItem eventItem, ApplicationContext context,	Event event, EventData eventData, Collection<BuiltInFunction> listToExecute, EventHandlerImpl eventHandler, DataIdentifier dataId) throws ExternalException {
		this.event = event;
		this.eventData = eventData;
		this.eventHandler = eventHandler;
		return execute(context, dataId, eventItem, listToExecute);
	}
	
	@Override
	protected void process(ApplicationContext context, DataIdentifier dataId, ResultItem resultItem, Collection<BuiltInFunction> listToExecute) throws ExternalException {
		if (resultItem instanceof EventItem) {
			EventItem eventItem = (EventItem)resultItem;
			EventItemExecutor.getInstance().execute(eventItem, context, event, eventData, listToExecute, eventHandler, dataId);
		}
	}
	
	@Override
	protected Object evaluateExpression(ApplicationContext context, ApplicationStoreIdentifier storeId, String localStoreId, DataIdentifier dataId, Parameter expression) {
		storeId = eventData.getApplicationStoreIdentifier();
		localStoreId = EventItemExecuteHelper.generateLocalStoreId(eventData.getWindowSession(), context, eventData.getWindowId());
		return super.evaluateExpression(context, storeId, localStoreId, dataId, expression);
	}
	
	@Override
	protected void resolvePlaceholders(ApplicationContext context, ApplicationStoreIdentifier storeId, String localStoreId, DataIdentifier dataId, Parameter expression) {
		if (expression == null) {
			return;
		}
		List<PlaceHolder> placeHolders = expression.getPlaceHolders();
		if (placeHolders != null) {
			for (PlaceHolder placeHolder : placeHolders) {
				Object value = EventItemExecuteHelper.getValue(context, dataId, placeHolder, eventData);
				DataStore.store(dataId, placeHolder.getName(), value);
			}
		}
	}
}