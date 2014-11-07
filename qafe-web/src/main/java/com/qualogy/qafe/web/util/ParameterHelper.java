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
package com.qualogy.qafe.web.util;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ParameterHelper {
	public final static Logger logger = Logger.getLogger(ParameterHelper.class.getName());

	public static void  processParameters(HttpServletRequest request, HttpServletResponse response){
		// first get the token
		//String uuid = ServletUtilities.getQTokenUUID(request, response);
		//String windowSession  = ServletUtilities.getWindowSession();
		//SessionContainer sessionContainer = new SessionContainer(uuid,windowSession,request.getLocale());
		Map<String,String> parameters = createParamaterMap(request);
		for(Entry<String,String> entry: parameters.entrySet()){
			ServletUtilities.createCookie("qafe-param"+entry.getKey(),entry.getValue(), response);			
		}
		
		
//		Map<String,Map<String,Map<String,String>>>  params =ApplicationCluster.getInstance().getParameters();
//		
//		if (!params.containsKey(uuid)){
//			Map<String,Map<String,String>> paramatersMap = new HashMap<String,Map<String,String>>();
//			params.put(uuid, paramatersMap);
//		}
//		
//		Map<String,Map<String,String>> windowsMap = params.get(uuid);
//		if (windowsMap!=null){
//			if (!windowsMap.containsKey(windowSession)){
//				windowsMap.put(windowSession,new HashMap<String,String>());
//			}
//			Map<String,String> windowsParameters = windowsMap.get(windowSession);
//			if (windowsParameters!=null){
//				for (String paramName : parameters.keySet()) {
//					windowsParameters.put(paramName, parameters.get(paramName));
//				}
//			}
//			
//			// The Application is already loaded through the contextloader.
//					
//			ApplicationCluster.getInstance().setParameters(uuid,windowSession,windowsParameters);
//		} else {
//			logger.debug("Params found for uuid: " + uuid);
//		}
//		// Redirect to the original startup page.
		//RequestDispatcher dispatcher = request.getRequestDispatcher("/index.jsp?uuid="+uuid);
//		return sessionContainer;

	}
	
	private static Map<String,String> createParamaterMap(HttpServletRequest request){
		Enumeration<String> paramsNames = request.getParameterNames();
		Map<String, String> parameterMap = new HashMap<String, String>();
		if (paramsNames != null) {
			while (paramsNames.hasMoreElements()) {
				String param = paramsNames.nextElement();
				 logger.fine("PARAM="+param +"\t VALUE="+ request.getParameter(param));
				parameterMap.put(param, request.getParameter(param));
			}
		}
		return parameterMap;
	}
	
	public static String createParameterInputString(HttpServletRequest request){
		Map<String, String> parameterMap  = createParamaterMap(request);
		StringBuilder buffer = new StringBuilder();
		for (String key :parameterMap.keySet()){
			buffer.append(key+"="+parameterMap.get(key)+"&");
		}
		return buffer.toString();
	}
	
	
}
