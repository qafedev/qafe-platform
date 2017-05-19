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
package com.qualogy.qafe.bind.io.document;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.SchemaFactory;

import org.w3c.dom.Document;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import com.qualogy.qafe.bind.orm.jibx.BindException;

public class DocumentLoader {

	/**
	 * JAXP attribute used to configure the schema language for validation.
	 */
	//private static final String SCHEMA_LANGUAGE_ATTRIBUTE = "http://java.sun.com/xml/jaxp/properties/schemaLanguage";

	/**
	 * JAXP attribute value indicating the XSD schema language.
	 */
	//private static final String XSD_SCHEMA_LANGUAGE = "http://www.w3.org/2001/XMLSchema";
	
	public static final Logger logger = Logger.getLogger(DocumentLoader.class.getName());
		
	private ErrorHandler errorHandler = new SimpleSaxErrorHandler(logger);
	
	private static final Map<String, DocumentBuilder> schemaValidators = new HashMap<String, DocumentBuilder>();
	
	private Document loadDocument(InputSource xmlInputSource, boolean validationMode) {
		Document doc = null;
		try {
			
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			
			logger.log(Level.FINE, "Using JAXP provider [{0}]", factory.getClass().getName());

			// In case of validationMode, xmlInputSource will be copied to newXMLInputSource for the second parsing,
			// because after the first parsing the xmlInputSource is at the end, 
			// so when use it again the 'Premature end of file.' exception will raise
			InputSource newXMLInputSource = null;
			if (validationMode) {
				if (xmlInputSource.getSystemId() != null) {
					newXMLInputSource = new InputSource(xmlInputSource.getSystemId());
				} else if (xmlInputSource.getByteStream() != null) {
					BufferedInputStream bufferedXMLInputStream = new BufferedInputStream(xmlInputSource.getByteStream());
					ByteArrayOutputStream xmlOutputStream1 = new ByteArrayOutputStream();
					ByteArrayOutputStream xmlOutputStream2 = new ByteArrayOutputStream();
					try {
						while (true) {
							int data = bufferedXMLInputStream.read();
						    if (data == -1) {
						    	break;
						    }					        
						    xmlOutputStream1.write(data);
						    xmlOutputStream2.write(data);
						}
						xmlOutputStream1.flush();
						xmlOutputStream2.flush();
						xmlInputSource = new InputSource((InputStream)new ByteArrayInputStream(xmlOutputStream1.toByteArray()));
						newXMLInputSource = new InputSource((InputStream)new ByteArrayInputStream(xmlOutputStream2.toByteArray()));	
					} finally {
						xmlOutputStream1.close();
						xmlOutputStream2.close();	
					}
				} 
			}
			
			DocumentBuilder builder = factory.newDocumentBuilder();
			builder.setErrorHandler(errorHandler);
			doc = builder.parse(xmlInputSource);
			
			if (validationMode) {
				String xsdName = getSchemaResourceName(doc);
				builder = schemaValidators.get(xsdName);
				if (builder == null) {
					SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
					InputStream xsdInputStream = getContextClassLoader().getResourceAsStream(xsdName);
					LocalXSDResolver localXSDResolver = new LocalXSDResolver();
					xsdInputStream = localXSDResolver.resolveToLocalXSDs(xsdInputStream);
					Source xsdSource = new StreamSource(xsdInputStream);
					factory.setSchema(schemaFactory.newSchema(xsdSource));
		            factory.setNamespaceAware(true);
		            builder = factory.newDocumentBuilder();
		            builder.setErrorHandler(errorHandler);
		            schemaValidators.put(xsdName, builder);
				}
	            builder.parse(newXMLInputSource);
			}
		} catch (ParserConfigurationException e) {
			throw new BindException(e);
		} catch (SAXParseException e) {
			StringBuffer message = new StringBuffer();
			String fileName = e.getSystemId();
			if (fileName != null) {
				fileName = " '" + fileName.substring(fileName.lastIndexOf("/") + 1) + "' ";
			}
			if (fileName == null) {
				fileName = " ";
			}
			message.append("XML document" + fileName + "is invalid: ");
			message.append("[line: " + e.getLineNumber() + ", column: " + e.getColumnNumber() + "]");
			message.append(" - " + e.getMessage());
			throw new BindException(message.toString());
		} catch (SAXException e) {
			throw new BindException("XML document is invalid", e);
		} catch (IOException e) {
			throw new BindException("Parser configuration exception parsing XML", e);
		}
		return doc;
	}
	
	public Document loadDocument(InputStream is, boolean validationMode){
		return loadDocument(new InputSource(is), validationMode);
	}
	
	public Document loadDocument(File file, boolean validationMode){
		if (file == null) {
            throw new IllegalArgumentException("File cannot be null");
        }
        String uri = "file:" + file.getAbsolutePath();
	    if (File.separatorChar == '\\') {
	        uri = uri.replace('\\', '/');
	    }
	    uri = file.toURI().toString();
	    return loadDocument(new InputSource(uri), validationMode);
	}
	
	public static ClassLoader getContextClassLoader() {
		ClassLoader cl = null;
		try {
			cl = Thread.currentThread().getContextClassLoader();
		} catch (Throwable ex) {
			// Cannot access thread context ClassLoader - falling back to system class loader...
		}
		return cl;
	}
	
	public static String getSchemaResourceName(Document document) {
		String xsi_schemaLocation = document.getDocumentElement().getAttribute("xsi:schemaLocation");
		String resouceName = xsi_schemaLocation.substring(xsi_schemaLocation.lastIndexOf("/") + 1);
		return resouceName;
	}
}
