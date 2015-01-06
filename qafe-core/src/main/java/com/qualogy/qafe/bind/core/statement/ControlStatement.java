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

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.qualogy.qafe.bind.business.action.BusinessActionItem;
import com.qualogy.qafe.bind.presentation.event.EventItem;
/**
 * Superclass for statements
 * @author 
 *
 */
@SuppressWarnings("serial")
public abstract class ControlStatement implements EventItem, BusinessActionItem, ResultItem, Serializable {
	protected Integer order;

	public ControlStatement() {
		super();
	}

	public Integer getOrder() {
		return order;
	}

	public void setOrder(Integer order) {
		this.order = order;
	}
	public String toString(){
		return ToStringBuilder.reflectionToString(this);
	}
}
