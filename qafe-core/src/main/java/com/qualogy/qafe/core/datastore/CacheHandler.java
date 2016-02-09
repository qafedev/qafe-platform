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
package com.qualogy.qafe.core.datastore;

import java.util.Date;
import java.util.Map;
import java.util.logging.Logger;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

import com.qualogy.qafe.bind.core.application.ApplicationContext;
import com.qualogy.qafe.bind.integration.service.Method;
import com.qualogy.qafe.bind.integration.service.ServiceRef;
import com.qualogy.qafe.platform.distribution.DistributionFactory;

public class CacheHandler {

	private final static Logger logger = Logger.getLogger(CacheHandler.class.getName());
	
	public final static String KEY_SHARED_CACHE = "$SHARED_CACHING";
	public final static String KEY_DELIMITER 	= "_$$_";
	public final static int NO_CACHING 			= -1;
	public final static int UNLIMITED_CACHING 	= 0;
	
	private static CacheHandler instance = null;
	private Map<Long,CacheObject> store;
	
	public static CacheHandler getInstance() {
		if (instance == null) {
			instance = new CacheHandler();
		}
		return instance;
	}
	
	private CacheHandler() {
		store = DistributionFactory.getInstance().getDistributionManager().getCacheManager().getDistributedMap(KEY_SHARED_CACHE);
	}
	
	public void store(String key, Object value) {		
		store.put(getChecksumValue(key), new CacheObject(value));
	}
	
	public Object retrieve(String key) {
		Object cacheObject = store.get(getChecksumValue(key));
		if(cacheObject != null) {
			return ((CacheObject)cacheObject).getValue();
		}
		return null;
	}
	
	public boolean contains(String key) {
		return store.containsKey(getChecksumValue(key));
	}
	
	public void remove(String key) {
		store.remove(getChecksumValue(key));
	}

	public boolean isCached(ServiceRef serviceRef) {
		if (serviceRef != null) {
			Method method = serviceRef.getMethod();
			long cachDuration = getCacheDuration(method);
			if (cachDuration > NO_CACHING) {
				return true;
			}
		}
		return false;
	}
	
	public boolean hasCache(ApplicationContext applicationContext, ServiceRef serviceRef, String cacheToken) {
		if (isCached(serviceRef)) {
			String cacheKey = generateKey(applicationContext, serviceRef, cacheToken);
			if (store.containsKey(getChecksumValue(cacheKey))) {
				Method method = serviceRef.getMethod();
				if (!isCacheExpired(cacheKey, method)) {
					return true;
				}
			}
		}
		return false;
	}
	
	public String generateKey(ApplicationContext applicationContext, ServiceRef serviceRef, String cacheToken) {
		if ((serviceRef != null) && (applicationContext != null)) {
			Method method = serviceRef.getMethod();
			String applicationId = applicationContext.getId().toString();
			String serviceId = serviceRef.getRef().getId();
			String methodId = method.getId();
			return generate(applicationId, serviceId, methodId, cacheToken);
		}
		return null;
	}
	
	private boolean isCacheExpired(String cacheKey, Method method) {
		long expiration = getCacheDuration(method);
		if(expiration == UNLIMITED_CACHING) {
			return false;
		}
		Date cacheStartTime = store.get(getChecksumValue(cacheKey)).getStartTime();
		if (cacheStartTime != null) {
			expiration += cacheStartTime.getTime();	
		}
		Date currentTime = new Date();
		return (currentTime.getTime() > expiration);
	}
	
	private long getCacheDuration(Method method) {
		if (method != null) {
			return method.getCache();
		}
		return NO_CACHING;
	}
	
	private String generate(String... values) {
		StringBuffer sb = new StringBuffer();
		if (values != null) {
			for (int i=0; i<values.length; i++) {
				String value = values[i];
				if (value != null) {
					String delimiter = KEY_DELIMITER;
					if (i == 0) {
						delimiter = "";
					}
					sb.append(delimiter + value);
				}
			}
		}
		if (sb.length() != 0) {
			return sb.toString();
		}
		return null;
	}
	
	public static Long getChecksumValue(String key) {
		Checksum checksum = new CRC32();
		byte[] bytes = key.getBytes();
		checksum.update(key.getBytes(), 0, bytes.length);
		return checksum.getValue();
	}
	
	class CacheObject {		
		private Object value;
		private Date startTime;
		
		public CacheObject(Object value) {
			this.value = value;
			this.startTime = new Date();
		}
		
		public Object getValue() {
			return value;
		}
		public Date getStartTime() {
			return startTime;
		}
	}
}
