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
package com.qualogy.qafe.bind.presentation.event.function;

import com.qualogy.qafe.bind.commons.type.Parameter;




public class LogFunction extends BuiltInFunction{


	/**
	 * 
	 */
	private static final long serialVersionUID = 9207493049157115183L;
	protected  Parameter message;
	protected  Object messageData;
	protected  Integer delay = 2000;
	public Integer getDelay() {
		return delay;
	}

	public void setDelay(Integer delay) {
		this.delay = delay;
	}

	public String getStyleClass() {
		return styleClass;
	}

	public void setStyleClass(String styleClass) {
		this.styleClass = styleClass;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	protected String styleClass;	
	protected String style;
	
	
	private Boolean debug=Boolean.TRUE;
	
	public Boolean getDebug() {
		return debug;
	}

	public void setDebug(Boolean debug) {
		this.debug = debug;
	}

	public Object getMessageData() {
		return messageData;
	}

	public void setMessageData(Object messageData) {
		this.messageData = messageData;
	}

	public LogFunction(){
		
	}
	
	public LogFunction(String message){		
		setMessageData(message);
	}

	public Parameter getMessage() {
		return message;
	}

	public void setMessage(Parameter message) {
		this.message = message;
	}

	
}
