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

import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Logger;

/**
 * Store for data. Uses the UniqueIdentifier to keep
 * data together.
 *  
 * note: this implementation is not synchronized since it makes
 * use of a HashMap, which isn't
 * 
 * @author Marc van der Wurff
 *
 */
public class DataStore {
	
	public final static Logger logger = Logger.getLogger(DataStore.class.getName());

	/**
	 * 
	 */
	
	public final static String KEY_PARAMETERS ="$PARAMETER";
	/**
	 * keyword reserverd for passing a pagesize
	 */
	public final static String KEY_LOCALE = "$LOCALE";
	/**
	 * keyword reserved for passing a pagesize
	 */
	public final static String KEY_WORD_PAGESIZE = "$PAGESIZE";
	/**
	 * keyword reserved for passing the total amount of pages avialable
	 */
	public final static String KEY_WORD_PAGESAVAILABLE = "$AMOUNT_PAGESAVAILABLE";
	/**
	 * key to store the error message
	 */
	public final static String KEY_ERROR_MESSAGE = "$ERROR_MESSAGE";
	/**
	 * keyword reserved for passing the total amount of results avialable
	 */
	public final static String KEY_WORD_AMNT_RESULTS = "$AMNT_RESULTS";
	/**
	 * keyword reserved for passing a boolean string [true/false] telling to calculate the ttl
	 * amount of pages that will be available based on the result
	 */
	public final static String KEY_WORD_CALCULATEPAGESAVAILABLE = "$COUNTPAGESAVAILABLE";
	/**
	 * keyword reserved for passing a pagenumber
	 */
	public final static String KEY_LOOKUP_DATA = "__$$DATA$$__";
	/**
	 * keyword reserved for passing a pagenumber
	 */
	public final static String KEY_WORD_PAGE_NUMBER = "$OFFSET";
	/**
	 * keyword reserved for passing the last pagenumber
	 */
	public final static String KEY_WORD_PAGE_NUMBER_LAST = "MAX";
	/**
	 * keyword reserved for passing a column to sort on
	 */
	public static final String KEY_WORD_SORT_ON_COLUMN = "$SORT_COLUMN";
	/**
	 * keyword reserved for passing sort order
	 */
	public static final String KEY_WORD_SORT_ORDER = "$SORT_ORDER";
	/**
	 * keyword reserved for indicating that count is expected as result
	 */
	public static final String KEY_WORD_COUNT = "$COUNT";
	
	public static final String KEY_WORD_QAFE_BUILT_IN_LIST = "QAFE_BUILT_IN_LIST";
	
	/**
	 * keys reserved for identifying the models being updated 
	 */
	public static final String KEY_SERVICE_MODIFY = "$SERVICE_MODIFY";
	public static final String KEY_SERVICE_MODIFIED_MODELS = "$SERVICE_MODIFIED_MODELS";
	
	public final static String[] KEY_WORDS = {
		KEY_WORD_PAGE_NUMBER, 
		KEY_WORD_PAGESIZE,
		KEY_WORD_PAGESAVAILABLE,
		KEY_WORD_CALCULATEPAGESAVAILABLE,
		KEY_WORD_SORT_ON_COLUMN,
		KEY_WORD_SORT_ORDER,
		KEY_WORD_COUNT
	};
	
	
	
	private static Map store =  new HashMap();

	/**
	 * Method to check if an entry already exists for given id.
	 * @param id
	 * @return - true if given id is contained whitin the store
	 */
	public static boolean isRegistered(DataIdentifier id){
		return DataStore.store.containsKey(id);
	}

	/**
	 * method to check if a certain key for a given id exists whitin the datastore
	 * @param id
	 * @param key
	 * @return - true when data exists for id and given key 
	 */
	public static boolean contains(DataIdentifier id, String key){
		return DataStore.store.get(id)!=null && ((Data)DataStore.store.get(id)).contains(key);
	}
	
	/**
	 * Method to register with the DataStore so data can be added
	 * whitin a sequence. This method has a uniqueness check,
	 * to make sure the key isn't already used whitin the datastore at
	 * the moment of registration.
	 * @return uniqueIdentifier - a registration ticket
	 */
	public static synchronized DataIdentifier register(){
		DataIdentifier id = DataIdentifier.create();
/*		//since unique is not guaranteed
		int tryoutCounter = 5;		
		while(isRegistered(id) && tryoutCounter-->0){
			id = DataIdentifier.create();
		}
		if(tryoutCounter<0)
			throw new UnknownError("random came back 5 times with a used identifier");
		*/
		DataStore.store.put(id, Data.create());
		
		return id;
	}
	
