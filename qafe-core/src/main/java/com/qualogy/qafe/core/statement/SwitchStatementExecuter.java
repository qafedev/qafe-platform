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
package com.qualogy.qafe.core.statement;

import java.util.List;

import com.qualogy.qafe.bind.core.application.ApplicationContext;
import com.qualogy.qafe.bind.core.statement.ControlStatement;
import com.qualogy.qafe.bind.core.statement.ResultItem;
import com.qualogy.qafe.bind.core.statement.SwitchResult;
import com.qualogy.qafe.bind.core.statement.SwitchStatement;
import com.qualogy.qafe.core.datastore.ApplicationStoreIdentifier;
import com.qualogy.qafe.core.datastore.DataIdentifier;
import com.qualogy.qafe.core.datastore.ParameterValueHandler;

@Deprecated
public class SwitchStatementExecuter extends ControlStatementExecuter {

	/**
	 * method to execute a casestatement returning the outcome of the evaluation
	 * of the switch against the possible results
	 * @return List of resultItems 
	 */
	public List<ResultItem> execute(ApplicationContext context, ApplicationStoreIdentifier storeId, DataIdentifier id, ControlStatement controlStatement, String localStoreId){
		if(controlStatement==null)
			throw new IllegalArgumentException("controlStatement cannot be null");
		
		SwitchStatement statement = (SwitchStatement)controlStatement;
		
		Object swits = ParameterValueHandler.get(context, storeId, id, statement.getExpression(), localStoreId);
		
		return match(swits, statement, context, storeId, id, localStoreId);
	}
	
	public List<ResultItem> execute(ApplicationContext context, ApplicationStoreIdentifier storeId, DataIdentifier id, ControlStatement controlStatement){
		return execute(context, storeId, id, controlStatement, null);
	}
	
	/**
	 * @pre results are validated
	 * @param swits
	 * @param results
	 * @param context
	 * @param storeId
	 * @param dataId
	 * @return
	 */
	private List<ResultItem> match(Object swits, SwitchStatement statement, ApplicationContext context, ApplicationStoreIdentifier storeId, DataIdentifier dataId, String localStoreId) {
		
		List<SwitchResult> results = statement.getResults().getResults();
		
		SwitchResult result = null; 
		if(results!=null){
			for (SwitchResult selectionResult : results) {
				
				Object value = ParameterValueHandler.get(context, storeId, dataId, selectionResult.getValue(), localStoreId);
				
				if(value!=null && value.equals(swits)){
					result = selectionResult;
					break;
				}
			}
		}
		return (result!=null)?result.getResultItems():((statement.getResults().getDefaultResult()!=null)?statement.getResults().getDefaultResult():null);
	}
	
}
