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
package com.qualogy.qafe.bind.presentation.component;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jibx.runtime.IUnmarshallingContext;

import com.qualogy.qafe.bind.PostProcessing;

/**
 * @author rjankie
 *
 */

public class View implements PostProcessing, Serializable {
	
	
	/**
	 * 
	 */
	
	protected Map<String, Window> windowNamesMap = new HashMap<String, Window>(17);
	

	protected String title;
	
	
	/**
	 * In case of MDI application, windows are defined rather than one rootPanel (each window has a rootPanel)
	 */
	protected List<Window> windows;
	/**
	 * 
	 * List of context menus
	 */
	
	protected List<MenuItem> contextMenus;
	
	protected List<Toolbar> toolbars;
	
	protected List<PanelDefinition> panelDefinitions;
	

	public View() {
		super();
		windows = new ArrayList<Window>();
		toolbars = new ArrayList<Toolbar>();
		contextMenus = new ArrayList<MenuItem>();
	}

	public List<Toolbar> getToolbars() {
		return toolbars;
	}

	public void setToolbars(List<Toolbar> toolbars) {
		this.toolbars = toolbars;
	}

	public List<MenuItem> getContextMenus() {
		return contextMenus;
	}

	public void setContextMenus(List<MenuItem> contextMenus) {
		this.contextMenus = contextMenus;
	}


	public List<Window> getWindows() {
		return windows;
	}

	public void setWindows(List<Window> windows) {
		this.windows = windows;
	}


	
	

	public  String getTitle() {
		return title;
	}

	public  void setTitle(String title) {
		this.title = title;
	}

	public void performPostProcessing() {
		if (windows!=null){
			for (Window window : windows) {
				
				if (!windowNamesMap.containsKey(window.getId())){
					windowNamesMap.put(window.getId(), window);
				} else {
					Logger.getLogger(this.getClass().getName()).log(Level.SEVERE,"There is more than one window with id ["+window.getId()+"]. Please check your code");
				}
			}
		}
		
	}

	public Map<String, Window> getWindowNamesMap() {
		return windowNamesMap;
	}

	public void postset(IUnmarshallingContext context) {
		performPostProcessing();
		
	}

	public List<PanelDefinition> getPanelDefinitions() {
		return panelDefinitions;
	}

	public void setPanelDefinitions(List<PanelDefinition> panelDefinitions) {
		this.panelDefinitions = panelDefinitions;
	}

	
	public void addAll(View otherView){
		if(this.contextMenus==null)
			this.contextMenus = new ArrayList<MenuItem>();
		if(otherView.getContextMenus()!=null)
			contextMenus.addAll(otherView.getContextMenus());
		
		if(this.panelDefinitions==null)
			this.panelDefinitions = new ArrayList<PanelDefinition>();
		if(otherView.getPanelDefinitions()!=null)
			panelDefinitions.addAll(otherView.getPanelDefinitions());
		
		if(this.toolbars == null)
			this.toolbars = new ArrayList<Toolbar>();
		if(otherView.getToolbars()!=null)
			toolbars.addAll(otherView.getToolbars());
		
		if(this.windowNamesMap == null)
			this.windowNamesMap = new HashMap<String, Window>();
		if(otherView.getWindowNamesMap()!=null)
			windowNamesMap.putAll(otherView.getWindowNamesMap());
		
		if(this.windows==null)
			this.windows = new ArrayList<Window>();
		if(otherView.getWindows()!=null)
			windows.addAll(otherView.getWindows());
		
	
	}

}
