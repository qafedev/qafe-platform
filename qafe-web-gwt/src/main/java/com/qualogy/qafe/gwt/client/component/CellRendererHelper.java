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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.HasChangeHandlers;
import com.google.gwt.event.dom.client.HasKeyUpHandlers;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.gen2.table.client.CellRenderer;
import com.google.gwt.gen2.table.client.ColumnDefinition;
import com.google.gwt.gen2.table.client.TableDefinition.AbstractCellView;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;
import com.qualogy.qafe.gwt.client.ui.renderer.AnyComponentRenderer;
import com.qualogy.qafe.gwt.client.ui.renderer.RendererHelper;
import com.qualogy.qafe.gwt.client.ui.renderer.events.KeyBoardHelper;
import com.qualogy.qafe.gwt.client.util.QAMLConstants;
import com.qualogy.qafe.gwt.client.vo.functions.DataContainerGVO;
import com.qualogy.qafe.gwt.client.vo.functions.SetValueGVO;
import com.qualogy.qafe.gwt.client.vo.functions.execute.SetValueExecute;
import com.qualogy.qafe.gwt.client.vo.ui.CheckBoxGVO;
import com.qualogy.qafe.gwt.client.vo.ui.ComponentGVO;
import com.qualogy.qafe.gwt.client.vo.ui.DataGridColumnGVO;
import com.qualogy.qafe.gwt.client.vo.ui.DataGridGVO;
import com.qualogy.qafe.gwt.client.vo.ui.EditableComponentGVO;
import com.qualogy.qafe.gwt.client.vo.ui.TextFieldGVO;
import com.qualogy.qafe.gwt.client.vo.ui.event.EventListenerGVO;
import com.qualogy.qafe.gwt.client.vo.ui.event.InputVariableGVO;

public class CellRendererHelper {

	private static Map<String,DataChangeHandler> dataChangeHandlers4Default = new HashMap<String,DataChangeHandler>();
	private static Map<String,HandlerRegistration> selectionHandlers4Default = new HashMap<String,HandlerRegistration>();
	private static Map<String,HandlerRegistration> valueChangeHandlers4Default = new HashMap<String,HandlerRegistration>();
	private static Map<String,HandlerRegistration> changeHandlers4Default = new HashMap<String,HandlerRegistration>();
	private static Map<String,HandlerRegistration> keyUpHandlers4Default = new HashMap<String,HandlerRegistration>();
	
	private static String getKey4Handler(DataGridColumnGVO column, int rowIndex, String parent) {
		String generatedId = generateIdBasedOnIndex(column, rowIndex);
		return RendererHelper.generateId(generatedId, parent, column.getContext());
	}
	
	private static void removeExistingHandler(String key4Handler, Map<String,HandlerRegistration> handlers) {
		HandlerRegistration handler = handlers.get(key4Handler);
		if (handler != null) {
			handler.removeHandler();	
		}
	}

	public static CellCleaner getCellCleaner(final DataGridGVO source, final DataGridColumnGVO column, final String uuid, final String parent, final HasDataGridMethods parentWidget) {
		CellCleaner cellCleaner = new CellCleaner() {
			public void cleanRow(int rowIndex, ColumnDefinition<DataContainerGVO, ?> columnDefinition) {
				String generatedId = generateIdBasedOnIndex(column, rowIndex);
				String renderedComponentId = RendererHelper.generateId(generatedId, parent, column.getContext());
				RendererHelper.removeComponent(renderedComponentId);
			}
		};
		return cellCleaner;
	}
	
	public static CellRenderer<DataContainerGVO, String> getCellRenderer(final DataGridGVO source, final DataGridColumnGVO column, final String uuid, final String parent, final HasDataGridMethods parentWidget) {
		if (column != null) {
			if (column.getComponent() != null) {
				if (column.getStaticField()) {
					return createRenderer(source, column, uuid, parent, column.getComponent());
				} 
				return createDefaultRenderer(column, uuid, parent, parentWidget, column.getComponent());
			}	
			if (column.isString()) {
				return createSpreadSheetCellRenderer(source, column, uuid, parent, parentWidget);
			} 
			if (column.isHTML()) {
				return createDefaulHTMLRenderer(source, column, uuid, parent, parentWidget);
			} 
			if (column.isLink()) {
				return createLinkRenderer(source, column, uuid, parent, source.getEditable() ? "#" : "history", parentWidget);
			} 
			if (column.isCheckbox()) {
				return createCheckboxRenderer(source, column, uuid, parent, parentWidget);
			}
		}	
		return null;
	}

