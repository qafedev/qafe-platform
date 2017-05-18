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
package com.qualogy.qafe.core.application;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.qualogy.qafe.bind.core.application.ApplicationContext;
import com.qualogy.qafe.bind.core.application.ApplicationIdentifier;
import com.qualogy.qafe.bind.core.application.ApplicationStack;
import com.qualogy.qafe.bind.core.application.Configuration;
import com.qualogy.qafe.bind.domain.ApplicationMapping;
import com.qualogy.qafe.bind.presentation.component.Window;
import com.qualogy.qafe.bind.presentation.event.Component;
import com.qualogy.qafe.bind.presentation.event.Event;
import com.qualogy.qafe.bind.presentation.event.Listener;
import com.qualogy.qafe.bind.presentation.event.ListenerGroup;
import com.qualogy.qafe.bind.presentation.event.function.LocalDelete;
import com.qualogy.qafe.bind.presentation.event.function.OpenWindow;
import com.qualogy.qafe.util.ManageParameterUtil;

/**
 * A cluster in this context is a collection of applications (ApplicationContexts). 
 * This class holds the applications statically.
 * @author 
 *
 */
public class ApplicationCluster {
	
	
	/**
	 * holder for the genesis applications
	 */
	private Map<ApplicationIdentifier, ApplicationContext> contexts;
	
	/**
	 * This list keeps the order of the list as the Application contexts are being read.
	 * This is needed, since the contexts datamember implements the Map (specifically the TreeMap)
	 * which uses the UniqueIdentifier for for comparing the order (since the ApplicationIdentifier is 
	 * Keyfield for this map). The UniqueIdentifier has a uuid which can be randomly and uniquely defined.
	 * The uuid comparison does not guarantee the order in which you read the the file. So that's
	 * why we have to keep track of the order in a separate list.
	 */
	private List<ApplicationIdentifier> readOrderList;
	
	///////////// configuration ///////////////////
	/**
	 * holder for configuration items
	 */
	protected Configuration globalConfiguration;
		
	public Configuration getGlobalConfiguration() {
		return globalConfiguration;
	}
	
	private static String appClusterUID = null;
	
	/**
	 * single instance
	 */
	private static ApplicationCluster instance = null;	
	
	private ApplicationCluster() {
		super();
		contexts = new TreeMap<ApplicationIdentifier, ApplicationContext>();
		readOrderList = new ArrayList<ApplicationIdentifier>();
	}
	/**
	 * method to get an applicationcontext for id. method throws exception
	 * when not found.
	 * @param applicationId
	 * @throws IllegalArgumentException - when applicationcontext does not exist for id
	 * @return context
	 */
	public ApplicationContext get(ApplicationIdentifier applicationId){
		if(applicationId==null)
			throw new IllegalArgumentException("applicationId cannot be null");
		
		if(!contexts.containsKey(applicationId))
			throw new NotLoadedException("application with id["+applicationId+"] not in cluster");
		
		return (ApplicationContext)contexts.get(applicationId);
	}
	
	private void put(ApplicationContext context){
		if(context == null)
			throw new IllegalArgumentException("cannot put with null object");
		
		contexts.put(context.getId(), context);
		
		if(!readOrderList.contains(context.getId())){
			readOrderList.add(context.getId());
		}
		
		if(readOrderList.size()!=contexts.size())
			throw new IndexOutOfBoundsException("readorderlist size is not equal to contexts size");
	}
	
	public ApplicationContext remove(ApplicationIdentifier applicationId){
		ApplicationContext context =  (ApplicationContext)contexts.remove(applicationId);
		readOrderList.remove(applicationId);
		return context;
	}
	
	public ApplicationContext[] clear(){
		Collection<ApplicationContext> values = contexts.values();
		ApplicationContext[] retValue = (ApplicationContext[])values.toArray(new ApplicationContext[values.size()]);
		contexts.clear();
		readOrderList.clear();
		return retValue;
	}
	
