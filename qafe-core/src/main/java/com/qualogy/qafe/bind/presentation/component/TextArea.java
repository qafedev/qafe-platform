/**
 * Copyright 2008-2016 Qualogy Solutions B.V.
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


/**
 * @author rjankie 
 *   This is the superclass of all the editable components which will be
 *   rendered as graphical controls.
 */

public class TextArea extends TextField{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2233645103663293658L;

	protected int rows = 1;
	protected int cols;
	
	protected Boolean rich=Boolean.FALSE;

	public Boolean getRich() {
		return rich;
	}

	public void setRich(Boolean rich) {
		this.rich = rich;
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public int getCols() {
		return cols;
	}

	public void setCols(int cols) {
		this.cols = cols;
	}
}
