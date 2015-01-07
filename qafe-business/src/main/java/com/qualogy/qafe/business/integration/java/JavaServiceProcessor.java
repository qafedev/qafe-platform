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
package com.qualogy.qafe.business.integration.java;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.apache.commons.lang.math.NumberUtils;

import com.qualogy.qafe.bind.commons.type.Parameter;
import com.qualogy.qafe.bind.core.application.ApplicationContext;
import com.qualogy.qafe.bind.core.messages.HasMessage;
import com.qualogy.qafe.bind.integration.service.Method;
import com.qualogy.qafe.bind.integration.service.Service;
import com.qualogy.qafe.bind.resource.JavaResource;
import com.qualogy.qafe.business.integration.Processor;
import com.qualogy.qafe.business.integration.adapter.AdaptedToService;
import com.qualogy.qafe.business.integration.filter.Filters;
import com.qualogy.qafe.business.integration.filter.page.Page;
import com.qualogy.qafe.business.integration.filter.sort.ComparatorFactory;
import com.qualogy.qafe.business.integration.filter.sort.Sort;
import com.qualogy.qafe.business.integration.filter.sort.comparators.SortComparator;
import com.qualogy.qafe.business.resource.ResourcePool;
import com.qualogy.qafe.business.resource.java.JavaClass;
import com.qualogy.qafe.core.datastore.DataIdentifier;
import com.qualogy.qafe.core.datastore.DataStore;
import com.qualogy.qafe.core.errorhandling.ExternalException;

/**
 * actual executer of the method on the service
 * @author Marc van der Wurff
 *
 */
public class JavaServiceProcessor extends Processor {

	public final static Logger logger = Logger.getLogger(JavaServiceProcessor.class.getName()); 
	
	protected void postProcess(DataIdentifier dataId, Method method, Filters filters) throws ExternalException {
		if (filters != null) {
        	if (filters.getSort() != null) {
        		processSorting(dataId, method.getOutput(), filters.getSort());	
        	}
        	
        	if (filters.getPage() != null) {
        		processPaging(dataId, method.getOutput(), filters.getPage());	
        	}
        }
	}
	
	private void processSorting(DataIdentifier dataId, List outputMapping, Sort sort) {
    	if ((outputMapping != null) && (sort != null)) {
        	String sortColumn = sort.getColumn();
        	boolean sortAscending = Sort.ASCENDING.equalsIgnoreCase(sort.getSortOrder());
        	
        	if (sortColumn != null) {
    			for (Iterator iter1=outputMapping.iterator(); iter1.hasNext();) {
    				Parameter param = (Parameter)iter1.next();
    				if (param.getName() != null) {
    					Object value = DataStore.getValue(dataId, param.getName());
    					if (value instanceof List) {
    						List valueList = (List)value;
    						if (valueList.size() > 0) {
    							Object entry = valueList.get(0);
    							Object item = SortComparator.getValue(entry, sortColumn);
        						Comparator sortComparator = ComparatorFactory.createComparator(sortColumn, sortAscending, item);
        						if (sortComparator != null) {
        							Collections.sort(valueList, sortComparator);
        						}
        					}
    					}
    				}
    			}
    		}
    	}
    }
	
	private void processPaging(DataIdentifier dataId, List outputMapping, Page page) {
    	if ((outputMapping != null) && (page.getMaxRows() > 0)) {
            int offsetStart;
            int offsetEnd;
            int pageSize = page.getMaxRows();
            int currentPage = page.getOffset();
            
			for (Iterator iter1=outputMapping.iterator(); iter1.hasNext();) {
				Parameter param = (Parameter)iter1.next();
				if (param.getName() != null) {
					Object value = DataStore.getValue(dataId, param.getName());
					
					if (value instanceof List) {
						List valueList = (List)value;
						
						if (page.getOffset() == Integer.MAX_VALUE) {
			            	int totalCount = valueList.size();
			            	currentPage = Math.round(((float) totalCount / (float) pageSize) - 0.6f);
			                page.setOffset(currentPage);
			            }
						
						offsetStart =  Math.max(currentPage * pageSize, 0);
			            offsetEnd = offsetStart + pageSize - 1;
			            
			    		List newValueList = null;
			    		if (offsetStart < valueList.size()) {
			    			if (offsetEnd < valueList.size()) {
			    				newValueList = valueList.subList(offsetStart, offsetEnd + 1);
			    			} else {
			    				newValueList = valueList.subList(offsetStart, valueList.size());
			    			}
			    		} else {
			    			newValueList = new ArrayList();
			    		}
			    		
			    		DataStore.store(dataId, param.getName(), newValueList);

					}
				}
			}
    	}
    }
	