	public static ApplicationCluster getInstance(){
		if(instance == null)
			instance = new ApplicationCluster();
		return instance;
	}
	
	public Iterator<ApplicationIdentifier> keysIterator(){
		return readOrderList.iterator();
	}
	
	/**
	 * method returns an iterator of ApplicationContexts, only when
	 * cluster contains contexts, otherwise returns null
	 * @return
	 * @throws NotLoadedException when not loaded
	 */
	public Iterator<ApplicationContext> iterator(){
		return contexts.values().iterator();
	}
	
	public String toString(){
		return ToStringBuilder.reflectionToString(this);
	}
	
	public int size(){
		return contexts.size();
	}
	
	public void replace(ApplicationContext oldContext, ApplicationContext newContext) {
		if(oldContext == null)
			throw new IllegalArgumentException("cannot replace, old object does not exist");
		if(!contexts.containsKey(oldContext.getId()))
			throw new NotLoadedException("application with id["+oldContext.getId()+"] not in cluster, while replacing");
		
		put(newContext);
	}
	
	public ApplicationContext get(ApplicationMapping mapping){
		boolean found = false;
		ApplicationContext context = null;
		
		for (Iterator<ApplicationContext> iter = iterator(); iter.hasNext() && !found;) {
			ApplicationContext tmpContext = (ApplicationContext)iter.next();
			if(tmpContext.getApplicationMapping()!=null && tmpContext.getApplicationMapping().equals(mapping)){
				found = true;
				context = tmpContext;
			}
		}
		return context;
	}
	/**
	 * Method uses public method put to put all contexts onto cluster, beacuse of logic
	 * in put method.
	 * @param contexts
	 */
	public void putAll(ApplicationStack stack) {
		if (stack == null)
			throw new IllegalArgumentException("stacks cannot be null");
		
		for (Iterator<ApplicationContext> iter = stack.getApplicationsIterator(); iter.hasNext();) {
			ApplicationContext context = (ApplicationContext) iter.next();
			put(context);
		}
		
		globalConfiguration = stack.getGlobalConfiguration();
		
		createWindowEvents();
		createLoadOnStartupEvents();
	}
	
	private void createWindowEvents() {
		Iterator<ApplicationContext> itr = contexts.values().iterator();
		while (itr.hasNext()) {
			ApplicationContext appContext = itr.next();
			if (appContext.getApplicationMapping() != null) {
				ApplicationMapping appMapping = appContext.getApplicationMapping();
				if ((appMapping.getPresentationTier() != null) && (appMapping.getPresentationTier().getView() != null) && (appMapping.getPresentationTier().getView().getWindows() != null)) {
					List<Window> windowList = appMapping.getPresentationTier().getView().getWindows();
					for (int i=0; i<windowList.size(); i++) {
						Window window = windowList.get(i);
						//String appWindowId = appContext.getId().stringValueOf() + "." + window.getId();
						String appWindowId =  window.getId();
						Event event = createOpenWindowEvent(Listener.EVENT_ONCLICK, appWindowId, appWindowId);
						appMapping.getPresentationTier().add(event);
						event = createLocalDeleteEvent(Listener.EVENT_ONUNLOAD, window.getId());
						window.add(event);
					}
				}
				appMapping.loadLookupEvents();
			}
		}	
	}
	
	private void createLoadOnStartupEvents() {
		if (getConfigurationItem(Configuration.LOAD_ON_STARTUP) != null) {
			String[] loadOnStartupWindows = StringUtils.split(getConfigurationItem(Configuration.LOAD_ON_STARTUP), ",");
			if (loadOnStartupWindows != null){
				for (int i=0; i<loadOnStartupWindows.length; i++) {
					String loadOnStartupWindow = loadOnStartupWindows[i].trim();
					Event event = createOpenWindowEvent(Listener.EVENT_ONLOAD, loadOnStartupWindow, loadOnStartupWindows[i].trim());
//					if (getSystemApplicationContext() != null) {
//						getSystemApplicationContext().getApplicationMapping().getPresentationTier().add(event);	
//					} else {
						String appId = loadOnStartupWindow;
						if (appId.indexOf(".") > -1) {
							appId = appId.substring(0, appId.indexOf("."));
						}	
						ApplicationContext appContext = getApplicationContext(appId);
						if (appContext != null) {
							ApplicationMapping appMapping = appContext.getApplicationMapping();
							if (appMapping != null) {
								appMapping.getPresentationTier().add(event);
							}
						}
//					}
				}
			}
		}
	}
	
