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
package com.qualogy.qafe.presentation.handler.executors;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.qualogy.qafe.bind.business.action.BusinessActionItem;
import com.qualogy.qafe.bind.business.action.BusinessActionRef;
import com.qualogy.qafe.bind.commons.type.In;
import com.qualogy.qafe.bind.commons.type.Out;
import com.qualogy.qafe.bind.commons.type.Parameter;
import com.qualogy.qafe.bind.commons.type.Reference;
import com.qualogy.qafe.bind.core.application.ApplicationContext;
import com.qualogy.qafe.bind.integration.service.ServiceRef;
import com.qualogy.qafe.bind.presentation.event.Event;
import com.qualogy.qafe.bind.presentation.event.EventItem;
import com.qualogy.qafe.bind.presentation.event.function.BuiltInFunction;
import com.qualogy.qafe.bind.presentation.event.function.UpdateModel;
import com.qualogy.qafe.core.conflictdetection.ConflicDetectionConstants;
import com.qualogy.qafe.core.datastore.DataIdentifier;
import com.qualogy.qafe.core.datastore.DataStore;
import com.qualogy.qafe.core.errorhandling.ExternalException;
import com.qualogy.qafe.core.framework.business.UnableToManageException;
import com.qualogy.qafe.core.framework.presentation.EventData;
import com.qualogy.qafe.core.framework.presentation.EventHandlerException;
import com.qualogy.qafe.presentation.EventHandlerImpl;
import com.qualogy.qafe.presentation.builtins.BuiltinConvertor;
import com.qualogy.qafe.presentation.builtins.BuiltinConvertorImpl;
import com.qualogy.qafe.presentation.handler.ExecuteEventItem;
import com.qualogy.qafe.util.ExceptionHelper;

public class BusinessActionRefExecute extends AbstractEventItemExecute implements ExecuteEventItem {
	public final static Logger logger = Logger.getLogger(BusinessActionRefExecute.class.getName());

	public boolean execute(EventItem eventItem, ApplicationContext context,
			Event event, EventData eventData, Collection<BuiltInFunction> listToExecute,
			EventHandlerImpl eventHandler, DataIdentifier dataId) throws ExternalException{
	
		if (eventItem instanceof BusinessActionRef) {
			BusinessActionRef businessActionRef = (BusinessActionRef) eventItem;

			try {
				// Before execution, the input parameters, if any, should be resolved (storing in the pipe),
				// because input parameters can refer to an input variable (component) which is not accessible when further processing
				if (businessActionRef.getInput() != null) {
					resolveParameters(context, dataId, businessActionRef.getInput().iterator(), eventData);
					logger.fine("BEFORE BUSINESS ACTION\n"+DataStore.toLogString(dataId));
				}
				
				context.getBusinessManager().manage(context, dataId, businessActionRef.getRef());
					
				if (businessActionRef.getOutput()!=null){
					Iterator<Parameter> itr = businessActionRef.getOutput().iterator();
					while(itr.hasNext()){
						Parameter p = itr.next();
						if (p instanceof Out){
							Out out = (Out)p;
							Object o = getValue(context, dataId, p, eventData);//DataStore.getValue(dataId,out.getRef().getRootRef());
							DataStore.store(dataId, out.getName(), o);
						}
					}
				}
				
				createInternalBuiltIn4UpdatingModels(businessActionRef, listToExecute, dataId);
				
				collectQafeBuiltInsFromBackEnd(context, eventData, listToExecute, dataId);
				
				logger.fine(DataStore.toLogString(dataId));
				
			} catch (ExternalException e) {
				logger.fine(ExceptionHelper.printStackTrace(e));
				logger.fine("AFTER BUSINESS ACTION : "+businessActionRef.getRef()+"\n"+DataStore.toLogString(dataId));
				throw e;
			} catch (UnableToManageException e) {
				logger.fine(ExceptionHelper.printStackTrace(e));
				logger.fine("AFTER BUSINESS ACTION : "+businessActionRef.getRef()+"\n"+DataStore.toLogString(dataId));
				throw new EventHandlerException(e.getMessage(), e);
			} catch (RuntimeException e){
				logger.fine(ExceptionHelper.printStackTrace(e));
				logger.fine("AFTER BUSINESS ACTION : "+businessActionRef.getRef()+"\n"+DataStore.toLogString(dataId));
				throw e;
			} 
		}
		return false;
	}

	private void collectQafeBuiltInsFromBackEnd(ApplicationContext context,
			EventData eventData, Collection<BuiltInFunction> listToExecute,
			DataIdentifier dataId) {
		// refer - Adapter - retrieveQafeBuiltInList method.
		// We have to get the QAFE_BUILT_IN_LIST which is mentioned in the database procedure out variable and convert to the qafe builtins.
		Reference ref = new Reference(DataStore.KEY_WORD_QAFE_BUILT_IN_LIST, Reference.SOURCE_DATASTORE_ID);
		Parameter builtInParameter = new Parameter(ref);
		Object builtInList = getValue(context, dataId, builtInParameter, eventData);
		if(builtInList != null && builtInList instanceof String) {
			BuiltinConvertor builtInConverter = new BuiltinConvertorImpl();
			List<BuiltInFunction> builtInFunctions = builtInConverter.convert((String) builtInList);
			listToExecute.addAll(builtInFunctions);
		}
		
	}
	
	private void createInternalBuiltIn4UpdatingModels(BusinessActionRef businessActionRef, Collection<BuiltInFunction> listToExecute, DataIdentifier dataId) {
		if (DataStore.contains(dataId, DataStore.KEY_SERVICE_MODIFIED_MODELS)) {
			Map<String,List> serviceModifiedModels = (Map<String,List>)DataStore.getValue(dataId, DataStore.KEY_SERVICE_MODIFIED_MODELS);
			for (BusinessActionItem businessActionItem : businessActionRef.getRef().getBusinessActionItems()) {
				if (businessActionItem instanceof ServiceRef) {
					ServiceRef serviceRef = (ServiceRef)businessActionItem;
					String methodId = serviceRef.getMethod().getId();
					if (serviceModifiedModels.containsKey(methodId)) {
						List modifiedModels = serviceModifiedModels.get(methodId);
						Iterator<Parameter> itrParameter = businessActionRef.getInput().iterator();
						while(itrParameter.hasNext()){
							Parameter parameter = itrParameter.next();
							if (parameter instanceof In){
								In in = (In)parameter;
								if (in.getRef() == null) {
									continue;
								}
								String model = in.getName();
								if (in.getRef().isComponentReference() && modifiedModels.contains(model)) {
									UpdateModel updateModel = new UpdateModel();
									updateModel.setRef(in.getRef().getRefWithoutRoot());
									updateModel.setUpdateAction(ConflicDetectionConstants.QAFE_CHECKSUM);
									listToExecute.add(updateModel);
								}
							}
						}
					}
				}
			}
			DataStore.getDataStore(dataId).remove(DataStore.KEY_SERVICE_MODIFIED_MODELS);
		}
	}
}
