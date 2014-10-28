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
package com.qualogy.qafe.bind.core.application;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.jibx.runtime.IUnmarshallingContext;

import com.qualogy.qafe.bind.PostProcessing;
/**
 * Class which holds the configuration properties for a Genesis system.
 * 
 * TODO: make JMX enabled
 * @author 
 *
 */
public class Configuration implements Serializable, PostProcessing, Cloneable{
	
	private static final long serialVersionUID = 2649329894105431590L;

	/**
	 * key for businessactionamanger classname to be used
	 */
	public final static String BUSINESSMANAGER_IMPL_CLASSNAME = "businessmanager.implementation.class";
	
	/**
	 * key for eventhandler classname to be used
	 */
	public final static String EVENTHANDLER_IMPL_CLASSNAME = "eventhandler.implementation.class";
	
	/**
	 * key for settings for default global transactionmanagement
	 */
	public final static String DEFAULT_GLOBAL_TRANSACTION_MANAGEMENT = "global.transaction.behaviour";
	
	/**
	 * key for setting the script engine that should be used by the application
	 */
	public final static String SCRIPT_EGINE_NAME = "script.engine.name";
	
	/**
	 * key for setting the development mode to true or false
	 */
	public final static String DEVELOPMENT_MODE = "mode.development";
	
	/**
	 * key for setting the mdi mode to true or false
	 */
	public final static String MDI_MODE = "mode.mdi";
	
	/**
	 * key for setting the show the log (true/false)
	 */
	public final static String SHOW_LOG = "showlog";
	
	
	public final static String DOCKING_PANEL_ENABLED="mode.usedock";
	
	public final static String EXTERNAL_PROPERTIES_FILE="external.properties";
	/**
	 * key for setting the max upload filesize in bytes
	 */
	public final static String MAX_UPLOAD_FILESIZE = "max.upload.filesize";
	
	public final static String DATE_FORMAT ="date.format";
	
	/**
	 * key for setting the name of the AIR desktop application,
	 * do not use "main", reserverd word
	 */
	public final static String AIR_APPLICATION_NAME ="air.application.name";
	
	/**
	 * key for setting the version of the AIR desktop application,
	 * this will be used for updating the installed AIR desktop application
	 */
	public final static String AIR_APPLICATION_VERSION ="air.application.version";
	
	/**
	 * key for setting the release notes of the AIR desktop application,
	 * this will be shown when updating the installed AIR desktop application
	 */
	public final static String AIR_APPLICATION_NOTES ="air.application.notes";
	
//	/**
//	 * key for setting the icon (16x16) of the AIR desktop application
//	 */
//	public final static String AIR_APPLICATION_ICON16 ="air.application.icon16";
//	
//	/**
//	 * key for setting the icon (32x32) of the AIR desktop application
//	 */
//	public final static String AIR_APPLICATION_ICON32 ="air.application.icon32";
	
	/**
	 * key for setting the icon (48x48) of the AIR desktop application
	 */
	public final static String AIR_APPLICATION_ICON48 ="air.application.icon48";
	
//	/**
//	 * key for setting the icon (128x128) of the AIR desktop application
//	 */
//	public final static String AIR_APPLICATION_ICON128 ="air.application.icon128";
	
	/**
	 * key for setting the server URL for the AIR desktop application,
	 * this will be used for communication with the backend
	 * URL:	http://hostname[:port-number]/context-root
	 */
	public final static String AIR_SERVER_URL = "air.server.url";
	
	public final static String WINDOW_DEFAULT_HEIGHT = "window.default.height";
	
	public final static String WINDOW_DEFAULT_WIDTH = "window.default.width";
	
	public final static String WEBSERVICE_URL = "webservice.url";
	
	public final static String WEBSERVICE_MAIL_URL = "webservice.mail.url";
	
	public final static String WEBSERVICE_FMB_URL = "webservice.fmb.url";
	
	public final static String WEBSERVICE_LICENSE_CREATION_URL = "webservice.license.creation.url";
	
	public final static String WEBSERVICE_LICENSE_VALIDATION_URL = "webservice.license.validation.url";

	public final static String GEO_LOCATION_ENABLED = "geo.location.enabled";

	public final static String FLEX_DEMO_WAR_URL = "flex.demo.war.url";
	
	public final static String CONCURRENT_MODIFICATION_ENABLED = "concurrent.modification.enabled";
	
	public final static String CLIENTSIDE_EVENT_ENABLED = "clientside.event.enabled";
	
	public static final String ANIMATION_ENABLED = "animation.enabled";
	
	/**
	 * MOBILE SETTINGS
	 */
	public final static String MOBILE_NAVIGATION_PANEL_ENABLED = "mobile.navigation.panel.enabled";
	
	
	private ApplicationContext systemApplication = null;
	

	public ApplicationContext getSystemApplication() {
		return systemApplication;
	}

	public void setSystemApplication(ApplicationContext systemApplication) {
		this.systemApplication = systemApplication;
	}

	/**
	 * key for setting the mdi mode to true or false
	 */
	public final static String LOAD_ON_STARTUP = "load.on.startup";
	
	
	/**
	 * key for setting the stack recorder on or of to true or false
	 */
	public final static String MONITOR_STACK_RECORDER = "monitor.stack.recorder";
	
	/**
	 * key for turning xsd validation over xml mapping files on or of (TRUE OR FALSE) 
	 */
	public final static String QAFE_XML_VALIDATION = "qafe.xml.validation";
	
	/**
	 * key for setting the invalidation time for the localstore in ms 
	 */
	public final static String LOCAL_STORE_INVALIDATION_TIME = "localstore.timeout";
	
