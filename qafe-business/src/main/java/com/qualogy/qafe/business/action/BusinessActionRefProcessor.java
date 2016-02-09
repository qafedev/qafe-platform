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
package com.qualogy.qafe.business.action;

import com.qualogy.qafe.bind.business.action.BusinessActionRef;
import com.qualogy.qafe.bind.core.application.ApplicationContext;
import com.qualogy.qafe.bind.item.Item;
import com.qualogy.qafe.core.datastore.DataIdentifier;
import com.qualogy.qafe.core.errorhandling.ExternalException;

/**
 * Processor class for businessaction refs
 * @author mvanderwurff
 *
 */
public class BusinessActionRefProcessor extends ItemExecuter {
	
	public void execute(ApplicationContext context, DataIdentifier dataId, Item item) throws ExternalException {
		BusinessActionRef businessActionRef = (BusinessActionRef)item;
		processIn(context, dataId, businessActionRef.getInput());
		context.getBusinessManager().manage(context, dataId, businessActionRef.getRef(), true);
		processOut(context, dataId, businessActionRef.getOutput());
	}
}
