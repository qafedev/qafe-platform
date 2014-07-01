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
package com.qualogy.qafe.mgwt.client.ui.renderer;

import java.util.List;

import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.UIObject;
import com.qualogy.qafe.mgwt.client.activities.AbstractActivity;
import com.qualogy.qafe.mgwt.client.ui.component.ComponentHelper;
import com.qualogy.qafe.mgwt.client.ui.component.IsEditable;
import com.qualogy.qafe.mgwt.client.vo.ui.ComponentGVO;
import com.qualogy.qafe.mgwt.client.vo.ui.ConditionGVO;
import com.qualogy.qafe.mgwt.client.vo.ui.ConditionalStyleRefGVO;
import com.qualogy.qafe.mgwt.client.vo.ui.EditableComponentGVO;
import com.qualogy.qafe.mgwt.client.vo.ui.HasRequired;
import com.qualogy.qafe.mgwt.client.vo.ui.HasRequiredValidationMessage;
import com.qualogy.qafe.mgwt.shared.QAMLConstants;

public abstract class AbstractEditableComponentRenderer extends AbstractComponentRenderer {

	@Override
	protected void init(ComponentGVO component, UIObject widget, String owner, String uuid, String parent, String context, AbstractActivity activity) {
		super.init(component, widget, owner, uuid, parent, context, activity);
		if (component instanceof EditableComponentGVO) {
			EditableComponentGVO editableComponentGVO = (EditableComponentGVO)component;
			if (widget instanceof IsEditable) {
				boolean editable = editableComponentGVO.getEditable(); 
				IsEditable isEditable = (IsEditable)widget;
				isEditable.setEditable(editable);
			}
			initRequired(editableComponentGVO, widget, uuid, parent, context);
			initRequiredValidationMessage(editableComponentGVO, widget, uuid, parent, context);
		}
	}


	protected void initRequired(EditableComponentGVO component, UIObject widget, String uuid, String parent, String context) {
		if (!(widget instanceof HasRequired)) {
			return;
		} 
		if (!(component instanceof HasRequired)) {
			return;
		}
		HasRequired hasRequired = (HasRequired)widget;
		HasRequired hasRequiredGVO = (HasRequired)component;
		boolean required = hasRequiredGVO.getRequired();
		hasRequired.setRequired(required);
	}
	
	protected void initRequiredValidationMessage(EditableComponentGVO component, UIObject widget, String uuid, String parent, String context) {
		if (!(widget instanceof HasRequiredValidationMessage)) {
			return;
		} 
		if (!(component instanceof HasRequiredValidationMessage)) {
			return;
		}
		HasRequiredValidationMessage hasRequiredValidationMessage = (HasRequiredValidationMessage)widget;
		HasRequiredValidationMessage hasRequiredValidationMessageGVO = (HasRequiredValidationMessage)component;
		String requiredValidationMessage = hasRequiredValidationMessageGVO.getRequiredValidationMessage();
		String requiredValidationTitle = hasRequiredValidationMessageGVO.getRequiredValidationTitle();
		hasRequiredValidationMessage.setRequiredValidationMessage(requiredValidationMessage);
		hasRequiredValidationMessage.setRequiredValidationTitle(requiredValidationTitle);
	}
	
	protected void handleConditonalStyle(EditableComponentGVO component, UIObject widget, Object value) {
		ConditionalStyleRefGVO conditionalStyleRefGVO = component.getConditionalStyleRef();
		if (conditionalStyleRefGVO == null) {
			return;
		}
		List<UIObject> styleWidgets = ComponentHelper.getStyleWidgets(widget);
		for (UIObject styleWidget : styleWidgets) {
			Element element = styleWidget.getElement();
			handleConditonalStyle(component, element, value);
		}
	}
	
	protected void handleConditonalStyle(EditableComponentGVO component, Element element, Object value) {
		ConditionalStyleRefGVO conditionalStyleRefGVO = component.getConditionalStyleRef();
		if (conditionalStyleRefGVO == null) {
			return;
		}
		List<ConditionGVO> conditions = conditionalStyleRefGVO.getConditions();
		if (conditions == null) {
			return;
		}
		resetInlineStyle(element);
		for (ConditionGVO conditionGVO: conditions) {
			handleConditionalStyleClass(component, element, conditionGVO, value);
			handleConditionalInlineStyle(component, element, conditionGVO, value);
		}
	}
	
	private void resetInlineStyle(Element element) {
		if (element == null) {
			return;
		}
		String inlineStyleInit = element.getPropertyString(QAMLConstants.INTERNAL_STYLE_INIT);
		if ((inlineStyleInit == null) || (inlineStyleInit.length() == 0)) {
			inlineStyleInit = QAMLConstants.INTERNAL_EMPTY;
			if (element.hasAttribute(QAMLConstants.INTERNAL_STYLE)) {
				inlineStyleInit = element.getAttribute(QAMLConstants.INTERNAL_STYLE);
			} 
			element.setPropertyString(QAMLConstants.INTERNAL_STYLE_INIT, inlineStyleInit);
		} else {
			if (QAMLConstants.INTERNAL_EMPTY.equals(inlineStyleInit)) {
				inlineStyleInit = "";
			}
			element.setAttribute(QAMLConstants.INTERNAL_STYLE, inlineStyleInit);
		}
	}
	
	private void handleConditionalStyleClass(EditableComponentGVO component, Element element, ConditionGVO conditionGVO, Object value) {
		String styleClass = conditionGVO.getStyleClass();
		if (styleClass == null) {
			return;
		}
		String expression = conditionGVO.getExpr();
		if (isMatched(expression, value)) {
			ComponentHelper.addStyle(element, styleClass);
		} else {
			ComponentHelper.removeStyle(element, styleClass);
		}
	}
	
	private void handleConditionalInlineStyle(EditableComponentGVO component, Element element, ConditionGVO conditionGVO, Object value) {
		String inlineStyle = conditionGVO.getStyle();
		if (inlineStyle == null) {
			return;
		}
		String expression = conditionGVO.getExpr();
		if (isMatched(expression, value)) {
			String[] styles = inlineStyle.split(";");
			for (String style : styles) {
				String[] styleAttribute = style.split(":");
				if (styleAttribute.length == 2) {
					String attribute = styleAttribute[0];
					String attributeValue = styleAttribute[1];
					ComponentHelper.setStyle(element, attribute, attributeValue);
				}
			}
		}
	}
	
	private boolean isMatched(String expression, Object value) {
		if ((expression == null) || (expression.length() == 0)) {
			return false;
		}
		if (value == null) {
			return false;
		}
		String key = null;
		String keyValue = null;
		try {
			key = expression.substring(1, expression.indexOf('='));
			keyValue = expression.substring(expression.indexOf("'") + 1, expression.length() - 2);
		} catch (Exception e) {
			key = null;
			keyValue = null;
		}
		if (QAMLConstants.INTERNAL_VALUE.equals(key)) {
			return value.equals(keyValue);
		}
		return false;
	}
}
