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
package com.qualogy.qafe.platform.distribution.impl;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

public class QafeEHCache implements Map {
	
	private Cache cache;
	
	public QafeEHCache(Cache cache) {
		this.cache = cache;
	}
	
	public int size() {
		return cache.getSize();
	}

	public boolean isEmpty() {
		return cache.getSize() < 1;
	}

	public boolean containsKey(Object key) {
		return cache.isKeyInCache(key);
	}

	public boolean containsValue(Object value) {
		return cache.isValueInCache(value);
	}

	public Object get(Object key) {
		Object result = null;
		Element elm = cache.get(key);
		if (elm != null) {
			result = elm.getObjectValue();
		}
		return result;
	}

	public Object put(Object key, Object value) {
		Object previous = get(key);
		cache.put(new Element(key, value));
		return previous;
	}

	public Object remove(Object key) {
		Object previous = get(key);
		cache.remove(key);
		return previous;
	}

	public void putAll(Map m) {
		for (Object key: m.keySet()) {
			put(key, m.get(key));
		}
	}

	public void clear() {
		cache.removeAll();
	}

	public Set keySet() {
		return new HashSet(cache.getKeys());
	}

	public Collection values() {
		Collection values = new LinkedList();
		for (Object key: cache.getKeys()) {
			values.add(cache.get(key));
		}
		return values;
	}

	public Set entrySet() {
		throw new UnsupportedOperationException();
	}

}
