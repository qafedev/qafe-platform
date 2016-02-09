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
package com.qualogy.qafe.gwt.client.vo.handlers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.widgetideas.client.SliderBar;
import com.google.gwt.widgetideas.client.ValueSpinner;
import com.qualogy.qafe.gwt.client.component.AreaWidget;
import com.qualogy.qafe.gwt.client.component.DataMap;
import com.qualogy.qafe.gwt.client.component.HasData;
import com.qualogy.qafe.gwt.client.component.HasDataGridMethods;
import com.qualogy.qafe.gwt.client.component.HasRequiredValidationMessage;
import com.qualogy.qafe.gwt.client.component.LabeledTextFieldWidget;
import com.qualogy.qafe.gwt.client.component.MapWidget;
import com.qualogy.qafe.gwt.client.component.QDatePicker;
import com.qualogy.qafe.gwt.client.component.QPagingScrollTable;
import com.qualogy.qafe.gwt.client.component.QRadioButton;
import com.qualogy.qafe.gwt.client.component.ShowPanelComponent;
import com.qualogy.qafe.gwt.client.component.Tiles;
import com.qualogy.qafe.gwt.client.component.TitledComponent;
import com.qualogy.qafe.gwt.client.context.ClientApplicationContext;
import com.qualogy.qafe.gwt.client.ui.renderer.RendererHelper;
import com.qualogy.qafe.gwt.client.ui.renderer.events.TypeValidationException;
import com.qualogy.qafe.gwt.client.ui.renderer.events.exception.RequiredFieldException;
import com.qualogy.qafe.gwt.client.util.ComponentRepository;
import com.qualogy.qafe.gwt.client.util.QAMLConstants;
import com.qualogy.qafe.gwt.client.vo.functions.DataContainerGVO;
import com.qualogy.qafe.gwt.client.vo.functions.ShowPanelGVO;
import com.qualogy.qafe.gwt.client.vo.functions.execute.SetMaskHelper;
import com.qualogy.qafe.gwt.client.vo.ui.CheckBoxGVO;
import com.qualogy.qafe.gwt.client.vo.ui.TextFieldGVO;

public class BuiltinHandlerHelper {
	
    public static Object getValue(UIObject uiObject, final UIObject sender, boolean idValueOnly
    		, String groupName) {
        Object returnObject = null;
        if (uiObject instanceof QPagingScrollTable) {
            returnObject = getValue((QPagingScrollTable) uiObject, groupName);
        } else if (uiObject instanceof QRadioButton) {
            returnObject = getValue((QRadioButton) uiObject);
        } else if (uiObject instanceof HasData) {
            returnObject = getValue((HasData) uiObject);
        } else if (uiObject instanceof CheckBox) {
            returnObject = getValue((CheckBox) uiObject);
        } else if (uiObject instanceof FormPanel) {
            returnObject = getValue((FormPanel) uiObject);
        } else if (uiObject instanceof ListBox) {
            returnObject = getValue((ListBox) uiObject, idValueOnly);
        } else if (uiObject instanceof QDatePicker) {
            returnObject = getValue((QDatePicker) uiObject);
        } else if (uiObject instanceof HasText) {
            returnObject = getValue((HasText) uiObject);
        } else if (uiObject instanceof MapWidget) {
            returnObject = getValue((MapWidget) uiObject, sender);
        } else if (uiObject instanceof Image) {
            returnObject = getValue((Image) uiObject);
        } else if (uiObject instanceof ValueSpinner) {
            returnObject = getValue((ValueSpinner) uiObject);
        } else if (uiObject instanceof Tiles) {
            returnObject = getValue((Tiles) uiObject, sender);
        } else if (uiObject instanceof SliderBar) {
            returnObject = getValue((SliderBar) uiObject);
        } else if (isDataGridField(uiObject)) {
            returnObject = getDataGridValue(sender);
        }

        return returnObject;
    }
    
