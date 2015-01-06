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
package com.qualogy.qafe.mgwt.client.vo.ui.event;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.qualogy.qafe.mgwt.client.vo.data.EventDataI;


/**
 * @author rjankie
 * This class can be registered to any Component type which needs to
 * handle click events.
 */
public abstract class EventListenerGVO implements IsSerializable {
	
	private String eventId;
	
	private List<ParameterGVO> parameterList= null;
	
	private List<InputVariableGVO> inputvariablesList = null;
	
	private String eventListenerType = null;
	
	private String eventComponentId = null;
	
	private Map<String,String> sourceInfo = new HashMap<String,String>();
	
	
	public Map<String, String> getSourceInfo() {
		return sourceInfo;
	}
	
	public void setSourceInfo(Map<String, String> sourceInfo) {
		this.sourceInfo = sourceInfo;
	}

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}
	
	public EventListenerGVO() {
	}

	public EventListenerGVO(String eventId, final Map<String,String> sourceInfo, List<InputVariableGVO> input, List<ParameterGVO> parameters) {
		this(eventId, sourceInfo, input, parameters, null, null);
	}
	
	public EventListenerGVO(String eventId, final Map<String,String> sourceInfo, List<InputVariableGVO> input, List<ParameterGVO> parameters, String eventListenerType, String eventComponentId) {
		this.inputvariablesList = input;
		this.parameterList = parameters;
		this.sourceInfo = sourceInfo;
		this.eventId = eventId;
		this.eventListenerType = eventListenerType;
		this.eventComponentId = eventComponentId;
	}

	public List<InputVariableGVO> getInputvariablesList() {
		return inputvariablesList;
	}

	public void setInputvariablesList(List<InputVariableGVO> inputvariablesList) {
		this.inputvariablesList = inputvariablesList;
	}

	public List<ParameterGVO> getParameterList() {
		return parameterList;
	}

	public void setParameterList(List<ParameterGVO> parameterList) {
		this.parameterList = parameterList;
	}
	
	private String getProperty(String property){
		if (sourceInfo.containsKey(property)){
			return sourceInfo.get(property);
		} else {
			return null;
		}
	}
	
	public String getSourceId(){
		return getProperty(EventDataI.SOURCE_ID);
	}
	
	public void setSourceId(String value){
		sourceInfo.put(EventDataI.SOURCE_ID, value);
	}
	
	public String getSourceIdValue(){
		return getProperty(EventDataI.SOURCE_ID_VALUE);
	}
	
	public void setSourceIdValue(String value){
		sourceInfo.put(EventDataI.SOURCE_ID_VALUE, value);
	}
	
	public String getSourceName(){
		return getProperty(EventDataI.SOURCE_NAME);
	}
	
	public void setSourceName(String value){
		sourceInfo.put(EventDataI.SOURCE_NAME, value);
	}
	
	public String getSourceNameValue(){
		return getProperty(EventDataI.SOURCE_NAME_VALUE);
	}
	
	public void setSourceNameValue(String value){
		sourceInfo.put(EventDataI.SOURCE_NAME_VALUE, value);
	}

	public String getSourceValue(){
		return getProperty(EventDataI.SOURCE_VALUE);
	}
	
	public void setSourceValue(String value){
		sourceInfo.put(EventDataI.SOURCE_VALUE, value);
	}
	
	public String getSourceValueValue(){
		return getProperty(EventDataI.SOURCE_VALUE_VALUE);
	}
	
	public void setSourceValueValue(String value){
		sourceInfo.put(EventDataI.SOURCE_VALUE_VALUE, value);
	}

	public String getSourceListenerType(){
		return getProperty(EventDataI.SOURCE_LISTENER_TYPE);
	}
	
	public void setSourceListenerType(String value){
		sourceInfo.put(EventDataI.SOURCE_LISTENER_TYPE, value);
	}
	
	public String getSourceListenerTypeValue(){
		return getProperty(EventDataI.SOURCE_LISTENER_TYPE_VALUE);
	}
	
	public void setSourceListenerTypeValue(String value){
		sourceInfo.put(EventDataI.SOURCE_LISTENER_TYPE_VALUE, value);
	}

	public String getEventListenerType() {
		return eventListenerType;
	}

	public String getEventComponentId() {
		return eventComponentId;
	}

}