	private static CellRenderer<DataContainerGVO, String> createDefaultRenderer(final DataGridColumnGVO column,	final String uuid, final String parent, final HasDataGridMethods parentWidget, final ComponentGVO component) {
		CellRenderer<DataContainerGVO, String> cellRenderer;
		cellRenderer = new CellRenderer<DataContainerGVO, String>() {
			public void renderRowValue(final DataContainerGVO rowValue,final ColumnDefinition<DataContainerGVO, String> columnDef, AbstractCellView<DataContainerGVO> view) {
				if (rowValue.isMap()){
					String generatedId = generateIdBasedOnIndex(column, view.getRowIndex());
					component.setId(generatedId);
					component.setEvents(column.getEvents());
					if (component instanceof EditableComponentGVO) {
						EditableComponentGVO editableComponent = (EditableComponentGVO)component;
						editableComponent.setEditable(column.getEditable());
					}
					final UIObject uiObject = getOrCreateRenderedComponent(column, component, generatedId, uuid, parent);

					// Get key for handler based on component uuid
					String key4Handler = getKey4Handler(column, view.getRowIndex(), parent);
					
					// HasSelectionHandlers is added for handling suggest text fields
					if(uiObject instanceof HasSelectionHandlers){
						HasSelectionHandlers hasSelectionHandlers = (HasSelectionHandlers)uiObject;
						
						// Remove existing handler containing old rowValue
						removeExistingHandler(key4Handler, selectionHandlers4Default);
						
						HandlerRegistration handler = hasSelectionHandlers.addSelectionHandler(new SelectionHandler<String>() {
							public void onSelection(SelectionEvent<String> event) {
								if (column.getEditable()) {
									handleSetModifiedForSelectionEvent(parentWidget, rowValue, columnDef, uiObject, event);	
								}
							}
						});
						selectionHandlers4Default.put(key4Handler, handler);
					} else if (uiObject instanceof HasDataChangeHandlers) {
						HasDataChangeHandlers hasDataChangeHandlers = (HasDataChangeHandlers)uiObject;
						
						// Remove existing handler containing old rowValue
						DataChangeHandler dataChangeHandler = dataChangeHandlers4Default.get(key4Handler);
						hasDataChangeHandlers.removeDataChangeHandler(dataChangeHandler);
						
						dataChangeHandler = new DataChangeHandler() {
							public void onDataChange(UIObject uiObject, Object oldValue, Object newValue) {
								if (column.getEditable()) {
									handleSetModified(parentWidget, rowValue, columnDef, uiObject, newValue);
								}
							}
						};
						hasDataChangeHandlers.addDataChangeHandler(dataChangeHandler);
						dataChangeHandlers4Default.put(key4Handler, dataChangeHandler);
					} else if (uiObject instanceof HasValueChangeHandlers) {
						HasValueChangeHandlers hasValueChangeHandlers = (HasValueChangeHandlers)uiObject;
						
						// Remove existing handler containing old rowValue
						removeExistingHandler(key4Handler, valueChangeHandlers4Default);
						
						HandlerRegistration handler = hasValueChangeHandlers.addValueChangeHandler(new ValueChangeHandler() {
							public void onValueChange(ValueChangeEvent event) {
								if (column.getEditable()) {
									handleSetModifiedForValueChangeEvent(parentWidget, rowValue, columnDef, uiObject, event);
								}
							}
						});
						valueChangeHandlers4Default.put(key4Handler, handler);
					} else if (uiObject instanceof HasChangeHandlers){
						HasChangeHandlers hasChangeHandlers = (HasChangeHandlers)uiObject;
						
						// Remove existing handler containing old rowValue
						removeExistingHandler(key4Handler, changeHandlers4Default);
						
						HandlerRegistration handler = hasChangeHandlers.addChangeHandler(new ChangeHandler(){
							public void onChange(ChangeEvent event) {
								if (column.getEditable()) {	
									handleSetModifiedForChangeEvent(parentWidget, rowValue, columnDef, uiObject, event);
								}	
							}
						});
						changeHandlers4Default.put(key4Handler, handler);
					}
					if (uiObject instanceof HasKeyUpHandlers) {
						// Handle value changed when typing
						HasKeyUpHandlers hasKeyUpHandlers = (HasKeyUpHandlers)uiObject;
						
						// Remove existing handler containing old rowValue
						removeExistingHandler(key4Handler, keyUpHandlers4Default);
						
						HandlerRegistration handler = hasKeyUpHandlers.addKeyUpHandler(new KeyUpHandler() {
							public void onKeyUp(KeyUpEvent event) {
								if (column.getEditable() && KeyBoardHelper.isValidKeyInput(event.getNativeEvent())) {
									handleSetModifiedForValueChangeEvent(parentWidget, rowValue, columnDef, uiObject, event);
								}
							}
						});
						keyUpHandlers4Default.put(key4Handler, handler);
					}
					
					SetValueGVO setValueGVO = new SetValueGVO();
					DataContainerGVO valueDTC = new DataContainerGVO(simpleObjectToText(columnDef.getCellValue(rowValue))); 
					if (uiObject instanceof Widget) {
						if(uiObject instanceof QDatePicker){
							valueDTC = ((QColumnDefinition)columnDef).getDataContainerGVO(rowValue);
						} else if(uiObject instanceof ListBox){
							// Inner component is a drop down and hence is first filled with the stored value and the selected index is managed later with the column value.
							QPagingScrollTable qPagingScrollTable = ((QPagingScrollTable)view.getSourceTableDefinition());
							if(qPagingScrollTable.getDropDownColumnAndValues() != null && qPagingScrollTable.getDropDownColumnAndValues().containsKey(column.getId())) {
								SetValueGVO setValueGVOForInitialising = qPagingScrollTable.getDropDownColumnAndValues().get(column.getId());												
								// Do the set operation for the drop down - it can be a set with action set/add
								SetValueExecute.processValue(uiObject, setValueGVOForInitialising.getDataContainer(), setValueGVOForInitialising, setValueGVOForInitialising.getDataContainer());
							}											
						}
						// Now set the value to the column
						SetValueExecute.processValue(uiObject, simpleObjectToText(columnDef.getCellValue(rowValue)), setValueGVO, valueDTC);
						view.setWidget((Widget) uiObject);
					}
				}
			}
		};
		return cellRenderer;
	}

