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
package com.qualogy.ws.businessaction.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.qualogy.qafe.bind.business.action.BusinessAction;
import com.qualogy.qafe.bind.business.action.BusinessActionItem;
import com.qualogy.qafe.bind.business.action.BusinessActionRef;
import com.qualogy.qafe.bind.commons.type.Parameter;
import com.qualogy.qafe.bind.core.application.ApplicationContext;
import com.qualogy.qafe.bind.integration.service.ServiceRef;
import com.qualogy.qafe.core.application.ApplicationCluster;
import com.qualogy.qafe.core.datastore.DataIdentifier;
import com.qualogy.qafe.core.datastore.DataStore;
import com.qualogy.qafe.core.framework.business.BusinessManager;
import com.qualogy.ws.businessaction.BusinessActionExecutor;
import com.qualogy.ws.exception.NotFoundException;

/**
 * Implementation of BusinessActionExecutor. This class is used to retrieve and execute a business action.
 * 
 * @author jroosing
 * 
 */
public class BusinessActionExecutorImpl implements BusinessActionExecutor {

    protected ApplicationContext getApplicationContext(String appId) {
        ApplicationContext applicationContext = null;
        if (applicationContext == null && appId != null) {
            final Iterator<ApplicationContext> itrAppContext = ApplicationCluster.getInstance().iterator();
            while (itrAppContext.hasNext()) {
                final ApplicationContext appContext = itrAppContext.next();
                if (appId.equals(appContext.getId().toString())) {
                    applicationContext = appContext;
                }
            }
        }
        return applicationContext;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Map<String, Object> execute(final String appId, final String businessActionId,
            final Map<String, Object> inputParameters) throws NotFoundException {

        final BusinessAction businessAction = getBusinessAction(appId, businessActionId);

        // determine the names of the output parameters
        List<String> outputNames = generateOutputParamNames(businessAction);

        final Map<String, Object> output =
            executeBusinessAction(appId, businessAction, inputParameters, outputNames);
        return output;
    }

    public List<String> generateOutputParamNames(BusinessAction businessAction) {
        final List<String> outputNames = new ArrayList<String>();
        for (BusinessActionItem item : businessAction.getBusinessActionItems()) {
            List<Parameter> outputParams = null;

            if (item instanceof BusinessActionRef) {
                BusinessActionRef ref = (BusinessActionRef) item;
                outputParams = ref.getOutput();
            } else if (item instanceof ServiceRef) {
                ServiceRef ref = (ServiceRef) item;
                outputParams = ref.getOutput();                        
            }

            if (outputParams == null || outputParams.isEmpty()) {
                break;
            }

            for (Parameter param : outputParams) {
                outputNames.add(param.getName());
            }
        }
        return outputNames;
    }

    public BusinessAction getBusinessAction(final String appId, final String businessActionId)
            throws NotFoundException {
        
        final ApplicationContext appContext = getApplicationContext(appId);
        
        if (appContext == null) {
            throw new NotFoundException("Application with id: [" + appId + "] can not be found");
        }
        
        BusinessAction businessAction = null;

        final List<BusinessAction> businessActions =
            appContext.getApplicationMapping().getBusinessTier().getBusinessActions();
        for (BusinessAction ba : businessActions) {
            if (ba.getId().equals(businessActionId)) {
                businessAction = ba;
                break;
            }
        }

        if (businessAction == null) {
            throw new NotFoundException("Business action with id: [" + businessActionId + "] can not be found");
        }

        return businessAction;
    }

    public Map<String, Object> executeBusinessAction(String appId, BusinessAction businessAction,
            Map<String, Object> input, List<String> outputNames) {
        if (businessAction == null) {
            return null;
        }
        Map<String, Object> output = null;
        try {
            DataIdentifier dataId = DataStore.register();
            try {
                if (input != null) {
                    Iterator<String> itrParamName = input.keySet().iterator();
                    while (itrParamName.hasNext()) {
                        String paramName = itrParamName.next();
                        Object paramValue = input.get(paramName);
                        DataStore.store(dataId, paramName, paramValue);
                    }
                }
                ApplicationContext applicationContext = getApplicationContext(appId);
                BusinessManager businessManager = applicationContext.getBusinessManager();
                businessManager.manage(applicationContext, dataId, businessAction);

                if (outputNames != null) {
                    output = new HashMap<String, Object>();
                    for (String outputName : outputNames) {
                        Object outputValue = DataStore.getValue(dataId, outputName);
                        output.put(outputName, outputValue);
                    }
                }
            } finally {
                DataStore.unregister(dataId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return output;
    }
}
