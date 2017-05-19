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
package com.qualogy.qafe.gwt.client.vo.ui;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.qualogy.qafe.gwt.client.vo.ui.event.EventListenerGVO;

/**
 * @author rjankie This is the superclass of all the components which will be
 *         rendered as graphical controls.
 */

public abstract class ComponentGVO extends UIVO implements IsSerializable {

	/**
	 * The unique id of the component
	 */
	protected String id;

	/**
	 * The indicator to enable/disable the component
	 */
	protected Boolean disabled = Boolean.FALSE;

	/**
	 * The indicator to show/hide the component
	 */
	protected Boolean visible = Boolean.TRUE;

	/**
	 * In case of WebApplication one can provide the class for the style of the
	 * Component (if a non-inline css is used)
	 */
	protected String styleClass;

	protected String[][] styleProperties;

	protected EventListenerGVO[] events;

	protected MenuItemGVO menu;

	private String window;

	private String tooltip;

	private String fieldName;
	
	private String group;

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	private String width;

	private String height;
	
	private String parent;
	
	private String context;

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getWindow() {
		return window;
	}

	public void setWindow(String window) {
		this.window = window;
	}

	public MenuItemGVO getMenu() {
		return menu;
	}

	public void setMenu(MenuItemGVO menu) {
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

	/**
	 * @return
	 */
	public String getStyleClass() {
		return styleClass;
	}

	/**
	 * @param styleClass
	 */
	public void setStyleClass(String styleClass) {
		this.styleClass = styleClass;
	}

	public abstract String getClassName();

	//public abstract String getStyleName();

	public EventListenerGVO[] getEvents() {
		return events;
	}

	public void setEvents(EventListenerGVO[] events) {
		this.events = events;
	}

	public Boolean isDisabled() {
		return disabled;
	}

	public void setDisabled(Boolean disabled) {
		this.disabled = disabled;
	}

	public String[][] getStyleProperties() {
		return styleProperties;
	}

	public void setStyleProperties(String[][] styleProperties) {
		this.styleProperties = styleProperties;
	}

	public Boolean getVisible() {
		return visible;
	}

	public void setVisible(Boolean visible) {
		this.visible = visible;
	}

	public Boolean getDisabled() {
		return disabled;
	}

	/**
	 * This is function is for items for which the styles are not correctly
	 * processed. The DataGrid/PagingDataGrid are examples of this.
	 * 
	 * @return
	 */
	public String getHeight() {
	/*	if (this.height != null) {
			return this.height;
		} else {
			String height = "100%";
			if (getStyleProperties() != null) {
				for (int i = 0; i < getStyleProperties().length; i++) {
					if ("height".equalsIgnoreCase(getStyleProperties()[i][0])) {
						height = getStyleProperties()[i][1];
						break;
					}
				}
			}
			return height;
		}*/
		return height;
	}

	public String getWidth() {
		/*if (this.width!=null){
			return this.width;
		}
		String width = "100%";
		if (getStyleProperties() != null) {
			for (int i = 0; i < getStyleProperties().length; i++) {
				if ("width".equalsIgnoreCase(getStyleProperties()[i][0])) {
					width = getStyleProperties()[i][1];
					break;
				}
			}
		}*/
		return width;
	}

	public String getTooltip() {
		return tooltip;
	}

	public void setTooltip(String tooltip) {
		this.tooltip = tooltip;
	}

	public boolean hasName() {
		return (fieldName != null && fieldName.length() > 0);
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

	public void setContext(String context) {
		this.context = context;
	}

	public String getContext() {
		return context;
	}
}
