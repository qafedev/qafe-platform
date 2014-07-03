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
package com.qualogy.qafe.bind.core.statement;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SwitchResults implements Serializable{
	 
	private static final long serialVersionUID = -7998504222271285975L;
	
	protected List<ResultItem> defaultResult;

	public List<ResultItem> getDefaultResult() {
		return defaultResult;
	}

	/**
	 * list of evaluationresults
	 */
	protected List<SwitchResult> results;
	
	public void setDefaultResult(List<ResultItem> defaultResult) {
		this.defaultResult = defaultResult;
	}
	
	public List<SwitchResult> getResults() {
		return results;
	}
	
	/**
     * method to add a StatementResult to a StatementResult list. The list will be created when null
     * 
     * @param action
     * @throws IllegalArgumentException -
     *                 when object param passed is null
     */
    public void add(SwitchResult result) {
        if (result == null)
            throw new IllegalArgumentException("result cannot be null");
        if (results == null)
        	results = new ArrayList<SwitchResult>();

        results.add(result);
    }
}
