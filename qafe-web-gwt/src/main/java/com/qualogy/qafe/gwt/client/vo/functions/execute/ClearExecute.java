/**
 * Copyright 2008-2015 Qualogy Solutions B.V.
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
package com.qualogy.qafe.gwt.client.vo.functions.execute;

import java.util.List;

import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBoxBase;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;
import com.qualogy.qafe.gwt.client.component.HasDataGridMethods;
import com.qualogy.qafe.gwt.client.component.QDatePicker;
import com.qualogy.qafe.gwt.client.component.QLabel;
import com.qualogy.qafe.gwt.client.component.QRadioButton;
import com.qualogy.qafe.gwt.client.component.QSuggestBox;
import com.qualogy.qafe.gwt.client.ui.renderer.DropDownRenderer;
import com.qualogy.qafe.gwt.client.ui.renderer.RendererHelper;
import com.qualogy.qafe.gwt.client.vo.functions.BuiltInFunctionGVO;
import com.qualogy.qafe.gwt.client.vo.functions.ClearGVO;
import com.qualogy.qafe.gwt.client.vo.functions.SetValueGVO;

@Deprecated
public class ClearExecute implements ExecuteCommand {
	
	public void execute(BuiltInFunctionGVO builtInFunction) {
		if (builtInFunction instanceof ClearGVO) {
			boolean referencedByName = false;
			ClearGVO clear = (ClearGVO) builtInFunction;
			List<UIObject> uiObjects = null;
			if (clear.getBuiltInComponentGVO().getComponentIdUUID() != null) {
				uiObjects = RendererHelper.getComponent(clear
						.getBuiltInComponentGVO().getComponentIdUUID());
			} 
			// clear tag has only one attribute ref. So it can be id or name.
			// first search considering ref is id if not take that as component name.
			if(uiObjects == null) {
				referencedByName = true;
				uiObjects = RendererHelper.getNamedComponent(clear
						.getBuiltInComponentGVO().getComponentIdUUID());
			}
			
			//could be group
			if(uiObjects == null) {
				referencedByName = true;
				uiObjects = RendererHelper.getGroupedComponent(clear
						.getBuiltInComponentGVO().getComponentIdUUID());
			}
			
			if(uiObjects != null) {
				for (UIObject uiObject : uiObjects) {

					if (uiObject != null) {
						clear(uiObject, builtInFunction, referencedByName);
					}
				}
			}
			
		}
		FunctionsExecutor.setProcessedBuiltIn(true);

	}
	
	public static void clear(UIObject uiObject,	BuiltInFunctionGVO builtInFunction) {
		clear(uiObject, builtInFunction, false);
	}
	//TODO: Add missing components and make it generalised
	//TODO: Make all if conditions together using else-if
	/**
	 * clear method will be executed when using clear buidin or when setting null value to a component using set buildin.
	 * Rules applied for clearing:
		If the ref attribute references to the id of : 
			an editable component (like a TextField): the value will be cleared
			a non-editable component (like a Label): won't do anything
			a containment component (like a Panel): the value of all editable components will be cleared 

		If the ref attribute references to the name of :
			an editable component (like a TextField): the value will be cleared
			a non-editable component (like a Label): the displayname will be cleared 
			a container component (like a Panel): the value or displayname of all child components with the name attribute will be cleared
			
	  Rules applied while setting a value to a component: (While setting null value also this rules gets applied.)
		If component-id attribute references to: 
			an editable component (like a TextField): the value will be set
			a non-editable component (like a Label):  the displayname will be set
			a container component (like a Panel): the displayname will be set

		If name attribute references to the  :
			editable component (like a TextField): the value will be set
			non-editable component (like a Label): the displayname will be set 
			container component (like a Panel): the value  or displayname of all child components with the name attribute will be set. 	 
	 *
	 *
	 */
	public static void clear(UIObject uiObject,	BuiltInFunctionGVO builtInFunction, boolean referencedByName) {
		if (uiObject != null) {
			if( !referencedByName || RendererHelper.isNamedComponent(uiObject)) {
				processClearForNonInputComponents(uiObject, builtInFunction, referencedByName);
				processClearForInputComponents(uiObject, builtInFunction);
			}
			
			// When it is referenced by the id of the component
			// and the component is a datagrid, no need to clear its children
			if (!referencedByName && (uiObject instanceof HasDataGridMethods)) {
				return;
			}
			
			if (uiObject instanceof HasWidgets) {
				HasWidgets hasWidgets = (HasWidgets) uiObject;
				for (Widget widget : hasWidgets) {					
					clear(widget, builtInFunction, referencedByName);
				}
			}
		}
	}

	private static void processClearForInputComponents(UIObject uiObject, BuiltInFunctionGVO builtInFunction) {
		if (uiObject instanceof TextBoxBase) {
			TextBoxBase textBoxBase = (TextBoxBase) uiObject;
			textBoxBase.setText("");
		}

		if (uiObject instanceof QDatePicker) {
			QDatePicker qDatePicker = (QDatePicker) uiObject;
			qDatePicker.getTextBox().setText("");
		}
		
		if (uiObject instanceof HasDataGridMethods) {
			((HasDataGridMethods) uiObject).insertData(null, false,
					builtInFunction.getSenderId(),
					builtInFunction.getListenerType());
			((HasDataGridMethods) uiObject).redraw();
		}
		if (uiObject instanceof ListBox) {
			ListBox listBox = (ListBox) uiObject;
			boolean hasEmptyItem = DropDownRenderer.hasEmptyItem(listBox);
			int indexOfValue = hasEmptyItem ? 0 : -1;
			listBox.setSelectedIndex(indexOfValue);
			if (builtInFunction instanceof ClearGVO) {
				int offset = hasEmptyItem ? 1 : 0;
				while (listBox.getItemCount() > offset) {
					listBox.removeItem(listBox.getItemCount() - 1);
				}
			}
		}

		if (uiObject instanceof CheckBox) {
			CheckBox checkbox = (CheckBox) uiObject;
			checkbox.setValue(false);
			if (uiObject instanceof QRadioButton) {
				((QRadioButton) uiObject).reset();
			}
		}

		if (uiObject instanceof FileUpload) {
			FileUpload fileUpload = (FileUpload) uiObject;
			Widget fup = fileUpload.getParent();
			// fup will be the layout component
			if (fup != null) {
				Widget fupp = fup.getParent();

				if (fupp instanceof FormPanel) {
					((FormPanel) fupp).reset();
				}
			}
		}
		
		if(uiObject instanceof QSuggestBox){
			QSuggestBox suggestTextField = (QSuggestBox)uiObject;
			suggestTextField.getTextBox().setText("");
		}
	}

	private static void processClearForNonInputComponents(UIObject uiObject, BuiltInFunctionGVO builtInFunctionGVO, boolean referencedByName) {
		if((builtInFunctionGVO instanceof SetValueGVO) || (referencedByName && (RendererHelper.isNamedComponent(uiObject)))) {
			if(uiObject instanceof QLabel){
				QLabel qLabel = (QLabel) uiObject;
				qLabel.setText("");			
			}
		}		
	}
}