    private static void handleSimpleValue(UIObject uiObject, Object valueObject) {
        String valueString = null;
        if (valueObject instanceof String) {
            valueString = (String) valueObject;
        } else if (valueObject instanceof DataContainerGVO) {
            DataContainerGVO gvo = (DataContainerGVO) valueObject;
            if (gvo.getDataString() != null) {
                valueString = gvo.getDataString();
            } else if (gvo.getDateData() != null) {
                valueString = gvo.getDateData().toString();
            }
        }
        // Required field check
        handleRequired(uiObject, valueString);
        // Validation based on type- for textfield with type
        handleTypeValidation(uiObject, valueString);
    }
    
	private static DataContainerGVO getValue(QPagingScrollTable qPagingScrollTable, String groupName) {
        Object object = qPagingScrollTable.getData(null, groupName);
        DataContainerGVO dtc = DataContainerGVO.create(object);
        return dtc;
	}
	
	private static String getValue(QRadioButton qRadioButton) {
		String value = qRadioButton.getText();
		handleSimpleValue(qRadioButton, value);
        return value;
	}
	
	private static Object getValue(HasData hasData) {
        Object data = hasData.getData();
        
        if (!(data instanceof String)) {
            DataContainerGVO dtc = DataContainerGVO.create(data);
            if (dtc != null) {
                return dtc;
            }
        }
        
        if (hasData instanceof UIObject) {
        	handleSimpleValue((UIObject) hasData, data);
        }
        
        return data;
	}
	
	private static String getValue(CheckBox checkBox) {
        String value = checkBox.getValue().toString();
                
        if (checkBox.getValue()) {
            String attributeValue =
                DOM.getElementAttribute(checkBox.getElement(), CheckBoxGVO.CHECKED_VALUE_ATTRIBUTE_TAG);
            if (attributeValue != null && attributeValue.length() > 0) {
            	value = attributeValue;
            }
        } else {
            String attributeValue =
                DOM.getElementAttribute(checkBox.getElement(), CheckBoxGVO.UNCHECKED_VALUE_ATTRIBUTE_TAG);
            if (attributeValue != null && attributeValue.length() > 0) {
            	value = attributeValue;
            }
        }
        
        handleSimpleValue(checkBox, value);
        
        return value;
	}
	
	private static String getValue(FormPanel formPanel) {
		String value = null;
		
        if (formPanel instanceof HasWidgets) {
            HasWidgets hasWidgets = formPanel;
            Iterator<Widget> itr = hasWidgets.iterator();
            while (itr.hasNext()) {
                Widget widget = itr.next();
                if (widget instanceof Grid) {
                    Grid gridPanel = (Grid) widget;
                    FileUpload fileUpload = (FileUpload) gridPanel.getWidget(0, 0);
                    value = DOM.getElementAttribute(fileUpload.getElement(), "fu-uuid");
                    handleSimpleValue(formPanel, value);
                }
            }
        }
        
        return value;
	}
	
