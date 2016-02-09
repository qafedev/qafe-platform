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
package com.qualogy.qafe.mgwt.client.vo.functions.execute;

import java.util.List;

import com.google.gwt.user.client.ui.HasEnabled;
import com.google.gwt.user.client.ui.HasVisibility;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;
import com.qualogy.qafe.mgwt.client.activities.AbstractActivity;
import com.qualogy.qafe.mgwt.client.activities.WindowActivity;
import com.qualogy.qafe.mgwt.client.ui.component.HasDisplayname;
import com.qualogy.qafe.mgwt.client.ui.component.HasIndexSelection;
import com.qualogy.qafe.mgwt.client.ui.component.HasPaging;
import com.qualogy.qafe.mgwt.client.ui.component.HasRange;
import com.qualogy.qafe.mgwt.client.ui.component.HasSelection;
import com.qualogy.qafe.mgwt.client.ui.component.IsEditable;
import com.qualogy.qafe.mgwt.client.vo.functions.BuiltInComponentGVO;
import com.qualogy.qafe.mgwt.client.vo.functions.BuiltInFunctionGVO;
import com.qualogy.qafe.mgwt.client.vo.functions.SetPropertyGVO;
import com.qualogy.qafe.mgwt.shared.QAMLConstants;
import com.qualogy.qafe.mgwt.shared.QAMLUtil;

public class SetPropertyExecute extends BuiltInExecute {

	public void execute(BuiltInFunctionGVO builtInFunctionGVO, AbstractActivity activity) {
		if (builtInFunctionGVO instanceof SetPropertyGVO) {
			SetPropertyGVO setPropertyGVO = (SetPropertyGVO)builtInFunctionGVO;
			if (activity instanceof WindowActivity) {
				WindowActivity windowActivity = (WindowActivity)activity;
				setProperty(setPropertyGVO, windowActivity);
			}
		}
		FunctionsExecutor.setProcessedBuiltIn(true);
	}

	private void setProperty(SetPropertyGVO setPropertyGVO, WindowActivity activity) {
		List<BuiltInComponentGVO> builtInComponents = setPropertyGVO.getComponents();
		if (builtInComponents == null) {
			return;
		}
		for (BuiltInComponentGVO builtInComponentGVO : builtInComponents) {
			List<UIObject> widgets = getWidgets(builtInComponentGVO, activity);
			if (QAMLUtil.isEmpty(widgets)) {
				continue;
			}
			for (UIObject widget : widgets) {
				setProperty(setPropertyGVO, widget);
				if (widget instanceof HasWidgets) {
					if (hasEffectOnChild(setPropertyGVO, (HasWidgets)widget)) {
						setProperty(setPropertyGVO, (HasWidgets)widget);
					}
				}
			}
		}
	}

	private void setProperty(SetPropertyGVO setPropertyGVO, HasWidgets widget) {
		for (Widget childWidget : widget) {
			setProperty(setPropertyGVO, childWidget);
			if (childWidget instanceof HasWidgets) {
				setProperty(setPropertyGVO, (HasWidgets)childWidget);
			}
		}
	}

