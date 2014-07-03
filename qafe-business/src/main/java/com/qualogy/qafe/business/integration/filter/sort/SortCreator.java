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
package com.qualogy.qafe.business.integration.filter.sort;

import com.qualogy.qafe.core.datastore.DataIdentifier;
import com.qualogy.qafe.core.datastore.DataStore;

public class SortCreator {

	/**
	 * Method to create a sort object based sort data in the datastore.
	 * If no data supplied for the page, null is returned
	 * @param id
	 * @return
	 */
	public static Sort create(DataIdentifier id){
		
		Sort sort = null;
		
		Object column = DataStore.findValue(id, DataStore.KEY_WORD_SORT_ON_COLUMN);
		Object sortOrder = DataStore.findValue(id, DataStore.KEY_WORD_SORT_ORDER);
		
		if(column != null && sortOrder != null){
			sort = Sort.create(column.toString(), sortOrder.toString(), id);
		}
		
		return sort;
	}
	
}
