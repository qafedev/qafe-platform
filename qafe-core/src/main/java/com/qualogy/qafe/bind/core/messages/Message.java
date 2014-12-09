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
package com.qualogy.qafe.bind.core.messages;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jibx.runtime.IUnmarshallingContext;

import com.qualogy.qafe.bind.PostProcessing;

public class Message implements Serializable, PostProcessing{
	
	/**
	 * if a message is set without locale, the message are
	 * set to this default key
	 */
	public final static String DEFAULT_LOCALE_KEY = "";
	
	/**
	 * bind variables
	 */
	protected String key;
	protected List<MessageValue> messageValues ;
	
	private static final long serialVersionUID = -5190746112696063105L;
	
	/**
	 * postprocessing output
	 */
	private Map<String, String> values = new HashMap<String, String>();

	public Message() {
		super();
	}
	
	public String get(String locale){
		String result =values.get(locale);
		if (result==null){ // so there was no direct hit, maybe the local is for exampe 'nl' while in the messages file 'nl_NL' is specified. Then the next best solution must be searched!
			boolean found=false;
			for (Iterator<String> itr =values.keySet().iterator(); itr.hasNext() &&!found;){
				String key = itr.next();
				if (key!=null && key.startsWith(locale)){
					result = values.get(key);
					found=true;
				}
				
			}
		}
		return result;
	}
	
	public String get(){
		return get(DEFAULT_LOCALE_KEY);
	}

	public void performPostProcessing() {
		if(messageValues!=null){
			for (Iterator<MessageValue> iter = messageValues.iterator(); iter.hasNext();) {
				MessageValue mv = iter.next();
				values.put(mv.getLocale()!=null?mv.getLocale():DEFAULT_LOCALE_KEY, mv.getValue());
			}
		}
	}

	public void put(String value){
		put(DEFAULT_LOCALE_KEY, value);
	}
	
	public void put(String locale, String value){	
		values.put(locale, value);
	}
	
	public void postset(IUnmarshallingContext context) {
		performPostProcessing();
	}
	
	public Map<String, String> toMap(){
		return values;
	}

	protected String getKey() {
		return key;
	}

	protected Map<String, String> getValues() {
		return values;
	}

	public void setKey(String key) {
		this.key = key;
	}
}
