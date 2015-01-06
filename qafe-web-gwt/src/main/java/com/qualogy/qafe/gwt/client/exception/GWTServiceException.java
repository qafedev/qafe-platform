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
package com.qualogy.qafe.gwt.client.exception;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.qualogy.qafe.gwt.client.vo.data.GDataObject;

public class GWTServiceException extends Exception implements IsSerializable {

	/**
	 * 
	 */
	private String message;
	private String detailedMessage;
	
	public String getDetailedMessage() {
		return detailedMessage;
	}

	public void setDetailedMessage(String detailedMessage) {
		this.detailedMessage = detailedMessage;
	}

	private GDataObject gDataObject;
	
	private static final long serialVersionUID = 3248544762313193159L;
	
	public GWTServiceException(String message, String detailedMessage){
		this(message);
		setDetailedMessage(detailedMessage);
	}
	
	public GWTServiceException(String message) {
		super();
		this.message = message;
	}

	public GWTServiceException() {
		super();
	}

	public GWTServiceException(String message, String detailedMessage, Throwable cause) {
		this(message, cause);
		setDetailedMessage(detailedMessage);
	}
	
	public GWTServiceException(String message, Throwable cause) {
		super(message, cause);
		this.message = message;
	}

	public GWTServiceException(Throwable cause) {
		super(cause);
		message = cause.getMessage();
	}

	public String getMessage() {
		return message;
	}

	public void setGDataObject(GDataObject object) {
		gDataObject = object;
		
	}

	public GDataObject getGDataObject() {
		return gDataObject;
	}
	
	
	
	

}
