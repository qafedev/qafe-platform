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
package test.com.qualogy.qafe.core.datastore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import com.qualogy.qafe.core.datastore.DataIdentifier;
import com.qualogy.qafe.core.datastore.DataNotFoundException;
import com.qualogy.qafe.core.datastore.DataStore;



public class DataStoreTest extends TestCase {
	public void testGetValue(){
		DataIdentifier id = DataStore.register();
		
		Map mapCar = new HashMap();
		Map mapPerson = new HashMap();
		Map mapAddress1 = new HashMap();
		Map mapAddress2 = new HashMap();
		
		mapAddress1.put("street", "somestreet");
		mapAddress1.put("number", "1");
		
		mapPerson.put("name", "jaja");
		mapPerson.put("age", "18");
		mapPerson.put("address1", mapAddress1);
		
		mapCar.put("wheel", "19inches");
		mapCar.put("lights", null);
		mapCar.put("person", mapPerson);
		
		DataStore.store(id,"car",mapCar);
		
		DataStore.store(id,"carName","buick");
		
		assertEquals("buick", DataStore.getValue(id, "carName"));
		System.out.println(DataStore.toLogString(id));
		assertEquals("19inches", DataStore.getValue(id, "car.wheel"));
		
		assertNull(DataStore.getValue(id, "car.lights"));
		
		assertEquals("18", DataStore.getValue(id, "car.person.age"));
		assertEquals("jaja", DataStore.getValue(id, "car.person.name"));
		
		assertEquals("somestreet", DataStore.getValue(id, "car.person.address1.street"));
		
		try{
			DataStore.getValue(id, "car.person.firstname");
			fail("expecting DataNotFoundException since data is never set");
		}catch(DataNotFoundException e){
			
		}
		try{
			DataStore.getValue(id, null);
			fail("should throw cannot get on empty key");
		}catch(Exception e){
			//expected result
		}
		try{
			DataStore.getValue(id, "car.person.address1.street.");
			fail("should throw cannot get on key ending with '.'");
		}catch(Exception e){
			//expected result
		}
		
		mapAddress2.put("street", "somestreet");
		mapAddress2.put("number", "2");
		mapPerson.put("address2", mapAddress2);
		
		assertEquals("1", DataStore.getValue(id, "car.person.address1.number"));
		assertEquals("2", DataStore.getValue(id, "car.person.address2.number"));
		
		List addresses = new ArrayList();
		addresses.add(mapAddress1);
		addresses.add(mapAddress2);
		mapPerson.put("addresses", addresses);
		assertEquals("1", DataStore.getValue(id, "car.person.addresses[0].number"));
		
		try{
			DataStore.getValue(id, "car.person.addresses.number");
			fail("should throw cannot get anyfurther on a list result");
		}catch(Exception e){
		}
	}
	

	public void testStore(){
		DataIdentifier id = DataStore.register();
		DataStore.store(id, "car.person", "jan");
		
		DataStore.store(id, "car.person.name", "jan");
		assertEquals("jan", DataStore.getValue(id, "car.person.name"));
		DataStore.store(id, "car.person.age", "18");
		assertEquals("jan", DataStore.getValue(id, "car.person.name"));
		assertEquals("18", DataStore.getValue(id, "car.person.age"));
		
		
		DataStore.store(id, "car.person.addresslist[0].street", "somestreet");
		DataStore.store(id, "car.person.addresslist[1].street", "somestreet");
		DataStore.store(id, "car.person.addresslist[2]", "somestreet");
		DataStore.store(id, "car.person.addresslist[3]", "somestreet");
		assertEquals("somestreet", DataStore.getValue(id, "car.person.addresslist[0].street"));
		DataStore.store(id, "car.person.addresslist[4][0].street", "somestreet");
		assertEquals("somestreet", DataStore.getValue(id, "car.person.addresslist[1][0].street"));
		System.out.println(DataStore.toLogString(id));
		
		DataStore.store(id, "car.person.address.street", "somestreet");
		assertEquals("jan", DataStore.getValue(id, "car.person.name"));
		assertEquals("18", DataStore.getValue(id, "car.person.age"));
		assertEquals("somestreet", DataStore.getValue(id, "car.person.address.street"));
	}

	public void testNotRegistered(){
		try{
			DataStore.getValue(DataIdentifier.create(), "jaja");
			fail("not registered yet, should throw an exception");
		}catch(Exception e){
			//expected result
		}
	}
	
	public void testOneResultConvinienceGet(){
		DataIdentifier id = DataStore.register();
		DataStore.store(id, "name[0]", "jaja");
		System.out.println(DataStore.toLogString(id));
		//assertEquals("jaja", DataStore.getValue(id, "name[0]"));
		//assertEquals("jaja", DataStore.getValue(id, "name"));
	}
	
	public void testStoreList(){
		List list = new ArrayList();
		list.add("name");
		DataIdentifier id = DataStore.register();
		DataStore.store(id, "name", list);
		Object name = DataStore.getValue(id, "name");
		assertTrue("not of type String but ["+DataStore.getValue(id, "name").getClass()+"]", ArrayList.class.isInstance(name));
	}
	
//	public void testCaseInsensitive(){
//		DataIdentifier id = DataStore.register();
//		DataStore.store(id, "One", "1");
//		DataStore.store(id, "ONe", "2");
//		DataStore.store(id, "ONE", "3");
//		assertEquals(DataStore.getValue(id, "oNe"), "3");
//	}
	
	public void testUnregister(){
		DataIdentifier id = DataStore.register();
		DataStore.store(id, "ONE", "3");
		DataStore.unregister(id);
		try{
			DataStore.getValue(id, "oNe");
			fail("expect an exception since id was unregistered from the datastore");
		}catch(IllegalArgumentException e){
			
		}
	}
	
	public void testClear(){
		DataIdentifier id = DataStore.register();
		DataStore.store(id, "ONE", "3");
		DataStore.clear(id);
		try{
			DataStore.getValue(id, "ONE");
			fail("expect a DataNotFoundException");
		}catch(DataNotFoundException e){
			
		}
		DataStore.store(id, "ONE", "3");
		assertNotNull(DataStore.getValue(id, "ONE"));
	}
}
