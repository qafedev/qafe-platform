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

import java.util.List;

import com.qualogy.qafe.bind.core.application.ApplicationContext;
import com.qualogy.qafe.bind.core.statement.IfResult;
import com.qualogy.qafe.bind.core.statement.IfStatement;
import com.qualogy.qafe.bind.core.statement.ResultItem;
import com.qualogy.qafe.bind.item.Item;
import com.qualogy.qafe.core.datastore.DataIdentifier;

public class IfStatementExecuter extends SelectionStatementExecuter {

	@Override
	protected List<ResultItem> getResultItems(ApplicationContext context, DataIdentifier dataId, Item item, Object resultValue) {
		if (item instanceof IfStatement) {
			IfStatement ifStatement = (IfStatement)item;
			boolean value = evaluate(resultValue); 
			IfResult ifResult = ifStatement.getResultForValue(value);
			if (ifResult != null) {
				return ifResult.getResultItems();
			}
		}
		return null;
	}
	
	private boolean evaluate(Object value) {
		if (!(value instanceof String) || (!"0".equals(value) && !"1".equals(value))) {
			throw new IllegalArgumentException("Expression for if statement is invalid, should result in true or false");
		}
		return "1".equals(value);
	}
}