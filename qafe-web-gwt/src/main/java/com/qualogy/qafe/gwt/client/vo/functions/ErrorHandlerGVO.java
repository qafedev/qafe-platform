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
package com.qualogy.qafe.gwt.client.vo.functions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ErrorHandlerGVO extends BuiltInFunctionGVO {

	public final static String CLASS_NAME = "com.qualogy.qafe.gwt.client.vo.functions.ErrorHandlerGVO";

	public final static String FINALLY_RETHROW = "rethrow";
	
	public final static String FINALLY_SWALLOW = "swallow";
	
	private String id;

	private Integer order;
	
	private String exception;
	
	private String finalAction;
	
	private List<BuiltInFunctionGVO> eventItems = new ArrayList<BuiltInFunctionGVO>();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getOrder() {
		return order;
	}

	public void setOrder(Integer order) {
		this.order = order;
	}

	public String getException() {
		return exception;
	}

	public void setException(String exception) {
		this.exception = exception;
	}

	public String getFinalAction() {
		return finalAction;
	}

	public void setFinalAction(String finalAction) {
		this.finalAction = finalAction;
	}
	
	public Collection<BuiltInFunctionGVO> getEventItems() {
		return eventItems;
	}

	public void addEventItem(BuiltInFunctionGVO eventItem) {
		if (eventItem != null) {
			eventItems.add(eventItem);
		}
	}

	@Override
	public String getClassName() {
		return CLASS_NAME;
	}
}