	protected Object execute(ApplicationContext context, Service service, Method method, Map paramsIn, Filters filters, DataIdentifier dataId) throws ExternalException {
		
		String actualMethodName = method.getName();
		
		if(paramsIn == null)
			throw new IllegalArgumentException("expecting a non-null paramsIn list");
		
		//1. create instance
		if(service.getResourceRef()==null || service.getResourceRef().getRef()==null || !(service.getResourceRef().getRef() instanceof JavaResource))
			throw new IllegalArgumentException("resource 'javaclass' for javaservice must be set");

		JavaClass resource = (JavaClass)ResourcePool.getInstance().get(context.getId(), service.getResourceRef().getRef().getId());
				
		//2. determine param types and names
		AdaptedToService methodParams[] = filterMethodParams(paramsIn);
		Class[] parameterClasses = new Class[methodParams.length];
		Object[] parameters = new Object[methodParams.length];
		
		for (int i = 0; i < methodParams.length; i++) {
			AdaptedToService adapted = methodParams[i];
			if(adapted!=null){
				parameters[i] = adapted.getValue();
				
//				if(adapted.getClazz()==null){
//					throw new UnableToProcessException("class for param [] cannot be determined"); 
//				}
				parameterClasses[i] = adapted.getClazz();
			}
		}

		//3. execute method
		return executeMethod(resource, actualMethodName, parameterClasses, parameters, context);
	}

	/**
	 * The actual method calll is processed in this method
	 * @param clazz
	 * @param parameterTypes
	 * @param instance
	 * @param parameters
	 * @param context 
	 * @throws thrown when anything goes wrong upon calling the method
	 * @return the outcome
	 */
	private Object executeMethod(JavaClass resource, String actualMethodName, Class[] parameterClasses, Object[] parameters, ApplicationContext context) throws ExternalException{
		Object instance = resource.getInstance();
		Object result = null;
		try{
			result = resource.getMethod(actualMethodName, parameterClasses).invoke(instance, parameters);
			if(instance instanceof HasMessage){
				HasMessage hasMessage = (HasMessage)instance;
				List<String> messages = hasMessage.getMessages();
				for(String message: messages){
					context.getWarningMessages().add(message);
				}
			}
		}catch(NoSuchMethodException e){
			String errorMessage = resolveErrorMessage(e);
			throw new ExternalException(errorMessage, errorMessage, e);
		}catch(InvocationTargetException e){
			String errorMessage = resolveErrorMessage(e.getTargetException());
			throw new ExternalException(e.getMessage(), errorMessage, e.getTargetException());
		}catch(IllegalAccessException e){
			String errorMessage = resolveErrorMessage(e);
			throw new ExternalException(e.getMessage(), errorMessage, e.getCause());
		}catch(Exception e){
			String errorMessage = resolveErrorMessage(e);
			throw new ExternalException(e.getMessage(), errorMessage, e.getCause());
		}
		return result;
	}

	/**
	 * method iterates through the entire input filtering the actual method
	 * params denoted by a method param order.
	 * @param paramsIn
	 * @return
	 */
	protected AdaptedToService[] filterMethodParams(Map paramsIn){
		Map<String, AdaptedToService> methodParams = new HashMap<String, AdaptedToService>();
		int highest = -1;
		for (Iterator iter = paramsIn.keySet().iterator(); iter.hasNext();) {
			String key = (String)iter.next();
			if(NumberUtils.isNumber(key)){
				int index = Integer.parseInt(key);
				if(index > highest)
					highest = index;
				AdaptedToService adapted = (AdaptedToService)paramsIn.get(key);
				methodParams.put(key, adapted);
			}else{
				logger.info("Ignoring attribute ["+key+"] for javaservice. Javaservices only accept numbered parameters");
			}
		}
		int size = highest + 1;
		AdaptedToService[] params = new AdaptedToService[size];
		for (int j = 0; j < params.length; j++) {
			params[j] = (AdaptedToService)methodParams.get("" + j);
		}
		return params; 
	}
	
	@Override
	protected String filterToString(Filters filters){
		return null;
	}
	
	private static String resolveErrorMessage(Throwable cause) {
		String errorMessage = null;
		if (cause instanceof NullPointerException) {
			StackTraceElement[] stackTraceElements = ((NullPointerException)cause).getStackTrace();
			StackTraceElement stackTraceElement = stackTraceElements[0];
			errorMessage = cause.toString() + ": " + stackTraceElement.toString();
		} else {
			errorMessage = cause.getMessage();
		}
		if (errorMessage != null) {
			errorMessage = errorMessage.trim();
		}
		return errorMessage;
	}
}
