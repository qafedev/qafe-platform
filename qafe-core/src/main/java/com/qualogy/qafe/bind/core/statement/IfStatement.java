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

import java.util.ArrayList;
import java.util.List;


/**
 * @see SelectionStatement
 * @author 
 */
public class IfStatement extends SelectionStatement{
	
    private static final long serialVersionUID = 4415479805346282874L;

    /**
	 * list of evaluationresults
	 */
	protected List<IfResult> results;
    
	public List<IfResult> getResults() {
		return results;
	}
	
	/**
     * method to add a StatementResult to a StatementResult list. The list will be created when null
     * 
     * @param action
     * @throws IllegalArgumentException -
     *                 when object param passed is null
     */
    public void add(IfResult result) {
        if (result == null)
            throw new IllegalArgumentException("result cannot be null");
        if (results == null)
        	results = new ArrayList<IfResult>();

        results.add(result);
    }
	
	/**
	 * @pre this statement must be validated, by validate method
	 * @param value
	 * @return
	 */
	public IfResult getResultForValue(boolean value){
		String resultValue = value? IfResult.STMT_RESULT_TRUE_VALUE : IfResult.STMT_RESULT_FALSE_VALUE;
		
		IfResult selectionResult = null;
		for (IfResult result: this.getResults()) {
			if(result.getValue().equals(resultValue)){
				selectionResult = result;
				break;
			}
		}
		return selectionResult;
	}
    
}
