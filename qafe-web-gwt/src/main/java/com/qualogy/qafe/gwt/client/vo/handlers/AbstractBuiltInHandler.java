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
package com.qualogy.qafe.gwt.client.vo.handlers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.UIObject;
import com.qualogy.qafe.gwt.client.component.DataMap;
import com.qualogy.qafe.gwt.client.component.HasDataGridMethods;
import com.qualogy.qafe.gwt.client.component.QPagingScrollTable;
import com.qualogy.qafe.gwt.client.context.ClientApplicationContext;
import com.qualogy.qafe.gwt.client.storage.DataStorage;
import com.qualogy.qafe.gwt.client.ui.renderer.RendererHelper;
import com.qualogy.qafe.gwt.client.util.ComponentRepository;
import com.qualogy.qafe.gwt.client.util.QAMLConstants;
import com.qualogy.qafe.gwt.client.vo.functions.BuiltInFunctionGVO;
import com.qualogy.qafe.gwt.client.vo.functions.DataContainerGVO;
import com.qualogy.qafe.gwt.client.vo.ui.event.ParameterGVO;

public abstract class AbstractBuiltInHandler implements BuiltInHandler {

    protected Object getValue(UIObject sender, ParameterGVO parameterGVO, String appId, String windowId,
            String eventSessionId) {
        Object value = null;
        if (parameterGVO == null) {
            return value;
        }

        Map<String, Object> placeHolderValues = new HashMap<String, Object>();

        if (parameterGVO.getPlaceHolders() != null && !parameterGVO.getPlaceHolders().isEmpty()) {
            for (ParameterGVO placeholder : parameterGVO.getPlaceHolders()) {
                Object placeHolderValue = getValue(sender, placeholder, appId, windowId, eventSessionId);
                placeHolderValues.put(placeholder.getName(), placeHolderValue);
            }
        }

        value = getValue(sender, parameterGVO, appId, windowId, eventSessionId, placeHolderValues);

        return value;
    }

    protected final Object getValue(final UIObject sender, final ParameterGVO parameterGVO,
            final String appId, final String windowId, final String eventSessionId,
            final Map<String, Object> placeHolderValues) {
        Object value = null;
        if (parameterGVO == null) {
            return value;
        }
        if (parameterGVO.getValue() != null) {
            value = parameterGVO.getValue();
            if (value instanceof String) {
                value = resolveVariables((String) value, placeHolderValues, eventSessionId);
            }
        } else if (parameterGVO.getReference() != null) {
            final String source = parameterGVO.getSource();
            final String reference = parameterGVO.getReference();

            if (BuiltInFunctionGVO.SOURCE_COMPONENT_ID.equals(source)) {
                value = getComponentValue(sender, reference, appId, windowId);
            } else {
                if (BuiltInFunctionGVO.SOURCE_DATASTORE_ID.equals(source)
                        && placeHolderValues.containsKey(reference)) {
                    value = placeHolderValues.get(reference);
                } else {
                    final String dataId = generateDataId(source, appId, windowId, eventSessionId);

                    value = getData(dataId, reference);
                }
            }
        }
        return value;
    }