	private static Object getValue(ListBox listBox, boolean idValueOnly) {
		Object value = null;
		
        if (!(listBox.isMultipleSelect()) && listBox.getSelectedIndex() != -1) { // dropdown
            int index = listBox.getSelectedIndex();
            if (idValueOnly) {
            	value = listBox.getValue(index);
            } else {
                DataMap dm = new DataMap();
                dm.put("id", new DataContainerGVO(listBox.getValue(index)));
                dm.put("value", new DataContainerGVO(listBox.getItemText(index)));

                DataContainerGVO dtcMap = new DataContainerGVO();
                dtcMap.setKind(DataContainerGVO.KIND_MAP);
                dtcMap.setDataMap(dm);
                value = dtcMap;

                // TODO: refactor, this is a workaround for checking simple
                // value
                dtcMap.setDataString(listBox.getValue(index));
            }
            
            handleSimpleValue(listBox, value);
        } else if (listBox.getSelectedIndex() != -1) {
            DataContainerGVO dtclist = new DataContainerGVO();
            dtclist.setKind(DataContainerGVO.KIND_COLLECTION);
            List<DataContainerGVO> list = new ArrayList<DataContainerGVO>();
            dtclist.setListofDC(list);
            int items = listBox.getItemCount();
            for (int itemIndex = 0; itemIndex < items; itemIndex++) {
                if (listBox.isItemSelected(itemIndex)) {
                    DataMap dataMap = new DataMap();
                    DataContainerGVO dtcId = new DataContainerGVO();
                    dtcId.setKind(DataContainerGVO.KIND_STRING);
                    dtcId.setDataString(listBox.getValue(itemIndex));
                    dtcId.setStringDataType(DataContainerGVO.TYPE_STRING);
                    dataMap.put("id", dtcId);

                    DataContainerGVO dtcValue = new DataContainerGVO();
                    dtcValue.setKind(DataContainerGVO.KIND_STRING);
                    dtcValue.setDataString(listBox.getItemText(itemIndex));
                    dtcValue.setStringDataType(DataContainerGVO.TYPE_STRING);
                    dataMap.put("value", dtcValue);

                    list.add(new DataContainerGVO(dataMap));
                }
            }
            value = dtclist;
        }
        
        return value;
	}
	
	private static DataContainerGVO getValue(QDatePicker qDatePicker) {
        DataContainerGVO value = new DataContainerGVO();
        value.setKind(DataContainerGVO.KIND_STRING);
        value.setStringDataType(DataContainerGVO.TYPE_DATE);
        value.setDateData(qDatePicker.getValue());
        
        handleSimpleValue(qDatePicker, value);
        
        return value;
	}
	
	private static String getValue(HasText hasText) {
        String value = hasText.getText();
        if (hasText instanceof UIObject) {
        	handleSimpleValue((UIObject) hasText, value);
        }
        return value;
	}

	private static String getValue(MapWidget mapWidget, UIObject sender) {
		String value = null;
		
        AreaWidget[] areas = mapWidget.getItems();
        if (areas != null) {
            for (int k = 0; k < areas.length; k++) {
                if (areas[k] == sender) {
                	value = sender.getTitle();
                    handleSimpleValue(mapWidget, value);
                }
            }
        }
        
        return value;
	}	
	
	private static String getValue(Image image) {
		String value = image.getUrl();
        if (value != null) {
            handleSimpleValue(image, value);
        }
        return value;
	}
	
	private static String getValue(ValueSpinner valueSpinner) {
		String value = null;
        if (valueSpinner.getTextBox() != null) {
            value = valueSpinner.getTextBox().getValue();
            handleSimpleValue(valueSpinner, value);
        }
        return value;
	}
	
	private static DataContainerGVO getValue(Tiles tiles, final UIObject sender) {
        DataContainerGVO dtc = new DataContainerGVO();
        dtc.setKind(DataContainerGVO.KIND_MAP);
        DataMap dataMap = new DataMap();
        dtc.setDataMap(dataMap);        
        String originalSenderId = RendererHelper.getComponentId(sender);
        
        if (originalSenderId != null) {
            String index = originalSenderId.substring(0, originalSenderId
            		.lastIndexOf(QAMLConstants.TOKEN_INDEXING));
            index = index.replace(QAMLConstants.TOKEN_INDEXING, "");
            Integer i = Integer.parseInt(index);
            UIObject tileElement = tiles.getTileElements().get(i);
            if (tileElement instanceof HasWidgets) {
                HasWidgets hasWidgets = (HasWidgets) tileElement;
                processWidgets(hasWidgets, dataMap, sender);
            }
        }
        
        return dtc;
	}
	
	private static String getValue(SliderBar sliderBar) {
		String value = null;
        if (sliderBar.getCurrentValue() > 0) {
            value = String.valueOf(sliderBar.getCurrentValue());
        }
        return value;
	}

