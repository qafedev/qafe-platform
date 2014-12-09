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
package com.qualogy.qafe.gwt.client.component;

import java.util.List;

import com.google.gwt.gen2.table.client.ColumnDefinition;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;
import com.qualogy.qafe.gwt.client.vo.functions.DataContainerGVO;
import com.qualogy.qafe.gwt.client.vo.functions.SetValueGVO;
import com.qualogy.qafe.gwt.client.vo.ui.DataGridGVO;

public interface HasDataGridMethods extends HasEditable {
	void setDelete(Boolean bool);
	void setAdd(Boolean bool);
	void setShowAll(Boolean bool);
	void setExport(Boolean bool);
	void insertDataRow(List<String> listOfDataMap, Boolean append, String senderId, String listenerType);
	void redraw();
	void insertData(List<DataContainerGVO> listOfDataMap, Boolean append, String senderId, String listenerType);
	void setOverflow(Widget overflow);
	Integer getMaxRows();
	int getPageSize();
	void setPageSize(Integer pageSize);
	void setModified(boolean modified, int row, int column,String value);	
	void setModified(ColumnDefinition<DataContainerGVO, String> columnDefinition, UIObject uiObject, DataContainerGVO rowValue, Object newValue);	
	void setModified(ColumnDefinition<DataContainerGVO, String> columnDefinition, UIObject uiObject, DataContainerGVO rowValue, Object  newValue, boolean changedByUser);
	void processActions(DataGridGVO gvo, String uuid, String parent);
	void setup();
	void selectRow(int rowNr);
	public int getCurrentPage();
	public void setCurrentPage(int currentPage);
	void setDataToCell(DataContainerGVO valueToset, boolean b, String senderId, String cellOnRowToSet);
	void setColumnValue(String columnName, DataContainerGVO value);
    void setColumnValue(int rowIndex, String columnName, DataContainerGVO value);
	void addToDropDownValuesForColumnList(String key, SetValueGVO setValue);
	int getRowIndex(String rowIndex);
	DataContainerGVO getRowValue(int rowIndex);
	void setColumnVisible(String column, boolean value);
	void setColumnLabel(String column, String value);
}
