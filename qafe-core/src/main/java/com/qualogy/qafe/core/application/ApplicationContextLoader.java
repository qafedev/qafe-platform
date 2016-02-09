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
package com.qualogy.qafe.core.application;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang.time.StopWatch;

import com.qualogy.qafe.bind.core.application.ApplicationContext;
import com.qualogy.qafe.bind.core.application.ApplicationIdentifier;
import com.qualogy.qafe.bind.core.application.ApplicationStack;
import com.qualogy.qafe.bind.core.application.Configuration;
import com.qualogy.qafe.bind.io.Reader;
/**
 * Class with functionality to start up and load resources.
 * @author 
 *
 */
public class ApplicationContextLoader {

	public final static Logger logger = Logger.getLogger(ApplicationContextLoader.class.getName());
	
	private static Reader reader = null;
	
	public synchronized static void load(URI applicationFilePath, boolean validating){
		logger.info("start application loading according to config file: ["+applicationFilePath+"]");
		StopWatch watch = new StopWatch();
		
		watch.start();
		ApplicationStack contexts = read(applicationFilePath,validating);
		load(contexts, applicationFilePath);
		watch.stop();
		
		logger.info("done loading from ["+applicationFilePath+"] in " + watch.getTime() + "ms");
		
		ApplicationCluster.getInstance().putAll(contexts);
	}
	
	public synchronized static void load(URI applicationFilePath){
		load(applicationFilePath,false);
	}
	
	public static void load(String applicationFilePath, boolean validating) {
		File file = new File(applicationFilePath);
		if(!file.exists()){
			throw new LoadFailedException("Cannot find applicationconfig at ["+applicationFilePath+"]");
		}
		
		load(file.toURI(),validating);
	}
	
	
	public static void load(String applicationFilePath) {
		load(applicationFilePath,false);
	}
	
	/**
	 * reload function per application
	 * @param applicationId
	 * @return
	 */
	public synchronized static ApplicationContext reload(ApplicationIdentifier applicationId){
		ApplicationContext oldContext = ApplicationCluster.getInstance().get(applicationId);
		
		ApplicationStack contexts = read(oldContext.getOriginAppConfigFileLocation());
		if(contexts.size()==0)
			throw new IllegalArgumentException("No contexts have been re-read");
		
		ApplicationContext newContext = contexts.get(applicationId);
		
		load(newContext, oldContext.getOriginAppConfigFileLocation());
		
		ApplicationCluster.getInstance().replace(oldContext, newContext);
		
		return newContext;
	}
	
	/**
	 * 1) collect all the original file locations
	 * 2) clear the cluster
	 * 3)a. read the original file locations
	 * 3)b. reload the contexts
	 * 4) add all re-read/loaded contexts 
	 */
	public synchronized static void reload(){
		
		Set<URI> fileLocations = new HashSet<URI>();
		for (Iterator<ApplicationContext> iter = ApplicationCluster.getInstance().iterator(); iter.hasNext();) {
			ApplicationContext context = (ApplicationContext) iter.next();
			fileLocations.add(context.getOriginAppConfigFileLocation());
		}
		
		ApplicationCluster.getInstance().clear();
		
		for (Iterator<URI> iter = fileLocations.iterator(); iter.hasNext();) {
			URI fileLocation = (URI) iter.next();
			ApplicationStack contexts = read(fileLocation);
			
			load(contexts, fileLocation);
			
			ApplicationCluster.getInstance().putAll(contexts);
		}
	}
	
	/**
	 * 
	 *
	 */
	public synchronized static void unload(ApplicationIdentifier applicationId){
		ApplicationContext context = ApplicationCluster.getInstance().remove(applicationId);
		if(context!=null)
			context.destroy();
	}
	
	/**
	 * unload all application contexts loaded in the cluster
	 *
	 */
	public synchronized static void unload(){
		ApplicationContext[] contexts = ApplicationCluster.getInstance().clear();
		for (int i = 0; i < contexts.length; i++) {
			contexts[i].destroy();
		}
	}
	
	private static void load(ApplicationStack contexts, URI applicationFilePath){
		if(contexts==null)
			throw new LoadFailedException("", "no context loaded");
		
	//	contexts.loadSystemMenu(applicationFilePath);
		for (Iterator<ApplicationContext> iter = contexts.getApplicationsIterator(); iter.hasNext();) {
			ApplicationContext context = (ApplicationContext) iter.next();
			if (context.getApplicationMapping()!=null){
				load(context, applicationFilePath);
			}
		}
	}

	
	private static ApplicationIdentifier load(ApplicationContext context, URI applicationFilePath){
		
		if(context == null)
			throw new IllegalArgumentException("context cannot be null");
		
		if(logger.isLoggable(Level.FINE))
			logger.info("start loading ["+context.getName()+"]\ncontext ["+context+"]\ncluster ["+ApplicationCluster.getInstance().toString()+"]");
		else if(logger.isLoggable(Level.INFO))
			logger.info("start loading ["+context.getName()+"]");
		
		try {
			context = createManagers(context);
			context.setOriginAppConfigFileLocation(applicationFilePath);
			context.init();	
		} catch (Exception e) {
			e.printStackTrace();
			context.handleLoadFailure(e);
		}
		
		if(logger.isLoggable(Level.FINE))
			logger.info("done loading ["+context.getName()+"]\nstored in cluster with id ["+context.getId()+"]\ncontext ["+context+"]\ncluster ["+ApplicationCluster.getInstance().toString()+"]");
		else if(logger.isLoggable(Level.INFO))
			logger.info("done loading ["+context.getName()+"], stored in cluster with id ["+context.getId()+"]");
		
		return context.getId();
	}
	
	private static ApplicationContext createManagers(ApplicationContext context) {
	
		List<String> managerClassNames = new ArrayList<String>();
		
		if(context.getConfigurationItem(Configuration.EVENTHANDLER_IMPL_CLASSNAME)!=null)
			managerClassNames.add(context.getConfigurationItem(Configuration.EVENTHANDLER_IMPL_CLASSNAME));
		
		if(context.getConfigurationItem(Configuration.BUSINESSMANAGER_IMPL_CLASSNAME)!=null)
			managerClassNames.add(context.getConfigurationItem(Configuration.BUSINESSMANAGER_IMPL_CLASSNAME));
		
		for (Iterator<String> iter = managerClassNames.iterator(); iter.hasNext();) {
			context.add(ContainerManagerFactory.create((String) iter.next()));
		}

		return context;
	}
	
	private synchronized static ApplicationStack read(URI applicationFilePath, boolean validating){
		if(reader==null){ 
			reader = new Reader(ApplicationStack.class,validating);
		}
		
		ApplicationStack contexts = (ApplicationStack)reader.read(applicationFilePath);
		if (contexts!=null){
			contexts.setOriginalFilePath(applicationFilePath);
			contexts.init();
	
		}
		return contexts;
	}
	
	private synchronized static ApplicationStack read(URI applicationFilePath){
		return read(applicationFilePath,false);
	}
}