	private Event createOpenWindowEvent(String listenerType, String componentId, String openWindow) {
		Listener listener = new Listener();
		listener.setType(listenerType);
		
		Component component = new Component();
		component.setComponentId(componentId);
		
		ListenerGroup listenerGroup = new ListenerGroup();
		listenerGroup.add(listener);
		listenerGroup.add(component);
		
		OpenWindow builtin = new OpenWindow();
		builtin.setWindow(ManageParameterUtil.getParameterObjWithValue(openWindow));

		Event event = new Event();
		String eventId = Event.createEventId(componentId + listenerType);
		event.setId(eventId);
		event.add(listenerGroup);
		event.add(builtin);
		return event;
	}
	
	private Event createLocalDeleteEvent(String listenerType, String componentId) {
		Listener listener = new Listener();
		listener.setType(listenerType);
		
		Component component = new Component();
		component.setComponentId(componentId);
		
		ListenerGroup listenerGroup = new ListenerGroup();
		listenerGroup.add(listener);
		listenerGroup.add(component);
		
		LocalDelete builtin = new LocalDelete();
		//builtin.setScope(LocalStore.SCOPE_LOCAL);

		Event event = new Event();
		event.createAndSetId();
		event.add(listenerGroup);
		event.add(builtin);
		return event;
	}
	
	/**
	 * convinience method to get a configuration item from the configuration
	 * returns null if item not found for key or config is null
	 * @return
	 */
	public String getConfigurationItem(String key) {
		return globalConfiguration!=null?globalConfiguration.get(key):null;
	}
	
	public ApplicationContext getSystemApplicationContext(){
//		ApplicationContext systemContext = null;
//		ApplicationIdentifier systemAppId = new ApplicationIdentifier(QAFEKeywords.SYSTEM_APP);
//		Iterator<ApplicationIdentifier> itr = contexts.keySet().iterator();
//		while (itr.hasNext()){
//			ApplicationIdentifier appId = itr.next();
//			if (appId.equals(systemAppId)){
//				systemContext = contexts.get(appId);
//				break;
//			}
//		}
//		return systemContext;
		//ApplicationIdentifier systemAppId = new ApplicationIdentifier(QAFEKeywords.SYSTEM_APP);
		//return getApplicationContext(systemAppId);
		return getApplicationContext(QAFEKeywords.SYSTEM_APP);
	}
	
	public ApplicationContext getApplicationContext(String appId){
		if (appId != null) {
			ApplicationIdentifier applicationId = new ApplicationIdentifier(appId);
			Iterator<ApplicationIdentifier> itr = contexts.keySet().iterator();
			while (itr.hasNext()){
				ApplicationIdentifier id = itr.next();
				if (id.equals(applicationId)){
					return contexts.get(id);
				}
			}
		}
		return null;
	}

	public Boolean isConcurrentModificationEnabled() {
		String concurrentModificationEnabled = getConfigurationItem(Configuration.CONCURRENT_MODIFICATION_ENABLED);
		if ((concurrentModificationEnabled == null) || Boolean.parseBoolean(concurrentModificationEnabled)) {
			return true;
		}
		return false;
	}
	
	public Boolean isClientSideEventEnabled() {
	    String clientSideEventEnabled = getConfigurationItem(Configuration.CLIENTSIDE_EVENT_ENABLED);
        if (Boolean.parseBoolean(clientSideEventEnabled)) {
            return true;
        }
        return false;
    }
}