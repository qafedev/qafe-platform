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
package com.qualogy.qafe.util.io;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Properties;

import com.qualogy.qafe.util.ClassUtils;

/**
 * Convenient utility methods for loading of <code>java.util.Properties</code>,
 * performing standard handling of input streams.
 * 
 * NOTE: Use for initialisation fase only since no properties are cached!
 */
public class PropertiesUtils {
	
	/**
	 * Load all properties from the given class path resource,
	 * using the default class loader.
	 * <p>Merges properties if more than one resource of the same name
	 * found in the class path.
	 * @param resourceName the name of the class path resource
	 * @return the populated Properties instance
	 * @throws IOException if loading failed
	 */
	public static Properties loadAllProperties(String resourceName) throws IOException {
		return loadAllProperties(resourceName, null);
	}

	public static String getProperty(String resourceName, String propertyName) throws IOException{
		Properties props = loadAllProperties(resourceName);
		return (String)props.getProperty(propertyName);
	}
	
	public static String getKey(String resourceName, String propertyValue) throws IOException{
		String key = null;
		
		Properties props = loadAllProperties(resourceName);
		if(props!=null && props.containsValue(propertyValue)){
			for (Iterator iter = props.keySet().iterator(); iter.hasNext();) {
				String tmp = (String) iter.next();
				if(props.get(key).equals(propertyValue)){
					key = tmp;
					break;
				}
			}
		}
		return key;
	}
	
	/**
	 * Load all properties from the given class path resource,
	 * using the given class loader.
	 * <p>Merges properties if more than one resource of the same name
	 * found in the class path.
	 * @param resourceName the name of the class path resource
	 * @param classLoader the ClassLoader to use for loading
	 * (or <code>null</code> to use the default class loader)
	 * @return the populated Properties instance
	 * @throws IOException if loading failed
	 */
	public static Properties loadAllProperties(String resourceName, ClassLoader classLoader) throws IOException {
		if(resourceName==null)
			throw new IllegalArgumentException("Resource name must not be null");
		
		ClassLoader clToUse = classLoader;
		if (clToUse == null) {
			clToUse = ClassUtils.getDefaultClassLoader();
		}
		Properties properties = new Properties();
		Enumeration urls = clToUse.getResources(resourceName);
		while (urls.hasMoreElements()) {
			URL url = (URL) urls.nextElement();
			InputStream is = null;
			try {
				URLConnection con = url.openConnection();
				con.setUseCaches(false);
				is = con.getInputStream();
				properties.load(is);
			}
			finally {
				if (is != null) {
					is.close();
				}
			}
		}
		return properties;
	}

	/**
	 * Load the general properties this application needs in order to run.
	 */
	public static Properties loadPropertiesFile(String propertyFileName, Class clazz) {
		Properties properties = new Properties();
		InputStream in = null;
		try {
			in = clazz.getResourceAsStream(propertyFileName);
			properties.load(in);
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return properties;
	}
	
	/**
	 * 
	 * @param propertyFileName
	 * @return
	 */
	public static Properties loadPropertiesFile(String propertyFileName) {
		return loadPropertiesFile(propertyFileName, PropertiesUtils.class);
	}
}