	/**
	 * NOTE: when adding a configurationitemname for application configuration, add this name to the following
	 * arry as well, so the default will be fetched
	 */
	private final static String[] APP_CONFIGURATION_KEYS = {
		EVENTHANDLER_IMPL_CLASSNAME,
		BUSINESSMANAGER_IMPL_CLASSNAME,
		DEFAULT_GLOBAL_TRANSACTION_MANAGEMENT,
		SCRIPT_EGINE_NAME,
		LOCAL_STORE_INVALIDATION_TIME
	};
	
	/**
	 * NOTE: when adding a configurationitemname for context configuration (global), 
	 * add this name to the following arry as well, so the default will be fetched
	 */
	private final static String[] GLOBAL_CONFIGURATION_KEYS = {
		EVENTHANDLER_IMPL_CLASSNAME,
		BUSINESSMANAGER_IMPL_CLASSNAME,
		DEFAULT_GLOBAL_TRANSACTION_MANAGEMENT,
		SCRIPT_EGINE_NAME,
		DEVELOPMENT_MODE,
		MAX_UPLOAD_FILESIZE,
		MDI_MODE,
		SHOW_LOG,
		MONITOR_STACK_RECORDER,
		QAFE_XML_VALIDATION,
		LOCAL_STORE_INVALIDATION_TIME,
		DATE_FORMAT,
		DOCKING_PANEL_ENABLED,
		EXTERNAL_PROPERTIES_FILE
		/*,
		SYSTEM_APP*/
		
	};
	
	protected HashMap<String, String> configuration = null;
	
	private Properties  properties = null;
	public Properties getProperties() {
		return properties;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	/**
	 * list for jibx of KeyValue instances
	 */
	protected List<KeyValue> configurationItems;
	
	public String get(String key){
		return  configuration.get(key);
	}
	
	public String stringValueOf(String key){
		return get(key);
	}
	
	/**
	 * this method returns true only if the value
	 * for the given key equals either the string 'TRUE' or 'true'
	 * @param key
	 * @return
	 */
	public boolean booleanValueOf(String key){
		return Boolean.valueOf(stringValueOf(key)).booleanValue(); 
	}
	
	public void put(String key, String value){
		configuration.put(key, value);
	}

	public void addConfigurationItem(String key, String value) {
		if (key == null) {
			return;
		}
		KeyValue keyValue = new KeyValue();
		keyValue.setKey(key);
		keyValue.setValue(value);
		addConfigurationItem(keyValue);
	}
	
	public void addConfigurationItem(KeyValue keyValue){
		if (keyValue == null) {
			return;
		}
		if (configurationItems == null) {
			configurationItems = new ArrayList<KeyValue>();
		}
		configurationItems.add(keyValue);
	}	
	
	private Configuration() {
		super();
		configuration = new HashMap<String, String>();
	}
	
	public Iterator<String> keySetIterator(){
		return configuration.keySet().iterator();
	}
	
	public void performPostProcessing() {
		if(configurationItems!=null && !configurationItems.isEmpty()){
			configuration = new HashMap<String, String>();
			for (Iterator<KeyValue> iter = configurationItems.iterator(); iter.hasNext();) {
				KeyValue configItem = (KeyValue) iter.next();
				configuration.put(configItem.getKey(), configItem.getValue());
			}
			
		}
		
	}

	public void postset(IUnmarshallingContext context) {
		performPostProcessing();
	}
	
	private boolean isGlobalConfigurationItemKey(String key){
		boolean isC = false;
		for (int i = 0; !isC && i < GLOBAL_CONFIGURATION_KEYS.length; i++) {
			isC = GLOBAL_CONFIGURATION_KEYS[i].equals(key);
		}
		return isC;
	}
	
	private boolean isApplicationConfigurationItemKey(String key){
		boolean isC = false;
		for (int i = 0; !isC && i < APP_CONFIGURATION_KEYS.length; i++) {
			isC = APP_CONFIGURATION_KEYS[i].equals(key);
		}
		return isC;
	}
	
	public static Configuration create(Properties properties, boolean global){
		Configuration config = new Configuration();
		if(properties!=null){
			for (Iterator<Object> iter = properties.keySet().iterator(); iter.hasNext();) {
				String key = (String) iter.next();
				if((global && config.isGlobalConfigurationItemKey(key)) ||
				(!global && config.isApplicationConfigurationItemKey(key)))
					config.configuration.put(key, (String)properties.get(key));
			}
		}
		return config;
	}
	
	/**
	 * Method to overwrite null values (properties not set by user) with defaults
	 * read from either <code>global-configuration-defaults.properties</code>
	 * or <code>application-context-defaults.properties</code>
	 */
	public void fillInTheBlanks(Configuration otherConfig){
		if(otherConfig==null)
			throw new IllegalArgumentException("otherConfig cannot be null");
		for (Iterator<String> iter = otherConfig.configuration.keySet().iterator(); iter.hasNext();) {
			String key = (String) iter.next();
			if(!this.configuration.containsKey(key) || this.configuration.get(key)==null)
				this.configuration.put(key, otherConfig.configuration.get(key));
		}
	}
	
	/**
	 * Returns a shallow copy of this <tt>Configuration</tt> instance: the keys and
     * values themselves are not cloned.
     *
     * @return a shallow copy of this configuration.
	 */
	protected Object clone() throws CloneNotSupportedException {
		Configuration config = (Configuration)super.clone();
		config.configuration.clone();
		return config;
	}
}
