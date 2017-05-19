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
package com.qualogy.qafe.gwt.client.vo.functions;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

public abstract class BuiltInFunctionGVO implements IsSerializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4689414680328141882L;
	
	public final static String SOURCE_APP_LOCAL_STORE_ID        = "user";
    public final static String SOURCE_APP_GLOBAL_STORE_ID       = "global";
    public final static String SOURCE_COMPONENT_ID              = "component";
    public final static String SOURCE_DATASTORE_ID              = "pipe";
    public final static String SOURCE_MESSAGE_ID                = "message";

	private String context;
	private String uuid;
	private String senderId;
	private String listenerType;
	
	protected List<BuiltInComponentGVO> components;

	public List<BuiltInComponentGVO> getComponents() {
		return components;
	}

	public void setComponents(List<BuiltInComponentGVO> components) {
		this.components = components;
	}

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}
	
	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getSenderId() {
		return senderId;
	}

	public void setSenderId(String senderId) {
		this.senderId = senderId;
	}

	public String getListenerType() {
		return listenerType;
	}

	public void setListenerType(String listenerType) {
		this.listenerType = listenerType;
	}
	
	public abstract String getClassName();
}