    // The solution is based on retrieving value from textfield
    // TODO: Refactor to handle multiple components
    private Object getComponentValue(UIObject sender, String reference, String appId, String windowId) {
        // String uuid = DOM.getElementProperty(sender.getElement(), "uuid");
        String value = null;

        // DataContainerGVO dataContainerObject = null;

        // String inputVariableReference = inputVariables.getReference();
        // if (hasAttribute(reference)) {
        // value = getAttributeValue(reference, uuid, windowId, appId);
        // } else if(reference.contains(".$$")){
        // dataContainerObject = fetchDatagridRowValues(reference, uuid, windowId, appId);
        // } else if(reference.contains("[")){
        // dataContainerObject = fetchDatagridCellValue(reference, uuid, windowId, appId);
        // } else {
        String key = RendererHelper.generateId(reference, windowId, appId); // inputVariables[i][1]
        ClientApplicationContext.getInstance().log(key);
        List<UIObject> uiObjects = ComponentRepository.getInstance().getComponent(key);

        // since the parameter can be a complex object, we need to create a substitute for it.
        // This can only be used in the name variant though (namedcomponents)
        if (uiObjects != null) {
            for (UIObject uiObject : uiObjects) {

                // Object o = getValue(uiObject, sender, null, false, null);
                value = ((HasText) uiObject).getText();

                // if (o instanceof String) {
                // value = o.toString();
                // } else if (o instanceof DataContainerGVO) {
                // dataContainerObject = (DataContainerGVO) o;
                // }

                // not needed accoording to Reyco
                // if (uiObject instanceof HasDataModel) {
                // // Get also the data model behind
                // HasDataModel hasDataModel = (HasDataModel)uiObject;
                // if (hasDataModel.getDataModel() != null) {
                // Object dataModel = hasDataModel.getDataModel();
                // String newInputVariableReference = reference + QAMLConstants.DATAMODEL_POSTFIX;
                // if (eventDataObject.getInternalVariables() == null) {
                // eventDataObject.setInternalVariables(new HashMap<String,Object>());
                // }
                // eventDataObject.getInternalVariables().put(newInputVariableReference, dataModel);
                // }
                // }

            }

        }
        // else {
        // // so the object could not be found in the ComponentRepository, maybe we try by name to find it.
        // ClientApplicationContext.getInstance().log("Reference" + reference);
        // String[] keysSet = reference.split("[.]");
        // if (keysSet != null) {
        // String searchKey = null;
        // if (keysSet.length == 1) {// so only the key
        // searchKey = key;
        // } else {
        // searchKey = RendererHelper.generateId(keysSet[0], windowId, appId);
        // }
        // if (searchKey != null) {
        // uiObjects = ComponentRepository.getInstance().getNamedComponent(searchKey);
        // if (uiObjects != null) {
        // for (UIObject uiObject : uiObjects) {
        // ClientApplicationContext.getInstance().log("Title" + uiObject.getTitle());

        // Collect all the data from a list of named components
        // DataContainerGVO dataContainer = createDataContainer(reference, uiObject, sender, eventDataObject);
        // if (dataContainerObject == null) {
        // dataContainerObject = dataContainer;
        // } else if (dataContainer != null) {
        // if (dataContainer.getKind() == dataContainerObject.getKind()) {
        // switch (dataContainer.getKind()) {
        // case DataContainerGVO.KIND_MAP : {
        // dataContainerObject.getDataMap().putAll(dataContainer.getDataMap());
        // } break;
        // }
        // }
        // }

        // Get value of a data member
        // if(keysSet.length > 1){
        // if(dataContainerObject.getDataMap().get(keysSet[1]) != null){
        // value = dataContainerObject.getDataMap().get(keysSet[1]).getDataString();
        // }
        // dataContainerObject = null;
        // }
        // }
        // }
        // else { // Apparently we have to search for the Group now.
        // uiObjects = ComponentRepository.getInstance().getGroupedComponent(key);
        // if (uiObjects != null) {
        // DataContainerGVO dataContainer = new DataContainerGVO();
        // dataContainer.setParameterName(reference);
        // dataContainer.setKind(DataContainerGVO.KIND_MAP);
        // DataMap dataMap = new DataMap();
        // dataContainer.setDataMap(dataMap);
        // for (UIObject uiObject : uiObjects) {
        // // Collect all the data from a list of named components
        // // fillDataContainerMapForGroup(dataMap,reference, uiObject, sender, eventDataObject);
        // }
        // dataContainerObject = dataContainer;
        // }
        // }
        // }
        // }
        // }
        // }

        // String x = inputVariables.getComponentValue() != null ? inputVariables.getComponentValue() : value;
        // if ((inputVariableReference != null) && inputVariableReference.startsWith("||")) {
        // // so this is a click from a table
        // inputVariableReference = (inputVariables.getReference().substring(senderId.lastIndexOf("||") + 2));
        // }
        // eventDataObject.getInputVariables().add(new InputVariableGVO(inputVariables.getName(),
        // inputVariableReference, inputVariables.getDefaultValue(), x, dataContainerObject));
        return value;
    }

