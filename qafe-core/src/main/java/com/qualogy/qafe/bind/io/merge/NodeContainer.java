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
package com.qualogy.qafe.bind.io.merge;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Common class for nodecontainers. A nodecontainer,
 * contains nodes while merging documents. A nodecontainer
 * also has functionality for merging nodes when a node
 * of that kind was already contained
 * 
 * This container can only contain nodes three levels deep
 * f.i. <1><2><3></3></2></1>, because of the added convinience
 * functionality. When necessary this feature can be changed,
 * but for now it's to complex and has no use.
 * @author 
 *
 */
public abstract class NodeContainer {

	private Map<String, Node> childs;
	
	public NodeContainer() {
		super();
		this.childs = new HashMap<String, Node>();
		for (int i = 0; i < getChildNodeNames().length; i++) {
			this.childs.put(getChildNodeNames()[i], null);
		}
	}
	
	/**
	 * method needs to be implemented by child class and needs
	 * to return the childnodes directly underneath the root.
	 * @return
	 */
	public abstract String[] getChildNodeNames();
	
	/**
	 * method to get a childnode from a persisted child of the root
	 * @param childName
	 * @return
	 */
	private Node getChildNode(String childName) {
		return (Node)childs.get(childName);
	}
	
	/**
	 * method to append a child to the root
	 * @param document
	 * @param child
	 */
	public void appendChild(Document document, Node child){
		if(childs.get(child.getNodeName())!=null){
			NodeList childChildNodes = child.getChildNodes();//inside tier
			for (int i = 0; i < childChildNodes.getLength(); i++) {
				apendChildsChild(document, child, childChildNodes.item(i));
			}
		}else{
			childs.put(child.getNodeName(), child);
		}
	}
	/**
	 * method to append a child to a child of the root
	 * @param doc
	 * @param child
	 * @param childsChildNode
	 */
	private void apendChildsChild(Document doc, Node child, Node childsChildNode){
		NodeList containedChildChilds = getChildNode(child.getNodeName()).getChildNodes();
		boolean contained = false;
		for (int i = 0; i < containedChildChilds.getLength(); i++) {
			contained = (containedChildChilds.item(i).getNodeName().equals(childsChildNode.getNodeName()));
			if(contained){
				NodeList toAppend = childsChildNode.getChildNodes();
				for (int j = 0; j < toAppend.getLength(); j++) {
					containedChildChilds.item(i).appendChild(doc.importNode(toAppend.item(j),true));
				}
				break;
			}
		}
		if(!contained){
			getChildNode(child.getNodeName()).appendChild(childsChildNode);//doc.importNode(childsChildNode, true));
		}
	}
	
	/**
	 * Method returns the contents of this container as a list.
	 * The method uses the getChildNodeNames method to order
	 * the list.
	 * @return
	 */
	public List toList(){
		List<Node> list = new ArrayList<Node>();
		for (int i = 0; i < getChildNodeNames().length; i++) {
			list.add(childs.get(getChildNodeNames()[i]));
		}
		return list;
	}
	
}
