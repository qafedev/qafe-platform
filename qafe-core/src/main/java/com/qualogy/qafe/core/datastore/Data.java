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
package com.qualogy.qafe.core.datastore;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.CaseInsensitiveMap;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import com.qualogy.qafe.bind.commons.type.Parameter;


/**
 * Element to be inserted in datastore. This class wraps a Map (CaseInsensitiveMap in this matter)
 * and so supports key, value get and add (put).
 * 
 * The CaseInsensitiveMap allows the data in this class to be searched case insensitive, so for example:
 * 
 * Map map = new CaseInsensitiveMap();
 * map.put("One", "a");
 * map.put("Two", "b");
 * map.put("one", "c");
 * map.put(null, "d");//will throw an illegalargumentexception since key cannot be null
 * 
 * - creates a CaseInsensitiveMap with two entries 
 * - map.get(null) will throw an illegalargumentexception since key cannot be null 
 * - map.get("ONE") returns "c". 
 * - the Set returned by keySet() equals {"one", "two"}. 
 * 
 * NOTE: the CaseInsensitiveMap has one disadvantage, being that the keys are converted before storage
 * 
 * LastTouched (for cleanup purposes) is updated after the following actions on this object:
 *  - add
 *  - get
 *  - create (in constructor)
 * 
 * @author 
 */
@SuppressWarnings("unchecked") //generic class
public class Data {
	
	private long lastTouched;
	private Map elements;
	
	public Map getElements() {
		return elements;
	}

	private Data(){
		//elements = new CaseInsensitiveMap();
		elements = new DataMap();
		updateLastTouched();
	}
	
	protected static Data create(){
		return new Data();
	}
	
	protected void clear(){
		elements.clear();
	}
	/**
	 * lastTouched is updated after get
	 * @param key
	 * @return
	 */
	protected final Object get(String key) throws DataNotFoundException {
		
		updateKey(key);
		
		Object result = elements;
		try{
			if(key.indexOf(Parameter.OBJECT_DELIMITER)>-1){//cheaper to check fst before creating tokenizer
				result = getNested(result, key);
			}else{	
				result = getResult(result, key);
			}
		}catch(DataNotFoundException e){
			throw new DataNotFoundException("Data cannot be found for ["+key+"]", e); 
		}
		
			
		updateLastTouched();
		
		return result;
	}
	
	protected final Object find(String key){
		
		Object result = null;
		try{
			result = get(key);
		}catch(DataNotFoundException e){
			//nothing since it is a find 
		}
		return result;
	}
	
	/**
	 * @pre key is not null
	 * @pre key is to lower case
	 * method returns null when no data found and throws keynotfoundException when key has not been registered
	 * @param from
	 * @param key
	 * @return
	 */
	private Object getResult(Object from, String key) throws DataNotFoundException{
		
		Object result = null;
		if(from!=null){
			if(!(from instanceof Map))
				throw new IllegalArgumentException("Trying to get nested result from a non-nested object. Key that was used: "+key+" on object:"+from+"("+from.getClass()+")");
		
			boolean isList = key.indexOf("[")>-1;
			String getKey = (isList)?key.substring(0, key.indexOf("[")):key;
			
			if(!((Map)from).containsKey(getKey)&& !getKey.contains(""+Parameter.ATTRIBUTE_DELIMITER))
				throw new DataNotFoundException("Data cannot be found for endkey ["+key+"]");
			
			result = ((Map)from).get(getKey);
			
			if(isList && result instanceof List){
				int i = Integer.parseInt(key.substring(key.indexOf("[")+1,key.indexOf("]")));
				result = ((List)result).get(i);
			} else {
				if (key.contains(""+Parameter.ATTRIBUTE_DELIMITER)){
					String attribute = key.substring(key.indexOf(Parameter.ATTRIBUTE_DELIMITER)+1,key.length());
					String elementKey = key.substring(0,key.indexOf(Parameter.ATTRIBUTE_DELIMITER));
					Object object = elements.get(elementKey);
					if (object instanceof Collection){
						if (Parameter.ATTRIBUTE_SIZE.equals(attribute)){
							result = ((Collection)object).size();
						}		
					}
				}
			}
			
		}
		/* Commented coz when only one value is selected in listbox, 
		 * its converted to hashmap instead of keeping the type as list itself
		 * 
		 * if(result instanceof List && ((List)result).size()==1){
			result = ((List)result).get(0);
		}*/
		return result;
	}
	/**
	 * @pre key is not null
	 * @pre key is to lower case	 
	 * @param result
	 * @param key
	 * @return
	 */
	private Object getNested(Object result, String key) throws DataNotFoundException{
		String[] tokens = StringUtils.split(key, Parameter.OBJECT_DELIMITER);
		for (int i = 0; i < tokens.length; i++) {
			result = getResult(result, tokens[i]);
		}
		return result;
	}
	
	protected boolean contains(String key){
		//TODO:come on!!!
		try{
			get(key);
			return true;
		}catch(DataNotFoundException e){
			return false;
		}
	}
	/**
	 * method to add data to this
	 * lastTouched is updated after add
	 * @param key
	 * @param value
	 */
	
