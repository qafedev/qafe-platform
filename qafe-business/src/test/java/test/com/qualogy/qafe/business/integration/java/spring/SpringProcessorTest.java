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
package test.com.qualogy.qafe.business.integration.java.spring;

import java.util.logging.Logger;

import com.qualogy.qafe.business.resource.ResourcePool;
import com.qualogy.qafe.business.test.BusinessActionTestCase;
import com.qualogy.qafe.core.datastore.DataStore;

public class SpringProcessorTest extends BusinessActionTestCase {

	public final static Logger logger = Logger.getLogger(SpringProcessorTest.class.getName());
	
	
	public void testSayHello() throws Exception{
		String ACTION_NAME = "sayHello1A";
		String expectedValue = "Hello";
		
		manage(ACTION_NAME);
		logger.info(ResourcePool.getInstance().toLogString());

		String result = (String)DataStore.getValue(dataId, "result");
		assertEquals(expectedValue, result);
	}


	public void testSayGreetings() throws Exception{
		String ACTION_NAME = "sayGreetings2A";
		String expectedValue = "Greetings";
		
		manage(ACTION_NAME);
		logger.info(ResourcePool.getInstance().toLogString());

		String result = (String)DataStore.getValue(dataId, "result");
		assertEquals(expectedValue, result);
	}


	public void testSayHello2() throws Exception{
		String ACTION_NAME = "sayHello1B";
		String expectedValue = "Hello";
		
		manage(ACTION_NAME);
		logger.info(ResourcePool.getInstance().toLogString());

		String result = (String)DataStore.getValue(dataId, "result");
		assertEquals(expectedValue, result);
	}


	public void testSayGreetings2() throws Exception{
		String ACTION_NAME = "sayGreetings2B";
		String expectedValue = "Greetings";
		
		manage(ACTION_NAME);
		logger.info(ResourcePool.getInstance().toLogString());

		String result = (String)DataStore.getValue(dataId, "result");
		assertEquals(expectedValue, result);
	}

	public void testSayGoodMorningUseValue() throws Exception{
		String ACTION_NAME = "sayGoodMorning";
		String expectedValue = "Good Morning";
		
		manage(ACTION_NAME);
		logger.info(ResourcePool.getInstance().toLogString());

		String result = (String)DataStore.getValue(dataId, "result");
		assertEquals(expectedValue, result);
	}

	public void testSaySomethingUseReference() throws Exception{
		String ACTION_NAME = "saySomething";
		String expectedValue = "Good Afternoon";
		
		String[][] data = new String[][]{
			{"input", expectedValue}, 			
		};
		manage(ACTION_NAME, data);
		logger.info(ResourcePool.getInstance().toLogString());

		String result = (String)DataStore.getValue(dataId, "result");
		assertEquals(expectedValue, result);
	}

	@Override
	public String getAppContextDir() {
		return getDirBasedUponPackage();
	}
}