	/**
	 * Method to store data (value param) for a UniqueId, under the given key.
	 * If an entry already exists for the given key the data will be overridden
	 * (like a HashMap.put())
	 * @param id - uniqueid
	 * @param key - the key the data will be stored under
	 * @param value - the value for this entry
	 * @throws IllegalArgumentException - when id is null or not contained whitin the datastore
	 */
	public static void store(DataIdentifier id, String key, Object value){
		if(id==null)
			throw new IllegalArgumentException("Identifier cannot be null, element cannot be stored on null id");
		
		if(!DataStore.isRegistered(id))
			throw new IllegalArgumentException("Identifier ["+id+"] hasn't been registered with the datastore or has expired");
		
		if(key==null)
			throw new IllegalArgumentException("Cannot store data for null key.");
		
		((Data)DataStore.store.get(id)).add(key, value);
	}
	
	public static void store(DataIdentifier id, String key){
		store(id, key, null);
	}
	
	/**
	 * Method that iterate through a given map to store data .
	 * @param id - uniqueid
	 * @param data - map containing the key value pair
	 */
	public static void store(DataIdentifier id, Map<String, Object> data){
		for(Entry<String, Object> entry: data.entrySet()){
			DataStore.store(id, entry.getKey(), entry.getValue());
		}
	}
	
	/**
	 * This method gets the value for given dataidentifier and key. This method can return null
	 * values when the data for a given key is null. If data cannot be found for specified identifier
	 * a DataNotFoundException is thrown.
	 * @param id - uniqueid
	 * @param key - the key the data will be stored under
	 * @return Object - data found
	 * @throws IllegalArgumentException - when id is not registered whith the datastore or when the key passed is null
	 * @throws DataNotFoundException - when no data exists for given key 
	 *  
	 */
	public static Object getValue(DataIdentifier id, String key){
		if(!DataStore.isRegistered(id))
			throw new IllegalArgumentException("identifier ["+id+"] isn't registered with the datastore or has expired when searching for key:"+key);
		if(key==null)
			throw new IllegalArgumentException("Cannot get data for null search key:"+key);
		
		Data data = (Data)DataStore.store.get(id);
		if(!data.contains(key))
			throw new DataNotFoundException("There is no data stored for Key ["+key+"] and DataIdentifier ["+id+"]");
		
		return data.get(key);
	}
	
	/**
	 * Works like the getValue method with the one exception that when there is no data registered
	 * for the given key null is returned.
	 * @param id - uniqueid
	 * @param key - the key the data will be stored under
	 * @return Object - data found
	 * @throws IllegalArgumentException - when id is not registered whith the datastore or when the key passed is null
	 */
	public static Object findValue(DataIdentifier id, String key){		
		if(!DataStore.isRegistered(id)) {
			logger.info("[thread:"+Thread.currentThread().getId()+"]identifier ["+id+"] isn't registered with the datastore or has expired when searching for key:" + key);
			return null;
		}
		if(key==null) {
			logger.info("[thread:"+Thread.currentThread().getId()+"] Cannot get data for null search key:" + key);
			return null;
		}
		Data data = (Data)DataStore.store.get(id);
		return data.find(key);
	}

	/**
	 * Method to remove entire entry for id, also removes id itself (unregisters). 
	 * @param id
	 */
	public static void unregister(DataIdentifier id){
		DataStore.store.remove(id);
	}
	
	/**
	 * Method to clear entries for an id. This method doesnot remove the id
	 * from the datastore (use unregister for that feature) 
	 * @param id
	 */
	public static void clear(DataIdentifier id){
		((Data)DataStore.store.get(id)).clear();
	}
	
	/**
	 * Method to do a datstore cleanup for data not touched
	 * for a period greather than given timeout 
	 * @param timeout
	 */
	public static void cleanUp(long timeout){
		long nowMinusTimeout = Calendar.getInstance().getTime().getTime()-timeout;
		Set keys = DataStore.store.keySet();
		for (Iterator iter = keys.iterator(); iter.hasNext();) {
			DataIdentifier id = (DataIdentifier) iter.next();
			if(((Data)DataStore.store.get(id)).isTouchedAfter(nowMinusTimeout));
				unregister(id);
		}
	}
	
	/**
	 * Method for displaying purposes, f.i. in a logfile
	 * @param id
	 * @return
	 */
	public static String toLogString(DataIdentifier id){
		return store.get(id)!=null?"Data for key ["+id+"]:\n" + ((Data)store.get(id)).toLogString():null;
	}
	
	/**
	 * Method to return the store as such
	 * @parem id
	 */
	public static Map getDataStore(DataIdentifier id){
		Data d = (Data) store.get(id);
		if (d!=null){
			return d.getElements();
		}
		else {
			return null;
		}
	}
}
