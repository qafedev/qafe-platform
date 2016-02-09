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
package com.qualogy.qafe.gwt.client.component;

import java.util.List;

import org.gwt.mosaic.core.client.DOM;

import com.google.gwt.user.client.ui.UIObject;
import com.qualogy.qafe.gwt.client.ui.renderer.RendererHelper;
import com.qualogy.qafe.gwt.client.vo.ui.ConditionGVO;
import com.qualogy.qafe.gwt.client.vo.ui.ConditionalStyleRefGVO;
import com.qualogy.qafe.gwt.client.vo.ui.EditableComponentGVO;

public abstract class ComponentRendererHelper {
	
	public static void handleDataChange(EditableComponentGVO component, UIObject uiObject, Object oldValue, Object newValue) {
		processConditionalStyle(component, uiObject, newValue);
	}
	
	public static void processConditionalStyle(EditableComponentGVO component, UIObject uiObject, Object value) {
		if (component != null) {
			ConditionalStyleRefGVO conditionalStyleRefGVO = component.getConditionalStyleRef();
			if (conditionalStyleRefGVO != null) {
				List<ConditionGVO> conditions = conditionalStyleRefGVO.getConditions();
				if (conditions != null) {
					resetInlineStyleToOrigin(uiObject);
					StringBuffer appliedInlineStyles = new StringBuffer();
					for (ConditionGVO condition: conditions) {
						if ((condition.getStyleClass() != null) || (condition.getStyle() != null)) {
							boolean expressionMatched = isExpressionMatched(condition.getExpr(), (value==null) ? null : value.toString());
							processConditionalStyleClass(component, uiObject, condition, expressionMatched);
							processConditionalInlineStyle(component, uiObject, condition, expressionMatched, appliedInlineStyles);
						}
					}
				}
			}	
		}
	}
	
	private static void resetInlineStyleToOrigin(UIObject uiObject) {
		String originInlineStyle = DOM.getElementAttribute(uiObject.getElement(), RendererHelper.ATTRIBUTE_ORIGIN_STYLE);
		if ((originInlineStyle != null) && (originInlineStyle.length() > 0)) {
			if (originInlineStyle.equals(RendererHelper.EMPTY_VALUE)) {
				DOM.removeElementAttribute(uiObject.getElement(), RendererHelper.ATTRIBUTE_STYLE);
			} else {
				DOM.setElementAttribute(uiObject.getElement(), RendererHelper.ATTRIBUTE_STYLE, originInlineStyle);	
			}
		} else {
			originInlineStyle = DOM.getElementAttribute(uiObject.getElement(), RendererHelper.ATTRIBUTE_STYLE);
			if ((originInlineStyle == null) || (originInlineStyle.length() == 0)) {
				originInlineStyle = RendererHelper.EMPTY_VALUE;
			}
			DOM.setElementAttribute(uiObject.getElement(), RendererHelper.ATTRIBUTE_ORIGIN_STYLE, originInlineStyle);
		}
	}
	
	private static void processConditionalStyleClass(EditableComponentGVO component, UIObject uiObject, ConditionGVO condition, boolean matched) {
		if (condition.getStyleClass() != null) {
			uiObject.setStyleName(condition.getStyleClass(), matched);
			if(uiObject instanceof SpreadsheetCell) {
				SpreadsheetCell ui = (SpreadsheetCell) uiObject;
				// spreadsheetcell have a focuslabel inside(which is a container of html component). So we have apply the style for that also.
				ui.getLabel().setStyleName(condition.getStyleClass(), matched);
			}
		}
	}
	
	private static void processConditionalInlineStyle(EditableComponentGVO component, UIObject uiObject, ConditionGVO condition, boolean matched, StringBuffer appliedInlineStyles) {
		if (matched && (condition.getStyle() != null)) {
			appliedInlineStyles.append(condition.getStyle() + ";");
			DOM.setElementAttribute(uiObject.getElement(), RendererHelper.ATTRIBUTE_STYLE, appliedInlineStyles.toString());
		}
	}
	
	// TODO
	private static boolean isExpressionMatched(String expr, String value){
		if( expr != null && expr.length() > 0 ) {
			String exprKey = null;
			String exprValue = null;
			try {
				exprKey = expr.substring(1, expr.indexOf('='));
				exprValue = expr.substring(expr.indexOf("'") + 1, expr.length() - 2 );
				
			} catch(Exception e) {
				exprKey = null;
				exprValue = null;
			}
			
			if("value".equals(exprKey) && exprValue != null  && exprValue.equals(value)) {
				return true;
			}
		}
		return false;
	}
}
