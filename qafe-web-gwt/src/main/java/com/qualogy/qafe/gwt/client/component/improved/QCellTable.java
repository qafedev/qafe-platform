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
package com.qualogy.qafe.gwt.client.component.improved;

import java.util.List;

import com.google.gwt.gen2.table.client.ColumnDefinition;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SelectionModel;
import com.google.gwt.view.client.SingleSelectionModel;
import com.qualogy.qafe.gwt.client.component.DataMap;
import com.qualogy.qafe.gwt.client.component.HasDataGridMethods;
import com.qualogy.qafe.gwt.client.ui.renderer.cell.LinkColumn;
import com.qualogy.qafe.gwt.client.ui.renderer.cell.QButtonColumn;
import com.qualogy.qafe.gwt.client.vo.functions.DataContainerGVO;
import com.qualogy.qafe.gwt.client.vo.functions.SetValueGVO;
import com.qualogy.qafe.gwt.client.vo.ui.ButtonGVO;
import com.qualogy.qafe.gwt.client.vo.ui.ComponentGVO;
import com.qualogy.qafe.gwt.client.vo.ui.DataGridGVO;

public class QCellTable extends CellTable<DataContainerGVO> implements HasDataGridMethods {

	List<DataContainerGVO> list = null;

	private final static String CONTENT_HTML ="html";
	private final static String CONTENT_LINK ="link";
	private final static String CONTENT_CHECKBOX ="checkbox";
	private final static String CONTENT_STRING ="string";
	
	
	public QCellTable(DataGridGVO gvo, String uuid, String parent){
		setSize(gvo.getWidth(),gvo.getHeight());
		String context = gvo.getContext();
		String window  = gvo.getWindow();
		String component =gvo.getId();
		
		Column<DataContainerGVO,String> [] columns = null;
		
		if (gvo!=null){
			if (gvo.getColumns()!=null){
				int gvoColSize=  gvo.getColumns().length;
				columns = new Column[gvoColSize];
				for (int i=0;i<gvoColSize;i++){
					final String columnName=  gvo.getColumns()[i].getFieldName();
					String contentType = gvo.getColumns()[i].getContent();
					if (CONTENT_STRING.equals(contentType)){
					
						if (gvo.getColumns()[i].getComponent()!=null){
							ComponentGVO c = gvo.getColumns()[i].getComponent();
							if (c instanceof ButtonGVO){
								ButtonGVO bc = (ButtonGVO)c;
								columns[i] =   new QButtonColumn<DataContainerGVO>(uuid,context,window,component,gvo.getColumns()[i].getId(),parent,gvo.getColumns()[i].getTooltip(), gvo.getColumns()[i],bc) {
								      @Override
								      public String getValue(DataContainerGVO map) {
								    	  return map.getDataMap().get(columnName)!=null ? map.getDataMap().get(columnName).toString(): null;
								      }
								 };
							}
							
						} else {
							columns[i] =   new TextColumn<DataContainerGVO>() {
							      @Override
							      public String getValue(DataContainerGVO map) {
							        return map.getDataMap().get(columnName)!=null ? map.getDataMap().get(columnName).toString(): null;
							      }
							    };
						}
					} else if (CONTENT_LINK.equals(contentType)){
						columns[i] =   new LinkColumn<DataContainerGVO>(uuid,context,window,component,gvo.getColumns()[i].getId(),parent,gvo.getColumns()[i].getTooltip(), gvo.getColumns()[i]) {
						      @Override
						      public String getValue(DataContainerGVO map) {
						    	  return map.getDataMap().get(columnName)!=null ? map.getDataMap().get(columnName).toString(): null;
						      }
						    };
					}
					
					if (columns[i]!=null){
						if (gvo.getColumns()[i].getSortable()!=null && gvo.getColumns()[i].getSortable().booleanValue()){
							//columns[i].
						
						}
						addColumn(columns[i],gvo.getColumns()[i].getDisplayname());
					}
					
				}
			}
			ListDataProvider<DataContainerGVO> adapter = new ListDataProvider<DataContainerGVO>();
			list = adapter.getList();
			if (gvo.getPageSize()!=null){
				if (gvo.getPageSize()>0){
					setPageSize(gvo.getPageSize());
				}
			}


			
			SelectionModel<DataContainerGVO> selectionModel = null;
		
			if (gvo.getMultipleSelect()!=null && gvo.getMultipleSelect().booleanValue()){
				selectionModel = new MultiSelectionModel<DataContainerGVO>();
			} else {
				selectionModel = new SingleSelectionModel<DataContainerGVO>();
			}
			setSelectionModel(selectionModel);
			adapter.addDataDisplay(this);
			setPageStart(0);

		}
	}
	public void setDelete(Boolean bool) {
		// TODO Auto-generated method stub
		
	}

	public void setAdd(Boolean bool) {
		// TODO Auto-generated method stub
		
	}

