/**
 * Copyright 2008-2016 Qualogy Solutions B.V.
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
package com.qualogy.qafe.core.application;

import com.qualogy.qafe.bind.core.application.ApplicationContext;

/**
 * super-interface for container managers (f.i. a businessmanager)
 * @author 
 *
 */
public interface ContainerManager{
	
	/**
	 * method to initialize the container the manager manages
	 * @param context
	 * @throws InitializationFailedException
	 */
	void init(ApplicationContext context) throws InitializationFailedException;
	
	/**
	 * method to cleanup/destroy any resource that needs to be destroyed for the manager
	 * @param context
	 * @throws DestoryFailedException
	 */
	void destroy(ApplicationContext context) throws DestoryFailedException;
	
	/**
	 * method to reload/refresh the applicationcontext
	 * @param context
	 * @throws InitializationFailedException
	 */
	void refresh(ApplicationContext context) throws InitializationFailedException;
}
