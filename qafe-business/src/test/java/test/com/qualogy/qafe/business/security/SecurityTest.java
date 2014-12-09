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
package test.com.qualogy.qafe.business.security;

import java.util.logging.Logger;

import com.qualogy.qafe.business.resource.ResourcePool;
import com.qualogy.qafe.business.test.BusinessActionTestCase;
import com.qualogy.qafe.core.datastore.DataStore;

public class SecurityTest extends BusinessActionTestCase {
	
	public final static Logger logger = Logger.getLogger(SecurityTest.class.getName());

	private final String ACTION_NAME = "$_handle_security";
	
	protected void setUp() throws Exception{
		super.setUp();
	}
	
	public void testValidLogin4Java() throws Exception {
		Number expectedValue = 0;
		
		String[][] data = new String[][]{
			{"$username", "qafe"}, 			
			{"$password", "qafe"},
		};
		manage(ACTION_NAME, data);
		logger.info(ResourcePool.getInstance().toLogString());

		Object result = DataStore.getValue(dataId, "authenticate_result");
		Number authenticateResult = null;
		if (result instanceof Number) {
			authenticateResult = (Number)result;
		}
		assertNotNull(authenticateResult);
		assertEquals(expectedValue, authenticateResult);
	}

	public void testInvalidLogin4Java() throws Exception {
		Number expectedValue = 1;
		
		String[][] data = new String[][]{
			{"$username", "qafe1"}, 			
			{"$password", "qafe1"},
		};
		manage(ACTION_NAME, data);
		logger.info(ResourcePool.getInstance().toLogString());

		Object result = DataStore.getValue(dataId, "authenticate_result");
		Number authenticateResult = null;
		if (result instanceof Number) {
			authenticateResult = (Number)result;
		}
		
		assertNotNull(authenticateResult);
		assertEquals(expectedValue, authenticateResult);
	}
	
	public void testAuthorisation4Java() throws Exception {
		String[][] data = new String[][]{
			{"$username", "qafe"}, 			
		};
		manage(ACTION_NAME, data);
		logger.info(ResourcePool.getInstance().toLogString());

		Object result = DataStore.getValue(dataId, "authorize_result");
		
		assertNotNull(result);
	}
	
	@Override
	public String getAppContextDir() {
		return getDirBasedUponPackage();
	}

}