    private static Object getDataGridValue(UIObject sender) {
        String id = DOM.getElementAttribute(sender.getElement(), "id");
        String row =
            id.substring(0,
                id.lastIndexOf(QAMLConstants.TOKEN_INDEXING) + QAMLConstants.TOKEN_INDEXING.length());
        row = row.replace(QAMLConstants.TOKEN_INDEXING, "");

        String datagridUUID =
            id.substring(id.lastIndexOf(QAMLConstants.TOKEN_INDEXING) + QAMLConstants.TOKEN_INDEXING.length());
        ;
        String postfix = datagridUUID.substring(datagridUUID.indexOf("|") + 1);
        String prefix = datagridUUID.substring(0, datagridUUID.indexOf("|"));
        if (prefix.contains(".")) {
            prefix = prefix.substring(0, prefix.indexOf("."));
        }
        DataContainerGVO dtc = null;
        List<UIObject> ui = ComponentRepository.getInstance().getComponent(prefix + "|" + postfix);
        if (ui != null) {
            for (UIObject u : ui) {
                if (u instanceof QPagingScrollTable) {
                    QPagingScrollTable qps = (QPagingScrollTable) u;
                    dtc = qps.getRowValue(Integer.valueOf(row));

                }
            }
        }

        return dtc;
    }

    private static void handleRequired(UIObject uiObject, String value) {
        if (RendererHelper.isRequiredComponent(uiObject)) {
            if (uiObject instanceof ListBox) {
                ListBox listBox = (ListBox) uiObject;
                if (listBox.getSelectedIndex() == 0) {
                    value = null;
                }
            }

            if (value == null || value.trim().length() == 0) {
                ClientApplicationContext.getInstance().log(
                    "Required field not filled in",
                    "Please check the input since a required field is not filled in.("
                            + RendererHelper.getComponentId(uiObject) + ").", false);
                String componentFullId = RendererHelper.getComponentId(uiObject);
                String componentId = componentFullId;
                if (componentFullId != null) {
                    componentId = componentFullId.substring(0, componentFullId.indexOf('|'));
                }
                String message = null;
                String title = null;
                if (uiObject instanceof HasRequiredValidationMessage) {
                    HasRequiredValidationMessage hasRequiredValidationMessage =
                        (HasRequiredValidationMessage) uiObject;
                    message = hasRequiredValidationMessage.getRequiredValidationMessage();
                    title = hasRequiredValidationMessage.getRequiredValidationTitle();
                }
                if (message == null) {
                    message = QAMLConstants.DEFAULT_REQUIRED_VALIDATION_MESSAGE + componentId;
                }
                if (title == null) {
                    title = QAMLConstants.DEFAULT_REQUIRED_VALIDATION_TITLE;
                }
                throw new RequiredFieldException(title, message);
            }

        }
    }

    private static void handleTypeValidation(UIObject uiObject, String value) {
        String type = null;
        if (uiObject instanceof TextBox || uiObject instanceof LabeledTextFieldWidget) {

            type = RendererHelper.getComponentAttributeValue(uiObject, TextFieldGVO.REGEXPTYPE);

            if (type != null && type.trim().length() > 0) {
                if (value != null && value.length() != 0 && !isValidType(uiObject, value)) {

                    String componentFullId = RendererHelper.getComponentId(uiObject);
                    String componentId = componentFullId;
                    if (componentFullId != null) {
                        componentId = componentFullId.substring(0, componentFullId.indexOf('|'));
                    }

                    throw new TypeValidationException(TextFieldGVO.getRegExpMessage(type) + "(" + componentId
                            + ").");
                }
            }
        }
    }

    private static boolean isValidType(UIObject uiObject, String value) {
        String type = RendererHelper.getComponentAttributeValue(uiObject, TextFieldGVO.REGEXPTYPE);
        String regExp = TextFieldGVO.getRegExp(type);

        if (regExp == null || value == null || value.replaceFirst(regExp, "").length() > 0) {
            return false;
        }
        return true;
    }

