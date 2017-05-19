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

public class ChoiceGVO extends EditableComponentGVO implements HasComponentsI,
		HasRequired {

	private Boolean required = Boolean.FALSE;
	private static final long serialVersionUID = -5536890209258097912L;
	protected ChoiceItemGVO[] choiceItems = null;
	protected Boolean horizontalOrientation = Boolean.TRUE;
	//private String styleClassName = "qafe_choice";

	public Boolean getRequired() {
		return required;
	}

	public void setRequired(Boolean required) {
		this.required = required;
	}

	public ChoiceItemGVO[] getChoiceItems() {
		return choiceItems;
	}

	public void setChoiceItems(ChoiceItemGVO[] choiceItems) {
		this.choiceItems = choiceItems;
	}

	public String getClassName() {
		return "com.qualogy.qafe.gwt.client.vo.ui.ChoiceGVO";
	}

	public Boolean getHorizontalOrientation() {
		return horizontalOrientation;
	}

	public void setHorizontalOrientation(Boolean horizontalOrientation) {
		this.horizontalOrientation = horizontalOrientation;
	}

	public ComponentGVO[] getComponents() {
		return getChoiceItems();
	}

	/*public String getStyleClassName() {
		return styleClassName;
	}

	public void setStyleClassName(String styleClassName) {
		this.styleClassName = styleClassName;
	}*/
}
