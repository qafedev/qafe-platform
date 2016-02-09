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
package test.com.qualogy.qafe.business.integration.rdb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Ignore;

import com.qualogy.qafe.business.test.BusinessActionTestCase;
import com.qualogy.qafe.core.datastore.CacheHandler;
import com.qualogy.qafe.core.datastore.DataStore;

@Ignore
public class RDBServiceProcessorTest extends BusinessActionTestCase {
	
	private static final String APP_ID = "RPTest1";
	private static final String SERVICE_ID = "databaseService";
	
	
	@Override
	protected String[] getSetupActions() {
		return new String[]{"setup"};
	}

	@Override
	protected String[] getTearDownActions() {
		return new String[]{"teardown"};
	}

	public void testGetUsersOrderdById()throws Exception{
		manage("getUsersOrderdById");
		assertEquals(10, ((List)DataStore.getValue(dataId, "user")).size());
	}
	
	public void testGetUsersById()throws Exception{
		DataStore.store(dataId, "id", new Integer(8));
		manage("getUsersById");
		assertEquals("marc8", ((Map)DataStore.getValue(dataId, "outcome[0]")).get("name"));
	}
	
	public void testGetUsersByIdWhileIdNotSet()throws Exception{
		DataStore.clear(dataId);
		manage("getUsersById");
		//assertEquals("marc8", ((Map)DataStore.getValue(dataId, "outcome[0]")).get("name"));
		DataStore.toLogString(dataId);
		assertEquals(10, ((List)DataStore.getValue(dataId, "outcome")).size());
	}
	
	public void testGetUsersDataSorted()throws Exception {
		DataStore.store(dataId, "$SORT_COLUMN", "name");
		DataStore.store(dataId, "$SORT_ORDER", "desc");
		manage("getUsersDataSorted");
		assertEquals(10, ((List)DataStore.getValue(dataId, "outcome")).size());
		assertEquals("8", ((Map)DataStore.getValue(dataId, "outcome[0]")).get("id"));
		assertEquals("0", ((Map)DataStore.getValue(dataId, "outcome[9]")).get("id"));
	}
	
	public void testCacheFirstTimeCall()throws Exception {
		String methodId =  "testCache";
		String cacheKey = "RPTest2_$$_databaseService_$$_testCache_$$_justsomething";
		String cachedValue = "cachedValue";
		CacheHandler.getInstance().store(cacheKey, cachedValue);		
		manage(methodId);
		assertNotSame(cachedValue, DataStore.getValue(dataId, "outcome"));
		assertEquals("marc", ((Map)DataStore.getValue(dataId, "outcome[0]")).get("name"));
	}
	
	public void testCacheWithoutInputs()throws Exception {
		String methodId =  "testCache";
		String cacheKey = "RPTest2_$$_databaseService_$$_testCache_$$_";
		String cachedValue = "cachedValue";
		CacheHandler.getInstance().store(cacheKey, cachedValue);		
		manage(methodId);
		assertEquals(cachedValue, DataStore.getValue(dataId, "outcome"));
	}
	
	
	public void testCacheWithInputs()throws Exception {
		String methodId =  "testCache";
		String cacheKey = "RPTest2_$$_databaseService_$$_testCache_$$_id=myId_$$_";
		String cachedValue = "cachedValueWithInput";
		CacheHandler.getInstance().store(cacheKey, cachedValue);
		DataStore.store(dataId, "id", "myId");
		manage(methodId);
		assertEquals(cachedValue, DataStore.getValue(dataId, "outcome"));
		CacheHandler.getInstance().remove(cacheKey);
	}
	
	public void testCacheExpired()throws Exception {
		String methodId =  "testCache";
		String cacheKey = "RPTest2_$$_databaseService_$$_testCache_$$_id=myId_$$_";
		String cachedValue = "cachedValueWithInput";
		CacheHandler.getInstance().store(cacheKey, cachedValue);
		Thread.sleep(7000);
		DataStore.store(dataId, "id", "myId");
		manage(methodId);
		assertNotSame(cachedValue, DataStore.getValue(dataId, "outcome"));
		assertEquals("marc", ((Map)DataStore.getValue(dataId, "outcome[0]")).get("name"));
		CacheHandler.getInstance().remove(cacheKey);
	}
	
