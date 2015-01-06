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
package test.com.qualogy.qafe.business;

import java.util.ArrayList;

import com.qualogy.qafe.business.test.BusinessActionTestCase;
import com.qualogy.qafe.core.datastore.DataStore;

public class BusinessActionManagerIntegrationBindingTest extends BusinessActionTestCase{

	public final static String CONFIG_LOCATION = "samples/businessactionmanager";
	
	protected String[] getSetupActions() {
		return new String[]{"setup"};
	}

	protected String[] getTearDownActions() {
		return new String[]{"teardown"};
	}

	public void testPrintCar()throws Exception{
		manage("printCar");
		assertEquals("jaja", DataStore.getValue(dataId, "success"));
	}
	public void testTransaction()throws Exception{
		manage("localTransaction");
	}
	
	/**
	 * 
	 */
	public void testLocalTransactionWithDuplicate()throws Exception{
		try{
			manage("insertuseralreadyexists");
			fail("exception expected since duplicate creation taken place");
		}catch(Exception e){
			//expected
		}
		
		//second one fails but first one is not in transaction!
		DataStore.store(dataId, "id", "10000");
		manage("testSelectSQLAsAttribute");
		ArrayList vfn = (ArrayList)DataStore.getValue(dataId, "db_value_for_name");
		assertNotNull(vfn);
		assertEquals("name", vfn.get(0));
//		if(!"name".equals(DataStore.getValue(id, "db_value_for_name")));
//			fail("name not found");
	}
	
	/**
	 * method checks if transaction is taken over two separate businessactions
	 * @throws Exception
	 */
	public void testJavaServiceThrowingAfterDBCalls()throws Exception {		
		DataStore.store(dataId, "id", "2");
		DataStore.store(dataId, "name", "jaja");
		manage("testSelectSQLAsAttribute");
		assertEquals(null, DataStore.getValue(dataId, "db_value_for_name"));
		try{
			manage("testJavaServiceThrowingAfterDBCalls");
			fail("exception expected since thrown in javaservice");
		}catch(Exception e){
			//test if rollback did its job
			manage("testSelectSQLAsAttribute");
//			assertEquals("[jaja]", DataStore.getValue(dataId, "db_value_for_name"));
		}
	}
	
	public String getAppContextDir() {
		return CONFIG_LOCATION;
	}

	/**
	 * TODO: test datastore restore
	 */
	
	/**
	 * TODO: check how transactionaladvice responds to two different types of
	 * transactionmanagement
	 */
//	public void test2MthdCallsWithDifferentManagements()throws Exception{
//		UniqueIdentifier id = DataStore.register();
//		manage(id, "donothinglocal");
//		try{
//			manage(id, "donothingglobal");
//			fail("exception expected since using local and global at the same time");
//		}catch(Exception e){
//			//expected
//		}
//	}
}

