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
package com.qualogy.qafe.bind.domain.io;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ApplicationMappingXMLEnhancer{
	
	public static Document enhance(Document document, EnhancementSettings settings){
		if(settings!=null && settings.isSetToEnhance()){
			if(document==null)
				throw new IllegalArgumentException("doc is null");
		
			NodeList nodes = document.getChildNodes();
			for (int j = 0; j < nodes.getLength(); j++) {
				NodeList children = nodes.item(j).getChildNodes();
				for (int k = 0; k < children.getLength(); k++) {
					Node tierNode = document.importNode(children.item(k),true);
					NodeList containerNodes = tierNode.getChildNodes();
					for (int i = 0; i < containerNodes.getLength(); i++) {
						NodeList items = containerNodes.item(i).getChildNodes();
						for (int index = 0; index < items.getLength(); index++) {
							if(items.item(index).getNodeType()==Node.ELEMENT_NODE){
								//Element attr = document.createElement("order");
								for (int m = 0; m < ((Element)items.item(index)).getAttributes().getLength(); m++) {
									//System.out.println(((Element)items.item(index)).getAttributes().item(m));
								}
								((Element)items.item(index)).setAttribute("ofrder", "jaja");
							}
							
						}
					}
				}
			}
		}
		return document;
	}
}
