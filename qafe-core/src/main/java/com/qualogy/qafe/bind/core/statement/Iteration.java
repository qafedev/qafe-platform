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
package com.qualogy.qafe.bind.core.statement;

import java.util.Iterator;
import java.util.List;

import com.qualogy.qafe.bind.ValidationException;
import com.qualogy.qafe.bind.commons.type.Reference;


/**
 * Holder for iteration settings
 * @author 
 */
public class Iteration extends ControlStatement {

	private static final long serialVersionUID = 4415479805346282874L;

	protected int begin;
	protected int end;
	protected List<ResultItem> resultItems;
	protected int increment;
	protected String condition;
	protected Reference ref;
	protected String var;
	protected String varIndex;	
	protected int itemCount;	

	public int getBegin() {
		return begin;
	}

	public String getCondition() {
		return condition;
	}

	public int getEnd() {
		return end;
	}

	public int getIncrement() {
		return increment;
	}

	public Reference getReference() {
		return ref;
	}

	public String getVar() {
		return var;
	}

	public Iterator<ResultItem> resultItemsIterator() {
		return resultItems!=null ? resultItems.iterator() : null;
	}

	public void validate() throws ValidationException {
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

	public List<ResultItem> getResultItems() {
		return resultItems;
	}

	public void setResultItems(List<ResultItem> resultItems) {
		this.resultItems = resultItems;
	}

	public Reference getRef() {
		return ref;
	}

	public void setRef(Reference ref) {
		this.ref = ref;
	}

	public void setBegin(int begin) {
		this.begin = begin;
	}

	public void setEnd(int end) {
		this.end = end;
	}

	public void setIncrement(int increment) {
		this.increment = increment;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public void setVar(String var) {
		this.var = var;
	}
		
}