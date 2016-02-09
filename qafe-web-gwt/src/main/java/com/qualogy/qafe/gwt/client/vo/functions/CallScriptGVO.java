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
package com.qualogy.qafe.gwt.client.vo.functions;

import java.util.ArrayList;
import java.util.List;

import com.qualogy.qafe.gwt.client.vo.ui.event.ParameterGVO;

public class CallScriptGVO extends BuiltInFunctionGVO {

    public final static String CLASS_NAME = "com.qualogy.qafe.gwt.client.vo.functions.CallScriptGVO";
    
    public static final String TYPE_JAVASCRIPT = "javascript";
    
    private String type = TYPE_JAVASCRIPT;
    private String functionName;
    private List<ParameterGVO> params = new ArrayList<ParameterGVO>();
       
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
    
    public List<ParameterGVO> getParams() {
        return params;
    }
    
    public void addParam(ParameterGVO param) {
        if (param != null) {
            params.add(param);
        }
    }

    @Override
    public String getClassName() {
        return CLASS_NAME;
    }
}