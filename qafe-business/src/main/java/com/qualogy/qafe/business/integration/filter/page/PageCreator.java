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

import org.apache.commons.lang.math.NumberUtils;

import com.qualogy.qafe.core.datastore.DataIdentifier;
import com.qualogy.qafe.core.datastore.DataStore;

public class PageCreator {

	/**
	 * Method to create a page object based page data in the datastore.
	 * If no data supplied for the page, null is returned
	 * @param id
	 * @return
	 */
	public static Page create(DataIdentifier id){
		
		Page page = null;
		
		Object objPageNr = DataStore.findValue(id, DataStore.KEY_WORD_PAGE_NUMBER);
		Object objPageSize = DataStore.findValue(id, DataStore.KEY_WORD_PAGESIZE);
		
		if(objPageSize!=null){
			int pagesize = 0;
			if(objPageSize instanceof Number)
				pagesize = ((Number)objPageSize).intValue();
			else if(objPageSize instanceof String && NumberUtils.isNumber((String)objPageSize))
				pagesize = Integer.parseInt((String)objPageSize);
			
			if (pagesize > -1) {
				int pagenumber = 0;
				if(objPageNr instanceof Number)
					pagenumber = ((Number)objPageNr).intValue();
				else if(objPageNr instanceof String && NumberUtils.isNumber((String)objPageNr))
					pagenumber = Integer.parseInt((String)objPageNr);
				else if(objPageNr instanceof String && ((String)objPageNr).toUpperCase().equals(DataStore.KEY_WORD_PAGE_NUMBER_LAST))
					pagenumber = Integer.MAX_VALUE;
				boolean countPages = false;
				Object objCountPages = DataStore.findValue(id, DataStore.KEY_WORD_CALCULATEPAGESAVAILABLE);
				if(objCountPages instanceof String)
					countPages = Boolean.valueOf((String)objCountPages).booleanValue();
				else if(objCountPages instanceof Boolean)
					countPages = ((Boolean)objCountPages).booleanValue();
				
				page = Page.create(pagenumber, pagesize, countPages, id);
			}
		}
		return page;
	}
}
