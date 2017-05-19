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
package com.qualogy.qafe.bind.integration.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jibx.runtime.IUnmarshallingContext;

import com.qualogy.qafe.bind.PostBindException;
import com.qualogy.qafe.bind.PostProcessing;
import com.qualogy.qafe.bind.Validatable;
import com.qualogy.qafe.bind.ValidationException;
import com.qualogy.qafe.bind.domain.BindBase;
import com.qualogy.qafe.bind.resource.ResourceRef;

public class Service extends BindBase implements Validatable, PostProcessing{
	
	public final static String SERVICE_TYPE_WEBSERVICE = "ws";
	public final static String SERVICE_TYPE_CLASSCALL = "js";
	public final static String SERVICE_TYPE_DAOSERVICE = "dbs";
	
	protected String id;

	protected ResourceRef resourceRef;
	protected List<Method> methods = new ArrayList<Method>();

	private Map<String, Method> methodMap = null;

	private static final long serialVersionUID = 2678923352240430889L;
	
	public Service() {
		super();
	}
	public Service(String id) {
		super();
		this.id = id;
	}
	public Method getMethod(String id) {
		methodsToMap();
		
		if (methodMap == null) {
			throw new NullPointerException("no methods defined on service ["+this.id+"] while trying to retrieve method ["+id+"]");
		}
			
		return (Method) methodMap.get(id);
	}
	
	public String getId() {
		return id;
	}
	
	public List<Method> getMethods() {
		return methods;
	}
	
	/**
	 * function to setup a map of methods from the methods list
	 * @return
	 */
	public Map<String, Method> methodsToMap(){
		if(methodMap == null && methods!=null){
			for (Iterator<Method> iter = methods.iterator(); iter.hasNext();) {
				iter.next().sortInput();
			}
			
			methodMap = new HashMap<String, Method>(17);
			
			for (Method m : getMethods()) {
				String id = (m.getId() != null && m.getId().length() > 0) ? m.getId() : m.getName();
				
				if (methodMap.containsKey(id)) {
					throw new PostBindException("Service ["+this.getId()+"] already contains method with name [" + id + "] in methodMap");
				}
				methodMap.put(id, m);
			}
		}
		return methodMap;
	}
	
	/**
	 * input gets sorted if possible
	 */
	public void performPostProcessing() {
		methodsToMap();
	}
	
	public Map<String, Method> getMethodMap() {
		return methodMap;
	}

	public void setMethodMap(Map<String, Method> methodMap) {
		this.methodMap = methodMap;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void add(Method method) {
	
		methods.add(method);
	}
	
	/**
	 * 
	 */
	public void validate() throws ValidationException {
		if (getResourceRef() == null)
			throw new ValidationException("Service ["+id+"] has no resource, one resource must be supplied");
	
		if (getResourceRef().getRef() == null)
			throw new ValidationException("Service ["+id+"] specifies a non existing resource");
	}

	public ResourceRef getResourceRef() {
		return resourceRef;
	}

	public void setResourceRef(ResourceRef resourceRef) {
		this.resourceRef = resourceRef;
	}

	public void postset(IUnmarshallingContext context) {
		performPostProcessing();
	}
	
}
