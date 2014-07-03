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
package com.qualogy.qafe.web.css.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Logger;

import org.apache.commons.lang.StringUtils;

import com.qualogy.qafe.bind.core.application.ApplicationContext;
import com.qualogy.qafe.bind.core.application.ApplicationIdentifier;
import com.qualogy.qafe.bind.presentation.style.StyleInjector;
import com.qualogy.qafe.bind.util.InterfaceScanner;
import com.qualogy.qafe.core.application.ApplicationCluster;
import com.qualogy.qafe.util.ExceptionHelper;
import com.qualogy.qafe.web.ContextLoaderHelper;

public class CssProvider {
	
	public final static Logger logger = Logger.getLogger(CssProvider.class.getName());

	private Map<String, Map<String, String>> styleRenderers = new HashMap<String, Map<String, String>>();

	private Map<String, String> cachedCSS = new HashMap<String, String>();

	private static CssProvider singleton = null;

	private CssProvider() {
		
		Properties p = new Properties();
		try {
			p.load(this.getClass().getResourceAsStream("supported-style-rendering.properties"));
		} catch (IOException e1) {
			throw new RuntimeException(e1);
		}
		String value = p.getProperty("styles");
		String[] supportedStyles = StringUtils.split(value, ',');

		if (supportedStyles == null || supportedStyles.length == 0) {
			throw new RuntimeException("There is no supported styles defined.");
		}

		for (int i = 0; i < supportedStyles.length; i++) {
			Map<String, String> translationMap = new HashMap<String, String>();
			styleRenderers.put(supportedStyles[i].toLowerCase(), translationMap);

			Properties styleProperties = new Properties();
			try {
				styleProperties.load(this.getClass().getResourceAsStream("qafe-style-rendering-" + supportedStyles[i] + ".properties"));
				translationMap.put("prefixed", styleProperties.getProperty("prefixed"));
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		logger.fine("");

	}

	public static CssProvider getInstance() {
		if (singleton == null) {
			singleton = new CssProvider();
		}
		return singleton;
	}

	public boolean contains(final String cssSelector, final String renderer) {
		if (cssSelector != null && cssSelector.length() > 1) {
			Map<String, String> translationMap = styleRenderers.get(renderer.toLowerCase());
			return translationMap.containsKey(cssSelector.toString().substring(1));
		} else {
			return false;
		}
	}

	public boolean isPrefixed(final String renderer) {
		Map<String, String> translationMap = styleRenderers.get(renderer.toLowerCase());
		return Boolean.valueOf(translationMap.get("prefixed").toString()).booleanValue();
	}

	public String getTranslationValue(final String cssSelector, final String renderer) {
		if (cssSelector != null && cssSelector.length() > 1) {
			Map<String, String> translationMap = styleRenderers.get(renderer.toLowerCase());
			if (translationMap.containsKey(cssSelector.toString().substring(1))) {
				return translationMap.get(cssSelector.toString().substring(1));
			} else {
				return cssSelector;
			}

		} else {
			return cssSelector;
		}
	}
	
	public String getCSS(final String rendererType) {
		return cachedCSS.get(rendererType);
	}

	private Map<String, InputStream> findResourceInFile(File resourceFile, String fileNamePattern) throws IOException {
		Map<String, InputStream> result = new TreeMap<String, InputStream>();
		if (resourceFile.canRead() && resourceFile.getAbsolutePath().endsWith(".jar")) {
			JarFile jarFile = new JarFile(resourceFile);
			Enumeration<JarEntry> entries = jarFile.entries();
			while (entries.hasMoreElements()) {
				JarEntry singleEntry = entries.nextElement();
				if (singleEntry.getName().matches(fileNamePattern)) {
					result.put(jarFile.getName() + "/" + singleEntry.getName(), jarFile.getInputStream(singleEntry));
				}
			}
		}
		return result;
	}

	private Map<String, InputStream> findResourceInDirectory(File directory, String fileNamePattern) throws IOException {
		Map<String, InputStream> result = new TreeMap<String, InputStream>();
		File[] files = directory.listFiles();
		for (File currentFile : files) {
			if (currentFile.getAbsolutePath().matches(fileNamePattern)) {
				result.put(currentFile.getAbsolutePath(), new FileInputStream(currentFile));
			} else if (currentFile.isDirectory()) {
				result.putAll(findResourceInDirectory(currentFile, fileNamePattern));
			} else {
				result.putAll(findResourceInFile(currentFile, fileNamePattern));
			}
		}
		return result;
	}

	public Map<String, InputStream> findFilesInClassPath(String fileNamePattern) {
		Map<String, InputStream> result = new TreeMap<String, InputStream>();
		String classPath = System.getProperty("java.class.path");
		String[] pathElements = classPath.split(System.getProperty("path.separator"));
		for (String element : pathElements) {

			try {
				File newFile = new File(element);
				if (newFile.isDirectory()) {
					result.putAll(findResourceInDirectory(newFile, fileNamePattern));
				} else {
					result.putAll(findResourceInFile(newFile, fileNamePattern));
				}
			} catch (IOException e) {

			}
		}
		return result;
	}

	public String generateTypedCSS(String rendererType, String applicationId) {
		if (ContextLoaderHelper.isDebugMode()){
			cachedCSS.clear();
		}
			
		if (rendererType != null) {
			Iterator<ApplicationIdentifier> contextItr = ApplicationCluster.getInstance().keysIterator();
			String cssData = "";
			if (contextItr.hasNext()) {
				while (contextItr.hasNext()) {
					ApplicationIdentifier key = contextItr.next();
					ApplicationContext context = ApplicationCluster.getInstance().get(key);// (ApplicationContext)contextItr.next();
					if (context!=null && context.getApplicationMapping()!=null){
						List<StyleInjector> styleInjectors = InterfaceScanner.scan(context.getApplicationMapping(), StyleInjector.class, "com.qualogy.qafe");
	
						logger.fine("style " + rendererType + "\t appId: " + key.toString() + ", styles :" + styleInjectors.size());
						if (styleInjectors.size() > 0) {
							String css = "";	
							for (StyleInjector sI : styleInjectors) {
								if (sI != null) {
									if (sI.getCssData() != null) {
										css = new String(sI.getCssData());
										cssData += css;
									}
								}
							}
							
							cssData = replaceWithRendererTypeCss(rendererType, cssData);
							
						} 
					}
				}
				logger.fine("Generated css for type = " + rendererType + "\n" + cssData);
				cachedCSS.put(rendererType, cssData);
			} else { // there are no applications defined in the cluster
				cachedCSS.put(rendererType, cssData);
			}
			
			return cachedCSS.get(rendererType).toString();
		} else {
			return "";
		}

	}

	public String replaceWithRendererTypeCss(String rendererType, String cssData) {
		Properties styleProperties = new Properties();
		try {
			styleProperties.load(this.getClass().getResourceAsStream("qafe-style-rendering-" + rendererType + ".properties"));
			Iterator itr = styleProperties.keySet().iterator();
			while(itr.hasNext()) {
				String qafeStyleClass = itr.next().toString();
				if(cssData.contains(qafeStyleClass)) {
					String replacementStyleClass = styleProperties.getProperty(qafeStyleClass);
					cssData = cssData.replaceAll(qafeStyleClass, replacementStyleClass);
				}
			}
		} catch (IOException e) {
			logger.fine(ExceptionHelper.printStackTrace(e));
			throw new RuntimeException(e);
		}
		return cssData;
	}
}
