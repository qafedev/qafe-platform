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
package com.qualogy.qafe.business;

import java.util.logging.Logger;

import com.qualogy.qafe.bind.business.action.BusinessAction;
import com.qualogy.qafe.bind.core.application.ApplicationContext;
import com.qualogy.qafe.business.action.ItemExecuter;
import com.qualogy.qafe.business.resource.ResourcePool;
import com.qualogy.qafe.core.application.DestoryFailedException;
import com.qualogy.qafe.core.application.InitializationFailedException;
import com.qualogy.qafe.core.conflictdetection.UpdateConflictException;
import com.qualogy.qafe.core.datastore.DataIdentifier;
import com.qualogy.qafe.core.errorhandling.ExternalException;
import com.qualogy.qafe.core.framework.business.BusinessManager;
import com.qualogy.qafe.core.framework.business.UnableToManageException;

/**
 * implementation of the businessprocessor interface.
 * @author Marc van der Wurff
 *
 */
public class BusinessActionManagerImpl implements BusinessManager{
	
	public final static Logger logger = Logger.getLogger(BusinessActionManagerImpl.class.getName());

	public void manage(ApplicationContext context, DataIdentifier dataId, BusinessAction action) throws ExternalException {
		manage(context, dataId, action, false);
	}
	

	public void manage(ApplicationContext context, DataIdentifier dataId, BusinessAction action, boolean nestedAction) throws ExternalException {
		try {
			validate(context, dataId, action, nestedAction);
			doManage(context, dataId, action, nestedAction);
		} catch (ExternalException e) {
			throw e;
		} catch (UpdateConflictException e) {
			throw e;
		} catch (Exception e) {
			String message = e.getMessage();
			throw new UnableToManageException(message, e);
		}
	}
	
	private void validate(ApplicationContext context, DataIdentifier dataId, BusinessAction action, boolean nestedAction) {
		if (action == null) {
			throw new IllegalArgumentException("Business Action is null, possible cause is that the business-action 'ref' does not match with any existing business action in the business tier.");
		}	
    	
    	if (context == null) {
    		throw new IllegalArgumentException("context is null");
    	}	
    	
    	if (dataId == null) {
    		throw new IllegalArgumentException("dataId is null");
    	}	
    		
		if (!nestedAction && action.isPrivate()) {
			throw new IllegalArgumentException("Cannot access private business action [" + action.getId() + "] from outside business tier. Either change the visibility of this action or wrap the action in a public action.");
		}	
	}

	private void doManage(ApplicationContext context, DataIdentifier dataId, BusinessAction action, boolean nestedAction) throws ExternalException {
		ItemExecuter.execute(context, dataId, action.getBusinessActionItems());
		logger.fine("Application: " + (context!=null?context.getName():"") + ", BusinessAction: " + (action!=null?action.getId():""));
	}
	
	public void destroy(ApplicationContext context) throws DestoryFailedException {
		ResourcePool.getInstance().destroy(context);
	}

	public void init(ApplicationContext context) throws InitializationFailedException {
		ResourcePool.getInstance().init(context);
	}

	public void refresh(ApplicationContext context) throws InitializationFailedException {
		ResourcePool.getInstance().init(context);
	}
}