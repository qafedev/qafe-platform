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
package com.qualogy.qafe.util.preferences;

import java.util.Map;
import java.util.TreeMap;

public class Preferences {
		
	private Map<String, Object> map;
	
	public Preferences() {
		this.map = new TreeMap<String, Object>();
	}
	
	public Preferences(Object[] keyValuePairs) {
		this.map = new TreeMap<String, Object>();
		for (int i = 0; i < keyValuePairs.length; i += 2) {
			map.put(((String)keyValuePairs[i]), keyValuePairs[i+1]);
		}
	}
	
	public Object get(String key) {
		return map.get(key);
	}
	
	public Object put(String key, Object value) {
		return map.put(key, value);
	}
	
	public Map<String, Object> getAll() {
		return map;
	}
}
