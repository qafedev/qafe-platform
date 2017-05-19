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
package com.qualogy.qafe.bind.resource.query;

import java.io.Serializable;
import java.util.List;

import org.jibx.runtime.IUnmarshallingContext;

import com.qualogy.qafe.bind.PostProcessing;

public class Batch extends Query implements PostProcessing, Serializable{
	private static final long serialVersionUID = 7743618263451887030L;
	/**
	 * A list of queries to perform in batch. The queries in the list are ordered.
	 * A query in the list can be anything extending the Query class (so this includes a Batch)
	 */
	protected List<Query> queries;

	protected String id;
	
	public String getId() {
		return id;
	}
	
	public List<Query> getQueries() {
		return queries;
	}

	public void performPostProcessing() {
		//TODO: order
	}

	public void postset(IUnmarshallingContext context) {
		performPostProcessing();
	}

	public void validate() {
	}
}
