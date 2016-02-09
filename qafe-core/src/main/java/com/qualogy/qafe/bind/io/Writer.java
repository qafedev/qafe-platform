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
package com.qualogy.qafe.bind.io;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.jibx.runtime.BindingDirectory;
import org.jibx.runtime.IBindingFactory;
import org.jibx.runtime.IMarshallingContext;
import org.jibx.runtime.JiBXException;
import org.xml.sax.SAXException;

import com.qualogy.qafe.bind.core.application.ApplicationStack;
import com.qualogy.qafe.bind.core.messages.Messages;
import com.qualogy.qafe.bind.domain.ApplicationMapping;
import com.qualogy.qafe.bind.io.document.DocumentLoader;
import com.qualogy.qafe.bind.orm.jibx.BindException;
import com.qualogy.qafe.bind.resource.query.QueryContainer;
import com.qualogy.qafe.bind.rules.FilterRules;

public class Writer {

	public final static String OUTPUT_ENCODING_TYPE = "UTF-8"; 
	
	private final static Logger log = Logger.getLogger(Writer.class.getName());
	
	/**
	 * method creates dir f not exists, than continues using the dir-less method
	 * @param domain
	 * @param dir
	 * @param fileName
	 * @throws IOException 
	 */
	public void write(Object domain, String dir, String fileName){
		write(domain,  dir, fileName, true);
	}
	
	
	/**
	 * validation over the output xml is on. if you require no validation,
	 * use write(Object, String, boolean) with the last arg set to false
	 * @param domain
	 * @param fileName
	 */
	public void write(Object domain, String fileName){
		write(domain, fileName, true);
	}
	
	/**
	 * Method to write given object to xml according a jibx bind file
	 * @param domain
	 * @param fileName
	 * @param validating
	 */
	public void write(Object domain, String fileName, boolean validating){
		File file = getFile(fileName);
		OutputStream out = null;
		try {
			out = new FileOutputStream(file);
			write(domain, out, validating);
		} catch (FileNotFoundException e) {
			throw new BindException(e);
		}finally{
			if(out!=null){
				try {
					out.close();
				} catch (IOException e) {
					throw new BindException(e);
				}
			}
		}
		
	}
	
	/**
	 * method writes object to the given outputstream, validating default true
	 * @param domain
	 * @param out
	 */
	public void write(Object domain, OutputStream out){
		write(domain, out, true);
	}
	
	/**
	 * method writes object to the given outputstream
	 * @param domain
	 * @param out
	 */
	public void write(Object domain, OutputStream out, boolean validating){
		String rootNode = WriterMapping.getRootNode(domain.getClass());
		String schemaLocation = WriterMapping.getSchemaLocation(domain.getClass());
		
		doWrite(domain, new BindOutputStream(out, rootNode, schemaLocation), validating);
	}
	
	/**
	 * method writes object to the given outputstream
	 * @param domain
	 * @param out
	 */
	private void doWrite(Object domain, BindOutputStream out, boolean validating){
		
		try{
			IBindingFactory bfact = BindingDirectory.getFactory(domain.getClass());
			IMarshallingContext mctx = bfact.createMarshallingContext();
			
			mctx.setIndent(2);
						
			mctx.startDocument(OUTPUT_ENCODING_TYPE, Boolean.TRUE, out);
			mctx.marshalDocument(domain);
			
			
		} catch (JiBXException e) {
			throw new BindException(e);
		} 
		if(validating){
			try {
				InputStream is = new ByteArrayInputStream(out.toByteArray());
				validate(is);
			} catch (SAXException e) {
				throw new BindException(e);
			} catch (IOException e) {
				throw new BindException(e);
			}
		}
	}
	
	private void validate(InputStream in) throws SAXException, IOException {
		new DocumentLoader().loadDocument(in, true);
	}
	
	/**
	 * method creates dir f not exists, than continues using the dir-less method
	 * @param domain
	 * @param dir
	 * @param fileName
	 * @throws IOException 
	 */
	public void write(Object domain, String dir, String fileName, boolean validating){
		String path = "";
		if(dir!=null){
			File directory = new File(dir);
			if(!directory.exists()){
				if(!directory.mkdir())
					throw new BindException("directory ["+directory+"] does not exist and cannot be created");
			}
			path = directory.getAbsolutePath() + File.separator;
		}
		write(domain,  path + fileName, validating);
	}
	

	private File getFile(String fileName) {
		File file = null; 
		try {
			file = new File(fileName);
			if(!file.exists()){
				log.info("creating new file for writting with name " + fileName);
				file.createNewFile();
			}
		} catch (IOException e) {
			throw new BindException(e);
		}
		return file;
	}
	
	//TODO: in configfile?
	public static class WriterMapping{
		private static Map<Class, String[]> MAPPING = new HashMap<Class, String[]>();
		static{
			MAPPING.put(ApplicationMapping.class, new String[]{"application-mapping", "http://qafe.com/schema http://www.qafe.com/schema/2.2/application-mapping.xsd"});
			MAPPING.put(ApplicationStack.class, new String[]{"applications", "http://qafe.com/schema http://www.qafe.com/schema/application-context.xsd"});
			MAPPING.put(QueryContainer.class, new String[]{QueryContainer.ROOT_ELEMENT_NAME, "http://qafe.com/schema http://www.qafe.com/schema/application-statements.xsd"});
			MAPPING.put(Messages.class, new String[]{"messages", "http://qafe.com/schema http://www.qafe.com/schema/application-messages.xsd"});
			MAPPING.put(FilterRules.class, new String[]{"filter-rules", "http://qafe.com/schema http://www.qafe.com/schema/filter-rule.xsd"});
			
		};
		
		private static String getRootNode(Class clazz){
			return get(clazz)[0];
		}
		
		private static String getSchemaLocation(Class clazz){
			return get(clazz)[1];			
		}
		
		private static String[] get(Class clazz){
			if(!MAPPING.containsKey(clazz)){
				throw new IllegalArgumentException("Unimplemented writer object ["+clazz+"]");
			}
			return MAPPING.get(clazz);
		}
	}
}
