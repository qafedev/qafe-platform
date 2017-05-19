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
package com.qualogy.qafe.web;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang.StringUtils;

import com.qualogy.qafe.bind.core.application.Configuration;
import com.qualogy.qafe.core.application.ApplicationCluster;
import com.qualogy.qafe.core.datastore.ApplicationLocalStore;
import com.qualogy.qafe.core.datastore.DataStore;
import com.qualogy.qafe.core.id.UniqueIdentifier;


public class UploadService {

	public static final String APP_UUID = "APPUUID";
	public static final String APP_WINDOWID = "WINDOWID";

	public static final String ACTION_REMOVE = "REMOVE";
	public static final String ACTION_VIEW = "VIEW";
	
	public static final String FILE_MIME_TYPE = "mime-type";
	public static final String FILE_NAME = "filename";
	public static final String FILE_CONTENT = "filecontent";
	
	protected static final String UPLOAD_COMPLETE = "UUID";
	protected static final String UPLOAD_ERROR = "ERROR";
	
	protected static final String REMOVE_COMPLETE = "OK";
	protected static final String REMOVE_ERROR = UPLOAD_ERROR;

	protected static final String DEFAULT_MIME_TYPE = "application/octet-stream";
	
	
	
	public String uploadFile(HttpServletRequest request) {
		ServletFileUpload upload = new ServletFileUpload();
        String errorMessage = "";
        byte[] filecontent = null;
        String appUUID = null;
        String windowId = null;
        String filename = null;
   		String mimeType = null;
   		InputStream inputStream = null;
   		ByteArrayOutputStream outputStream = null;
        try {
            FileItemIterator fileItemIterator = upload.getItemIterator(request);
            while (fileItemIterator.hasNext()) {
				FileItemStream item = fileItemIterator.next();
				inputStream = item.openStream();
				// Read the file into a byte array.
				outputStream = new ByteArrayOutputStream();
				byte[] buffer = new byte[8192];
				int len = 0;
				while (-1 != (len = inputStream.read(buffer))) {
					outputStream.write(buffer, 0, len);
				}
				if (filecontent == null) {
					filecontent = outputStream.toByteArray();
					filename = item.getName();
					mimeType = item.getContentType();
				}

				if (item.getFieldName().indexOf(APP_UUID) > -1) { 
					appUUID = item.getFieldName().substring(item.getFieldName().indexOf(APP_UUID)+ APP_UUID.length() + 1);
				}
				if (item.getFieldName().indexOf(APP_WINDOWID) > -1) {
					windowId = item.getFieldName().substring(item.getFieldName().indexOf(APP_WINDOWID)+ APP_WINDOWID.length() + 1);
				}
            }
            
            if ((appUUID != null) && (windowId != null)) {
            	if (filecontent != null) {
	           		int maxFileSize = 0;            		
	           		if (ApplicationCluster.getInstance().getConfigurationItem(Configuration.MAX_UPLOAD_FILESIZE) != null) {
	           			String maxUploadFileSzie = ApplicationCluster.getInstance().getConfigurationItem(Configuration.MAX_UPLOAD_FILESIZE);
	           			if (StringUtils.isNumeric(maxUploadFileSzie)) {
	           				maxFileSize = Integer.parseInt(maxUploadFileSzie);
	           			}
	           		}
	           		
	           		if ((maxFileSize == 0) || (filecontent.length <= maxFileSize)) {
	           			Map<String, Object> fileData = new HashMap<String, Object>();
	               		fileData.put(FILE_MIME_TYPE, mimeType);
	               		fileData.put(FILE_NAME, filename);
	               		fileData.put(FILE_CONTENT, filecontent);
	
	               		String uploadUUID = DataStore.KEY_LOOKUP_DATA + UniqueIdentifier.nextSeed().toString();
	               		appUUID = concat(appUUID, windowId);

	               		ApplicationLocalStore.getInstance().store(appUUID, uploadUUID, fileData);
	               		
	               		return filename + "#" + UPLOAD_COMPLETE + "=" + uploadUUID;
	           		} else {
	           			errorMessage = "The maxmimum filesize in bytes is " + maxFileSize;
	           		}            	
            	}
            } else {
            	errorMessage = "Application UUID not specified";
            }
            inputStream.close();
            outputStream.close();
		} catch (Exception e) {
			errorMessage = e.getMessage();
		} 
		
		return UPLOAD_ERROR + "=" + "File can not be uploaded: " + errorMessage;
	}
	
	
	public Map getUploadedFile(String appUUID, String windowId, String uploadUUID) {
		if ((appUUID != null) && (windowId != null)) {
			if ((uploadUUID != null) && (uploadUUID.indexOf(DataStore.KEY_LOOKUP_DATA) > -1)) {
				appUUID = concat(appUUID, windowId);
				if (ApplicationLocalStore.getInstance().contains(appUUID, uploadUUID)) {
					Object uploadedFile = ApplicationLocalStore.getInstance().retrieve(appUUID, uploadUUID);
					if ((uploadedFile != null) && (uploadedFile instanceof Map)) {
						return (Map) uploadedFile;
					}
				}
			}
		}
		return null;
	}
	
	public String removeUploadedFile(String appUUID, String windowId, String uploadUUID) {
		String errorMessage = "File can not be found";
		if ((appUUID != null) && (windowId != null)) {
			if ((uploadUUID != null) && (uploadUUID.indexOf(DataStore.KEY_LOOKUP_DATA) > -1)) {
				appUUID = concat(appUUID, windowId);
				if (ApplicationLocalStore.getInstance().contains(appUUID, uploadUUID)) {
					ApplicationLocalStore.getInstance().delete(appUUID, uploadUUID);
					return REMOVE_COMPLETE;
				}
			}
		}
		return REMOVE_ERROR + "=" + "Uploaded file can not be removed: " + errorMessage;	
	}
	
	private String concat(String str1, String str2) {
		return str1 + "." + str2;
	}
	
}
