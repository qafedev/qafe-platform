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
/**
 * 
 */
package com.qualogy.qafe.bind.presentation.component;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.jibx.runtime.IUnmarshallingContext;

import com.qualogy.qafe.bind.PostProcessing;




/**
 * @author rjankie 
 */

public  class DataGridColumn extends EditableComponent implements PostProcessing, HasVisibleText{
	
	/**
	 * 
	 */
	
	public final static String CONTENT_STRING="string";
	public final static String CONTENT_HTML ="html";
	public final static String CONTENT_CHECKBOX="checkbox";
	public final static String CONTENT_LINK="link";
	
	private static final long serialVersionUID = 1195820494468727410L;

	protected Component component = null;
	protected Boolean staticField=Boolean.FALSE;
	protected Boolean identifyingField = Boolean.FALSE;
	
	
	
	protected Boolean sortable=Boolean.FALSE;
	//protected String type="string";
	protected String displayname="";
	protected String messageKey="";
	protected String content=CONTENT_STRING;
	public Boolean getSortable() {
		return sortable;
	}
	public void setSortable(Boolean sortable) {
		this.sortable = sortable;
	}
	
	
	/*public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}*/
	
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	public Boolean getStaticField() {
		return staticField;
	}
	public void setStaticField(Boolean staticField) {
		this.staticField = staticField;
	}
	public Component getComponent() {
		return component;
	}
	public void setComponent(Component component) {
		this.component = component;
	}
	public void performPostProcessing() {
		if (getStaticField()!=null){
			if (getStaticField().booleanValue()){
				if (getComponent()==null){
					Logger.getLogger(this.getClass().getName()).log(Level.WARNING,"When a column has static=\"true\"  a component is expected (see datagrid column: "+getId() +".");
				}
			}		
		}		
	}
	
	public void postset(IUnmarshallingContext context) {
		performPostProcessing();
		
	}
	public Boolean getIdentifyingField() {
		return identifyingField;
	}
	public void setIdentifyingField(Boolean identifyingField) {
		this.identifyingField = identifyingField;
	}

	public String getDisplayname() {
		return displayname;
	}
	public void setDisplayname(String displayname) {
		this.displayname = displayname;
	}
	public String getMessageKey() {
		return messageKey;
	}
	public void setMessageKey(String messageKey) {
		this.messageKey = messageKey;
	}

	
}
