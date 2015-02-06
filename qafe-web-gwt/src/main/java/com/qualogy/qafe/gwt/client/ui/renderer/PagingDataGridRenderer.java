/**
 * Copyright 2008-2015 Qualogy Solutions B.V.
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


import java.util.List;
import java.util.Vector;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.HasChangeHandlers;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.gen2.table.override.client.FlexTable;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.SourcesChangeEvents;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;
import com.qualogy.qafe.gwt.client.component.DataChangeHandler;
import com.qualogy.qafe.gwt.client.component.HasDataChangeHandlers;
import com.qualogy.qafe.gwt.client.component.HasDataGridMethods;
import com.qualogy.qafe.gwt.client.component.HasRowSelectionChangeHandlers;
import com.qualogy.qafe.gwt.client.component.RowSelectionChangeEvent;
import com.qualogy.qafe.gwt.client.component.RowSelectionChangeHandler;
import com.qualogy.qafe.gwt.client.ui.renderer.events.CallbackHandler;
import com.qualogy.qafe.gwt.client.vo.functions.BuiltInComponentGVO;
import com.qualogy.qafe.gwt.client.vo.functions.DataContainerGVO;
import com.qualogy.qafe.gwt.client.vo.functions.SetValueGVO;
import com.qualogy.qafe.gwt.client.vo.functions.execute.FunctionsExecutor;
import com.qualogy.qafe.gwt.client.vo.ui.ComponentGVO;
import com.qualogy.qafe.gwt.client.vo.ui.DataGridColumnGVO;
import com.qualogy.qafe.gwt.client.vo.ui.DataGridGVO;

public class PagingDataGridRenderer extends AbstractComponentRenderer {

    private Vector<DataGridGVO> registeryOfOverflowItem4DataChange = new Vector<DataGridGVO>();
    private Vector<DataGridGVO> changedByRowSelection = new Vector<DataGridGVO>();
    
	public PagingDataGridRenderer() {
	}
	
	@Override
	public UIObject render(ComponentGVO component, String uuid,String parent, String context) {
		UIObject uiObject = null;
		if (component != null) {
			if (component instanceof DataGridGVO) {
				DataGridGVO gvo = ((DataGridGVO)component).clone();
				DataGridColumnGVO[] columns = gvo.getColumns();
				if (columns!=null) {
					for(int i=0; i<columns.length; i++){
						columns[i].setUuid(uuid);
					}
				}
				uiObject = DataGridFactory.createPagingDataGrid(gvo,uuid,parent);
				
				if(gvo.getOverflowGroup() != null) {
					UIObject datagrid = ((FlexTable)uiObject).getWidget(0, 0);
					registerRowSelectionChange(gvo, datagrid);
				}				
			}
		}
		return uiObject;
	}

	private void registerRowSelectionChange(final DataGridGVO datagridGVO, UIObject datagrid) {
        if (datagrid instanceof HasRowSelectionChangeHandlers) {
            ((HasRowSelectionChangeHandlers)datagrid).addRowSelectionChangeHandler(new RowSelectionChangeHandler() {
                @Override
				public void onRowSelectionChange(RowSelectionChangeEvent event) {
                    UIObject source = event.getSource();
                    int rowIndex = event.getRowIndex();
                    Object rowValue = event.getRowValue();

                    try {
                        changedByRowSelection.add(datagridGVO);
                        
                        handleRowSelectionChange(datagridGVO, source, rowIndex, rowValue);
                        
                        if (!registeryOfOverflowItem4DataChange.contains(datagridGVO)) {
                            registerOverflowItemDataChange(datagridGVO, source);
                            registeryOfOverflowItem4DataChange.add(datagridGVO);
                        }
                    } finally {
                        changedByRowSelection.remove(datagridGVO);
                    }
                }
            });
        }    
    }

    private void handleRowSelectionChange(DataGridGVO datagridGVO, UIObject datagrid, int rowIndex, Object rowValue) {
    	String overflowGroup = datagridGVO.getOverflowGroup();
    	String key4OverflowGroup = generateId(overflowGroup, datagrid);
    	SetValueGVO setValueGVO = new SetValueGVO();
    	BuiltInComponentGVO dummyBuiltinGvo = new BuiltInComponentGVO();
    	setValueGVO.setBuiltInComponentGVO(dummyBuiltinGvo);
    	if(rowValue instanceof DataContainerGVO) {
    		setValueGVO.setDataContainer((DataContainerGVO) rowValue);
    	}        
        setValueGVO.setGroup(key4OverflowGroup);
        FunctionsExecutor.getInstance().execute(setValueGVO);
    }
    
    private void registerOverflowItemDataChange(final DataGridGVO datagridGVO, final UIObject datagrid) {
        String overflowGroup = datagridGVO.getOverflowGroup();
        String key4OverflowGroup = generateId(overflowGroup, datagrid);
        List<UIObject> overflowItems = RendererHelper.getGroupedComponent(key4OverflowGroup);
        if (overflowItems == null) {
            return;
        }
        for (UIObject overflowItem : overflowItems) {
            if (overflowItem instanceof HasDataChangeHandlers) {
                HasDataChangeHandlers hasDataChangeHandlers = (HasDataChangeHandlers)overflowItem;
                hasDataChangeHandlers.addDataChangeHandler(new DataChangeHandler() {
                    @Override
					public void onDataChange(UIObject uiObject, Object oldValue, Object newValue) {
                        handleOverflowItemDataChange(datagridGVO, datagrid, uiObject, newValue);
                    }
                });        
            } else if (overflowItem instanceof HasValueChangeHandlers) {
                HasValueChangeHandlers hasValueChangeHandlers = (HasValueChangeHandlers)overflowItem;
                hasValueChangeHandlers.addValueChangeHandler(new ValueChangeHandler<Object>() {
                    @Override
					public void onValueChange(ValueChangeEvent<Object> event) {
                        UIObject source = (UIObject)event.getSource();
                        handleOverflowItemDataChange(datagridGVO, datagrid, source);
                    }
                });        
            } else if (overflowItem instanceof HasChangeHandlers) {
                HasChangeHandlers hasChangeHandlers = (HasChangeHandlers)overflowItem;
                hasChangeHandlers.addChangeHandler(new ChangeHandler() {
                    @Override
					public void onChange(ChangeEvent event) {
                        UIObject source = (UIObject)event.getSource();
                        handleOverflowItemDataChange(datagridGVO, datagrid, source);
                    }
                });        
            } else if (overflowItem instanceof SourcesChangeEvents) {
                // LEGACY CODE
                SourcesChangeEvents sourcesChangeEvents = (SourcesChangeEvents)overflowItem;
                sourcesChangeEvents.addChangeListener(new ChangeListener() {
                    @Override
					public void onChange(Widget sender) {
                        handleOverflowItemDataChange(datagridGVO, datagrid, sender);
                    }
                });
            }
        }
    }
    
    private void handleOverflowItemDataChange(DataGridGVO datagridGVO, UIObject datagrid, UIObject overflowItem) {
        Object value = null;
        try {
            value = CallbackHandler.getValue(overflowItem, overflowItem, true, null);    
        } catch (Exception e) {
            return;
        }
        handleOverflowItemDataChange(datagridGVO, datagrid, overflowItem, value);
    }
    
    private void handleOverflowItemDataChange(DataGridGVO datagridGVO, UIObject datagrid, UIObject overflowItem, Object value) {
    	DataContainerGVO valueGVO = DataContainerGVO.create(value);
        handleOverflowItemDataChange(datagridGVO, datagrid, overflowItem, valueGVO);
    }
    
    private void handleOverflowItemDataChange(DataGridGVO datagridGVO, UIObject datagrid, UIObject overflowItem, DataContainerGVO value) {
        if (changedByRowSelection.contains(datagridGVO)) {
            return;
        }
        if (!(datagrid instanceof HasDataGridMethods)) {
            return;
        }
        String columnName = RendererHelper.getNamedComponentName(overflowItem);
        HasDataGridMethods hasDataGridMethods = (HasDataGridMethods)datagrid;
        hasDataGridMethods.setColumnValue(columnName, value);
    }
    
    private String generateId(String value, UIObject component) {
        String parent = RendererHelper.getParentComponent(component);
        String context = RendererHelper.getComponentContext(component);
        return RendererHelper.generateId(value, parent, context);       
    }
}