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
package test.com.qualogy.qafe.business.integration;

import java.util.List;
import java.util.logging.Logger;

import com.qualogy.qafe.business.resource.ResourcePool;
import com.qualogy.qafe.business.test.BusinessActionTestCase;
import com.qualogy.qafe.core.datastore.DataStore;

public class PagingTest extends BusinessActionTestCase {

	public final static Logger logger = Logger.getLogger(PagingTest.class.getName());
	
	/**
	 * this method tests paging of java service result list
	 * @throws Exception 
	 */
	public void testPagingAJavaResultList() throws Exception{
		// this call will return a list of 10 items
		String ACTION_NAME = "testPagingAJavaResultList";

		int pagesize = 3;
		for (int i = 0; i < 5; i++) {
			String[][] data = new String[][]{
				{DataStore.KEY_WORD_PAGE_NUMBER, "" + i}, 
				{DataStore.KEY_WORD_PAGESIZE, "" + pagesize}
			};
			manage(ACTION_NAME, data);
			logger.info(ResourcePool.getInstance().toLogString());
			List list = (List)DataStore.getValue(dataId, "result");
			if (i == 4) {
				assertEquals(0, list.size());
			} else if (i == 3) {
				assertEquals(1, list.size());
			} else {
				assertEquals(pagesize, list.size());
			}
		}
	}
	
	/**
	 * this method tests paging of java service result list with sorting ASC
	 * @throws Exception 
	 */
	public void testPagingWithSortingAJavaResultListASC() throws Exception{
		// this call will return a list of 10 items
		String ACTION_NAME = "testPagingAJavaResultList";

		int pagesize = 3;
		String[][] data = new String[][]{
			{DataStore.KEY_WORD_PAGE_NUMBER, "" + 0}, 
			{DataStore.KEY_WORD_PAGESIZE, "" + pagesize},
			{DataStore.KEY_WORD_SORT_ON_COLUMN, "name"},
			{DataStore.KEY_WORD_SORT_ORDER, "asc"}
		};
		manage(ACTION_NAME, data);
		logger.info(ResourcePool.getInstance().toLogString());
		List list = (List)DataStore.getValue(dataId, "result");
		
		String entry = (String)list.get(0);

		String startEntry = entry.substring(0, entry.indexOf(" ") ) ;
		assertEquals("0",startEntry);
		
		entry = (String)list.get(list.size()-1);
		String lastEntry = entry.substring(0, entry.indexOf(" ") ) ;
		assertEquals("2",lastEntry);
		
	}
	
	/**
	 * this method tests paging of java service result list with sorting DSC
	 * @throws Exception 
	 */
	public void testPagingWithSortingAJavaResultListDSC() throws Exception{
		// this call will return a list of 10 items
		String ACTION_NAME = "testPagingAJavaResultList";

		int pagesize = 3;
		String[][] data = new String[][]{
			{DataStore.KEY_WORD_PAGE_NUMBER, "" + 0}, 
			{DataStore.KEY_WORD_PAGESIZE, "" + pagesize},
			{DataStore.KEY_WORD_SORT_ON_COLUMN, "name"},
			{DataStore.KEY_WORD_SORT_ORDER, "desc"}
		};
		manage(ACTION_NAME, data);
		logger.info(ResourcePool.getInstance().toLogString());
		List list = (List)DataStore.getValue(dataId, "result");
		
		String entry = (String)list.get(0);

		String startEntry = entry.substring(0, entry.indexOf(" ") ) ;
		assertEquals("9",startEntry);
		
		entry = (String)list.get(list.size()-1);
		String lastEntry = entry.substring(0, entry.indexOf(" ") ) ;
		assertEquals("7",lastEntry);
		
	}
	
	/**
	 * this method tests paging of java service result array
	 */
	public void testPagingAnArrayResultList() throws Exception{
		String ACTION_NAME = "testPagingAnArrayResultList";
		
		String[][] data = new String[][]{
				{DataStore.KEY_WORD_PAGE_NUMBER, "2"}, 
				{DataStore.KEY_WORD_PAGESIZE, "2"}
		};
		manage(ACTION_NAME, data);
		
		List list = (List)DataStore.getValue(dataId, "result");
		assertEquals(2, list.size());
	}
	
