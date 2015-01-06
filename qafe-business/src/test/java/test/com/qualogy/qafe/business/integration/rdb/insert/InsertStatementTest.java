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
package test.com.qualogy.qafe.business.integration.rdb.insert;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import com.qualogy.qafe.business.test.BusinessActionTestCase;
import com.qualogy.qafe.core.datastore.DataStore;

public class InsertStatementTest extends BusinessActionTestCase {
	
	/**
	 * 
  CREATE TABLE QAFE_TEST_INSERT
   (	"EMP_ID" VARCHAR2(20 BYTE) NOT NULL ENABLE, 
	"EMP_NAME" VARCHAR2(100 BYTE), 
	"DEP_ID" VARCHAR2(20 BYTE), 
	 CONSTRAINT "QAFE_TEST_INSERT_PK" PRIMARY KEY ("EMP_ID")  
   ) ;
 
	 */
	
	private String EMP_ID = "E1";
	private String EMP_NAME = "E1Name";
	private String DEP_ID = "D1";
	private Date ENTRY_DATE = new Date("01/14/2011");
	private int SALARY = 10;

	private String SECOND_EMP_ID = "E2";
	private String SECOND_EMP_NAME = "E2Name";
	private String SECOND_DEP_ID = "D2";
	private Date SECOND_ENTRY_DATE = new Date("03/04/2011");
	private int SECOND_SALARY = 544;
	
	protected String[] getSetupActions() {
		return new String[]{"setup"};
	}

	protected String[] getTearDownActions() {
		return new String[]{"teardown"};
	}

	public String getAppContextDir() {
		return getDirBasedUponPackage();
	}
	
	// passing by tablename
	// passing each columns as separate inputs. 
	public void testInsertPassingTableNameWithInputsFormService(){
		storeInputs();
		try {
			manage("testInsertPassingTableNameWithInputsFormService");
			assertEquals("1", DataStore.getValue(dataId, "result").toString());
			
			//retrieve the inserted data and test values
			manage("getData");
			ArrayList data = (ArrayList)DataStore.getValue(dataId, "result");
			assertNotNull(data);
			assertEquals(EMP_ID, ((Map)data.get(0)).get("EMP_ID"));
			assertEquals(EMP_NAME, ((Map)data.get(0)).get("EMP_NAME"));
			assertEquals(DEP_ID, ((Map)data.get(0)).get("DEP_ID"));
			
		} catch(Exception e) {
			Assert.fail("Exception occured while inserting data:" + e.getMessage());
		}
	}

	// passing only non nullable columns. 
	public void testInsertPassingTableNameWithNotNullableColumnsOnlyFormService(){
		storeInputs();
		try {
			manage("testInsertPassingTableNameWithNotNullableColumnsOnlyFormService");
			verifyPassingNonNullableColumnsOnlyRowResult();			
			
		} catch(Exception e) {
			Assert.fail("Exception occured while inserting data:" + e.getMessage());
		}
	}

	// passing columns which are nullable and Id (PK) null should fail. 
	public void testInsertPassingTableNameWithNullableColumnsOnlyFormService(){
		storeInputs();
		try {
			manage("testInsertPassingTableNameWithNullableColumnsOnlyFormService");			
			Assert.fail("It should not reach this point- there should be a cannot insert NULL into ... message");
			
		} catch(Exception e) {
			assertTrue(e.getMessage() != null);
		}
	}

	//checking primary key violation - inserting same data twice
	public void testInsertPassingTableNameWithInputsFormServicePKViolation() throws Exception {
		storeInputs();
		try {
			manage("testInsertPassingTableNameWithInputsFormService");
			assertEquals("1", DataStore.getValue(dataId, "result").toString());
			manage("testInsertPassingTableNameWithInputsFormService");
			Assert.fail("It should not reach this point- there should be unique key violation");
		} catch(Exception e) {
			assertTrue(e.getMessage() != null);
		}		
	}

	//checking primary key violation - inserting same data twice
	public void testInsertPassingTableNameWithListOfHashMapOfDuplicateInputsFormService() throws Exception {
		storeListOfMapsWithDuplicateInputs();
		try {
			manage("testInsertPassingTableNameWithListOfHashMapOfInputsFormService");
			Assert.fail("It should not reach this point- there should be unique key violation");
		} catch(Exception e) {
			assertTrue(e.getMessage() != null);
		}		
	}

	// inserting row with all data including a date and an integer
	public void testInsertPassingTableNameWithAllInputsColumnsOnlyFormService() throws Exception {
		storeInputs();
		manage("testInsertPassingTableNameWithAllInputsColumnsOnlyFormService");		
		verifyFirstRowResult();
	}

