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

import com.qualogy.qafe.business.test.BusinessActionTestCase;
import com.qualogy.qafe.core.errorhandling.ExternalException;

public class ErrorHandlingTest extends BusinessActionTestCase{

	public final static String CONFIG_LOCATION = "samples/errorhandling";
	
	public String getAppContextDir() {
		return CONFIG_LOCATION;
	}
	
	//test transaction rollback
	
	//test getting the right errorhandler, when all known errors are handled
	
	//simple exception handling test with systemout call
	
	public void testExceptionRethrow(){
		try {
			manage("testExceptionRethrow");
			fail("expecting to rethrow");
		} catch (ExternalException e) {
			
		}
	}
	
	public void testExceptionSwallow(){
		try {
			manage("testExceptionSwallow");
		} catch (ExternalException e) {
			fail("expecting to swallow");
		}
	}
	
}
