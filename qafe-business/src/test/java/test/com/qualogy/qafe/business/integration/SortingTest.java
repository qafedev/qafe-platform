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

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.qualogy.qafe.business.resource.ResourcePool;
import com.qualogy.qafe.business.test.BusinessActionTestCase;
import com.qualogy.qafe.core.datastore.DataStore;

public class SortingTest extends BusinessActionTestCase {

	public final static Logger logger = Logger.getLogger(SortingTest.class.getName());
	
	public final static String ACTION_NAME = "testSortingAJavaResultList";
	
	/**
	 * this method tests sorting of java service result list
	 * @throws Exception 
	 */
	public void testStringASCSortingOfJavaResultList() throws Exception{
		String fieldName = "name";
		String sortOrder = "asc";
		String[][] data = createData(fieldName, sortOrder);
		
		manage(ACTION_NAME, data);
		logger.info(ResourcePool.getInstance().toLogString());
		List list = getResult();
		
		Object firstEntryValue = getFirstEntryValue(list, fieldName);
		Object lastEntryValue = getLastEntryValue(list, fieldName);
		validateSorting(firstEntryValue, "Name0", lastEntryValue, "Name9");
	}

	/**
	 * this method tests sorting of java service result list
	 * @throws Exception 
	 */
	public void testStringDESCSortingOfJavaResultList() throws Exception{
		String fieldName = "name";
		String sortOrder = "desc";
		String[][] data = createData(fieldName, sortOrder);
		
		manage(ACTION_NAME, data);
		logger.info(ResourcePool.getInstance().toLogString());
		List list = getResult();
		
		Object firstEntryValue = getFirstEntryValue(list, fieldName);
		Object lastEntryValue = getLastEntryValue(list, fieldName);
		validateSorting(firstEntryValue, "Name9", lastEntryValue, "Name0");
	}
	

	/**
	 * this method tests sorting of java service result list
	 * @throws Exception 
	 */
	public void testIntegerASCSortingOfJavaResultList() throws Exception{
		String fieldName = "age";
		String sortOrder = "asc";
		String[][] data = createData(fieldName, sortOrder);
		
		manage(ACTION_NAME, data);
		logger.info(ResourcePool.getInstance().toLogString());
		List list = getResult();
		
		Object firstEntryValue = getFirstEntryValue(list, fieldName);
		Object lastEntryValue = getLastEntryValue(list, fieldName);
		validateSorting(firstEntryValue, new Integer(0), lastEntryValue, new Integer(20));
	}

	/**
	 * this method tests sorting of java service result list
	 * @throws Exception 
	 */
	public void testIntegerDESCSortingOfJavaResultList() throws Exception{
		String fieldName = "age";
		String sortOrder = "desc";
		String[][] data = createData(fieldName, sortOrder);
		
		manage(ACTION_NAME, data);
		logger.info(ResourcePool.getInstance().toLogString());
		List list = getResult();
		
		Object firstEntryValue = getFirstEntryValue(list, fieldName);
		Object lastEntryValue = getLastEntryValue(list, fieldName);
		validateSorting(firstEntryValue, new Integer(20), lastEntryValue, new Integer(0));
	}
	
	/**
	 * this method tests sorting of java service result list
	 * @throws Exception 
	 */
	public void testBooleanASCSortingOfJavaResultList() throws Exception{
		String fieldName = "male";
		String sortOrder = "asc";
		String[][] data = createData(fieldName, sortOrder);
		
		manage(ACTION_NAME, data);
		logger.info(ResourcePool.getInstance().toLogString());
		List list = getResult();
		
		Object firstEntryValue = getFirstEntryValue(list, fieldName);
		Object lastEntryValue = getLastEntryValue(list, fieldName);
		validateSorting(firstEntryValue, Boolean.FALSE, lastEntryValue, Boolean.TRUE);
	}

	/**
	 * this method tests sorting of java service result list
	 * @throws Exception 
	 */
	public void testBooleanDESCSortingOfJavaResultList() throws Exception{
		String fieldName = "male";
		String sortOrder = "desc";
		String[][] data = createData(fieldName, sortOrder);
		
		manage(ACTION_NAME, data);
		logger.info(ResourcePool.getInstance().toLogString());
		List list = getResult();
		
		Object firstEntryValue = getFirstEntryValue(list, fieldName);
		Object lastEntryValue = getLastEntryValue(list, fieldName);
		validateSorting(firstEntryValue, Boolean.TRUE, lastEntryValue, Boolean.FALSE);
	}
	
	/**
	 * this method tests sorting of java service result list
	 * @throws Exception 
	 */
	public void testDateASCSortingOfJavaResultList() throws Exception{
		String fieldName = "birthday";
		String sortOrder = "asc";
		String[][] data = createData(fieldName, sortOrder);
		
		manage(ACTION_NAME, data);
		logger.info(ResourcePool.getInstance().toLogString());
		List list = getResult();
		
		Object firstEntryValue = getFirstEntryValue(list, fieldName);
		Object lastEntryValue = getLastEntryValue(list, fieldName);
		validateSorting(firstEntryValue, new Date(2010, 8, 1), lastEntryValue, new Date(2010, 8, 21));
	}

