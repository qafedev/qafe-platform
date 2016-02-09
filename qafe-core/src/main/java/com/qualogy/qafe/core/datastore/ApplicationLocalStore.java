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

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.apache.commons.lang.StringUtils;

import com.qualogy.qafe.platform.distribution.DistributionFactory;

//##hkr
public class ApplicationLocalStore {

	private  final static Logger logger = Logger.getLogger(ApplicationLocalStore.class.getName());

	public final static String KEY_USER_ID 				= "$USER_ID";
	public final static String KEY_SHARED_CACHE 		= "$SHARED_CACHE";
	public final static char OBJECT_DELIMITER			= '.';
	public final static char CONTEXT_DELIMITER			= '|';
	public final static char ATTRIBUTE_DELIMITER		= '@';
	public final static String ATTRIBUTE_SIZE 			= "size";

	private static ApplicationLocalStore singleton=null;

	private Map<String,Map<String,Object>> store;

	private ApplicationLocalStore(){
		store = DistributionFactory.getInstance().getDistributionManager().getCacheManager().getDistributedMap(KEY_SHARED_CACHE);
	}

	public static ApplicationLocalStore getInstance(){
		if (singleton==null){
			singleton = new ApplicationLocalStore();
		}
		return singleton;
	}

	public void storeTemporary(String uuid,String key, Object value){
		store(uuid,key,new TemporaryStoreItem(value));
	}

	public void store(String uuid, String key, Object value){
		debug("store: uuid=" + uuid + " - key=" + key + " - value=" + value);
		Map<String,Object> localStoreMap = store.get(uuid);
		debug("store: uuid=" + uuid + " - localStoreMap=" + localStoreMap);
		if (localStoreMap == null){
			localStoreMap = new DataMap<String,Object>();
		}
		localStoreMap.put(key, value);
		store(uuid, localStoreMap);
		debug("store: uuid=" + uuid + " - localStoreMap=" + localStoreMap);
	}

	public void deleteAll(String uuid){
		Map<String,Object> localStoreMap = store.get(uuid);
		debug("delelteAll: uuid=" + uuid + " - localStoreMap=" + localStoreMap);
		if (localStoreMap != null){
			localStoreMap.clear();
			// Make changes affected to the store when using distributed cache in Google AppEngine
			store(uuid, localStoreMap);
		}
	}

	public void delete(String uuid, String key){
		Map<String,Object> localStoreMap = store.get(uuid);
		debug("delete: uuid=" + uuid + " - localStoreMap=" + localStoreMap);
		if (localStoreMap != null){
			localStoreMap.remove(key);
			// Make changes affected to the store when using distributed cache in Google AppEngine
			store(uuid, localStoreMap);
		}
	}

	public boolean exists(String uuid){
		return (store.get(uuid) != null);
	}

	public boolean contains(String uuid,String key){
		boolean contains=false;
		Map<String,Object> localStoreMap = store.get(uuid);
		if (localStoreMap!=null){
			contains  = localStoreMap.containsKey(key);
		}
		debug("contains: uuid=" + uuid + " - key=" + key + " - contains=" + contains);
		return contains;
	}

