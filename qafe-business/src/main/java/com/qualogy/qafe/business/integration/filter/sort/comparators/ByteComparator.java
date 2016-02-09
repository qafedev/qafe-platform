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


public class ByteComparator extends SortComparator {

	public ByteComparator() {
	}

	public ByteComparator(String fieldName, boolean sortAscending) {
		super(fieldName, sortAscending);
	}

	public int compare(Object entry1, Object entry2) {
		Byte item1 = (Byte) getValue(entry1,fieldName);
		Byte item2 = (Byte) getValue(entry2,fieldName);
		Integer returnValue = compareNull(item1, item2);
		if (returnValue != null) {
			return returnValue;
		}
		if (sortAscending) {
			return (item1.compareTo(item2));
		} else {
			return item2.compareTo(item1);
		}
	}
}
