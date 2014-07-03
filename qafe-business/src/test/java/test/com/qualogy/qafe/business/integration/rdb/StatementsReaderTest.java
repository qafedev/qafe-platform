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
package test.com.qualogy.qafe.business.integration.rdb;

import java.util.Iterator;

import junit.framework.TestCase;

import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.StringUtils;

import com.qualogy.qafe.bind.core.application.ApplicationContext;
import com.qualogy.qafe.bind.core.application.ApplicationStack;
import com.qualogy.qafe.bind.io.Reader;
import com.qualogy.qafe.bind.resource.query.QueryContainer;

public class StatementsReaderTest extends TestCase{
	
	public void testReadStatement(){
		
		QueryContainer qc = (QueryContainer)new Reader(QueryContainer.class).read(getDirBasedUponPackage() + "statements.xml");
		for (Iterator<String> iter = qc.keySet().iterator(); iter.hasNext();) {
			System.out.println((String) iter.next());
			
		}
	}
	public void testReadContext(){
		ApplicationStack contexts = (ApplicationStack)new Reader(ApplicationStack.class).read(getDirBasedUponPackage() + "application-config.xml");
		for (Iterator<ApplicationContext> iter = contexts.getApplicationsIterator(); iter.hasNext();) {
			ApplicationContext context = (ApplicationContext) iter.next();
//			ResourcePool.getInstance().init(context);
		}
	}
	private String getDirBasedUponPackage() {
		String pckName = ClassUtils.getPackageName(this.getClass());
		return StringUtils.replace(pckName, ".", "/") + "/";
	}
}
