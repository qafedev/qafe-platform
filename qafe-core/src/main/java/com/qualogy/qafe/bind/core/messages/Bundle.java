/**
 * Copyright 2008-2017 Qualogy Solutions B.V.
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

/**
 * Class explicitly
 * @author 
 *
 */
public class Bundle implements Serializable, PostProcessing{
	
	/**
	 * if a bundle is set without id, the bundle's messages are
	 * merged to a default bundle
	 */
	public final static String DEFAULT_BUNDLE_ID = "";
	
	private static final long serialVersionUID = 1960280186138928411L;
	
	protected String id;
	protected String description;
	
	protected List bindmessages;

	private Map<String, Message> messages;
	
	public String getId() {
		return id;
	}
	
	public String get(String key, String locale){
		String retVal = null;
		
		Message message = (Message)messages.get(key);
		if(message!=null)
			retVal = locale!=null ? message.get(locale) : message.get();
			
		return retVal;
	}
	
	public void performPostProcessing(String root) {
		if(bindmessages!=null){
			messages = new HashMap<String, Message>();
			for (Iterator iter = bindmessages.iterator(); iter.hasNext();) {
				Message message = (Message) iter.next();
				put(message);
			}
		}
	}
	
	/**
     * method to put a placeHolder to a placeHolder list. The list will be
     * created when null 
     * @param placeHolder
     * @throws IllegalArgumentException - when object param passed is null or reference within the item is null
     */
    public void put(Message message) {
    	if(message==null)
    		throw new IllegalArgumentException("message cannot be null");
    	
    	if(message.getKey()==null)
    		throw new IllegalArgumentException("key cannot be null");
    	
    	if(messages==null)
    		messages = new HashMap<String, Message>();
    	
    	messages.put(message.getKey(), message);
    }
	
	public void performPostProcessing() {
		performPostProcessing(null);
	}

	public void postset(IUnmarshallingContext context) {
		performPostProcessing(context.getDocumentName());
	}

	public Map<String, Message> getMessages() {
		return messages;
	}

	public void putAll(Map<String, Message> messages) {
		this.messages.putAll(messages);
	}
}
