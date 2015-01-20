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
package com.qualogy.qafe.jaxrs;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Singleton;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;

import com.qualogy.qafe.bind.business.action.BusinessAction;
import com.qualogy.qafe.bind.commons.type.Parameter;
import com.qualogy.qafe.bind.core.application.ApplicationContext;
import com.qualogy.qafe.bind.core.application.ApplicationIdentifier;
import com.qualogy.qafe.businessaction.BusinessActionHandler;
import com.qualogy.qafe.businessaction.exception.ApplicationNotFoundException;
import com.qualogy.qafe.businessaction.exception.BusinessActionInvokeException;
import com.qualogy.qafe.businessaction.exception.BusinessActionNotFoundException;
import com.qualogy.qafe.businessaction.impl.ApplicationBean;
import com.qualogy.qafe.businessaction.impl.BusinessActionBean;
import com.qualogy.qafe.businessaction.impl.BusinessActionParameterBean;
import com.qualogy.qafe.jaxrs.businessaction.BusinessActionResource;
import com.qualogy.qafe.jaxrs.businessaction.ParameterMessageBodyReaderWriter;

/**
 * REST API tests.
 * 
 * @author sdahlberg
 * 
 */
public final class BusinessActionRestTest extends JerseyTest {

    //@formatter:off
    private final GenericType<List<ApplicationBean>> listOfApps = new GenericType<List<ApplicationBean>>() {};
    
    private final GenericType<List<BusinessActionBean>> listOfActions = 
            new GenericType<List<BusinessActionBean>>() {};
    
    private final GenericType<List<BusinessActionParameterBean>> listOfParameters = 
            new GenericType<List<BusinessActionParameterBean>>() {};
    //@formatter:on

    @Override
    protected Application configure() {
        final ResourceConfig resourceConfig = new ResourceConfig(BusinessActionResource.class);
        resourceConfig.register(new AbstractBinder() {

            @Override
            protected void configure() {
                bind(new TestBusinessActionHandler()).to(BusinessActionHandler.class);
            }
        });
        resourceConfig.register(ParameterMessageBodyReaderWriter.class);
        return resourceConfig;
    }

    @Override
    protected void configureClient(ClientConfig config) {
        super.configureClient(config);
        config.register(ParameterMessageBodyReaderWriter.class);
    }

    @Test
    public void requestApplications() {
        final List<ApplicationBean> applications = target("applications").request().get(listOfApps);
        assertEquals("myappid", applications.get(0).getApplicationId());
    }

    @Test
    public void requestBusinessActions() {
        final List<BusinessActionBean> actions =
            target("applications/myappid/businessactions").request().get(listOfActions);
        assertEquals("myactionid", actions.get(0).getBusinessActionId());
    }

    @Test
    public void invokeBusinessAction() {
        final List<BusinessActionParameterBean> parameters = new ArrayList<BusinessActionParameterBean>();
        final BusinessActionParameterBean parameterBean = new BusinessActionParameterBean();
        parameterBean.setKey("mykey");
        parameterBean.setValue("myValue");
        parameters.add(parameterBean);

        final GenericEntity<List<BusinessActionParameterBean>> genericEntity =
            new GenericEntity<List<BusinessActionParameterBean>>(parameters, listOfParameters.getType());

        final Entity<GenericEntity<List<BusinessActionParameterBean>>> entity =
            Entity.entity(genericEntity, MediaType.APPLICATION_JSON);

        final List<BusinessActionParameterBean> outputParameters =
            target("applications/myappid/businessactions/myactionid").request()
                .post(entity, listOfParameters);

        assertEquals("output", outputParameters.get(0).getKey());
        assertEquals("success", outputParameters.get(0).getValue());
    }

    @Singleton
    private static class TestBusinessActionHandler implements BusinessActionHandler {

        @Override
        public List<ApplicationContext> getApplicationContexts() {
            try {
                return Arrays.asList(getApplicationContext("myappid"));
            } catch (ApplicationNotFoundException e) {
                return Collections.emptyList();
            }
        }

        @Override
        public List<BusinessAction> getBusinessActions(final ApplicationContext context) {
            try {
                BusinessAction businessAction = getBusinessAction(context, "myactionid");
                return Arrays.asList(businessAction);
            } catch (BusinessActionNotFoundException e) {
                return Collections.emptyList();
            }
        }

        @Override
        public BusinessAction getBusinessAction(final ApplicationContext context,
                final String businessActionId) throws BusinessActionNotFoundException {
            if (context.getId().stringValueOf().equals("myappid") && "myactionid".equals(businessActionId)) {
                return new BusinessAction("myactionid");
            }
            return null;
        }

        @Override
        public ApplicationContext getApplicationContext(final String applicationId)
                throws ApplicationNotFoundException {
            if ("myappid".equals(applicationId)) {
                ApplicationContext applicationContext = new ApplicationContext();
                applicationContext.setId(new ApplicationIdentifier("myappid"));
                return applicationContext;
            }
            return null;
        }

        @Override
        public List<Parameter> getInputParameters(final BusinessAction businessAction) {
            final List<Parameter> parameters = new ArrayList<Parameter>();
            parameters.add(new Parameter("hoeba"));
            return parameters;
        }

        @Override
        public Map<String, Object> invokeBusinessAction(final ApplicationContext context,
                final BusinessAction businessAction, final List<BusinessActionParameterBean> input)
                throws BusinessActionInvokeException {

            if ("myappid".equals(context.getId().stringValueOf())
                    && "myactionid".equals(businessAction.getId())) {
                final Map<String, Object> output = new HashMap<String, Object>();
                output.put("output", "success");
                return output;
            }
            return Collections.emptyMap();
        }

    }
}
