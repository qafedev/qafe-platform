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
package com.qualogy.qafe.gwt.client.util;

import com.google.gwt.user.client.rpc.IncompatibleRemoteServiceException;
import com.google.gwt.user.client.rpc.InvocationException;
import com.qualogy.qafe.gwt.client.exception.GWTApplicationStoreException;

public class ExceptionUtil {
	public static final String messageTitle = "Not able to handle request";
	public static String getMessage(Throwable caught, String message) {
		String messageBody = "";
		try {
			throw caught;
		} catch (IncompatibleRemoteServiceException incompatibleException) {
			messageBody = "This application is out of date, please click the refresh button on your browser.";
		} catch (InvocationException  invocationException) {
			
			messageBody =  "The network connection to the server is temporarily unavailable.Please retry or refresh the browser. If the problem still exists please contact your support team.";						
		} catch (GWTApplicationStoreException  gwtApplicationStoreException) {
			
			messageBody =  "Please refresh the browser. If the problem still exists please contact your support team.";						
		} 
		catch (Throwable te) {
			messageBody = message;		
	    } 
		return messageBody;
	}	
}
