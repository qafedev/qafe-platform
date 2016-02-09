/**
 * Copyright 2008-2016 Qualogy Solutions B.V.
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
package com.qualogy.qafe.core.io.jar;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

/**
 * This loader, loads external classes in a library denoted by given jarfilelocation uri.
 * All refenced libraries by the external lib are also loaded according to the jars manifest. 
 * @author 
 *
 */
public class ExternalClassLoader {
	
	/**
	 * Method to load a class from an external jar denoted by given jar file location
	 * URI. The class loaded is the one denoted by the given className.
	 * Referenced classes in external libs are loaded through instructions in the manifest.
	 * @param jarfileLocation
	 * @param className
	 * @return class if it can be loaded
	 * @throws UnableToLoadException - when class cannot be loaded because ClassNotFoundException 
	 * 		(for desired class) or MalformedURLException (for given jars either through URI and or manifest)
	 * 		or URISyntaxException (for given jars either through URI and or manifest)
	 */
	public static Class<?> loadClass(URI jarFileLocation, String className){
		if(jarFileLocation==null)
			throw new IllegalArgumentException("jarFileLocation cannot be null");
		
		ClassLoader loader = create(jarFileLocation);
		
		return loadClass(loader, className);
	}
	
	/**
	 * @see ExternalClassLoader.loadClass(URI, className)
	 * @param externalClassLoader
	 * @param className
	 * @return
	 */
	private static Class<?> loadClass(ClassLoader externalClassLoader, String className){
		
		Class<?> clazz = null;
		try {
			clazz = externalClassLoader.loadClass(className);
		} catch (ClassNotFoundException e) {
			throw new UnableToLoadException(e);
			
		}
		return clazz;
		
	}
	
	public static ClassLoader create(URI jarFileLocation){
		URL[] urls = createURLs(jarFileLocation);
		
		URLClassLoader loader = new URLClassLoader(urls);
		
		return loader;
	}
	
	private static URL[] createURLs(URI jarFileLocation){ 
		List<URL> listUrls = new ArrayList<URL>();
		try{
			File file = new File(jarFileLocation);
			if(!file.exists())
				throw new UnableToLoadException("file not found ["+file.getAbsolutePath()+"]");
			
			if(file.getName().endsWith(".jar")){
				listUrls.add(file.toURL());
			}else if(file.isDirectory()){
				String[] locations = file.list();
				for (int i = 0; i < locations.length; i++) {//nested within dir not implemented yet
					if(new File(locations[i]).isDirectory())
						continue;
					URI uri = new URI(jarFileLocation.toString() + locations[i]);
					URL[] tmp = createURLs(uri);
					if(tmp==null || tmp.length!=1) continue;
					
					if(tmp.length==1)
						listUrls.add(tmp[0]);
				}
			}
		}catch(MalformedURLException e){
			throw new UnableToLoadException(e);
		} catch (URISyntaxException e) {
			throw new UnableToLoadException(e);
		}
		
		return (URL[]) listUrls.toArray(new URL[listUrls.size()]);
	}
}