	private static CellRenderer<DataContainerGVO, String> createCheckboxRenderer(final DataGridGVO source, final DataGridColumnGVO column, final String uuid, final String parent, final HasDataGridMethods uiParent) {
		return new CellRenderer<DataContainerGVO, String>() {
			public void renderRowValue(DataContainerGVO rowValue, ColumnDefinition<DataContainerGVO, String> columnDef, AbstractCellView<DataContainerGVO> view) {
				boolean alreadyRendered = false;
				String value = simpleObjectToText(columnDef.getCellValue(rowValue));
				int rowIndex = view.getRowIndex();
				String generatedId = generateIdBasedOnIndex(column, rowIndex);
				CheckBox renderedComponent = (CheckBox)getRenderedComponent(column, generatedId, uuid, parent);
				if (renderedComponent != null) {
					renderedComponent.setText(value);
					alreadyRendered = true;
				} else {
					renderedComponent = new CheckBox(value);					
				}
				
				view.setWidget(renderedComponent);
				CellRendererHelper.addBasicInfoToUIObject(renderedComponent, uuid, parent, source, column, rowValue, rowIndex, null, alreadyRendered);
				handleCellSet(uiParent, columnDef, renderedComponent, rowValue);
			}
		};
	}

	private static CellRenderer<DataContainerGVO, String> createRenderer(final DataGridGVO source, final DataGridColumnGVO column, final String uuid, final String parent, final ComponentGVO component) {
		CellRenderer<DataContainerGVO, String> cellRenderer;
		cellRenderer = new CellRenderer<DataContainerGVO, String>() {
			public void renderRowValue(DataContainerGVO rowValue, ColumnDefinition<DataContainerGVO, String> columnDef, AbstractCellView<DataContainerGVO> view) {
				boolean alreadyRendered = false;
				UIObject uiObject = null;
				int rowIndex = view.getRowIndex();
				String generatedId = generateIdBasedOnIndex(column, rowIndex);
				if (isRenderedComponent(column, component, generatedId, uuid, parent)) {
					uiObject = getRenderedComponent(column, generatedId, uuid, parent);
					alreadyRendered = true;
				} else {
					uiObject = AnyComponentRenderer.getInstance().render(component, uuid, parent, column.getContext());	
				}
				if (uiObject instanceof Widget) {
					view.setWidget((Widget) uiObject);
				}
				CellRendererHelper.addBasicInfoToUIObject(uiObject, uuid, parent, source, column, rowValue, rowIndex, null, alreadyRendered);
			}
		};
		return cellRenderer;
	}

