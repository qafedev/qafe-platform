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
package com.qualogy.qafe.bind.domain;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jibx.runtime.IUnmarshallingContext;

import com.qualogy.qafe.bind.PostProcessing;
import com.qualogy.qafe.bind.ValidationException;
import com.qualogy.qafe.bind.orm.jibx.BindException;
import com.qualogy.qafe.bind.presentation.component.Window;
import com.qualogy.qafe.bind.presentation.event.Event;
import com.qualogy.qafe.bind.util.Validator;

/**
 * Resource holder for applicationmappings, this class can hold a mapping file path
 * or actual mapping (NOTE: so only one is allowed in which filepath is leading) 
 * @author 
 */
public class ApplicationMapping implements Serializable, PostProcessing{

	private static final long serialVersionUID = -3104524285974142531L;

    protected PresentationTier presentationTier;
    protected BusinessTier businessTier;
    protected IntegrationTier integrationTier;
    protected ResourceTier resourceTier;
    
    private HashMap<String, Event> events = new HashMap<String, Event>();
    
    public ApplicationMapping(){
    	super();
    }
    
    private ApplicationMapping(PresentationTier presentationTier, BusinessTier businessTier, IntegrationTier integrationTier, ResourceTier resourceTier) {
		this();
		this.presentationTier = presentationTier;
		this.businessTier = businessTier;
		this.integrationTier = integrationTier;
		this.resourceTier = resourceTier;
	}

	public static ApplicationMapping create(BusinessTier businessTier,IntegrationTier integrationTier,ResourceTier resourceTier){
    	return create(null, businessTier, integrationTier, resourceTier);
    }

	public static ApplicationMapping create(PresentationTier presentationTier,BusinessTier businessTier,IntegrationTier integrationTier,ResourceTier resourceTier){
		return new ApplicationMapping(presentationTier, businessTier, integrationTier, resourceTier);
    }

	/**
     * @return the businessTier
     */
    public BusinessTier getBusinessTier() {
        return businessTier;
    }

    /**
     * @param businessTier
     *            the businessTier to set
     */
    public void setBusinessTier(BusinessTier businessTier) {
        this.businessTier = businessTier;
    }

    /**
     * @return the integrationTier
     */
    public IntegrationTier getIntegrationTier() {
        return integrationTier;
    }

    /**
     * @param integrationTier
     *            the integrationTier to set
     */
    public void setIntegrationTier(IntegrationTier integrationTier) {
        this.integrationTier = integrationTier;
    }

    /**
     * @return the presentationTier
     */
    public PresentationTier getPresentationTier() {
        return presentationTier;
    }

    /**
     * @param presentationTier
     *            the presentationTier to set
     */
    public void setPresentationTier(PresentationTier presentationTier) {
        this.presentationTier = presentationTier;
    }

    /**
     * @return the resourceTier
     */
    public ResourceTier getResourceTier() {
        return resourceTier;
    }

    /**
     * @param resourceTier
     *            the resourceTier to set
     */
    public void setResourceTier(ResourceTier resourceTier) {
        this.resourceTier = resourceTier;
    }

    public HashMap<String, Event> getEvents() {
		return events;
	}

	/**
     * method to conveniently put a tier on the genesisframework.
     * this method will override the existing value with the given
     * one.
     * @param tier
     * @throws IllegalArgumentException when tier is null or not applicable
     */
    public void put(Tier tier){
    	if(tier instanceof BusinessTier){
    		this.businessTier = (BusinessTier)tier;
    	}else if(tier instanceof PresentationTier){
    		this.presentationTier = (PresentationTier)tier;
    	}else if(tier instanceof IntegrationTier){
    		this.integrationTier = (IntegrationTier)tier;
    	}else if(tier instanceof ResourceTier){
    		this.resourceTier = (ResourceTier)tier;
    	}
    }
    
