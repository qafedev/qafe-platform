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
package test.com.qualogy.qafe.business;


import com.qualogy.qafe.business.test.BusinessActionTestCase;
import com.qualogy.qafe.core.datastore.DataStore;

public class ParameterTest extends BusinessActionTestCase{
	
	public final static String TEST_DIR = "samples/parametertest";
	
	public final static String OUTCOME_KEY = "outcome";
	
	public void testParameter() throws Exception{
		DataStore.store(dataId, "businessactionin", "businessactionin");
		manage("test");
	}
	
	public void testValueWithPlaceHolders() throws Exception{
		manage("testValueWithPlaceHolders");
		assertEquals("hello world", DataStore.getValue(dataId, OUTCOME_KEY));
	}
	
	public void testValueWithNoPlaceHolders() throws Exception{
		manage("testValueWithNoPlaceHolders");
		assertEquals("", DataStore.getValue(dataId, OUTCOME_KEY));
	}
	
	public void testValueWithTheSamePlaceHolders() throws Exception{
		manage("testValueWithTheSamePlaceHolders");
		assertEquals("hello world - hello world", DataStore.getValue(dataId, OUTCOME_KEY));
	}
	
	public void testExpression() throws Exception{
		manage("testExpression");
		assertEquals("1", DataStore.getValue(dataId, OUTCOME_KEY));
	}
	
	public void testExpressionWithPlaceHolders() throws Exception{
		manage("testExpressionWithPlaceHolders");
		assertEquals("1", DataStore.getValue(dataId, OUTCOME_KEY));
	}
	
	public void testMessages() throws Exception{
		manage("testMessages");
		assertEquals("amessagefromcontextfile", DataStore.getValue(dataId, OUTCOME_KEY));
	}
	
	public void testMessagesWithPlaceHolder() throws Exception{
		manage("testMessagesWithPlaceHolder");
		assertEquals("amessagefromcontextfilewithplaceholder", DataStore.getValue(dataId, OUTCOME_KEY));
	}
	public void testMessagesWithPlaceHolderAndBundle() throws Exception{
		manage("testMessagesWithPlaceHolderAndBundle");
		assertEquals("bundle_id_1amessagefromcontextfilewithplaceholder", DataStore.getValue(dataId, OUTCOME_KEY));
	}
	public void testMessagesWithBundle() throws Exception{
		manage("testMessagesWithBundle");
		assertEquals("bundle_id_1amessagefromcontextfile", DataStore.getValue(dataId, OUTCOME_KEY));
	}
	public void testMessagesWithBundleFromFile() throws Exception{
		manage("testMessagesWithBundleFromFile");
		assertEquals("bundle_id_2amessagefrommessagefile", DataStore.getValue(dataId, OUTCOME_KEY));
	}
	
	public String getAppContextDir() {
		return TEST_DIR;
	}
}
