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
package com.qualogy.qafe.bind.core.application.io;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang.StringUtils;

import com.qualogy.qafe.bind.core.messages.Messages;
import com.qualogy.qafe.bind.domain.ApplicationMapping;
import com.qualogy.qafe.bind.io.FileLocation;
import com.qualogy.qafe.bind.io.Reader;
import com.qualogy.qafe.bind.io.Writer;
import com.qualogy.qafe.bind.orm.jibx.BindException;
import com.qualogy.qafe.core.application.LoadFailedException;

public class ResourceMerger {

	public final static Logger logger = Logger.getLogger(ResourceMerger.class.getName());

	// CHECKSTYLE.OFF: CyclomaticComplexity
	@SuppressWarnings("unchecked")
	public static ApplicationMapping merge(Class mergeableClassName, List<ApplicationMapping> concreteMappings, List<FileLocation> fileLocations, String appName, String root, boolean autoScanRoot, boolean recursiveScan, boolean validating){

		logger.log(Level.FINE, "Start merging application mappings for application [{0}]", appName);

		ApplicationMapping applicationMapping = null;

		//boolean to see whether a concrete and/or location is/are set
		boolean noMappings = ((concreteMappings==null || concreteMappings.isEmpty()) && (fileLocations==null || fileLocations.isEmpty()));

		ByteArrayInputStream in = null;
		try{

			if(concreteMappings!=null){
				for (ApplicationMapping mapping : concreteMappings) {
					if(applicationMapping!=null)
						throw new LoadFailedException(appName, "loading two application-mappings through application-context without resource is not yet supported");
					if((fileLocations==null || fileLocations.isEmpty())){
						applicationMapping = mapping;
					}else{
						ByteArrayOutputStream out = null;
						try{
							out = new ByteArrayOutputStream();
							new Writer().write(mapping, out, false);
							byte[] bytes = out.toByteArray();
							in = new ByteArrayInputStream(bytes);
						}finally{
							if(out!=null)
								try {
									out.close();
								} catch (IOException e) {
									logger.log(Level.SEVERE, "Error occured while closing outputstream for application: " + appName, e);
								}
						}
					}
				}
			}

			if(noMappings || fileLocations!=null){
				List<URI> filePaths = new ArrayList<URI>();
				if(noMappings && autoScanRoot){
					logger.warning("No application-mapping found for context, using root ["+root+"] to scan for application-mapping (since auto-scan-root is set to true)");
					filePaths.add(new FileLocation(root, "").toURI());
				}else if(noMappings && !autoScanRoot){
					throw new LoadFailedException(appName, "No application-mapping found and auto-scan is false (default), cannot startup without application-mapping");
				}else if(fileLocations!=null){
					for (FileLocation location : fileLocations) {
						location.setRoot(root);
						URI path = location.toURI();
						if(path==null)
							throw new BindException("No file found at mapping file location ["+location.toString()+"]");
						filePaths.add(path);
					}
				}

				logger.info("Start reading application mappings for application ["+appName+"] from ["+filePaths+"]");

				try{
					applicationMapping = (ApplicationMapping)new Reader(ApplicationMapping.class).read(in, filePaths, root, recursiveScan);
				}catch(RuntimeException e){
					String message = e != null ? e.getMessage() : null;
					throw new LoadFailedException(appName, message, e);
				}

				logger.info("Finished reading application mappings for application ["+appName+"]");
			}
		}finally{
			try {
				if(in!=null)
					in.close();
			} catch (IOException e) {
				logger.log(Level.SEVERE, "Error occured while closing inputstream", e);
			}
		}

		return applicationMapping;
	}
	// CHECKSTYLE.ON: CyclomaticComplexity

	public static Messages merge(Messages messages, String appName, String root, boolean validating){
		if(messages!=null && messages.getBundleFileLocations()!=null && !messages.getBundleFileLocations().isEmpty()){
			for (FileLocation fLocation: messages.getBundleFileLocations()) {
				if(StringUtils.isEmpty(fLocation.toString())){
					throw new BindException("Application " + appName + ": filelocation cannot be left empty for bundlefile.");
				}
				fLocation.setRoot(root);
				URI path = fLocation.toURI();
				Messages otherMessages = (Messages)new Reader(Messages.class).read(path);
				messages.merge(otherMessages);
			}
		}
		return messages;
	}


}
