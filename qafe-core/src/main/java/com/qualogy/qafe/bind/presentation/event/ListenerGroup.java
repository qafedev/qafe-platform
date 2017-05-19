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
package com.qualogy.qafe.bind.presentation.event;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author rjankie
 */
public class ListenerGroup implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7261081489039398530L;
	protected List<Component> components;	
	protected List<Listener> listenerTypes;
	
	public List<Component> getComponents() {
		return components;
	}
	
	/**
	 * @deprecated use add
	 * @param components
	 */
	public void setComponents(List<Component> components) {
		this.components = components;
	}
	
	
	public List<Listener> getListenerTypes() {
		return listenerTypes;
	}
	
	/**
	 * @deprecated use add
	 * @return
	 */
	public void setListenerTypes(List<Listener> listenerTypes) {
		this.listenerTypes = listenerTypes;
	}
	
    /**
     * Adds this component to the list of components. If this list is still null a new list is
     * created.
     * @param component to add
     */
    public void add(final Component component) {
        if (components == null) {
            components = new ArrayList<Component>();
        }
        components.add(component);
    }
    
    public void add(final Listener listener) {
        if (listenerTypes == null) {
        	listenerTypes = new ArrayList<Listener>();
        }
        listenerTypes.add(listener);
    }


}
