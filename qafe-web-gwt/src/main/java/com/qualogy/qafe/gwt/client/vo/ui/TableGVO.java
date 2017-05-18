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



/**
 * @author rjankie 
 */

public  class TableGVO extends EditableComponentGVO{

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
	protected TableRowGVO[]   rows;

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



	public String getClassName() {
		return "com.qualogy.qafe.gwt.client.vo.ui.TableGVO";
	}


	
}
