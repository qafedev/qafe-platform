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
package com.qualogy.qafe.businessaction;

import java.util.List;
import java.util.Map;

import com.qualogy.qafe.bind.business.action.BusinessAction;
import com.qualogy.qafe.bind.commons.type.Parameter;
import com.qualogy.qafe.bind.core.application.ApplicationContext;
import com.qualogy.qafe.businessaction.exception.ApplicationNotFoundException;
import com.qualogy.qafe.businessaction.exception.BusinessActionInvokeException;
import com.qualogy.qafe.businessaction.exception.BusinessActionNotFoundException;
import com.qualogy.qafe.businessaction.impl.BusinessActionParameterBean;

/**
 * Handler contract for accessing business actions for some application context.
 * 
 * @author sdahlberg
 * 
 */
public interface BusinessActionHandler {

    /**
     * Returns all application contexts.
     * 
     * @return list of application contexts, or empty list if there are no application contexts.
     */
    List<ApplicationContext> getApplicationContexts();

    /**
     * Returns all business actions for the given application context;
     * 
     * @param context application context to return business actions for.
     * @return list of business actions, or empty list if there are no business actions.
     */
    List<BusinessAction> getBusinessActions(final ApplicationContext context);

    /**
     * Returns a business action for the given business action identifier and the application context.
     * 
     * @param context application context to return a business action for.
     * @param businessActionId business action identifier to look for.
     * @return business action.
     * @throws BusinessActionNotFoundException if no business action can be found.
     */
    BusinessAction getBusinessAction(final ApplicationContext context, final String businessActionId)
            throws BusinessActionNotFoundException;

    /**
     * Returns the application context for the given application context identifier.
     * 
     * @param applicationId application context identifier.
     * @return application context.
     * @throws ApplicationNotFoundException if no application context can be found.
     */
    ApplicationContext getApplicationContext(final String applicationId) throws ApplicationNotFoundException;

    /**
     * Returns input parameters for the given business action.
     * 
     * @param businessAction business action to return its input parameters for.
     * @return list of parameters, or empty list if the business action has no input parameters.
     */
    List<Parameter> getInputParameters(final BusinessAction businessAction);

    /**
     * Invokes the given business action with the given input.
     * 
     * @param context application context the business action must be part of.
     * @param businessAction business action to invoke.
     * @param input list of input parameters the business action needs.
     * @return map of output parameters.
     * @throws BusinessActionInvokeException if some exception occurs during execution of the business action.
     */
    Map<String, Object> invokeBusinessAction(final ApplicationContext context,
            final BusinessAction businessAction, final List<BusinessActionParameterBean> input)
            throws BusinessActionInvokeException;
}
