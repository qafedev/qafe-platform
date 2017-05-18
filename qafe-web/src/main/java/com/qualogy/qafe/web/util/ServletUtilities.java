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
package com.qualogy.qafe.web.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.qualogy.qafe.util.UUIDHelper;

public class ServletUtilities {
	
	public final static String QAFE_CONVERSATION_APPID =  "qafe-conversation-appId";
	public final static String QAFE_CONVERSATION_WINDOWSESSION = "qafe-conversation-windowsession";
	
	public static String  initConversationAppId(HttpServletResponse response){
		String uuid= UUIDHelper.generateUUID();		
		//createCookie(QAFE_CONVERSATION_APPID, uuid,response);
		return uuid;
	}
	
	public static String  initConversationWindowSession(HttpServletResponse response){
		String uuid= UUIDHelper.generateUUID();		
	//	createCookie(QAFE_CONVERSATION_WINDOWSESSION,UUIDHelper.generateUUID(),response);
		return uuid;
		
	}
	
	public static String getCookieValue(HttpServletRequest request,String cookieName, String defaultValue){
		return getCookieValue(request.getCookies(),cookieName,defaultValue);
	}
	
	public static String getCookieValue(Cookie[] cookies, String cookieName, String defaultValue) {
		if (cookies!=null){
			for (int i = 0; i < cookies.length; i++) {
				Cookie cookie = cookies[i];
				if (cookieName.equals(cookie.getName()))
					return (cookie.getValue());
			}
		}
		return defaultValue;
	}
	
	
	
	public static void createCookie(String cookieName,String value,HttpServletResponse response){
		Cookie cookie = new Cookie(cookieName,value);
		response.addCookie(cookie);
	}
}