	// inserting row with all data including a date and an integer as an HashMap
	public void testInsertPassingTableNameWithMapOfInputsFormService() throws Exception {
		storeMapWithInputs();
		manage("testInsertPassingTableNameWithMapOfInputsFormService");		
		verifyFirstRowResult();
	}

	// inserting two row with all data including a date and an integer as a list of hashmaps
	public void testInsertPassingTableNameWithListOfHashMapOfInputsFormService() throws Exception {
		storeListOfMapsWithInputs();
		manage("testInsertPassingTableNameWithListOfHashMapOfInputsFormService");
		verifyDoubleRowResult();
	}
	
	// passing by sql as text
	// passing only non nullable columns. 
	public void testInsertPassingSqlAsTextWithNotNullableColumnsOnlyFormService(){
		storeNonNullableInputs();
		try {
			manage("testInsertPassingSqlAsTextWithNotNullableColumnsOnlyFormService");
			Assert.fail("It should not reach this point- No value supplied for the SQL parameter 'EMP_NAME' ...");
		} catch(Exception e) {
			assertTrue(e.getMessage() != null);
		}
	}

	// inserting row with all data including a date and an integer
	public void testInsertPassingSqlAsTextWithAllInputsColumnsOnlyFormService() throws Exception {
		storeInputs();
		manage("testInsertPassingSqlAsTextWithAllInputsColumnsOnlyFormService");		
		verifyFirstRowResult();
	}

	// inserting row with all data including a date and an integer as an HashMap
	public void testInsertPassingSqlAsTextWithMapOfInputsFormService() throws Exception {
		storeMapWithInputs();
		manage("testInsertPassingSqlAsTextWithMapOfInputsFormService");		
		verifyFirstRowResult();
	}

	// inserting two row with all data including a date and an integer as a list of hashmaps
	public void testInsertPassingSqlAsTextWithListOfHashMapOfInputsFormService() throws Exception {
		storeListOfMapsWithInputs();
		manage("testInsertPassingSqlAsTextWithListOfHashMapOfInputsFormService");		
		verifyDoubleRowResult();	
	}

	// inserting two row with all data including a date and an integer as a list of hashmaps
	public void testInsertPassingCDataSqlAsTextWithInputsFormService() throws Exception {
		storeInputs();
		manage("testInsertPassingCDataSqlAsTextWithInputsFormService");		
		verifyFirstRowResult();
	}

	// passing by sql attribute
	// passing only non nullable columns. 
	public void testInsertPassingSqlAttributeWithNotNullableColumnsOnlyFormService(){
		storeNonNullableInputs();
		try {
			manage("testInsertPassingSqlAttributeWithNotNullableColumnsOnlyFormService");
			Assert.fail("It should not reach this point- No value supplied for the SQL parameter 'EMP_NAME' ...");
		} catch(Exception e) {
			assertTrue(e.getMessage() != null);
		}	
	}
	
	// inserting row with all data including a date and an integer
	public void testInsertPassingSqlAttributeWithAllInputsColumnsOnlyFormService() throws Exception {
		storeInputs();
		manage("testInsertPassingSqlAttributeWithInputsFormService");		
		verifyFirstRowResult();
	}

	// inserting row with all data including a date and an integer as an HashMap
	public void testInsertPassingSqlAttributeWithMapOfInputsFormService() throws Exception {
		storeMapWithInputs();
		manage("testInsertPassingSqlAttributeWithMapOfInputsFormService");		
		verifyFirstRowResult();
	}

	// inserting two row with all data including a date and an integer as a list of hashmaps
	public void testInsertPassingSqlAttributeWithListOfHashMapOfInputsFormService() throws Exception {
		storeListOfMapsWithInputs();
		manage("testInsertPassingSqlAttributeWithListOfHashMapOfInputsFormService");		
		verifyDoubleRowResult();	
	}
	
	private void storeInputs() {		
		DataStore.store(dataId,"EMP_ID", EMP_ID);
		DataStore.store(dataId,"EMP_NAME", EMP_NAME);
		DataStore.store(dataId,"DEP_ID", DEP_ID);
		DataStore.store(dataId,"SALARY", SALARY);
		DataStore.store(dataId,"ENTRY_DATE", ENTRY_DATE);
	}
	
	private void storeNonNullableInputs() {
		DataStore.store(dataId,"EMP_ID", EMP_ID);
		DataStore.store(dataId,"DEP_ID", DEP_ID);
	}

	private void storeMapWithInputs() {
		HashMap<String, Object> mapRefVar = createFirstRowHashMap();
		DataStore.store(dataId, "mapRefVar", mapRefVar);
	}

