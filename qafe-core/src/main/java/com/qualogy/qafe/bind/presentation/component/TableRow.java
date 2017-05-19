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
package com.qualogy.qafe.bind.presentation.component;

import java.util.ArrayList;
import java.util.List;


/**
 * @author rjankie 
 */

public  class TableRow extends Component{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6207925561525926487L;


	protected List<TableCell> cells= new ArrayList<TableCell>();


	public List<TableCell> getCells() {
		return cells;
	}


	public void setCells(List<TableCell> cells) {
		this.cells = cells;
	}
	
	
}
