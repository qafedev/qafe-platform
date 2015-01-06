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
import java.util.ArrayList;
import java.util.List;

import com.qualogy.qafe.bind.Validatable;
import com.qualogy.qafe.bind.ValidationException;

public class IfResult implements Serializable, Validatable{
	 
	private static final long serialVersionUID = -2921987491706094492L;

	/**
     * Predefined true Value string
     */
    public final static String STMT_RESULT_TRUE_VALUE = Boolean.TRUE.toString();
    /**
     * Predefined false Value string
     */
    public final static String STMT_RESULT_FALSE_VALUE = Boolean.FALSE.toString();
	
	/**
     * holder for the the result value
     */
    protected String value;
    
    /**
     * holder for the items
     */
    protected List<ResultItem> resultItems;
    
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
    
    /**
     * method to add a ResultItem to a ResultItem list. The list will be created when null
     * 
     * @param action
     * @throws IllegalArgumentException -
     *                 when object param passed is null
     */
    public void add(ResultItem item) {
        if (item == null)
            throw new IllegalArgumentException("item cannot be null");
        if (resultItems == null)
            resultItems = new ArrayList<ResultItem>();

        resultItems.add(item);
    }

    public List<ResultItem> getResultItems() {
        return resultItems;
    }

	public void validate() throws ValidationException {
		if(this.getValue()==null)
			throw new ValidationException("Statementresult value cannot be null for (tip: search on result without an attribute value)");
		if(!STMT_RESULT_TRUE_VALUE.equals(this.getValue()) && !STMT_RESULT_FALSE_VALUE.equals(this.getValue())){
			throw new ValidationException("'if' statement result value must be either true or false");
		}
	}
}
