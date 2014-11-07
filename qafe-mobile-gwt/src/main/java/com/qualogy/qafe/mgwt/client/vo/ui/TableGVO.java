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
/**
 * 
 */
package com.qualogy.qafe.mgwt.client.vo.ui;

/**
 * @author rjankie 
 */

public class TableGVO extends EditableComponentGVO implements HasComponentsI {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2241847670709460407L;
	
	/**
	 * 
	 */
	protected TableRowGVO header;
	
	/**
	 * 
	 */
	protected TableRowGVO[] rows;

	/**
	 * @return
	 */
	public TableRowGVO getHeader() {
		return header;
	}

	/**
	 * @param header
	 */
	public void setHeader(TableRowGVO header) {
		this.header = header;
	}

	public TableRowGVO[] getRows() {
		return rows;
	}

	public void setRows(TableRowGVO[] rows) {
		this.rows = rows;
	}

	@Override
	public ComponentGVO[] getComponents() {
		int numComponents = 0;
		if (header != null) {
			numComponents++;
		}
		if (rows != null) {
			numComponents += rows.length;
		}
		if (numComponents == 0) {
			return null;
		}
		ComponentGVO[] components = new ComponentGVO[numComponents];
		int index = 0;
		if (header != null) {
			components[index] = header;
			index++;
		}
		if (rows != null) {
			for (int i=0; i<rows.length; i++) {
				components[index] = rows[i];
				index++;
			}
		}
		return components;
	}

	public String getClassName() {
		return "com.qualogy.qafe.gwt.client.vo.ui.TableGVO";
	}
}
