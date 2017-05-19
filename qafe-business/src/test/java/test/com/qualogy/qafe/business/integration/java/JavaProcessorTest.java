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
package test.com.qualogy.qafe.business.integration.java;

import java.util.Iterator;

import junit.framework.TestCase;

import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.StringUtils;

import com.qualogy.qafe.bind.business.action.BusinessAction;
import com.qualogy.qafe.bind.core.application.ApplicationContext;
import com.qualogy.qafe.bind.domain.ApplicationMapping;
import com.qualogy.qafe.core.application.ApplicationCluster;
import com.qualogy.qafe.core.application.ApplicationContextLoader;
import com.qualogy.qafe.core.datastore.DataIdentifier;
import com.qualogy.qafe.core.datastore.DataStore;
import com.qualogy.qafe.core.errorhandling.ExternalException;


public class JavaProcessorTest extends TestCase{
	public final static String PACKAGE = "test.com.qualogy.genesis.core.application.";
	
	private static String getSamplesDir(){
		String pckName = ClassUtils.getPackageName(JavaProcessorTest.class);
		return StringUtils.replace(pckName, ".", "/") + "/";
	}
	
	
	public void testLoad() throws ExternalException{
		ApplicationContextLoader.load(JavaProcessorTest.getSamplesDir() + "application-config.xml");
		for (Iterator<ApplicationContext> iterator = ApplicationCluster.getInstance().iterator(); iterator.hasNext();) {
			ApplicationContext context = iterator.next();
			
			ApplicationMapping mapping = context.getApplicationMapping();
			for (int i = 1; i < mapping.getBusinessTier().getBusinessActions().size(); i++){ 
				
				BusinessAction action = mapping.getBusinessTier().getBusinessAction("" + i);
				
				DataIdentifier dataId = DataStore.register();
				
				System.out.println("app ["+context.getId()+"] + actionId ["+action.getId()+"]");
				context.getBusinessManager().manage(context, dataId, action);
			}
		}
	}
	
}

