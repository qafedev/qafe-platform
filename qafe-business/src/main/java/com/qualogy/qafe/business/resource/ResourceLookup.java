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
package com.qualogy.qafe.business.resource;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.qualogy.qafe.bind.business.action.BusinessAction;
import com.qualogy.qafe.bind.business.action.BusinessActionItem;
import com.qualogy.qafe.bind.business.action.BusinessActionRef;
import com.qualogy.qafe.bind.core.application.ApplicationIdentifier;
import com.qualogy.qafe.bind.integration.service.ServiceRef;
import com.qualogy.qafe.bind.resource.ResourceRef;
import com.qualogy.qafe.business.resource.rdb.RDBDatasource;
import com.qualogy.qafe.business.transaction.SupportsLocalTransactions;
/**
 * Convinience class to do a lookup for all datasources linked to a businessaction
 * @author mvanderwurff
 *
 */
public class ResourceLookup {

	/**
	 * Method to retrieve all (java.sql.)DataSources related to the given businessation, iow all
	 * the datasources to perform a businessaction. 
	 * @param action
	 * @return a list a java.sql.DataSources
	 */
	public static Collection getDatasourcesRelatedTo(ApplicationIdentifier applicationId, BusinessAction action){
		return getDatasources(applicationId, action).values();
	}
	/**
	 * internal recursive method called by getDatasourcesRelatedTo(BusinessAction action),
	 * searches in depth.
	 * @param action
	 * @return
	 */
	private static Map getDatasources(ApplicationIdentifier applicationId, BusinessAction action){
		Map dataSources = new HashMap();//map is used to filter duplicates
		if(action.getBusinessActionItems()!=null){
			for (Iterator actionIterator = action.getBusinessActionItems().iterator(); actionIterator.hasNext();) {
				BusinessActionItem item = (BusinessActionItem) actionIterator.next();
				if(item instanceof BusinessActionRef){
					dataSources.putAll(getDatasources(applicationId, ((BusinessActionRef)item).getRef()));
				}else if(item instanceof ServiceRef){
					ResourceRef ref = ((ServiceRef)item).getRef().getResourceRef();
					Resource resource = ResourcePool.getInstance().get(applicationId, ref);
					if(SupportsLocalTransactions.class.isInstance(resource))
						dataSources.put(resource.getName(), ((RDBDatasource)resource).getDataSource());
				}
			}
		}
		return dataSources;
	}
}
