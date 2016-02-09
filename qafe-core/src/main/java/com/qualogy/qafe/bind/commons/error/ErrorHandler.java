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
package com.qualogy.qafe.bind.commons.error;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

import com.qualogy.qafe.bind.business.action.BusinessActionItem;
import com.qualogy.qafe.bind.core.statement.ResultItem;
import com.qualogy.qafe.bind.item.Item;
import com.qualogy.qafe.bind.presentation.event.EventItem;

public class ErrorHandler implements Serializable, BusinessActionItem, EventItem, ResultItem{
	
	/**
	 * string indicating exception should be rethrown
	 */
	public final static String FINALLY_RETHROW = "rethrow";
	
	/**
	 * string indicating exception should be swallowed
	 */
	public final static String FINALLY_SWALLOW = "swallow";
	
	/**
	 * default finally action 
	 */
	public final static String DEFAULT_FINALLY_ACTION = FINALLY_RETHROW;
	
	/**
	 * 
	 */
	protected String id;
	
	/**
	 * 
	 */
	protected Integer order;
	
	/**
	 * reference to the error this handler should respond on
	 */
	protected ServiceErrorRef errorRef;
	
	/**
	 * items to process after error occured
	 */
	protected List<Item> resultItems;
	
	/**
	 * action that should be taken after all resultitems are processed 
	 */
	protected String finalAction = DEFAULT_FINALLY_ACTION;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7034802390956818772L;
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Iterator<Item> resultItemsIterator() {
		return resultItems!=null ? resultItems.iterator() : null;
	}
	public Integer getOrder() {
		return order;
	}
	public ServiceErrorRef getErrorRef() {
		return errorRef;
	}
	public void setErrorRef(ServiceErrorRef errorRef) {
		this.errorRef = errorRef;
	}
	public String getFinalAction() {
		return finalAction;
	}
	public void setFinalAction(String finalAction) {
		this.finalAction = finalAction;
	}
	public List<Item> getResultItems() {
		return resultItems;
	}
	public void setResultItems(List<Item> resultItems) {
		this.resultItems = resultItems;
	}
}
