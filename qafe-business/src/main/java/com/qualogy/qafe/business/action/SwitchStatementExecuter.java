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
import com.qualogy.qafe.bind.core.statement.ResultItem;
import com.qualogy.qafe.bind.core.statement.SwitchResult;
import com.qualogy.qafe.bind.core.statement.SwitchStatement;
import com.qualogy.qafe.bind.item.Item;
import com.qualogy.qafe.core.datastore.DataIdentifier;
import com.qualogy.qafe.core.datastore.ParameterValueHandler;

public class SwitchStatementExecuter extends SelectionStatementExecuter {

	@Override
	protected List<ResultItem> getResultItems(ApplicationContext context, DataIdentifier dataId, Item item, Object resultValue) {
		if (item instanceof SwitchStatement) {
			SwitchStatement switchStatement = (SwitchStatement)item;
			SwitchResult switchResult = null;
			List<SwitchResult> selectionResults = switchStatement.getResults().getResults();	
			if (selectionResults != null) {
				for (SwitchResult selectionResult : selectionResults) {
					Object value = ParameterValueHandler.get(context, dataId, selectionResult.getValue());
					if ((value != null) && value.equals(resultValue)){
						switchResult = selectionResult;
						break;
					}
				}
			}
			if (switchResult != null) {
				return switchResult.getResultItems();
			}
			return switchStatement.getResults().getDefaultResult();
		}
		return null;
	}
}