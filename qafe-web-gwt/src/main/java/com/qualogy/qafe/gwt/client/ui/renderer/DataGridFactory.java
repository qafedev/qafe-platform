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
package com.qualogy.qafe.gwt.client.ui.renderer;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.gen2.table.client.AbstractScrollTable.ResizePolicy;
import com.google.gwt.gen2.table.client.CellRenderer;
import com.google.gwt.gen2.table.client.DefaultRowRenderer;
import com.google.gwt.gen2.table.client.DefaultTableDefinition;
import com.google.gwt.gen2.table.client.FixedWidthFlexTable;
import com.google.gwt.gen2.table.client.FixedWidthGrid;
import com.google.gwt.gen2.table.client.FixedWidthGridBulkRenderer;
import com.google.gwt.gen2.table.client.SelectionGrid.SelectionPolicy;
import com.google.gwt.gen2.table.client.TableDefinition;
import com.google.gwt.gen2.table.override.client.FlexTable;
import com.google.gwt.gen2.table.override.client.FlexTable.FlexCellFormatter;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;
import com.qualogy.qafe.gwt.client.component.CellCleaner;
import com.qualogy.qafe.gwt.client.component.CellRendererHelper;
import com.qualogy.qafe.gwt.client.component.DataMap;
import com.qualogy.qafe.gwt.client.component.HasDataGridMethods;
import com.qualogy.qafe.gwt.client.component.QColumnDefinition;
import com.qualogy.qafe.gwt.client.component.QDefaultRowRenderer;
import com.qualogy.qafe.gwt.client.component.QPagingOptions;
import com.qualogy.qafe.gwt.client.component.QPagingScrollTable;
import com.qualogy.qafe.gwt.client.component.QPagingScrollTableOperation;
import com.qualogy.qafe.gwt.client.component.QTableModel;
import com.qualogy.qafe.gwt.client.context.ClientApplicationContext;
import com.qualogy.qafe.gwt.client.vo.functions.DataContainerGVO;
import com.qualogy.qafe.gwt.client.vo.ui.ComponentGVO;
import com.qualogy.qafe.gwt.client.vo.ui.DataGridColumnGVO;
import com.qualogy.qafe.gwt.client.vo.ui.DataGridGVO;
import com.qualogy.qafe.gwt.client.vo.ui.LabelGVO;
import com.qualogy.qafe.gwt.client.vo.ui.event.EventListenerGVO;

public class DataGridFactory {

	private DataGridFactory() {
	}

	public static UIObject createPagingDataGrid(final DataGridGVO gvo, final String uuid, final String parent) {
		final QTableModel tableModel = new QTableModel();

		if (gvo.getPageSize() != null) {
			tableModel.setRowCount(gvo.getPageSize());
		}

		// Create a TableCellRenderer
		final TableDefinition<DataContainerGVO> tableDef = new DefaultTableDefinition<DataContainerGVO>();
		final FixedWidthFlexTable headerTable = new FixedWidthFlexTable();
		final FixedWidthGrid dataTable = new FixedWidthGrid();

		if (gvo.getMultipleSelect()!=null && gvo.getMultipleSelect().booleanValue()){
			dataTable.setSelectionPolicy(SelectionPolicy.MULTI_ROW);
		} else {
			dataTable.setSelectionPolicy(SelectionPolicy.ONE_ROW);
		}

		final QPagingScrollTable pagingScrollTable = new QPagingScrollTable(tableModel, dataTable, headerTable, tableDef, gvo, uuid, parent);
		tableModel.setSource(pagingScrollTable);
		// setup the bulk renderer
        final FixedWidthGridBulkRenderer<DataContainerGVO> bulkRenderer = new FixedWidthGridBulkRenderer<DataContainerGVO>(pagingScrollTable.getDataTable(), pagingScrollTable);

        pagingScrollTable.setBulkRenderer(bulkRenderer);

		if(gvo.getStyleClass() != null) {
			pagingScrollTable.setStylePrimaryName(gvo.getStyleClass());
		}

		pagingScrollTable.setPageSize(gvo.getPageSize(), true);
		pagingScrollTable.setEmptyTableWidget(new AbstractComponentRenderer.MessageBox());
		pagingScrollTable.setup();
		pagingScrollTable.fillWidth();
		pagingScrollTable.setResizePolicy(ResizePolicy.FILL_WIDTH);

		// Setup the formatting
		pagingScrollTable.setCellPadding(3);
		pagingScrollTable.setCellSpacing(0);

		FlexTable layout = null;
		if (gvo.getMenu() != null) {
			final ComponentGVO finalComponentGVO = gvo;
			final String finalUuid = uuid;
			final String finalParent = parent;
			layout = new FlexTable() {
				@Override
				public void onBrowserEvent(final Event event) {
					if (event.getTypeInt() == Event.ONCONTEXTMENU) {
						DOM.eventPreventDefault(event);
						AbstractComponentRenderer.applyContextMenu(event, finalComponentGVO, finalUuid, finalParent);
					}
					super.onBrowserEvent(event);
				}

				@Override
				protected void setElement(final Element elem) {
					super.setElement(elem);
					sinkEvents(Event.ONCONTEXTMENU);
				}
			};
		} else {
			layout = new FlexTable();
		}
		layout.setWidth("100%");
		final FlexCellFormatter formatter = layout.getFlexCellFormatter();
		// Add the scroll table to the layout
		layout.setWidget(0, 0, pagingScrollTable);
		formatter.setWidth(0, 0, "100%");
		formatter.setVerticalAlignment(0, 0, HasVerticalAlignment.ALIGN_TOP);

		final QPagingScrollTableOperation operations = new QPagingScrollTableOperation(pagingScrollTable);
		operations.initForCallback(gvo.getId(), uuid, parent, gvo.getContext());
		operations.setVisible(true);
		final HorizontalPanel hp = new HorizontalPanel();
		QPagingOptions pagingOptions = null;
		final Integer pageSize = gvo.getPageSize();
		if ((pageSize != null) && (pageSize > -1)) {
			pagingOptions = new QPagingOptions(pagingScrollTable);
			pagingOptions.initForCallback(gvo.getId(), uuid, parent, gvo.getContext());
			pagingOptions.setVisible(true);
			hp.add(pagingOptions);
			if(gvo.getPageScroll()) {
				pagingScrollTable.addScrollHandler(pagingScrollTable);
				pagingOptions.setVisible(false);
			}
		}

		hp.add(operations);
		layout.insertRow(1);
		layout.setWidget(1, 0, hp);
		RendererHelper.fillIn(gvo, pagingScrollTable, uuid, parent, gvo.getContext());

		if (gvo.hasOverFlow()){
			final UIObject  overflow = AnyComponentRenderer.getInstance().render(gvo.getOverflow(), uuid, parent, gvo.getContext());
			if (overflow!=null && overflow instanceof Widget){
				((HasDataGridMethods) pagingScrollTable).setOverflow((Widget) overflow);
				layout.insertRow(2);
				layout.setWidget(2,0,(Widget) overflow);
				RendererHelper.addVisibleInfo(gvo,overflow);
			}
		}
		return layout;
	}

