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
package com.qualogy.qafe.presentation;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.qualogy.qafe.bind.core.application.ApplicationIdentifier;
import com.qualogy.qafe.bind.presentation.event.InputVariable;
import com.qualogy.qafe.core.datastore.ApplicationStoreIdentifier;
import com.qualogy.qafe.core.framework.presentation.EventData;

public class EventDataObject implements EventData, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3437491783315344271L;

	private Map<String,String> sourceInfo = null;
	private String eventId;
	private ApplicationIdentifier applicationIdentifier;
	private String listenerType;
	private String senderName;
	private String windowSession;
	
	private Map<String, Object> request = null;
	private Map<String, String> mouse = null;
	

	public String getWindowSession() {
		return windowSession;
	}

	public void setWindowSession(String windowSession) {
		this.windowSession = windowSession;
	}

	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}

	public String getListenerType() {
		return listenerType;
	}

	public void setListenerType(String listenerType) {
		this.listenerType = listenerType;
	}

	private List<InputVariable> inputVariables;
	private Map<String,Object> internalVariables;
	private String sender;
	private String windowId;
	private ApplicationStoreIdentifier applicationStoreIdentifier;
	@Deprecated
	private String uuid;
	private Map<String,String > urlParameters = null;
	
	private String userUID;

	// private String sourceValue;
	
	public EventDataObject(String eventId, Map<String, String> sourceInfo, ApplicationIdentifier applicationIdentifier, String listenerType, List<InputVariable> params, String sender, String senderName, String windowId, String uuid, String userUID,String windowSession, Map<String, Object> request,Map<String,String> urlParameters) {
		this(eventId, sourceInfo, applicationIdentifier, listenerType, params, null, sender, senderName, windowId, uuid, userUID, windowSession, request,urlParameters,null);
	}
	
	public EventDataObject(String eventId, Map<String, String> sourceInfo, ApplicationIdentifier applicationIdentifier, String listenerType, List<InputVariable> params, Map<String,Object> internalParams, String sender, String senderName, String windowId, String uuid, String userUID,String windowSession, Map<String, Object> request,Map<String,String> urlParameters,Map<String,String> mouse) {
		super();
		this.eventId = eventId;
		this.applicationIdentifier = applicationIdentifier;
		this.listenerType = listenerType;
		this.inputVariables = params;
		this.internalVariables = internalParams;
		this.sender = sender;
		this.windowId = windowId;
		this.uuid = uuid;
		this.userUID = userUID;
		this.sourceInfo = sourceInfo;
		this.senderName = senderName;
		this.windowSession = windowSession;
		this.request = request;
		this.urlParameters = urlParameters;
		this.mouse = mouse;
	}

	public ApplicationIdentifier getApplicationIdentifier() {
		return applicationIdentifier;
	}

	public void setApplicationIdentifier(ApplicationIdentifier applicationIdentifier) {
		this.applicationIdentifier = applicationIdentifier;
	}

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getWindowId() {
		return windowId;
	}

	public void setWindowId(String windowId) {
		this.windowId = windowId;
	}

	public List<InputVariable> getInputVariables() {
		return inputVariables;
	}

	public void setInputVariables(List<InputVariable> inputVariables) {
		this.inputVariables = inputVariables;
	}

	public Map getInternalVariables() {
		return internalVariables;
	}

	public void setInternalVariables(Map internalVariables) {
		this.internalVariables = internalVariables;
	}
	
	public ApplicationStoreIdentifier getApplicationStoreIdentifier() {
		return applicationStoreIdentifier;
	}

	public void setApplicationStoreIdentifier(ApplicationStoreIdentifier applicationStoreIdentifier) {
		this.applicationStoreIdentifier = applicationStoreIdentifier;
	}
	@Deprecated
	public String getUuid() {
		return uuid;
	}
	@Deprecated
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	private String getProperty(String property) {
		if (sourceInfo.containsKey(property)) {
			return sourceInfo.get(property);
		} else {
			return null;
		}
	}

	public String getSourceId() {
		return getProperty(SOURCE_ID);
	}

	public String getSourceIdValue() {
		return getProperty(SOURCE_ID_VALUE);
	}

	public String getSourceName() {
		return getProperty(SOURCE_NAME);
	}

	public String getSourceNameValue() {
		return getProperty(SOURCE_NAME_VALUE);
	}

	public String getSourceValue() {
		return getProperty(SOURCE_VALUE);
	}

	public String getSourceValueValue() {
		return getProperty(SOURCE_VALUE_VALUE);
	}

	public String getSourceListenerType() {
		return getProperty(SOURCE_LISTENER_TYPE);
	}

	public String getSourceListenerTypeValue() {
		return getProperty(SOURCE_LISTENER_TYPE_VALUE);
	}

	public void setSourceId(String value) {
		sourceInfo.put(SOURCE_ID, value);
	}

	public void setSourceIdValue(String value) {
		sourceInfo.put(SOURCE_ID_VALUE, value);
	}

	public void setSourceName(String value) {
		sourceInfo.put(SOURCE_NAME, value);
	}

	public void setSourceNameValue(String value) {
		sourceInfo.put(SOURCE_NAME_VALUE, value);
	}

	public void setSourceValue(String value) {
		sourceInfo.put(SOURCE_VALUE, value);
	}

	public void setSourceValueValue(String value) {
		sourceInfo.put(SOURCE_VALUE_VALUE, value);
	}

	public void setSourceListenerType(String value) {
		sourceInfo.put(SOURCE_LISTENER_TYPE, value);
	}

	public void setSourceListenerTypeValue(String value) {
		sourceInfo.put(SOURCE_LISTENER_TYPE_VALUE, value);
	}

	public String getSenderName() {
		return senderName;
	}

	public String getUserUID() {
		return userUID;
	}

	public void setUserUID(String userUID) {
		this.userUID = userUID;
	}

	public Map<String, Object> getRequest() {
		return request;
	}

	public EventData clone() {
		EventDataObject eventData = new EventDataObject(eventId, sourceInfo, applicationIdentifier, listenerType, inputVariables, internalVariables, sender, senderName, windowId, uuid, userUID,windowSession, request,urlParameters,mouse);
		return eventData;
	}

	public Map<String, String> getParameters() {
		return urlParameters;
	}

	public Map<String, String> getMouse() {
		return mouse;
	}
}
