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
package com.qualogy.qafe.business.integration.java.spring;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.qualogy.qafe.bind.core.application.ApplicationContext;
import com.qualogy.qafe.bind.core.messages.HasMessage;
import com.qualogy.qafe.bind.integration.service.Method;
import com.qualogy.qafe.bind.integration.service.Service;
import com.qualogy.qafe.bind.resource.SpringBeanResource;
import com.qualogy.qafe.business.integration.adapter.AdaptedToService;
import com.qualogy.qafe.business.integration.filter.Filters;
import com.qualogy.qafe.business.integration.java.JavaServiceProcessor;
import com.qualogy.qafe.business.resource.ResourcePool;
import com.qualogy.qafe.business.resource.java.spring.SpringContext;
import com.qualogy.qafe.core.datastore.DataIdentifier;
import com.qualogy.qafe.core.errorhandling.ExternalException;
import com.qualogy.qafe.core.framework.business.UnableToProcessException;

public class SpringServiceProcessor extends JavaServiceProcessor {

	public final static Logger logger = Logger.getLogger(SpringServiceProcessor.class.getName());
	
	
	@Override
	protected Object execute(ApplicationContext context, Service service, Method method, Map paramsIn, Filters filters, DataIdentifier dataId) throws ExternalException {
		
		if (paramsIn == null) {
			throw new IllegalArgumentException("expecting a non-null paramsIn list");
		}
		
		//1. get instance
		if ((service.getResourceRef() == null) || (service.getResourceRef().getRef() == null) || !(service.getResourceRef().getRef() instanceof SpringBeanResource)) {
			throw new IllegalArgumentException("resource 'spring' must be set");
		}
		
		String beanRef = service.getResourceRef().getRef().getId();
		String methodName = method.getName();
		
		SpringContext resource = (SpringContext)ResourcePool.getInstance().get(context.getId(), service.getResourceRef().getRef().getId());
				
		//2. determine param types and names
		AdaptedToService methodParams[] = filterMethodParams(paramsIn);
		Class[] parameterClasses = new Class[methodParams.length];
		Object[] parameters = new Object[methodParams.length];
		for (int i=0; i<methodParams.length; i++) {
			AdaptedToService adapted = methodParams[i];
			if (adapted != null) {
				parameters[i] = adapted.getValue();
				parameterClasses[i] = adapted.getClazz();
			}
		}

		//3. execute method
		return executeMethod(resource, beanRef, methodName, parameterClasses, parameters, context);
	}
	
	private Object executeMethod(SpringContext resource, String beanRef, String methodName, Class[] parameterClasses, Object[] parameters, ApplicationContext context) throws ExternalException {
		Object result = null;
		try {
			Object instance = resource.getBean(beanRef);
			result = resource.getMethod(instance, methodName, parameterClasses).invoke(instance, parameters);
			if (instance instanceof HasMessage){
				HasMessage hasMessage = (HasMessage)instance;
				List<String> messages = hasMessage.getMessages();
				for(String message: messages){
					context.getWarningMessages().add(message);
				}
			}
		} catch(NoSuchMethodException e) {
			throw new UnableToProcessException(e);
		} catch(InvocationTargetException e) {
			throw new ExternalException(e.getCause());
		} catch(IllegalAccessException e) {
			throw new ExternalException(e.getCause());
		}
		return result;
	}

}
