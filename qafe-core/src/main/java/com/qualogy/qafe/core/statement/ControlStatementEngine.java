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
package com.qualogy.qafe.core.statement;

import java.util.List;

import com.qualogy.qafe.bind.core.application.ApplicationContext;
import com.qualogy.qafe.bind.core.statement.ControlStatement;
import com.qualogy.qafe.bind.core.statement.IfStatement;
import com.qualogy.qafe.bind.core.statement.Iteration;
import com.qualogy.qafe.bind.core.statement.ResultItem;
import com.qualogy.qafe.bind.core.statement.SwitchStatement;
import com.qualogy.qafe.core.datastore.ApplicationStoreIdentifier;
import com.qualogy.qafe.core.datastore.DataIdentifier;

public class ControlStatementEngine {

	public static List<ResultItem> run(ApplicationContext context, DataIdentifier id, ControlStatement statement){
		return run(context, null, id, statement);
	}
	
	/**
	 * Method to run a ControlStatement. An executer for the type of
	 * statement is created and the statement will be executed by that executer
	 * @param id
	 * @param statement
	 * @return list of resultitems that result from evaluating the statement
	 */
	public static List<ResultItem> run(ApplicationContext context, ApplicationStoreIdentifier storeId, DataIdentifier id, ControlStatement statement, String localStoreId){
		ControlStatementExecuter executer = null;
		
		if(statement instanceof SwitchStatement){
			executer = new SwitchStatementExecuter();
		}else if(statement instanceof IfStatement){
			executer = new IfStatementExecuter();
		}else if(statement instanceof Iteration){
			executer = new IterationExecuter();
		}else{
			throw new IllegalArgumentException("unknown controlstatement [" + statement + "]");
		}
		
		return executer.execute(context, storeId, id, statement, localStoreId);
	}
	
	public static List<ResultItem> run(ApplicationContext context, ApplicationStoreIdentifier storeId, DataIdentifier id, ControlStatement statement){
		return run(context, storeId, id, statement, null);
	}
}
