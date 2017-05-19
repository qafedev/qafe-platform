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

import com.google.gwt.user.client.rpc.IsSerializable;




/**
 * @author rjankie 
 *   This is the superclass of all the editable components which will be
 *   rendered as graphical controls.
 */

public abstract class EditableComponentGVO extends ComponentGVO implements IsSerializable{

	protected Boolean editable = Boolean.TRUE;
	
	protected ConditionalStyleRefGVO conditionalStyleRef;
	
	public Boolean getEditable() {
		return editable;
	}

	public void setEditable(Boolean editable) {
		this.editable = editable;
	}

	public ConditionalStyleRefGVO getConditionalStyleRef() {
		return conditionalStyleRef;
	}

	public void setConditionalStyleRef(ConditionalStyleRefGVO conditionalStyleRef) {
		this.conditionalStyleRef = conditionalStyleRef;
	}
	
	

}
