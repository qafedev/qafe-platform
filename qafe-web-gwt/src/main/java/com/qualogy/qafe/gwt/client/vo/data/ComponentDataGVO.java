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
package com.qualogy.qafe.gwt.client.vo.data;

import com.google.gwt.user.client.rpc.IsSerializable;

public class ComponentDataGVO implements IsSerializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3986198350017885720L;
	private String component;
	private String data;
	
	
	public ComponentDataGVO(){
		
	}
	public ComponentDataGVO(String component, String data) {
		super();
		setComponent(component);
		setData(data);
	}

	public String getComponent() {
		return component;
	}
	public void setComponent(String component) {
		this.component = component;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	
}
