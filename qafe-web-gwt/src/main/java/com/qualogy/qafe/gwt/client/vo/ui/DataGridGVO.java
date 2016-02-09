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
/**
 * 
 */
package com.qualogy.qafe.gwt.client.vo.ui;

import java.util.ArrayList;
import java.util.List;

/**
 * @author rjankie
 */

public class DataGridGVO extends EditableComponentGVO {

    private Integer maxRows;

    private Integer minRows;

    private Integer pageSize;

    private DataGridColumnGVO[] columns;

    private Boolean delete = Boolean.FALSE;

    private Boolean add = Boolean.FALSE;

    private Boolean pageScroll = Boolean.FALSE;

    private Boolean export = Boolean.FALSE;

    private String exportFormats = "excel,pdf,csv,xml";

    private Boolean importEnabled = Boolean.FALSE;

    private String importAction = "set";
    
    private ComponentGVO saveComponent;

    private ComponentGVO addComponent;

    private ComponentGVO deleteComponent;

    private ComponentGVO refreshComponent;

    private ComponentGVO cancelComponent;

    private ComponentGVO pageSizeComponent;

    private ComponentGVO offSetComponent;

    private ComponentGVO exportComponent;

    private Boolean multipleSelect = Boolean.FALSE;

    private String[] rowColors;

    private PanelGVO overflow;

    private Boolean selectFirstRow = Boolean.FALSE;

    private Boolean save = Boolean.TRUE;

    private Boolean cancel = Boolean.TRUE;

    private Boolean refresh = Boolean.TRUE;

    private String overflowGroup;

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

    public ComponentGVO[] getComponents() {
        List<ComponentGVO> cs = new ArrayList<ComponentGVO>();
        cs.add(saveComponent);
        return cs.toArray(new ComponentGVO[]{});
    }

    public Boolean getAdd() {
        return add;
    }

    public void setAdd(Boolean add) {
        this.add = add;
    }

    @Override
    public String getClassName() {
        return "com.qualogy.qafe.gwt.client.vo.ui.DataGridGVO";
    }

    public DataGridColumnGVO[] getColumns() {
        return columns;
    }

    public void setColumns(DataGridColumnGVO[] columns) {
        this.columns = columns;
    }

    public Integer getMaxRows() {
        return maxRows;
    }

