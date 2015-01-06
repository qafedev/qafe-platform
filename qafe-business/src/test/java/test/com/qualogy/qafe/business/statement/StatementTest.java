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
package test.com.qualogy.qafe.business.statement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.qualogy.qafe.business.test.BusinessActionTestCase;
import com.qualogy.qafe.core.datastore.DataStore;
import com.qualogy.qafe.core.errorhandling.ExternalException;
import com.qualogy.qafe.core.framework.business.UnableToManageException;

public class StatementTest extends BusinessActionTestCase{

	public String getAppContextDir() {
		return getDirBasedUponPackage();
	}

	public void testHappyDay1() throws Exception{
		DataStore.store(dataId, "alist", createPersonsList(15));
		manage("baforloop-begin2-end10-increment2");
		logDataStore();
		assertEquals("10", DataStore.getValue(dataId, "name"));
	}
	
	public void testHappyDay2() throws Exception{
		DataStore.store(dataId, "alist", createPersonsList(15));
		manage("baforloop");
		logDataStore();
		assertEquals("14", DataStore.getValue(dataId, "name"));
	}
	
	public void testIncrementTooLarge() throws Exception{
		DataStore.store(dataId, "alist", createPersonsList(150));
		manage("baforloop-begin2-end10-increment100");
		logDataStore();
		assertEquals("2", DataStore.getValue(dataId, "name"));
	}
	
	public void testIncrementToOneAfterLast() throws ExternalException{
		DataStore.store(dataId, "alist", createPersonsList(4));
		try{
			manage("baforloop-begin2-end4-increment2");
		}catch(UnableToManageException e){
			
		}
		
	}
	
	public void testIncrementToLast() throws Exception{
		DataStore.store(dataId, "alist", createPersonsList(5));
		manage("baforloop-begin2-end4-increment2");
		logDataStore();
		assertEquals("4",DataStore.getValue(dataId, "name"));
	}
	
	public void testForLoopTooMuchInList() throws Exception{
		DataStore.store(dataId, "alist", createPersonsList(3));
		manage("baforloop-begin0-end2");
		logDataStore();
		assertEquals("2",DataStore.getValue(dataId, "name"));
	}
	
	public void testForLoopTooLittleInList() throws ExternalException{
		DataStore.store(dataId, "alist", createPersonsList(1));
		try{
			manage("baforloop-begin0-end2");
		}catch(UnableToManageException e){
			
		}
		
	}
	
	public void testForLoopStart0End0() throws Exception{
		DataStore.store(dataId, "alist", createPersonsList(6));
		manage("baforloop-begin0-end0");
		logDataStore();
		assertEquals("0", DataStore.getValue(dataId, "name"));
	}
	
	public void testBaIfWithExplicitExpression() throws Exception{
		DataStore.store(dataId, "left", "2");
		DataStore.store(dataId, "right", "2");
		manage("baifwithexplicit");
		assertEquals("true", DataStore.getValue(dataId, "outcome"));
	
		DataStore.clear(dataId);
		DataStore.store(dataId, "left", "2");
		DataStore.store(dataId, "right", "3");
		manage("baifwithexplicit");
		assertEquals("false", DataStore.getValue(dataId, "outcome"));
	}
	public void testBaIfWithExplicitExpression1() throws Exception{
		DataStore.store(dataId, "left", "1+1");
		DataStore.store(dataId, "right", "3");
		manage("baifwithexplicit");
		assertEquals("false", DataStore.getValue(dataId, "outcome"));
	}
	public void testBaswitchWithExplicitExpression() throws Exception{
		DataStore.store(dataId, "switch", "1");
		manage("baswitchwithexplicit");
		assertEquals("1", DataStore.getValue(dataId, "outcome"));
		
		DataStore.store(dataId, "switch", "2");
		System.err.println(DataStore.toLogString(dataId));
		manage("baswitchwithexplicit");
		System.err.println(DataStore.toLogString(dataId));
		assertEquals("2", DataStore.getValue(dataId, "outcome"));
		
		DataStore.store(dataId, "switch", "3");
		manage("baswitchwithexplicit");
		assertEquals("3", DataStore.getValue(dataId, "outcome"));
	}
	
	public void testBaswitchWithExplicitExpressionWithDefault() throws Exception{
		DataStore.store(dataId, "switch", "4");
		manage("baswitchwithexplicit");
		assertEquals("3", DataStore.getValue(dataId, "outcome"));
	}
	
	private List createPersonsList(int amount){
		List<Map<String, String>> persons = new ArrayList<Map<String, String>>();
		for (int i = 0; i < amount; i++) {
			Map<String, String> person = new HashMap<String, String>();
			person.put("name", "" + i);
			persons.add(person);
		}
		return persons;
	}
}
