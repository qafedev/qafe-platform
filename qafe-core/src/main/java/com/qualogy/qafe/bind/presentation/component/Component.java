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
package com.qualogy.qafe.bind.presentation.component;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import com.qualogy.qafe.bind.core.security.AuthorizationControlled;
import com.qualogy.qafe.bind.core.security.AuthorizationRule;
import com.qualogy.qafe.bind.domain.BindBase;

/**
 * @author rjankie This is the superclass of all the components which will be
 *         rendered as graphical controls.
 */

public abstract class Component extends BindBase implements AuthorizationControlled {

	/**
	 * 
	 */
	private static final long serialVersionUID = 165625576871630419L;

	/**
	 * The unique id of the component
	 */
	protected String id;

	/**
	 * Enabled or disable this component. Default every component is enabled.
	 */
	protected Boolean disabled = Boolean.FALSE;

	/**
	 * Show or hide this component. Default every component is visible.
	 */
	protected Boolean visible  = Boolean.TRUE;

	protected MenuItem menu;
	
	protected String tooltip;

	protected String fieldName;
	
	protected String group;
	
	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}
	
	public void addGroup(String group) {
		if(this.group != null) {
			this.group = this.group + "," + group;
		} else {
			this.group = group;
		}
	}

	protected String width;
	
	protected String height;
	
	/**
	 * authorization rule controlling this object
	 */
	private AuthorizationRule controllingAuthorizationRule;
	
	protected String styleClass;
	
	protected String style;
		
	
	public MenuItem getMenu() {
		return menu;
	}

	public void setMenu(MenuItem menu) {
		this.menu = menu;
	}
	
	/**
	 * @return id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            Unique id of the component
	 */
	public void setId(String id) {
		this.id = id;
	}

	public Boolean getDisabled() {
		return disabled;
	}

	public void setDisabled(Boolean disabled) {
		this.disabled = disabled;
	}

	public Boolean getVisible() {
		return visible;
	}

	public void setVisible(Boolean visible) {
		this.visible = visible;
	}

	public String getTooltip() {
		return tooltip;
	}

	public void setTooltip(String tooltip) {
		this.tooltip = tooltip;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public String toString(){
		return ReflectionToStringBuilder.reflectionToString(this);
	}

	public AuthorizationRule getControllingAuthorizationRule() {
		return controllingAuthorizationRule;
	}

	public void setControllingAuthorizationRule(
			AuthorizationRule controllingAuthorizationRule) {
		this.controllingAuthorizationRule = controllingAuthorizationRule;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public String getStyleClass() {
		return styleClass;
	}

	public void setStyleClass(String styleClass) {
		this.styleClass = styleClass;
	}
	
	public boolean isAuthorizationControlled() {
		return getControllingAuthorizationRule()!=null;
	}
	
}
