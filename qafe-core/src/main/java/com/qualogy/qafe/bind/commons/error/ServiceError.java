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
package com.qualogy.qafe.bind.commons.error;

import java.io.Serializable;

import org.jibx.runtime.IUnmarshallingContext;

import com.qualogy.qafe.bind.PostProcessing;

public class ServiceError implements Serializable, PostProcessing {

	private static final long serialVersionUID = -4298632426827669156L;
	
	private String id;
	private String exception;
	private LoggingSettings loggingSettings;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getException() {
		return exception;
	}

	public void setException(String exception) {
		this.exception = exception;
	}

	public LoggingSettings getLoggingSettings() {
		return loggingSettings;
	}
	
	public void setLoggingSettings(LoggingSettings loggingSettings) {
		this.loggingSettings = loggingSettings;
	}
	
	public void postset(IUnmarshallingContext context) {
		performPostProcessing();
	}

	public void performPostProcessing() {
	}
}