    // CHECKSTYLE.OFF: CyclomaticComplexity
    // public static Object getValue(UIObject uiObject, final UIObject sender, EventDataGVO eventDataObject,
    // boolean idValueOnly, String groupName){
    // Object returnObject = null;
    // boolean hasSimpleValue = false;
    // if (uiObject instanceof QPagingScrollTable) {
    // QPagingScrollTable qps = (QPagingScrollTable) uiObject;
    // returnObject = qps.getData(null, groupName);
    // DataContainerGVO dtc = convertToDataGVO(returnObject);
    // returnObject = dtc;
    // } else if (uiObject instanceof QRadioButton) {
    // QRadioButton qRadioButton = (QRadioButton) uiObject;
    // returnObject = qRadioButton.getText();
    // hasSimpleValue = true;
    // } else if (uiObject instanceof HasChoice) {
    // HasChoice hasChoice = (HasChoice)uiObject;
    // returnObject = hasChoice.getData();
    // hasSimpleValue = true;
    // } else if (uiObject instanceof HasData) {
    // HasData hasData = (HasData)uiObject;
    // returnObject = hasData.getData();
    // hasSimpleValue = true;
    // if (!(returnObject instanceof String)) {
    // DataContainerGVO dtc = convertToDataGVO(returnObject);
    // if (dtc != null) {
    // returnObject = dtc;
    // hasSimpleValue = false;
    // }
    // }
    // } else if (uiObject instanceof CheckBox) {
    // CheckBox checkBox = (CheckBox) uiObject;
    // returnObject = checkBox.getValue().toString();
    // hasSimpleValue = true;
    // if (checkBox.getValue()) {
    // String attributeValue = DOM.getElementAttribute(checkBox.getElement(),
    // CheckBoxGVO.CHECKED_VALUE_ATTRIBUTE_TAG);
    // if (attributeValue != null && attributeValue.length() > 0) {
    // returnObject = attributeValue;
    // }
    // } else {
    // String attributeValue = DOM.getElementAttribute(checkBox.getElement(),
    // CheckBoxGVO.UNCHECKED_VALUE_ATTRIBUTE_TAG);
    // if (attributeValue != null && attributeValue.length() > 0) {
    // returnObject = attributeValue;
    // }
    // }
    // } else if (uiObject instanceof FormPanel) {
    // FormPanel fp = (FormPanel) uiObject;
    // if (fp instanceof HasWidgets) {
    // HasWidgets hasWidgets = (HasWidgets) fp;
    // Iterator<Widget> itr = hasWidgets.iterator();
    // while (itr.hasNext()) {
    // Widget widget = itr.next();
    // if (widget instanceof Grid) {
    // Grid gridPanel = (Grid) widget;
    // FileUpload fileUpload = (FileUpload) gridPanel.getWidget(0, 0);
    // returnObject = DOM.getElementAttribute(fileUpload.getElement(), "fu-uuid");
    // hasSimpleValue = true;
    // }
    // }
    // }
    // } else if (uiObject instanceof ListBox) {
    // ListBox listBox = (ListBox) uiObject;
    // if (!(listBox.isMultipleSelect()) && listBox.getSelectedIndex() != -1) { // dropdown
    // int index = listBox.getSelectedIndex();
    // if (idValueOnly) {
    // returnObject = listBox.getValue(index);
    // hasSimpleValue = true;
    // } else {
    // DataMap dm = new DataMap();
    // dm.put("id", new DataContainerGVO(listBox.getValue(index)));
    // dm.put("value", new DataContainerGVO(listBox.getItemText(index)));
    //
    // DataContainerGVO dtcMap = new DataContainerGVO();
    // dtcMap.setKind(DataContainerGVO.KIND_MAP);
    // dtcMap.setDataMap(dm);
    // returnObject = dtcMap;
    //
    // // TODO: refactor, this is a workaround for checking simple value
    // dtcMap.setDataString(listBox.getValue(index));
    // hasSimpleValue = true;
    // }
    // } else if (listBox.getSelectedIndex() != -1) {
    // DataContainerGVO dtclist = new DataContainerGVO();
    // dtclist.setKind(DataContainerGVO.KIND_COLLECTION);
    // List<DataContainerGVO> list = new ArrayList<DataContainerGVO>();
    // dtclist.setListofDC(list);
    // int items = listBox.getItemCount();
    // for (int itemIndex = 0; itemIndex < items; itemIndex++) {
    // if (listBox.isItemSelected(itemIndex)) {
    // DataMap dataMap = new DataMap();
    // DataContainerGVO dtcId = new DataContainerGVO();
    // dtcId.setKind(DataContainerGVO.KIND_STRING);
    // dtcId.setDataString(listBox.getValue(itemIndex));
    // dtcId.setStringDataType(DataContainerGVO.TYPE_STRING);
    // dataMap.put("id", dtcId);
    //
    // DataContainerGVO dtcValue = new DataContainerGVO();
    // dtcValue.setKind(DataContainerGVO.KIND_STRING);
    // dtcValue.setDataString(listBox.getItemText(itemIndex));
    // dtcValue.setStringDataType(DataContainerGVO.TYPE_STRING);
    // dataMap.put("value", dtcValue);
    //
    // list.add(new DataContainerGVO(dataMap));
    // }
    // }
    // returnObject = dtclist;
    // }
    // } else if (uiObject instanceof QDatePicker) {
    // DataContainerGVO data = new DataContainerGVO();
    // data.setKind(DataContainerGVO.KIND_STRING);
    // data.setStringDataType(DataContainerGVO.TYPE_DATE);
    // data.setDateData(((QDatePicker) uiObject).getValue());
    // returnObject = data;
    // hasSimpleValue = true;
    // } else if (uiObject instanceof HasText) {
    // returnObject = ((HasText) uiObject).getText();
    // hasSimpleValue = true;
    // } else if (uiObject instanceof MapWidget) {
    // MapWidget mapWidget = (MapWidget) uiObject;
    // AreaWidget[] areas = mapWidget.getItems();
    // if (areas != null) {
    // for (int k = 0; k < areas.length; k++) {
    // if (areas[k] == sender) {
    // returnObject = sender.getTitle();
    // hasSimpleValue = true;
    // // The senderId has to be the one of the Map, not of the area.
    // String senderId = DOM.getElementProperty(mapWidget.getElement(), "id");
    // eventDataObject.setSender(senderId);
    // }
    // }
    // }
    // } else if (uiObject instanceof HasDataGridMethods) {
    // HasDataGridMethods dataGridSortableTable = (HasDataGridMethods) uiObject;
    // // MaxRowSize with the call
    // if (dataGridSortableTable.getMaxRows() != null) {
    // eventDataObject.getInputVariables().add(new
    // InputVariableGVO(DOM.getElementAttribute(uiObject.getElement(), "id") + ".max_rows", null,
    // dataGridSortableTable.getMaxRows().toString()));
    // }
    // eventDataObject.getInputVariables().add(new
    // InputVariableGVO(DOM.getElementAttribute(uiObject.getElement(), "id") + ".pagesize", null, "" +
    // dataGridSortableTable.getPageSize()));
    // } else if (uiObject instanceof Image) {
    // Image img = (Image) uiObject;
    // if (img.getUrl() != null) {
    // returnObject = img.getUrl();
    // hasSimpleValue = true;
    // }
    // } else if (uiObject instanceof ValueSpinner) {
    // ValueSpinner spinner = (ValueSpinner) uiObject;
    // if (spinner.getTextBox() != null) {
    // returnObject = spinner.getTextBox().getValue();
    // hasSimpleValue = true;
    // }
    // } else if (uiObject instanceof Tiles) {
    // Tiles tiles = (Tiles) uiObject;
    // DataContainerGVO dtc = new DataContainerGVO();
    // dtc.setKind(DataContainerGVO.KIND_MAP);
    // DataMap dataMap = new DataMap();
    // dtc.setDataMap(dataMap);
    // if (eventDataObject.getOriginalSenderId() != null) {
    // String index = eventDataObject.getOriginalSenderId().substring(0,
    // eventDataObject.getOriginalSenderId().lastIndexOf(QAMLConstants.TOKEN_INDEXING));
    // index = index.replace(QAMLConstants.TOKEN_INDEXING, "");
    // Integer i = Integer.parseInt(index);
    // UIObject tileElement = tiles.getTileElements().get(i);
    // if (tileElement instanceof HasWidgets) {
    // HasWidgets hasWidgets = (HasWidgets) tileElement;
    // processWidgets(hasWidgets, dataMap, sender, eventDataObject);
    // }
    //
    // }
    // returnObject = dtc;
    // } else if(uiObject instanceof SliderBar){
    // SliderBar slider = (SliderBar)uiObject;
    // if(slider.getCurrentValue() > 0){
    // returnObject = String.valueOf(slider.getCurrentValue());
    // }
    // } else if (isDataGridField(uiObject)) {
    // returnObject = getDataGridValue(uiObject, sender, eventDataObject);
    // }
    //
    // if (hasSimpleValue) {
    // String value = null;
    // if (returnObject instanceof String) {
    // value = (String)returnObject;
    // } else if (returnObject instanceof DataContainerGVO) {
    // DataContainerGVO gvo = (DataContainerGVO)returnObject;
    // if (gvo.getDataString() != null) {
    // value = gvo.getDataString();
    // } else if (gvo.getDateData() != null) {
    // value = gvo.getDateData().toString();
    // }
    // }
    // // Required field check
    // handleRequired(uiObject, value);
    // // Validation based on type- for textfield with type
    // handleTypeValidation(uiObject, value);
    // }
    //
    // return returnObject;
    // }

