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
package test.com.qualogy.qafe.business.integration.adapter;

import java.util.HashMap;
import java.util.Map;

import test.com.qualogy.qafe.business.integration.testservices.MyPerson;

import com.qualogy.qafe.business.test.BusinessActionTestCase;
import com.qualogy.qafe.core.datastore.DataStore;

public class AdaptToServiceTest extends BusinessActionTestCase {

	public String getAppContextDir() {
		return getDirBasedUponPackage() + "";
	}
	public void testValidateWithMap() throws Exception{
		Map<String, Object> personMap = new HashMap<String, Object>();
		personMap.put("lastName", "lastName");
		personMap.put("name", "name");
		DataStore.store(dataId, "0", personMap);
		manage("testValidateWithMap");				
		assertNotNull(DataStore.getValue(dataId, "0"));		
	}
	
	public void testValidate() throws Exception{	
	
		MyPerson mp = new MyPerson();
		mp.setLastName("lastName");
		mp.setName("name");
		DataStore.store(dataId, "0", mp);
		manage("testValidateWithMyPerson");			
		assertNotNull(DataStore.getValue(dataId, "0"));
	}
	
	
	/**
	 * method checks if leaving the ref attribute out leaves
	 * null values in the datastore 
	 * @throws Exception
	 */
	public void testSupportNullReferences() throws Exception{
		DataStore.store(dataId, "servicein", null);
		assertNull(DataStore.getValue(dataId, "servicein"));
		manage("testSupportNullReferences");
		assertNull(DataStore.getValue(dataId, "servicein"));
		assertTrue(((Boolean)DataStore.getValue(dataId, "serviceout")).booleanValue());
		assertNull(DataStore.getValue(dataId, "businessactionout"));
	}
	
	/**
	 * method checks if primitive javaservice calls work
	 * @throws Exception
	 */
	public void testMethodWithPrimitive() throws Exception{
		DataStore.store(dataId, "servicein", new Integer(1));
		manage("testMethodWithPrimitive");
		assertTrue(((Boolean)DataStore.getValue(dataId, "serviceout")).booleanValue());
	}
	
//	public void testNamedParametersOnJavaService() throws Exception{
//		DataStore.store(dataId, "servicein", null);
//		assertNull(DataStore.getValue(dataId, "servicein"));
//		manage("testNamedParametersOnJavaService");
//		assertNull(DataStore.getValue(dataId, "servicein"));
//		assertNull(DataStore.getValue(dataId, "serviceout"));
//		assertNull(DataStore.getValue(dataId, "businessactionout"));
//		assertTrue(((Boolean)DataStore.getValue(dataId, "actualserviceout")).booleanValue());
//	}
}