	private static CellRenderer<DataContainerGVO, String> createLinkRenderer(final DataGridGVO source, final DataGridColumnGVO column, final String uuid, final String parent, final String targetHistoryToken, final HasDataGridMethods uiParent) {
		CellRenderer<DataContainerGVO, String> cellRenderer;
		cellRenderer = new CellRenderer<DataContainerGVO, String>() {
			public void renderRowValue(DataContainerGVO rowValue, ColumnDefinition<DataContainerGVO, String> columnDef, AbstractCellView<DataContainerGVO> view) {
				boolean alreadyRendered = false;
				String value = simpleObjectToText(columnDef.getCellValue(rowValue));
				int rowIndex = view.getRowIndex();
				String generatedId = generateIdBasedOnIndex(column, rowIndex);
				Hyperlink renderedComponent = (Hyperlink)getRenderedComponent(column, generatedId, uuid, parent);
				if (renderedComponent != null) {
					renderedComponent.setText(value);
					alreadyRendered = true;
				} else {
					renderedComponent = new Hyperlink(value, targetHistoryToken);					
				}
				
				view.setWidget(renderedComponent);
				CellRendererHelper.addBasicInfoToUIObject(renderedComponent, uuid, parent, source, column, rowValue, rowIndex, null, alreadyRendered);
				handleCellSet(uiParent, columnDef, renderedComponent, rowValue);
			}
		};
		return cellRenderer;
	}

	private static CellRenderer<DataContainerGVO, String> createSpreadSheetCellRenderer(final DataGridGVO source, final DataGridColumnGVO column, final String uuid, final String parent, final HasDataGridMethods uiParent){
		return new CellRenderer<DataContainerGVO, String>() {
			public void renderRowValue(DataContainerGVO rowValue, ColumnDefinition<DataContainerGVO, String> columnDef, AbstractCellView<DataContainerGVO> view) {
				boolean alreadyRendered = false;
				String value = simpleObjectToText(columnDef.getCellValue(rowValue));
				int rowIndex = view.getRowIndex();
				String generatedId = generateIdBasedOnIndex(column, rowIndex);
				SpreadsheetCell renderedComponent = (SpreadsheetCell)getRenderedComponent(column, generatedId, uuid, parent);
				if (renderedComponent != null) {
					// Update the data model
					renderedComponent.setRowValue(rowValue);
					
					renderedComponent.setText(value);
					alreadyRendered = true;
				} else {
					renderedComponent = new SpreadsheetCell(value, uiParent, rowValue, columnDef, column);					
				}
				
				view.setWidget(renderedComponent);
				CellRendererHelper.addBasicInfoToUIObject(renderedComponent, uuid, parent, source, column, rowValue, rowIndex, null, alreadyRendered);
				// spreadsheetcell have a focuslabel inside(which is a container
				// of html component). So we have apply the style for that also.
				RendererHelper.addStyle(column, renderedComponent.getLabel());
			}
		};
	}
	
