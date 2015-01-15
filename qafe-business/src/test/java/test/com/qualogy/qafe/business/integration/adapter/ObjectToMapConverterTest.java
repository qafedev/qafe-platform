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
package test.com.qualogy.qafe.business.integration.adapter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import junit.framework.TestCase;

import org.springframework.util.LinkedCaseInsensitiveMap;

import com.qualogy.qafe.business.integration.adapter.ObjectMapConverter;

public class ObjectToMapConverterTest extends TestCase {
	public class SuperTestObject{
		public String aSuperString;
	}
	public class TestObject extends SuperTestObject{
		@SuppressWarnings("unused")
		private String aString;
		@SuppressWarnings("unused")
		private int aInt;
		@SuppressWarnings("unused")
		private Integer aInteger;
		public TestObject(String superString, String a, Integer b, int aInt){
			this.aString = a;
			this.aInteger = b;
			super.aSuperString = superString;
			this.aInt = aInt;
		}
	}
	
	public void testArray(){
		
	}
	public void testSomeObject(){
		SomeObject so = new SomeObject("nene");
		so.toString();
		
		Object outcome = ObjectMapConverter.convert(so);
		System.out.println(outcome);
//		assertTrue(!SomeOtherObject.class.equals(outcome.get("soo").getClass()));
		
		
	}
	
	public void testObject(){
		Object s = ObjectMapConverter.convert(new Object());
		if (s instanceof Map){
			assertTrue(((Map)s).isEmpty());
		}
	}
	
	public void testString(){
		String jaja = "jaja";
		Object obj = ObjectMapConverter.convert(jaja);
		assertNull(obj);
	}
	
	public void testMap(){
		Map<String, String> map = new HashMap<String, String>();
		map.put("key", "jaja");
		ObjectMapConverter.convert(map);
	}
	
	//not able to read hashmap data
	public void testObjectInMapList(){
		List list = new ArrayList();
		Map data = new HashMap<String, DummyPerson>();
		data.put("person1" , new DummyPerson("Hallo","Khaznadar"));	
		list.add(data);
		Object result = ObjectMapConverter.convert(list);
		System.err.println(result);
	}
	
	
	//not able to read hashmap data
	public void testObjectInMap(){
		Map data = new HashMap<String, DummyPerson>();
		data.put("person1" , new DummyPerson("Hallo","Khaznadar"));	
		Object result = ObjectMapConverter.convert(data);
		System.err.println(result);
	}
	
	/**
	 * TODO: removed from converter, check if still necessary on converter otherwise remove test
	 *
	 */
//	public void testObjectInMap(){
//		Map map = new HashMap();
//		TestObject object = new TestObject("superString", "aString", new Integer(1), 1234);
//		map.put("key", object);
//		map = ObjectToMapConverter.convert(map);
//		assertEquals("superString", (((Map)map.get("key")).get("aSuperString")));
//		assertEquals("aString", (((Map)map.get("key")).get("aString")));
//		assertEquals(new Integer(1), (((Map)map.get("key")).get("aInteger")));
//		assertEquals(1234, ((Integer)(((Map)map.get("key")).get("aInt"))).intValue());
//	}
	public void testNullInMap(){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("key", null);
		Object o  = ObjectMapConverter.convert(map);
		if (o instanceof Map)
		assertNull(((Map)o).get("key"));
	}
	public void testNull(){
		Object map = ObjectMapConverter.convert(null);
		assertNull(map);
	}
	
	/**
	 * OJDBC gives back a result set containing a list of LinkedCaseInsensitiveMaps.
	 * Inside that Map, BigDecimal and BigInteger are used as a representation of a number.
	 * LinkedCaseInsensitiveMap, BigDecimal and bigInteger are not serializable to be send to the client-side.
	 */
	public void testConvertOptions() {
		BigInteger bigInteger = BigInteger.valueOf(10);
		BigDecimal bigDecimal = BigDecimal.valueOf(20);
		Map<String, Object> map = new LinkedCaseInsensitiveMap<Object>();
		Set<String> options = new HashSet<String>();
		options.add(ObjectMapConverter.OPTION_SERIALIZABLE_OBJECTS);
		
		String bigIntegerName = "bi";
		String bigDecimalName = "bc";
		map.put(bigIntegerName, bigInteger);
		map.put(bigDecimalName, bigDecimal);
		
		Object resultOptionsMap = ObjectMapConverter.convert(map, options);
		assertFalse("Map is an instance of LinkedCaseInsensitiveMap", resultOptionsMap instanceof LinkedCaseInsensitiveMap);
		assertTrue("Map is not an instance of HashMap", resultOptionsMap instanceof HashMap);

		@SuppressWarnings("unchecked")
		Map<String, Object> resultOptionHashMap = (Map<String, Object>) resultOptionsMap;
		Object bigIntegerObject = resultOptionHashMap.get(bigIntegerName);
		Object bigDecimalObject = resultOptionHashMap.get(bigDecimalName);
		
		assertTrue("Big Integer object is not a Long", bigIntegerObject instanceof Long);
		assertTrue("Big Decimal object is not a Long", bigDecimalObject instanceof Long);
		assertEquals("Big Integer object is not properly converted", bigIntegerObject, 10L);
		assertEquals("Big Decimal object is not properly converted", bigDecimalObject, 20L);
	}
}
