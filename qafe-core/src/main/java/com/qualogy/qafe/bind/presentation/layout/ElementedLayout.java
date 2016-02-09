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
package com.qualogy.qafe.bind.presentation.layout;

import java.util.ArrayList;
import java.util.List;

import com.qualogy.qafe.bind.presentation.component.Element;

public abstract class ElementedLayout extends Layout{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2448390425053287558L;
	
	protected List<Element> elements;
	
	public List<Element> getElements() {
		return elements;
	}

	public void setElements(List<Element> elements) {
		this.elements = elements;
	}
	
	public void add(Element element){
		if(element == null)
			throw new IllegalArgumentException("cannot add null element");
			
		if(this.elements == null)
			this.elements = new ArrayList<Element>();
		
		this.elements.add(element);
	}

	public void addAll(List<Element> elements){
		if(elements == null)
			throw new IllegalArgumentException("cannot add null element");
			
		if(this.elements == null)
			this.elements = new ArrayList<Element>();
		
		this.elements.addAll(elements);
	}

}
