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
package com.qualogy.qafe.business.integration;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.qualogy.qafe.bind.commons.type.Parameter;
import com.qualogy.qafe.bind.core.application.ApplicationContext;
import com.qualogy.qafe.bind.integration.service.Method;
import com.qualogy.qafe.bind.integration.service.Service;
import com.qualogy.qafe.bind.integration.service.ServiceRef;
import com.qualogy.qafe.business.integration.adapter.AdaptedToService;
import com.qualogy.qafe.business.integration.adapter.Adapter;
import com.qualogy.qafe.business.integration.adapter.UnableToAdaptException;
import com.qualogy.qafe.business.integration.filter.Filters;
import com.qualogy.qafe.business.integration.filter.page.Page;
import com.qualogy.qafe.business.integration.filter.page.PageCreator;
import com.qualogy.qafe.business.integration.filter.sort.Sort;
import com.qualogy.qafe.business.integration.filter.sort.SortCreator;
import com.qualogy.qafe.business.resource.Resource;
import com.qualogy.qafe.business.resource.ResourcePool;
import com.qualogy.qafe.business.resource.java.JavaClass;
import com.qualogy.qafe.core.datastore.CacheHandler;
import com.qualogy.qafe.core.datastore.DataIdentifier;
import com.qualogy.qafe.core.datastore.DataStore;
import com.qualogy.qafe.core.errorhandling.ExternalException;
import com.qualogy.qafe.core.framework.business.IntegrationProcessor;


public abstract class Processor implements IntegrationProcessor {
    public final void process(ApplicationContext context,
        DataIdentifier dataId, ServiceRef serviceRef)
        throws ExternalException, UnableToAdaptException {
        Method method = serviceRef.getMethod();

        if (method == null) {
            throw new IllegalArgumentException(
                "unknown method call in service reference [" +
                serviceRef.getId() + "]");
        }

        ClassLoader classLoader = getClassLoader(context, serviceRef.getRef());

        Map<String, AdaptedToService> serviceParams = Adapter.adaptIn(classLoader,
                context, dataId, method.getInput());
        
        Filters filters = new Filters();
        Page page = null;

        if (method.isScrollable()) {
            page = PageCreator.create(dataId);
        }
        Sort sort = SortCreator.create(dataId);
        
        filters.setPage(page);
        filters.setSort(sort);        
        
        Object result = execute(context, serviceRef, method, serviceParams, filters, dataId);
        Adapter.adaptOut(dataId, result, method.getOutput());
        
        performPostProcess(dataId, method, filters);
    }
    
    private Object execute(ApplicationContext context, ServiceRef serviceRef, Method method, Map<String, AdaptedToService> paramsIn, Filters filters, DataIdentifier dataId) throws ExternalException {
    	Object result = null;
    	String cacheToken = generateCacheToken(paramsIn, filters);
    	String cacheKey = CacheHandler.getInstance().generateKey(context, serviceRef, cacheToken);
    	if (CacheHandler.getInstance().hasCache(context, serviceRef, cacheToken)) {
    		result = CacheHandler.getInstance().retrieve(cacheKey);
    	} else {
    		result = execute(context, serviceRef.getRef(), method, paramsIn, filters, dataId);
    		if (CacheHandler.getInstance().isCached(serviceRef)) {
    			CacheHandler.getInstance().store(cacheKey, result);	
    		}
    	}
    	return result;
    }
    
    private String generateCacheToken(Map<String, AdaptedToService> paramsIn, Filters filters) {
    	StringBuffer cacheToken = new StringBuffer();
    	List<Object> inputParams = resolveInputParams(paramsIn);
    	cacheToken.append(inputToString(inputParams));
    	String filterToString = filterToString(filters);
    	if(filterToString != null) {
    		cacheToken.append(filterToString);
    	}
    	return cacheToken.toString();
    }
	
