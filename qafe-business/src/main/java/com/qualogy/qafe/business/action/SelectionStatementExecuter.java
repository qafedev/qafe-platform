/**
 * Copyright 2008-2017 Qualogy Solutions B.V.
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

import java.util.Collection;
import java.util.List;

import com.qualogy.qafe.bind.commons.type.Parameter;
import com.qualogy.qafe.bind.core.application.ApplicationContext;
import com.qualogy.qafe.bind.core.statement.ResultItem;
import com.qualogy.qafe.bind.core.statement.SelectionStatement;
import com.qualogy.qafe.bind.item.Item;
import com.qualogy.qafe.bind.presentation.event.function.BuiltInFunction;
import com.qualogy.qafe.core.datastore.ApplicationStoreIdentifier;
import com.qualogy.qafe.core.datastore.DataIdentifier;
import com.qualogy.qafe.core.datastore.ParameterValueHandler;
import com.qualogy.qafe.core.errorhandling.ExternalException;

public abstract class SelectionStatementExecuter extends StatementExecuter {
	
	@Override
	protected boolean execute(ApplicationContext context, DataIdentifier dataId, Item item, Collection<BuiltInFunction> listToExecute) throws ExternalException {
		if (item instanceof SelectionStatement) {
			SelectionStatement selectionStatement = (SelectionStatement)item;
			Object value = evaluateExpression(context, null, null, dataId, selectionStatement.getExpression());
			List<ResultItem> resultItems = getResultItems(context, dataId, selectionStatement, value);
			if (resultItems != null) {
				for (ResultItem resultItem : resultItems) {
					process(context, dataId, resultItem, listToExecute);
				}
			}
		}
		return false;
	}
	
	protected Object evaluateExpression(ApplicationContext context, ApplicationStoreIdentifier storeId, String localStoreId, DataIdentifier dataId, Parameter expression) {
		resolvePlaceholders(context, storeId, localStoreId, dataId, expression);
		return ParameterValueHandler.get(context, storeId, dataId, expression, localStoreId);
	}
	
	protected void resolvePlaceholders(ApplicationContext context, ApplicationStoreIdentifier storeId, String localStoreId, DataIdentifier dataId, Parameter expression) {
		// All placeholders should be placed in the pipe when calling within a business-action
	}
	
	protected abstract List<ResultItem> getResultItems(ApplicationContext context, DataIdentifier dataId, Item item, Object resultValue);
}
