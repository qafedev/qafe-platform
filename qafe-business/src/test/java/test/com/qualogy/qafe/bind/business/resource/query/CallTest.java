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
package test.com.qualogy.qafe.bind.business.resource.query;

import junit.framework.TestCase;

import com.qualogy.qafe.bind.resource.query.Call;
import com.qualogy.qafe.bind.resource.query.Delete;
import com.qualogy.qafe.bind.resource.query.MetaData;
import com.qualogy.qafe.bind.resource.query.Update;
import com.qualogy.qafe.business.resource.rdb.query.QueryToStringCreator;

public class CallTest extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	
	public void testParseProcedureNameFromProcedureSQL(){
		String callName = "jaja.jaja";
		
		String sql2 = "{ call "+callName+"( from_city ?, to_city ?) }";
		Call call = Call.createWithSQLStr(sql2);
		assertEquals(true, call.isPrepared());
		assertEquals(callName.toUpperCase(), call.getCallName());
		
	}
	public void testParseProcedureNameFromFuncationSQL(){
		String callName = "jaja.jaja";
		
		String sql1 = "{ call ?,? = "+callName+"( from_city ?, to_city ?) }";
		
		Call call = Call.createWithSQLStr(sql1);
		assertEquals(true, call.isPrepared());
		assertEquals(callName.toUpperCase(), call.getCallName());
	}
	
	/*public void testExtraWhereClauseInDelete(){
		Delete delete = new Delete();
		delete.setTable("table");
		delete.setWhere("extraParam1 = :extraParam1 and extraParam2 = :extraParam2 ");
		
		// Normal parameters
		String[] normalParams = new String[3];
		normalParams[0] = "fieldToFilter1";
		normalParams[1] = "primaryKey1";
		normalParams[2] = "primaryKey2";
		
		// Metadata
		MetaData metaData = new MetaData();
		metaData.addPrimaryKey("primaryKey1");
		metaData.addPrimaryKey("primaryKey2");
		delete.setMetaData(metaData);
		
		String result = QueryToStringCreator.toString(delete, normalParams, null);
		System.err.println(result);
		
		String expectedResult = "delete from table where fieldToFilter1=:fieldToFilter1 AND primaryKey1=:primaryKey1 AND primaryKey2=:primaryKey2 and extraParam1 = :extraParam1 and extraParam2 = :extraParam2 ";
		System.err.println(expectedResult);
		assertTrue(expectedResult.equals(result)); 
	}*/
}
