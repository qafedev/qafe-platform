/**
 * Copyright 2008-2015 Qualogy Solutions B.V.
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
package com.qualogy.qafe.bind.core.application;

import java.util.List;

import com.qualogy.qafe.bind.business.action.BusinessAction;
import com.qualogy.qafe.bind.business.transaction.TransactionBehaviour;
import com.qualogy.qafe.bind.core.application.io.ResourceMerger;
import com.qualogy.qafe.bind.domain.ApplicationMapping;
import com.qualogy.qafe.core.application.LoadFailedException;

public class ApplicationContextPostProcessHelper {
	
	public static void postProcess(ApplicationContext context){
		//1. configuration
		context.setDefaults();
		boolean validating = Boolean.valueOf(context.getConfigurationItem(Configuration.QAFE_XML_VALIDATION)).booleanValue();
		
		//2. application mappings
		context.setApplicationMapping((ApplicationMapping)ResourceMerger.merge(ApplicationMapping.class, context.concreteApplicationMappings, context.mappingFileLocations, context.name, context.root, context.autoScanRoot, context.recursiveScan, validating));
		
		if(context.getApplicationMapping()==null)
			throw new LoadFailedException(context.name, "Both application-mapping-file and application-mapping were left null, tried to read from root [" + context.root + "] to find mapping files but didn't find any, please specify one or more application-mapping-file and or application-mapping in the app context configuration");
		
		//3. transaction behaviour
		if(context.globalTransactions!=null){
			List<BusinessAction> businessActions = context.getApplicationMapping().getBusinessTier().getBusinessActions();
			for (BusinessAction ba: businessActions) {
				if(ba.getTransactionBehaviour()==null){
					TransactionBehaviour tb = context.globalTransactions.getBehaviourForBA(ba.getId());
					ba.setTransactionBehaviour(tb);
				}
			}
		}
		
		//4. messages
		context.messages = ResourceMerger.merge(context.messages, context.name, context.root, validating);
	}
}
