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
package com.qualogy.qafe.bind.core.application;

import java.io.IOException;
import java.util.Iterator;
import java.util.Properties;
import java.util.logging.Logger;

import com.qualogy.qafe.bind.orm.jibx.BindException;
import com.qualogy.qafe.util.io.PropertiesUtils;

/**
 * This class holds default framework configuration. First this class on init will
 * reader a property file to load application context defaults. These properties are Framework defined. 
 * Users can override these properties on global level and application level through application-context file. 
 * This class takes care if the global settings through classpath and overrides the 
 * defaults read from the property file
 * 
 * NOTE: this class loads default configuration items so that they can be merged when
 * an application loads to one the application has not altered. For this to work properly
 * make sure all the predefined configuration item keys are set in the Configuration.CONFIGURATION_KEYS array.
 * 
 * @author 
 */
public class ConfigurationDefaults {
	
	public final static Logger logger = Logger.getLogger(ConfigurationDefaults.class.getName());
	
	private final static String APP_CONFIG_DEFAULTS_FILE_PATH = "application-configuration-defaults.properties";
	private final static String GLOBAL_CONFIG_DEFAULTS_FILE_PATH = "application-cluster-configuration-defaults.properties";
	
	private static ConfigurationDefaults instance = null;
	
	private Configuration globalConfiguration = null;
	private Configuration applicationConfiguration = null;
	
	private ConfigurationDefaults(){
		init();
	}
	
	private void init() {
		globalConfiguration = load(true);
		applicationConfiguration = load(false);
		if(applicationConfiguration == null){
			applicationConfiguration = globalConfiguration;
		}else{
			applicationConfiguration.fillInTheBlanks(globalConfiguration);
		}
	}

	private Configuration load(boolean global){
		
		String path = global ? APP_CONFIG_DEFAULTS_FILE_PATH : GLOBAL_CONFIG_DEFAULTS_FILE_PATH;
		
		Properties properties = null;
		try {
			properties = PropertiesUtils.loadAllProperties(path);
		} catch (IOException e) {
			throw new BindException("Cannot load Qafe configuration defaults from ["+path+"] and therefore application will not be loaded");
		}	
		Configuration configuration = Configuration.create(properties, global);
		
		for (Iterator iter = configuration.keySetIterator(); iter.hasNext();) {
			String key = (String) iter.next();
			String systemValue = System.getProperty(key);
			if(systemValue!=null)
				configuration.put(key, systemValue);			
		}
		return configuration;
	}
	

	
	public static synchronized ConfigurationDefaults getInstance(){
		if(instance == null){
			instance = new ConfigurationDefaults();
		}
		return instance;
	}
	/**
	 * method returns a cloned instance of the configuration
	 * @return
	 */
	public Configuration getGlobalConfiguration(){
		Configuration cloned = null;
		try {
			cloned = (Configuration)globalConfiguration.clone();
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException(e);
		}
		return cloned;
	}
	
	/**
	 * method returns a cloned instance of the configuration
	 * @return
	 */
	public Configuration getAppConfiguration(){
		Configuration cloned = null;
		try {
			cloned = (Configuration)applicationConfiguration.clone();
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException(e);
		}
		return cloned;
	}
}
