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
package com.qualogy.qafe.jaxrs.businessaction;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.qualogy.qafe.bind.business.action.BusinessAction;
import com.qualogy.qafe.bind.commons.type.Parameter;
import com.qualogy.qafe.bind.core.application.ApplicationContext;
import com.qualogy.qafe.businessaction.BusinessActionHandler;
import com.qualogy.qafe.businessaction.exception.ApplicationNotFoundException;
import com.qualogy.qafe.businessaction.exception.BusinessActionResourceException;
import com.qualogy.qafe.businessaction.impl.ApplicationBean;
import com.qualogy.qafe.businessaction.impl.BusinessActionBean;
import com.qualogy.qafe.businessaction.impl.BusinessActionParameterBean;

/**
 * JAX-RS resource for accessing and invoking business actions.
 * 
 * @author sdahlberg
 * 
 */
@Path("/")
public final class BusinessActionResource {

    @Inject
    private BusinessActionHandler handler;

    @GET
    @Path("applications")
    @Produces(MediaType.APPLICATION_JSON)
    public List<ApplicationBean> getApplications() {

        final List<ApplicationBean> applications = new ArrayList<ApplicationBean>();

        for (final ApplicationContext context : handler.getApplicationContexts()) {
            final ApplicationBean applicationBean = new ApplicationBean();
            applicationBean.setApplicationId(context.getId().toString());
            applications.add(applicationBean);
        }

        return applications;
    }

    @GET
    @Path("applications/{applicationId}/businessactions")
    @Produces(MediaType.APPLICATION_JSON)
    public List<BusinessActionBean> getBussinessActions(
// @formatter:off 
            @PathParam("applicationId") final String applicationId) throws ApplicationNotFoundException {
            // @formatter:on

        final List<BusinessActionBean> businessActions = new ArrayList<BusinessActionBean>();

        final ApplicationContext context = handler.getApplicationContext(applicationId);
        final List<BusinessAction> actions = handler.getBusinessActions(context);

        for (final BusinessAction action : actions) {
            final BusinessActionBean businessActionBean = new BusinessActionBean();
            businessActionBean.setApplicationId(applicationId);
            businessActionBean.setBusinessActionId(action.getId());

            final List<BusinessActionParameterBean> inputParameters =
                new ArrayList<BusinessActionParameterBean>();

            final List<Parameter> parameters = handler.getInputParameters(action);
            for (final Parameter parameter : parameters) {
                final BusinessActionParameterBean inputParameterBean = new BusinessActionParameterBean();
                inputParameterBean.setKey(parameter.getName());
                inputParameters.add(inputParameterBean);
            }
            businessActionBean.setInputParameters(inputParameters);

            businessActions.add(businessActionBean);
        }

        return businessActions;
    }

    @POST
    @Path("applications/{applicationId}/businessactions/{businessActionId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public List<BusinessActionParameterBean> invokeBusinessAction(
// @formatter:off 
            @PathParam("applicationId") final String applicationId,
            @PathParam("businessActionId") final String businessActionId,
            final List<BusinessActionParameterBean> parameters) throws BusinessActionResourceException {            
            // @formatter:on            

        final ApplicationContext context = handler.getApplicationContext(applicationId);
        final BusinessAction businessAction = handler.getBusinessAction(context, businessActionId);

        final Map<String, Object> result = handler.invokeBusinessAction(context, businessAction, parameters);

        final List<BusinessActionParameterBean> outputParameters =
            new ArrayList<BusinessActionParameterBean>();

        for (final Entry<String, Object> entry : result.entrySet()) {

            final BusinessActionParameterBean outputParameterBean = new BusinessActionParameterBean();
            outputParameterBean.setKey(entry.getKey());
            outputParameterBean.setValue(entry.getValue());
            outputParameters.add(outputParameterBean);
        }

        return outputParameters;
    }
}
