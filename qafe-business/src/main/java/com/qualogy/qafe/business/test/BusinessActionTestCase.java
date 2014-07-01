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
package com.qualogy.qafe.business.test;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import junit.framework.TestCase;

import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.StringUtils;

import com.qualogy.qafe.bind.business.action.BusinessAction;
import com.qualogy.qafe.bind.core.application.ApplicationContext;
import com.qualogy.qafe.business.resource.ResourcePool;
import com.qualogy.qafe.core.application.ApplicationCluster;
import com.qualogy.qafe.core.application.ApplicationContextLoader;
import com.qualogy.qafe.core.datastore.DataIdentifier;
import com.qualogy.qafe.core.datastore.DataStore;
import com.qualogy.qafe.core.errorhandling.ExternalException;

public abstract class BusinessActionTestCase extends TestCase{
	
	private static final Logger logger = Logger.getLogger(BusinessActionTestCase.class.getName());
	
	public final static String DEFAULT_APP_CONTEXT_FILE_NAME = "application-config.xml";
	
	private List actions = null;
	protected DataIdentifier dataId;
	protected ApplicationContext context;
	
	protected void setUp() throws Exception{
		super.setUp();
		String configFile = getAppContextDir() + File.separator + DEFAULT_APP_CONTEXT_FILE_NAME;
		logger.fine("config location ["+configFile+"]");
		
		ApplicationCluster.getInstance().clear();
		ApplicationContextLoader.load(configFile);
		for (Iterator iter = ApplicationCluster.getInstance().iterator(); iter.hasNext();) {
			context = (ApplicationContext) iter.next();//expect there to be one in the file
		}
		
		if(context==null)
			fail("context is null");
			
		logger.info(context.toString());
		
		dataId = DataStore.register();
		
		this.actions = context.getApplicationMapping().getBusinessTier().getBusinessActions();
		
		String[] setupActions = getSetupActions();
		for (int i = 0; i < setupActions.length; i++) {
			manage(setupActions[i]);
		}
	}
	
	protected void tearDown() throws Exception{
		super.tearDown();
		String[] tearDownActions = getTearDownActions();
		for (int i = 0; i < tearDownActions.length; i++) {
			manage(tearDownActions[i]);
		}
		ApplicationContextLoader.unload();
	}
	
	protected String[] getSetupActions(){
		return new String[0];
	}
	protected String[] getTearDownActions(){
		return new String[0];
	}
	
	public abstract String getAppContextDir();
	
	public void manage(String businessActionId, String[][] data) throws Exception{
		storeData(data);
		manage(businessActionId);
	}
	
	public void manage(String businessActionId) throws ExternalException{
		logger.info(ResourcePool.getInstance().toLogString());
		
		BusinessAction ba = BusinessActionLookup.getBusinessActionForId(actions, businessActionId);
		if(ba==null)
			throw new NullPointerException("businessaction not found for id["+businessActionId+"]");
		context.getBusinessManager().manage(context, dataId, ba);
		
		logger.info(DataStore.toLogString(dataId));
	}
	
	protected void storeData(String[][] data){
		for (int i = 0; i < data.length; i++) {
			DataStore.store(dataId, data[i][0], data[i][1]); 
		}
	}
	
	protected void logDataStore(){
			logger.fine(DataStore.toLogString(dataId));
	}
	
	protected String getDirBasedUponPackage(){
		String pckName = ClassUtils.getPackageName(this.getClass());
		return StringUtils.replace(pckName, ".", "/") + "/";
	}
}
