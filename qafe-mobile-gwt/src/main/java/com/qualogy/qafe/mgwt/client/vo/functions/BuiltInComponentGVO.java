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
package com.qualogy.qafe.mgwt.client.vo.functions;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

public class BuiltInComponentGVO implements IsSerializable {

	private String uuid;
	private String componentId;
	private String windowId;
	private String componentUUID;
	private String componentName;
	
	

	private List<String> attributes;
	
	public String getComponentId() {
		return componentId;
	}
	public void setComponentId(String componentId) {
		this.componentId = componentId;
	}
	public String getComponentIdUUID() {
		return componentUUID;
	}
	public void setComponentIdUUID(String componentUUID) {
		this.componentUUID = componentUUID;
	}
	public String getUUID() {
		return uuid;
	}
	public void setUUID(String uuid) {
		this.uuid = uuid;
	}
	public String getWindowId() {
		return windowId;
	}
	public void setWindowId(String windowId) {
		this.windowId = windowId;
	}
	public List<String> getAttributes() {
		return attributes;
	}
	public void setAttributes(List<String> attributes) {
		this.attributes = attributes;
	}
	public String getComponentName() {
		return componentName;
	}
	public void setComponentName(String componentName) {
		this.componentName = componentName;
	}
	
}
