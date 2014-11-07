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
package com.qualogy.qafe.bind.io.merge;

import java.util.List;
import org.w3c.dom.Document;

/*import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.DOMException;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Attr;
import org.w3c.dom.NodeList;
import org.w3c.dom.NamedNodeMap;

import com.qualogy.qafe.bind.orm.jibx.BindException;*/

public class DocumentMerger {
	public static Document merge(Class expectedResultType, List<Document> documents) {
		Document doc = null;
		
		if (documents.size() > 1) {
			XMLMerger merger = XMLMergerFactory.create(expectedResultType);
			if (merger == null)
				throw new IllegalArgumentException("unable to merge your files since merger not present for this type of file");
			
			Document[] docs = (Document[]) documents.toArray(new Document[documents.size()]);
			
			doc = merger.merge(docs);
		} else if (documents.size() == 1) {
			doc = (Document)documents.get(0);
			//doc = getDocumentWitheNameSpace(doc);
			//doc.getDocumentElement().setAttribute(com.qualogy.qafe.bind.domain.io.ApplicationMappingXMLMerger.ATTRIBUTE_KEY_XMLNS, com.qualogy.qafe.bind.domain.io.ApplicationMappingXMLMerger.NAME_SPACE);
		}
		return doc;
	}
	
	/*public static final String NAME_SPACE_URI = "http://qafe.com/schema";
	public static final String ATTRIBUTE_KEY_XMLNS = "xmlns";
	public static final String SCHEMA_LANGUAGE_ATTRIBUTE = "http://java.sun.com/xml/jaxp/properties/schemaLanguage";
	public static final String XSD_SCHEMA_LANGUAGE = "http://www.w3.org/2001/XMLSchema";
	
	public static Document getDocumentWitheNameSpace(Document doc) {
		Document document = null;
		Node root = null;
		String rootNodeName = null;
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setNamespaceAware(true);
			document = factory.newDocumentBuilder().newDocument();
			rootNodeName = doc.getDocumentElement().getNodeName();
			Element el = document.createElement(rootNodeName);
			NamedNodeMap attrs = doc.getDocumentElement().getAttributes();
			for (int i = 0; i < attrs.getLength(); i++) {
				Attr attr = (Attr) attrs.item(i);
				el.setAttribute(attr.getName(), attr.getValue());
			}
			root = document.appendChild(el);
		} catch (DOMException dome) {
			dome.printStackTrace();
		} catch (ParserConfigurationException e) {
			throw new BindException(e);
		} catch (FactoryConfigurationError e) {
			throw new BindException(e);
		}
		NodeList children = doc.getChildNodes();
		if (children.getLength() > 0) {
			Node rootNode = children.item(0);
			NodeList rootChildren = rootNode.getChildNodes();
			for (int i = 0; i < rootChildren.getLength(); i++) {
				Node child = rootChildren.item(i).cloneNode(true);
				document.adoptNode(child);
				root.appendChild(child);
			}
		}
		document.renameNode(document.getDocumentElement(), NAME_SPACE_URI, document.getDocumentElement().getTagName());
		return document;
	}*/
}
