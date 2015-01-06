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
package com.qualogy.qafe.bind.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import com.qualogy.qafe.bind.ValidationException;
import com.qualogy.qafe.bind.integration.service.Service;
import com.qualogy.qafe.bind.resource.BindResource;
import com.qualogy.qafe.bind.resource.ResourceRef;

public class ResourceTier implements Serializable, Tier{

    private static final long serialVersionUID = -2220723939145122100L;

    protected List<BindResource> resources;

    /**
     * @return the resources
     */
    public List<BindResource> getResources() {
        return resources;
    }

    public BindResource getResource(String id){
    	BindResource resource = null;
    	if(resources!=null)
    		for (Iterator<BindResource> iter = resources.iterator(); iter.hasNext();) {
    			BindResource properties = (BindResource) iter.next();
    			if(properties.getId().equals(id)){
    				resource = properties;
    				break;
    			}
    		}
    	return resource;
    }
    
    /**
     * method to add a ResourceProperties to a ResourceProperties list. The list will be
     * created when null 
     * @param action
     * @throws IllegalArgumentException - when object param passed is null
     */
    public void add(BindResource resource) {
    	if(resource==null)
    		throw new IllegalArgumentException("resourceProperties cannot be null");
    	if(resources==null)
    		resources = new ArrayList<BindResource>();
    	
    	resources.add(resource);
    }
   	public void validate() throws ValidationException {
	}
   	
   	public void addAll(ResourceTier otherResourceTier){
   		// resources
   		if(resources==null)
   			resources = new ArrayList<BindResource>();
   		
   		resources.addAll(otherResourceTier.getResources());
   		
   	}

	public void removeResource(String resourceId) {
		if(resources!=null){
    		for (ListIterator<BindResource> iter = resources.listIterator(); iter.hasNext();) {
    			if(((BindResource) iter.next()).getId().equals(resourceId)){
    				iter.remove();
    				break;
    			}
    		}
		}
	}
	
	public void replaceResource(String placeholder, BindResource actualResource) {
		if (resources != null) {
			for (int i=0; i<resources.size(); i++) {
				BindResource resource = resources.get(i);
				if (resource.getId().equals(placeholder)) {
					resources.remove(i);
					resources.add(i, actualResource);
					break;
    			}
			}
		}
	}
}