	private static CellRenderer<DataContainerGVO, String> createDefaulHTMLRenderer(final DataGridGVO source, final DataGridColumnGVO column, final String uuid, final String parent, final HasDataGridMethods uiParent){
		return new CellRenderer<DataContainerGVO, String>() {
			public void renderRowValue(DataContainerGVO rowValue, ColumnDefinition<DataContainerGVO, String> columnDef, AbstractCellView<DataContainerGVO> view) {
				boolean alreadyRendered = false;
				String value = simpleObjectToText(columnDef.getCellValue(rowValue));
				int rowIndex = view.getRowIndex();
				String generatedId = generateIdBasedOnIndex(column, rowIndex);
				HTML renderedComponent = (HTML)getRenderedComponent(column, generatedId, uuid, parent);
				if (renderedComponent != null) {
					renderedComponent.setHTML(value);
					alreadyRendered = true;
				} else {
					renderedComponent = new HTML(value);					
				}
				
				view.setWidget(renderedComponent);
				CellRendererHelper.addBasicInfoToUIObject(renderedComponent, uuid, parent, source, column, rowValue, rowIndex, null, alreadyRendered);
				handleCellSet(uiParent, columnDef, renderedComponent, rowValue);
			}
		};
	}

	private static UIObject getOrCreateRenderedComponent(final DataGridColumnGVO column, ComponentGVO component, String generatedComponentId, String uuid, String parent) {
		if (isRenderedComponent(column, component, generatedComponentId, uuid, parent)) {
			return getRenderedComponent(column, generatedComponentId, uuid, parent);
		}
		return AnyComponentRenderer.getInstance().render(component, uuid, parent, column.getContext());
	}

	private static boolean isRenderedComponent(final DataGridColumnGVO column, ComponentGVO component, String generatedComponentId, String uuid, String parent) {
		// If the id of the column is null which means the internal column "rowNumber",
		// then it should render the component again
		if (column.getId() != null) {
			UIObject renderedComponent = getRenderedComponent(column, generatedComponentId, uuid, parent);
			if (renderedComponent != null) {
				return true;
			}
		}
		return false;
	}
	
	private static UIObject getRenderedComponent(final DataGridColumnGVO column, String generatedComponentId, String uuid, String parent) {
		String renderedComponentId = RendererHelper.generateId(generatedComponentId, parent, column.getContext());
		List<UIObject> list = RendererHelper.getComponent(renderedComponentId);
		if ((list != null) && !list.isEmpty()) {
			return list.get(0); 
		}
		return null;
	}
	
	protected static void addBasicInfoToUIObject(UIObject uiObject, String uuid, String parent, DataGridGVO source, DataGridColumnGVO column, DataContainerGVO rowValue, int row, String linkValue, boolean alreadyRendered) {
				
		// /Change ID temporarily///////
		String temp = column.getId(); // store
		String generatedId = generateIdBasedOnIndex(column, row);
		column.setId(generatedId);
				
		// temporarily change the id....
		RendererHelper.addId(column, uiObject, uuid, parent, column.getContext(), true);
		RendererHelper.addUUID(column, uiObject, column.getUuid());
		RendererHelper.addDisabledInfo(column, uiObject);
		RendererHelper.addEditableInfo(column, uiObject);
		RendererHelper.addStyle(column, uiObject);
		RendererHelper.addTooltip(column, uiObject);
		RendererHelper.addVisibleInfo(column, uiObject);
		
		// Event are also slightly different for their input
		EventListenerGVO[] events = column.getEvents();
		if (!alreadyRendered && (events != null)) {
			for (int i = 0; i < events.length; i++) {
				List<InputVariableGVO> inputVariables = events[i].getInputvariablesList();
				Iterator<InputVariableGVO> itr = inputVariables.iterator();
				List<InputVariableGVO> datagridInputVariables = new ArrayList<InputVariableGVO>();
				while (itr.hasNext()) {
					InputVariableGVO inputVariable =  itr.next().getCopy();
					datagridInputVariables.add(inputVariable);
				}
				RendererHelper.processEvents(column, uiObject, events[i], datagridInputVariables);
			}
		}
		column.setId(temp);
	}

	public static String simpleObjectToText(Object object) {
		return (object != null && object.toString().length() > 0) ? object.toString() : "";
	}

	private static String generateIdBasedOnIndex(DataGridColumnGVO column, int index) {
		if (column == null) {
			return null;
		}
		String columnId = column.getId();
		if (columnId == null) {
			columnId = column.getFieldName();
		}
		return QAMLConstants.TOKEN_INDEXING + index + QAMLConstants.TOKEN_INDEXING + column.getContainerName() + "." + columnId;
	}
	
