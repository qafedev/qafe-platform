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

import com.qualogy.qafe.gwt.client.vo.ui.event.ParameterGVO;

public class LocalStoreGVO extends BuiltInFunctionGVO {

    public final static String CLASS_NAME       = "com.qualogy.qafe.gwt.client.vo.functions.LocalStoreGVO";
    
    public final static String ACTION_ADD       = "add";
    public final static String ACTION_DELETE    = "delete";
    public final static String ACTION_SET       = "set";
    
    private String target = SOURCE_DATASTORE_ID;
    private String action = ACTION_SET;
    private ParameterGVO parameter;
    
    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }
    
    public String getAction() {
        return action;
    }
    
    public void setAction(String action) {
        this.action = action;
    }
    
    public ParameterGVO getParameter() {
        return parameter;
    }

    public void setParameter(ParameterGVO parameter) {
        this.parameter = parameter;
    }

    @Override
    public String getClassName() {
        return CLASS_NAME;
    }
}