	public void setShowAll(Boolean bool) {
		// TODO Auto-generated method stub
		
	}

	public void setExport(Boolean bool) {
		// TODO Auto-generated method stub
		
	}

	public boolean isEditable() {
		// TODO Auto-generated method stub
		return false;
	}
	
	public void setEditable(boolean value) {
		// TODO Auto-generated method stub
		
	}

	public void insertData(int i, DataContainerGVO map, Boolean append, String senderId,
			String listenerType) {
		// TODO Auto-generated method stub
		
	}

//	public void insertDataRow(List<String> listOfDataMap, Boolean append,
//			String senderId, String listenerType) {
//		// TODO Auto-generated method stub
//		
//	}

//	public void insertData(List<DataContainerGVO> listOfDataMap, Boolean append,
//			String senderId, String listenerType) {
//		if (listOfDataMap!=null){
//			if (getRowCount()>0){
//				list.clear();
//			}
//			//setRowData(0, listOfDataMap);
//			list.addAll(listOfDataMap);
//			//setCurrentPage(0);
//			
//		}
//		
//	}

	public void setOverflow(Widget overflow) {
		// TODO Auto-generated method stub
		
	}

	public Integer getMaxRows() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setPageSize(Integer pageSize) {
		super.setPageSize(pageSize);
		
	}

	public void setModified(boolean modified, int row, int column, String value) {
		// TODO Auto-generated method stub
		
	}

	public void setModified(ColumnDefinition<DataMap, String> columnDefinition,
			UIObject uiObject, DataMap rowValue, Object newValue) {
		// TODO Auto-generated method stub
		
	}

	public void processActions(DataGridGVO gvo, String uuid, String parent) {
		// TODO Auto-generated method stub
		
	}

	public void setup() {
		// TODO Auto-generated method stub
		
	}

	public void selectRow(int rowNr) {
		// TODO Auto-generated method stub
		
	}

	public int getCurrentPage() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void setCurrentPage(int currentPage) {
		// TODO Auto-generated method stub
		
	}
	public Object getData() {
		if(getSelectionModel() instanceof SingleSelectionModel){
			return (( SingleSelectionModel<DataContainerGVO>)getSelectionModel()).getSelectedObject();
		} else if (getSelectionModel() instanceof MultiSelectionModel) {
			return (( MultiSelectionModel<DataContainerGVO>)getSelectionModel()).getSelectedSet();			
		} else {
			return null;
		}
	}
	public void insertDataRow(List<String> listOfDataMap, Boolean append, String senderId, String listenerType) {
		// TODO Auto-generated method stub
		
	}
//	public void insertData(List<DataContainerGVO> listOfDataMap, Boolean append, String senderId, String listenerType) {
//		// TODO Auto-generated method stub
//		
//	}
	public void setModified(ColumnDefinition<DataContainerGVO, String> columnDefinition, UIObject uiObject, DataContainerGVO rowValue, String newValue) {
		// TODO Auto-generated method stub
		
	}

	public void setModified(ColumnDefinition<DataMap, String> columnDefinition,
			UIObject uiObject, DataMap rowValue, Object newValue,
			boolean changedByUser) {
		// TODO Auto-generated method stub
		
	}
	public void insertData(List<DataContainerGVO> listOfDataMap,
			Boolean append, String senderId, String listenerType) {
		// TODO Auto-generated method stub
		
	}
	public void setModified(
			ColumnDefinition<DataContainerGVO, String> columnDefinition,
			UIObject uiObject, DataContainerGVO rowValue, Object newValue) {
		// TODO Auto-generated method stub
		
	}
	public void setModified(
			ColumnDefinition<DataContainerGVO, String> columnDefinition,
			UIObject uiObject, DataContainerGVO rowValue, Object newValue,
			boolean changedByUser) {
		// TODO Auto-generated method stub
		
	}
	public void setDataToCell(DataContainerGVO valueToset, boolean b,
			String senderId, String cellOnRowToSet) {
		// TODO Auto-generated method stub
		
	}
	public void addToDropDownValuesForColumnList(String key,
			SetValueGVO setValue) {
		// TODO Auto-generated method stub
		
	}
	public int getRowIndex(String rowIndex) {
		// TODO Auto-generated method stub
		return 0;
	}
	public DataContainerGVO getRowValue(int rowIndex) {
		// TODO Auto-generated method stub
		return null;
	}
	public void setColumnVisible(String column, boolean value) {
		// TODO Auto-generated method stub
		
	}
	public void setColumnLabel(String column, String value) {
		// TODO Auto-generated method stub
		
	}
    public void setColumnValue(String columnName, DataContainerGVO value) {
        // TODO Auto-generated method stub
        
    }
    public void setColumnValue(int rowIndex, String columnName, DataContainerGVO value) {
        // TODO Auto-generated method stub
        
    }
}
