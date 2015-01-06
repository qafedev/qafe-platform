/**
 * Copyright 2008-2015 Qualogy Solutions B.V.
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
package test.com.qualogy.qafe.bind.core.application.security;

import junit.framework.TestCase;

import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.StringUtils;

import com.qualogy.qafe.bind.core.application.ApplicationStack;
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
	public void testAppConfig(){
		reader.read(getSamplesDir()+"app-config.xml");
	}
	
}
