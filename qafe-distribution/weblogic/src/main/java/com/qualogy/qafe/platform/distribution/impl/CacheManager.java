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
package com.qualogy.qafe.platform.distribution.impl;

import java.util.Map;

import com.tangosol.net.CacheFactory;
import com.tangosol.net.NamedCache;

public class CacheManager implements com.qualogy.qafe.platform.distribution.api.CacheManager {

	private static CacheManager instance;
	
	public static CacheManager getInstance() {
		if (instance == null) {
			instance = new CacheManager();
		}
		return instance;
	}
	
	private CacheManager() {
	}
	
	public Map getDistributedMap(String name) {		 
		NamedCache sharedCache = null;
		try {
			sharedCache = CacheFactory.getCache(name);			
		} catch (Exception e) {
			e.printStackTrace();
			throw new IllegalStateException(e.getMessage());
		}
		return sharedCache;
	}
}
