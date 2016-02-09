/**
 * Copyright 2008-2016 Qualogy Solutions B.V.
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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

//import org.apache.jcs.JCS;
//import org.apache.jcs.access.exception.CacheException;

import net.sf.ehcache.Cache;
import net.sf.ehcache.config.Configuration;
import net.sf.ehcache.config.TerracottaClientConfiguration;
import net.sf.ehcache.config.TerracottaConfiguration;
import net.sf.ehcache.management.CacheConfiguration;

public class CacheManager implements com.qualogy.qafe.platform.distribution.api.CacheManager {
	
	private static final CacheManager instance = new CacheManager();
	
	private net.sf.ehcache.CacheManager ehCacheManager;
	
	private CacheManager() {
		if (instance != null) {
			throw new IllegalStateException();
		}
		//this.ehCacheManager = new net.sf.ehcache.CacheManager("/media/Data/develop/temp/qafe-distribution/ehcache-2.4.5/ehcache.xml");
		String ehcacheXML = System.getProperty("qafe-ehcache");
		if ((ehcacheXML == null) || "".equals(ehcacheXML)) {
			System.out.println("##### QAFE Distribution is disabled to enable it pass -Dqafe-ehcache=ehcache-location.xml");
		} else {
			InputStream fis = null;
			try {
				fis = new FileInputStream(new File(ehcacheXML).getAbsolutePath());
				this.ehCacheManager = new net.sf.ehcache.CacheManager(fis);
			} catch (Exception e) {
				//System.out.println(#####)
			} finally {
				try {
					if (fis != null) {
						fis.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static CacheManager getInstance() {
		return instance;
	}

	public Map getDistributedMap(String name) {
		//Cache memoryOnlyCache = new Cache(name, 5000, false, false, 0, 120);
		
		//ehCacheManager.addCache(memoryOnlyCache);
		
		if (ehCacheManager == null) {
			return new HashMap();
		}
        if (distributedQafeCache.equals(name)) {
	        Cache cache = ehCacheManager.getCache(name);
	        assert (cache != null);
	        return new QafeEHCache(cache);
        } else {
			throw new IllegalStateException("only (" + distributedQafeCache + ") is available ! as a distributed Map");
		}
	}
}
