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

import com.qualogy.qafe.gwt.client.vo.ui.event.ParameterGVO;


public class SetPropertyGVO extends BuiltInFunctionGVO {
    
    public final static String CLASS_NAME = "com.qualogy.qafe.gwt.client.vo.functions.SetPropertyGVO";
    
	private String property;
	private String value;
	private ParameterGVO parameter;
	
	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}
    
    public String getValue() {
        return value;
    }
    
    public void setValue(String value) {
        this.value = value;
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