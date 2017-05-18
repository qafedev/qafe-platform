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
package com.qualogy.qafe.business.integration.filter.page;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import com.qualogy.qafe.business.integration.rdb.MetaDataRowMapper;
/**
 * Class to extract data from a resultset for paging. Use this class
 * as a last resort, because it will do a full table search (according to
 * the sql supplied) and works on the ResultSet of that query, fetching the ones
 * in a while through the set denoted bij page offset and maxrows.
 * 
 * In case of a callablestatement, paging should be either done within the call or this class,
 * there seems to be no other option to it (still investigating ScrollableResultsets) 
 * 
 * @author mvanderwurff
 */
public class ResultSetDataExtractor implements ResultSetExtractor, DataExtractor{
	
	private Page page;
	private MetaDataRowMapper rowMapper;
	
	public ResultSetDataExtractor(Page page, MetaDataRowMapper rowMapper) {
		this.page = page;
		this.rowMapper = rowMapper;
	}
	
	public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
		final List<Object> pageItems = page.getPageItems();
		int currentRow = 0;
		
		while (rs.next() && currentRow < page.getOffset() + page.getMaxRows()) {
			if (currentRow >= page.getOffset()) {
				pageItems.add(rowMapper.mapRow(rs, currentRow));
			}
			currentRow++;
		}
		return page;
	}
}
