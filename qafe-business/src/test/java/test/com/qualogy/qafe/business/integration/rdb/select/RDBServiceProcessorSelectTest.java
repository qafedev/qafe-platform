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
package test.com.qualogy.qafe.business.integration.rdb.select;

import java.util.List;
import java.util.Map;

import com.qualogy.qafe.business.test.BusinessActionTestCase;
import com.qualogy.qafe.core.datastore.DataStore;

public class RDBServiceProcessorSelectTest extends BusinessActionTestCase {
	
	protected String[] getSetupActions() {
		return new String[]{"setupInMemory"};
	}

	protected String[] getTearDownActions() {
		return new String[]{"teardownInMemory"};
	}
	
	// Tests against an in memory database

	public void testGetUsersOrderdByIdInMemory()throws Exception{
		manage("getUsersOrderdByIdInMemory");
		assertEquals(10, ((List)DataStore.getValue(dataId, "user")).size());
	}
	
	public void testGetUsersByIdInMemory()throws Exception{
		DataStore.store(dataId, "id", new Integer(8));
		manage("getUsersByIdInMemory");
		assertEquals("marc8", ((Map)DataStore.getValue(dataId, "outcome[0]")).get("name"));
		assertEquals(1, ((List)DataStore.getValue(dataId, "outcome")).size());
	}
	
	public void testGetUsersByIdWhileIdNotSetInMemory()throws Exception{
		DataStore.clear(dataId);
		manage("getUsersByIdInMemory");
		DataStore.toLogString(dataId);
		assertEquals(10, ((List)DataStore.getValue(dataId, "outcome")).size());
	}
	
	public void testGetUsersDataSortedInMemory()throws Exception{
		DataStore.store(dataId, "$SORT_COLUMN", "name");
		DataStore.store(dataId, "$SORT_ORDER", "desc");
		manage("getUsersDataSortedInMemory");
		assertEquals(10, ((List)DataStore.getValue(dataId, "outcome")).size());
		// paging on other databases than Oracle doesn't work
		assertEquals("8", ((Map)DataStore.getValue(dataId, "outcome[0]")).get("id"));
		assertEquals("0", ((Map)DataStore.getValue(dataId, "outcome[9]")).get("id"));
	}
	
	public String getAppContextDir() {
		return getDirBasedUponPackage();
	}
}
