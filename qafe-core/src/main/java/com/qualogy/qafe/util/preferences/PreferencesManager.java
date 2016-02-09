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
package com.qualogy.qafe.util.preferences;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

public class PreferencesManager {
	
	private static final PreferencesManager instance = new PreferencesManager();
	
	private Preferences preferences;
	
	private PreferencesManager() {
		if (instance != null) {
			throw new IllegalStateException();
		} else {
			preferences = loadPreferences();
		}
	}
	
	public static PreferencesManager getInstance() {
		return instance;
	}
	
	public Preferences getPreferences() {
		return preferences;
	}
	
	public void settPreferences(Preferences preferences) {
		this.preferences = preferences;
	}
	
	public void storePreferences() {
		Properties properties = new Properties();
		properties.putAll(this.preferences.getAll());
		OutputStream os;
		try {
			os = new FileOutputStream(new File(getPreferenceFullFileName()));
			properties.store(os, "modified");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
	private static Preferences loadPreferences() {
		
		Properties properties = new Properties();	
		
		
		File preferenceFile = new File(getPreferenceFullFileName());
		InputStream is = null;
		
		try {
			is = new FileInputStream(preferenceFile);
			properties.load(is);
		} catch (FileNotFoundException e) {
			return null;
		} catch (IOException e) {
			return null;
		}
		
		if(properties.values() == null || properties.values().isEmpty()) {
			return null;
		}
				
		Preferences preferences = new Preferences();
		for(Object key:  properties.keySet()) {			
			preferences.put((String)key, properties.get(key));
		}
		
		return preferences;
	}
	
	private static String getPreferenceFullFileName() {
		return DefaultPreferences.USER_HOME + File.separator + DefaultPreferences.PREFERENCE_FILE_NAME;
	}
	
}