	protected final void add(String key, Object value) {
		
		key = updateKey(key);
		
		String[] tokens = StringUtils.split(key, Parameter.OBJECT_DELIMITER);
	
		Object mother = elements;
		for (int i = 0; i < tokens.length; i++) {
			if(i == tokens.length-1){//last token
				String partkey = tokens[i];
				if(tokens[i].indexOf("[")>-1){
					partkey = tokens[i].substring(0, tokens[i].indexOf("["));
					List tmpList = (List)((Map)mother).get(partkey);
					if(tmpList==null)tmpList = new ArrayList();
					value = addToList(tokens[i], tmpList, value);
				}
				((Map)mother).put(partkey, value);
				break;
			}
			//if more tokens, find next object in the map 
			//(assumption for map is because if there are tokens left, token search can only be on a map)
			mother = getNextObj((Map)mother, tokens[i]);
		}
		
		updateLastTouched();
	}
	
	/**
	 * @pre within the list there can be no more map, assuming the last token is past on to this method
	 * @param token
	 * @param mother
	 * @param value
	 */
	private List addToList(String token, List mother, Object value) {
		int indexOfLastStartingBracket = token.indexOf("[");
		int indexOfLastClosingBracket = token.indexOf("]");
		if(indexOfLastClosingBracket<0)
			throw new IllegalArgumentException("specified token ["+token+"], but object for this key is a list, so index is needed");
		while(indexOfLastClosingBracket>-1){//in case of f.i. key[0][0].key
			String indexStr = token.substring(indexOfLastStartingBracket+1, indexOfLastClosingBracket);
			if(!NumberUtils.isNumber(indexStr))
				throw new IllegalArgumentException("index on data, set by [<index>], must be a number, found ["+indexStr+"]"); 
			indexOfLastStartingBracket = token.indexOf("[", indexOfLastClosingBracket+1);
			indexOfLastClosingBracket = token.indexOf("]", indexOfLastClosingBracket+1);
			if(indexOfLastClosingBracket<0){//if this is the last, add the value
				mother.add(Integer.parseInt(indexStr),value);
			}else{
				mother = (List)mother.get(Integer.parseInt(indexStr));
			}
		}
		return mother;
	}

	/**
	 * @pre this is not the deepest nested object in line
	 * @param mother
	 * @param token
	 */
	private Object getNextObj(Map<String, Object> mother, String token){
		Object result = null;
		
		boolean isList = token.indexOf("[")>-1;//according to the user this is a list
		String key = isList?token.substring(0, token.indexOf("[")):token;//get root key, without index
		
		if(!isList && !(mother.get(key) instanceof Map)){//create the map if not exists
			mother.put(key, new HashMap<String,Object>());
		}else if(isList && !(mother.get(key) instanceof List)){//create the list if not exists
			mother.put(key, new ArrayList<Object>());	
		}
		
		if(isList){
			Object tmpresult = (List)mother.get(key);
			int indexOfLastStartingBracket = token.indexOf("[");
			int indexOfLastClosingBracket = token.indexOf("]");
			while(indexOfLastClosingBracket>-1){//in case of f.i. key[0][0].key
				String indexStr = token.substring(indexOfLastStartingBracket+1, indexOfLastClosingBracket);
				
				indexOfLastStartingBracket = token.indexOf("[", indexOfLastClosingBracket+1);
				indexOfLastClosingBracket = token.indexOf("]", indexOfLastClosingBracket+1);
				
				if(!NumberUtils.isNumber(indexStr))
					throw new IllegalArgumentException("index on data, set by [<index>], must be a number, found ["+indexStr+"]");
				
				int listSize = ((List)tmpresult).size();
				if(listSize==Integer.parseInt(indexStr)){
					if(indexOfLastClosingBracket>0){
						((List)tmpresult).add(new ArrayList());
					}else{
						((List)tmpresult).add(new HashMap());
					}
				}else if(Integer.parseInt(indexStr)>listSize){
					throw new IllegalArgumentException("trying to add data on position ["+indexStr+"] in a list of size ["+listSize+"]");
				}
				tmpresult = ((List)tmpresult).get(Integer.parseInt(indexStr));
			}
			result = tmpresult;
		}else{
			result = mother.get(key);
		}
		return result;
	}
	/**
	 * method validates key and updates it to lowercase 
	 * @param key
	 */
	private String updateKey(String key){
		if(key==null)
			throw new IllegalArgumentException("Cannot store data for null key");
		
		if(key.length()==0)
			throw new IllegalArgumentException("Cannot store data for empty key");
		
		if(key.endsWith(""+Parameter.OBJECT_DELIMITER))
			throw new IllegalArgumentException("Cannot end key with "+Parameter.OBJECT_DELIMITER);
		
		return key;
	}
	
	protected boolean isTouchedAfter(long time) {
		return lastTouched>time;
	}
	private void updateLastTouched(){
		lastTouched = Calendar.getInstance().getTime().getTime();
	}
	protected String toLogString(){
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(lastTouched);
		StringBuilder builder = new StringBuilder(255);
		builder.append("Last touched > " + cal.getTime() + (!elements.isEmpty()?"\n":""));
		
		DataToLogStringBuilder.build(elements,builder);
		return builder.toString();
	}
}