	//This testcase is commnted as paging is not for DB other than Oracle
	/**
	 * this method tests paging of java service result array
	 * @throws Exception 
	 */
	public void commentedtestPagingADBResultList() throws Exception{
		// this call will return a list of 10 items
		String ACTION_NAME = "testPagingADBResultList";
		
		int pagesize = 3;
		for (int i = 1; i < 10; i++) {
			String[][] data = new String[][]{
				{DataStore.KEY_WORD_PAGE_NUMBER, "" + i}, 
				{DataStore.KEY_WORD_PAGESIZE, "" + pagesize}
			};
			if (i == 8) {
				String x = null;
			}
			manage(ACTION_NAME, data);
			logger.info(ResourcePool.getInstance().toLogString());
			List list = (List)DataStore.getValue(dataId, "result");
			
			if (i == 10) {
				assertNull(list);
				System.out.println("testPagingADBResultList 10=" + list.size());

			} else if (i == 9) {
				System.out.println("testPagingADBResultList 9=" + list.size());
//				assertEquals(1, list.size());
			} else if (i == 8) {
				System.out.println("testPagingADBResultList 8=" + list.size());
//				assertEquals(2, list.size());
			} else {
				System.out.println("testPagingADBResultList " + i + "=" + list.size());
//				assertEquals(pagesize, list.size());
			}
		}
		
	}
	
	/**
	 * this method tests that there should not be paging becasue scrollable is false
	 * @throws Exception 
	 */
	public void testADBResultListNotPagingBecauseScrollableFalse() throws Exception{
		String ACTION_NAME = "testADBResultListNotPagingBecauseScrollableFalse";
		
		String[][] data = new String[][]{
				{DataStore.KEY_WORD_PAGE_NUMBER, "2"}, 
				{DataStore.KEY_WORD_PAGESIZE, "2"}
		};
		manage(ACTION_NAME, data);
		
		List list = (List)DataStore.getValue(dataId, "result");
		assertEquals(10, list.size());//since scrolable is false
	}
	
	/**
	 * this method tests that there should not be paging becasue scrollable is false
	 * @throws Exception 
	 */
	public void testNotPagingBecauseScrollableFalse() throws Exception{
		String ACTION_NAME = "testNotPagingBecauseScrollableFalse";
		
		String[][] data = new String[][]{
				{DataStore.KEY_WORD_PAGE_NUMBER, "2"}, 
				{DataStore.KEY_WORD_PAGESIZE, "2"}
		};
		manage(ACTION_NAME, data);
		
		List list = (List)DataStore.getValue(dataId, "result");
		assertEquals(100, list.size());//since scrolable is false
	}
	
	/**
	 * this method tests that there should not be paging becasue scrollable is false
	 * @throws Exception 
	 */
	public void testNotPagingBecauseNopagesize() throws Exception{
		String ACTION_NAME = "testPagingAJavaResultList";
		
		String[][] data = new String[][]{
				{DataStore.KEY_WORD_PAGE_NUMBER, "2"}, 
		};
		manage(ACTION_NAME, data);
		
		List list = (List)DataStore.getValue(dataId, "result");
		assertEquals(10, list.size());//since scrolable is false
	}
	
	/**
	 * this method shoudl still page eventhough pagenumber is not set,
	 * default page should be 'shown'
	 * @throws Exception 
	 */
	public void testPagingNoPageNumber() throws Exception{
		String ACTION_NAME = "testPagingAJavaResultList";
		
		String[][] data = new String[][]{
				{DataStore.KEY_WORD_PAGESIZE, "5"} 
		};
		
		DataStore.store(dataId, DataStore.KEY_WORD_PAGE_NUMBER, null);
		manage(ACTION_NAME, data);
		
		List list = (List)DataStore.getValue(dataId, "result");
		assertEquals(5, list.size());
	}
	
	/**
	 * 
	 * @throws Exception 
	 */
	public void testPagingPageNumberTooHigh() throws Exception{
		String ACTION_NAME = "testPagingAJavaResultList";
		
		String[][] data = new String[][]{
				{DataStore.KEY_WORD_PAGESIZE, "1"}, 
				{DataStore.KEY_WORD_PAGE_NUMBER, "1000"}
		};
		manage(ACTION_NAME, data);
		
		List list = (List)DataStore.getValue(dataId, "result");
        assertEquals(0, list.size());
	}
	
	/**
	 * this method tests that there should not be paging becasue scrollable is false
	 * @throws Exception 
	 */
	public void testPageSize0() throws Exception{
		String ACTION_NAME = "testPagingAJavaResultList";
		
		
		String[][] data = new String[][]{
				{DataStore.KEY_WORD_PAGESIZE, "0"}, 
				{DataStore.KEY_WORD_PAGE_NUMBER, "0"}
		};
		manage(ACTION_NAME, data);
		
		List list = (List)DataStore.getValue(dataId, "result");
        assertEquals(10, list.size());
//		assertNull(list);
	}
	
	/**
	 * @throws Exception 
	 */
	public void testPagingNothingSet() throws Exception{
		String ACTION_NAME = "testPagingAJavaResultList";
		
		manage(ACTION_NAME);
		
		List list = (List)DataStore.getValue(dataId, "result");
		assertEquals(10, list.size());
	}
	
	protected String[] getSetupActions() {
		return new String[]{"dbSetup"};
	}

	protected String[] getTearDownActions() {
		return new String[]{"dbTearDown"};
	}

	public String getAppContextDir() {
		return getDirBasedUponPackage();
	}
}
