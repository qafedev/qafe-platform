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
package com.qualogy.qafe.gwt.client.vo.functions.execute;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;
import com.qualogy.qafe.gwt.client.component.HasDataGridMethods;
import com.qualogy.qafe.gwt.client.ui.renderer.RendererHelper;
import com.qualogy.qafe.gwt.client.vo.functions.BuiltInComponentGVO;
import com.qualogy.qafe.gwt.client.vo.functions.BuiltInFunctionGVO;
import com.qualogy.qafe.gwt.client.vo.functions.FocusGVO;

@Deprecated
public class FocusExecute extends AbstractBuiltInExecute implements ExecuteCommand {

	public void execute(BuiltInFunctionGVO builtInFunction) {
		if (builtInFunction instanceof FocusGVO) {
			FocusGVO focus = (FocusGVO) builtInFunction;
			BuiltInComponentGVO builtInComponentGVO = focus.getBuiltInComponentGVO();
			String component = builtInComponentGVO.getComponentIdUUID();
			List<UIObject> uiObjects = null;
			if (component != null) {
				uiObjects = getUIObjects(component, focus);
			} else {
				uiObjects = RendererHelper.getNamedComponent(builtInComponentGVO.getComponentName());
			}

			if (uiObjects != null) {
				for (UIObject uiObject : uiObjects) {
					if (uiObject instanceof Widget) {
						Widget w = (Widget) uiObject;
						makeParentsVisible(w);
//						TO BE REMOVED						
//						if (w.getParent() != null) {
//							Widget parent = w.getParent();
//							makeParentsVisible(parent);
//						}
					}
					if (uiObject instanceof Focusable) {
						((Focusable) uiObject).setFocus(true);
					}
				}
			}
		}

		FunctionsExecutor.setProcessedBuiltIn(true);

	}

	private void makeParentsVisible(Widget w) {
		if (w.getParent() != null) {
			Widget parent = w.getParent();
			makeParentsVisible(parent);
			if (parent instanceof DeckPanel) {
				DeckPanel dp = (DeckPanel) parent;
				dp.showWidget(dp.getWidgetIndex(w));
				((TabPanel) dp.getParent().getParent()).selectTab(dp.getWidgetIndex(w));
			}
		}
	}
	
	private List<UIObject> getUIObjects(String uuid, FocusGVO focus) {
		List<UIObject> uiObjects = RendererHelper.getComponent(uuid);
		if (uiObjects == null) {
			List<UIObject> parentUIObjects = getParentUIObjects(uuid);
			if (parentUIObjects != null) {
				uiObjects = new ArrayList<UIObject>();
				for (UIObject parentUIObject : parentUIObjects) {
					if (parentUIObject instanceof HasDataGridMethods) {
						HasDataGridMethods hasDataGridMethods = (HasDataGridMethods)parentUIObject;						
						int rowIndex = getRowIndex(uuid, hasDataGridMethods);
						uiObjects = collectCellUIObjects(uuid, rowIndex, uiObjects);
					}
				}
			}
		}
		return uiObjects;
	}
}