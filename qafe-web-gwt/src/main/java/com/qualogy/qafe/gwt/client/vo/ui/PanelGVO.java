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
/**
 * 
 */
package com.qualogy.qafe.gwt.client.vo.ui;

import java.util.List;

import com.qualogy.qafe.gwt.client.vo.layout.LayoutGVO;

/**
 * @author rjankie
 * The Panel class is the container 
 */
public class PanelGVO extends ComponentGVO implements HasVisibleTextI  {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2191873699672485876L;
	
	protected List<ComponentGVO> dataPanelControl;
	protected Boolean showdatacontrol = Boolean.FALSE;
	protected String title;
	protected String displayname;
	protected LayoutGVO layout;
	protected List<ComponentGVO> components;
	protected Boolean disclosure = Boolean.FALSE;
	protected Boolean concurrentModificationEnabled = Boolean.FALSE;
		
	public Boolean getDisclosure() {
		return disclosure;
	}

	public void setDisclosure(Boolean disclosure) {
		this.disclosure = disclosure;
	}

	/**
	 * @return
	 */
	public List<ComponentGVO> getComponents() {
		return components;
	}

	/**
	 * @param components
	 */
	public void setComponents(List<ComponentGVO> components) {
		this.components = components;
	}

	/**
	 * @return
	 */
	public LayoutGVO getLayout() {
		return layout;
	}

	/**
	 * @param layout
	 */
	public void setLayout(LayoutGVO layout) {
		this.layout = layout;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getClassName() {
		return "com.qualogy.qafe.gwt.client.vo.ui.PanelGVO";
	}

	public List<ComponentGVO> getDataPanelControl() {
		return dataPanelControl;
	}

	public void setDataPanelControl(List<ComponentGVO> dataPanelControl) {
		this.dataPanelControl = dataPanelControl;
	}

	public Boolean getShowdatacontrol() {
		return showdatacontrol;
	}

	public void setShowdatacontrol(Boolean showdatacontrol) {
		this.showdatacontrol = showdatacontrol;
	}

	public String getDisplayname() {
		return displayname;
	}

	public void setDisplayname(String displayname) {
		this.displayname = displayname;
	}

	public Boolean getConcurrentModificationEnabled() {
		return concurrentModificationEnabled;
	}
	
	public void setConcurrentModificationEnabled(Boolean concurrentModificationEnabled) {
		this.concurrentModificationEnabled = concurrentModificationEnabled;
	}
}