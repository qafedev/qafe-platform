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
package com.qualogy.qafe.core.framework.business;

import com.qualogy.qafe.bind.business.action.BusinessAction;
import com.qualogy.qafe.bind.core.application.ApplicationContext;
import com.qualogy.qafe.core.application.ContainerManager;
import com.qualogy.qafe.core.datastore.DataIdentifier;
import com.qualogy.qafe.core.errorhandling.ExternalException;

/**
 * Interface which defines the rules for processing businessactions
 * @author 
 *
 */
public interface BusinessManager extends ContainerManager{
	/**
	 * Method that manages the processing of business actions upon the given business
	 * action. If something with a technical reason fails an UnableTomanageException is thrown.
	 * @param context - the context in which the businessaction must be processed
	 * @param dataId
	 * @param action
	 * @param nestedAction
	 * @throws UnableToManageException - for technical or userinput errors
	 */
	void manage(ApplicationContext context, DataIdentifier dataId, BusinessAction action, boolean nestedAction) throws ExternalException;
	
	void manage(ApplicationContext context, DataIdentifier dataId, BusinessAction action) throws ExternalException;
}