	private List<Object> resolveInputParams(Map<String, AdaptedToService> paramsIn) {
    	List<Object> inputParamList = new ArrayList<Object>();
    	if (paramsIn != null) {
    		Iterator<String> itrKey = paramsIn.keySet().iterator();
    		while (itrKey.hasNext()) {
    			String key = itrKey.next();
    			AdaptedToService adaptedToService = paramsIn.get(key);
    			if(adaptedToService != null) {
    				inputParamList.add(key + "=" + adaptedToService.getValue());
    			}    			
    		}
    	}
    	return inputParamList;
    }
	
	private String inputToString(List<Object> inputParams) {
		StringBuffer sb = new StringBuffer();
		if (inputParams != null) {
			for (Object object : inputParams) {
				sb.append(object.toString());
				sb.append(CacheHandler.KEY_DELIMITER);
			}
		}
		return sb.toString();
	}
	
	protected String filterToString(Filters filters){
		StringBuilder filterString = new StringBuilder();
		if(filters != null) {
    		Page page = filters.getPage();
    		if(page != null) {    				
    			filterString.append(DataStore.KEY_WORD_PAGE_NUMBER + "=" + page.getOffset());
    			filterString.append(DataStore.KEY_WORD_PAGESIZE + "=" + page.getMaxRows());
    		}
    		Sort sort = filters.getSort();
    		if(sort != null) {    			
    			filterString.append(DataStore.KEY_WORD_SORT_ON_COLUMN + "=" + sort.getColumn());
    			filterString.append(DataStore.KEY_WORD_SORT_ORDER + "=" + sort.getSortOrder());
    		}
    	}
		return filterString.toString();
	}
    
	protected void performPostProcess(DataIdentifier dataId, Method method, Filters filters) throws ExternalException {
		postProcess(dataId, method, filters);
		if ((filters != null) && (filters.getPage() != null)) {
			
			// It could be that the method does not have output parameters,
			// even when paging is enabled
			if (method.getOutput() == null) {
				return;
			}
			
			// Store the new value of page number, so it can be saved back to the datagrid,
			// if no data is present the page number is set to null so the datagrid should take care of it
			for (Iterator iter1=method.getOutput().iterator(); iter1.hasNext();) {
				Parameter param = (Parameter) iter1.next();
				if (param.getName() != null) {
					Object value = DataStore.getValue(dataId, param.getName());

					Integer actualPageNumber = filters.getPage().getOffset();
					if ((value == null)	|| ((value instanceof List) && (((List) value).isEmpty()))) {
						actualPageNumber = null;
					}
					DataStore.store(dataId, DataStore.KEY_WORD_PAGE_NUMBER,	actualPageNumber);
				}
			}
		}
	}

	/**
	 *  Subclasses should overwrite this method for post processing (filtering)
	 */
	protected void postProcess(DataIdentifier dataId, Method method, Filters filters) throws ExternalException {
	}
	
    private ClassLoader getClassLoader(ApplicationContext context,
        Service service) {
        Resource resource = getResource(context, service);
        ClassLoader classLoader = null;

        if (resource instanceof JavaClass &&
                (((JavaClass) resource).getExternalClassLoader() != null)) {
            classLoader = ((JavaClass) resource).getExternalClassLoader();
        } else {
            classLoader = this.getClass().getClassLoader();
        }

        return classLoader;
    }

    private Resource getResource(ApplicationContext context, Service service) {
        return ResourcePool.getInstance()
                           .get(context.getId(),
            service.getResourceRef().getRef().getId());
    }

    /**
     * expecting a non-null params list
     *
     * @param context
     * @param service
     * @param method
     * @param paramsIn
     * @param filters
     * @return
     * @throws ExternalException
     */
    protected abstract Object execute(ApplicationContext context,
        Service service, Method method, Map<String, AdaptedToService> paramsIn,
        Filters filters, DataIdentifier dataId) throws ExternalException;
}
