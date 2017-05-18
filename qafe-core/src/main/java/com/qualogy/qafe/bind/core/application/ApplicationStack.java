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
package com.qualogy.qafe.bind.core.application;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

import org.apache.commons.io.FilenameUtils;
import org.jibx.runtime.IUnmarshallingContext;

import com.qualogy.qafe.bind.PostProcessing;
import com.qualogy.qafe.bind.domain.ApplicationMapping;
import com.qualogy.qafe.bind.io.Reader;
import com.qualogy.qafe.core.application.LoadFailedException;
import com.qualogy.qafe.core.application.QAFEKeywords;
import com.qualogy.qafe.util.ExceptionHelper;
/**
 * holder for application contexts and global configuration
 *
 *
 */
public class ApplicationStack implements PostProcessing{
	
	protected List<ApplicationContext> applications;
	public final static Logger logger = Logger.getLogger(ApplicationStack.class.getName());

	/**
	 * holder for configuration items
	 */
	protected Configuration globalConfiguration;

	/*
	 *  The applicaton File path that is needed to get the information for external properties and other things.
	 */
	private URI applicationFilePath;

	public void addApplication (ApplicationContext application){
		if (applications == null) applications = new ArrayList<ApplicationContext>();
		applications.add(application);
	}
	
	/**
	 * returns 0 when private member applications not initialized
	 * @return
	 */
	public int size(){
		return applications!=null?applications.size():0;
	}

	public ApplicationContext get(ApplicationIdentifier id) {
		ApplicationContext context = null;
		if(applications!=null){
			for (Iterator<ApplicationContext> iter = getApplicationsIterator(); iter!=null && iter.hasNext();) {
				ApplicationContext tmp = (ApplicationContext) iter.next();
				if(id.equals(tmp.getId())){
					context = tmp;
					break;
				}
			}
		}
		return context;
	}

	public Configuration getGlobalConfiguration() {
		return globalConfiguration;
	}
	
	public void setGlobalConfiguration(Configuration globalConfiguration) {
		this.globalConfiguration = globalConfiguration;
	}

	public void performPostProcessing() {
		
		//global configuration
		if(globalConfiguration==null)
			globalConfiguration = ConfigurationDefaults.getInstance().getGlobalConfiguration();
		else	
			globalConfiguration.fillInTheBlanks(ConfigurationDefaults.getInstance().getGlobalConfiguration());
	}

	public void postset(IUnmarshallingContext context) {
		performPostProcessing();
	}
	/**
	 * @return null when private member applications not initialized
	 */
	public Iterator<ApplicationContext> getApplicationsIterator() {
		return applications!=null?applications.iterator():null;
	}
	
	@Deprecated
	public void loadSystemMenu(URI filePath){
		if (globalConfiguration!=null && filePath!=null){
			String fileName = globalConfiguration.get(QAFEKeywords.SYSTEM_APP);
			// For the system file menu we need to look it up at another location
			ApplicationMapping appMapping =  null;
			if (fileName.indexOf(QAFEKeywords.SYSTEM_APP_DEFAULT_FILENAME)>=0){
				InputStream in =getClass().getResourceAsStream("/"+QAFEKeywords.SYSTEM_APP_DEFAULT_FILENAME);
				if (in!=null){
					appMapping = (ApplicationMapping) new Reader(ApplicationMapping.class).read(in);					
				}
			} else{
				File file = new File(filePath);			
				String location = file.getAbsolutePath().substring(0,file.getAbsolutePath().lastIndexOf(File.separatorChar))+File.separatorChar + fileName;
				appMapping =((ApplicationMapping) new Reader(ApplicationMapping.class).read(location));
			}
			if (appMapping!=null){
				ApplicationContext applicationContext = new ApplicationContext();
				applicationContext.setApplicationMapping(appMapping);
				File file = new File(filePath);
				File origLocation = new File(file.getAbsolutePath().substring(0,file.getAbsolutePath().lastIndexOf(File.separatorChar)));
				applicationContext.setOriginAppConfigFileLocation(origLocation.toURI());
				applicationContext.setId(new ApplicationIdentifier(QAFEKeywords.SYSTEM_APP));
				globalConfiguration.setSystemApplication(applicationContext);
			}
		}
	}

	public void setOriginalFilePath(URI applicationFilePath) {
		this.applicationFilePath = applicationFilePath;
		
	}
	
	public void init() {
		if (getGlobalConfiguration()!=null){
			loadExternalProperties();				
		}		
	}

	private void loadExternalProperties() {
		if (getGlobalConfiguration().get(Configuration.EXTERNAL_PROPERTIES_FILE)!=null) {
			try {					
				File file = new File(applicationFilePath);				
				String path =file.getAbsolutePath();
				path = FilenameUtils.getFullPath(path);					
				
				loadDefaultExternalPropertiesFile(path);					
				if(isNonDefaultExternalPropertiesSpecified()) {
					loadCustomExternalPropertiesFile(path);
				}
				
			} catch (FileNotFoundException e) {
				logger.fine(ExceptionHelper.printStackTrace(e));
				throw  new LoadFailedException("Global Configuration","The external properties file ("+globalConfiguration.get(Configuration.EXTERNAL_PROPERTIES_FILE)+") could not be found (make sure it is in the same location as where the application-config.xml is)" , e);
			} catch (IOException e) {
				logger.fine(ExceptionHelper.printStackTrace(e));
				throw  new LoadFailedException("Global Configuration","The external properties file ("+globalConfiguration.get(Configuration.EXTERNAL_PROPERTIES_FILE)+") has an I/O problem (make sure it is in the same location as where the application-config.xml is and not locked by another program)" , e);
			}
		}
	}

	private void loadDefaultExternalPropertiesFile(String path)
			throws IOException, FileNotFoundException {		
		File defaultExternalPropertiesFile = new File(path + File.separator + Configuration.EXTERNAL_PROPERTIES_FILE);
		if(defaultExternalPropertiesFile.exists()) {
			loadFromPropertiesFile(defaultExternalPropertiesFile);
		} else {
			logger.fine("The external properties file (" + path + ") could not be found (make sure it is in the same location as where the application-config.xml is)");
		}
	}

	private void loadFromPropertiesFile(File externalPropertiesFile) throws IOException,
			FileNotFoundException {
		Properties p = new Properties();
		p.load(new FileInputStream(externalPropertiesFile));					
		Iterator<Object> itr = p.keySet().iterator();
		while (itr.hasNext()){
			Object o = itr.next();
			getGlobalConfiguration().put(o.toString(), p.getProperty(o.toString()));
		}
	}
	
	private boolean isNonDefaultExternalPropertiesSpecified() {		
		Object customExternalFileValue = getGlobalConfiguration().get(Configuration.EXTERNAL_PROPERTIES_FILE);
		if((customExternalFileValue != null) && !(Configuration.EXTERNAL_PROPERTIES_FILE.equals(customExternalFileValue.toString()))) {
			return true;
		}
		return false;
	}
	
	private void loadCustomExternalPropertiesFile(String path)
			throws IOException, FileNotFoundException {		
		File customPropertiesFile = new File(path + File.separator + getGlobalConfiguration().get(Configuration.EXTERNAL_PROPERTIES_FILE));
		loadFromPropertiesFile(customPropertiesFile);
	}
}
