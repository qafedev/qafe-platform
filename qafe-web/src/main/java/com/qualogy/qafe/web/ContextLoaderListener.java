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
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class ContextLoaderListener implements ServletContextListener {

	private ContextLoader contextLoader;

	public void contextDestroyed(ServletContextEvent event) {
		if (contextLoader != null) {
			contextLoader.close(event.getServletContext());
		}

	}

	public void contextInitialized(ServletContextEvent event) {
		contextLoader = createContextLoader();
		event.getServletContext().setAttribute("contextloader", contextLoader);
		contextLoader.init(event.getServletContext());
		
	}

	protected ContextLoader createContextLoader() {
		return new ContextLoader();
	}

	public ContextLoader getContextLoader() {
		return contextLoader;
	}

}
