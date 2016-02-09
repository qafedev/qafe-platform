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
package com.qualogy.qafe.gwt.client.vo.functions.execute;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.UIObject;
import com.qualogy.qafe.gwt.client.component.HasDataGridMethods;
import com.qualogy.qafe.gwt.client.ui.renderer.RendererHelper;
import com.qualogy.qafe.gwt.client.util.QAMLConstants;

public abstract class AbstractBuiltInExecute {

	protected List<UIObject> getParentUIObjects(String component) {
		String parentComponent = component.replaceFirst("\\.[\\w\\$]+\\|", "|").replaceFirst("\\[.+\\]", "");
		return RendererHelper.getComponent(parentComponent);
	}

	protected int getRowIndex(String component, HasDataGridMethods hasDataGridMethods) {
		int rowIndex = -1;
		int indexPrefix = component.indexOf("[");
		if (indexPrefix > -1) {
			int indexPostfix = component.indexOf("]");
			String selectedIndex = component.substring(indexPrefix + 1, indexPostfix);
			rowIndex = hasDataGridMethods.getRowIndex(selectedIndex);
		}
		return rowIndex;
	}
	
	protected List<UIObject> collectCellUIObjects(String component, int rowIndex, List<UIObject> cellUIObjects) {
		if (cellUIObjects == null) {
			cellUIObjects = new ArrayList<UIObject>();
		}
		component = component.replaceFirst("\\[.+\\]", "");
		boolean rowSelection = (rowIndex > -1);
		while (true) {
			String cellUUID = QAMLConstants.TOKEN_INDEXING + rowIndex + QAMLConstants.TOKEN_INDEXING + component;  
			List<UIObject> uiObjects = RendererHelper.getComponent(cellUUID);
			if (uiObjects != null) {
				cellUIObjects.addAll(uiObjects);
			}
			if (rowSelection || (uiObjects == null)) {
				break;
			}
			rowIndex++;
		}
		return cellUIObjects;
	}
}