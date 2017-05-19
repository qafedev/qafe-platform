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
package test.com.qualogy.qafe.business.integration.rdb.delete;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Ignore;

import com.qualogy.qafe.business.test.BusinessActionTestCase;
import com.qualogy.qafe.core.datastore.DataStore;

@Ignore
public class DeleteStatementTest extends BusinessActionTestCase {
	
	/**
	 * 
  	CREATE TABLE QAFE_TEST_DELETE
   (	"EMP_ID" VARCHAR2(20 BYTE) NOT NULL ENABLE, 
	"EMP_NAME" VARCHAR2(100 BYTE), 
	"DEP_ID" VARCHAR2(20 BYTE), 
	 CONSTRAINT "QAFE_TEST_DELETE_PK" PRIMARY KEY ("EMP_ID")
  
   )  ;
 

	 */
	@Override
	protected String[] getSetupActions() {
		return new String[]{"setup"};
	}

	@Override
	protected String[] getTearDownActions() {
		return new String[]{"teardown"};
	}

	@Override
	public String getAppContextDir() {
		return getDirBasedUponPackage();
	}
		
	// specifying wrong table name - should give exception
	public void testDeletePassingWrongTableName(){
		try {
			manage("testDeletePassingWrongTableName");
			Assert.fail("It should noot reach here as the table is not existing");
		} catch(Exception e) {
			assertTrue(e.getMessage() != null);
		}
	}
	
	// specifying only table name - should delete all data from table
	public void testDeletePassingTableNameOnly(){
		try {
			manage("insertTestDataSet1"); // inserting 3 records and verifying	
			manage("getData");
			assertNotNull(DataStore.getValue(dataId, "result"));
			
			manage("testDeletePassingTableNameOnly");
			
			manage("getData");
			assertNull(DataStore.getValue(dataId, "result"));
		} catch(Exception e) {
			e.printStackTrace();
			Assert.fail("Exception occured while inserting data:" + e.getMessage());		
		}
	}
	
	// specifying table name with inputs makes where condition based on inputs
	public void testDeletePassingTableNameWithInput(){
		try {
			manage("insertTestDataSet1"); // inserting 3 records and verifying	
			manage("getData");
			assertNotNull(DataStore.getValue(dataId, "result"));
			DataStore.store(dataId, "empId","E1");
			manage("testDeletePassingTableNameWithInputs");			
			manage("getData");
			ArrayList data = (ArrayList)DataStore.getValue(dataId, "result");
			assertNotNull(data);
			for(int i=0;i<data.size();i++) {
				assertNotSame("E1", ((Map)data.get(i)).get("EMP_ID"));
			}
			
		} catch(Exception e) {
			e.printStackTrace();
			Assert.fail("Exception occured while inserting data:" + e.getMessage());		
		}
	}
	
	public void testDeletePassingTableNameWithMultipleInputs(){
		try {
			manage("insertTestDataSet1"); // inserting 3 records and verifying	
			DataStore.store(dataId, "depId","D1");
			DataStore.store(dataId, "empName","E2Name"); // based on these condition E2 should be deleted

			manage("testDeletePassingTableNameWithMultipleInputs");			
			manage("getData");
			ArrayList data = (ArrayList)DataStore.getValue(dataId, "result");
			assertNotNull(data);
			assertTrue(data.size() == 2);
			for(int i=0;i<data.size();i++) {
				assertFalse("E2".equals((((Map)data.get(i)).get("EMP_ID")).toString()));
			}
			
		} catch(Exception e) {
			e.printStackTrace();
			Assert.fail("Exception occured while inserting data:" + e.getMessage());		
		}
	}
	
	public void testWithInputMap(){
		try {
			manage("insertTestDataSet1"); // inserting 3 records	
			HashMap<String, Object> inputMap = new HashMap<String, Object>();
			inputMap.put("dep_Id","D1");
			inputMap.put("emp_Name","E2Name");
			DataStore.clear(dataId);
			DataStore.store(dataId, "inputMap",inputMap);// based on these condition E2 should be deleted

			manage("testTableNameWithInputsInMap");			
			manage("getData");
			ArrayList data = (ArrayList)DataStore.getValue(dataId, "result");
			assertNotNull(data);
			assertTrue(data.size() == 2);
			for(int i=0;i<data.size();i++) {
				assertFalse("E2".equals((((Map)data.get(i)).get("EMP_ID")).toString()));
			}
			
		} catch(Exception e) {
			e.printStackTrace();
			Assert.fail("Exception occured while inserting data:" + e.getMessage());		
		}
	}
	
	//When where is specified with Tablename inputs are used as placeholders only. 
	public void testDeleteWithWhere(){
		try {
			manage("insertTestDataSet1"); // inserting 3 records
			HashMap<String, Object> inputMap = new HashMap<String, Object>();
			inputMap.put("dep_Id","D1");
			inputMap.put("emp_Name","E2Name");
			DataStore.clear(dataId);
			DataStore.store(dataId, "inputMap",inputMap);

			manage("deleteWithWhere");			
			manage("getData");
			ArrayList data = (ArrayList)DataStore.getValue(dataId, "result");
			assertNotNull(data);
			assertTrue(data.size() == 1);
			assertEquals("E3", (((Map)data.get(0)).get("EMP_ID")).toString());
			
		} catch(Exception e) {
			e.printStackTrace();
			Assert.fail("Exception occured while inserting data:" + e.getMessage());		
		}
	}
	
	//When where is specified with sql attribute where will be ignored
	public void testDeleteWithWhereAndSQLAttribute(){
		try {
			manage("insertTestDataSet1"); // inserting 3 records
			HashMap<String, Object> inputMap = new HashMap<String, Object>();
			inputMap.put("dep_Id","D1");
			inputMap.put("emp_Name","E2Name");
			DataStore.clear(dataId);
			DataStore.store(dataId, "inputMap",inputMap);

			manage("deleteWithWhereAndSQLAttribute");			
			manage("getData");
			ArrayList data = (ArrayList)DataStore.getValue(dataId, "result");
			assertNull(data);
			
		} catch(Exception e) {
			e.printStackTrace();
			Assert.fail("Exception occured while inserting data:" + e.getMessage());		
		}
	}
	
}
