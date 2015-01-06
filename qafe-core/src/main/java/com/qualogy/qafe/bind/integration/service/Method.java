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
package com.qualogy.qafe.bind.integration.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.qualogy.qafe.bind.commons.type.In;
import com.qualogy.qafe.bind.commons.type.Out;
import com.qualogy.qafe.bind.commons.type.Parameter;

/**
 * if id not set name will be taken as id for this method
 * name must be the actual methodname, id is for convinience
 * in case of overloading.
 * 
 * @author 
 *
 */
public class Method implements Serializable {
	
	private static final long serialVersionUID = -4937052200175474105L;
	
	protected String id;
	
	/**
	 * reference to actual method name
	 */
	protected String name;
	
	protected List<In> input;//list consists of Param instances
	protected List<Out> output;//list consists of Param instances
	
	protected Boolean scrollable;
	protected long cache = -1;
	
	/**
	 * @param scrollable the scrollable to set
	 */
	public void setScrollable(Boolean scrollable) {
		this.scrollable = scrollable;
	}
	
	public Method(String id, String name, List<In> input, List<Out> output, long cache) {
		super();
		this.id = id;
		this.name = name;
		this.input = input;
		this.output = output;
		this.cache = cache;
	}
	public Method() {
		super();
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public List<In> getInput() {
		return input;
	}
	public List<Out> getOutput() {
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
    		input = new ArrayList<In>();
    	
    	input.add(in);
    }

    public void addAllIn(List<In> ins){
        if(ins==null)
    		throw new IllegalArgumentException("in cannot be null");
    	if(input==null)
    		input = new ArrayList<In>();
        for(In in : ins){
    	    input.add(in);
        }
    }

    public void addAllOut(List<Out> outs){
        if(outs==null)
    		throw new IllegalArgumentException("in cannot be null");
    	if(output==null)
    		output = new ArrayList<Out>();
        for(Out out : outs){
    	    output.add(out);
        }
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
    		output = new ArrayList<Out>();
    	
    	output.add(out);
    }
    
    public void sortInput() {
		if(input!=null){
			Collections.sort(input);
		}
	}
    
    /**
     * 
     * @return
     */
    public boolean isScrollable(){
    	return scrollable!=null && scrollable.booleanValue();
    }
    
    public String toString(){
    	String toString = "";
    	toString += outputToString(output);
    	toString += " ";
    	toString += name;
    	toString += "";
    	toString += inputToString(input);
    	return toString;
    }
    
    private String inputToString(List<In> params){
    	String paramsStr = "(";
    	if(params!=null){
    		for (Iterator<In> iter = params.iterator(); iter.hasNext();) {
				Parameter param = (In) iter.next();
				if(!"(".equals(paramsStr))
					paramsStr += ", ";
				paramsStr += ((param.getType()!=null)?param.getType().getId():"unknown") + " " + param.getName(); 
			}
    	}
    	paramsStr+= ")";
    	return paramsStr;
    }
    
    private String outputToString(List<Out> params){
    	String paramsStr = "";
    	if(params==null || params.isEmpty()){
    		paramsStr += "void";
    	}else{
    		for (Iterator<Out> iter = params.iterator(); iter.hasNext();) {
				Parameter param = (Out) iter.next();
				if(!"".equals(paramsStr))
					paramsStr += ", ";
				paramsStr += ((param.getType()!=null)?param.getType().getId():"unknown") + " " + param.getName(); 
			}
    	}
    	paramsStr+= "";
    	return paramsStr;
    }
    
	/**
	 * method returns id if name is not set, otherwise returns method name
	 * @return
	 */
	public String getName() {
		return name==null?id:name;
	}
	
	/**
	 * 
	 */
	public void setName(String name){
		this.name = name;
	}

	public long getCache() {
		return cache;
	}

	public void setCache(long cache) {
		this.cache = cache;
	}
}

