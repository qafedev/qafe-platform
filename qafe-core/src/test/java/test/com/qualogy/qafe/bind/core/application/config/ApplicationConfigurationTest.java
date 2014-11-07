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
package test.com.qualogy.qafe.bind.core.application.config;

import java.util.Iterator;

import junit.framework.TestCase;

import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.StringUtils;

import com.qualogy.qafe.bind.core.application.ApplicationContext;
import com.qualogy.qafe.bind.core.application.ApplicationStack;
import com.qualogy.qafe.bind.core.application.Configuration;
import com.qualogy.qafe.bind.io.Reader;

public class ApplicationConfigurationTest  extends TestCase {
	
	private Reader reader = new Reader(ApplicationStack.class);
	
	private String getSamplesDir(){
		String pckName = ClassUtils.getPackageName(this.getClass());
		return StringUtils.replace(pckName, ".", "/") + "/";
	}
	
	/**
	 * test if no exceptions occur upon read
	 *
	 */
	public void testClusterConfig(){
		ApplicationStack stack = (ApplicationStack)reader.read(getSamplesDir()+"cluster-config.xml");
		Configuration config = stack.getGlobalConfiguration();
		assertEquals(config.get(Configuration.DEFAULT_GLOBAL_TRANSACTION_MANAGEMENT), Configuration.DEFAULT_GLOBAL_TRANSACTION_MANAGEMENT);
	}
	/**
	 * test if no exceptions occur upon read
	 *
	 */
	public void testAppConfig(){
		ApplicationStack stack = (ApplicationStack)reader.read(getSamplesDir()+"app-config.xml");
		for (Iterator<ApplicationContext> iter = stack.getApplicationsIterator(); iter.hasNext();) {
			ApplicationContext context = iter.next();
			assertEquals(context.getConfigurationItem(Configuration.DEFAULT_GLOBAL_TRANSACTION_MANAGEMENT), Configuration.DEFAULT_GLOBAL_TRANSACTION_MANAGEMENT);
		}
		
	}
	/**
	 * test if no exceptions occur upon read
	 *
	 */
	public void testDefaultConfig(){
		ApplicationStack stack = (ApplicationStack)reader.read(getSamplesDir()+"app-config.xml");
		assertEquals(stack.getGlobalConfiguration().get(Configuration.DEVELOPMENT_MODE), Boolean.FALSE.toString());
		printConfig(stack.getGlobalConfiguration());
		
		for (Iterator<ApplicationContext> iter = stack.getApplicationsIterator(); iter.hasNext();) {
			ApplicationContext context = iter.next();
			assertEquals(context.getConfigurationItem(Configuration.DEVELOPMENT_MODE), Boolean.FALSE.toString());
		}
	}
	
	private void printConfig(Configuration config){
		for (Iterator<String> iter = config.keySetIterator(); iter.hasNext();) {
			String key = iter.next();
			System.out.println(key + "=" + config.get(key));
		}
	}
}
