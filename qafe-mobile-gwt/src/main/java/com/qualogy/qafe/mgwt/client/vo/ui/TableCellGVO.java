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
package com.qualogy.qafe.mgwt.client.vo.ui;

/**
 * @author rjankie
 *
 */
public class TableCellGVO extends EditableComponentGVO implements HasComponentsI {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7512588691125132578L;
	
	/**
	 * 
	 */
	protected String label;
	
	/**
	 * 
	 */
	protected ComponentGVO  component;

	/**
	 * 
	 */
	protected Integer cellSpan;
	
	/**
	 * 
	 */
	protected Integer rowSpan;

	/**
	 * @return
	 */
	public Integer getCellSpan() {
		return cellSpan;
	}

	/**
	 * @param cellSpan
	 */
	public void setCellSpan(Integer cellSpan) {
		this.cellSpan = cellSpan;
	}

	/**
	 * @return
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * @param label
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * @return
	 */
	public Integer getRowSpan() {
		return rowSpan;
	}

	/**
	 * @param rowSpan
	 */
	public void setRowSpan(Integer rowSpan) {
		this.rowSpan = rowSpan;
	}


	public ComponentGVO getComponent() {
		return component;
	}

	public void setComponent(ComponentGVO component) {
		this.component = component;
	}

	@Override
	public ComponentGVO[] getComponents() {
		if (component == null) {
			return null;
		}
		ComponentGVO[] components = new ComponentGVO[1];
		components[0] = component;
		return components;
	}
	
	public String getClassName() {
		return "com.qualogy.qafe.gwt.client.vo.ui.TableCellGVO";
	}
}