    // CHECKSTYLE.OFF: CyclomaticComplexity
	private void setProperty(SetPropertyGVO setPropertyGVO, UIObject widget) {
		if (widget == null) {
			return;
		}
		String propertyName = setPropertyGVO.getProperty();
		if (QAMLConstants.PROPERTY_VISIBLE.equals(propertyName)) {
			if (widget instanceof HasVisibility) {
				boolean visible = Boolean.valueOf(setPropertyGVO.getValue());
				HasVisibility hasVisibility = (HasVisibility)widget;
				hasVisibility.setVisible(visible);
			}
		} else if (QAMLConstants.PROPERTY_EDITABLE.equals(propertyName)) {
			if (widget instanceof IsEditable) {
				boolean editable = Boolean.valueOf(setPropertyGVO.getValue());
				IsEditable isEditable = (IsEditable)widget;
				isEditable.setEditable(editable);
			}
		} else if (QAMLConstants.PROPERTY_ENABLED.equals(propertyName)) {
			if (widget instanceof HasEnabled) {
				boolean enabled = Boolean.valueOf(setPropertyGVO.getValue());
				HasEnabled hasEnabled = (HasEnabled)widget;
				hasEnabled.setEnabled(enabled);
			}
		} else if (QAMLConstants.PROPERTY_HEIGHT.equals(propertyName)) {
			String height = setPropertyGVO.getValue();
			widget.setHeight(height);
		} else if (QAMLConstants.PROPERTY_WIDTH.equals(propertyName)) {
			String width = setPropertyGVO.getValue();
			widget.setWidth(width);
		} else if (QAMLConstants.PROPERTY_DISPLAYNAME.equals(propertyName)) {
			if (widget instanceof HasDisplayname) {
				HasDisplayname hasDisplayname = (HasDisplayname)widget;
				hasDisplayname.setDisplayname(setPropertyGVO.getValue());
			}
		} else if (QAMLConstants.PROPERTY_SELECTED.equals(propertyName)) {
			if (widget instanceof HasSelection) {
				HasSelection hasSelection = (HasSelection)widget;
				hasSelection.setSelected(setPropertyGVO.getValue());
			}
		} else if (QAMLConstants.PROPERTY_SELECTED_ROW.equals(propertyName)) {
			if (widget instanceof HasIndexSelection) {
				int selectedIndex = QAMLUtil.toInteger(setPropertyGVO.getValue());
				HasIndexSelection hasIndexSelection = (HasIndexSelection)widget;
				hasIndexSelection.setSelectedIndex(selectedIndex);
			}
		} else if (QAMLConstants.PROPERTY_CURRENT_PAGE.equals(propertyName)) {
			if (widget instanceof HasPaging) {
				int currentPage = QAMLUtil.toInteger(setPropertyGVO.getValue());
				HasPaging hasPaging = (HasPaging)widget;
				hasPaging.setCurrentPage(currentPage);
			}
		} else if (QAMLConstants.PROPERTY_PAGESIZE.equals(propertyName)) {
			if (widget instanceof HasPaging) {
				int pageSize = QAMLUtil.toInteger(setPropertyGVO.getValue());
				HasPaging hasPaging = (HasPaging)widget;
				hasPaging.setPageSize(pageSize);
			}
		} else if (QAMLConstants.PROPERTY_MIN_TICKS.equals(propertyName)) {
			if (widget instanceof HasRange) {
				int minValue = QAMLUtil.toInteger(setPropertyGVO.getValue());
				HasRange hasRange = (HasRange)widget;
				hasRange.setMinValue(minValue);
			}
		} else if (QAMLConstants.PROPERTY_MAX_TICKS.equals(propertyName)) {
			if (widget instanceof HasRange) {
				int maxValue = QAMLUtil.toInteger(setPropertyGVO.getValue());
				HasRange hasRange = (HasRange)widget;
				hasRange.setMaxValue(maxValue);
			}
		} else if (QAMLConstants.PROPERTY_TICKSIZE.equals(propertyName)) {
			if (widget instanceof HasRange) {
				int stepValue = QAMLUtil.toInteger(setPropertyGVO.getValue());
				HasRange hasRange = (HasRange)widget;
				hasRange.setStepValue(stepValue);
			}
		} else if (QAMLConstants.PROPERTY_TICK_LABELS.equals(propertyName)) {
			// Do nothing
		}
	}
    // CHECKSTYLE.ON: CyclomaticComplexity

	private boolean hasEffectOnChild(SetPropertyGVO setPropertyGVO,	HasWidgets widget) {
		if (widget == null) {
			return false;
		}
		String propertyName = setPropertyGVO.getProperty();
		if (QAMLConstants.PROPERTY_HEIGHT.equals(propertyName)) {
			return false;
		}
		if (QAMLConstants.PROPERTY_WIDTH.equals(propertyName)) {
			return false;
		}
		if (QAMLConstants.PROPERTY_TOOLTIP.equals(propertyName)) {
			return false;
		}
		if (QAMLConstants.PROPERTY_DISPLAYNAME.equals(propertyName)) {
			return false;
		}
		if (QAMLConstants.PROPERTY_VISIBLE.equals(propertyName)) {
			return false;
		}
		if (QAMLConstants.PROPERTY_ENABLED.equals(propertyName)) {
			return false;
		}
		return true;
	}
}