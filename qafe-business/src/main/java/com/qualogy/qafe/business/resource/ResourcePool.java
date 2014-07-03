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
package com.qualogy.qafe.business.resource;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.qualogy.qafe.bind.core.application.ApplicationContext;
import com.qualogy.qafe.bind.core.application.ApplicationIdentifier;
import com.qualogy.qafe.bind.domain.ApplicationMapping;
import com.qualogy.qafe.bind.resource.BindResource;
import com.qualogy.qafe.bind.resource.ResourceRef;
import com.qualogy.qafe.bind.resource.SpringContextResource;
/**
 * 
 * @author mvanderwurff
 *
 */
public class ResourcePool{
	
	private Map<ApplicationIdentifier, Pool> poolOfPools;
	private static final ResourcePool instance = new ResourcePool();
	
	private class Pool{
		
		private Map<String, Resource> resources;
		
		protected Pool() {
			super();
			resources = new HashMap<String, Resource>();
		}
		
		public Resource get(String resourceId) {
			return (Resource)resources.get(resourceId);
		}
		public void set(String resourceId, Resource resource) {
			this.resources.put(resourceId, resource);
		}
		public void destroy(ApplicationContext context){
			for (Iterator<Resource> iter = resources.values().iterator(); iter.hasNext();) {
				Resource resource = (Resource) iter.next();
				resource.destroy(context);
				iter.remove();
			}
		}
		public void init(ApplicationContext context){
			ApplicationMapping mapping = context.getApplicationMapping();
			if (mapping.getResourceTier()!= null) {
				List<BindResource> bindResourceList = mapping.getResourceTier().getResources();
				if (bindResourceList != null) {
					for (BindResource bindResource : bindResourceList) {
						Resource resource = ResourceFactory.create(bindResource);
						resource.validate();
						resource.init(context);
						if (bindResource instanceof SpringContextResource) {
							for (BindResource beanResource : ((SpringContextResource)bindResource).getBeans()) {
								set(beanResource.getId(), resource);		
							}
						} else {
							set(bindResource.getId(), resource);	
						}
					}
				}
			}
		}
		
		public String toString(){
			String s = "";
			boolean first = true;
			for (Iterator<String> iter = resources.keySet().iterator(); iter.hasNext();) {
				String resourceId = (String) iter.next();
				
				if(!first)s += "\n";
				else first = false;
				
				s += " " + resourceId;
				s += "=>";
				s += resources.get(resourceId);
			}
			return s;
		}
		//TODO: reinitialize??
	}
	
	private ResourcePool() {
		if (instance != null) {
			throw new IllegalStateException("Only one instance of this class is allowed");
		}
	}
	
	public static ResourcePool getInstance(){
		return instance;
	}
	
	private Pool find(ApplicationIdentifier applicationId){
		return (Pool)poolOfPools.get(applicationId);
	}
	
	private Pool get(ApplicationIdentifier applicationId){
		if(!poolOfPools.containsKey(applicationId))
			throw new IllegalArgumentException("application with id ["+applicationId+"] not correctly initialzed, resourcepool does not exist");
		return (Pool)instance.poolOfPools.get(applicationId);
	}
	
	/**
	 * get resource based upon resourceId
	 * @param resourceId
	 * @return
	 */
	public Resource get(ApplicationIdentifier applicationId, String resourceId){
		if(applicationId == null || resourceId == null)
			throw new IllegalArgumentException("applicationId or resourceId cannot be null");
		
		if(poolOfPools==null)
			throw new NullPointerException("pool not initialized yet");
		
		return get(applicationId).get(resourceId);
	}
	
	public Resource get(ApplicationIdentifier applicationId, ResourceRef resourceRef){
		if(resourceRef==null)
			throw new IllegalArgumentException("resourceId cannot be null");
		if(resourceRef.getRef()==null)
			throw new IllegalArgumentException("resource ref cannot be null");
		
		return get(applicationId, resourceRef.getRef().getId());
	}
	
	public String toLogString(){
		
		if(poolOfPools==null)
			throw new NullPointerException("pool not initialized yet");
		
		String s = "";
		boolean first = true;
		for (Iterator<ApplicationIdentifier> iter = poolOfPools.keySet().iterator(); iter.hasNext();) {
			ApplicationIdentifier appId = (ApplicationIdentifier) iter.next();
			if(!first)
				s+="\n";
			else
				first = false;
			s += "Resources for application " + appId;
			s += "\n";
			s += get(appId);
		}
		return s;
	}
	
	public void destroy(ApplicationContext context){
		if(context==null || context.getApplicationMapping()==null)
			throw new IllegalArgumentException("cannot load with null context or mapping not set");
		
		get(context.getId()).destroy(context);
		
		instance.poolOfPools.remove(context.getId());
	}
	
	//TODO: gain performance ;)
	public void refresh(ApplicationContext context){
		destroy(context);
		init(context);
	}
	
	public void init(ApplicationContext context){
		if(context==null || context.getApplicationMapping()==null)
			throw new IllegalArgumentException("cannot load with null context or mapping not set");
		
		if(instance.poolOfPools==null)
			instance.poolOfPools = new HashMap<ApplicationIdentifier, Pool>();
		
		Pool pool = instance.find(context.getId());
		if(pool==null)
			pool = instance.new Pool();
		
		pool.init(context);
		
		instance.poolOfPools.put(context.getId(), pool);
	}
}
