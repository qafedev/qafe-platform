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
/*
 * Marc van der Wurff
 * 2007
 */
package com.qualogy.qafe.bind.presentation.event;

import java.io.Serializable;

public class Component implements Serializable {

    private static final long serialVersionUID = 6739256632887179166L;

    protected String componentId;
    
    protected String componentName;
    

    /**
     * Default constructor.
     */
    public Component() {
        super();
    }

    /**
     * Convenience constructor for easily constructing this component with a
     * componentId.
     * 
     * @param componentId
     */
    public Component(String componentId) {
        this();
        this.componentId = componentId;
    }

    public String getComponentId() {
        return componentId;
    }

    public void setComponentId(String componentId) {
        this.componentId = componentId;
    }

	public String getComponentName() {
		return componentName;
	}

	public void setComponentName(String componentName) {
		this.componentName = componentName;
	}
	
	public Component clone(){
		Component c = new Component();
		c.setComponentId(getComponentId());
		c.setComponentName(getComponentName());
		return c;
		
	}
}
