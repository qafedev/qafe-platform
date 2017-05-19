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
package test.com.qualogy.qafe.business.integration.rdb.query;

import java.util.List;
import java.util.Map;

import org.junit.Ignore;

import com.qualogy.qafe.business.test.BusinessActionTestCase;
import com.qualogy.qafe.core.datastore.DataStore;

@Ignore
public class RDBServiceProcessorQueryTest extends BusinessActionTestCase {
	
	@Override
	protected String[] getSetupActions() {
		return new String[]{"setupInMemory"};
	}

	@Override
	protected String[] getTearDownActions() {
		return new String[]{"teardownInMemory"};
	}

	// Tests against a local Oracle database instance

	public void testGetUsersSqlAsAttribute()throws Exception{
		manage("getUsersSqlAsAttribute");
		assertEquals(10, ((List)DataStore.getValue(dataId, "outcome")).size());
		assertEquals("A", ((Map)DataStore.getValue(dataId, "outcome[0]")).get("name"));
	}

	public void testGetUsersSqlAsText()throws Exception{
		manage("getUsersSqlAsText");
		assertEquals(10, ((List)DataStore.getValue(dataId, "outcome")).size());
		assertEquals("Z", ((Map)DataStore.getValue(dataId, "outcome[8]")).get("name"));
	}
	
	public void testGetUsersSqlAsAttributeById()throws Exception{
		DataStore.store(dataId, "id", new Integer(8));
		manage("getUsersSqlAsAttributeById");
		assertEquals("marc8", ((Map)DataStore.getValue(dataId, "outcome[0]")).get("name"));
		assertEquals(1, ((List)DataStore.getValue(dataId, "outcome")).size());
	}

	public void testGetUsersSqlAsTextById()throws Exception{
		DataStore.store(dataId, "id", new Integer(8));
		manage("getUsersSqlAsTextById");
		assertEquals("marc8", ((Map)DataStore.getValue(dataId, "outcome[0]")).get("name"));
		assertEquals(1, ((List)DataStore.getValue(dataId, "outcome")).size());
	}

	public void testGetUsersSqlAsAttributeByIdWhileIdNotSet()throws Exception{
		try {
			DataStore.clear(dataId);
			manage("getUsersSqlAsAttributeById");
			DataStore.toLogString(dataId);
			assertEquals(10, ((List)DataStore.getValue(dataId, "outcome")).size());
			fail("Should fail because of : No value supplied for the SQL parameter 'id': No value registered for key 'id'");
		} catch (Exception e) {
			// should fail because of not set id value
		}
	}

	public void testGetUsersSqlAsTextByIdWhileIdNotSet()throws Exception{
		try {
			DataStore.clear(dataId);
			manage("getUsersSqlAsTextById");
			DataStore.toLogString(dataId);
			assertEquals(10, ((List)DataStore.getValue(dataId, "outcome")).size());
			fail("Should fail because of : No value supplied for the SQL parameter 'id': No value registered for key 'id'");
		} catch (Exception e) {
			// should fail because of not set id value
		}
	}

	// Tests against an in memory database

	public void testGetUsersSqlAsAttributeInMemory()throws Exception{
		manage("getUsersSqlAsAttributeInMemory");
		assertEquals(10, ((List)DataStore.getValue(dataId, "outcome")).size());
		assertEquals("A", ((Map)DataStore.getValue(dataId, "outcome[0]")).get("name"));
	}

	public void testGetUsersSqlAsTextInMemory()throws Exception{
		manage("getUsersSqlAsTextInMemory");
		assertEquals(10, ((List)DataStore.getValue(dataId, "outcome")).size());
		assertEquals("marc8", ((Map)DataStore.getValue(dataId, "outcome[8]")).get("name"));
	}
	
	public void testGetUsersSqlAsAttributeByIdInMemory()throws Exception{
		DataStore.store(dataId, "id", new Integer(8));
		manage("getUsersSqlAsAttributeByIdInMemory");
		assertEquals("marc8", ((Map)DataStore.getValue(dataId, "outcome[0]")).get("name"));
		assertEquals(1, ((List)DataStore.getValue(dataId, "outcome")).size());
	}

	public void testGetUsersSqlAsTextByIdInMemory()throws Exception{
		DataStore.store(dataId, "id", new Integer(8));
		manage("getUsersSqlAsTextByIdInMemory");
		assertEquals("marc8", ((Map)DataStore.getValue(dataId, "outcome[0]")).get("name"));
		assertEquals(1, ((List)DataStore.getValue(dataId, "outcome")).size());
	}

	public void testGetUsersSqlAsAttributeByIdWhileIdNotSetInMemory()throws Exception{
		try {
			DataStore.clear(dataId);
			manage("getUsersSqlAsAttributeByIdInMemory");
			DataStore.toLogString(dataId);
			assertEquals(10, ((List)DataStore.getValue(dataId, "outcome")).size());
			fail("Should fail because of : No value supplied for the SQL parameter 'id': No value registered for key 'id'");
		} catch (Exception e) {
			// should fail because of not set id value
		}
	}

	public void testGetUsersSqlAsTextByIdWhileIdNotSetInMemory()throws Exception{
		try {
			DataStore.clear(dataId);
			manage("getUsersSqlAsTextByIdInMemory");
			DataStore.toLogString(dataId);
			assertEquals(10, ((List)DataStore.getValue(dataId, "outcome")).size());
			fail("Should fail because of : No value supplied for the SQL parameter 'id': No value registered for key 'id'");
		} catch (Exception e) {
			// should fail because of not set id value
		}
	}
	
	@Override
	public String getAppContextDir() {
		return getDirBasedUponPackage();
	}
}
