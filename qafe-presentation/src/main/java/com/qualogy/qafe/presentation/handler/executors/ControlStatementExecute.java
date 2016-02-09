/**
 * Copyright 2008-2016 Qualogy Solutions B.V.
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
import java.util.logging.Level;
import java.util.logging.Logger;

import com.qualogy.qafe.bind.core.application.ApplicationContext;
import com.qualogy.qafe.bind.core.messages.PlaceHolder;
import com.qualogy.qafe.bind.core.statement.ControlStatement;
import com.qualogy.qafe.bind.core.statement.Iteration;
import com.qualogy.qafe.bind.core.statement.ResultItem;
import com.qualogy.qafe.bind.core.statement.SelectionStatement;
import com.qualogy.qafe.bind.presentation.event.Event;
import com.qualogy.qafe.bind.presentation.event.EventItem;
import com.qualogy.qafe.bind.presentation.event.function.BuiltInFunction;
import com.qualogy.qafe.core.datastore.ApplicationLocalStore;
import com.qualogy.qafe.core.datastore.ApplicationStoreIdentifier;
import com.qualogy.qafe.core.datastore.DataIdentifier;
import com.qualogy.qafe.core.datastore.DataStore;
import com.qualogy.qafe.core.errorhandling.ExternalException;
import com.qualogy.qafe.core.framework.presentation.EventData;
import com.qualogy.qafe.core.framework.presentation.EventHandlerException;
import com.qualogy.qafe.core.statement.ControlStatementEngine;
import com.qualogy.qafe.presentation.EventHandlerImpl;
import com.qualogy.qafe.presentation.handler.EventItemExecutor;
import com.qualogy.qafe.presentation.handler.ExecuteEventItem;
import com.qualogy.qafe.util.ExceptionHelper;

@Deprecated
public class ControlStatementExecute extends AbstractEventItemExecute implements ExecuteEventItem {

	public boolean execute(EventItem eventItem, ApplicationContext context, Event event, EventData eventData, Collection<BuiltInFunction> listToExecute, EventHandlerImpl eventHandler, DataIdentifier dataId) throws ExternalException {
		List<BuiltInFunction> builtIns = new ArrayList<BuiltInFunction>();
		ControlStatement controlStatement = (ControlStatement) eventItem;

		// Recursively process the Controlstatements.
		boolean stopProcessing = processControlStatements(context, eventData.getApplicationStoreIdentifier(), dataId, controlStatement, builtIns, eventData, event, eventHandler);
		listToExecute.addAll(builtIns);
		return stopProcessing;

	}

    // CHECKSTYLE.OFF: CyclomaticComplexity
	private boolean processControlStatements(ApplicationContext context, ApplicationStoreIdentifier storeId, DataIdentifier dataIdentifier, ControlStatement controlStatement, List<BuiltInFunction> builtIns, EventData eventData, Event event, EventHandlerImpl eventHandler) throws ExternalException {
		boolean stopLoop = false;

		try {
			if (controlStatement instanceof SelectionStatement) {
				SelectionStatement selectionStatement = (SelectionStatement) controlStatement;
				if (selectionStatement.getExpression() != null) {
					List<PlaceHolder> placeHolders = selectionStatement.getExpression().getPlaceHolders();
					if (placeHolders != null) {
						for (PlaceHolder placeHolder : placeHolders) {
							Object o = getValue(context, dataIdentifier, placeHolder, eventData);
							DataStore.store(dataIdentifier, placeHolder.getName(), o);
						}
					}
				}
			}
			Logger.getLogger(this.getClass().getName()).log(Level.INFO,"BEFORE ControlStatement Execute\n"+DataStore.toLogString(dataIdentifier));

			String localStoreId = eventData.getWindowSession() + ApplicationLocalStore.OBJECT_DELIMITER + eventData.getWindowId();
			List<ResultItem> bi = ControlStatementEngine.run(context, storeId, dataIdentifier, controlStatement, localStoreId);
			int itemCount = 0;
			String iterationVarIndexName = null;
			if (bi != null) {
				Iterator<ResultItem> itr = bi.iterator();
				if (controlStatement instanceof Iteration){
					Iteration iteration = (Iteration)controlStatement;
					if (iteration.getVarIndex() != null){
						iterationVarIndexName = iteration.getVarIndex();
						itemCount = iteration.getItemCount();
					}
				}

				int iteratorCount = 0;
				int currentItemIndex = 1;
				while (itr.hasNext() && !stopLoop) {
					if(iterationVarIndexName != null){
						if(currentItemIndex > itemCount){
							currentItemIndex = 1;
							iteratorCount++;
						}
						DataStore.store(dataIdentifier, iterationVarIndexName, iteratorCount);
					}
					ResultItem item = itr.next();
					if (item instanceof EventItem) {
						EventItem eventItem = (EventItem) item;
						if (eventItem instanceof ControlStatement) {
							ControlStatement cs = (ControlStatement) eventItem;
							processControlStatements(context, storeId, dataIdentifier, cs, builtIns, eventData, event, eventHandler);
						} else {
							try {
								stopLoop = EventItemExecutor.getInstance().execute(eventItem, context, event, eventData, builtIns, eventHandler, dataIdentifier);
							}  catch (ReturnBuiltInException e ){
								stopLoop = true;
							}
						}
					}
					currentItemIndex++;
				}
				if(iterationVarIndexName != null){
					DataStore.getDataStore(dataIdentifier).remove(iterationVarIndexName); // Clearing the stored var-index after iteration
				}
			}
		} catch (ExternalException e) {
			throw e;
		} catch (Exception e) {
			ExceptionHelper.printStackTrace(e);
			throw new EventHandlerException(e);
		}
		return stopLoop;
	}
	// CHECKSTYLE.ON: CyclomaticComplexity
}
