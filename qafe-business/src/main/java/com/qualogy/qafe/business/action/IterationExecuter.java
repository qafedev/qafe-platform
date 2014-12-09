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
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.apache.commons.lang.StringUtils;

import com.qualogy.qafe.bind.commons.type.Reference;
import com.qualogy.qafe.bind.core.application.ApplicationContext;
import com.qualogy.qafe.bind.core.statement.Iteration;
import com.qualogy.qafe.bind.core.statement.ResultItem;
import com.qualogy.qafe.bind.item.Item;
import com.qualogy.qafe.bind.presentation.event.function.BuiltInFunction;
import com.qualogy.qafe.core.application.MappingError;
import com.qualogy.qafe.core.datastore.DataIdentifier;
import com.qualogy.qafe.core.datastore.DataStore;
import com.qualogy.qafe.core.datastore.ParameterValueHandler;
import com.qualogy.qafe.core.errorhandling.ExternalException;

public class IterationExecuter extends StatementExecuter {

	protected final static Logger logger = Logger.getLogger(IterationExecuter.class.getName());

	private Object[] array;
	private int beginIndex;
	private int endIndex;
	private int increment;
	
	@Override
	protected boolean execute(ApplicationContext context, DataIdentifier dataId, Item item, Collection<BuiltInFunction> listToExecute) throws ExternalException {
		if (!(item instanceof Iteration)) {
			return false;
		}
		Iteration iteration = (Iteration)item;
		init(context, dataId, iteration);
		
		logger.fine("Iteration starting... with begin["+ beginIndex +"] end[" + endIndex +"] increment[" + increment + "] array[" + array + "]");
		
		if ((array == null) || (array.length == 0)) {
			return false;
		}
		List<ResultItem> resultItems = getResultItems(iteration);
		for (int i=beginIndex; i<=endIndex; i+=increment) {
			if (!StringUtils.isEmpty(iteration.getCondition())) {
				// TODO
			}
			storeData(context, dataId, iteration, i);
			for (ResultItem resultItem : resultItems) {
				process(context, dataId, resultItem, listToExecute);
			}
			clearData(context, dataId, iteration);
		}
		
		logger.fine("Iteration completed");
		
		return false;
	}
	
	private List<ResultItem> getResultItems(Iteration iteration) {
		List<ResultItem> resultItems = new ArrayList<ResultItem>();
		if (iteration != null) {
			Iterator<ResultItem> itrResultItem = iteration.resultItemsIterator();
			while (itrResultItem.hasNext()) {
				ResultItem resultItem = itrResultItem.next();
				resultItems.add(resultItem);
			}	
		}
		return resultItems;
	}
	
	private void storeData(ApplicationContext context, DataIdentifier dataId, Iteration iteration, int index) {
		String var = iteration.getVar();
		if (!StringUtils.isEmpty(var)) {
			DataStore.store(dataId, var, array[index]);
		}
		String varIndex = iteration.getVarIndex();
		if (!StringUtils.isEmpty(varIndex)) {
			DataStore.store(dataId, varIndex, index);
		}
	}
	
	private void clearData(ApplicationContext context, DataIdentifier dataId, Iteration iteration) {
		Map dataStore = DataStore.getDataStore(dataId);
		if (dataStore == null) {
			return;
		}
		String var = iteration.getVar();
		if (!StringUtils.isEmpty(var)) {
			dataStore.remove(var);
		}
		String varIndex = iteration.getVarIndex();
		if (!StringUtils.isEmpty(varIndex)) {
			dataStore.remove(varIndex);
		}
	}

	private void init(ApplicationContext context, DataIdentifier dataId, Iteration iteration) {
		array = getArray(context, dataId, iteration.getReference());
		if (array == null) {
			return;
		}
		
		beginIndex = iteration.getBegin();
		if (beginIndex == -1) {
			beginIndex = 0;
		}
		if (beginIndex < 0) {
			throw new MappingError("Cannot iterate starting at negative number");
		}
		
		endIndex = iteration.getEnd();
		if (endIndex < 0) {
			endIndex = array.length - 1;
		} else {
			endIndex = Math.min(endIndex, array.length - 1);
		}
		
		increment = iteration.getIncrement();
		if (increment == -1) {
			increment = 1;
		}
		if (increment < 1) {
			throw new MappingError("Cannot iterate increment by a number smaller than 1");
		}
	}

	private Object[] getArray(ApplicationContext context, DataIdentifier dataId, Reference reference) {
		Object data = getIterationData(context, dataId, reference);
		if (data instanceof Object[]) {
			return (Object[])data;
		}
		if (data instanceof Collection) {
			return ((Collection<?>)data).toArray();
		}
		return null;
	}
	
	protected Object getIterationData(ApplicationContext context, DataIdentifier dataId, Reference reference) {
		return ParameterValueHandler.get(context, dataId, reference);
	}
}