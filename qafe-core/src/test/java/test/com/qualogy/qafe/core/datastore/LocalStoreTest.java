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
package test.com.qualogy.qafe.core.datastore;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import junit.framework.TestCase;

import com.qualogy.qafe.core.datastore.ApplicationLocalStore;
import com.qualogy.qafe.core.datastore.DataMap;



public class LocalStoreTest extends TestCase {
	
	public void testGetValue(){
		List<Integer> list = new ArrayList<Integer>();
		list.add(1);
		list.add(2);
		ApplicationLocalStore.getInstance().store("1234","array", list);
		Object o = ApplicationLocalStore.getInstance().retrieve("1234", "array@size");
		assertEquals(2, o);
	}
	
	public void testListOfdataMapRetrieval() {
		List<DataMap> listOfDataMap = new ArrayList<DataMap>();
		DataMap<String, Object> dm1 = new DataMap<String, Object>();
		dm1.put("ID", 5);
		dm1.put("NAME", "Abc");
		dm1.put("DATE", new Date());
		listOfDataMap.add(dm1);
		ApplicationLocalStore.getInstance().store("4", "ldm", listOfDataMap);
		Object obj = ApplicationLocalStore.getInstance().retrieve("4", "ldm");
		assertEquals(listOfDataMap, obj);		
	}
		
}
