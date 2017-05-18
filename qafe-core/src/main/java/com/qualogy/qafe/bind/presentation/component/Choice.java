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

public  class Choice extends EditableComponent implements HasComponents, HasRequiredProperty{
	
	 /**
	 * 
	 */
	private static final long serialVersionUID = -5536890209258097912L;
	/**
	 * 
	 */
	
	protected Boolean horizontalOrientation=Boolean.TRUE;
	
	protected List<ChoiceItem> choiceItems = new ArrayList<ChoiceItem>();

	/**
	 * @return
	 */
	public List<ChoiceItem> getChoiceItems() {
		return choiceItems;
	}

	/**
	 * @param choiceItems
	 */
	public void setChoiceItems(List<ChoiceItem> choiceItems) {
		this.choiceItems = choiceItems;
	}
	
	public void add(ChoiceItem choiceItem){
		if (choiceItem !=null){
			getChoiceItems().add(choiceItem);
		} else {
			// throw NullPointerException
		}
	}
	 
	public void remove(ChoiceItem choiceItem){
		if (choiceItem !=null){
			getChoiceItems().remove(choiceItem);			
		} else {
			// throw NullPointerException
		}
	}

	public Boolean getHorizontalOrientation() {
		return horizontalOrientation;
	}

	public void setHorizontalOrientation(Boolean horizontalOrientation) {
		this.horizontalOrientation = horizontalOrientation;
	}

	public List<? extends Component> getComponents() {
		return getChoiceItems();
	}
	
	protected Boolean required=Boolean.FALSE;
	
	public Boolean getRequired() {
		return required;
	}
	public void setRequired(Boolean required) {
		this.required = required;
	}

}
