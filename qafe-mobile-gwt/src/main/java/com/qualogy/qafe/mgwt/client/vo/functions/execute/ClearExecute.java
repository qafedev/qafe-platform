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
package com.qualogy.qafe.mgwt.client.vo.functions.execute;

import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.qualogy.qafe.mgwt.client.activities.AbstractActivity;
import com.qualogy.qafe.mgwt.client.activities.WindowActivity;
import com.qualogy.qafe.mgwt.client.places.WindowPlace;
import com.qualogy.qafe.mgwt.client.ui.component.HasData;
import com.qualogy.qafe.mgwt.client.ui.component.IsEditable;
import com.qualogy.qafe.mgwt.client.util.ComponentRepository;
import com.qualogy.qafe.mgwt.client.vo.functions.BuiltInComponentGVO;
import com.qualogy.qafe.mgwt.client.vo.functions.BuiltInFunctionGVO;
import com.qualogy.qafe.mgwt.client.vo.functions.ClearGVO;
import com.qualogy.qafe.mgwt.client.vo.ui.ComponentGVO;
import com.qualogy.qafe.mgwt.shared.QAMLUtil;

public class ClearExecute extends BuiltInExecute {
	
	public void execute(BuiltInFunctionGVO builtInFunctionGVO, AbstractActivity activity) {
		if (builtInFunctionGVO instanceof ClearGVO) {
			ClearGVO clearGVO = (ClearGVO)builtInFunctionGVO;
			if (activity instanceof WindowActivity) {
				WindowActivity windowActivity = (WindowActivity)activity;
				clear(clearGVO, windowActivity);
			}
		}
		FunctionsExecutor.setProcessedBuiltIn(true);
	}
	
	private void clear(ClearGVO clearGVO, WindowActivity activity) {
		WindowPlace windowPlace = activity.getPlace();
		String context = windowPlace.getContext();
		String windowId = windowPlace.getId();
		String viewKey = ComponentRepository.getInstance().generateViewKey(windowId, context);
		Map<UIObject,ComponentGVO> components = ComponentRepository.getInstance().getComponents(viewKey);
		if (components == null) {
			return;
		}
		
		BuiltInComponentGVO builtInComponentGVO = clearGVO.getBuiltInComponentGVO();
		String componentKey = builtInComponentGVO.getComponentIdUUID();
		List<UIObject> widgets = activity.getClientFactory().getComponentById(componentKey);
		if (QAMLUtil.isEmpty(widgets)) {
			widgets = activity.getClientFactory().getComponentByName(componentKey);
			if (QAMLUtil.isEmpty(widgets)) {
				widgets = activity.getClientFactory().getComponentByGroup(componentKey);
			}
			clear(widgets, components);
		} else {
			for (UIObject widget : widgets) {
				clear(widget, components, true, false);
			}			
		}
	}
	
	private void clear(Iterable<? extends UIObject> widget, Map<UIObject,ComponentGVO> components) {
		if (widget == null) {
			return;
		}
		for (UIObject childWidget : widget) {
			if (childWidget instanceof HasWidgets) {
				clear((HasWidgets)childWidget, components);
			} else {
				clear(childWidget, components, false, true);				
			}
		}
	}
	
	/*
	 * Rules:
	 * The ref attribute references to the:
	 * 	- id of: 
			an editable component (like a TextField): the value will be cleared
			a non-editable component (like a Label): won't do anything
			a containment component (like a Panel): the value of all editable child components will be cleared 
		- name of :
			an editable component (like a TextField): the value will be cleared
			a non-editable component (like a Label): the displayname will be cleared 
			a container component (like a Panel): the value or displayname of all child components with the name attribute will be cleared
	 */
	private void clear(UIObject widget, Map<UIObject,ComponentGVO> components, boolean onlyEditable, boolean named) {
		if (onlyEditable) {
			if (widget instanceof IsEditable) {
				IsEditable editable = (IsEditable)widget;
				editable.setData(null);
			} else if (widget instanceof HasWidgets) {
				HasWidgets hasWidgets = (HasWidgets)widget;
				for (UIObject childWidget : hasWidgets) {
					clear(childWidget, components, onlyEditable, false);
				}
			}
			return;
		}
		if (widget instanceof HasData) { 
			if (named) {
				ComponentGVO componentGVO = components.get(widget);
				if (componentGVO == null) {
					return;
				}
				String componentName = componentGVO.getFieldName();
				if (QAMLUtil.isEmpty(componentName)) {
					return;
				}
			}
			HasData hasData = (HasData)widget;
			hasData.setData(null);
		} else if (widget instanceof HasWidgets) {
			HasWidgets hasWidgets = (HasWidgets)widget;
			for (UIObject childWidget : hasWidgets) {
				clear(childWidget, components, onlyEditable, true);
			}
		}
	}
}
