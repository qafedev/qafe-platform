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
package com.qualogy.qafe.core.application;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;
import java.util.logging.Logger;

import com.qualogy.qafe.util.ExceptionHelper;

/**
 * Class with functionality to load configurations
 * 
 * @author rjankie
 * 
 */
public class ConfigurationPropertiesLoader {

	public final static Logger logger = Logger.getLogger(ConfigurationPropertiesLoader.class.getName());

	public static Properties read(String filePath) {
		Properties prop = new Properties();
		FileInputStream fis;
		try {
			fis = new FileInputStream(filePath);
			prop.loadFromXML(fis);
		} catch (Exception e) {
			logger.severe(ExceptionHelper.printStackTrace(e));
		}

		return prop;
	}

	public static void writeToFile(String filePath, Properties properties, String comment) {
		if (properties != null) {
			try {
				FileOutputStream fout = new FileOutputStream(new File(filePath));
				write(filePath, properties, comment, fout);
			} catch (FileNotFoundException e) {
				logger.severe(ExceptionHelper.printStackTrace(e));
			}
		} else {
			logger.info("There is no properties file written, since the properties are null! ");
		}
	}

	public static void write(String filePath, Properties properties, String comment, OutputStream out) {
		if (properties != null) {
			if (out != null) {
				try {
					properties.storeToXML(out, comment);
				} catch (IOException e) {
					logger.severe(ExceptionHelper.printStackTrace(e));
				}
			} else {
				logger.info("There is no valid output stream, since the outputstream is null! ");
			}
		} else {
			logger.info("There is no properties file written, since the properties are null! ");
		}
	}
}
