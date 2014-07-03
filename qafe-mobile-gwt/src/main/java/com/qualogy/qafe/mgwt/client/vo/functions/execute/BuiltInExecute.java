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
package com.qualogy.qafe.mgwt.client.vo.functions.execute;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.ui.UIObject;
import com.qualogy.qafe.mgwt.client.activities.AbstractActivity;
import com.qualogy.qafe.mgwt.client.activities.ActivityHelper;
import com.qualogy.qafe.mgwt.client.activities.WindowActivity;
import com.qualogy.qafe.mgwt.client.vo.functions.BuiltInComponentGVO;
import com.qualogy.qafe.mgwt.client.vo.ui.ComponentGVO;
import com.qualogy.qafe.mgwt.shared.QAMLUtil;

public abstract class BuiltInExecute implements ExecuteCommand {

	protected List<UIObject> getWidgets(BuiltInComponentGVO builtInComponentGVO, WindowActivity activity) {
		if (builtInComponentGVO == null) {
			return null;
		}
		List<UIObject> widgets = null;
		String componentKey = builtInComponentGVO.getComponentIdUUID();
		if (componentKey != null) {
			widgets = activity.getClientFactory().getComponentById(componentKey);
			if (QAMLUtil.isEmpty(widgets)) {
				widgets = activity.getClientFactory().getComponentByName(componentKey);
			}
		} else {
			componentKey = builtInComponentGVO.getComponentName();
			widgets = activity.getClientFactory().getComponentByName(componentKey);
		}
		return widgets;
	}
	
	protected void registerEvents(Map<UIObject,ComponentGVO> components, String windowId, String context, AbstractActivity activity) {
		if (components == null) {
			return;
		}
		Iterator<UIObject> itrWidget = components.keySet().iterator();
		while (itrWidget.hasNext()) {
			UIObject widget = itrWidget.next();
			ComponentGVO componentGVO = components.get(widget);
			ActivityHelper.registerEvents(componentGVO, widget, windowId, context, activity);
		}
	}
}
