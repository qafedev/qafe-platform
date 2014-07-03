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
package com.qualogy.qafe.bind.integration.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.qualogy.qafe.bind.Validatable;
import com.qualogy.qafe.bind.ValidationException;
import com.qualogy.qafe.bind.business.action.BusinessActionItem;
import com.qualogy.qafe.bind.commons.type.In;
import com.qualogy.qafe.bind.commons.type.Out;
import com.qualogy.qafe.bind.commons.type.Parameter;
import com.qualogy.qafe.bind.commons.type.ReferencingItem;
import com.qualogy.qafe.bind.core.statement.ResultItem;
import com.qualogy.qafe.bind.domain.BaseRef;

public class ServiceRef extends BaseRef implements BusinessActionItem, ResultItem, Serializable, Validatable, ReferencingItem {

	private static final long serialVersionUID = 7985389945826191059L;
	
	protected Service ref;
	protected MethodRef methodRef;
	protected List<In> input;
	protected List<Out> output;
	protected Integer order;
	
	/**
	 * postbinding convinience
	 */
	private Method method;
	
	public ServiceRef() {
	}
	
	public ServiceRef(Service ref, MethodRef methodRef, List<In> input, List<Out> output, Integer order) {
		super();
		this.ref = ref;
		this.methodRef = methodRef;
		this.input = input;
		this.output = output;
		this.order = order;
	}
	
	public List getInput() {
		return input;
	}
	
	public void setInput(List<In> input) {
		this.input = input;
	}
	
	public List getOutput() {
		return output;
	}
	
	public void setOutput(List<Out> output) {
		this.output = output;
	}
	
	public Service getRef() {
		return ref;
	}

       
	public void setRef(Service ref) {
		this.ref = ref;
	}
	
	public Integer getOrder() {
		return order;
	}
	
	public void setOrder(Integer order) {
		this.order = order;
	}
	
	public MethodRef getMethodRef() {
		return methodRef;
	}
	
	public void setMethodRef(MethodRef methodRef) {
		this.methodRef = methodRef;
	}
	
	public void setMethod(Method method) {
		this.method = method;
	}
	
	public Method getMethod() {
		if (this.method == null) {
			enrich();
		}	
		return this.method;
	}
	
	public String getId() {
		return getRefId();
	}
	
	public List<Parameter> getParameters() {
		List<Parameter> params = new ArrayList<Parameter>();
		if (input != null) {
			params.addAll(input);
		}	
		if (output != null) {
			params.addAll(output);
		}	
		return params;
	}
	
	public Object clone() throws CloneNotSupportedException{
		ServiceRef clone = new ServiceRef();
		clone.method = method;
		clone.methodRef = methodRef;
		clone.order = order;
		clone.ref = ref;
		if (input != null) {
			clone.input = new ArrayList<In>(this.input);
		}	
		if (output != null) {
			clone.output = new ArrayList<Out>(this.output);
		}	
		clone.enrich();
		return clone;
	}
	
	public void enrich(){
		Service service = getRef();
		if (service != null) {
			Method method = service.getMethod(getMethodRef().getRef());
			setMethod(method);
		}
	}
	
	public void replace(Parameter oldParam, Parameter newParam) {
		if ((oldParam instanceof In) && (input != null) && input.remove(oldParam)) {
			input.add((In)newParam);
		}	
		if ((oldParam instanceof Out) && (output!=null) && output.remove(oldParam)) {
			output.add((Out)newParam);
		}	
	}
	
	public void validate() throws ValidationException {
		Service service = getRef();
		if (service == null) {
			throw new ValidationException("Service [" + getRefId() + "] not found, check your items with a reference to a service and make sure the reference points to an existing service");
		}
		Method serviceMethod = getMethod();
		if (serviceMethod == null) {
			String methodId = getMethodRef().getRef();
			throw new ValidationException("Method [" + methodId + "] not found for service [" + getRefId() + "], check your service references and make sure the reference points to an existing method");
		}
	}
}