    private static boolean isDataGridField(UIObject uiObject) {
        String id = DOM.getElementAttribute(uiObject.getElement(), "id");
        return (id != null && id.startsWith(QAMLConstants.TOKEN_INDEXING));
    }

    public static DataContainerGVO fetchDatagridRowValues(String inputVariableReference,
            String parent, String context) {
        String[] inputRef = inputVariableReference.split("[.]");
        String key = RendererHelper.generateId(inputRef[0], parent, context);
        List<UIObject> uiObjects = ComponentRepository.getInstance().getComponent(key);
        DataContainerGVO dtc = null;
        if (uiObjects != null) {
            dtc = fetchDatagridRowValues(inputRef, uiObjects);
        }
        return dtc;
    }

    private static DataContainerGVO fetchDatagridRowValues(String[] inputRef, List<UIObject> uiObjects) {
        DataContainerGVO dtc = null;
        if (inputRef == null || uiObjects == null) {
            return null;
        }
        for (UIObject uiObject : uiObjects) {
            if (uiObject instanceof QPagingScrollTable) {
                QPagingScrollTable qps = (QPagingScrollTable) uiObject;
                String[] rowStatusAndColumnGroup = null;
                Object obj = null;
                if (inputRef[1].contains(QAMLConstants.TOKEN_INDEXING)) { // Reference
                                                                          // is
                                                                          // by
                                                                          // row
                                                                          // status
                                                                          // and
                                                                          // group
                                                                          // name
                                                                          // on
                                                                          // columns.
                    rowStatusAndColumnGroup = inputRef[1].split(QAMLConstants.TOKEN_INDEXING);
                }
                if (rowStatusAndColumnGroup == null) {
                    obj = qps.getData(inputRef[1], null); // Fetch data based on
                                                          // row status alone.
                } else {
                    obj = qps.getData(rowStatusAndColumnGroup[0], rowStatusAndColumnGroup[1]); // Fetch data
                                                                                               // based on
                                                                                               // row status
                                                                                               // and
                                                                                               // group name
                                                                                               // on
                                                                                               // columns.
                }
                dtc = DataContainerGVO.create(obj);
            }
        }
        return dtc;
    }

    public static DataContainerGVO fetchDatagridCellValue(String inputVariableReference,
            String parent, String context) {
        String datagrid = inputVariableReference.replaceFirst("\\[.+", "");
        String datagridId = RendererHelper.generateId(datagrid, parent, context);
        String column = null;
        if (inputVariableReference.contains(".")) {
            column = inputVariableReference.replaceFirst(".+\\.", "");
        }
        String selectedIndex = inputVariableReference.replaceFirst(".+\\[", "").replaceFirst("\\].*", "");
        DataContainerGVO value = null;
        List<UIObject> uiObjects = ComponentRepository.getInstance().getComponent(datagridId);
        if (uiObjects != null) {
            for (UIObject uiObject : uiObjects) {
                if (uiObject instanceof HasDataGridMethods) {
                    HasDataGridMethods hasDataGridMethods = (HasDataGridMethods) uiObject;
                    int rowIndex = hasDataGridMethods.getRowIndex(selectedIndex);
                    if (rowIndex == -1) {
                        continue;
                    }
                    value = hasDataGridMethods.getRowValue(rowIndex);
                    if ((value == null) || (column == null)) {
                        continue;
                    }
                    value = value.getDataMap().get(column);
                }
            }
        }
        return value;
    }

    public static boolean hasAttribute(String inputVariableReference) {
        if ((inputVariableReference != null) && (inputVariableReference.indexOf("@") > -1)) {
            return true;
        }
        return false;
    }

