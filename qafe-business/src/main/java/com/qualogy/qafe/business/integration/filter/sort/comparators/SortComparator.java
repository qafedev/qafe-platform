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
package com.qualogy.qafe.business.integration.filter.sort.comparators;

import java.util.Comparator;
import java.util.Map;

public abstract class SortComparator implements Comparator<Object> {
	
	protected String fieldName = null;
	protected boolean sortAscending;
	
	public SortComparator() {
	}
	
	public SortComparator(String fieldName, boolean sortAscending) {
		this.fieldName = fieldName;
		this.sortAscending = sortAscending; 
	}
	
	public Integer compareNull(Object item1, Object item2) {
		if ((item1 == null) && (item2 == null)) {
    		return 0;
		} else if ((item1 == null) && (item2 != null)) {
    		return -1;
    	} else if ((item1 != null) && (item2 == null)) {
    		return 1;
    	} 
    	return null;
    }

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public boolean isSortAscending() {
		return sortAscending;
	}

	public void setSortAscending(boolean sortAscending) {
		this.sortAscending = sortAscending;
	}
	
	public static Object getValue(Object obj, String fieldName) {
		Object value = obj;
		if (obj instanceof Map) {
			Map mapEntry = (Map)obj;
			value = mapEntry.get(fieldName);
		}
		return value;
	}
}
