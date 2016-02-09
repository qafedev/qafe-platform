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
package com.qualogy.qafe.bind.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.qualogy.qafe.bind.ValidationException;
import com.qualogy.qafe.bind.commons.error.Errors;
import com.qualogy.qafe.bind.commons.error.ServiceError;
import com.qualogy.qafe.bind.commons.type.AdapterMapping;
import com.qualogy.qafe.bind.integration.service.Service;
import com.qualogy.qafe.bind.resource.BindResource;
import com.qualogy.qafe.bind.resource.ResourceRef;
/**
 * Holder for businesstier domain. Holds typedefinitions and businessactions
 * @author 
 */
public class IntegrationTier implements Serializable, Tier {

    private static final long serialVersionUID = -4667961028780500650L;

    protected List<Service> services = new ArrayList<Service>();
    protected List<AdapterMapping> adapters = new ArrayList<AdapterMapping>();
    protected Errors errors = new Errors();
    
    /**
     * @return the adapters
     */
    public List<AdapterMapping> getAdapters() {
        return adapters;
    }

    /**
     * @return the services
     */
    public List<Service> getServices() {
        return services;
    }

    /**
     * @return the errors
     */
	public Errors getErrors() {
		return errors;
	}
	
    /**
     * method to add a AdapterMapping to a AdapterMapping list. The list will be
     * created when null 
     * @param action
     * @throws IllegalArgumentException - when object param passed is null
     */
    public void add(AdapterMapping adapter) {
    	if (adapter == null) {
    		throw new IllegalArgumentException("adapter cannot be null");
    	}	
    	adapters.add(adapter);
    }
    
    /**
     * method to add a Service to a Service list. The list will be
     * created when null 
     * @param action
     * @throws IllegalArgumentException - when object param passed is null
     */
    public void add(Service service) {
    	if (service == null) {
    		throw new IllegalArgumentException("service cannot be null");
    	}	
    	services.add(service);
    }

    /**
     * method to add a Error to a Error list. The list will be
     * created when null 
     * @param action
     * @throws IllegalArgumentException - when object param passed is null
     */
    public void add(ServiceError error) {
    	if (error == null) {
    		throw new IllegalArgumentException("error cannot be null");
    	}	
    	errors.add(error);
    }
    
    /**
     *  Add a list of services.
     * @param services
     */
    public void addAllServices(List<Service> services) {
    	 this.services.addAll(services);
    }

    /**
     *  Add a list of adapters.
     * @param adapters
     */
    public void addAllAdapters(List<AdapterMapping> adapters){
        this.adapters.addAll(adapters);
    }

    /**
     *  Add errors.
     * @param errors
     */
    public void addAllErros(Errors errors) {
        this.errors.addAll(errors);
    }
    
	public void validate() throws ValidationException {
	}
	
	/**
	 * method to remove all services from the integrationtier
	 */
	public void clearServiceList() {
		services.clear();
	}
    
    public void addAll(IntegrationTier otherIntegrationTier){
    	//adapters
        if (adapters == null) {
        	adapters = otherIntegrationTier.getAdapters();
        } else if (otherIntegrationTier.getAdapters() != null) {
        	adapters.addAll(otherIntegrationTier.getAdapters()); 
        }
        
        // services
        if (services == null) {
        	services = otherIntegrationTier.getServices();
        } else if (otherIntegrationTier.getServices() != null) {
        	services.addAll(otherIntegrationTier.getServices());
        }
        
        // errors
        if (errors == null) {
        	errors = otherIntegrationTier.getErrors();
        } else if (otherIntegrationTier.getErrors() != null) {
        	errors.addAll(otherIntegrationTier.getErrors());
        }	
    }

	public void replaceResourceRef(String placeholder, BindResource actualRef) {
		for (Service service : services) {
			if ((service.getResourceRef()!= null) && placeholder.equals(service.getResourceRef().getRef().getId())) {
				service.setResourceRef(new ResourceRef(actualRef));
			}
		}
	}
}