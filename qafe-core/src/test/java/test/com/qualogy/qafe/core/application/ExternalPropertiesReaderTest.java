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
package test.com.qualogy.qafe.core.application;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import junit.framework.TestCase;

import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.StringUtils;

import com.qualogy.qafe.core.application.ApplicationCluster;
import com.qualogy.qafe.core.application.ApplicationContextLoader;

public class ExternalPropertiesReaderTest extends TestCase {
	private String getSamplesDir(){
		String pckName = ClassUtils.getPackageName(this.getClass());
		return StringUtils.replace(pckName, ".", "/") + "/";
	}
	
	public void testWithExternalProperites(){
		ApplicationContextLoader.load(getSamplesDir() + "application-config-with-external-properties.xml");
		Map<String, String> externalProperties = getExternalProperties();
		assertEquals("Not expected value", Boolean.FALSE.toString() , externalProperties.get("mode.mdi").toString());
		assertEquals("Not expected value", "30" , externalProperties.get("dock.magnify").toString());		
	}
	
	public void testWithNoExternalProperites(){
		ApplicationContextLoader.load(getSamplesDir() + "application-config-without-external-properties.xml");
		Map<String, String> externalProperties = getExternalProperties();
		assertEquals("Not expected value", Boolean.FALSE.toString() , externalProperties.get("mode.mdi").toString());
		assertEquals("Not expected value", "30" , externalProperties.get("dock.magnify").toString());		
	}
	
	public void testWithCutomExternalProperites(){
		ApplicationContextLoader.load(getSamplesDir() + "application-config-with-custom-external-properties.xml");
		Map<String, String> externalProperties = getExternalProperties();
		assertEquals("Not expected value", Boolean.TRUE.toString() , externalProperties.get("mode.mdi").toString());
		assertEquals("Not expected value", "30" , externalProperties.get("dock.magnify").toString());		
	}

	public static Map<String,String> getExternalProperties(){
		Map<String,String> externalProperties = new HashMap<String,String>();
		Iterator itr = ApplicationCluster.getInstance().getGlobalConfiguration().keySetIterator();
		while(itr.hasNext()){
			String key = itr.next().toString();
			externalProperties.put(key, ApplicationCluster.getInstance().getConfigurationItem(key));
		}
		return externalProperties;
	}
}
