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
import java.util.Map;

import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;
import com.qualogy.qafe.gwt.client.component.DataMap;
import com.qualogy.qafe.gwt.client.ui.renderer.RendererHelper;
import com.qualogy.qafe.gwt.client.ui.renderer.events.CallbackHandler;
import com.qualogy.qafe.gwt.client.util.QAMLUtil;
import com.qualogy.qafe.gwt.client.vo.functions.BuiltInFunctionGVO;
import com.qualogy.qafe.gwt.client.vo.functions.DataContainerGVO;
import com.qualogy.qafe.gwt.client.vo.functions.UpdateModelGVO;

public class UpdateModelExecute implements InternalExecuteCommand {

	@Override
	public void execute(BuiltInFunctionGVO builtInFunction) {
		if (builtInFunction instanceof UpdateModelGVO) {
			UpdateModelGVO updateModelGVO = (UpdateModelGVO)builtInFunction;
			String component = updateModelGVO.getRef();
			List<UIObject> uiObjects = RendererHelper.getComponent(component);
			if (uiObjects == null) {
				uiObjects = RendererHelper.getNamedComponent(component);
			}
			if (uiObjects !=null) {
				String updateAction = updateModelGVO.getUpdateAction();
				for (UIObject uiObject : uiObjects) {
					if (DataContainerGVO.QAFE_CHECKSUM.equals(updateAction)) {
						try {
							DataContainerGVO dataContainer = CallbackHandler.createDataContainer(component, uiObject, null);
							if ((dataContainer != null) && dataContainer.isMap()) {
								updateChecksum(uiObject, dataContainer.getDataMap());
							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
		}
		FunctionsExecutor.setProcessedBuiltIn(true);
	}
	
	private void updateChecksum(UIObject uiObject, DataMap dataMap) {
		UIObject component = getComponentByName(uiObject, DataContainerGVO.QAFE_CHECKSUM);
		if (component != null) {
			int checksum = 0;
			for (Map.Entry<String, DataContainerGVO> field : dataMap.entrySet()) {
				if (!isMetaData(field.getKey())) {
					Object value = field.getValue().createType();
					checksum += QAMLUtil.calculateChecksum(field.getKey(), value);
				}
			}
			if (component instanceof HasText) {
				HasText hasText = (HasText)component;
				hasText.setText(String.valueOf(checksum));
			}
		}
	}

	private boolean isMetaData(String key) {
		return DataContainerGVO.QAFE_CHECKSUM.equals(key);
	}
	
	private UIObject getComponentByName(UIObject uiObject, String name) {
		if (RendererHelper.isNamedComponent(uiObject)) {
			String componentName = RendererHelper.getNamedComponentName(uiObject);
			if (componentName.equals(name)) {
				return uiObject;
			}
		}
		if (uiObject instanceof HasWidgets) {
			HasWidgets hasWidgets = (HasWidgets)uiObject;
			for (Widget widget : hasWidgets) {
				UIObject component = getComponentByName(widget, name);
				if (component != null) {
					return component;
				}
			}
		}
		return null;
	}
}