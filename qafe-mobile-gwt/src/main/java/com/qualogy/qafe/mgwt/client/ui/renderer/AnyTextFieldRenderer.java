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
package com.qualogy.qafe.mgwt.client.ui.renderer;

import com.google.gwt.user.client.ui.UIObject;
import com.qualogy.qafe.mgwt.client.activities.AbstractActivity;
import com.qualogy.qafe.mgwt.client.ui.component.IsTextField;
import com.qualogy.qafe.mgwt.client.ui.events.DataChangeEvent;
import com.qualogy.qafe.mgwt.client.ui.events.DataChangeHandler;
import com.qualogy.qafe.mgwt.client.vo.ui.ComponentGVO;
import com.qualogy.qafe.mgwt.client.vo.ui.ConditionalStyleRefGVO;
import com.qualogy.qafe.mgwt.client.vo.ui.TextFieldGVO;

public class AnyTextFieldRenderer extends AbstractTextComponentRenderer {

	public UIObject render(ComponentGVO component, String owner, String uuid, String parent, String context, AbstractActivity activity) {
		UIObject widget = null;
		if (component instanceof TextFieldGVO) {
			TextFieldGVO textFieldGVO = (TextFieldGVO)component;
			AnyTextFieldRenderer textFieldRenderer = null;
			if (TextFieldGVO.TYPE_TEXT.equals(textFieldGVO.getType())) {
				if (textFieldGVO.getSuggest()) {
					textFieldRenderer = new TextFieldSuggestionRenderer();	
				} else {
					textFieldRenderer = new TextFieldTextRenderer();
				}
			} else if (TextFieldGVO.TYPE_DATE.equals(textFieldGVO.getType())) {
				textFieldRenderer = new TextFieldDateRenderer();
			} else if (TextFieldGVO.TYPE_EMAIL.equals(textFieldGVO.getType())) {
				textFieldRenderer = new TextFieldEmailRenderer();
			} else if (TextFieldGVO.TYPE_INTEGER.equals(textFieldGVO.getType())) {
				textFieldRenderer = new TextFieldIntegerRenderer();
			} else if (TextFieldGVO.TYPE_DOUBLE.equals(textFieldGVO.getType())) {
				textFieldRenderer = new TextFieldDoubleRenderer();
			} else if (TextFieldGVO.TYPE_SIGNED_INTEGER.equals(textFieldGVO.getType())) {
				textFieldRenderer = new TextFieldSignedIntegerRenderer();
			} else if (TextFieldGVO.TYPE_CHARACTERS.equals(textFieldGVO.getType())) {
				textFieldRenderer = new TextFieldCharsRenderer();
			} else if (TextFieldGVO.TYPE_SPINNER.equals(textFieldGVO.getType())) {
				textFieldRenderer = new TextFieldSpinnerRenderer();
			}
			UIObject textField = null;
			if (textFieldRenderer != null) {
				textField = textFieldRenderer.render(textFieldGVO, owner, uuid, parent, context, activity);
				textFieldRenderer.init(component, textField, owner, uuid, parent, context, activity);
			}
			widget = textField;
			registerComponent(component, textField, owner, parent, context);
		}
		return widget;
	}
	
	@Override
	protected void init(final ComponentGVO component, final UIObject widget, String owner, String uuid, String parent, String context, AbstractActivity activity) {
		super.init(component, widget, owner, uuid, parent, context, activity);
		final TextFieldGVO textFieldGVO = (TextFieldGVO)component;
		IsTextField textField = (IsTextField)widget;
		
		ConditionalStyleRefGVO conditionalStyleRefGVO = textFieldGVO.getConditionalStyleRef();
		if (conditionalStyleRefGVO != null) {
			textField.addDataChangeHandler(new DataChangeHandler() {
				@Override
				public void onDataChange(DataChangeEvent event) {
					handleConditonalStyle(textFieldGVO, widget, event.getNewValue());
				}
			});
		}
		
		Object value = textFieldGVO.getValue();
		if (value == null) {
			return;
		}
		textField.setData(value);
	}
}