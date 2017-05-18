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
package test.com.qualogy.qafe.bind.io;

import java.io.File;
import java.util.Properties;

import junit.framework.TestCase;

import com.qualogy.qafe.core.application.ConfigurationPropertiesLoader;
/**
 * a test for testing the configurationproperties
 * @author rjankie
 *
 */
public class ConfigurationPropertiesTest extends TestCase {
	
	public final static String SAMPLES_DIR_PATH = "samples/properties/";
	
	public final static String OUTPUT_DIR_PATH = "../output/properties/";
	

	public void testRead(){
		Properties properties = ConfigurationPropertiesLoader.read(SAMPLES_DIR_PATH+"testproperties.xml");
		assertNotNull(properties);
		
	}
	
	public void testReadProperties(){
		Properties properties = ConfigurationPropertiesLoader.read(SAMPLES_DIR_PATH+"testproperties.xml");
		assertNotNull(properties);
		String value = properties.getProperty("toolbar");
		assertTrue("true".equals(value));	
	}

	
	public void testWrite(){
		String fileName =OUTPUT_DIR_PATH+"testproperties-output.xml";
		Properties properties = ConfigurationPropertiesLoader.read(SAMPLES_DIR_PATH+"testproperties.xml");
		
		File directory = new File(OUTPUT_DIR_PATH);
		if (!directory.exists()){
			directory.mkdirs();
		}
		ConfigurationPropertiesLoader.writeToFile(fileName, properties, "Output generated");	
		
	}
}
