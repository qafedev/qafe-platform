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

import java.util.logging.Level;
import java.util.logging.Logger;


public class ContainerManagerFactory {
	
	public final static Logger logger = Logger.getLogger(ContainerManagerFactory.class.getName());
	
	/**
	 * creates manager
	 * @param className
	 * @return
	 */
	public static ContainerManager create(String className){
		if(className==null)
			throw new IllegalArgumentException("cannot create on a null className");
		
		ContainerManager manager = null;
		try {
			logger.log(Level.FINE, "trying to create containermanager [{0}]", className);
			manager = (ContainerManager)Class.forName(className).newInstance();
		} catch (InstantiationException e) {
			throw new UnableToCreateContainerManagerException("error occured while creating EventHandler on className ["+className+"]", e);
		} catch (IllegalAccessException e) {
			throw new UnableToCreateContainerManagerException("error occured while creating EventHandler on className ["+className+"]", e);
		} catch (ClassNotFoundException e) {
			logger.warning("ContainerManager ["+className+"] not loaded since it is not found on your path, no actions can be performed on that container");
		}
		return manager;
	}
}
