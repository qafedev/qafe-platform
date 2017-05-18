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
package com.qualogy.qafe.bind.presentation.event.function;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.qualogy.qafe.bind.core.statement.ResultItem;
import com.qualogy.qafe.bind.presentation.event.Component;
import com.qualogy.qafe.bind.presentation.event.EventItem;

public abstract class BuiltInFunction implements EventItem, ResultItem, Serializable, Cloneable {

    private static final long serialVersionUID = -784409194304198353L;

    protected List<Component> components;
 
    public List<Component> getComponents() {
        return components;
    }

    public void setComponents(List<Component> components) {
        this.components = components;
    }
    
    public BuiltInFunction clone() throws CloneNotSupportedException {
    	BuiltInFunction clone = (BuiltInFunction)super.clone();
    	if (components != null) {
    		List<Component> copyComponents = new ArrayList<Component>();
    		for (Component component : components) {
    			Component copyComponent = component.clone();
    			copyComponents.add(copyComponent);
    		}
    	}
		return clone;
	}
}