    public static String getAttributeValue(String inputVariableReference, String parent,
            String context) {
        String value = "";
        if (hasAttribute(inputVariableReference)) {
            String inputReference = inputVariableReference.substring(0, inputVariableReference.indexOf("@"));
            String attribute = inputVariableReference.substring(inputVariableReference.indexOf("@") + 1);
            String key = RendererHelper.generateId(inputReference, parent, context);
            List<UIObject> uiObjects = ComponentRepository.getInstance().getComponent(key);
            if (uiObjects != null) {
                for (UIObject uiObject : uiObjects) {
                    if ("pagesize".equals(attribute)) {
                        if (uiObject instanceof HasDataGridMethods) {
                            HasDataGridMethods dataGridSortableTable = (HasDataGridMethods) uiObject;
                            value = String.valueOf(dataGridSortableTable.getPageSize());
                        }
                    } else if ("currentpage".equals(attribute)) {
                        if (uiObject instanceof HasDataGridMethods) {
                            HasDataGridMethods dataGridSortableTable = (HasDataGridMethods) uiObject;
                            value = String.valueOf(dataGridSortableTable.getCurrentPage());
                        }
                    }
                }
            }
        }
        return value;
    }

    private static void fillDataContainerMapForGroup(DataMap dataMap, String groupName, UIObject uiObject,
            final UIObject sender) throws RequiredFieldException {

        if (uiObject instanceof Widget) {

            Widget widget = (Widget) uiObject;
            if (widget instanceof HasWidgets && !(widget instanceof ValueSpinner)
                    && !(widget instanceof FormPanel) && !(widget instanceof HasDataGridMethods)) {

                HasWidgets innerHasWidget = (HasWidgets) widget;
                processWidgets(innerHasWidget, dataMap, sender);
            } else {
                processNamedComponent(widget, dataMap, sender, groupName);
            }
        }

    }

    public static DataContainerGVO createDataContainer(String parameterName, UIObject uiObject,
            final UIObject sender) throws RequiredFieldException {
        DataContainerGVO dtc = new DataContainerGVO();
        dtc.setParameterName(parameterName);
        if (uiObject != null) {
            dtc.setKind(DataContainerGVO.KIND_MAP);
            DataMap dataMap = new DataMap();
            dtc.setDataMap(dataMap);

            if (uiObject instanceof Tiles) {
                Tiles tiles = (Tiles) uiObject;
                
                String originalSenderId = RendererHelper.getComponentId(sender);
                
                if (originalSenderId != null) {
                    String index = originalSenderId.substring(0, originalSenderId
                    		.lastIndexOf(QAMLConstants.TOKEN_INDEXING));
                    index = index.replace(QAMLConstants.TOKEN_INDEXING, "");
                    Integer i = Integer.parseInt(index);
                    UIObject tileElement = tiles.getTileElements().get(i);
                    if (tileElement instanceof HasWidgets) {
                        HasWidgets hasWidgets = (HasWidgets) tileElement;
                        processWidgets(hasWidgets, dataMap, sender);
                    }
                    if (parameterName != null && parameterName.contains(".")) {
                        String[] parameterParts = parameterName.split("[.]");
                        if (parameterParts != null && parameterParts.length > 1) {
                            DataContainerGVO dtcInner = new DataContainerGVO();
                            dtcInner.setParameterName(parameterName);
                            dtcInner.setKind(DataContainerGVO.KIND_STRING);
                            dtcInner.setDataString(dataMap.get(parameterParts[1]) != null ? dataMap.get(
                                parameterParts[1]).toString() : null);
                            dtc = dtcInner;
                        }
                    }
                }
            } else if (uiObject instanceof HasWidgets) {
                HasWidgets hasWidgets = (HasWidgets) uiObject;
                processWidgets(hasWidgets, dataMap, sender);

            }
        }
        return dtc;

    }

