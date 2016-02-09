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
package com.qualogy.qafe.gwt.server.event.assembler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.qualogy.qafe.bind.commons.type.Parameter;
import com.qualogy.qafe.bind.commons.type.Reference;
import com.qualogy.qafe.bind.commons.type.Value;
import com.qualogy.qafe.bind.core.messages.PlaceHolder;
import com.qualogy.qafe.bind.presentation.event.Component;
import com.qualogy.qafe.bind.presentation.event.function.SetValueMapping;
import com.qualogy.qafe.gwt.client.vo.functions.BuiltInComponentGVO;
import com.qualogy.qafe.gwt.client.vo.ui.event.ParameterGVO;

public abstract class AbstractEventItemAssembler implements EventItemAssembler {
    
	protected BuiltInComponentGVO assembleBuiltInComponent(Component component) {
        if (component == null) {
            return null;
        }
		
		BuiltInComponentGVO builtInComponentGVO = new BuiltInComponentGVO();
		builtInComponentGVO.setComponentId(component.getComponentId());
		builtInComponentGVO.setComponentName(component.getComponentName());
		return builtInComponentGVO;		
	}
	
    protected ParameterGVO assembleParameter(Parameter parameter) {
        if (parameter == null) {
            return null;
        }
        
        ParameterGVO parameterGVO = createParameterGVO(parameter);
        
        if (parameter.getPlaceHolders() != null) {
            for(PlaceHolder placeHolder: parameter.getPlaceHolders()) {
                ParameterGVO plHolder = createParameterGVO(placeHolder);
                parameterGVO.addPlaceHolder(plHolder);
            }
        }
        
        return parameterGVO;
    }

    private ParameterGVO createParameterGVO(Parameter parameter) {
    	String name = parameter.getName();
    	ParameterGVO parameterGVO = new ParameterGVO(name);
        if (parameter.getValue() != null) {
            Value value = parameter.getValue();
            parameterGVO.setValue(value.getStaticValue());
        } else if (parameter.getRef() != null) {
            Reference reference = parameter.getRef();
            parameterGVO.setReference(reference.stringValueOf());
            parameterGVO.setSource(reference.getSource());
        } else if (parameter.getExpression() != null) {
        	parameterGVO.setExpression(parameter.getExpression());
        }
        return parameterGVO;
    }
    
    protected Map<String, String> assembleMapping(List<SetValueMapping> mapping) {
        Map<String,String> map = null;
        if (mapping != null) {
            map = new HashMap<String, String>();
            for (SetValueMapping item : mapping) {
                String key = item.getKey();
                String value = item.getValue();
                map.put(key, value);
            }
        }
        return map;
    }
}