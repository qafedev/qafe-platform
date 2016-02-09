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
package com.qualogy.qafe.core.statement;

import java.util.List;

import com.qualogy.qafe.bind.commons.type.Parameter;
import com.qualogy.qafe.bind.core.application.ApplicationContext;
import com.qualogy.qafe.bind.core.statement.ControlStatement;
import com.qualogy.qafe.bind.core.statement.IfResult;
import com.qualogy.qafe.bind.core.statement.IfStatement;
import com.qualogy.qafe.bind.core.statement.ResultItem;
import com.qualogy.qafe.core.datastore.ApplicationStoreIdentifier;
import com.qualogy.qafe.core.datastore.DataIdentifier;
import com.qualogy.qafe.core.datastore.ParameterValueHandler;
import com.qualogy.qafe.core.id.UniqueIdentifier;

@Deprecated
public class IfStatementExecuter extends ControlStatementExecuter{

	/**
	 * method to execute a controlstatement returning the outcome of the execution
	 * method uses {@link #evaluate(UniqueIdentifier, Case)}
	 * @return List of resultItems 
	 */
	public List<ResultItem> execute(ApplicationContext context, ApplicationStoreIdentifier storeId, DataIdentifier id, ControlStatement controlStatement, String localStoreId){
		
		if(controlStatement==null)
			throw new IllegalArgumentException("controlStatement cannot be null");
		
		IfStatement statement = (IfStatement)controlStatement;
		boolean result = evaluate(context, storeId, id, statement.getExpression(), localStoreId);
		
		IfResult stmtResult = statement.getResultForValue(result);

		return (stmtResult != null)?stmtResult.getResultItems():null;
	}
	
	public List<ResultItem> execute(ApplicationContext context, ApplicationStoreIdentifier storeId, DataIdentifier id, ControlStatement controlStatement){
		return execute(context, storeId, id, controlStatement, null);
	}
	
	/**
	 * evaluate if explicit expression is supplied
	 * @param context
	 * @param dataId
	 * @param explicitExpression
	 * @return
	 */
	private boolean evaluate(ApplicationContext context, ApplicationStoreIdentifier storeId, DataIdentifier dataId, Parameter expression, String localStoreId) {
		Object obj = ParameterValueHandler.get(context, storeId, dataId, expression, localStoreId);
		
		if(!(obj instanceof String) || (!"0".equals(obj) && !"1".equals(obj))){
			throw new IllegalArgumentException("expression for ifstatement is invalid, should result in true or false");
		}
		return "1".equals(obj);
	}
}
