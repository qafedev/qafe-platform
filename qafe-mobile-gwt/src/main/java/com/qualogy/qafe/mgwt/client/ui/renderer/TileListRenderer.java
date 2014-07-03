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
import java.util.Map;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.qualogy.qafe.mgwt.client.activities.AbstractActivity;
import com.qualogy.qafe.mgwt.client.activities.ActivityHelper;
import com.qualogy.qafe.mgwt.client.ui.component.HasData;
import com.qualogy.qafe.mgwt.client.ui.component.QTileList;
import com.qualogy.qafe.mgwt.client.ui.events.CellRenderEvent;
import com.qualogy.qafe.mgwt.client.ui.events.CellRenderHandler;
import com.qualogy.qafe.mgwt.client.ui.events.DataChangeEvent;
import com.qualogy.qafe.mgwt.client.ui.events.DataChangeHandler;
import com.qualogy.qafe.mgwt.client.ui.events.HasDataChangeHandlers;
import com.qualogy.qafe.mgwt.client.ui.events.NotifyHandler;
import com.qualogy.qafe.mgwt.client.vo.ui.ComponentGVO;
import com.qualogy.qafe.mgwt.client.vo.ui.TileListGVO;
import com.qualogy.qafe.mgwt.shared.QAMLConstants;

public class TileListRenderer extends AbstractComponentRenderer {

	public UIObject render(ComponentGVO component, String owner, String uuid, String parent, String context, AbstractActivity activity) {
		UIObject widget = null;
		if (component instanceof TileListGVO) {
			TileListGVO tileListGVO = (TileListGVO)component;
			QTileList tileList = new QTileList();			
			init(tileListGVO, tileList, owner, uuid, parent, context, activity);
			widget = tileList;
		}
		registerComponent(component, widget, owner, parent, context);
		return widget;
	}
	
	@Override
	protected void init(ComponentGVO component, UIObject widget, String owner, String uuid, String parent, String context, AbstractActivity activity) {
		super.init(component, widget, owner, uuid, parent, context, activity);
		TileListGVO tileListGVO = (TileListGVO)component;
		QTileList tileList = (QTileList)widget;
		int colCount = tileListGVO.getColumns();
		tileList.setColCount(colCount);
		registerCellRender(tileListGVO, tileList, uuid, parent, context, activity);
	}

	private void registerCellRender(final TileListGVO component, final QTileList widget, final String uuid, final String parent, final String context, final AbstractActivity activity) {
		widget.registerCellRenderHandler(new CellRenderHandler() {
			@Override
			public UIObject doCellRender(CellRenderEvent event) {
				Object cellModel = event.getModel();
				if (!(cellModel instanceof Map)) {
					return null;
				}
				int index = event.getIndex();
				String owner = component.getId() + QAMLConstants.INDEXING + index;
				UIObject cellWidget = renderChildComponent(component.getComponent(), owner, uuid, parent, context, activity);
				initCell(widget, cellWidget, cellModel, owner, parent, context, activity);
				return cellWidget;
			}
		});
	}
	
	private void initCell(QTileList widget, UIObject cellWidget, Object cellValue, String owner, String parent, String context, AbstractActivity activity) {
		if (cellWidget == null) {
			return;
		}
		
		// Register events for components within the tile
		String viewKey = activity.getClientFactory().generateViewKey(parent, context);
		Map<UIObject,ComponentGVO> cellComponents = activity.getClientFactory().getComponents(viewKey, owner);
		registerCellEvents(cellComponents, widget, parent, context, activity);
		
		// Set value to the tile
		if (cellWidget instanceof HasWidgets) {
			ActivityHelper.setValue((HasWidgets)cellWidget, cellValue, null, null, null, cellComponents);
		} else if (cellWidget instanceof HasData) {
			ActivityHelper.setValue((HasData)cellWidget, cellValue, null, null, null);
		}
	}

	private void registerCellEvents(Map<UIObject,ComponentGVO> components, final NotifyHandler notifyHandler, String windowId, String context, AbstractActivity activity) {
		if (components == null) {
			return;
		}
		Iterator<UIObject> itrWidget = components.keySet().iterator();
		while (itrWidget.hasNext()) {
			UIObject widget = itrWidget.next();
			ComponentGVO componentGVO = components.get(widget);
			ActivityHelper.registerEvents(componentGVO, widget, notifyHandler, windowId, context, activity);
			
			// When an item in a cell is changed notify the parent to update the model 
			if (widget instanceof HasDataChangeHandlers) {
				HasDataChangeHandlers hasDataChangeHandlers = (HasDataChangeHandlers)widget;
				hasDataChangeHandlers.addDataChangeHandler(new DataChangeHandler() {
					@Override
					public void onDataChange(DataChangeEvent event) {
						ActivityHelper.notify(notifyHandler, event.getSource(), QAMLConstants.EVENT_ONCHANGE);
					}
				});
			}
		}
	}
}
