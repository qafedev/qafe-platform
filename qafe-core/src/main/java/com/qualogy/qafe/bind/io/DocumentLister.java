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
package com.qualogy.qafe.bind.io;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.w3c.dom.Document;

import com.qualogy.qafe.bind.io.document.DocumentLoader;
import com.qualogy.qafe.bind.orm.jibx.BindException;

public class DocumentLister {

	public final static Logger logger = Logger.getLogger(DocumentLister.class.getName());
	
	/**
	 * file extensions for this reader to parse
	 */
	public final static String FILE_EXTENSION = "xml";
	public final static String FILE_EXTENSION_ALTERNATIVE = "qaml";

	/**
	 * method filters from a filename, array of filenames or directory (including subdirectories)
	 * the parsable documents.
	 * @param fileNames
	 * @param docBuilder
	 * @return List of Document
	 */
	public static List<Document> filter(DocumentLoader documentLoader, String rootNode, List<Document> documents, List<URI> fileNames, boolean recursiveScan, boolean validating) {
		List<Document> localTmpDocuments = new ArrayList<Document>(); 
		for (URI uri : fileNames) {
			if(uri.getScheme().equals(FileLocation.SCHEME_FILE)){
				File file = new File(uri);
				if(!file.exists())
					logger.warning("No file present at ["+file.getAbsolutePath()+"]");
				documents = filter(documentLoader, rootNode, file, documents, recursiveScan, validating, 0);
			}else{
				InputStream stream = null;
				try {
					stream = uri.toURL().openStream();
					Document doc = documentLoader.loadDocument(stream, validating);
					localTmpDocuments.add(doc);
				} catch (MalformedURLException e) {
					throw new BindException(e);
				} catch (IOException e) {
					throw new BindException(e);
				} finally{
					if(stream!=null){
						try {
							stream.close();
						} catch (Exception e) {
							logger.log(Level.INFO, "couldn't close stream to resource " + uri.getPath(), e);
						}
					}
				}
			}
		}
		if(documents!=null)documents.addAll(localTmpDocuments);
		else documents = localTmpDocuments;
		
		return documents;
	}
	
	private static List<Document> filter(DocumentLoader documentLoader, String rootNode, File file, List<Document> documents, boolean recursiveScan, boolean validating, int level) {
		if (!file.isDirectory()) {
			if (file.exists() && (file.getName().endsWith(FILE_EXTENSION) || file.getName().endsWith(FILE_EXTENSION_ALTERNATIVE))){ 
				Document document = documentLoader.loadDocument(file, validating);
				if(meetsRequirements(document, rootNode)){
					logger.info("using file ["+file+"]");
					documents.add(document);
				}
			}
		} else if(level==0 || (level > 0 && recursiveScan)){
			String[] dirList = file.list();// TODO: call this and addAll
			level++;
			for (int j = 0; j < dirList.length; j++) {
				documents = filter(documentLoader, rootNode, new File(file.getAbsolutePath() + File.separator + dirList[j]), documents, recursiveScan, validating, level);
			}
		}
		return documents;
	}
	
	/**
	 * Method to see if a Document meets the Readers requirements
	 * @param expectedOutcome 
	 * @param file
	 * @return
	 */
	private static boolean meetsRequirements(Document document, String rootNode) {
		boolean valid = false;
		if(document!=null && document.getDocumentElement()!=null){
			String nodeName = document.getDocumentElement().getNodeName();
			valid = nodeName.equals(rootNode);
		}
		return valid;
	}
	
	
}