    private static void processWidgets(HasWidgets hasWidgets, DataMap dataMap, final UIObject sender) 
    		throws RequiredFieldException {
        for (Widget widget : hasWidgets) {

            processNamedComponent(widget, dataMap, sender, null);
            if (widget instanceof HasWidgets && !(widget instanceof ValueSpinner)
                    && !(widget instanceof FormPanel) && !(widget instanceof HasDataGridMethods)) {
                // not clear what is this code was meant for.
                /*
                 * DataContainerGVO dtc = new DataContainerGVO(); dtc.setKind(DataContainerGVO.KIND_MAP);
                 * DataMap innerMap = new DataMap(); dtc.setDataMap(innerMap);
                 * 
                 * if (RendererHelper.isNamedComponent(widget)) { String name =
                 * RendererHelper.getNamedComponentName(widget); dtc.setParameterName(name); dataMap.put(name,
                 * dtc); }
                 */
                HasWidgets innerHasWidget = (HasWidgets) widget;
                processWidgets(innerHasWidget, dataMap, sender);
            }
        }
    }

    private static void processNamedComponent(Widget widget, DataMap dataMap, final UIObject sender
            , String groupName) throws RequiredFieldException {
        UIObject uiObject = widget;
        if (widget instanceof TitledComponent) {
            uiObject = ((TitledComponent) widget).getDataComponent();
        }

        if (RendererHelper.isNamedComponent(uiObject)) {
            String name = RendererHelper.getNamedComponentName(uiObject);
            String value = null;
            boolean valueOnly = false;
            if (uiObject instanceof ListBox) {
                valueOnly = true;
            }

            DataContainerGVO data = new DataContainerGVO();

            Object valueObject = getValue(uiObject, sender, valueOnly, groupName);
            
            if (valueObject instanceof String) {
                value = String.valueOf(valueObject);
                if (uiObject instanceof QDatePicker) {
                    data.setKind(DataContainerGVO.KIND_STRING);
                    data.setStringDataType(DataContainerGVO.TYPE_DATE);
                    data.setDateData(((QDatePicker) uiObject).getValue());
                } else {
                    data.setKind(DataContainerGVO.KIND_STRING);
                    data.setDataString(value);
                    /**
                     * When database column type is NUMBER(5,2) type which is accepting decimal values, If we
                     * pass string type with decimal value to the jdbc template it is giving exception. So the
                     * type of the textfield should be considered for setting datacontainer gvo type. This is
                     * now applicable only for textfield, same issue can come in any inputfield. But we dont
                     * have an option to set type to other components now.
                     */
                    String type = DOM.getElementAttribute(uiObject.getElement(), TextFieldGVO.REGEXPTYPE);
                    if (TextFieldGVO.TYPE_DOUBLE.equals(type)) {
                        data.setStringDataType(DataContainerGVO.TYPE_DOUBLE);
                    } else if (TextFieldGVO.TYPE_INTEGER.equals(type)) {
                        data.setStringDataType(DataContainerGVO.TYPE_INT);
                    } else {
                        data.setStringDataType(DataContainerGVO.TYPE_STRING);
                    }
                }
            } else if (valueObject instanceof DataContainerGVO) {
                data = (DataContainerGVO) valueObject;
            }

            if (dataMap.containsKey(name)) {
                // If value already exists, override the existing value when
                // null or new value is not null,
                // this case can occur when multiple components are added with
                // the same name, like TextArea
                Object oldData = dataMap.get(name);
                if ((oldData == null) || (data != null)) {
                    dataMap.put(name, data);
                }
            } else {
                dataMap.put(name, data);
            }
        }
    }

