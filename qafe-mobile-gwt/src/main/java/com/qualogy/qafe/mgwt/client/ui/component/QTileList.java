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
package com.qualogy.qafe.mgwt.client.ui.component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;
import com.qualogy.qafe.mgwt.client.component.DataMap;
import com.qualogy.qafe.mgwt.client.ui.events.CellRenderEvent;
import com.qualogy.qafe.mgwt.client.ui.events.CellRenderHandler;
import com.qualogy.qafe.mgwt.client.ui.events.DataChangeHandler;
import com.qualogy.qafe.mgwt.client.ui.events.HasCellRenderHandler;
import com.qualogy.qafe.mgwt.client.ui.events.HasScrollBottomHandlers;
import com.qualogy.qafe.mgwt.client.ui.events.NotifyEvent;
import com.qualogy.qafe.mgwt.client.ui.events.NotifyHandler;
import com.qualogy.qafe.mgwt.client.ui.events.ScrollBottomEvent;
import com.qualogy.qafe.mgwt.client.ui.events.ScrollBottomHandler;
import com.qualogy.qafe.mgwt.shared.QAMLConstants;

public class QTileList extends FlexTable implements HasData, HasCellRenderHandler, HasScrollBottomHandlers, NotifyHandler {

	private CellRenderHandler cellRenderHandler;
	private List models = new ArrayList();
	private Map<UIObject,Object> cellWidgetModels = new HashMap<UIObject,Object>();
	private int colCount;
	private int selectedIndex = -1;
	private String dataName;
	private HandlerRegistration scrollHandlerRegistration;
	
	public QTileList() {
		setWidth("100%");
		setHeight("100%");
	}
	
	public int getColCount() {
		return colCount;
	}

	public void setColCount(int colCount) {
		this.colCount = colCount;
	}

	@Override
	public void registerCellRenderHandler(CellRenderHandler handler) {
		this.cellRenderHandler = handler;
	}

	@Override
	public HandlerRegistration addDataChangeHandler(DataChangeHandler handler) {
		return ComponentHelper.addDataChangeHandler(this, handler);
	}

	@Override
	public HandlerRegistration addScrollBottomHandler(ScrollBottomHandler handler) {
		return addHandler(handler, ScrollBottomEvent.getType());
	}
	
	@Override
	public void doNotify(NotifyEvent event) {
		UIObject source = event.getSource();
		if (source instanceof Widget) {
			Widget widget = (Widget)source;
			updateSelectedIndex(widget);
			updateDataModel(widget, event.getListenerType());
		}
	}
	
	private void updateSelectedIndex(Widget source) {
		if (cellWidgetModels.containsKey(source)) {
			Object model = cellWidgetModels.get(source);
			selectedIndex = models.indexOf(model);
		} else {
			Widget tile = source;
			while ((tile != null) && (tile.getParent() != this)) {
				tile = tile.getParent();
			}
			if (tile != source) {
				updateSelectedIndex(tile);
			}
		}
	}
	
	private void updateDataModel(Widget source, String listenerType) {
		if (!QAMLConstants.EVENT_ONCHANGE.equals(listenerType)) {
			return;
		}
		if (!(source instanceof HasData)) {
			return;
		}
		if (selectedIndex == -1) {
			return;
		}
		HasData hasData = (HasData)source;
		String dataName = hasData.getDataName();
		Object newData = hasData.getData();
		Object dataModel = models.get(selectedIndex);
		if (dataModel instanceof String) {
			models.set(selectedIndex, newData);
		} else if (dataModel instanceof Map) {
			Map map = (Map)dataModel;
			if (map.containsKey(dataName)) {
				if (map instanceof DataMap) {
					DataMap dataMap = (DataMap)map;
					dataMap.put(dataName, newData);
				} else {
					map.put(dataName, newData);	
				}
			}
		}
	}
	
	@Override
	public void setDataName(String dataName) {
		this.dataName = dataName;
	}

	@Override
	public String getDataName() {
		return dataName;
	}
	
	@Override
	public void setData(Object data) {
		clear();
		if (data instanceof Map) {
			models.add(data);
		} else if (data instanceof List) {
			models.addAll((List)data);
		}
		render();
		ComponentHelper.fireDataChange(this, models, null);
	}

	@Override
	public Object getData() {
		if (selectedIndex == -1) {
			return null;	
		}
		if (selectedIndex > (models.size() -1)) {
			return null;	
		}
		Object data = models.get(selectedIndex);
		return data;
	}

	@Override
	public Object getDataModel() {
		return getData();
	}

	@Override
	public Object getModel() {
		List model = null;
		if (models.size() > 0) {
			model = new ArrayList();
			model.addAll(models);
		}
		return model;
	}
	
	@Override
	public void clear() {
		super.clear();
		models.clear();
		cellWidgetModels.clear();
		selectedIndex = -1;
	}
	 
	private void render() {
		int rowIndex = 0;
		int colIndex = 0;
		for (int i=0; i<models.size(); i++) {
			Object model = models.get(i);
			UIObject cellWidget = renderCell(model);
			if (cellWidget instanceof Widget) {
				if (colIndex >= getColCount()) {
					colIndex = 0;
					rowIndex++;
				}
				setWidget(rowIndex, colIndex, (Widget)cellWidget);
				cellWidgetModels.put(cellWidget, model);
				colIndex++;
			}
		}
		ComponentHelper.refreshScroll(this);
	}
	
	private UIObject renderCell(Object model) {
		UIObject cellWidget = null;
		if (cellRenderHandler != null) {
			int index = models.indexOf(model);
			CellRenderEvent event = new CellRenderEvent(this, model, index);
			cellWidget = (UIObject)event.execute(cellRenderHandler);
		}
		return cellWidget;
	}
	
	@Override
	protected void onAttach() {
		super.onAttach();
		scrollHandlerRegistration = ComponentHelper.registerScroll(this);
	}
	
	@Override
	protected void onDetach() {
		ComponentHelper.unregisterScroll(scrollHandlerRegistration);
		super.onDetach();
	}
}