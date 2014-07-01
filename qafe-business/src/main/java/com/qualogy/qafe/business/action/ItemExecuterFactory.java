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

import com.qualogy.qafe.bind.business.action.BusinessActionRef;
import com.qualogy.qafe.bind.core.statement.IfStatement;
import com.qualogy.qafe.bind.core.statement.Iteration;
import com.qualogy.qafe.bind.core.statement.SwitchStatement;
import com.qualogy.qafe.bind.integration.service.ServiceRef;
import com.qualogy.qafe.bind.item.Item;

public class ItemExecuterFactory {
	/**
	 * method returns an ItemExecuter for the given item. Method throws
	 * an illegalargumentexception if item is null or no executer available
	 * for item class.
	 * @param item
	 * @return
	 */
	public static ItemExecuter create(Item item){
		if (item == null) {
			throw new IllegalArgumentException("cannot create a processor for null item");
		}	
		
		ItemExecuter processor = null;
		if (item instanceof BusinessActionRef) {
			processor = new BusinessActionRefProcessor();
		} else if (item instanceof ServiceRef) {
			processor = new ServiceRefExecuter();
		} else if (item instanceof Iteration){
			processor = new IterationExecuter();
		} else if (item instanceof IfStatement) {
			processor = new IfStatementExecuter();
		} else if (item instanceof SwitchStatement) {
			processor = new SwitchStatementExecuter();
		} else {
			throw new IllegalArgumentException("no executer implementation for item class [" + item.getClass() + "]");
		}
		return processor; 
	}
}