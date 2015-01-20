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
package com.qualogy.qafe.businessaction.impl;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * JAXB-aware business action bean.
 * 
 * @author sdahlberg
 * 
 */
@XmlRootElement
public final class BusinessActionBean {

    private String applicationId;

    private String businessActionId;

    private List<BusinessActionParameterBean> inputParameters;

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(final String newApplicationId) {
        this.applicationId = newApplicationId;
    }

    public String getBusinessActionId() {
        return businessActionId;
    }

    public void setBusinessActionId(final String newBusinessActionId) {
        this.businessActionId = newBusinessActionId;
    }

    public List<BusinessActionParameterBean> getInputParameters() {
        return inputParameters;
    }

    public void setInputParameters(final List<BusinessActionParameterBean> newInputParameters) {
        this.inputParameters = newInputParameters;
    }
}
