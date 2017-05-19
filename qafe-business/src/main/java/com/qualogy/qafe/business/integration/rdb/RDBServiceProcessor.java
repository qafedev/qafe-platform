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
package com.qualogy.qafe.business.integration.rdb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.qualogy.qafe.bind.commons.type.Out;
import com.qualogy.qafe.bind.core.application.ApplicationContext;
import com.qualogy.qafe.bind.integration.service.Method;
import com.qualogy.qafe.bind.integration.service.Service;
import com.qualogy.qafe.bind.resource.DatasourceBindResource;
import com.qualogy.qafe.bind.resource.query.Call;
import com.qualogy.qafe.bind.resource.query.Query;
import com.qualogy.qafe.business.integration.Processor;
import com.qualogy.qafe.business.integration.adapter.AdaptedToService;
import com.qualogy.qafe.business.integration.filter.Filters;
import com.qualogy.qafe.business.resource.ResourcePool;
import com.qualogy.qafe.business.resource.rdb.RDBDatasource;
import com.qualogy.qafe.core.datastore.DataIdentifier;
import com.qualogy.qafe.core.datastore.DataMap;
import com.qualogy.qafe.core.errorhandling.ExternalException;

public class RDBServiceProcessor extends Processor {
	
    /**
     * Method to create a set of keys for output, filtered
     * from the outputparamlist. These keys can be used
     * for sql enhancements f.i. if useOut is set.
     *
     * @param params
     * @return
     */
    private Set outputParametersToSet(List params) {
        Set<String> set = new HashSet<String>();

        if (params != null) {
            for (Iterator iter = params.iterator(); iter.hasNext();) {
                Out out = (Out) iter.next();

                if (out.getRef() != null) {
                    set.add(out.getRef().getRootRef());
                }
            }
        }
        return set;
    }

    /**
     * Processor execute method which determines the datasource to use for
     * the execute of this method. Based upon the methodname the query is
     * looked up in the datasource wrapper
     */
    @Override
    protected Object execute(ApplicationContext context, Service service, Method method, Map<String, AdaptedToService> paramsIn, Filters filters, DataIdentifier dataId) throws ExternalException {
        String queryName = method.getName();
        if (queryName == null) {
            throw new IllegalArgumentException("queryName cannot be null");
        }

        if ((service.getResourceRef() == null) || (service.getResourceRef().getRef() == null) || !(service.getResourceRef().getRef() instanceof DatasourceBindResource)) {
            throw new IllegalArgumentException("resource for datasourceservice must be set");
        }

        String dsRef = service.getResourceRef().getRef().getId(); //expecting just one
        if (dsRef == null) {
            throw new IllegalArgumentException("datasourceRef cannot be null");
        }

        RDBDatasource ds = (RDBDatasource) ResourcePool.getInstance().get(context.getId(), dsRef);
        if (ds == null) {
            throw new NullPointerException("database resource is null");
        }
        
        Query query = ds.get(queryName);
        if (query == null) {
            throw new IllegalArgumentException("query not found for key [" + queryName + "]");
        }
        
        new QafeDataSource(ds, dataId);
        
        // Getting rid of the maps types in the input parameters,
    	// the input parameters can contain multiple records for processing
        Object result = null;
    	List<Map<String, AdaptedToService>> newParamsIn = checkParamsIn(query, paramsIn);
        if (newParamsIn.size() == 0) {
        	newParamsIn.add(new HashMap<String, AdaptedToService>());
        }
        for (Map<String, AdaptedToService> dataHashMap : newParamsIn) {
	        removeInternalKeys(dataHashMap);
	        result = DAODelegate.delegate(ds, query, method, dataHashMap, outputParametersToSet(method.getOutput()), filters, dataId);
        }
        return result;
    }

    private void removeInternalKeys(Map<String, AdaptedToService> paramsIn) {
    	if (paramsIn != null) {
    		if (paramsIn.containsKey(DataMap.ROW_NUMBER)){
    			paramsIn.remove(DataMap.ROW_NUMBER);
	        }
    	}
    }
    
    private List<Map<String, AdaptedToService>> checkParamsIn(Query query, Map<String, AdaptedToService> paramsIn) {
    	List<Map<String, AdaptedToService>> newParamsIn = new ArrayList<Map<String, AdaptedToService>>();
    	
    	// In case of Call the input can be an Object and the user is passing a map from UI, so it should not split the inputs.
    	if(query instanceof Call) {
    		newParamsIn.add(paramsIn);
    		return newParamsIn;
    	}
    	
    	if (paramsIn != null) {
    		Map<String, AdaptedToService> simpleData = new HashMap<String, AdaptedToService>();
            Iterator<String> keys = paramsIn.keySet().iterator();
            while (keys.hasNext()) {
                String key = keys.next();
                AdaptedToService adaptedToService = paramsIn.get(key);
                if (adaptedToService != null) {
                	if (adaptedToService.getValue() instanceof Map) {
                		Map map = (Map) adaptedToService.getValue();
                		populateFromMap(map, newParamsIn);
                		continue;
                	} else if (adaptedToService.getValue() instanceof List){
                		List list = (List)adaptedToService.getValue();
                		for (int i=0; i<list.size(); i++) {
                			populateFromMap((Map)list.get(i), newParamsIn);
                		}
                		continue;
                	}
                } 
                simpleData.put(key, adaptedToService);
            }
            if (!simpleData.isEmpty()){
        		newParamsIn.add(simpleData);
            }
        }
        return newParamsIn;
    }

	private void populateFromMap(Map map, List<Map<String, AdaptedToService>> newParamsIn) {
		Map<String, AdaptedToService> data = new HashMap<String, AdaptedToService>();
		Iterator itr = map.keySet().iterator();
		while (itr.hasNext()) {
		    Object key = itr.next();
		    Object value = map.get(key);
		    AdaptedToService adaptedToService = null;
		    if (value != null) {
		    	adaptedToService = AdaptedToService.create(value);
		    }
		    data.put(key.toString(), adaptedToService);
		}
		newParamsIn.add(data);
	}
}
