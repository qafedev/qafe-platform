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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.qualogy.qafe.bind.commons.type.Out;
import com.qualogy.qafe.business.integration.adapter.Adapter;
import com.qualogy.qafe.core.datastore.DataIdentifier;
import com.qualogy.qafe.core.datastore.DataStore;

import junit.framework.TestCase;

public class AdapterTest extends TestCase {

	public void testEmptyList(){
		DataIdentifier dataId = DataStore.register();
		List<Object> myList = new ArrayList<Object>();
		List outputMapping = new ArrayList();
		Out out = new Out();
		out.setName("result");
		outputMapping.add(out);
		Adapter.adaptOut(dataId,myList, outputMapping);
		Object result = DataStore.findValue(dataId, out.getName());
		System.err.println("testEmptyList");
		System.err.println(DataStore.toLogString(dataId));
		DataStore.unregister(dataId);
	}
	public void testListOfString(){
		DataIdentifier dataId = DataStore.register();
		List<String> myList = new ArrayList<String>();
		myList.add("String 1");
		myList.add("String 2");
		myList.add("String 3");
		List outputMapping = new ArrayList();
		Out out = new Out();
		out.setName("result");
		outputMapping.add(out);
		Adapter.adaptOut(dataId,myList, outputMapping);
		Object result = DataStore.findValue(dataId, out.getName());
		System.err.println("testListOfString");
		System.err.println(DataStore.toLogString(dataId));
		DataStore.unregister(dataId);
	}
	public void testListSingleElementSimpleType(){
		DataIdentifier dataId = DataStore.register();
		List<Object> myList = new ArrayList<Object>();
		myList.add(new Integer(1));
		List outputMapping = new ArrayList();
		Out out = new Out();
		out.setName("result");
		outputMapping.add(out);
		Adapter.adaptOut(dataId,myList, outputMapping);
		Object result = DataStore.findValue(dataId, out.getName());
		System.err.println("testListSingleElementSimpleType");
		System.err.println(DataStore.toLogString(dataId));
		DataStore.unregister(dataId);
	}
	
	public void testListSingleElementOnePerson(){
		DataIdentifier dataId = DataStore.register();
		List<Object> myList = new ArrayList<Object>();
		myList.add(new DummyPerson("Hallo","Khaznadar"));
		List outputMapping = new ArrayList();
		Out out = new Out();
		out.setName("result");
		outputMapping.add(out);
		Adapter.adaptOut(dataId,myList, outputMapping);
		Object result = DataStore.findValue(dataId, out.getName());
		System.err.println("testListSingleElementOnePerson");
		System.err.println(DataStore.toLogString(dataId));
		DataStore.unregister(dataId);
	}
	
	public void testListMultipleElementPerson(){
		DataIdentifier dataId = DataStore.register();
		List<Object> myList = new ArrayList<Object>();
		myList.add(new DummyPerson("Hallo","Khaznadar"));
		myList.add(new DummyPerson("Ravi","Nair"));
		
		List outputMapping = new ArrayList();
		Out out = new Out();
		out.setName("result");
		outputMapping.add(out);
		Adapter.adaptOut(dataId,myList, outputMapping);
		Object result = DataStore.findValue(dataId, out.getName());
		System.err.println("testListMultipleElementPerson");
		System.err.println(DataStore.toLogString(dataId));
		DataStore.unregister(dataId);
	}
	
	
	public void testHashMapWithPerson(){
		DataIdentifier dataId = DataStore.register();
		Map data = new HashMap<String, DummyPerson>();
		data.put("person1" , new DummyPerson("Hallo","Khaznadar"));		
		List outputMapping = new ArrayList();
		Out out = new Out();
		out.setName("result");
		outputMapping.add(out);
		Adapter.adaptOut(dataId,data, outputMapping);
		Object result = DataStore.findValue(dataId, out.getName());
		System.err.println("testHashMapWithPerson");
		System.err.println(DataStore.toLogString(dataId));
		DataStore.unregister(dataId);
	}
	
	// not working- TO DO
	public void testSetWithPerson(){
		DataIdentifier dataId = DataStore.register();
		Set data = new HashSet<String>();
		data.add("Hallo");		
		
		List outputMapping = new ArrayList();
		Out out = new Out();
		out.setName("result");
		outputMapping.add(out);
		Adapter.adaptOut(dataId,data, outputMapping);
		Object result = DataStore.findValue(dataId, out.getName());
		System.err.println("testSetWithPerson");
		System.err.println(DataStore.toLogString(dataId));
		DataStore.unregister(dataId);
	}
			
