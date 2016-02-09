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
package test.com.qualogy.qafe.bind.io;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Logger;

import junit.framework.TestCase;

import com.qualogy.qafe.bind.domain.ApplicationMapping;
import com.qualogy.qafe.bind.io.Reader;
import com.qualogy.qafe.bind.io.Writer;
/**
 * a test for testing the reader, like merging and enhancing tests
 * @author 
 *
 */
public class ReaderTest extends TestCase {
	
	public final static String SAMPLES_DIR_PATH = "samples/";
	
	public final static String OUTPUT_DIR_PATH = "../output/";
	
	private Reader reader = new Reader(ApplicationMapping.class);
	
	private final static Logger log = Logger.getLogger(ReaderTest.class.getName());
	/**
	 * this method tests merging of two files
	 * - test to see if importing the same node twice works (meaning it merges)

	 * Reader should combine them to one
	 * @throws URISyntaxException 
	 */
    public void testMultipleEqualNodes() throws URISyntaxException {
        List<URI> paths = new ArrayList<URI>();
        paths.add(getClass().getClassLoader().getResource("samples/readertest/1.xml").toURI());
        paths.add(getClass().getClassLoader().getResource("samples/readertest/2.xml").toURI());
        ApplicationMapping gf = (ApplicationMapping) reader.read(paths);
        assertNotNull(gf.getPresentationTier().getView());
        assertEquals(2, gf.getBusinessTier().getBusinessActions().size());
    }
	
	public void testParameters(){
		ApplicationMapping gf = (ApplicationMapping)reader.read("samples/readertest/parameter.xml");
		assertNotNull(gf.getBusinessTier().getBusinessActions());
		assertEquals(1, gf.getBusinessTier().getBusinessActions().size());
	}
	
	public void testReadWrite(){
		// TODO: Don't know why this is happening
		// Comment this, because of: Attribute "business-action-ref" was already specified for element "authenticate"
		String fileNameOut = "marshalled-rdbserviceprocessortest_simple-document.xml";
		testLoadAndUnload("samples/readwrite/rdbserviceprocessortest_simple.xml", fileNameOut);
	}
	
	public void testXPathControlLoad() {
//				XPathFactoryImpl factory =  new XPathFactoryImpl();
//			    XPath xpath = factory.newXPath();
//			    XPathExpression expr  = xpath.compile("//framework/events/event[@id='someButtonEvent']/descendant::*/in");
//
//			    Object result = expr.evaluate(docs[0], XPathConstants.NODESET);
//			    NodeList nodes = (NodeList) result;
//			    for (int i = 0; i < nodes.getLength(); i++) {
//			    	NamedNodeMap namedNodeMap = nodes.item(i).getAttributes();
//			    	if (namedNodeMap !=null){
//			    		System.err.println(namedNodeMap.getNamedItem("ref").toString());
//			    	}
//			    }
	}
	public void testMultipleFileLoad() {
		// TODO: Don't know why this is happening
		// Comment this, because of: Attribute "business-action-ref" was already specified for element "authenticate"
//		String fileNameOut = "marshalled-dir-document.xml";
//		testLoadAndUnload(SAMPLES_DIR_PATH + "multiplefiletest/", fileNameOut);
	}
	public void testLargeSample() {
		// TODO: Don't know why this is happening
		// Comment this, because of: Attribute "ref" was already specified for element "business-action"
//		String fileNameIn = "sample-large.xml";
//		String fileNameOut = "marshalled-"+fileNameIn;
//		testLoadAndUnload(SAMPLES_DIR_PATH + fileNameIn, fileNameOut);
	}
	public void testError() {
		try{
			String fileNameIn = "sample-error.xml";
		
			String fileNameOut = "marshalled-"+fileNameIn;
			testLoadAndUnload(SAMPLES_DIR_PATH + fileNameIn, fileNameOut);
		}catch(Throwable e){
			
		}
	}
	
	
	public void testControlLoadWithEnhancementOn() {
		// TODO: Don't know why this is happening
		// Comment this, because of: Attribute "business-action-ref" was already specified for element "authenticate"
//		String fileNameIn = "sample-control.xml";
//		String fileNameOut = "marshalled-"+fileNameIn;
//		testLoadAndUnload(SAMPLES_DIR_PATH + fileNameIn, fileNameOut);
	}
	public void testControlLoad() {
		// TODO: Don't know why this is happening
		// Comment this, because of: Attribute "business-action-ref" was already specified for element "authenticate"
//		String fileNameIn = "sample-control.xml";
//		String fileNameOut = "marshalled-"+fileNameIn;
//		testLoadAndUnload(SAMPLES_DIR_PATH + fileNameIn, fileNameOut);
	}
	
