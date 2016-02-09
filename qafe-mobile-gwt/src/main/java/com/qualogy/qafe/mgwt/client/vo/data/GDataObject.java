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
package com.qualogy.qafe.mgwt.client.vo.data;

import java.util.HashMap;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.qualogy.qafe.mgwt.client.vo.functions.BuiltInFunctionGVO;

public class GDataObject implements IsSerializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8381696301554889279L;
	private String senderId;
	private String listenerType;
	private String eventId;
	private String uuid;
	private Long time;
	private String parent;
	
	
	private BuiltInFunctionGVO[] functions;
	
	private HashMap<String,ComponentDataGVO> inputComponentDataMap = new HashMap<String,ComponentDataGVO>(17);
	
	private HashMap<String,ComponentDataGVO> outputComponentDataMap = new HashMap<String,ComponentDataGVO>(17);
	
	public HashMap<String,ComponentDataGVO> getOutputComponentDataMap() {
		return outputComponentDataMap;
	}
	public void setOutputComponentDataMap(HashMap<String,ComponentDataGVO> outputComponentDataMap) {
		this.outputComponentDataMap = outputComponentDataMap;
	}
	public String getEventId() {
		return eventId;
	}
	public void setEventId(String eventId) {
		this.eventId = eventId;
	}
	public BuiltInFunctionGVO[] getFunctions() {
		return functions;
	}
	public void setFunctions(BuiltInFunctionGVO[] functions) {
		this.functions = functions;
	}
	public String getSenderId() {
		return senderId;
	}
	public void setSenderId(String senderId) {
		this.senderId = senderId;
	}
	public HashMap<String,ComponentDataGVO> getInputComponentDataMap() {
		return inputComponentDataMap;
	}
	public void setInputComponentDataMap(HashMap<String,ComponentDataGVO> inputComponentDataMap) {
		this.inputComponentDataMap = inputComponentDataMap;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public Long getTime() {
		return time;
	}
	public void setTime(Long time) {
		this.time = time;
	}
	public String getParent() {
		return parent;
	}
	public void setParent(String parent) {
		this.parent = parent;
	}
	public String getListenerType() {
		return listenerType;
	}
	public void setListenerType(String listenerType) {
		this.listenerType = listenerType;
	}
}