	public void testListMultipleElementComplexPerson(){
		DataIdentifier dataId = DataStore.register();
		List<Object> myList = new ArrayList<Object>();
		DummyPersonMoreComplex tp1 = new DummyPersonMoreComplex("Hallo","Khaznadar");
		DummyPersonMoreComplex tp2 = new DummyPersonMoreComplex("Ravi","Nair");
		tp1.add("house", "nearby");
		myList.add(tp1);
		myList.add(tp2);
		
		List outputMapping = new ArrayList();
		Out out = new Out();
		out.setName("result");
		outputMapping.add(out);
		Adapter.adaptOut(dataId,myList, outputMapping);
		Object result = DataStore.findValue(dataId, out.getName());
		System.err.println("testListMultipleElementComplexPerson");
		System.err.println(DataStore.toLogString(dataId));
		DataStore.unregister(dataId);
	}
	
	public void testListMultipleElementComplexPersonObject(){
		DataIdentifier dataId = DataStore.register();
		List<Object> myList = new ArrayList<Object>();
		DummyPersonMoreComplexObject tp1 = new DummyPersonMoreComplexObject("Hallo","Khaznadar");
		DummyPersonMoreComplexObject tp2 = new DummyPersonMoreComplexObject("Ravi","Nair");
		tp1.add("colleague", tp2);
		myList.add(tp1);
		
		
		List outputMapping = new ArrayList();
		Out out = new Out();
		out.setName("result");
		outputMapping.add(out);
		Adapter.adaptOut(dataId,myList, outputMapping);
		Object result = DataStore.findValue(dataId, out.getName());
		System.err.println("testListMultipleElementComplexPersonObject");
		System.err.println(DataStore.toLogString(dataId));
		DataStore.unregister(dataId);
	}
	
	
	public void testListMultipleElementComplexPersonList(){
		DataIdentifier dataId = DataStore.register();
		List<Object> myList = new ArrayList<Object>();
		DummyPersonMoreComplexObjectList tp1 = new DummyPersonMoreComplexObjectList("Hallo","Khaznadar");
		DummyPersonMoreComplexObjectList tp2 = new DummyPersonMoreComplexObjectList("Ravi","Nair");
		tp1.add(tp2);
		myList.add(tp1);
		
		
		List outputMapping = new ArrayList();
		Out out = new Out();
		out.setName("result");
		outputMapping.add(out);
		Adapter.adaptOut(dataId,myList, outputMapping);
		Object result = DataStore.findValue(dataId, out.getName());
		System.err.println("testListMultipleElementComplexPersonList");
		System.err.println(DataStore.toLogString(dataId));
		DataStore.unregister(dataId);
	}
	
	public void testListMultipleElementComplexPersonListRecursive(){
		DataIdentifier dataId = DataStore.register();
		List<Object> myList = new ArrayList<Object>();
		DummyPersonMoreComplexObjectList tp1 = new DummyPersonMoreComplexObjectList("Hallo","Khaznadar");
		DummyPersonMoreComplexObjectList tp2 = new DummyPersonMoreComplexObjectList("Ravi","Nair");
		tp1.add(tp2);
		myList.add(tp1);
		tp2.add(tp1);
		
		
		List outputMapping = new ArrayList();
		Out out = new Out();
		out.setName("result");
		outputMapping.add(out);
		Adapter.adaptOut(dataId,myList, outputMapping);
		Object result = DataStore.findValue(dataId, out.getName());
		System.err.println("testListMultipleElementComplexPersonListRecursive");
		System.err.println(DataStore.toLogString(dataId));
		DataStore.unregister(dataId);
	}
	
	
	public void testListMultipleElementComplexListOfList(){
		DataIdentifier dataId = DataStore.register();
		List<DummyPersonMoreComplexObjectList> myList = new ArrayList<DummyPersonMoreComplexObjectList>();
		DummyPersonMoreComplexObjectList tp1 = new DummyPersonMoreComplexObjectList("Hallo","Khaznadar");
		DummyPersonMoreComplexObjectList tp2 = new DummyPersonMoreComplexObjectList("Ravi","Nair");
		
		myList.add(tp1);
		myList.add(tp2);
		
		List<List<DummyPersonMoreComplexObjectList>> listOfList = new ArrayList<List<DummyPersonMoreComplexObjectList>>();
		
		listOfList.add(myList);
		
		
		List outputMapping = new ArrayList();
		Out out = new Out();
		out.setName("result");
		outputMapping.add(out);
		Adapter.adaptOut(dataId,listOfList, outputMapping);
		Object result = DataStore.findValue(dataId, out.getName());
		System.err.println("testListMultipleElementComplexListOfList");
		System.err.println(DataStore.toLogString(dataId));
		DataStore.unregister(dataId);
	}
	
}
