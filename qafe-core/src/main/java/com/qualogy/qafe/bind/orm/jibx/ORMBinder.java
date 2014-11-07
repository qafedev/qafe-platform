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
package com.qualogy.qafe.bind.orm.jibx;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.IOUtils;
import org.jibx.runtime.BindingDirectory;
import org.jibx.runtime.IBindingFactory;
import org.jibx.runtime.IUnmarshallingContext;
import org.jibx.runtime.JiBXException;
import org.w3c.dom.Document;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSSerializer;

/*import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;*/

/*import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.Result;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.Transformer;*/

public class ORMBinder {
	
	public final static Logger logger = Logger.getLogger(ORMBinder.class.getName());
	
	/**
	 * 
	 */
	public final static String ENCODING_TYPE = "UTF-8";
	
	public static String docToString(Document doc) {
    	DOMImplementationLS domImplementation = (DOMImplementationLS) doc.getImplementation();
        LSSerializer lsSerializer = domImplementation.createLSSerializer();
        return lsSerializer.writeToString(doc);
    }
	
	/**
	 * closing the bytearrayinputstream has now effect
	 * @param doc
	 * @param root
	 * @return
	 */
	public static Object bind(Document doc, String root, Class expectedResultType, boolean readInDebugMode) {
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		
		// Refactor the commented code below, to make Google App Engine (GAE) compatible
		// The com.sun.org.apache.xml.internal.serialize.OutputFormat class is restricted
		/*OutputFormat format = new OutputFormat(doc);
		format.setIndenting(true);
		try {
			new XMLSerializer(bout, format).serialize(doc);
		} catch (IOException e) {
			throw new BindException(e);
		}*/
		/*try {
			Source source = new DOMSource(doc);
			Result result = new StreamResult(bout);
			TransformerFactory factory = TransformerFactory.newInstance();
			Transformer transformer = factory.newTransformer();
			transformer.transform(source, result);
		} catch (Exception e) {
			throw new BindException(e);
		}*/
		
		String docAsString = docToString(doc);
		byte[] docAsBytes = null;
		
		try {
			docAsBytes = docAsString.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		ByteArrayInputStream in = new ByteArrayInputStream(docAsBytes);
		//ByteArrayInputStream in = new ByteArrayInputStream(bout.toByteArray());
		
		if (readInDebugMode && logger.isLoggable(Level.FINE)) {
			try {
				logger.fine(docToString(in));
			} catch (IOException e1) {
				logger.log(Level.WARNING, "unable to print the merged document contents due to error", e1);
			}
		}

		Object obj = null;
		try {
			IBindingFactory bfact = BindingDirectory.getFactory(expectedResultType);
			IUnmarshallingContext uctx = bfact.createUnmarshallingContext();
			obj = uctx.unmarshalDocument(in, root, ENCODING_TYPE);
		} catch (JiBXException e) {
			String line = JIBXExceptionTranslator.getLine(e, in);
			BindException be = (line!=null)?new BindException(e.getMessage() + "\nLine: " + line, e):new BindException(e);
			throw be;
		} catch (ClassCastException e) {
			throw new BindException(e);
		} 
		return obj;
	}
	
	@SuppressWarnings("unchecked")
	private static String docToString(InputStream stream) throws IOException {
		StringBuffer buf = new StringBuffer();
		
		int i = 1;
		buf.append("\n");
		for (Iterator iter = IOUtils.lineIterator(stream, ENCODING_TYPE); iter.hasNext();i++) {
			String lnr = "" + i;
			for (int j = 0; lnr.length()< 4 && j < 4; j++) {
				lnr = " " + lnr;
			}
			buf.append(lnr + (String) iter.next());
		}
		return buf.toString();
	}
	
}
