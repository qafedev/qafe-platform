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
package com.qualogy.qafe.presentation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BusinessActionItemDataObject extends EventItemDataObject implements Serializable {

    private static final long serialVersionUID = 349178336673442712L;

    private String businessActionId;

    private Map<String, Object> inputValues = new HashMap<String, Object>();

    List<String> outputVariables = new ArrayList<String>();

    public BusinessActionItemDataObject(final String appId, final String businessActionId,
            final Map<String, Object> inputValues, List<String> outputVariables) {
        super(appId);
        this.businessActionId = businessActionId;
        this.inputValues = inputValues;
        this.outputVariables = outputVariables;
    }

    public final String getBusinessActionId() {
        return businessActionId;
    }

    public Map<String, Object> getInputValues() {
        return inputValues;
    }

    public void setInputValues(Map<String, Object> inputValues) {
        this.inputValues = inputValues;
    }

    public List<String> getOutputVariables() {
        return outputVariables;
    }

    public void setOutputVariables(List<String> outputVariables) {
        this.outputVariables = outputVariables;
    }

}
