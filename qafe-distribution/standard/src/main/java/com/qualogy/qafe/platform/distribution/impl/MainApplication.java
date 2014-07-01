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

import com.qualogy.qafe.platform.distribution.api.CacheManager;

public class MainApplication {
	
	public static void main(String[] args) {
		//QafeJCS qafeJCS = (QafeJCS) new DistributionManager().getCacheManager().getDistributedMap("qafeCache");
		//qafeJCS.put(args[0], args[1]);
		Map qafeEHCache = new DistributionManager().getCacheManager().getDistributedMap(CacheManager.distributedQafeCache);
		System.out.println("initial size=" + qafeEHCache.size());
		Object result = qafeEHCache.put(args[0], args[1]);
		System.out.println("result="+result);
		result = qafeEHCache.get(args[0]);
		System.out.println("result=" + result);
		System.out.println("last size=" + qafeEHCache.size());
		
		System.out.println("###### Cache Content ######");
		for (Object key: qafeEHCache.keySet()) {
			System.out.println("key=" + key + " value=" + qafeEHCache.get(key));
		}
		try {
			Thread.sleep(1000000);
		} catch (Exception e) {
			
		}
		System.out.println("end of program");
	}
}
