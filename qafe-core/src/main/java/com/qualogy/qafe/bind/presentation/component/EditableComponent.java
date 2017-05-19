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

import com.qualogy.qafe.bind.presentation.style.ConditionalStyle;


/**
 * @author rjankie 
 *   This is the superclass of all the editable components which will be
 *   rendered as graphical controls.
 */

public abstract class EditableComponent extends Component{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6443603835738321366L;
	/**
	 * The editable property of an editable component. Default is true.
	 */
	protected Boolean editable = Boolean.TRUE;
			
	protected ConditionalStyle conditionalStyleRef;
	
	public Boolean getEditable() {
		return editable;
	}

	public void setEditable(Boolean editable) {
		this.editable = editable;
	}
		
	public ConditionalStyle getConditionalStyleRef() {
		return conditionalStyleRef;
	}

	public void setConditionalStyleRef(ConditionalStyle conditionalStyleRef) {
		this.conditionalStyleRef = conditionalStyleRef;
	}

	

}
