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
package com.qualogy.qafe.gwt.client.vo.functions;

import java.util.ArrayList;
import java.util.List;

import com.qualogy.qafe.gwt.client.vo.ui.event.ParameterGVO;

public class BusinessActionRefGVO extends BuiltInFunctionGVO {

    public final static String CLASS_NAME = "com.qualogy.qafe.gwt.client.vo.functions.BusinessActionRefGVO";
    
    private String businessActionId;
    private List<ParameterGVO> inputParameters = new ArrayList<ParameterGVO>();
    private List<ParameterGVO> outputParameters = new ArrayList<ParameterGVO>();

    public final String getBusinessActionId() {
        return businessActionId;
    }

    public final void setBusinessActionId(final String businessActionId) {
        this.businessActionId = businessActionId;
    }

    public List<ParameterGVO> getInputParameters() {
        return inputParameters;
    }

    public void setInputParameters(List<ParameterGVO> inputParameters) {
        this.inputParameters = inputParameters;
    }

    public List<ParameterGVO> getOutputParameters() {
        return outputParameters;
    }
    
    public void setOutputParameters(List<ParameterGVO> outputParameters) {
        this.outputParameters = outputParameters;
    }

    @Override
    public final String getClassName() {
        return CLASS_NAME;
    }
}