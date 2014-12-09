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
package com.qualogy.qafe.business;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.qualogy.qafe.core.datastore.DataIdentifier;

public class StackRecorder {
	
	private static Map<DataIdentifier, List<String>> records = new HashMap<DataIdentifier, List<String>>();
	
	public static void add(DataIdentifier id, String entry){
		List<String> entries = (List<String>)records.get(id);
		if(entries==null)
			entries = new ArrayList<String>();
		entries.add(entry);
		records.put(id, entries);
	}
	
	public static void addHeader(DataIdentifier dataId, String header){
		List<String> entries = (List<String>)records.get(dataId);
		if(entries==null)
			entries = new ArrayList<String>();
		entries.add(0, header);
	}
	
	public static String toLogString(DataIdentifier id){
		return StackBuilder.build((List<String>)records.get(id));
	}
	
	public static void clear(){
		records.clear();
	}
	
	public static void clear(DataIdentifier id){
		records.remove(id);
	}
}