    public void addAll(ApplicationMapping otherMapping){
	    
        // presentationTier
        if(presentationTier == null) {
        	presentationTier = otherMapping.presentationTier;
        }else{
            presentationTier.addAll(otherMapping.presentationTier);
        }
        
        // businessTier
        if (businessTier == null) {
            businessTier = otherMapping.getBusinessTier();
        }else {
        	businessTier.addAll(otherMapping.getBusinessTier());
        }
        
        // integrationTier
        if (integrationTier == null) {
            integrationTier = otherMapping.getIntegrationTier();
        }else {
            integrationTier.addAll(otherMapping.getIntegrationTier());
        }
        
        // resourceTier
        if (resourceTier == null) {
            resourceTier = otherMapping.getResourceTier();
        }else {
        	resourceTier.addAll(otherMapping.getResourceTier());
        }
    }
    
    /**
     * method called to do processing after variables have been set
     * the method validates
     */
    public void performPostProcessing() {
		try {
			Validator.validate(this);
		} catch (ValidationException e) {
			throw new BindException(e);
		}
	}

	public void postset(IUnmarshallingContext context) {
		performPostProcessing();
	}

	public Boolean resolveDefaultLoginWindow(String defaultLoginWindowRef, ApplicationMapping resourceAppMapping, String customLoginWindowRef) {
		if ((getPresentationTier() != null) && (customLoginWindowRef != null)) {
			Window defaultLoginWindow = null;	
			Window customLoginWindow = null;
			
			Map<String, Window> windowNamesMap = getPresentationTier().view.getWindowNamesMap();
			if ((windowNamesMap != null) && windowNamesMap.containsKey(defaultLoginWindowRef)) {
				defaultLoginWindow = windowNamesMap.get(defaultLoginWindowRef);
			}	
								
			if (resourceAppMapping == null) {
				if ((windowNamesMap != null) && windowNamesMap.containsKey(customLoginWindowRef)) {
					customLoginWindow = windowNamesMap.get(customLoginWindowRef);
				}	
			} else {
				Map<String, Window> resourceWindowNamesMap = resourceAppMapping.getPresentationTier().view.getWindowNamesMap();
				if ((resourceWindowNamesMap != null) && (resourceWindowNamesMap.containsKey(customLoginWindowRef))) {
					customLoginWindow = resourceWindowNamesMap.get(customLoginWindowRef);
				}
			}
				
			if ((defaultLoginWindow != null) && (customLoginWindow != null)) {
				defaultLoginWindow.setDisplayname(customLoginWindow.getDisplayname());
				defaultLoginWindow.setWidth(customLoginWindow.getWidth());
				defaultLoginWindow.setHeight(customLoginWindow.getHeight());
				defaultLoginWindow.setRootPanel(customLoginWindow.getRootPanel());
				
				List<Event> customEventList = customLoginWindow.getEvents();
				if (customEventList != null) {
					for (int i=0; i<customEventList.size(); i++) {
						Event customEvent = customEventList.get(i);
						if (!defaultLoginWindow.getEventsMap().containsKey(customEvent.getId())) {
							defaultLoginWindow.add(customEvent);
						}
					}
				}
				return true;
			}
		}
		return false;
	}

	// Events are populated to have the look up when they are triggered in EventHandler
	public void loadLookupEvents() {
		this.getEvents().clear();
		// Extract local events.
		if (this.getPresentationTier() != null && this.getPresentationTier().getView() != null) {
			List<Window> windowList = this.getPresentationTier().getView().getWindows();
			if (windowList != null) {
				for(Window window : windowList) {
					if (window.getEvents() != null) {
						String windowId = window.getId();
						for (Event event : window.getEvents()) {
							event.setWindowId(windowId);
							this.getEvents().put(windowId+event.getId(), event);
						}
					}
				}
			}
			
		}
		// Extract global events
		if (this.getPresentationTier() != null && this.getPresentationTier().getEvents() != null) {
			List<Event> eventList = this.getPresentationTier().getEvents();
			if(eventList != null) {
				for (Event event : eventList) {
					this.getEvents().put(event.getId(), event);
				}
			}
		}	
	}
}
