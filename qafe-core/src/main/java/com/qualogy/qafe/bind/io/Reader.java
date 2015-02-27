/**
 * Copyright 2008-2015 Qualogy Solutions B.V.
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
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.apache.commons.lang.ArrayUtils;
import org.w3c.dom.Document;

import com.qualogy.qafe.bind.core.application.ApplicationStack;
import com.qualogy.qafe.bind.core.messages.Messages;
import com.qualogy.qafe.bind.domain.ApplicationMapping;
import com.qualogy.qafe.bind.io.document.DocumentLoader;
import com.qualogy.qafe.bind.io.merge.DocumentMerger;
import com.qualogy.qafe.bind.orm.jibx.BindException;
import com.qualogy.qafe.bind.orm.jibx.ORMBinder;
import com.qualogy.qafe.bind.resource.query.QueryContainer;
import com.qualogy.qafe.bind.rules.FilterRules;

public class Reader {

	public final static Logger logger = Logger.getLogger(Reader.class.getName());

	private Document DEFAULTS_DOCUMENT = null; 
	
	private Class expectedResultType = null;
	
	private DocumentLoader documentLoader = null;
	
	private boolean validating = true;
	
	private boolean readInDebugMode = false;
	
	/**
	 * Default constructor. Will result in validating=true
	 * and readInDebugMode=false
	 */
	public Reader(Class expectedResultType){
		this(expectedResultType, true);
	}
	
	/**
	 * Constructor for setting validating. Use this constructor to
	 * turn off validating, otherwise use default constructor. A construct through
	 * this constructor will result in readInDebugMode=false.
	 */
	public Reader(Class expectedResultType, boolean validating){
		this(expectedResultType, validating, false);
	}
	
	/**
	 * @param validating
	 * @param readInDebugMode
	 */
	private Reader(Class expectedResultType, boolean validating, boolean readInDebugMode){
		documentLoader = new DocumentLoader();

		this.validating = validating;
		this.readInDebugMode = readInDebugMode;
		this.expectedResultType = expectedResultType;
		
		if(DEFAULTS_DOCUMENT==null && usesDefaults()){
			String defaultsFilePath = getDefaultsFilePath();
			DEFAULTS_DOCUMENT = documentLoader.loadDocument(getClass().getClassLoader().getResourceAsStream(defaultsFilePath), validating);
		}
	}
	
	/**
	 * pointing to a defaults file that will be merged with the files given by the caller
	 * of the read method. if method returns null, it's saying no defaults are
	 * specified. In this super implementation null is returned.
	 * 
	 * @return
	 */
	private String getDefaultsFilePath() {
		return Mapping.getDefaults(expectedResultType);
	}

	/**
	 * method filters the given fileName array to a usable filename list, reads
	 * the file to dom document, merges the documents, enhances the outcome of
	 * the merged documents (based upon the given enhancementsettings) and binds
	 * the overall outcome to a GenesisFramework object. Runtime BindException
	 * is thrown when any of the steps cannot be completed due to an exception
	 * or missing paramters.
	 * 
	 * @param paths
	 * @param settings
	 * @return GenesisFramework object binded from the given fileNames
	 */
	public Object read(List<URI> paths) {
		return read(null, paths);
	}

    public Object read(String fileName) {
        final File file;

        final File checkFile = new File(getClass().getClassLoader().getResource(fileName).getFile());
        if (checkFile.exists()) {
            file = checkFile;
        } else {
            file = new File(fileName);
            if (!file.exists()) {
                throw new BindException("Cannot read file from location [" + fileName + "]");
            }
        }

        return read(file.toURI());
    }

	public Object read(URI fileName) {
		List<URI> fileNames = new ArrayList<URI>();
		fileNames.add(fileName);
		
		return read(fileNames);
	}
	
	public Object read(InputStream in) {
		return read(in, null);
	}

	private boolean usesDefaults(){
		return getDefaultsFilePath() != null && getDefaultsFilePath().length() > 0;
	}
	
	/**
	 * not scanning subdirectories
	 * @param in
	 * @param fileNames
	 * @return
	 */
	public Object read(InputStream in, List<URI> fileNames) {
		return read(in, fileNames, null);
	}
	
	public Object read(InputStream in, List<URI> fileNames, String root) {	
		return read(in, fileNames, root, false);
	}
	public Object read(InputStream in, List<URI> fileNames, String root, boolean recursiveScan) {
		
		if ((fileNames == null || fileNames.size() == 0) && in == null)
			throw new IllegalArgumentException("Both fileNames array parameter and inputstream cannot be null or empty for reading the files");
		
		//1. filter from files list
		List<Document> documents = new ArrayList<Document>();
		if (fileNames != null && fileNames.size() > 0) {
			documents = DocumentLister.filter(documentLoader, Mapping.getRootNode(expectedResultType), documents, fileNames, recursiveScan, validating);
			if (documents.isEmpty())
				throw new NoFilesFoundException("No files found with content starting with element "+Mapping.getRootNode(expectedResultType)+" at location(s) [" + ArrayUtils.toString(fileNames) + "]\nPossible reasons: No files present at location given, or files present do not meet the requirements (starting with root element ["+Mapping.getRootNode(expectedResultType)+"] or having file extension [xml/qaml])");
			
			logger.info("The following files are found for processing [" + documents + "]");
		}

		//2. add documents from stream
		documents = addDocumentsFromStream(documents, in);
		if (documents == null || documents.isEmpty())
			throw new NullPointerException("docs is null or empty");

		//3. add defaults
		documents = addDefaults(documents);
		
		//4. merge documents
		Document doc = DocumentMerger.merge(expectedResultType, documents);

		//6. nasty hack to get the root
		if (root == null && fileNames != null && fileNames.size() > 0)
			root = getApplicationRoot(fileNames.get(0));

		//7. bind and return
		return ORMBinder.bind(doc, root, expectedResultType, readInDebugMode);
	}

	private List<Document> addDefaults(List<Document> documents) {
		if(DEFAULTS_DOCUMENT != null){
			documents.add(0, DEFAULTS_DOCUMENT);
		}
		return documents;
	}

	/**
	 * method to add documents from stream to documents list
	 * @param documents
	 * @param in
	 * @param docBuilder
	 * @return
	 */
	private List<Document> addDocumentsFromStream(List<Document> documents, InputStream in) {
		if (in != null)
			documents.add(documentLoader.loadDocument(in, validating));
		return documents;
	}

	/**
	 * @deprecated: This method is a nasty hack to get the root path to pass it
	 *              to the unmarshallingcontext. The nastiest of all that this
	 *              method is called with the first string path passed to the
	 *              reader assuming this file is in the root.
	 * @param string
	 * @return
	 */
	private String getApplicationRoot(URI fileName) {
		String path = null;
		if(fileName.getScheme().equals(FileLocation.SCHEME_FILE)){
			File file = new File(fileName);
	
			if (file.exists()) {
				path = file.getAbsolutePath();
				String name = file.getName();
				path = path.substring(0, path.length() - name.length());
				path = path.replace('\\', File.separatorChar);
			}
		}else{
			path = fileName.toString();
		}
		return path;
	}

	//TODO: in configfile?
	public static class Mapping{
		private static Map<Class, String[]> MAPPING = new HashMap<Class, String[]>();
		static{
			MAPPING.put(ApplicationMapping.class, new String[]{"application-mapping", "qafe-config-app.xml"});
			MAPPING.put(ApplicationStack.class, new String[]{"applications"});
			MAPPING.put(QueryContainer.class, new String[]{QueryContainer.ROOT_ELEMENT_NAME});
			MAPPING.put(Messages.class, new String[]{"messages"});
            MAPPING.put(FilterRules.class, new String[]{"filter-rules"});
		};
		
		private static String getRootNode(Class clazz){
			return get(clazz)[0];
		}
		
		private static String getDefaults(Class clazz){
			String[] args = get(clazz);
			return args.length>1 ? get(clazz)[1] : null;
		}
		
		private static String[] get(Class clazz){
			if(!MAPPING.containsKey(clazz)){
				throw new IllegalArgumentException("Unimplemented reader object ["+clazz+"]");
			}
			return MAPPING.get(clazz);
		}
	}

}
