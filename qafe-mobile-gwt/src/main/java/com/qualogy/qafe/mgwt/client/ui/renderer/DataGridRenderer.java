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

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.dom.client.Node;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.UIObject;
import com.qualogy.qafe.mgwt.client.activities.AbstractActivity;
import com.qualogy.qafe.mgwt.client.ui.component.QDataGrid;
import com.qualogy.qafe.mgwt.client.ui.component.QLabel;
import com.qualogy.qafe.mgwt.client.ui.component.QLayoutAuto;
import com.qualogy.qafe.mgwt.client.ui.events.CellRenderEvent;
import com.qualogy.qafe.mgwt.client.ui.events.CellRenderHandler;
import com.qualogy.qafe.mgwt.client.ui.events.PostRenderEvent;
import com.qualogy.qafe.mgwt.client.ui.events.PostRenderHandler;
import com.qualogy.qafe.mgwt.client.vo.ui.ComponentGVO;
import com.qualogy.qafe.mgwt.client.vo.ui.DataGridColumnGVO;
import com.qualogy.qafe.mgwt.client.vo.ui.DataGridGVO;
import com.qualogy.qafe.mgwt.shared.QAMLConstants;

public class DataGridRenderer extends AbstractEditableComponentRenderer {

	public UIObject render(ComponentGVO component, String owner, String uuid, String parent, String context, AbstractActivity activity) {
		UIObject widget = null;
		if (component instanceof DataGridGVO) {
			DataGridGVO dataGridGVO = (DataGridGVO)component;
			QDataGrid dataGrid = new QDataGrid();
			init(dataGridGVO, dataGrid, owner, uuid, parent, context, activity);	
			widget = dataGrid;
		}
		registerComponent(component, widget, owner, parent, context);
		return widget;
	}
	
	@Override
	protected void init(ComponentGVO component, UIObject widget, String owner, String uuid, String parent, String context, AbstractActivity activity) {
		super.init(component, widget, owner, uuid, parent, context, activity);
		DataGridGVO dataGridGVO = (DataGridGVO)component;
		QDataGrid dataGrid = (QDataGrid)widget;
		if (dataGridGVO.getPageSize() != null) {
			dataGrid.setPageSize(dataGridGVO.getPageSize());
		}
		renderChildren(dataGridGVO, dataGrid, uuid, parent, context);
		registerCellRender(dataGridGVO, dataGrid, uuid, parent, context);
		registerPostRender(dataGridGVO, dataGrid, uuid, parent, context);
	}
	
	@Override
	protected void initSize(ComponentGVO component, UIObject widget, String uuid, String parent, String context) {
		// Do nothing, should be stretched
	}
	
	private void renderChildren(DataGridGVO component, QDataGrid widget, String uuid, String parent, String context) {
		DataGridColumnGVO[] dataGridColumns = component.getColumns();
		if (dataGridColumns != null) {
			for (int i=0; i<dataGridColumns.length; i++) {
				DataGridColumnGVO dataGridColumnGVO = dataGridColumns[i];
				String columnId = dataGridColumnGVO.getId();
				widget.addColumn(columnId);
			}
		}
	}
	
	private void registerCellRender(final DataGridGVO component, final QDataGrid widget, final String uuid, final String parent, final String context) {
		widget.registerCellRenderHandler(new CellRenderHandler() {
			@Override
			public UIObject doCellRender(CellRenderEvent event) {
				Object model = event.getModel();
				if (!(model instanceof Map)) {
					return null;
				}
				Map map = (Map)model;
				
				// Determine the data members, based on columns or keys in the map
				Map<String,String> columns = new LinkedHashMap<String,String>();
				DataGridColumnGVO[] dataGridColumns = component.getColumns();
				if ((dataGridColumns != null) && (dataGridColumns.length > 0)) {
					for (int i=0; i<dataGridColumns.length; i++) {
						DataGridColumnGVO dataGridColumnGVO = dataGridColumns[i];
						String columnName = dataGridColumnGVO.getFieldName();
						String columnLabel = dataGridColumnGVO.getDisplayname();
						if (columnLabel == null) {
							columnLabel = columnName;
						}
						columns.put(columnName, columnLabel);
					}
				} else {
					Iterator<Object> itrKey = map.keySet().iterator();
					while (itrKey.hasNext()) {
						String columnName = (String)itrKey.next();
						if (QAMLConstants.INTERNAL_DATA_FQN.equals(columnName)) {
							continue;
						}
						columns.put(columnName, columnName);
					}
				}
				
				// Creating labels for column names and values in gridlayout
				QLayoutAuto cellWidget = null;
				if (columns.size() > 0) {
					cellWidget = new QLayoutAuto();
					int rowIndex = 0;
					Iterator<String> itrColumnName = columns.keySet().iterator();
					while (itrColumnName.hasNext()) {
						String columnName = itrColumnName.next();
						String columnLabel = columns.get(columnName);
						String columnValue = "";
						if (map.containsKey(columnName)) {
							Object value = map.get(columnName);
							if (value != null) {
								columnValue = value.toString();
							}
						}
						QLabel colLabel = new QLabel(columnLabel, true);
						QLabel colValue = new QLabel(columnValue);
						cellWidget.setWidget(rowIndex, 0, colLabel);
						cellWidget.setWidget(rowIndex, 1, colValue);
						rowIndex++;
					}
				}
				return cellWidget;
			}
		});
	}
	
	private void registerPostRender(final DataGridGVO component, final QDataGrid widget, final String uuid, final String parent, final String context) {
		if (!hasConditionalStyle(component)) {
			return;
		}
		widget.addPostRenderHandler(new PostRenderHandler() {
			@Override
			public void onPostRender(PostRenderEvent event) {
				List model = event.getModel();
				if (model == null) {
					return;
				}
				DataGridColumnGVO[] dataGridColumns = component.getColumns();
				for (int i=0; i<model.size(); i++) {
					Object data = model.get(i);
					if (!(data instanceof Map)) {
						continue;
					}
					Map dataModel = (Map)data;
					Node node = widget.getElement().getChild(i);
					Element childElement = (Element)Element.as(node);
					for (int j=0; j<dataGridColumns.length; j++) {
						DataGridColumnGVO dataGridColumnGVO = dataGridColumns[j];
						if (dataGridColumnGVO.getConditionalStyleRef() != null) {
							String columnName = dataGridColumnGVO.getFieldName();
							Object value = dataModel.get(columnName);
							if (value != null) {
								value = value.toString();
							}
							handleConditonalStyle(dataGridColumnGVO, childElement, value);
						}
					}
				}
			}
		});
	}
	
	private boolean hasConditionalStyle(DataGridGVO component) {
		final DataGridColumnGVO[] dataGridColumns = component.getColumns();
		if ((dataGridColumns == null) || (dataGridColumns.length == 0)) {
			return false;
		}
		for (int i=0; i<dataGridColumns.length; i++) {
			DataGridColumnGVO dataGridColumnGVO = dataGridColumns[i];
			if (dataGridColumnGVO.getConditionalStyleRef() != null) {
				return true;
			}
		}
		return false;
	}
}
