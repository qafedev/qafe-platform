/**
 * Copyright 2008-2016 Qualogy Solutions B.V.
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
package com.qualogy.qafe.bind.presentation.event.function;

import java.util.List;

import org.jibx.runtime.IUnmarshallingContext;

import com.qualogy.qafe.bind.PostProcessing;
import com.qualogy.qafe.bind.commons.type.Parameter;


public class CallScript extends BuiltInFunction implements PostProcessing {
    
    //private static final Logger LOG = Logger.getLogger(Script.class.getName());

    private static final long serialVersionUID = -8884796796333294852L;
    public static final String TYPE_JAVASCRIPT = "javascript";
    
    private String type = TYPE_JAVASCRIPT;
    private String functionName;
    private List<Parameter> params;
       
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFunctionName() {
        return functionName;
    }

    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }
    
    public List<Parameter> getParams() {
        return params;
    }
    
    public void setParams(List<Parameter> params) {
        this.params = params;
    }

    @Override
    public void postset(IUnmarshallingContext context) {}

    @Override
    public void performPostProcessing() {}    
}