	public void testCacheWithMultipleInputs()throws Exception {
		String methodId =  "testCache";
		String cacheKey = "RPTest2_$$_databaseService_$$_testCache_$$_id=myId_$$_details=myDetails_$$_";
		String cachedValue = "cachedValueWithInput";
		CacheHandler.getInstance().store(cacheKey, cachedValue);
		DataStore.store(dataId, "id", "myId");
		DataStore.store(dataId, "details", "myDetails");
		manage(methodId);
		assertEquals(cachedValue, DataStore.getValue(dataId, "outcome"));
		
		DataStore.clear(dataId);
		DataStore.store(dataId, "id", "myId");
		DataStore.store(dataId, "name", "myDetails");
		manage(methodId);
		assertNotSame(cachedValue, DataStore.getValue(dataId, "outcome"));
		
		CacheHandler.getInstance().remove(cacheKey);
	}
	
	public void testCacheWithMapInput()throws Exception {
		String methodId =  "testCache";
		String cacheKey = "RPTest2_$$_databaseService_$$_testCache_$$_details={id=123, name=myname}_$$_";
		String cachedValue = "cachedValueWithInput";
		CacheHandler.getInstance().store(cacheKey, cachedValue);
		
		Map map = new HashMap<String, String>();
		map.put("id", "123");
		map.put("name", "myname");
		
		DataStore.store(dataId, "details", map);
		manage(methodId);
		assertEquals(cachedValue, DataStore.getValue(dataId, "outcome"));
		CacheHandler.getInstance().remove(cacheKey);
	}
	
	public void testCacheWithListOfMapInput()throws Exception {
		String methodId =  "testCache";
		String cacheKey = "RPTest2_$$_databaseService_$$_testCache_$$_details=[{id=123, name=myname}, {id=123, name=myname}]_$$_";
		String cachedValue = "cachedValueWithInput";
		CacheHandler.getInstance().store(cacheKey, cachedValue);
		List<Map> details = new ArrayList();
		Map map = new HashMap<String, String>();
		map.put("id", "123");
		map.put("name", "myname");
		Map map2 = new HashMap<String, String>();
		map2.put("id", "123");
		map2.put("name", "myname");
		details.add(map);
		details.add(map2);
		
		DataStore.store(dataId, "details", details);
		manage(methodId);
		assertEquals(cachedValue, DataStore.getValue(dataId, "outcome"));
		CacheHandler.getInstance().remove(cacheKey);
	}
	
	//no caching
	public void testCacheWithNegativeValue()throws Exception {
		String methodId =  "testCacheWithNegativeValue";
		manage(methodId);
		
		String cacheKey = "RPTest2_$$_databaseService_$$_testCacheWithNegativeValue_$$_";
		assertFalse(CacheHandler.getInstance().contains(cacheKey));	
		CacheHandler.getInstance().remove(cacheKey);
	}
	
	//unlimited caching
	public void testCacheWithZero()throws Exception {
		String methodId =  "testCacheWithZero";
		manage(methodId);
		
		String cacheKey = "RPTest2_$$_databaseService_$$_testCacheWithZero_$$_";
		Thread.sleep(8000);
		assertTrue(CacheHandler.getInstance().contains(cacheKey));
		List data = (List) CacheHandler.getInstance().retrieve(cacheKey);		
		assertEquals("marc", ((Map)data.get(0)).get("name"));
		CacheHandler.getInstance().remove(cacheKey);
	}
	
	//unlimited caching
	public void testCacheWithZeroDatStored()throws Exception {
		String methodId =  "testCacheWithZero";				
		String cacheKey = "RPTest2_$$_databaseService_$$_testCacheWithZero_$$_";
		String cachedValue = "cachedValuetest";
		CacheHandler.getInstance().store(cacheKey, cachedValue);
		
		Thread.sleep(8000);
		manage(methodId);
		
		assertEquals(cachedValue, DataStore.getValue(dataId, "outcome"));
		CacheHandler.getInstance().remove(cacheKey);
	}
	
	@Override
	public String getAppContextDir() {
		return getDirBasedUponPackage();
	}
}
