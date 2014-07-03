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
package com.qualogy.qafe.core;

import java.util.HashMap;
import java.util.Map;

public class QafeApplicationContext {
	
	private static final QafeApplicationContext instance = new QafeApplicationContext();
	
	private Map<String, Object> map = new HashMap<String, Object>();

	protected QafeApplicationContext() {
		if (instance != null) {
			throw new IllegalStateException();
		}
	}
	
	public static QafeApplicationContext getInstance() {
		return instance;
	}
	
	public void putInstance(Object value) {
		putInstance(value.getClass().getName(), value);
	}
	
	public void putInstance(Class<?> classAskey, Object value) {
		putInstance(classAskey.getName(), value);
	}
	
	public void putInstance(String key, Object value) {
		map.put(key, value);
	}
	
	public Object getInstance(String key) {
		return map.get(key);
	}
	
	public Object getInstance(Class<?> clazz) {
		return getInstance(clazz.getName());
	}
}

