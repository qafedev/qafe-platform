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
package com.qualogy.qafe.mgwt.client.ui.component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.UIObject;
import com.googlecode.mgwt.ui.client.widget.CellList;
import com.googlecode.mgwt.ui.client.widget.celllist.Cell;
import com.googlecode.mgwt.ui.client.widget.celllist.CellSelectedEvent;
import com.googlecode.mgwt.ui.client.widget.celllist.CellSelectedHandler;
import com.qualogy.qafe.mgwt.client.ui.events.CellItemSelectEvent;
import com.qualogy.qafe.mgwt.client.ui.events.CellItemSelectHandler;
import com.qualogy.qafe.mgwt.client.ui.events.CellRenderEvent;
import com.qualogy.qafe.mgwt.client.ui.events.CellRenderHandler;
import com.qualogy.qafe.mgwt.client.ui.events.DataChangeHandler;
import com.qualogy.qafe.mgwt.client.ui.events.HasCellItemSelectHandlers;
import com.qualogy.qafe.mgwt.client.ui.events.HasCellRenderHandler;
import com.qualogy.qafe.mgwt.client.ui.events.HasPostRenderHandlers;
import com.qualogy.qafe.mgwt.client.ui.events.HasScrollBottomHandlers;
import com.qualogy.qafe.mgwt.client.ui.events.PostRenderEvent;
import com.qualogy.qafe.mgwt.client.ui.events.PostRenderHandler;
import com.qualogy.qafe.mgwt.client.ui.events.ScrollBottomEvent;
import com.qualogy.qafe.mgwt.client.ui.events.ScrollBottomHandler;
import com.qualogy.qafe.mgwt.shared.QAMLConstants;

public class QDataGrid extends CellList implements IsEditable, HasCellRenderHandler, HasPostRenderHandlers, HasCellItemSelectHandlers, HasScrollBottomHandlers, HasPaging, HasIndexSelection, HasAttribute {

	private static class QCell implements Cell {
		
		private QDataGrid source;
		
		protected void setSource(QDataGrid source) {
			this.source = source;
		}
		
		@Override
		public void render(SafeHtmlBuilder safeHtmlBuilder, Object model) {
			if (source != null) {				
				final UIObject cellWidget = source.renderCell(model);
				safeHtmlBuilder.append(new SafeHtml() {
					@Override
					public String asString() {
						return cellWidget.getElement().getString();
					}
				});
			}
		}

		@Override
		public boolean canBeSelected(Object model) {
			return false;
		}
	}

	private CellRenderHandler cellRenderHandler;
	private List models = new ArrayList();
	private List<String> columns = new ArrayList<String>();
	private Map<String,CellItemSelectHandler> columnHandlers = new HashMap<String,CellItemSelectHandler>();
	private int selectedRowIndex = -1;
	private int scrollPosition;
	private String dataName;
	private HandlerRegistration scrollHandlerRegistration;
	private int pageSize;
	private int currentPage;
	
	public QDataGrid() {
		super(new QCell());
		((QCell)cell).setSource(this);
		
		addHandler(new CellSelectedHandler() {
			@Override
			public void onCellSelected(CellSelectedEvent event) {
				int index = event.getIndex();
				fireCellItemSelect(index);
			}
		}, CellSelectedEvent.getType());
	}

	@Override
	public void addCellItemSelectHandler(String cellItem, CellItemSelectHandler handler) {
		if (cellItem == null) {
			return;
		}
		if (handler == null) {
			return;
		}
		columnHandlers.put(cellItem, handler);
	}

	@Override
	public void registerCellRenderHandler(CellRenderHandler handler) {
		this.cellRenderHandler = handler;
	}

	@Override
	public HandlerRegistration addPostRenderHandler(PostRenderHandler handler) {
		return addHandler(handler, PostRenderEvent.getType());
	}
	
	@Override
	public HandlerRegistration addDataChangeHandler(DataChangeHandler handler) {
		return ComponentHelper.addDataChangeHandler(this, handler);
	}
	
	@Override
	public HandlerRegistration addScrollBottomHandler(ScrollBottomHandler handler) {
		return addHandler(handler, ScrollBottomEvent.getType());
	}

	public void addColumn(String column) {
		if (column == null) {
			return;
		}
		if (columns.contains(column)) {
			return;
		}
		columns.add(column);
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
		models.clear();
		if (data instanceof Map) {
			models.add(data);
		} else if (data instanceof List) {
			models.addAll((List)data);
		}
		render(models);
		ComponentHelper.fireDataChange(this, models, null);
	}

	@Override
	public Object getData() {
		if (selectedRowIndex == -1) {
			return null;	
		}
		if (selectedRowIndex > (models.size() -1)) {
			return null;	
		}
		Object data = models.get(selectedRowIndex);
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
	public void setEditable(boolean editable) {
		// TODO Auto-generated method stub
	}

	@Override
	public int getPageSize() {
		return pageSize;
	}

	@Override
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	@Override
	public int getCurrentPage() {
		return currentPage;
	}

	@Override
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}
	
	@Override
	public void setSelectedIndex(int index) {
		selectedRowIndex = index;
	}

	@Override
	public Object getAttribute(String name) {
		if (QAMLConstants.PROPERTY_PAGESIZE.equals(name)) {
			return getPageSize();
		}
		if (QAMLConstants.PROPERTY_CURRENT_PAGE.equals(name)) {
			return getCurrentPage();
		}
		return null;
	}
	
	@Override
	public void render(List models) {
		super.render(models);
		firePostRender();
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
	
	private void fireCellItemSelect(int index) {
		selectedRowIndex = index;
		Object model = models.get(selectedRowIndex);
		for (String column : columns) {
			// Mimic the record selection event
			if (columnHandlers.containsKey(column)) {
				CellItemSelectHandler cellItemSelectHandler = columnHandlers.get(column);
				CellItemSelectEvent event = new CellItemSelectEvent(this, column, model, selectedRowIndex);
				event.execute(cellItemSelectHandler);
				break;
			}
		}
	}
	
	private void firePostRender() {
		PostRenderEvent event = new PostRenderEvent(this, models);
		fireEvent(event);
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