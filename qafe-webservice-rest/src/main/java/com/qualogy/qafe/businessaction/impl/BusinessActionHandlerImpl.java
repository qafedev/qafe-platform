/**
 * Copyright 2008-2017 Qualogy Solutions B.V.
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
package com.qualogy.qafe.businessaction.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.inject.Singleton;

import com.qualogy.qafe.bind.business.action.BusinessAction;
import com.qualogy.qafe.bind.business.action.BusinessActionItem;
import com.qualogy.qafe.bind.business.action.BusinessActionRef;
import com.qualogy.qafe.bind.commons.type.Parameter;
import com.qualogy.qafe.bind.core.application.ApplicationContext;
import com.qualogy.qafe.bind.integration.service.ServiceRef;
import com.qualogy.qafe.businessaction.BusinessActionHandler;
import com.qualogy.qafe.businessaction.exception.ApplicationNotFoundException;
import com.qualogy.qafe.businessaction.exception.BusinessActionInvokeException;
import com.qualogy.qafe.businessaction.exception.BusinessActionNotFoundException;
import com.qualogy.qafe.core.application.ApplicationCluster;
import com.qualogy.qafe.core.datastore.DataIdentifier;
import com.qualogy.qafe.core.datastore.DataStore;
import com.qualogy.qafe.core.errorhandling.ExternalException;
import com.qualogy.qafe.core.framework.business.BusinessManager;

/**
 * Handler that acts as glue between qafe-core API and JAX-RS resource.
 * 
 * @author sdahlberg
 * 
 */
@Singleton
public final class BusinessActionHandlerImpl implements BusinessActionHandler {

    @Override
    public List<ApplicationContext> getApplicationContexts() {

        final List<ApplicationContext> contexts = new ArrayList<ApplicationContext>();

        final Iterator<ApplicationContext> iterator = ApplicationCluster.getInstance().iterator();
        while (iterator.hasNext()) {
            contexts.add(iterator.next());
        }

        return contexts;
    }

    @Override
    public List<BusinessAction> getBusinessActions(final ApplicationContext context) {
        return context.getApplicationMapping().getBusinessTier().getBusinessActions();
    }

    @Override
    public BusinessAction getBusinessAction(final ApplicationContext context, final String businessActionId)
            throws BusinessActionNotFoundException {

        final List<BusinessAction> actions = getBusinessActions(context);

        for (final BusinessAction action : actions) {
            if (businessActionId.equals(action.getId())) {
                return action;
            }
        }

        throw new BusinessActionNotFoundException("No business action found with ID: " + businessActionId);
    }

    @Override
    public ApplicationContext getApplicationContext(final String applicationId)
            throws ApplicationNotFoundException {

        if (applicationId == null || applicationId.isEmpty()) {
            throw new IllegalArgumentException("Empty parameter applicationId");
        }

        for (final ApplicationContext context : getApplicationContexts()) {
            if (applicationId.equals(context.getId().toString())) {
                return context;
            }
        }

        throw new ApplicationNotFoundException("No application found with ID: " + applicationId);
    }

    @Override
    public List<Parameter> getInputParameters(final BusinessAction businessAction) {

        final List<Parameter> parameters = new ArrayList<Parameter>();

        final List<BusinessActionItem> items = businessAction.getBusinessActionItems();

        for (final BusinessActionItem item : items) {
            final List<Parameter> input;
            if (item instanceof BusinessActionRef) {
                final BusinessActionRef ref = (BusinessActionRef) item;
                input = ref.getInput();
            } else if (item instanceof ServiceRef) {
                input = getUncheckedInput((ServiceRef) item);
            } else {
                throw new IllegalStateException("Business action item is of unknown type: " + item);
            }

            if (input != null) {
                parameters.addAll(input);
            }
        }

        return parameters;
    }

    @SuppressWarnings("unchecked")
    private List<Parameter> getUncheckedInput(final ServiceRef ref) {
        return ref.getInput();
    }

    @SuppressWarnings("unchecked")
    private List<Parameter> getUncheckedOutput(final ServiceRef ref) {
        return ref.getOutput();
    }

    @Override
    public Map<String, Object> invokeBusinessAction(final ApplicationContext context,
            final BusinessAction businessAction, final List<BusinessActionParameterBean> input)
            throws BusinessActionInvokeException {

        try {
            return invokeBusinessAction(context, businessAction, input,
                generateOutputParamNames(businessAction));
        } catch (ExternalException e) {
            throw new BusinessActionInvokeException("Business action with ID: " + businessAction.getId()
                    + " could not be invoked. Result is unknow.", e);
        }
    }

    private Map<String, Object> invokeBusinessAction(final ApplicationContext context,
            final BusinessAction businessAction, final List<BusinessActionParameterBean> input,
            final List<String> outputNames) throws ExternalException {

        final DataIdentifier dataId = DataStore.register();

        try {

            if (input != null) {
                for (final BusinessActionParameterBean inputParameter : input) {
                    DataStore.store(dataId, inputParameter.getKey(), inputParameter.getValue());
                }
            }

            final BusinessManager businessManager = context.getBusinessManager();
            businessManager.manage(context, dataId, businessAction);

            if (outputNames != null) {
                final Map<String, Object> output = new HashMap<String, Object>();
                for (final String outputName : outputNames) {
                    final Object outputValue = DataStore.getValue(dataId, outputName);
                    output.put(outputName, outputValue);
                }
                return output;
            }
        } finally {
            DataStore.unregister(dataId);
        }

        return Collections.emptyMap();
    }

    private List<String> generateOutputParamNames(final BusinessAction businessAction) {
        final List<String> outputNames = new ArrayList<String>();
        for (final BusinessActionItem item : businessAction.getBusinessActionItems()) {

            final List<Parameter> outputParams;
            if (item instanceof BusinessActionRef) {
                final BusinessActionRef ref = (BusinessActionRef) item;
                outputParams = ref.getOutput();
            } else if (item instanceof ServiceRef) {
                outputParams = getUncheckedOutput((ServiceRef) item);
            } else {
                throw new IllegalStateException("Business action item is of unknown type: " + item);
            }

            if (outputParams == null) {
                continue;
            }

            for (final Parameter param : outputParams) {
                outputNames.add(param.getName());
            }
        }
        return outputNames;
    }

}
