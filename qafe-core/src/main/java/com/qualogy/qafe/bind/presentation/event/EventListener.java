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
package com.qualogy.qafe.bind.presentation.event;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.jibx.runtime.IUnmarshallingContext;

import com.qualogy.qafe.bind.PostProcessing;

/**
 */
public class EventListener implements Serializable, PostProcessing {
   

	
    /**
	 * 
	 */
	private static final long serialVersionUID = -3525919335925294489L;

	// description fields for this event
    protected List<Component> components;// effective on

    protected List<Listener> listeners ;

        

    public EventListener() {
        super();
    }

    

    /**
     * Adds this listener to the list of listeners. If this list is still null a new list is
     * created.
     * 
     * @param listener
     *                to add
     */
    public void add(final Listener listener) {
        if (listeners == null) {
            listeners = new ArrayList<Listener>();
        }
        listeners.add(listener);
    }

    
    /**
     * Adds this component to the list of components. If this list is still null a new list is
     * created.
     * 
     * @param component
     *                to add
     */
    public void add(final Component component) {
        if (components == null) {
            components = new ArrayList<Component>();
        }
        components.add(component);
    }


    
    public List<Component> getComponents() {
        return components;
    }

    public List<Listener> getListeners() {
        return listeners;
    }

    
    /**
     * Post setter
     * 
     * @param context
     */
    public void postset(IUnmarshallingContext context) {
        performPostProcessing();
    }

    public void performPostProcessing() {
    	
        	    	
    }

	public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
