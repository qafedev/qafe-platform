/**
 * Copyright 2008-2014 Qualogy Solutions B.V.
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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.jibx.runtime.IUnmarshallingContext;

import com.qualogy.qafe.bind.PostProcessing;
import com.qualogy.qafe.bind.commons.type.Inputable;
import com.qualogy.qafe.bind.domain.BindBase;
import com.qualogy.qafe.bind.util.InterfaceScanner;
import com.qualogy.qafe.core.id.UniqueIdentifier;

/**
 */
public class Event extends BindBase implements PostProcessing {
   
	public static final String PREFIX_EVENT_ID = "QAFE_INTERNAL_";
	
	/**
     * 
     */
    private static final long serialVersionUID = 1173290029173902191L;


    private List<ListenerGroup> listeners = new ArrayList<ListenerGroup>();
    private List<ListenerGroup> listenerGroupsDefinedAsEventAttribute = new ArrayList<ListenerGroup>();
    private String id;
    private String sourceName;
    private String sourceId;
    private String sourceValue;
    private String sourceListenerType;
    private String components;
    private String listenerTypes;
    private String windowId;
    private List<EventItem> eventItems = new ArrayList<EventItem>();
    private List<InputVariable> input;

    public Event() {
        super();
    }
    
    public Event(String id, List<EventItem> eventItems){
    	this.id= id;
    	this.eventItems = eventItems;
	}

    public String getWindowId() {
		return windowId;
	}

	public void setWindowId(String windowId) {
		this.windowId = windowId;
	}
	
    public String getSourceName() {
		return sourceName;
	}

	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}

	public String getSourceId() {
		return sourceId;
	}

	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}

	public String getSourceValue() {
		return sourceValue;
	}

	public void setSourceValue(String sourceValue) {
		this.sourceValue = sourceValue;
	}
	
	public String getSourceListenerType() {
		return sourceListenerType;
	}

	public void setSourceListenerType(String sourceListenerType) {
		this.sourceListenerType = sourceListenerType;
	}
	
	public void setComponents(String components) {
		this.components = components;
	}

	private String getComponents() {
		return this.components;
	}

	public void setListenerTypes(String listenerTypes) {
		this.listenerTypes = listenerTypes;
	}

	private String getListenerTypes() {
		return this.listenerTypes;
	}
	
	/**
	 * @return
	 */
	private List<ListenerGroup> getListenerGroupDefinedAsEventAttribute(){
		List<ListenerGroup> listenerGroups = new ArrayList<ListenerGroup>();
		List<Component> componentList = getComponentList(getComponents());
		List<Listener> listenerTypeList = getListenerTypeList(getListenerTypes());
		
		ListenerGroup listenerGroup = new ListenerGroup();
		listenerGroup.setComponents(componentList);
		listenerGroup.setListenerTypes(listenerTypeList);
		
		listenerGroups.add(listenerGroup);
		
		return listenerGroups;
	}

	/**
	 * @param components
	 * @return
	 */
	private List<Component> getComponentList(String components){
		List<Component> componentList = new ArrayList<Component>();
		String[] componentsString = StringUtils.split(components, ",");
		if (componentsString != null){
			for (String comp : componentsString){
				Component component = new Component();
				component.setComponentId(comp);
				componentList.add(component);
			}
		}
		return componentList;
	}
	
	/**
	 * @param listenerTypes
	 * @return
	 */
	private List<Listener> getListenerTypeList(String listenerTypes){
		List<Listener> listenerTypeList = new ArrayList<Listener>();
		String[] listenerTypesString = StringUtils.split(listenerTypes, ",");
		if (listenerTypesString != null){
			for (String lTypeString : listenerTypesString){
				Listener listener = new Listener();
				listener.setType(lTypeString);
				listenerTypeList.add(listener);
			}
		}
		return listenerTypeList;
	}

    /**
     * Adds this listener to the list of listeners. If this list is still null a new list is
     * created.
     * 
     * @param listener
     *                to add
     */
    public void add(final ListenerGroup listenerGroup) {
        if (listeners == null) {
            listeners = new ArrayList<ListenerGroup>();
        }
        listeners.add(listenerGroup);
    }

    /**
     * Adds this eventItem to the list of eventItems. If this list is still null a new list is
     * created.
     * 
     * @param eventItem
     *                to add
     */
    public void add(final EventItem eventItem) {
        if (eventItems == null) {
            eventItems = new ArrayList<EventItem>();
        }
        eventItems.add(eventItem);
    }

    /**
     * Add a list of event items
     * to the event items collection.
     * @param eis
     */
   public void addAll(List<EventItem> eis){
       eventItems.addAll(eis);
   }

    public String getId() {
        return id;
    }

    public List<ListenerGroup> getListeners() {
    	List<ListenerGroup> listenerGroups = new ArrayList<ListenerGroup>();
    	if (StringUtils.isNotBlank(getComponents()) && StringUtils.isNotBlank(getListenerTypes())){
    		if (listenerGroupsDefinedAsEventAttribute.isEmpty()){
    			listenerGroupsDefinedAsEventAttribute = getListenerGroupDefinedAsEventAttribute();	
    		}
    		listenerGroups.addAll(listenerGroupsDefinedAsEventAttribute);
    	}
    	if (listeners != null){
    		listenerGroups.addAll(listeners);
    	}
        return listenerGroups;
    }

    public List<EventItem> getEventItems() {
        return eventItems;
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
    	
    	List<Inputable> inParameters= InterfaceScanner.scan(this, Inputable.class, "com.qualogy");
    	
    	Iterator<Inputable> itr = inParameters.iterator();
    	while (itr.hasNext()){
    		
    		Inputable p = (Inputable)itr.next();
    		if (p.getRef()!=null){
    			if (p.getRef().isComponentReference()){
    				if(input == null){
    					input = new ArrayList<InputVariable>();
    				}
					input.add(new InputVariable(p.getName(), p.getRef().toString(),
    						                    p.getValue()!=null? p.getValue().getStaticValue():null));
    			}
    		}
    	}
    	
    	// Create an implicit event ID if it's not set,
    	// event ID is important to able to handle events correctly
    	if (StringUtils.isEmpty(getId())) {
    		createAndSetId();
    	}
    	if ((StringUtils.isNotBlank(getComponents()) && StringUtils.isBlank(getListenerTypes())) ||
    			(StringUtils.isBlank(getComponents()) && StringUtils.isNotBlank(getListenerTypes()))){
    		throw new RuntimeException("Event with id: " + getId() +". " +
    				"In the event header you should have the combination of components and listener types together. Defining one without the other is not possible.");
    	}
     }

	public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    public void setId(String id) {
        this.id = id;
    }

	public List<InputVariable> getInput() {
		return input;
	}

	public void setInput(List<InputVariable> input) {
		this.input = input;
	}

	public void addAllParameters(List<InputVariable> parameters) {
		if(input==null)
			input = new ArrayList<InputVariable>();
		if(parameters!=null)
			input.addAll(parameters);
	}

	public void createAndSetId() {
		String eventId = createEventId();
		setId(eventId);
	}
	
	public static String createEventId() {
		return createEventId(UniqueIdentifier.nextSeed().toString());
	}
	
	public static String createEventId(String uniqueIdentifier) {
		return PREFIX_EVENT_ID + uniqueIdentifier;
	}
}