    private static boolean hasAttribute(String inputVariableReference) {
        if ((inputVariableReference != null) && (inputVariableReference.indexOf("@") > -1)) {
            return true;
        }
        return false;
    }

    private static String getAttributeValue(String inputVariableReference, String uuid, String parent,
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

    private static DataContainerGVO fetchDatagridRowValues(String inputVariableReference, String uuid,
            String parent, String context) {
        String[] inputRef = inputVariableReference.split("[.]");
        String key = RendererHelper.generateId(inputRef[0], parent, context);
        List<UIObject> uiObjects = ComponentRepository.getInstance().getComponent(key);
        DataContainerGVO dtc = null;
        if(uiObjects == null) {
            return dtc;
        }
        for (UIObject uiObject : uiObjects) {
            if (uiObject instanceof QPagingScrollTable) {
                QPagingScrollTable qps = (QPagingScrollTable) uiObject;
                String[] rowStatusAndColumnGroup = null;
                Object obj = null;
                if (inputRef[1].contains(QAMLConstants.TOKEN_INDEXING)) { // Reference is by row status and
                                                                          // group name on columns.
                    rowStatusAndColumnGroup = inputRef[1].split(QAMLConstants.TOKEN_INDEXING);
                }
                if (rowStatusAndColumnGroup == null) {
                    obj = qps.getData(inputRef[1], null); // Fetch data based on row status alone.
                } else {
                    obj = qps.getData(rowStatusAndColumnGroup[0], rowStatusAndColumnGroup[1]); // Fetch data
                                                                                               // based on row
                                                                                               // status and
                                                                                               // group name
                                                                                               // on columns.
                }
                dtc = convertToDataGVO(obj);
            }
        }
        return dtc;
    }

    private static DataContainerGVO convertToDataGVO(Object returnObject) {
        DataContainerGVO dtc = null;
        if (returnObject instanceof DataMap) {
            dtc = new DataContainerGVO();
            dtc.setDataMap((DataMap) returnObject);
            dtc.setKind(DataContainerGVO.KIND_MAP);

        } else if (returnObject instanceof List) {
            dtc = new DataContainerGVO();
            dtc.setListofDC((List<DataContainerGVO>) returnObject);
            dtc.setKind(DataContainerGVO.KIND_COLLECTION);
        }
        return dtc;
    }

    private static DataContainerGVO fetchDatagridCellValue(String inputVariableReference, String uuid,
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

    protected String resolveVariables(String name, Map<String, Object> placeHolderValues,
            String eventSessionId) {

        while (name != null && name.contains("${")) {
            String varName = name.substring(name.indexOf("{") + 1, name.indexOf("}"));
            Object value = null;
            if (placeHolderValues != null && placeHolderValues.containsKey(varName)) {
                value = placeHolderValues.get(varName);
            } else {
                value = getData(eventSessionId, varName);
            }

            if (value != null) {
                name = name.replace("${" + varName + "}", value.toString());
            }

        }
        return name;
    }

    protected void storeData(String dataId, String name, Object data) {
        DataStorage dataStorage = ClientApplicationContext.getInstance().getDataStorage();
        dataStorage.storeData(dataId, name, data);

        log("dataId1=" + dataId + " - " + name + "=" + data);
    }

    protected Object getData(String dataId, String name) {
        DataStorage dataStorage = ClientApplicationContext.getInstance().getDataStorage();
        return dataStorage.getData(dataId, name);
    }

    protected String generateDataId(String sourceOrTarget, String appId, String windowId,
            String eventSessionId) {
        if (BuiltInFunctionGVO.SOURCE_DATASTORE_ID.equals(sourceOrTarget)) {
            return eventSessionId;
        }
        if (BuiltInFunctionGVO.SOURCE_APP_GLOBAL_STORE_ID.equals(sourceOrTarget)) {
            return appId;
        }
        if (BuiltInFunctionGVO.SOURCE_APP_LOCAL_STORE_ID.equals(sourceOrTarget)) {
            return appId + "|" + windowId;
        }
        return null;
    }

    protected String getSenderId(final UIObject sender) {
        return RendererHelper.getComponentId(sender);
    }
    
    protected String getComponentName(final UIObject uiObject) {
        return RendererHelper.getNamedComponentName(uiObject);
    }

    protected boolean isNamedComponent(final UIObject uiObject) {
        return RendererHelper.isNamedComponent(uiObject);
    }
    
    protected void log(String message) {
        ClientApplicationContext.getInstance().log(message);
    }
}
