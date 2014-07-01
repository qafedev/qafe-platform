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

import java.util.Map;



public interface EventDataI {

	/**
		 * These fields correspond to attribute names for event in the xml
		 */
		String SOURCE_ID="src-id";
		String SOURCE_NAME="src-name";
		String SOURCE_VALUE="src-value";
		String SOURCE_LISTENER_TYPE="src-listener-type";
		
		String SOURCE_ID_VALUE="src-id-value";
		String SOURCE_NAME_VALUE="src-name-value";
		String SOURCE_VALUE_VALUE="src-value-value";
		String SOURCE_LISTENER_TYPE_VALUE="src-listener-type-value";
		
		@Deprecated
		String REQUEST_BASE_URL="$REQUEST_BASE_URL";
		@Deprecated
		String REQUEST_PROTOCOL="$REQUEST_PROTOCOL";
		@Deprecated
		String REQUEST_SERVER_NAME="$REQUEST_SERVER_NAME";
		@Deprecated
		String REQUEST_PORT="$REQUEST_PORT";
		@Deprecated
		String REQUEST_CONTEXT_ROOT="$REQUEST_CONTEXT_ROOT";

		String REQUEST_GEO = "$REQUEST_GEO";
		String REQUEST_COOKIES = "$COOKIES";
		String REQUEST = "$REQUEST";
		String REQUEST_PROP_BASE_URL="base-url";
		String REQUEST_PROP_PROTOCOL="protocol";
		String REQUEST_PROP_SERVER_NAME="servername";
		String REQUEST_PROP_PORT="port";
		String REQUEST_PROP_CONTEXT_ROOT="context-root";
		
		String MOUSE = "$MOUSE";
		String MOUSE_X = "x";
		String MOUSE_Y = "y";
		
		
		String getSender();
		String getEventName();
		String getEventId();
		String getWindowId();
		@Deprecated
		String getUuid();
		String getSourceValueValue();
		String getSourceValue();
		String getSourceId();                                      
		String getSourceIdValue();
		String getSourceName();
		String getSourceNameValue();
		String getSourceListenerType();
		String getSourceListenerTypeValue();
		String getUserUID();
		String getWindowSession();
		Map<String, Object> getRequest();
		Map<String,String> getMouse();
		String getIndex();
	
}