	private static void handleCellSet(HasDataGridMethods uiParent, ColumnDefinition<DataContainerGVO,String> columnDefinition, UIObject uiObject, DataContainerGVO rowValue) {
		if (uiParent != null) {
			uiParent.setModified(columnDefinition, uiObject, rowValue, columnDefinition.getCellValue(rowValue));
		}		
	}
	
	private static void handleSetModifiedForValueChangeEvent(final HasDataGridMethods parentWidget, final DataContainerGVO rowValue, final ColumnDefinition<DataContainerGVO, String> columnDef, final UIObject uiObject, GwtEvent event) {
		Object value = null;
		
		// TODO
		if (event instanceof ValueChangeEvent) {
			ValueChangeEvent valueChangeEvent = (ValueChangeEvent)event;
			value = valueChangeEvent.getValue();
		}
		
		Object source = event.getSource();
		if (source instanceof CheckBox) {
			CheckBox checkBox = (CheckBox)source;
			if ((Boolean)value) {
				String attributeValue = DOM.getElementAttribute(checkBox.getElement(), CheckBoxGVO.CHECKED_VALUE_ATTRIBUTE_TAG);
				if (attributeValue != null && attributeValue.length() > 0) {
					value = attributeValue;
				}
			} else {
				String attributeValue = DOM.getElementAttribute(checkBox.getElement(), CheckBoxGVO.UNCHECKED_VALUE_ATTRIBUTE_TAG);
				if (attributeValue != null && attributeValue.length() > 0) {
					value = attributeValue;
				}
			}
		} else if(source instanceof QTextField) {
			/**
			 * When database column type is NUMBER(5,2) type which is accepting decimal values, If we pass string type with decimal value to the jdbc template it is giving exception.
			 * So the type of the textfield should be considered.
			 * This is now applicable only for textfield, same issue can come in any inputfield. But we dont have an option to set type to other components now.
			 */
			String type = DOM.getElementAttribute(uiObject.getElement(), TextFieldGVO.REGEXPTYPE);
			QTextField textField = (QTextField)source;
			if(TextFieldGVO.TYPE_DOUBLE.equals(type)) {
				value = new Double(value.toString());
			} else if(TextFieldGVO.TYPE_INTEGER.equals(type)) {
				try {
					value = new Integer(value.toString());
				}catch (Exception e) {
					value = new Double(value.toString());
				}
			}
		}
		if (value == null) {
			if (source instanceof HasText) {
				HasText hasText = (HasText)source;
				value = hasText.getText();
			} else if (source instanceof HasData) {
				HasData hasData = (HasData)source;
				value = hasData.getData();
			}
		}
		handleSetModified(parentWidget, rowValue, columnDef, uiObject, value);
	}
	
	private static void handleSetModifiedForChangeEvent(final HasDataGridMethods parentWidget, final DataContainerGVO rowValue, final ColumnDefinition<DataContainerGVO, String> columnDef, final UIObject uiObject, ChangeEvent event) {
		Object source = event.getSource();
		Object value = null;
		if (source instanceof ListBox) {
			ListBox listBox = (ListBox)source;
			value = listBox.getValue(listBox.getSelectedIndex());
		} /*else {
			// TODO : In future for other sources with HasChangeHandlers, extracting value from the source has to be implemented.  
		}*/
		handleSetModified(parentWidget, rowValue, columnDef, uiObject, value);
	}
	
	private static void handleSetModifiedForSelectionEvent(final HasDataGridMethods parentWidget, final DataContainerGVO rowValue, final ColumnDefinition<DataContainerGVO, String> columnDef, final UIObject uiObject, SelectionEvent event) {
		Object value = event.getSelectedItem();
		if(value instanceof QMultiWordSuggestion) {
			value = ((QMultiWordSuggestion)value).getDisplayString();
		}
		Object source = event.getSource();		
		handleSetModified(parentWidget, rowValue, columnDef, uiObject, value);
	}
	
	private static void handleSetModified(final HasDataGridMethods parentWidget, final DataContainerGVO rowValue, final ColumnDefinition<DataContainerGVO, String> columnDef, final UIObject uiObject, Object newValue) {
		parentWidget.setModified(columnDef, uiObject, rowValue, newValue, true);
	}
}