	/**
	 * this method tests sorting of java service result list
	 * @throws Exception 
	 */
	public void testDateDESCSortingOfJavaResultList() throws Exception{
		String fieldName = "birthday";
		String sortOrder = "desc";
		String[][] data = createData(fieldName, sortOrder);
		
		manage(ACTION_NAME, data);
		logger.info(ResourcePool.getInstance().toLogString());
		List list = getResult();
		
		Object firstEntryValue = getFirstEntryValue(list, fieldName);
		Object lastEntryValue = getLastEntryValue(list, fieldName);
		validateSorting(firstEntryValue, new Date(2010, 8, 21), lastEntryValue, new Date(2010, 8, 1));
	}

	
	/**
	 * this method tests sorting of java service result list
	 * @throws Exception 
	 */
	public void testNoSortingOfJavaResultList() throws Exception{
		String fieldName = "name";
		String[][] data = createData(null, null);
		
		manage(ACTION_NAME, data);
		logger.info(ResourcePool.getInstance().toLogString());
		List list = getResult();
		
		Object firstEntryValue = getFirstEntryValue(list, fieldName);
		Object lastEntryValue = getLastEntryValue(list, fieldName);
		validateSorting(firstEntryValue, "Name0", lastEntryValue, "Name20");
	}
	
	/**
	 * this method tests sorting of java service result array - ASC
	 */
	public void testSortingArrayResultListASC() throws Exception{
		String ACTION_NAME = "testSortingAJavaArrayResultList";
		// fieldName is not considered when sorting a list of string
		String fieldName = "";
		String sortOrder = "asc";

		
		String[][] data = new String[][]{
				{DataStore.KEY_WORD_SORT_ON_COLUMN, fieldName}, 
				{DataStore.KEY_WORD_SORT_ORDER, sortOrder}
		};
		manage(ACTION_NAME, data);
				
		List list = getResult();
		Object firstEntryValue = getFirstEntryValue(list, fieldName);
		Object lastEntryValue = getLastEntryValue(list, fieldName);
		validateSorting(firstEntryValue, "0 > astring", lastEntryValue, "9 > astring");
	}
	
	/**
	 * this method tests sorting of java service result array - DESC
	 */
	public void testSortingArrayResultListDESC() throws Exception{
		String ACTION_NAME = "testSortingAJavaArrayResultList";
		// fieldName is not considered when sorting a list of string
		String fieldName = "";
		String sortOrder = "desc";

		
		String[][] data = new String[][]{
				{DataStore.KEY_WORD_SORT_ON_COLUMN, fieldName}, 
				{DataStore.KEY_WORD_SORT_ORDER, sortOrder}
		};
		manage(ACTION_NAME, data);
				
		List list = getResult();
		Object firstEntryValue = getFirstEntryValue(list, fieldName);
		Object lastEntryValue = getLastEntryValue(list, fieldName);
		System.out.println("firstEntryValue=" + firstEntryValue + " lastEntryValue=" + lastEntryValue );
		validateSorting(firstEntryValue, "9 > astring", lastEntryValue, "0 > astring");
	}
	
	
	private String[][] createData(String fieldName, String sortOrder) {
		String[][] data = null;
		if ((fieldName != null) && (sortOrder != null)) {
			data = new String[2][2];
			data[0][0] = DataStore.KEY_WORD_SORT_ON_COLUMN;
			data[0][1] = fieldName;
			data[1][0] = DataStore.KEY_WORD_SORT_ORDER;
			data[1][1] = sortOrder;
		} else {
			data = new String[][]{};
		}
		return data;
	}
		
	private Object getFirstEntryValue(List list, String fieldName) {
		Object entryValue = null;
		Object entry = list.get(0);
		if(entry instanceof Map) {
			Map mapEntry = (Map)entry;
			entryValue = mapEntry.get(fieldName);
		} else {
			entryValue = entry;
		}
		return entryValue;
	}
	
	private Object getLastEntryValue(List list, String fieldName) {
		Object entryValue = null;
		Object entry = list.get(list.size() -1);
		if(entry instanceof Map) {
			Map mapEntry = (Map)entry;
			entryValue = mapEntry.get(fieldName);
		} else {
			entryValue = entry;
		}
		return entryValue;
	}
	
	private List getResult() {
		List list = (List)DataStore.getValue(dataId, "result");
		return list;
	}
	
	private void validateSorting(Object firstEntryValue, Object firstEntryExpectedValue, Object lastEntryValue, Object lastEntryExpectedValue) {
		assertTrue(firstEntryValue.equals(firstEntryExpectedValue));
		assertTrue(lastEntryValue.equals(lastEntryExpectedValue));
	}
	
	@Override
	public String getAppContextDir() {
		return getDirBasedUponPackage();
	}
	
	/**
	 * this method tests paging of java service result array
	 * @throws Exception 
	 */
//	public void testPagingADBResultList() throws Exception{
//		String ACTION_NAME = "testPagingADBResultList";
//		
//		int pagesize = 2;
//		int idExpected = 0;
//		for (int i = 0; i < 11; i++) {
//			String[][] data = new String[][]{
//					{DataStore.KEY_WORD_PAGE_NUMBER, "" + i}, 
//					{DataStore.KEY_WORD_PAGESIZE, "" + pagesize}
//			};
//			manage(ACTION_NAME, data);
//			
//			Object result = DataStore.getValue(dataId, "result");
//			List list = null;
//			if (result instanceof List) {
//				list = (List)result;
//			} else {
//				continue;
//			}
//			
//			if(i<9){
//				assertEquals(2, list.size());
//				assertEquals("" + idExpected, DataStore.getValue(dataId, "result[0].ID"));//nasty since i expect the list to be ordered
//				idExpected += 1;
////				idExpected += pagesize;
//			}else if(i==10){
//				assertNull(list);
//			}
//		}
//	}
}
