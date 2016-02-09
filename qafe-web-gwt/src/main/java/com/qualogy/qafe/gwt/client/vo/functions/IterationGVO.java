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
package com.qualogy.qafe.gwt.client.vo.functions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.qualogy.qafe.gwt.client.vo.ui.event.ParameterGVO;

public class IterationGVO extends BuiltInFunctionGVO  {
	
	public final static String CLASS_NAME = "com.qualogy.qafe.gwt.client.vo.functions.IterationGVO";

	private int begin;
	
	private int end;
	
	private int increment;
	
	private String condition;
	
	private String var;
	
	private String varIndex;	
	
	private int itemCount;
	
	protected Integer order;
	
	private ParameterGVO reference;
    
	private List<BuiltInFunctionGVO> eventItems = new ArrayList<BuiltInFunctionGVO>();
	
    public int getBegin() {
		return begin;
	}

	public void setBegin(int begin) {
		this.begin = begin;
	}

	public int getEnd() {
		return end;
	}

	public void setEnd(int end) {
		this.end = end;
	}

	public int getIncrement() {
		return increment;
	}

	public void setIncrement(int increment) {
		this.increment = increment;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public String getVar() {
		return var;
	}

	public void setVar(String var) {
		this.var = var;
	}

	public String getVarIndex() {
		return varIndex;
	}

	public void setVarIndex(String varIndex) {
		this.varIndex = varIndex;
	}

	public int getItemCount() {
		return itemCount;
	}

	public void setItemCount(int itemCount) {
		this.itemCount = itemCount;
	}

	public Integer getOrder() {
		return order;
	}

	public void setOrder(Integer order) {
		this.order = order;
	}

	public ParameterGVO getReference() {
		return reference;
	}

	public void setReference(ParameterGVO reference) {
		this.reference = reference;
	}

	public Collection<BuiltInFunctionGVO> getEventItems() {
        return eventItems;
    }

    public void addEventItem(BuiltInFunctionGVO eventItem) {
        if (eventItem == null) {
            return;
        }
        eventItems.add(eventItem);
    }
    
	@Override
	public String getClassName() {
		return CLASS_NAME;
	}
}
