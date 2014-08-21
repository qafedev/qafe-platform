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
package com.qualogy.qafe.gwt.client.vo.functions;

import com.qualogy.qafe.gwt.client.vo.ui.event.ParameterGVO;

public class LogFunctionGVO extends BuiltInFunctionGVO {
	public static final String CLASS_NAME = "com.qualogy.qafe.gwt.client.vo.functions.LogFunctionGVO";
	
	private String windowId = null;
	private Boolean debug=Boolean.TRUE;
	private ParameterGVO messageGVO;
	private String message;
	private String styleClass;
	private String[][] styleProperties;
	private Integer delay=2000;
	
	public String getStyleClass() {
		return styleClass;
	}

	public void setStyleClass(String styleClass) {
		this.styleClass = styleClass;
	}

	public Integer getDelay() {
		return delay;
	}

	public void setDelay(Integer delay) {
		this.delay = delay;
	}

	public String[][] getStyleProperties() {
		return styleProperties;
	}

	public void setStyleProperties(String[][] styleProperties) {
		this.styleProperties = styleProperties;
	}
	
	public Boolean getDebug() {
		return debug;
	}

	public void setDebug(Boolean debug) {
		this.debug = debug;
	}

	public String getWindowId() {
		return windowId;
	}

	public void setWindowId(String windowId) {
		this.windowId = windowId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public ParameterGVO getMessageGVO() {
		return messageGVO;
	}

	public void setMessageGVO(ParameterGVO messageGVO) {
		this.messageGVO = messageGVO;
	}
	
	public String getClassName() {
		return CLASS_NAME;
	}	
}
