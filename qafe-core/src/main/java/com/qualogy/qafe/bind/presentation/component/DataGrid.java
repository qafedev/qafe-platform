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
/**
 * 
 */
package com.qualogy.qafe.bind.presentation.component;

import java.util.List;



/**
 * @author rjankie 
 */

public  class DataGrid extends EditableComponent implements HasComponents{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3607530581878883535L;

	protected Integer maxRows ;
	
	protected Integer pageSize;
	
	protected Integer currentPage;
	
	protected Integer minRows;
	
	
	
	protected Boolean delete=Boolean.FALSE;
	
	protected Boolean add=Boolean.FALSE;
	
	protected Boolean pageScroll=Boolean.FALSE;
	
	
	protected Boolean export=Boolean.FALSE;
	
	protected String exportFormats="excel,pdf,csv,xml";
	
	public String getExportFormats() {
		return exportFormats;
	}

	public void setExportFormats(String exportFormats) {
		this.exportFormats = exportFormats;
	}

	protected Boolean importEnabled=Boolean.FALSE;
		
	public Boolean getImportEnabled() {
		return importEnabled;
	}

	public void setImportEnabled(Boolean importEnabled) {
		this.importEnabled = importEnabled;
	}

	protected List<DataGridColumn> columns;
	
	protected OverFlowPanel overflow;
	
	protected DatagridControlBar controlbar;
	
	protected Boolean multipleSelect;
	
	protected String rowColors;
	private Boolean selectFirstRow;
	private Boolean save;
	private Boolean cancel;
	private Boolean refresh;
	private String overflowGroup;
	

	public DatagridControlBar getControlbar() {
		return controlbar;
	}

	public void setControlbar(DatagridControlBar controlbar) {
		this.controlbar = controlbar;
	}

	public List<DataGridColumn> getColumns() {
		return columns;
	}

	public void setColumns(List<DataGridColumn> columns) {
		this.columns = columns;
	}

	/**
	 * @deprecated
	 */
	public Integer getMaxRows() {
		return maxRows;
	}

	/**
	 * @deprecated
	 */
	public void setMaxRows(Integer maxRows) {
		this.maxRows = maxRows;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(Integer currentPage) {
		this.currentPage = currentPage;
	}

	public Boolean getDelete() {
		return delete;
	}

	public void setDelete(Boolean delete) {
		this.delete = delete;
	}

	public Boolean getAdd() {
		return add;
	}

	public void setAdd(Boolean add) {
		this.add = add;
	}

	public Integer getMinRows() {
		return minRows;
	}

	public void setMinRows(Integer minRows) {
		this.minRows = minRows;
	}

	public Boolean getExport() {
		return export;
	}

	public void setExport(Boolean export) {
		this.export = export;
	}



	public OverFlowPanel getOverflow() {
		return overflow;
	}

	public void setOverflow(OverFlowPanel overflow) {
		this.overflow = overflow;
	}
	
	public boolean hasOverflow(){
		return (overflow!=null);
	}
	
	public boolean hasColumns(){
		return (columns!=null);
	}

	public List<? extends Component> getComponents() {
		return columns;
	}

	public Boolean getMultipleSelect() {
		return multipleSelect;
	}

	public void setMultipleSelect(Boolean multipleSelect) {
		this.multipleSelect = multipleSelect;
	}

	public String getRowColors() {
		return rowColors;
	}

	public void setRowColors(String rowColors) {
		this.rowColors = rowColors;
	}

	public Boolean getPageScroll() {
		return pageScroll;
	}

	public void setPageScroll(Boolean pageScroll) {
		this.pageScroll = pageScroll;
	}

	public Boolean getSelectFirstRow() {
		return selectFirstRow;
	}

	public void setSelectFirstRow(Boolean selectFirstRow) {
		this.selectFirstRow = selectFirstRow;
	}

	public Boolean getSave() {
		return save;
	}

	public void setSave(Boolean save) {
		this.save = save;
	}

	public Boolean getCancel() {
		return cancel;
	}

	public void setCancel(Boolean cancel) {
		this.cancel = cancel;
	}

	public Boolean getRefresh() {
		return refresh;
	}

	public void setRefresh(Boolean refresh) {
		this.refresh = refresh;
	}

    
    public String getOverflowGroup() {
        return overflowGroup;
    }

    
    public void setOverflowGroup(String overflowGroup) {
        this.overflowGroup = overflowGroup;
    }
}
