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
package com.qualogy.qafe.mgwt.client.vo.ui;


/**
 * @author rjankie 
 */

public  class TileListGVO extends ComponentGVO implements  HasComponentsI{
	
	/**
	 * 
	 */
		/**
	 * 
	 */
	
	

	protected ComponentGVO component = null;
	protected Integer   columns  = 1;
	
	
	
	public Integer getColumns() {
		return columns;
	}
	public void setColumns(Integer columns) {
		this.columns = columns;
	}
	public ComponentGVO getComponent() {
		return component;
	}
	public void setComponent(ComponentGVO component) {
		this.component = component;
	}

	

	public ComponentGVO[] getComponents() {
			if (component!=null){
				return new ComponentGVO[]{component};
			} else {
				return new ComponentGVO[]{};
			}
	}
	@Override
	public String getClassName() {
		return "com.qualogy.qafe.gwt.client.vo.ui.TileListGVO";
	}
	
}
