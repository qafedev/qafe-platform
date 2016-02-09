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
package com.qualogy.qafe.bind.business.action;

import java.util.ArrayList;
import java.util.List;

import com.qualogy.qafe.bind.Validatable;
import com.qualogy.qafe.bind.ValidationException;
import com.qualogy.qafe.bind.commons.type.In;
import com.qualogy.qafe.bind.commons.type.Out;
import com.qualogy.qafe.bind.commons.type.Parameter;
import com.qualogy.qafe.bind.commons.type.ReferencingItem;
import com.qualogy.qafe.bind.core.statement.ResultItem;
import com.qualogy.qafe.bind.domain.BaseRef;
import com.qualogy.qafe.bind.presentation.event.EventItem;

/**
 * Business logic action type
 * @author 
 *
 */
public class BusinessActionRef extends BaseRef implements BusinessActionItem, EventItem, ResultItem, Validatable, ReferencingItem {

    private static final long serialVersionUID = 5299713470229586124L;
	
	protected BusinessAction ref;
	protected List<Parameter> input;
	protected List<Parameter> output;
	protected Integer order;

	public static BusinessActionRef create(BusinessAction businessActionRef, List<Parameter> in, List<Parameter> out){
		BusinessActionRef action = new BusinessActionRef();
		action.ref = businessActionRef;
		action.input = in;
		action.output = out;
		return action;
	}
	
	public Integer getOrder() {
		return order;
	}
	
	public void setOrder(Integer order) {
		this.order = order;
	}
	
	public List<Parameter> getInput() {
		return input;
	}
	
	public List<Parameter> getOutput() {
		return output;
	}
	
	/**
     * method to add a In to a In list. The list will be
     * created when null 
     * @param typeDefinition
     * @throws IllegalArgumentException - when object param passed is null
     */
    public void add(In in) {
    	if(in==null)
    		throw new IllegalArgumentException("in cannot be null");
    	if(input==null)
    		input = new ArrayList<Parameter>();
    	
    	input.add(in);
    }

    /**
     * method to add a Out to a Out list. The list will be
     * created when null 
     * @param typeDefinition
     * @throws IllegalArgumentException - when object param passed is null
     */
    public void add(Out out) {
    	if(out==null)
    		throw new IllegalArgumentException("out cannot be null");
    	if(output==null)
    		output = new ArrayList<Parameter>();
    	
    	output.add(out);
    }

	public BusinessAction getRef() {
		return ref;
	}
	   
	public void setRef(BusinessAction ref) {
		this.ref = ref;
	}

    /*
     * (non-Javadoc)
     * @see java.lang.Object#clone()
     */
	public Object clone() throws CloneNotSupportedException{
		BusinessActionRef clone = new BusinessActionRef();
		if (this.ref != null) {
			this.ref.enrich();
			
			// TODO: Make a copy, instead of referring
			clone.ref = this.ref;
		}
		if (this.order != null) {
			clone.order = new Integer(this.order);	
		}
		if (this.input != null) {
			clone.input = new ArrayList<Parameter>();
			for (Parameter parameter : this.input) {
				Parameter newParameter = (Parameter)parameter.clone();
				clone.input.add(newParameter);
			}
		}	
		if (this.output != null) {
			clone.output = new ArrayList<Parameter>();
			for (Parameter parameter : this.output) {
				Parameter newParameter = (Parameter)parameter.clone();
				clone.output.add(newParameter);
			}
		}
		return clone;
	}

	public List<Parameter> getParameters() {
		List<Parameter> params = new ArrayList<Parameter>();
		if(input!=null)
			params.addAll(input);
		if(output!=null)
			params.addAll(output);
		return params;
	}
	
	public void replace(Parameter oldParam, Parameter newParam) {
		if(input!=null && input.remove(oldParam))
			input.add(newParam);
		
		if(output!=null && output.remove(oldParam))
			output.add(newParam);
	}

	public void validate() throws ValidationException {
		if (getRef() == null) {
			throw new ValidationException("Business Action [" + getRefId() + "] not found, possible cause is that the business-action 'ref' does not match with any existing business action in the business tier.");
		}
	}
}
