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
package com.qualogy.qafe.gwt.client.component;

import com.google.gwt.gen2.table.client.AbstractColumnDefinition;
import com.qualogy.qafe.gwt.client.vo.functions.DataContainerGVO;

public class QColumnDefinition extends AbstractColumnDefinition<DataContainerGVO, String> {
	
	private String field;
	private Boolean identifyingField = Boolean.FALSE;
	private CellCleaner cellCleaner;
	
	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public Boolean getIdentifyingField() {
		return identifyingField;
	}

	public void setIdentifyingField(Boolean identifyingField) {
		this.identifyingField = identifyingField;
	}

	@Override
	public String getCellValue(DataContainerGVO rowValue) {
		String output = null;
		if (rowValue.isMap()){
			if (rowValue.getDataMap().containsKey(field)) {
				output = rowValue.getDataMap().get(field)!=null ? rowValue.getDataMap().get(field).toString() : "";
			} else if ((field != null) && (rowValue.getDataMap().containsKey(field.toUpperCase()))) {
				output = rowValue.getDataMap().get(field.toUpperCase()).toString();
			}
		}
		return output;
	}

	@Override
	public void setCellValue(DataContainerGVO rowValue, String cellValue) {
		DataContainerGVO data = new DataContainerGVO();
		data.setKind(DataContainerGVO.KIND_STRING);
		data.setDataString(cellValue);
		rowValue.getDataMap().put(field, data);

	}
	
	/**
	 * This method returns DataContainerGVO obect extracted from the DataMap.
	 * Variable rowValue holds data for a single row to be set in a DataGrid component.
	 * This method is used to set data for inner component textfield with type = date in a data grid.
	 * */
	public DataContainerGVO getDataContainerGVO(DataContainerGVO rowValue) {
		DataContainerGVO dataContainerGVO = null;
		if (rowValue.isMap()){
			if (rowValue.getDataMap().containsKey(field)) {
		
				dataContainerGVO = rowValue.getDataMap().get(field)!=null ? rowValue.getDataMap().get(field) : new DataContainerGVO();
			}
		}
		return dataContainerGVO;
	}

	public CellCleaner getCellCleaner() {
		return cellCleaner;
	}
	
	public void setCellCleaner(CellCleaner cellCleaner) {
		this.cellCleaner = cellCleaner;
	}
}