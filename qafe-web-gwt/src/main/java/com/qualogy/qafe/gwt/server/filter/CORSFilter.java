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
package com.qualogy.qafe.gwt.server.filter;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

public class CORSFilter implements Filter {
	
	public enum RequestHeader {
		ACCESS_CONTROL_REQUEST_HEADERS("Access-Control-Request-Headers"),
		ORIGIN("Origin");
		
		private String value;
		
		private RequestHeader(String value) {
			this.value = value;
		}
		
		@Override
		public String toString() {
			return value;
		}
	}
	
	public enum ResponseHeader {
		ACCESS_CONTROL_ALLOW_ORIGIN("Access-Control-Allow-Origin"),
		ACCESS_CONTROL_ALLOW_METHODS("Access-Control-Allow-Methods"),
		ACCESS_CONTROL_ALLOW_HEADERS("Access-Control-Allow-Headers");
		
		private String value;
		
		private ResponseHeader(String value) {
			this.value = value;
		}
		
		@Override
		public String toString() {
			return value;
		}
	}	

    // web.xml cors filter param names
	public static final String PARAM_CORS_ALLOWED_ORIGINS = "cors.allowed.origins";
	public static final String PARAM_CORS_ALLOWED_METHODS = "cors.allowed.methods";
	public static final String PARAM_CORS_ALLOWED_HEADERS = "cors.allowed.headers";
	// default values
	public static final String ALLOWED_ORIGINS_DEFAULT_VALUE = "*";
	public static final String ALLOWED_METHODS_DEFAULT_VALUE = "GET,POST,HEAD,OPTIONS,PUT";
	public static final String ALLOWED_HEADERS_DEFAULT_VALUE = "Content-Type,X-Requested-With,accept,Origin,Access-Control-Request-Method,Access-Control-Request-Headers,X-GWT-Module-Base,X-GWT-Permutation";

	private static Map<String, String> params2ResponseHeaders = new HashMap<String, String>();
	private static Map<String, Object> responseHeaders = new HashMap<String, Object>();
	static {
		params2ResponseHeaders.put(PARAM_CORS_ALLOWED_ORIGINS, ResponseHeader.ACCESS_CONTROL_ALLOW_ORIGIN.toString());
		params2ResponseHeaders.put(PARAM_CORS_ALLOWED_METHODS, ResponseHeader.ACCESS_CONTROL_ALLOW_METHODS.toString());
		params2ResponseHeaders.put(PARAM_CORS_ALLOWED_HEADERS, ResponseHeader.ACCESS_CONTROL_ALLOW_HEADERS.toString());
		
		responseHeaders.put(ResponseHeader.ACCESS_CONTROL_ALLOW_ORIGIN.toString(), ALLOWED_ORIGINS_DEFAULT_VALUE);
		responseHeaders.put(ResponseHeader.ACCESS_CONTROL_ALLOW_METHODS.toString(), ALLOWED_METHODS_DEFAULT_VALUE);
		responseHeaders.put(ResponseHeader.ACCESS_CONTROL_ALLOW_HEADERS.toString(), ALLOWED_HEADERS_DEFAULT_VALUE);
		/*
		 * Following is done to configure response headers based on the request headers
		 * responseHeaders.put(ResponseHeader.ACCESS_CONTROL_ALLOW_HEADERS.toString(), RequestHeader.ACCESS_CONTROL_REQUEST_HEADERS);
		 */
	}
	
	public void init(FilterConfig filterConfig) throws ServletException { 
		Iterator<String> iterParam = params2ResponseHeaders.keySet().iterator();
		while(iterParam.hasNext()) {
			String param = iterParam.next();
			String paramValue = filterConfig.getInitParameter(param);
			if (StringUtils.isNotBlank(paramValue)) {
				String header = params2ResponseHeaders.get(param);
				responseHeaders.put(header, paramValue);
			}
		}
	}

	public void destroy() {
		// TODO Auto-generated method stub
	}
	
	public void doFilter(ServletRequest request, ServletResponse response,FilterChain chain) throws IOException, ServletException {	
		HttpServletRequest req = (HttpServletRequest)request;
		HttpServletResponse resp = (HttpServletResponse)response;
		
		String origin = req.getHeader(RequestHeader.ORIGIN.toString());
		// Check if this is a cross origin request
		if (origin != null) {
			resolveResponse(req,resp);
		}
		chain.doFilter(request, response);
	}
			
	private void resolveResponse(HttpServletRequest request, HttpServletResponse response) {
		Iterator<String> iterHeader = responseHeaders.keySet().iterator();
		while(iterHeader.hasNext()) {
			String header = iterHeader.next();
			Object headerValue = responseHeaders.get(header);
			/*
			 * Following is only needed when we generate the response based on the request headers
			 * 	if (headerValue instanceof RequestHeader) {
					headerValue = getRequestHeader(request, headerValue.toString());
				}
			*/
			if (headerValue instanceof String) {
				response.setHeader(header, (String)headerValue);	
			}
		}
	}
	
	public static final String X_GWT_MODULE_BASE_RESPONSE_HEADER = "X-GWT-Module-Base";
	public static final String X_GWT_PERMUTATION_RESPONSE_HEADER = "X-GWT-Permutation";
	
	public void addGWTSpecificHeaders(HttpServletResponse response) {
		response.setHeader(ResponseHeader.ACCESS_CONTROL_ALLOW_HEADERS.toString(), X_GWT_MODULE_BASE_RESPONSE_HEADER);
		response.setHeader(ResponseHeader.ACCESS_CONTROL_ALLOW_HEADERS.toString(), X_GWT_PERMUTATION_RESPONSE_HEADER);
	}
	
	@SuppressWarnings({ "unused", "unchecked" })
	private String getRequestHeader(HttpServletRequest request, String header) {
		String headerInLowerCase = header.toLowerCase();
		Enumeration<String> headerNames = request.getHeaderNames();
		while (headerNames.hasMoreElements()) {
			String headerName = headerNames.nextElement();
			if (!headerInLowerCase.equals(headerName.toLowerCase())) {
				continue;
			}
			String headerValue = request.getHeader(headerName);
			return headerValue;
		}
		return null;
	}
}
