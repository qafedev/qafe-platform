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
package test.com.qualogy.qafe.bind.domain.io;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.custommonkey.xmlunit.*;

import org.w3c.dom.Document;

import com.qualogy.qafe.bind.io.document.DocumentLoader;
import com.qualogy.qafe.bind.io.merge.DocumentMerger;
import com.qualogy.qafe.bind.orm.jibx.ORMBinder;
import com.qualogy.qafe.bind.domain.ApplicationMapping;

public class ApplicationMappingXMLMergerTest extends XMLTestCase {
	
	private DocumentLoader documentLoader = new DocumentLoader();
    
    public void testForEquality() throws Exception {
        String myControlXML = "<msg><uuid>0x00435A8C</uuid></msg>";
        String myTestXML = "<msg><localId>2376</localId></msg>";
        //assertXMLEqual("comparing test xml to control xml", myControlXML, myTestXML);

        assertXMLNotEqual("test xml not similar to control xml", myControlXML, myTestXML);
    }
    
    public void testIdentical() throws Exception {
        String myControlXML = "<struct><int>3</int><boolean>false</boolean></struct>";
        String myTestXML = "<struct><boolean>false</boolean><int>3</int></struct>";
        Diff myDiff = new Diff(myControlXML, myTestXML);
        assertTrue("pieces of XML are similar " + myDiff, myDiff.similar());
        //assertTrue("but are they identical? " + myDiff, myDiff.identical());
    }
    
	public void testMerge() throws Exception {
		String base = System.getProperty("user.dir");
		String fs = File.separator;
		String xmlFile1 = base + fs + "xmlFile1.xml";
		String xmlFile2 = base + fs + "xmlFile2.xml";
		String xmlFile3 = base + fs + "xmlFile3.xml";
		
		System.out.println("## File 1 ##" + xmlFile1);
		System.out.println("## File 2 ##" + xmlFile2);
		System.out.println("## File 3 ##" + xmlFile3);
		
		String root = "test";
		Class expectedResultType = ApplicationMapping.class;
		boolean readInDebugMode = false;
		
		boolean validationMode = true;
		
		Document doc1 = documentLoader.loadDocument(new File(xmlFile1), validationMode);
		String doc1Str = ORMBinder.docToString(doc1);
		System.out.println("#doc1#\n" + doc1Str);
		
		Document doc2 = documentLoader.loadDocument(new File(xmlFile2), validationMode);
		String doc2Str = ORMBinder.docToString(doc2);
		System.out.println("#doc2#\n" + doc2Str);
		
		Document doc3 = documentLoader.loadDocument(new File(xmlFile3), validationMode);
		String fileBasedMergedXml = ORMBinder.docToString(doc3);
		//fileBasedMergedXml = StringUtils.strip(fileBasedMergedXml, "\t\n\r");
		System.out.println("#fileBasedMergedXml#\n" + fileBasedMergedXml);
		
		List<Document> documents = new ArrayList<Document>();
		documents.add(doc1);
		documents.add(doc2);
		
		Document domBasedMergedDoc = DocumentMerger.merge(expectedResultType, documents);
		String domBasedMergedXml = ORMBinder.docToString(domBasedMergedDoc);
		//domBasedMergedXml = StringUtils.strip(domBasedMergedXml, "\t\n\r");
		System.out.println("#domBasedMergedDoc#\n" + domBasedMergedXml);
		//Diff diff = new Diff(fileBasedMergedXml, domBasedMergedXml);
        //assertTrue("pieces of XML are similar " + diff, diff.similar());
        
		Object obj = ORMBinder.bind(domBasedMergedDoc, root, expectedResultType, readInDebugMode);
		if (obj instanceof ApplicationMapping) {
			assertTrue("Correct binding", true);
		} else {
			assertTrue("Wrong binding", false);
		}
	}
	
	public void testValidation() throws Exception {
		new DocumentLoader().loadDocument(getClass().getClassLoader().getResourceAsStream("application-mapping-defaults.xml"), true);
	}
}
