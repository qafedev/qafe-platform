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
package com.qualogy.qafe.gwt.client.vo.ui;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.qualogy.qafe.core.conflictdetection.ConflicDetectionConstants;

public class DataGridColumnGVO extends EditableComponentGVO implements IsSerializable, HasVisibleTextI, HasContainer {
	
	public static final String CONTENT_STRING = "string";
	public static final String CONTENT_HTML = "html";
	public static final String CONTENT_LINK = "link";
	public static final String CONTENT_CHECKBOX = "checkbox";
	
	private String container;
	// This uuid is explicitly needed, since the UUID is needed when a CONTENT_TYPE = link is selected;
	// In the DataGridRenderer the uuid is available so the uuid can only the be set.

	private  Boolean sortable=Boolean.FALSE;
	protected Boolean required=Boolean.FALSE;

	private String displayname="";
	private String content=CONTENT_STRING;
	

	protected ComponentGVO component = null;
	
	protected Boolean staticField=Boolean.FALSE;
	
	protected Boolean identifyingField=Boolean.FALSE;

	

	public Boolean getIdentifyingField() {
		return identifyingField;
	}

	public void setIdentifyingField(Boolean identifyingField) {
		this.identifyingField = identifyingField;
	}

	public String getClassName() {
		return "com.qualogy.qafe.gwt.client.vo.ui.DataGridColumnGVO";
	}

	

	public Boolean getSortable() {
		return sortable;
	}

	public void setSortable(Boolean sortable) {
		this.sortable = sortable;
	}

	


	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public boolean isLink(){
		return getContent().equals(CONTENT_LINK);
	}
	
	public boolean isHTML(){
		return getContent().equals(CONTENT_HTML);
	}
	public boolean isCheckbox(){
		return getContent().equals(CONTENT_CHECKBOX);
	}
	public boolean isString(){
		return getContent().equals(CONTENT_STRING);
	}
	public boolean isQafeChecksum(){
		return ConflicDetectionConstants.QAFE_CHECKSUM.equals(getFieldName());
	}

	public ComponentGVO getComponent() {
		return component;
	}

	public void setComponent(ComponentGVO component) {
		this.component = component;
	}

	public Boolean getStaticField() {
		return staticField;
	}

	public void setStaticField(Boolean staticField) {
		this.staticField = staticField;
	}

	public String getContainerName() {
		return container;
	}

	public void setContainerName(String container) {
		this.container = container;
	}

	public Boolean getRequired() {
		return required;
	}

	public void setRequired(Boolean required) {
		this.required = required;
	}

	public String getDisplayname() {
		return displayname;
	}

	public void setDisplayname(String displayname) {
		this.displayname = displayname;
	}
}