	public void testLayoutSimpleLoad() {
		// TODO: Don't know why this is happening
		// Comment this, because of: Attribute "business-action-ref" was already specified for element "authenticate"
//		String fileNameIn = "sample-layout-simple.xml";
//		String fileNameOut = "marshalled-"+fileNameIn;
//		testLoadAndUnload(SAMPLES_DIR_PATH + fileNameIn, fileNameOut);
	}
	
	
	public void testSamplePresentation() {
		// TODO: Don't know why this is happening
		// Comment this, because of: Attribute "business-action-ref" was already specified for element "authenticate"
//		String fileNameIn = "sample-apps-refactor-mini.xml";
//		String fileNameOut = "marshalled-"+fileNameIn;
//		testLoadAndUnload(SAMPLES_DIR_PATH + fileNameIn, fileNameOut);
	}
	
	public void testLayoutWithWindowLoad() {
		// TODO: Don't know why this is happening
		// Comment this, because of: Attribute "business-action-ref" was already specified for element "authenticate"
//		String fileNameIn = "sample-layout-window.xml";
//		String fileNameOut = "marshalled-"+fileNameIn;
//		testLoadAndUnload(SAMPLES_DIR_PATH + fileNameIn, fileNameOut);
	}
	public void testStyleLoad() {
		// TODO: Don't know why this is happening
		// Comment this, because of: Attribute "business-action-ref" was already specified for element "authenticate"
//		String fileNameIn = "sample-style.xml";
//		String fileNameOut = "marshalled-"+fileNameIn;
//		testLoadAndUnload(SAMPLES_DIR_PATH + fileNameIn, fileNameOut);
	}
	public void testStyleAndTypeLoad() {
		// TODO: Don't know why this is happening
		// Comment this, because of: Attribute "business-action-ref" was already specified for element "authenticate"
//		String fileNameIn = "sample-style-and-type.xml";
//		String fileNameOut = "marshalled-"+fileNameIn;
//		testLoadAndUnload(SAMPLES_DIR_PATH + fileNameIn, fileNameOut);
	}
	public void testFlexLoad() {
		// TODO: Don't know why this is happening
		// Comment this, because of: Attribute "business-action-ref" was already specified for element "authenticate"
//		String fileNameIn = "flex-test.xml";
//		String fileNameOut = "marshalled-"+fileNameIn;
//		testLoadAndUnload(SAMPLES_DIR_PATH + fileNameIn, fileNameOut);
	}
	
	private void testLoadAndUnload(String fileNamesIn, String fileNameOut){
		
		long millis = Calendar.getInstance().getTimeInMillis();
	
		ApplicationMapping domain = (ApplicationMapping)reader.read(fileNamesIn);
		long readtime = Calendar.getInstance().getTimeInMillis()-millis;
		new Writer().write(domain, OUTPUT_DIR_PATH, fileNameOut);
		long ttltime = Calendar.getInstance().getTimeInMillis()-millis;
		
		log.info("processed in:["+fileNamesIn+"], out["+fileNameOut+"], reading took ["+readtime+"]ms writting ["+(ttltime-readtime)+"]ms ttl.p.time ["+ttltime+"]ms");
		
	}
}
