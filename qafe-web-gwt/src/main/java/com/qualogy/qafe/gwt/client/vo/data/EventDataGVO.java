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
package com.qualogy.qafe.gwt.client.vo.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.qualogy.qafe.gwt.client.vo.functions.DataContainerGVO;
import com.qualogy.qafe.gwt.client.vo.ui.event.InputVariableGVO;

public class EventDataGVO implements EventDataI, IsSerializable {

	
	@Deprecated
	private String uuid;
	private String sender;
	private String eventId;
	private String listenerType;
	private String senderName;
	private String userUID;
	private String originalSenderId;
	private String windowSession;
	private String context;
	private String index;

	private Map<String, Object> internalVariables = null;
	private Map<String, Double> locationDataMap = null;
	private Map<String, Object> request = null;
	private Map<String, String> mouse = new HashMap<String,String>();
	

	public Map<String, String> getMouse() {
		return mouse;
	}
	public void setMouseCoordinates (int x, int y){
		mouse.put(MOUSE_X, ""+x );
		mouse.put(MOUSE_Y, ""+y );
		
	}
	public void setMouseCoordinates (String x, String y){
		mouse.put(MOUSE_X,x );
		mouse.put(MOUSE_Y,y );
		
	}
	
	public String getWindowSession() {
		return windowSession;
	}

	public void setWindowSession(String windowSession) {
		this.windowSession = windowSession;
	}

	public String getOriginalSenderId() {
		return originalSenderId;
	}

	public void setOriginalSenderId(String originalSenderId) {
		this.originalSenderId = originalSenderId;
	}

	public String getUserUID() {
		return userUID;
	}

	public void setUserUID(String userUID) {
		this.userUID = userUID;
	}

	public String getSenderName() {
		return senderName;
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

	private Map<String,String> sourceInfo= new HashMap<String,String>();
	public Map<String, String> getSourceInfo() {
		return sourceInfo;
	}

	public void setSourceInfo(Map<String, String> sourceInfo) {
		this.sourceInfo = sourceInfo;
	}

	private String parent;
	
	
	private DataContainerGVO dataContainer;
	
	private List<InputVariableGVO> inputVariables = new ArrayList<InputVariableGVO>();
	private Map<String, String> parameters = null;
	
	public EventDataGVO(){}
	
	public EventDataGVO(String uuid, String sender, String eventId,  final Map<String,String> sourceInfo) {
		super();
		this.uuid = uuid;
		this.sender = sender;
		this.eventId = eventId;
		this.sourceInfo = sourceInfo;
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
	@Deprecated
	public String getUuid() {
		return uuid;
	}
	@Deprecated
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String toString() {
		return "UUID "+ getUuid() +", Sender "+ getSender() + ", EventID "+getEventName();
	}

	

	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

	public DataContainerGVO getDataContainer() {
		return dataContainer;
	}

	public void setDataContainer(DataContainerGVO dataContainer) {
		this.dataContainer = dataContainer;
	}

	public List<InputVariableGVO> getInputVariables() {
		return inputVariables;
	}

	public void setInputVariables(List<InputVariableGVO> inputVariables) {
		this.inputVariables = inputVariables;
	}

	public void setLocationDataMap(Map<String, Double> locationDataMap) {
		this.locationDataMap = locationDataMap;
	}

	public Map<String, Double> getLocationDataMap() {
		return locationDataMap;
	}

	private String getProperty(String property){
		if (sourceInfo.containsKey(property)){
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

	public String getWindowId() {
		return getParent();
	}

	public String getEventName() {
		return getEventId();
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

	public Map<String,Object> getInternalVariables() {
		return internalVariables;
	}

	public void setInternalVariables(Map<String,Object> internalVariables) {
		this.internalVariables = internalVariables;
	}

	public Map<String, Object> getRequest() {
		return request;
	}
	
	public void setRequest(Map<String, Object> request) {
		this.request = request;
	}

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}
	
	public void setParameters(Map<String,String> map){
		parameters  = map;
	}
	public Map<String,String> getParameters(){
		return parameters;
	}
	public String getIndex() {
		return index;
	}
	public void setIndex(String index) {
		this.index = index;
	}

}