    public void setMaxRows(Integer maxRows) {
        this.maxRows = maxRows;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Boolean getDelete() {
        return delete;
    }

    public void setDelete(Boolean delete) {
        this.delete = delete;
    }

    public ComponentGVO getCancelComponent() {
        return cancelComponent;
    }

    public void setCancelComponent(ComponentGVO cancelComponent) {
        this.cancelComponent = cancelComponent;
    }

    public ComponentGVO getRefreshComponent() {
        return refreshComponent;
    }

    public void setRefreshComponent(ComponentGVO refreshComponent) {
        this.refreshComponent = refreshComponent;
    }

    public ComponentGVO getAddComponent() {
        return addComponent;
    }

    public void setAddComponent(ComponentGVO addComponent) {
        this.addComponent = addComponent;
    }

    public ComponentGVO getDeleteComponent() {
        return deleteComponent;
    }

    public void setDeleteComponent(ComponentGVO deleteComponent) {
        this.deleteComponent = deleteComponent;
    }

    public ComponentGVO getSaveComponent() {
        return saveComponent;
    }

    public void setSaveComponent(ComponentGVO saveComponent) {
        this.saveComponent = saveComponent;
    }

    public ComponentGVO getOffSetComponent() {
        return offSetComponent;
    }

    public void setOffSetComponent(ComponentGVO offSetComponent) {
        this.offSetComponent = offSetComponent;
    }

    public ComponentGVO getPageSizeComponent() {
        return pageSizeComponent;
    }

    public void setPageSizeComponent(ComponentGVO pageSizeComponent) {
        this.pageSizeComponent = pageSizeComponent;
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

    public PanelGVO getOverflow() {
        return overflow;
    }

    public void setOverflow(PanelGVO overflow) {
        this.overflow = overflow;
    }

    public boolean hasColumns() {
        return (columns != null);
    }

    public boolean hasOverFlow() {
        return (overflow != null);
    }

    public ComponentGVO getExportComponent() {
        return exportComponent;
    }

    public void setExportComponent(ComponentGVO exportComponent) {
        this.exportComponent = exportComponent;
    }

    public String[] getRowColors() {
        return rowColors;
    }

    public void setRowColors(String[] rowColors) {
        this.rowColors = rowColors;
    }

    public Boolean getPageScroll() {
        return pageScroll;
    }

    public void setPageScroll(Boolean pageScroll) {
        this.pageScroll = pageScroll;
    }

    public String getExportFormats() {
        return exportFormats;
    }

    public void setExportFormats(String exportFormats) {
        this.exportFormats = exportFormats;
    }

    public Boolean getImportEnabled() {
        return importEnabled;
    }

    public void setImportEnabled(Boolean importEnabled) {
        this.importEnabled = importEnabled;
    }
    
    public String getImportAction() {
        return importAction;
    }
    
    public void setImportAction(String importAction) {
        this.importAction = importAction;
    }

    public Boolean getMultipleSelect() {
        return multipleSelect;
    }

    public void setMultipleSelect(Boolean multipleSelect) {
        this.multipleSelect = multipleSelect;
    }

    public Boolean getSelectFirstRow() {
        return selectFirstRow;
    }

    public void setSelectFirstRow(Boolean selectFirstRow) {
        this.selectFirstRow = selectFirstRow;
    }

    public DataGridGVO clone() {
        DataGridGVO dataGridGVO = new DataGridGVO();
        dataGridGVO.setAdd(add);
        dataGridGVO.setDelete(delete);
        dataGridGVO.setExport(export);
        dataGridGVO.setImportEnabled(importEnabled);
        dataGridGVO.setImportAction(importAction);
        dataGridGVO.setMaxRows(maxRows);
        dataGridGVO.setMinRows(minRows);
        dataGridGVO.setPageSize(pageSize);
        dataGridGVO.setPageScroll(pageScroll);
        dataGridGVO.setAddComponent(addComponent);
        dataGridGVO.setCancelComponent(cancelComponent);
        dataGridGVO.setDeleteComponent(deleteComponent);
        dataGridGVO.setSaveComponent(saveComponent);
        dataGridGVO.setRefreshComponent(refreshComponent);
        dataGridGVO.setPageSizeComponent(pageSizeComponent);
        dataGridGVO.setOffSetComponent(offSetComponent);
        dataGridGVO.setExportComponent(exportComponent);
        dataGridGVO.setMultipleSelect(multipleSelect);
        dataGridGVO.setSelectFirstRow(selectFirstRow);
        dataGridGVO.setCancel(cancel);
        dataGridGVO.setRefresh(refresh);
        dataGridGVO.setSave(save);
        dataGridGVO.setRowColors(rowColors);
        dataGridGVO.setOverflow(overflow);
        dataGridGVO.setEditable(editable);
        dataGridGVO.setConditionalStyleRef(conditionalStyleRef);
        dataGridGVO.setId(id);
        dataGridGVO.setDisabled(disabled);
        dataGridGVO.setVisible(visible);
        dataGridGVO.setStyleClass(styleClass);
        dataGridGVO.setStyleProperties(styleProperties);
        dataGridGVO.setEvents(events);
        dataGridGVO.setMenu(menu);
        dataGridGVO.setWindow(getWindow());
        dataGridGVO.setTooltip(getTooltip());
        dataGridGVO.setFieldName(getFieldName());
        dataGridGVO.setWidth(getWidth());
        dataGridGVO.setHeight(getHeight());
        dataGridGVO.setParent(getParent());
        dataGridGVO.setContext(getContext());
        dataGridGVO.setUuid(getUuid());
        dataGridGVO.setColumns(columns);
        dataGridGVO.setGroup(getGroup());
        dataGridGVO.setExportFormats(getExportFormats());
        dataGridGVO.setOverflowGroup(getOverflowGroup());
        return dataGridGVO;
    }

    public String getOverflowGroup() {
        return overflowGroup;
    }

    public void setOverflowGroup(String overflowGroup) {
        this.overflowGroup = overflowGroup;
    }
}
