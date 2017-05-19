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
package com.qualogy.qafe.bind.util;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

import org.jibx.runtime.ITrackSource;
import org.jibx.runtime.impl.ITrackSourceImpl;

/**
 * http://jibx.sourceforge.net/binding/binding-element.html
 * - track-source: 
 * When this optional attribute is present with value "true", 
 * the binding compiler adds code to each bound object class to implement the org.jibx.runtime.ITrackSource interface 
 * and store source position information when instance objects are unmarshalled. 
 * This interface lets you retrieve information about the source document and specific line and column location of the document component associated with that object. 
 * The default value is "false".
 */
public class TrackSource {

	public static String getSimpleDocumentName(Object element) {
		String documentName = getDocumentName(element);
		if (documentName != null) {
			File file = new File(documentName);
			documentName = file.getName();
		}
		return documentName;
	}
	
	public static String getDocumentName(Object element) {
		if (element instanceof ITrackSource) {
			ITrackSource trackSource = (ITrackSource)element;
			return trackSource.jibx_getDocumentName();
		}
		return null;
	}
	
	public static void setDocumentName(Object element, String uri) {
		if (element instanceof ITrackSourceImpl) {
			try {
				URI documentURI = new URI(uri);
				ITrackSourceImpl trackSource = (ITrackSourceImpl)element;
				int lineNumber = trackSource.jibx_getLineNumber();
				int columnNumber = trackSource.jibx_getColumnNumber();
				trackSource.jibx_setSource(documentURI.getPath(), lineNumber, columnNumber);
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
