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
package com.qualogy.qafe.gwt.server;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.qualogy.qafe.core.datastore.ApplicationLocalStore;
import com.qualogy.qafe.gwt.client.component.DataMap;
import com.qualogy.qafe.gwt.client.exception.GWTServiceException;
import com.qualogy.qafe.gwt.client.vo.functions.DataContainerGVO;
import com.qualogy.qafe.web.util.DatagridStorageHelper;


public class RPCServiceImplTest {
	
	@Test
	public void testAllElementsRemovedOnAWindowSessionOnAWindow() {
		String windowSession = "windowSessionId";
		String windowId = "windowId";
		String generatedKey = windowSession + ApplicationLocalStore.OBJECT_DELIMITER + windowId;
		
		ApplicationLocalStore.getInstance().store(generatedKey, "key1", "value1");
		Object data = ApplicationLocalStore.getInstance().retrieve(generatedKey, "key1");
		assertEquals("value1", data.toString());
		ApplicationLocalStore.getInstance().store(generatedKey, "key2", "value2");
		assertEquals("value2",ApplicationLocalStore.getInstance().retrieve(generatedKey, "key2"));
		
		RPCServiceImpl impl = new RPCServiceImpl();
		impl.removeWindowsEventData(windowSession,windowId);
		assertEquals(null,ApplicationLocalStore.getInstance().retrieve(generatedKey, "key1"));
		assertEquals(null,ApplicationLocalStore.getInstance().retrieve(generatedKey, "key2"));
	}
	
	@Test
	public void testOnlyElementsInAWindowIsRemoved() {
		String windowSession = "windowSessionId";
		String windowId1 = "windowId1";
		String windowId2 = "windowId2";
		String generatedKey1 = windowSession + ApplicationLocalStore.OBJECT_DELIMITER + windowId1;
		String generatedKey2 = windowSession + ApplicationLocalStore.OBJECT_DELIMITER + windowId2;
		
		ApplicationLocalStore.getInstance().store(generatedKey1, "key", "value1");
		Object data = ApplicationLocalStore.getInstance().retrieve(generatedKey1, "key");
		assertEquals("value1", data.toString());
		
		ApplicationLocalStore.getInstance().store(generatedKey2, "key", "value2");
		assertEquals("value2", ApplicationLocalStore.getInstance().retrieve(generatedKey2, "key"));
		
		RPCServiceImpl impl = new RPCServiceImpl();
		impl.removeWindowsEventData(windowSession,windowId2);
		assertEquals(null, ApplicationLocalStore.getInstance().retrieve(generatedKey2, "key"));
		assertEquals("value1", ApplicationLocalStore.getInstance().retrieve(generatedKey1, "key"));
	}
	
	@Test
	public void testOnlyElementsInAWindowSessionIsRemoved() {
		String windowSession1 = "windowSessionId1";
		String windowSession2 = "windowSessionId2";
		String windowId1 = "windowId1";
		String generatedKey1 = windowSession1 + ApplicationLocalStore.OBJECT_DELIMITER + windowId1;
		String generatedKey2 = windowSession2 + ApplicationLocalStore.OBJECT_DELIMITER + windowId1;
		
		ApplicationLocalStore.getInstance().store(generatedKey1, "key", "value1");
		ApplicationLocalStore.getInstance().store(generatedKey2, "key", "value2");
		
		RPCServiceImpl impl = new RPCServiceImpl();
		impl.removeWindowsEventData(windowSession1,windowId1);
		assertEquals(null, ApplicationLocalStore.getInstance().retrieve(generatedKey1, "key"));
		assertEquals("value2", ApplicationLocalStore.getInstance().retrieve(generatedKey2, "key"));
	}
	
	
	/**
	 * Test that the prepare for exporting method stores the right data in the right order.
	 */
	@Test
	public void testPrepareforExportMantainingTheOrder() {
		
		DataMap dataMap1 = new DataMap(); 
		dataMap1.put("key11", new DataContainerGVO("data11"));
		dataMap1.put("key12", new DataContainerGVO("data12"));
		dataMap1.put("key13", new DataContainerGVO("data13"));
		dataMap1.put("key14", new DataContainerGVO("data14"));
		dataMap1.put("key15", new DataContainerGVO("data15"));
		dataMap1.put("key16", new DataContainerGVO("data16"));
		DataContainerGVO data1 = new DataContainerGVO(dataMap1);
		
		DataMap dataMap2 = new DataMap(); 
		dataMap2.put("key21", new DataContainerGVO("data21"));
		dataMap2.put("key22", new DataContainerGVO("data22"));
		dataMap2.put("key23", new DataContainerGVO("data23"));
		dataMap2.put("key24", new DataContainerGVO("data24"));
		dataMap2.put("key25", new DataContainerGVO("data25"));
		dataMap2.put("key26", new DataContainerGVO("data26"));
		DataContainerGVO data2 = new DataContainerGVO(dataMap2);
		
		DataMap dataMap3 = new DataMap(); 
		dataMap3.put("key31", new DataContainerGVO("data31"));
		dataMap3.put("key32", new DataContainerGVO("data32"));
		dataMap3.put("key33", new DataContainerGVO("data33"));
		dataMap3.put("key34", new DataContainerGVO("data34"));
		dataMap3.put("key35", new DataContainerGVO("data35"));
		dataMap3.put("key36", new DataContainerGVO("data36"));
		DataContainerGVO data3 = new DataContainerGVO(dataMap3);
		
		List<DataContainerGVO> list  = new ArrayList<DataContainerGVO>();
		list.add(data1);
		list.add(data2);
		list.add(data3);
		
		String exportCode = "TEST_CODE";
		String header = "TEST_HEADER";
		boolean autogenerateColumns = true;
		
		RPCServiceImpl RPCService = new RPCServiceImpl();
		String uuid;
		try {
			uuid = RPCService.prepareForExport(list, exportCode, header, autogenerateColumns);
			List<Map<String, Object>> storedData = DatagridStorageHelper.getData(uuid);
			
			
			for (int i = 0 ; i < storedData.size() ; i ++) {
				int j = 0;
				for (String key : storedData.get(i).keySet()) {
					assertEquals("key" + (i+1) + (j+1) , key);
					assertEquals("data" + (i+1) + (j+1) ,storedData.get(i).get(key));
					j ++;
				}
			}
		} catch (GWTServiceException e) {
			e.printStackTrace();
		}
		
		
	}
	
	
}
