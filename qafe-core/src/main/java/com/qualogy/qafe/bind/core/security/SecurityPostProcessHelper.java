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
package com.qualogy.qafe.bind.core.security;

import java.net.URI;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import com.qualogy.qafe.bind.core.application.ApplicationContext;
import com.qualogy.qafe.bind.core.application.ApplicationStack;
import com.qualogy.qafe.bind.io.FileLocation;
import com.qualogy.qafe.bind.io.Reader;
import com.qualogy.qafe.bind.orm.jibx.BindException;

public class SecurityPostProcessHelper {

	public static void postProcess(ApplicationStack stack){
		
		postProcessFileLocations(stack);
		
		//authentication
		AuthenticationPostProcessHelper.postProcess(stack);
		
		//authorization
		new AuthorizationRulesPostProcessor().postProcess(stack);
		
		//AuthenticationOverAuthorizationPostProcessorHelper.postProcess(stack);
	}

	private static void postProcessFileLocations(ApplicationStack stack) {
		if(stack.getSecuritySettings()!=null && CollectionUtils.isNotEmpty(stack.getSecuritySettings().getSettingsFileLocations())){
			SecuritySettings settingsFromFileLocations = readFromLocations(stack.getSecuritySettings().getSettingsFileLocations().iterator(), "context (top level)");
			stack.getSecuritySettings().merge(settingsFromFileLocations);
		}
		
		for (Iterator<ApplicationContext> iter = stack.getApplicationsIterator(); iter.hasNext();) {
			ApplicationContext context = (ApplicationContext) iter.next();
			postProcessFileLocations(context);
		}
	}
			
	private static void postProcessFileLocations(ApplicationContext context){
		if(context.getSecuritySettings()!=null && CollectionUtils.isNotEmpty(context.getSecuritySettings().getSettingsFileLocations())){
			SecuritySettings settingsFromFileLocations = readFromLocations(context.getSecuritySettings().getSettingsFileLocations().iterator(), context.getName());
			context.getSecuritySettings().merge(settingsFromFileLocations);
		}
		
	}

	private static SecuritySettings readFromLocations(Iterator<FileLocation> fileLocationsIterator, String name) {
		List<URI> paths = new ArrayList<URI>();
		while (fileLocationsIterator.hasNext()) {
			FileLocation location = (FileLocation) fileLocationsIterator.next();
			if(StringUtils.isEmpty(location.toString())){
				throw new BindException("Application " + name + ": filelocation cannot be left empty for security > settingsfile.");
			}
			URI path = location.toURI();
			paths.add(path);
		}
		SecuritySettings settingsFromFileLocations = (SecuritySettings)new Reader(SecuritySettings.class).read(paths);
		return settingsFromFileLocations;
	}
	
}