    // CHECKSTYLE.OFF: CyclomaticComplexity
	public Object retrieve(String uuid, String key){
		Object returnValue=null;
		Map<String,Object> localStoreMap = store.get(uuid);
		debug("retrieve: uuid=" + uuid + " - localStoreMap=" + localStoreMap);
		if (localStoreMap!=null){
			boolean hasChanges = false;
			if (key!=null){
				String[] keys = StringUtils.split(key,OBJECT_DELIMITER);
				if (keys!=null && keys.length==1 && !key.contains(""+ATTRIBUTE_DELIMITER)){  // simple property
					if(key.contains("[")){
						returnValue = extractDataMapOfIndexFromList(localStoreMap, key);
					} else {
						returnValue = localStoreMap.get(key);
					}
					debug("retrieve: uuid=" + uuid + " - key=" + key + " - returnValue=" + returnValue);
					if (returnValue instanceof TemporaryStoreItem){
						returnValue = ((TemporaryStoreItem)returnValue).getObject();
						localStoreMap.remove(key);
						hasChanges = true;
						debug("retrieve: uuid=" + uuid + " - returnValue(TemporaryStoreItem)=" + returnValue);
					}
				} else if (keys.length==2){ // nested object
					Object mapObject  = null;
					if(keys[0].contains("[")){
						mapObject = extractDataMapOfIndexFromList(localStoreMap, keys[0]);
					} else {
						mapObject = localStoreMap.get(keys[0]);
					}
					debug("retrieve: uuid=" + uuid + " - key=" + key + " - o=" + mapObject);
					if (mapObject instanceof Map){
						returnValue = ((Map)mapObject).get(keys[1]);
						debug("retrieve: uuid=" + uuid + " - returnValue=" + returnValue);
						if (returnValue instanceof TemporaryStoreItem){
							returnValue = ((TemporaryStoreItem)returnValue).getObject();
							localStoreMap.remove(keys[1]);
							hasChanges = true;
							debug("retrieve: uuid=" + uuid + " - returnValue(TemporaryStoreItem)=" + returnValue);
						}
					}
				} else {
					if (key.contains(""+ATTRIBUTE_DELIMITER)){
						String attribute = key.substring(key.indexOf(ATTRIBUTE_DELIMITER)+1,key.length());
						String elementKey = key.substring(0,key.indexOf(ATTRIBUTE_DELIMITER));
						Object object = localStoreMap.get(elementKey);
						debug("retrieve: uuid=" + uuid + " - key=" + key + " - object=" + object);
						if (object instanceof Collection){
							if (ATTRIBUTE_SIZE.equals(attribute)){
								returnValue = ((Collection)object).size();
								debug("retrieve: uuid=" + uuid + " - size=" + returnValue);
							}
						}
					}
				}
			}
			if (hasChanges) {
				// Make changes affected to the store when using distributed cache in Google AppEngine
				store(uuid, localStoreMap);
			}
		}
		return returnValue;
	}
    // CHECKSTYLE.ON: CyclomaticComplexity

	/**
	 * Method to add or update the object in the localstore.
	 *
	 * Checks:
	 *  1. if original is list and toBeAddOrUpdated is list, toBeAddOrUpdated
	 *  will replace old value
	 *  2. if original is list and toBeAddOrUpdated is not a list, toBeAddOrUpdated
	 *  will be set(if object reference exists) or added to list
	 *  3. if object in store is null, given object is stored
	 * @param uuid
	 * @param key
	 * @param original
	 * @return
	 */
	public void addOrUpdate(String uuid, String key, Object value){
		Object updated = update(uuid, key, value);
		if (updated == null){
			store(uuid, key, value);
		}
	}

	/**
	 * Method to update the object in the localstore.
	 *
	 * Checks:
	 *  1. if original is list and toBeAddOrUpdated is list, toBeAddOrUpdated
	 *  will replace old value
	 *  2. if original is list and toBeAddOrUpdated is not a list, toBeAddOrUpdated
	 *  will be set(if object reference exists) or added to list
	 * @param uuid
	 * @param key
	 * @param original
	 * @return
	 */
	public Object update(String uuid, String key, Object value){
		Object original = retrieve(uuid, key);
		if (original instanceof List){
			if(value instanceof List){
				original = value;
			}else{
				Iterator itr = ((List)original).iterator();
				int index=0;
				boolean found=false;
				while(itr.hasNext()&& !found){
					if (itr.next().equals(value)){
						found=true;
					} else {
						index++;
					}
				}
				if (found){
					((List)original).set(index, value);
				}else{
					((List)original).add(value);
				}
			}
		}else if(original!=null){
			original = value;
		}
		return value;
	}

	private Object extractDataMapOfIndexFromList(Map<String,Object> localStoreMap, String key) {
		String extractedKey = key.substring(0, key.indexOf("["));
		int index = Integer.parseInt(key.substring(key.indexOf("[")+1, key.indexOf("]")));
		List<DataMap> retrievedList = (List<DataMap>)localStoreMap.get(extractedKey);
		return retrievedList.get(index);
	}

	private void store(String uuid, Map map){
		store.put(uuid, map);
	}

	private void debug(String message) {
		logger.fine(message);
	}

	public  String toLogString(String uuid){
		return store.get(uuid)!=null?"LocalData for key ["+uuid+"]:\n" + store.get(uuid).toString():null;
	}
}