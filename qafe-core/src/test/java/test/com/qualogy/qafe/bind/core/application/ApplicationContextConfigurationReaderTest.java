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
package test.com.qualogy.qafe.bind.core.application;

import java.io.File;
import java.util.Iterator;

import junit.framework.TestCase;

import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.StringUtils;

import com.qualogy.qafe.bind.core.application.ApplicationContext;
import com.qualogy.qafe.bind.core.application.ApplicationStack;
import com.qualogy.qafe.bind.core.application.Configuration;
import com.qualogy.qafe.bind.io.Reader;
import com.qualogy.qafe.core.application.ApplicationContextLoader;

public class ApplicationContextConfigurationReaderTest extends TestCase {

	private Reader reader = new Reader(ApplicationStack.class);
	
	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	private String getSamplesDir(){
		String pckName = ClassUtils.getPackageName(this.getClass());
		return StringUtils.replace(pckName, ".", "/") + "/";
	}
	/**
	 * test if no exceptions occur upon read
	 *
	 */
	public void testHappyDay(){
		reader.read(getSamplesDir()+"happyday.xml");
	}
	
	public void testValuesBound(){
		ApplicationStack configs = (ApplicationStack)reader.read(getSamplesDir()+"checkvalues.xml");
		String samplesDir = getSamplesDir();
		int i = 1;
		for (Iterator<ApplicationContext> iter = configs.getApplicationsIterator(); iter.hasNext();i++) {
			ApplicationContext config = (ApplicationContext) iter.next();
			String appName = "app"+i;
			assertEquals(config.getId().stringValueOf(), appName);
			assertEquals(new File(config.getRoot()).getAbsolutePath(), new File(samplesDir).getAbsolutePath());
//			MessageFileLocation resource = (MessageFileLocation)config.getMessageResources().get(0);
//			assertEquals(resource.getFilePath(), ""+i+".xml");
			assertEquals(config.getConfigurationItem(Configuration.DEFAULT_GLOBAL_TRANSACTION_MANAGEMENT), "local"+i);
		}
	}
	
	public void testSystemMenuLoad(){
		ApplicationContextLoader.load(getSamplesDir()+"checksystem-menu.xml",true);
	}
}
