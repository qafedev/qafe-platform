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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.qualogy.qafe.bind.core.application.ApplicationContext;
import com.qualogy.qafe.bind.core.statement.ResultItem;
import com.qualogy.qafe.bind.item.Item;
import com.qualogy.qafe.bind.presentation.event.function.BuiltInFunction;
import com.qualogy.qafe.core.datastore.DataIdentifier;
import com.qualogy.qafe.core.errorhandling.ExternalException;

public abstract class StatementExecuter extends ItemExecuter {

	@Override
	public final void execute(ApplicationContext context, DataIdentifier dataId, Item item) throws ExternalException {
		execute(context, dataId, item, null);
	}
	
	protected void process(ApplicationContext context, DataIdentifier dataId, ResultItem resultItem, Collection<BuiltInFunction> listToExecute) throws ExternalException {
		if (resultItem == null) {
			return;
		}
		List<ResultItem> resultItems = new ArrayList<ResultItem>();
		resultItems.add(resultItem);
		ItemExecuter.execute(context, dataId, resultItems);
	}
	
	protected abstract boolean execute(ApplicationContext context, DataIdentifier dataId, Item item, Collection<BuiltInFunction> listToExecute) throws ExternalException;
}