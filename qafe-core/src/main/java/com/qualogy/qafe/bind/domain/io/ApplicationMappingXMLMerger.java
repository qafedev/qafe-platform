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
package com.qualogy.qafe.bind.domain.io;

import java.io.File;
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.qualogy.qafe.bind.io.merge.NodeContainer;
import com.qualogy.qafe.bind.io.merge.XMLMerger;
import com.qualogy.qafe.bind.orm.jibx.BindException;

public class ApplicationMappingXMLMerger implements XMLMerger{
	
	public static final String ROOT_NODE = "application-mapping";
	public static final String NAME_SPACE = "http://qafe.com/schema";
	
	private class ApplicationMappingNodeContainer extends NodeContainer{
		public String[] getChildNodeNames() {
			return new String[]{"presentation-tier", "business-tier", "integration-tier", "resource-tier"};
		}
	}

	public Document merge(Document[] docs){
		if(docs==null)
			throw new IllegalArgumentException("docs is null");
		
		Document document = null; 
	
		Node root = null;
		try {
			document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
			Element el = document.createElement(ROOT_NODE);
			NamedNodeMap attrs = docs[0].getDocumentElement().getAttributes();
			for (int i = 0; i < attrs.getLength(); i++) {
				Attr attr = (Attr) attrs.item(i);
				el.setAttribute(attr.getName(), attr.getValue());
			}
			root = document.appendChild(el);
		} catch (ParserConfigurationException e) {
			throw new BindException(e);
		} catch (FactoryConfigurationError e) {
			throw new BindException(e);
		}
		
		ApplicationMappingNodeContainer container = new ApplicationMappingNodeContainer();
		for (int i = 0; i < docs.length; i++) {
			container = gather(document, docs[i].getElementsByTagName(ROOT_NODE), container);
		}
		append(document, root, container);
		return document;
	}
	
	/**
	 * method gathers all document nodes in a given xmlcontainer
	 * @param document
	 * @param nodes
	 * @param container
	 * @return
	 */
	private static ApplicationMappingNodeContainer gather(Document document, NodeList nodes, ApplicationMappingNodeContainer container){
		
		for (int j = 0; j < nodes.getLength(); j++) {
			NodeList children = nodes.item(j).getChildNodes();
			for (int k = 0; k < children.getLength(); k++) {
				Node child = children.item(k).cloneNode(true);
				document.adoptNode(child);
				if(child.getChildNodes().getLength()>0)
					container.appendChild(document, child);
			}
		}
		return container;
	}
	
	public static void main(String[] args) {
		String base = System.getProperty("user.dir");
		String fs = File.pathSeparator;
		String xmlFile1 = base + fs + "xmlFile1.xml";
		String xmlFile2 = base + fs + "xmlFile1.xm2";
		System.out.println(xmlFile1);
	}
	
	private static Document append(Document document, Node root, ApplicationMappingNodeContainer container){
		for (Iterator iter = container.toList().iterator(); iter.hasNext();) {
			Node next  =(Node) iter.next();
			if(next!=null)
				root.appendChild(next);
		}
		return document;
	}
}
