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
package test.com.qualogy.qafe.business.integration.rdb.call;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.Ignore;

import com.qualogy.qafe.business.test.BusinessActionTestCase;
import com.qualogy.qafe.core.datastore.DataStore;

@Ignore
public class CallStatementTest extends BusinessActionTestCase {

	public void testFunctionByCallWithoutParam() throws Exception{
		String ACTION_NAME = "getFuncHello";
		String expectedValue = "hello";
		
		manage(ACTION_NAME);

		String result = (String)DataStore.getValue(dataId, "result");
		assertEquals(expectedValue, result);
	}

	public void testFunctionByCallName() throws Exception{
		String ACTION_NAME = "getFuncName";
		String id = "7839";
		String expectedValue = "KING";
		
		DataStore.store(dataId, "id", id);
		manage(ACTION_NAME);

		String result = (String)DataStore.getValue(dataId, "result");
		assertEquals(expectedValue, result);
	}

	public void testProcedureByCallName() throws Exception{
		String ACTION_NAME = "getProcName";
		String id = "7839";
		String expectedValue = "KING";
		
		DataStore.store(dataId, "id", id);
		manage(ACTION_NAME);

		String result = (String)DataStore.getValue(dataId, "result");
		assertEquals(expectedValue, result);
	}

	
	public void testPackageFunctionByCallName() throws Exception{
		String ACTION_NAME = "getPackFuncName";
		String id = "7782";
		String expectedValue = "CLARK";
		
		DataStore.store(dataId, "id", id);
		manage(ACTION_NAME);

		String result = (String)DataStore.getValue(dataId, "result");
		assertEquals(expectedValue, result);
	}

	public void testPackageProcedureByCallName() throws Exception{
		String ACTION_NAME = "getPackProcName";
		String id = "7782";
		String expectedValue = "CLARK";
		
		DataStore.store(dataId, "id", id);
		manage(ACTION_NAME);

		String result = (String)DataStore.getValue(dataId, "result");
		assertEquals(expectedValue, result);
	}


	public void testFunctionBySQL() throws Exception{
		String ACTION_NAME = "getFuncNameBySQL";
		String id = "7839";
		String expectedValue = "KING";
		
		DataStore.store(dataId, "id", id);
		manage(ACTION_NAME);

		String result = (String)DataStore.getValue(dataId, "result");
		assertEquals(expectedValue, result);
	}

	public void testProcedureBySQL() throws Exception{
		String ACTION_NAME = "getProcNameBySQL";
		String id = "7839";
		String expectedValue = "KING";
		
		DataStore.store(dataId, "id", id);
		manage(ACTION_NAME);

		String result = (String)DataStore.getValue(dataId, "result");
		assertEquals(expectedValue, result);
	}

	
	public void testPackageFunctionBySQL() throws Exception{
		String ACTION_NAME = "getPackFuncNameBySQL";
		String id = "7782";
		String expectedValue = "CLARK";
		
		DataStore.store(dataId, "id", id);
		manage(ACTION_NAME);

		String result = (String)DataStore.getValue(dataId, "result");
		assertEquals(expectedValue, result);
	}

	public void testPackageProcedureBySQL() throws Exception{
		String ACTION_NAME = "getPackProcNameBySQL";
		String id = "7782";
		String expectedValue = "CLARK";
		
		DataStore.store(dataId, "id", id);
		manage(ACTION_NAME);

		String result = (String)DataStore.getValue(dataId, "result");
		assertEquals(expectedValue, result);
	}

	public void testFunctionBySQLAndNamedVars() throws Exception{
		String ACTION_NAME = "getFuncNameBySQLAndNamedVars";
		String id = "7839";
		String expectedValue = "KING";
		
		DataStore.store(dataId, "id", id);
		manage(ACTION_NAME);

		String result = (String)DataStore.getValue(dataId, "result");
		assertEquals(expectedValue, result);
	}