	private HashMap<String, Object> createFirstRowHashMap() {
		HashMap<String, Object> mapRefVar = new HashMap<String, Object>();
		mapRefVar.put("EMP_ID", EMP_ID);
		mapRefVar.put("EMP_NAME", EMP_NAME);
		mapRefVar.put("DEP_ID", DEP_ID);
		mapRefVar.put("SALARY", SALARY);
		mapRefVar.put("ENTRY_DATE", ENTRY_DATE);
		return mapRefVar;
	}

	private void storeListOfMapsWithInputs() {
		List<HashMap<String, Object>> listRefVar = new ArrayList<HashMap<String, Object>>();
		HashMap<String, Object> hm1 = createFirstRowHashMap();
		HashMap<String, Object> hm2 = new HashMap<String, Object>();
		hm2.put("EMP_ID", SECOND_EMP_ID);
		addSecondRowDataWithoutPrimaryKey(hm2);
		listRefVar.add(hm1);
		listRefVar.add(hm2);
		DataStore.store(dataId, "listRefVar", listRefVar);
	}

	// duplicate Id (PK) in list
	private void storeListOfMapsWithDuplicateInputs() {
		List<HashMap<String, Object>> listRefVar = new ArrayList<HashMap<String, Object>>();
		HashMap<String, Object> hm1 = createFirstRowHashMap();
		HashMap<String, Object> hm2 = new HashMap<String, Object>();
		hm2.put("EMP_ID", EMP_ID);
		addSecondRowDataWithoutPrimaryKey(hm2);
		listRefVar.add(hm1);
		listRefVar.add(hm2);
		DataStore.store(dataId, "listRefVar", listRefVar);
	}

	private void addSecondRowDataWithoutPrimaryKey(HashMap<String, Object> hm2) {
		hm2.put("EMP_NAME", SECOND_EMP_NAME);
		hm2.put("DEP_ID", SECOND_DEP_ID);
		hm2.put("SALARY", SECOND_SALARY);
		hm2.put("ENTRY_DATE", SECOND_ENTRY_DATE);
	}

	private void verifyFirstRowResult() throws Exception {
		ArrayList data = verifyAndRetrieveRowResult();
		verifyFirstRowValues(data);		
	}
	
	private void verifyDoubleRowResult() throws Exception {
		ArrayList data = verifyAndRetrieveRowResult();
		verifyFirstRowValues(data);
		
		// verify second row values
		assertEquals(SECOND_EMP_ID, ((Map)data.get(1)).get("EMP_ID"));
		assertEquals(SECOND_EMP_NAME, ((Map)data.get(1)).get("EMP_NAME"));
		assertEquals(SECOND_DEP_ID, ((Map)data.get(1)).get("DEP_ID"));
		assertEquals(new BigDecimal(SECOND_SALARY), ((Map)data.get(1)).get("SALARY"));
		assertTrue(((Map)data.get(1)).get("SALARY") instanceof BigDecimal);
		assertEquals(SECOND_ENTRY_DATE, ((Map)data.get(1)).get("ENTRY_DATE"));
		assertTrue(((Map)data.get(1)).get("ENTRY_DATE") instanceof Timestamp);		
	}

	private void verifyFirstRowValues(ArrayList data) {
		assertEquals(EMP_ID, ((Map)data.get(0)).get("EMP_ID"));
		assertEquals(EMP_NAME, ((Map)data.get(0)).get("EMP_NAME"));
		assertEquals(DEP_ID, ((Map)data.get(0)).get("DEP_ID"));
		assertEquals(new BigDecimal(SALARY), ((Map)data.get(0)).get("SALARY"));
		assertTrue(((Map)data.get(0)).get("SALARY") instanceof BigDecimal);
		assertEquals(ENTRY_DATE, ((Map)data.get(0)).get("ENTRY_DATE"));
		assertTrue(((Map)data.get(0)).get("ENTRY_DATE") instanceof Timestamp);
	}
	
	private void verifyPassingNonNullableColumnsOnlyRowResult() throws Exception {
		ArrayList data = verifyAndRetrieveRowResult();
		assertEquals(EMP_ID, ((Map)data.get(0)).get("EMP_ID"));
		assertEquals(null, ((Map)data.get(0)).get("EMP_NAME"));
		assertEquals(DEP_ID, ((Map)data.get(0)).get("DEP_ID"));
	}
	
	private ArrayList verifyAndRetrieveRowResult() throws Exception {
		assertEquals("1", DataStore.getValue(dataId, "result").toString());

		//retrieve the inserted data and test values
		manage("getData");
		ArrayList data = (ArrayList)DataStore.getValue(dataId, "result");
		assertNotNull(data);
		return data;
	}
}