    public static DataContainerGVO getGroupedComponentValue(final UIObject sender, final String reference
            , String key) {
        List<UIObject> uiObjects = ComponentRepository.getInstance().getGroupedComponent(key);
        DataContainerGVO dataContainerObject = null;
        if (uiObjects != null) {
            DataContainerGVO dataContainer = new DataContainerGVO();
            dataContainer.setParameterName(reference);
            dataContainer.setKind(DataContainerGVO.KIND_MAP);
            DataMap dataMap = new DataMap();
            dataContainer.setDataMap(dataMap);
            for (UIObject uiObject : uiObjects) {
                // Collect all the data from a list of named
                // components
                fillDataContainerMapForGroup(dataMap, reference, uiObject, sender);
            }
            dataContainerObject = dataContainer;
        }
        return dataContainerObject;
    }
    
    public static void handleMask(ShowPanelGVO showPanelGVO, String panelDefKey, String windowKey, String windowId) {
		if(showPanelGVO.isModal()) {
			List<String> panelDefsOpened = ClientApplicationContext.getInstance().getPanelDefinitionsOpened(windowId);
			if(panelDefsOpened != null && panelDefsOpened.size() > 0) {
				//add mask on the panel from which showpanel is called .
				String lastPanelDefOpened = panelDefsOpened.get(panelDefsOpened.size() - 1);
				SetMaskHelper.setMask(lastPanelDefOpened, RendererHelper.QAFE_GLASS_PANEL_STYLE, true);
			} else {
				// if the show-panel is called for first time then do mask on the window.
				SetMaskHelper.setMask(windowKey, RendererHelper.QAFE_GLASS_PANEL_STYLE, true);
			}
			ClientApplicationContext.getInstance().addPanelDefinitionsOpened(windowId, panelDefKey);
		}
	}
    
    public static void handleStyle(ShowPanelGVO showPanelGVO, ShowPanelComponent showPanel, UIObject widget) {
		String styleClass = showPanelGVO.getSrc().getStyleClass();
		if (styleClass != null) {
			// The popup panel inherits the styleClass of the panel-definition
			// to avoid a white area on the right and bottom
			showPanel.addStyleName(styleClass);
			widget.removeStyleName(styleClass);
		}
	}
	
    public static void handleSize(ShowPanelGVO showPanelGVO, ShowPanelComponent showPanel, UIObject container) {
		// Obtained these values by measuring
		int intVScrollbarWidth = 16;
		int intHScrollbarHeight = 16;
		int offsetWidth = 34;
		int offsetHeight = 38;
					
		
		String height = showPanelGVO.getSrc().getHeight();
		if (height != null) {
			// The height of the panel-definition inside the container,
			// so the height of the popup panel should be bigger to avoid unnecessary scrollbar  
			int intHeight = Integer.valueOf(height);
			height = String.valueOf(intHeight + intHScrollbarHeight + offsetHeight);
			showPanel.setHeight(height);	
		}
		
		String width = showPanelGVO.getSrc().getWidth();
		if (width != null) {
			// The width of the panel-definition inside the container,
			// so the width of the popup panel should be bigger to avoid unnecessary scrollbar
			int intWidth = Integer.valueOf(width);
			width = String.valueOf(intWidth + intVScrollbarWidth + offsetWidth);
			showPanel.setWidth(width);
		}
	}
    
	public static String generatePanelDefinitionKey(String panelDefId, String appId, String windowId) {
    	return RendererHelper.generateId(panelDefId, windowId, appId);
    }
	
	public static void closeOpenedPanelDefinition(String panelDefId, String appId, String windowId, String eventSessionId) {
		// We have to make sure that all other showPanels using the same panel-definition is cleared
    	String panelDefinitionKey = generatePanelDefinitionKey(panelDefId, appId, windowId);
		List<UIObject> uiObjects = ComponentRepository.getInstance().getComponent(panelDefinitionKey);
		if(uiObjects != null){
			UIObject uiObject = uiObjects.iterator().next();
			if (uiObject instanceof ShowPanelComponent) {
				ShowPanelComponent showPanelComponent = (ShowPanelComponent)uiObject;
				
				// This will call showPanelComponent.onDetach()
				showPanelComponent.hide();
			}
		}    	
    }	
}
