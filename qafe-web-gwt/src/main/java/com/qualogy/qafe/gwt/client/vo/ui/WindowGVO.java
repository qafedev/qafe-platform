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
package com.qualogy.qafe.gwt.client.vo.ui;

import com.google.gwt.user.client.rpc.IsSerializable;

public class WindowGVO extends ComponentGVO implements IsSerializable, HasVisibleTextI {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4865753925690290020L;

	private RootPanelGVO rootPanel;

	private Boolean resizable = Boolean.TRUE;

	private Boolean closable = Boolean.TRUE;

	private Boolean minimizable = Boolean.TRUE;

	private Boolean maximizable = Boolean.TRUE;

	private Boolean draggable = Boolean.TRUE;

	private Boolean loadOnStartup = Boolean.FALSE;

	private String icon;

	protected String displayname; // Is set using UIAssemblerHelper.manageInternationalisation(...) and used to set title of window
	
	public Integer getLeft() {
		return left;
	}

	public void setLeft(Integer left) {
		this.left = left;
	}

	public Integer getTop() {
		return top;
	}

	public void setTop(Integer top) {
		this.top = top;
	}

	public String getIconStyle() {
		return iconStyle;
	}

	public void setIconStyle(String iconStyle) {
		this.iconStyle = iconStyle;
	}

	private Integer left;
	private Integer top;
	private String iconStyle;
	private Boolean inDock;
    
    public Boolean getInDock() {
		return inDock;
	}

	public void setInDock(Boolean inDock) {
		this.inDock = inDock;
	}
	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public Boolean getLoadOnStartup() {
		return loadOnStartup;
	}

	public void setLoadOnStartup(Boolean loadOnStartup) {
		this.loadOnStartup = loadOnStartup;
	}

	private Boolean isparent = Boolean.TRUE;

	private String title;

	public RootPanelGVO getRootPanel() {
		return rootPanel;
	}

	public void setRootPanel(RootPanelGVO rootPanel) {
		this.rootPanel = rootPanel;
	}

	public String getClassName() {
		return "com.qualogy.qafe.gwt.client.vo.ui.WindowGVO";
	}

	public void setTitle(String title) {
		this.title = title;

	}

	public String getTitle() {
		return title;
	}

	public Boolean getIsparent() {
		return isparent;
	}

	public void setIsparent(Boolean isparent) {
		this.isparent = isparent;
	}

	public Boolean getClosable() {
		return closable;
	}

	public void setClosable(Boolean closable) {
		this.closable = closable;
	}

	public Boolean getDraggable() {
		return draggable;
	}

	public void setDraggable(Boolean draggable) {
		this.draggable = draggable;
	}

	public Boolean getMinimizable() {
		return minimizable;
	}

	public void setMinimizable(Boolean minimizable) {
		this.minimizable = minimizable;
	}

	public Boolean getResizable() {
		return resizable;
	}

	public void setResizable(Boolean resizable) {
		this.resizable = resizable;
	}

	public Boolean getMaximizable() {
		return maximizable;
	}

	public void setMaximizable(Boolean maximizable) {
		this.maximizable = maximizable;
	}
	
	public String getDisplayname() {
		return displayname;
	}

	public void setDisplayname(String displayName) {
		this.displayname = displayName;
	}

	public WindowGVO strip() {
		WindowGVO strippedWindow = new WindowGVO();
		strippedWindow.setClosable(getClosable());
		strippedWindow.setDisabled(getDisabled());
		strippedWindow.setDraggable(getDraggable());
		strippedWindow.setEvents(getEvents());
		strippedWindow.setFieldName(getFieldName());
		strippedWindow.setHeight(getHeight());
		strippedWindow.setIcon(getIcon());
		strippedWindow.setIconStyle(getIconStyle());
		strippedWindow.setId(getId());
		strippedWindow.setInDock(getInDock());
		strippedWindow.setIsparent(getIsparent());
		strippedWindow.setLeft(getLeft());
		strippedWindow.setLoadOnStartup(getLoadOnStartup());
		strippedWindow.setMaximizable(getMaximizable());
		strippedWindow.setMenu(getMenu());
		strippedWindow.setMinimizable(getMinimizable());
		strippedWindow.setResizable(getResizable());
		strippedWindow.setStyleClass(getStyleClass());
		strippedWindow.setStyleProperties(getStyleProperties());
		strippedWindow.setTitle(getTitle());
		strippedWindow.setTooltip(getTooltip());
		strippedWindow.setTop(getTop());
		strippedWindow.setUuid(getUuid());
		strippedWindow.setVisible(getVisible());
		strippedWindow.setWidth(getWidth());
		strippedWindow.setWindow(getWindow());
		
		return strippedWindow;
	}

}
