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
package com.qualogy.qafe.bind.presentation.style;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class StyleSet implements Serializable{

	private static final long serialVersionUID = -2295173783910891334L;

	protected List<Style> styles;
	
	protected List<ConditionalStyle> conditionalStyles;
	
	public Iterator<Style> getStylesIterator() {
		return (styles!=null)?styles.iterator():null;
	}
	
	/**
	 * @deprecated use getStylesIterator
	 * @return
	 */
	public List<Style> getStyles(){
		return styles;
	}
	
	/**
     * method to add a Style to a Style list. The list will be
     * created when null 
     * @param action
     * @throws IllegalArgumentException - when object param passed is null
     */
    public void add(Style style) {
    	if(style==null)
    		throw new IllegalArgumentException("action cannot be null");
    	if(styles==null)
    		styles = new ArrayList<Style>();
    	
    	styles.add(style);
    }
    /**
     * @deprected use add method instead
     */
	public void setStyles(List<Style> styles) {
		this.styles = styles;
	}

	public void addAll(StyleSet otherStyles){
		
		if(styles==null)
			styles = new ArrayList<Style>();
		
		styles.addAll(otherStyles.styles);
	}
	
	/**
     * method to add a Style to a Style list. The list will be
     * created when null 
     * @param action
     * @throws IllegalArgumentException - when object param passed is null
     */
    public void add(ConditionalStyle conditionalStyle) {
    	if(conditionalStyle==null)
    		throw new IllegalArgumentException("action cannot be null");
    	if(conditionalStyles==null)
    		conditionalStyles = new ArrayList<ConditionalStyle>();
    	
    	conditionalStyles.add(conditionalStyle);
    }
    
    public Iterator<ConditionalStyle> getConditionalStylesIterator() {
		return (conditionalStyles != null)? conditionalStyles.iterator():null;
	}
    
    
	public List<ConditionalStyle> getConditionalStyles() {
		return conditionalStyles;
	}

	public void setConditionalStyles(List<ConditionalStyle> conditionalStyles) {
		this.conditionalStyles = conditionalStyles;
	}
}
