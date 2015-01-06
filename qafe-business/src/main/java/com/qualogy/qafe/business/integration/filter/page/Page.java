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
package com.qualogy.qafe.business.integration.filter.page;

import java.util.ArrayList;
import java.util.List;

import com.qualogy.qafe.core.datastore.DataIdentifier;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 
 * @author mvanderwurff
 */
public class Page {
	private int offset;
	private int maxRows;
    
	/**
     * boolean indicating if the ttl amount of pages should be counted
     * and returned 
     */
    private boolean countPages;

    private DataIdentifier dataIdentifier;
    
    private List<Object> pageItems = new ArrayList<Object>();

    /**
     * Constructor made private so cannot instantiate directly
     */
    private Page(){ }
    
    /**
     * A factory method to create a page. This method initializes:
     *  - maxRows variable with the pagesize
     *  - offset with pagenumber
     *  - pageItems with a new ArrayList with initialcapacity the pagesize 
     * @param pagenumber
     * @param pagesize
     * @param countPages - boolean indicating if the ttl amount 
     * 					   of pages available should be counted
     * @return the Page created
     */
    public static Page create(int pagenumber, int pagesize, boolean countPages, DataIdentifier id) {
		Page page = new Page();
		page.maxRows = pagesize;
		page.offset = pagenumber;
		page.pageItems = new ArrayList<Object>(pagesize);
		page.countPages = countPages;
        page.dataIdentifier = id;
		return page;
	}

    /**
     * @see this.create(int pagenumber, int pagesize, boolean countPages)
     * sets countPages to false
     */
    public Page create(int pagenumber, int pagesize) {
		return create(pagenumber, pagesize, false, null);
	}

    
	public void setOffset(int offset) {
        this.offset = offset;
    }

    public void add(Object pageItems) {
        this.pageItems.add(pageItems);
    }

    public int getOffset() {
        return offset;
    }

    public int getMaxRows() {
		return maxRows;
	}
    
    public List<Object> getPageItems() {
        return pageItems;
    }

	public void setPageItems(List<Object> pageItems) {
		this.pageItems = pageItems;
	}

	public boolean countPages() {
		return countPages;
	}
	public String toString(){
		return ToStringBuilder.reflectionToString(this);
	}

    public DataIdentifier getDataIdentifier() {
        return dataIdentifier;
    }

    public void setDataIdentifier(DataIdentifier dataIdentifier) {
        this.dataIdentifier = dataIdentifier;
    }
}