	public void testProcNameBySQLAndNamedVars() throws Exception{
		String ACTION_NAME = "getProcNameBySQLAndNamedVars";
		String id = "7782";
		String expectedValue = "CLARK";
		
		DataStore.store(dataId, "id", id);
		manage(ACTION_NAME);

		String result = (String)DataStore.getValue(dataId, "result");
		assertEquals(expectedValue, result);
	}

	public void testPackageFunctionBySQLAndNamedVars() throws Exception{
		String ACTION_NAME = "getPackFuncNameBySQLAndNamedVars";
		String id = "7782";
		String expectedValue = "CLARK";
		
		DataStore.store(dataId, "id", id);
		manage(ACTION_NAME);

		String result = (String)DataStore.getValue(dataId, "result");
		assertEquals(expectedValue, result);
	}

	public void testPackageProcedureBySQLAndNamedVars() throws Exception{
		String ACTION_NAME = "getPackProcNameBySQLAndNamedVars";
		String id = "7782";
		String expectedValue = "CLARK";
		
		DataStore.store(dataId, "id", id);
		manage(ACTION_NAME);

		String result = (String)DataStore.getValue(dataId, "result");
		assertEquals(expectedValue, result);
	}
	
	public void testCallWithObjectOutput() throws Exception{
		String ACTION_NAME = "callWithObjectOutput";		
		manage(ACTION_NAME);

		Object result = DataStore.getValue(dataId, "result");
		assertNotNull(result);
	}
	
	public void testCallWithObjectInput() throws Exception {
		String ACTION_NAME = "callWithObjectInput";
		Map<String,Object> input = new HashMap<String, Object>();
		input.put("LINE_ID", 1);
		input.put("HEADER_ID", 2);
		input.put("PROJECT_ID", 344);
		input.put("PROJECT_NAME", "my project");
		input.put("START_DATE", new Date());
			
		DataStore.store(dataId, "input", input);
		manage(ACTION_NAME);
		
		String expectedValue = "345";
		Map result = (Map) DataStore.getValue(dataId, "result");
		assertEquals(expectedValue , result.get("PROJECT_ID").toString());
	}
	
	public void testCallFucntion() throws Exception {
        String ACTION_NAME = "callSAY_HELLO_FNCOfQAFE_TESTS";
        String expectedValue = "QAFE";
        DataStore.store(dataId, "P_NAME", expectedValue);
        manage(ACTION_NAME);
        
        String result = (String) DataStore.getValue(dataId, "result");
        assertTrue(result.contains(expectedValue));
    }
    
    public void testCallProcedure() throws Exception {
        String ACTION_NAME = "callSAY_HELLO_PRCOfQAFE_TESTS";
        String expectedValue = "QAFE";
        DataStore.store(dataId, "P_NAME", expectedValue);
        manage(ACTION_NAME);
        
        String result = (String) DataStore.getValue(dataId, "P_RESULT");
        assertTrue(result.contains(expectedValue));
    }
    
    public void testCallFucntionInAnotherSchema() throws Exception {
        String ACTION_NAME = "callSAY_HELLO_FNCOfQAFE_TESTSOfAnotherSchema";
        String expectedValue = "QAFE";
        DataStore.store(dataId, "P_NAME", expectedValue);
        manage(ACTION_NAME);
        
        String result = (String) DataStore.getValue(dataId, "result");
        assertTrue(result.contains(expectedValue));
    }
    
    public void testCallProcedureInAnotherSchema() throws Exception {
        String ACTION_NAME = "callSAY_HELLO_PRCOfQAFE_TESTSOfAnotherSchema";
        String expectedValue = "QAFE";
        DataStore.store(dataId, "P_NAME", expectedValue);
        manage(ACTION_NAME);
        
        String result = (String) DataStore.getValue(dataId, "P_RESULT");
        assertTrue(result.contains(expectedValue));
    }
	
	@Override
	public String getAppContextDir() {
		return getDirBasedUponPackage();
	}
}