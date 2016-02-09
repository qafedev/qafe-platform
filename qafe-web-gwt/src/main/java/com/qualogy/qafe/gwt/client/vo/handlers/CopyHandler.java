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
package com.qualogy.qafe.gwt.client.vo.handlers;

import java.util.List;
import java.util.Map;
import java.util.Queue;

import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.UIObject;
import com.qualogy.qafe.gwt.client.util.ComponentRepository;
import com.qualogy.qafe.gwt.client.vo.functions.BuiltInFunctionGVO;
import com.qualogy.qafe.gwt.client.vo.functions.CopyGVO;

public class CopyHandler extends AbstractBuiltInHandler {

	@Override
	public BuiltInState executeBuiltIn(UIObject sender, String listenerType,
			Map<String, String> mouseInfo, BuiltInFunctionGVO builtInGVO,
			String appId, String windowId, String eventSessionId,
			Queue derivedBuiltIns) {
		CopyGVO copyGVO = (CopyGVO) builtInGVO;
		copy(copyGVO , sender, appId, windowId, eventSessionId);
		return BuiltInState.EXECUTED;
	}
	
	private void copy(CopyGVO copyGVO, UIObject sender, String appId, String windowId, String eventSessionId) {
		String toKey = generateId(copyGVO.getTo(), windowId, appId, eventSessionId);
		String fromKey = generateId(copyGVO.getFrom(), windowId, appId, eventSessionId);
		
		List<UIObject> uiObjectsFrom = getUIObjects(fromKey);
		if (uiObjectsFrom == null) {
			return;
		}
		
		List<UIObject> uiObjectsTo = getUIObjects(toKey);
		if (uiObjectsTo == null) {
			return;
		}
		if (uiObjectsTo.size()==uiObjectsFrom.size()){
			for (int i=0;i<uiObjectsTo.size();i++){
				UIObject uiObjectFrom = uiObjectsFrom.get(i);
				UIObject uiObjectTo = uiObjectsTo.get(i);
				
				if (uiObjectFrom instanceof HasText) {
					HasText hasTextFrom = (HasText) uiObjectFrom;
					String fromValue = hasTextFrom.getText();
					if (uiObjectTo instanceof HasText) {
						HasText hasTextTo = (HasText) uiObjectTo;
						hasTextTo.setText(fromValue);
					}
				}
			
			}
		}
	}
}
