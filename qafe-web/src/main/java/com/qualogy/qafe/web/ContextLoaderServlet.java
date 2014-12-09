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
package com.qualogy.qafe.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ContextLoaderServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3885341275104853733L;

	private ContextLoader contextLoader;

	/**
	 * Initialize the root web application context.
	 */
	public void init() throws ServletException {
		contextLoader = createContextLoader();
		contextLoader.init(getServletContext());
	}

	/**
	 * Create the ContextLoader to use. Can be overridden in subclasses.
	 * 
	 * @return the new ContextLoader
	 */
	protected ContextLoader createContextLoader() {
		return new ContextLoader();
	}

	/**
	 * Return the ContextLoader used by this servlet.
	 * 
	 * @return the current ContextLoader
	 */
	public ContextLoader getContextLoader() {
		return contextLoader;
	}

	/**
	 * Close the root web application context.
	 */
	public void destroy() {
		if (contextLoader != null) {
			contextLoader.close(getServletContext());
		}
	}

	/**
	 * This should never even be called since no mapping to this servlet should
	 * ever be created in web.xml. That's why a correctly invoked Servlet 2.3
	 * listener is much more appropriate for initialization work ;-)
	 */
	public void service(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		getServletContext().log(
				"Attempt to call service method on ContextLoaderServlet as ["
						+ request.getRequestURI() + "] was ignored");
		response.sendError(HttpServletResponse.SC_BAD_REQUEST);
	}

	public String getServletInfo() {
		return "ContextLoaderServlet for Servlet API 2.2/2.3 "
				+ "(deprecated in favor of ContextLoaderListener for Servlet API 2.4)";
	}

}
