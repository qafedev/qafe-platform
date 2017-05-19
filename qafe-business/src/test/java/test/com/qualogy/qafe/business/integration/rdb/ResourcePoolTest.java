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
package test.com.qualogy.qafe.business.integration.rdb;

import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;

import com.qualogy.qafe.bind.core.application.ApplicationContext;
import com.qualogy.qafe.bind.core.application.ApplicationStack;
import com.qualogy.qafe.bind.io.Reader;
import com.qualogy.qafe.business.resource.Resource;
import com.qualogy.qafe.business.resource.ResourcePool;
import com.qualogy.qafe.business.resource.rdb.RDBDatasource;
import com.qualogy.qafe.business.test.BusinessActionTestCase;

public class ResourcePoolTest extends BusinessActionTestCase {
	
	public void testLoadTwoApps() throws Exception, IllegalAccessException, InvocationTargetException{
		ApplicationStack contexts = (ApplicationStack)new Reader(ApplicationStack.class).read(getDirBasedUponPackage() + "application-config.xml");
		for (Iterator iter = contexts.getApplicationsIterator(); iter.hasNext();) {
			ApplicationContext context = (ApplicationContext) iter.next();
			ResourcePool.getInstance().init(context);
		}
		
		for (Iterator iter = contexts.getApplicationsIterator(); iter.hasNext();) {
			ApplicationContext context = (ApplicationContext) iter.next();
			assertNotNull(ResourcePool.getInstance().get(context.getId(), "db"));			
		}
	}
	
	public void testStatementLoad(){
		
		Resource resource = ResourcePool.getInstance().get(context.getId(), "db");
		assertNotNull("resource databaseService test not found", resource);
		if(resource.getName().equals("database")){
			if(!(resource instanceof RDBDatasource))
				fail("resource is instanceof ["+resource.getClass()+"] should be " + RDBDatasource.class);
			
			assertNotNull(((RDBDatasource)resource).get("setup"));
		}
		
	}
	public String getAppContextDir() {
		return getDirBasedUponPackage();
	}
}