	private static TableDefinition<DataContainerGVO> createTableDefinition(final DataGridGVO source,final String uuid,final String parent) {
		return createTableDefinition(source, source.getColumns(),uuid, parent, null);
	}

    // CHECKSTYLE.OFF: CyclomaticComplexity
	public static TableDefinition<DataContainerGVO> createTableDefinition(final DataGridGVO source,final DataGridColumnGVO[] dataGridColumnGVOs,final String uuid,final String parent, final HasDataGridMethods parentWidget) {
		// Create the table definition
		final DefaultTableDefinition<DataContainerGVO> tableDefinition = new DefaultTableDefinition<DataContainerGVO>();

		// Set the row renderer
		if(source.getRowColors() != null && source.getRowColors().length > 0) {
			tableDefinition.setRowRenderer(new QDefaultRowRenderer<DataContainerGVO>(source.getRowColors()));

		} else {
			tableDefinition.setRowRenderer(new DefaultRowRenderer<DataContainerGVO>());
		}

		final ArrayList<DataGridColumnGVO> copyDataGridColumnGVOs = new ArrayList<DataGridColumnGVO>();

		// Column to show the status of the row visibly
		if((source.getAdd() || source.getDelete() || source.getEditable()) && dataGridColumnGVOs != null && !DataMap.ROW_NUMBER.equals(dataGridColumnGVOs[0].getFieldName())) {
			final DataGridColumnGVO rowNumberDataGridColumnGVO = new DataGridColumnGVO();
			final LabelGVO rowNumberLabel = new LabelGVO();
			rowNumberDataGridColumnGVO.setComponent(rowNumberLabel);
			rowNumberDataGridColumnGVO.setFieldName(DataMap.ROW_NUMBER);
			rowNumberDataGridColumnGVO.setWidth("25");
			rowNumberDataGridColumnGVO.setContainerName(source.getId());
			rowNumberDataGridColumnGVO.setContext(source.getContext());
			resolveEvents4RowNumber(rowNumberDataGridColumnGVO, source);
			copyDataGridColumnGVOs.add(rowNumberDataGridColumnGVO);
		}

		if(dataGridColumnGVOs != null){
			for(final DataGridColumnGVO dgColumnGVO : dataGridColumnGVOs){
				copyDataGridColumnGVOs.add(dgColumnGVO);
			}
		}

		DataGridColumnGVO[] dataGridColumnGVOsArray = new DataGridColumnGVO[copyDataGridColumnGVOs.size()];
		dataGridColumnGVOsArray = copyDataGridColumnGVOs.toArray(dataGridColumnGVOsArray);
		if(dataGridColumnGVOs != null){ // If there are columns defined
			source.setColumns(dataGridColumnGVOsArray);
		}

		if (copyDataGridColumnGVOs != null) {
			for (int i = 0; i<dataGridColumnGVOsArray.length; i++) {
			    final DataGridColumnGVO dataGridColumnGVO = dataGridColumnGVOsArray[i];
			    if (dataGridColumnGVO.isQafeChecksum()) {
			        continue;
			    }
			    final String columnName = dataGridColumnGVO.getFieldName();
			    final String columnWidth = dataGridColumnGVO.getWidth();
			    final Boolean columnVisible = dataGridColumnGVO.getVisible();
			    
                final QColumnDefinition columnDef = new QColumnDefinition();
                columnDef.setField(columnName);
                columnDef.setIdentifyingField(dataGridColumnGVO.getIdentifyingField());
                columnDef.setColumnSortable(dataGridColumnGVO.getSortable());
                if (columnWidth != null && columnWidth.length() > 0) {
                    try {
                        columnDef.setPreferredColumnWidth(Integer.parseInt(columnWidth));
                    } catch (final Exception e){
                        ClientApplicationContext.getInstance().log("Could not parse column width for datagrid (id"+  source.getId() +") or name ("+source.getFieldName()+") for column (id=" + dataGridColumnGVO.getId()+ ",name="+ columnName  +")" );
                    }
                }
                
                tableDefinition.addColumnDefinition(columnDef);
                if ((columnVisible != null) && !columnVisible) {
                    tableDefinition.setColumnVisible(columnDef, columnVisible);    
                }
                
                final CellRenderer<DataContainerGVO, String> cellRenderer = CellRendererHelper.getCellRenderer(source, dataGridColumnGVO, uuid, parent, parentWidget);
                if (cellRenderer != null) {
                    columnDef.setCellRenderer(cellRenderer);
                }
                final CellCleaner cellCleaner = CellRendererHelper.getCellCleaner(source, dataGridColumnGVO, uuid, parent, parentWidget);
                columnDef.setCellCleaner(cellCleaner);
//				if (!dataGridColumnGVOsArray[i].isQafeChecksum() && dataGridColumnGVOsArray[i].getVisible().booleanValue()){
//					final String field = dataGridColumnGVOsArray[i].getFieldName();
//					QColumnDefinition columnDef = new QColumnDefinition();
//					columnDef.setField(field);
//					columnDef.setIdentifyingField(dataGridColumnGVOsArray[i].getIdentifyingField());
//					columnDef.setColumnSortable(dataGridColumnGVOsArray[i].getSortable());
//
//					if( dataGridColumnGVOsArray[i].getWidth() != null && dataGridColumnGVOsArray[i].getWidth().length() > 0){
//						try {
//							columnDef.setPreferredColumnWidth(Integer.parseInt(dataGridColumnGVOsArray[i].getWidth()));
//						} catch (Exception e){
//							ClientApplicationContext.getInstance().log("Could not parse column width for datagrid (id"+  source.getId() +") or name ("+source.getFieldName()+") for column (id=" + dataGridColumnGVOsArray[i].getId()+ ",name="+ dataGridColumnGVOsArray[i].getFieldName()  +")" );
//						}
//
//					}
//					tableDefinition.addColumnDefinition(columnDef);
//					CellRenderer<DataContainerGVO, String> cellRenderer = CellRendererHelper.getCellRenderer(source, dataGridColumnGVOsArray[i], uuid, parent, parentWidget);
//					if (cellRenderer != null) {
//						columnDef.setCellRenderer(cellRenderer);
//					}
//					CellCleaner cellCleaner = CellRendererHelper.getCellCleaner(source, dataGridColumnGVOsArray[i], uuid, parent, parentWidget);
//					columnDef.setCellCleaner(cellCleaner);
//				}
			}
		}
		return tableDefinition;
	}
    // CHECKSTYLE.ON: CyclomaticComplexity

	private static void resolveEvents4RowNumber(final DataGridColumnGVO rowNumberColumnGVO, final DataGridGVO datagridGVO) {
		final String datagridId = datagridGVO.getId();
		if ((datagridId == null) || (datagridId.length() == 0)) {
			return;
		}
		final EventListenerGVO[] events = datagridGVO.getEvents();
		if (events == null) {
			return;
		}
		final String rowNumberColumnId = datagridId + "." + rowNumberColumnGVO.getFieldName();
		final List<EventListenerGVO> rowNumberColumnEvents = new ArrayList<EventListenerGVO>();
		for (final EventListenerGVO event : events) {
			if (rowNumberColumnId.equals(event.getEventComponentId())) {
				rowNumberColumnEvents.add(event);
			}
		}
		if (rowNumberColumnEvents.size() > 0) {
			rowNumberColumnGVO.setEvents(rowNumberColumnEvents.toArray(new EventListenerGVO[]{}));
		}
	}
}