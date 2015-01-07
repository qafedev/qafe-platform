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
package com.qualogy.qafe.business.integration.filter.sort;

import com.qualogy.qafe.core.datastore.DataIdentifier;

public class Sort {
	
	private String column;
	private String sortOrder;
	private DataIdentifier dataIdentifier;
	public final static String ASCENDING ="asc";
	public final static String DESCENDING ="desc";
	
	/**
     * Constructor made private so cannot instantiate directly
     */
    private Sort(){ }
    
    /**
     * A factory method to create a Sort. This method initializes:
     *  - column variable with the column name
     *  - sortOrder with order of sorting 
     * @param column
     * @param sortOrder
     * @param id 
     * @return the Sort created
     */
    public static Sort create(String column, String sortOrder, DataIdentifier id) {
    	Sort sort = new Sort();
    	sort.column = column;
    	sort.sortOrder = sortOrder;
		sort.dataIdentifier = id;
		return sort;
	}

    /**
     * @see this.create(int pagenumber, int pagesize, boolean countPages)
     * sets countPages to false
     */
    public Sort create(String column, String sortOrder) {
		return create(column, sortOrder, null);
	}
	
	public String getColumn() {
		return column;
	}
	public void setColumn(String column) {
		this.column = column;
	}
	public String getSortOrder() {
		return sortOrder;
	}
	public void setSortOrder(String sortOrder) {
		this.sortOrder = sortOrder;
	}
	public DataIdentifier getDataIdentifier() {
        return dataIdentifier;
    }
    public void setDataIdentifier(DataIdentifier dataIdentifier) {
        this.dataIdentifier = dataIdentifier;
    }
}
