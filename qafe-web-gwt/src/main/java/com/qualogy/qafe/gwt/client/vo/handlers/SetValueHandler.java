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
package com.qualogy.qafe.gwt.client.vo.handlers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.RichTextArea;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;
import com.qualogy.qafe.gwt.client.component.DataMap;
import com.qualogy.qafe.gwt.client.component.HasData;
import com.qualogy.qafe.gwt.client.component.HasDataGridMethods;
import com.qualogy.qafe.gwt.client.component.QChart2D;
import com.qualogy.qafe.gwt.client.component.QDatePicker;
import com.qualogy.qafe.gwt.client.component.QMultiWordSuggestion;
import com.qualogy.qafe.gwt.client.component.QRadioButton;
import com.qualogy.qafe.gwt.client.component.QSliderBar;
import com.qualogy.qafe.gwt.client.component.QSuggestBox;
import com.qualogy.qafe.gwt.client.component.Tiles;
import com.qualogy.qafe.gwt.client.component.TitledComponent;
import com.qualogy.qafe.gwt.client.context.ClientApplicationContext;
import com.qualogy.qafe.gwt.client.ui.renderer.DropDownRenderer;
import com.qualogy.qafe.gwt.client.ui.renderer.RendererHelper;
import com.qualogy.qafe.gwt.client.util.ComponentRepository;
import com.qualogy.qafe.gwt.client.vo.functions.BuiltInFunctionGVO;
import com.qualogy.qafe.gwt.client.vo.functions.DataContainerGVO;
import com.qualogy.qafe.gwt.client.vo.functions.SetValueGVO;
import com.qualogy.qafe.gwt.client.vo.ui.CheckBoxGVO;
import com.qualogy.qafe.gwt.client.vo.ui.event.ParameterGVO;

public class SetValueHandler extends AbstractBuiltInHandler {

	protected BuiltInState executeBuiltIn(UIObject sender, String listenerType, Map<String, String> mouseInfo, BuiltInFunctionGVO builtInGVO, String appId, String windowId, String eventSessionId, Queue derivedBuiltIns) {
        SetValueGVO setValueGVO = (SetValueGVO) builtInGVO;
        ParameterGVO parameterGVO = setValueGVO.getParameter();
        Object value = getValue(sender, parameterGVO, appId, windowId, eventSessionId);
        setValue(sender, listenerType, setValueGVO, value, appId, windowId, eventSessionId);
        return BuiltInState.EXECUTED;
    }
    
    // CHECKSTYLE.OFF: CyclomaticComplexity
    private void setValue(UIObject sender, String listenerType, SetValueGVO setValueGVO, Object value, String appId, String windowId, String eventSessionId) {
        String senderId = getSenderId(sender);
        DataContainerGVO dataContainerGVO = DataContainerGVO.create(value);
        String action = setValueGVO.getAction();
        
        String componentId = setValueGVO.getComponentId();
        String key = RendererHelper.generateId(componentId, windowId, appId);
        List<UIObject> uiObjects = ComponentRepository.getInstance().getComponent(key);
        if (uiObjects != null) {
            for (UIObject uiObject : uiObjects) {
                if (uiObject == null) {
                    continue;
                }
                if (dataContainerGVO == null) {
                    // TODO: Clear component
                    continue;
                }
                switch (dataContainerGVO.getKind()) {
                    case DataContainerGVO.KIND_VALUE: {
                        process(uiObject, (String)value, setValueGVO, dataContainerGVO);
                    } break;
                    case DataContainerGVO.KIND_STRING: {
                        process(uiObject, dataContainerGVO.getDataString(), setValueGVO, dataContainerGVO);
                    } break;
                    case DataContainerGVO.KIND_MAP: {
                        DataMap dataMap = dataContainerGVO.getDataMap();
                        if (uiObject instanceof HasDataGridMethods) {
                            HasDataGridMethods dataGrid = (HasDataGridMethods) uiObject;
                            List<DataContainerGVO> listOfDataMap = new ArrayList<DataContainerGVO>();
                            listOfDataMap.add(new DataContainerGVO(dataContainerGVO.getDataMap()));
                            boolean append = SetValueGVO.ACTION_ADD.equals(action);
                            dataGrid.insertData(listOfDataMap, append, senderId, listenerType);
                        } else if (uiObject instanceof HasData) {
                            HasData hasData = (HasData) uiObject;
                            hasData.setData(dataMap, action, setValueGVO.getMapping());
                        } else if (uiObject instanceof Tiles) {
                            Tiles tiles = (Tiles) uiObject;
                            tiles.fillDataFromMap(new DataContainerGVO(dataMap), setValueGVO);
                        }
                    } break;
                    case DataContainerGVO.KIND_COLLECTION: {
                        List<DataContainerGVO> dataContainerGVOs = dataContainerGVO.getListofDC();
                        Map<String,String> mapping = setValueGVO.getMapping();
                        if (uiObject instanceof ListBox) {
                            processListBox(uiObject, setValueGVO, dataContainerGVO);
                        } else if (uiObject instanceof HasDataGridMethods) {
                            HasDataGridMethods dataGrid = (HasDataGridMethods) uiObject;
                            boolean append = SetValueGVO.ACTION_ADD.equals(action);
                            dataGrid.insertData(dataContainerGVOs, append, senderId, listenerType);
                            dataGrid.redraw();
                        } else if (uiObject instanceof HasData) {
                            HasData hasData = (HasData) uiObject;
                            hasData.setData(dataContainerGVOs, action, mapping);
                        } else if (uiObject instanceof QSuggestBox) {
                            QSuggestBox suggestionBox = (QSuggestBox) uiObject;
                            suggestionBox.clearSuggestions();
                            String displayField = QMultiWordSuggestion.DISPLAYFIELD_DEFAULT;
                            if (mapping != null) {
                                displayField = mapping.get("value");
                            }
                            for (DataContainerGVO dcGVO : dataContainerGVOs) {
                                QMultiWordSuggestion qms = new QMultiWordSuggestion(dcGVO, displayField);
                                suggestionBox.getOracle().add(qms);
                            }
                            suggestionBox.showSuggestionList();
                        } else if (uiObject instanceof Tiles) {
                            Tiles tiles = (Tiles) uiObject;
                            if (SetValueGVO.ACTION_SET.equals(action)) {
                                tiles.getTileElements().clear();
                                tiles.clear();
                            }
                            tiles.fillDataFromMapList(dataContainerGVOs, setValueGVO);
                        } else if(uiObject instanceof QChart2D) {
                            QChart2D chart = (QChart2D)uiObject;
                            chart.setChartData(uiObject, dataContainerGVOs);
                        }
                    } break;
                }
            }
        } else {
            String reference = setValueGVO.getNamedComponentId();
            boolean referenceByName = true;
            if ((reference == null) || (reference.length() == 0)) {
                reference = setValueGVO.getGroup();
                referenceByName = false;
            }
            key = RendererHelper.generateId(reference, windowId, appId);
            if (referenceByName) {
                uiObjects = ComponentRepository.getInstance().getNamedComponent(key);
            } else {
                uiObjects = ComponentRepository.getInstance().getGroupedComponent(key);
            }
            if (uiObjects != null) {
                for (UIObject uiObject : uiObjects) {
                    if (uiObject == null) {
                        continue;
                    }
                    if (dataContainerGVO == null) {
                        // TODO: Clear component
                        continue;
                    }
                    List<DataContainerGVO> dataContainerGVOs = dataContainerGVO.getListofDC();
                    if (uiObject instanceof HasDataGridMethods) {
                        HasDataGridMethods dataGrid = (HasDataGridMethods) uiObject;
                        boolean append = SetValueGVO.ACTION_ADD.equals(action);
                        dataGrid.insertData(dataContainerGVOs, append, senderId, listenerType);
                        dataGrid.redraw();
                    } else if (uiObject instanceof QSliderBar) {
                        processNamedComponent((Widget) uiObject, value, setValueGVO, dataContainerGVO);
                    } else if (uiObject instanceof HasWidgets) {
                        HasWidgets hasWidgets = (HasWidgets) uiObject;
                        processWidgets(hasWidgets, value, setValueGVO, dataContainerGVO);
                    } else if (uiObject instanceof Widget) {
                        processNamedComponent((Widget) uiObject, value, setValueGVO, dataContainerGVO);
                    }
                }
            }
        }
    }
    // CHECKSTYLE.ON: CyclomaticComplexity
    
    private void process(UIObject uiObject, String value, SetValueGVO setValueGVO, DataContainerGVO dataContainerGVO) {
        if (uiObject == null) {
            return;
        }
        
        // TODO: handle Attributes
//        if (setValueGVO.getBuiltInComponentGVO() != null && setValueGVO.getBuiltInComponentGVO().getAttributes() != null && setValueGVO.getBuiltInComponentGVO().getAttributes().size() > 0) {
//            processAttributes(uiObject, value, setValueGVO.getBuiltInComponentGVO().getAttributes(), setValueGVO,dataContainerGVO);
//        } else {
            processValue(uiObject, value, setValueGVO,dataContainerGVO);
//        }
    }

    // CHECKSTYLE.OFF: CyclomaticComplexity
    private void processValue(UIObject uiObject, Object value, SetValueGVO setValueGVO, DataContainerGVO dataContainerGVO) {
        if (uiObject != null) {
            if (uiObject instanceof HasText) {
                if (uiObject instanceof QRadioButton) {
                    QRadioButton qRadioButton = (QRadioButton) uiObject;
                    qRadioButton.reset();
                    if (value != null) {
                        qRadioButton.setValue(value.toString());
                    }

                } else if (uiObject instanceof CheckBox) {
                    CheckBox checkBox = (CheckBox) uiObject;
                    String checkedValue = DOM.getElementAttribute(checkBox.getElement(), CheckBoxGVO.CHECKED_VALUE_ATTRIBUTE_TAG);
                    String unCheckedValue = DOM.getElementAttribute(checkBox.getElement(), CheckBoxGVO.UNCHECKED_VALUE_ATTRIBUTE_TAG);
                    String checkedValueDomain = DOM.getElementAttribute(checkBox.getElement(), CheckBoxGVO.CHECKED_VALUE_DOMAIN_ATTRIBUTE_TAG);
                    String unCheckedValueDomain = DOM.getElementAttribute(checkBox.getElement(), CheckBoxGVO.UNCHECKED_VALUE_DOMAIN_ATTRIBUTE_TAG);

                    if (value != null) {
                        if (checkedValue != null && checkedValue.equals(value)) {
                            checkBox.setChecked(true);
                        } else if (unCheckedValue != null && unCheckedValue.equals(value)) {
                            checkBox.setChecked(false);
                        } else if (isInDomain(value.toString(), checkedValueDomain)) {
                            checkBox.setChecked(true);
                        } else if (isInDomain(value.toString(), unCheckedValueDomain)) {
                            checkBox.setChecked(false);
                        }
                    } else {
                        checkBox.setChecked(false);
                    }
                } else if (uiObject instanceof HTML) {
                    HTML html = (HTML) uiObject;
                    if (value != null) {
                        html.setHTML(value.toString());
                    } else {
                        html.setHTML("");
                    }
                } else if(uiObject instanceof PushButton) {
                    ((PushButton)uiObject).getUpFace().setText(value.toString());
                    ((PushButton)uiObject).getDownFace().setText(value.toString());
                } else if (uiObject instanceof RichTextArea) {
                    RichTextArea richTextArea = (RichTextArea)uiObject;
                    richTextArea.setHTML(value.toString());                    
                } else {
                    HasText hasText = (HasText) uiObject;
                    if (dataContainerGVO!=null){
                        if (dataContainerGVO.getKind()== DataContainerGVO.KIND_STRING){
                            if (dataContainerGVO.getStringDataType()==DataContainerGVO.TYPE_DATE){
                                if (uiObject instanceof QDatePicker) {
                                    ((QDatePicker) uiObject).setValue(dataContainerGVO.getDateData());
                                }
                            }else {
                                hasText.setText(value.toString());
                                uiObject.setStyleName(uiObject.getStyleName().replaceAll("qafe_invalid_field", "qafe_valid_field"));
                            }
                        }else {
                            hasText.setText(value.toString());
                        }
                    } else if (value != null) {
                            hasText.setText(value.toString());
                    } else {
                        hasText.setText("");
                    }
                }
            } else if (uiObject instanceof Frame) {
                Frame frame = (Frame) uiObject;
                if (value != null) {
                    frame.setUrl(value.toString());
                } else {
                    frame.setUrl("");
                }
            }

            if (uiObject instanceof ListBox) {
                ListBox listBox = (ListBox) uiObject;
                // If it is needed to populate data and select a data from dropdown it should be seperate calls.
                if(dataContainerGVO != null && dataContainerGVO.getListofDC() != null){
                    processListBox(uiObject, setValueGVO, dataContainerGVO);
                } else {
                    processValue4ListBox(listBox, value, setValueGVO.getAction());
                }

            }

            if (uiObject instanceof Image) {
                Image image = (Image) uiObject;
                if (value != null) {
                    image.setUrl(value.toString());
                }

            }

            if(uiObject instanceof QChart2D){
                QChart2D chart = (QChart2D)uiObject;
                DataContainerGVO val = (DataContainerGVO)value;
                chart.setChartData(uiObject, val.getListofDC());
            }

            if (uiObject instanceof QDatePicker) {
                QDatePicker qDatePicker = (QDatePicker)uiObject;
                if(dataContainerGVO != null) {
                    qDatePicker.setValue(dataContainerGVO.getDateData(), true);
                }
            }

            if(uiObject instanceof QSliderBar) {
                QSliderBar slider = (QSliderBar)uiObject;
                slider.setValue(value);
            }

            if(uiObject instanceof HasDataGridMethods) {
                HasDataGridMethods dataGrid = (HasDataGridMethods) uiObject;
                List<DataContainerGVO> listOfDataMap = new ArrayList<DataContainerGVO>();
                if (dataContainerGVO.getKind() == DataContainerGVO.KIND_MAP) {
                    listOfDataMap.add(new DataContainerGVO(dataContainerGVO.getDataMap()));
                    dataGrid.insertData(listOfDataMap, false, setValueGVO.getSenderId(), setValueGVO.getListenerType());
                } else if(dataContainerGVO.getKind() == DataContainerGVO.KIND_COLLECTION) {
                    listOfDataMap = dataContainerGVO.getListofDC();
                    dataGrid.insertData(listOfDataMap, false, setValueGVO.getSenderId(), setValueGVO.getListenerType());
                } else if(dataContainerGVO.getKind() == DataContainerGVO.KIND_STRING) {
                    String cellOnRowToSet = setValueGVO.getComponentId();
                    listOfDataMap.add(new DataContainerGVO(dataContainerGVO.getDataString()));
                    dataGrid.setDataToCell(new DataContainerGVO(dataContainerGVO.getDataString()), false, setValueGVO.getSenderId(), cellOnRowToSet);
                }
                dataGrid.redraw();
            }
        }
    }
    // CHECKSTYLE.ON: CyclomaticComplexity

    private boolean isInDomain(String valueToSet, String checkedValueDomain) {
        boolean checked = false;
        if (checkedValueDomain != null) {
            String[] domain = checkedValueDomain.split(",");
            if (domain != null) {
                for (int i = 0; i < domain.length && !checked; i++) {
                    if (domain[i].equals(valueToSet)) {
                        checked = true;
                    }
                }
            }
        }
        return checked;
    }
    
    private void processListBox(UIObject uiObject, SetValueGVO setValueGVO, DataContainerGVO dataContainerGVO) {
        ListBox listBox = (ListBox) uiObject;
        if (SetValueGVO.ACTION_SET.equals(setValueGVO.getAction())) {
            listBox.clear();
        }
        if (DropDownRenderer.hasEmptyItem(listBox)){
            DropDownRenderer.adaptEmptyItem(listBox);
        }
        if (dataContainerGVO.getListofDC() != null) {
            if (setValueGVO.getMapping() == null) {
                Iterator<DataContainerGVO> itr = dataContainerGVO.getListofDC().iterator();
                String key = null;
                while (itr.hasNext()) {
                    DataContainerGVO data = itr.next();
                    if (data.getKind()==DataContainerGVO.KIND_MAP){
                        Map<String,DataContainerGVO> m = data.getDataMap();
                        String id = DataContainerGVO.resolveValue(m.get("id"));
                        String value = DataContainerGVO.resolveValue(m.get("value"));
                        if(id == null) {
                            id = value;
                        }
                        listBox.addItem(value, id);
                    }
                }
            } else {
                Iterator<DataContainerGVO> itr = dataContainerGVO.getListofDC().iterator();
                while (itr.hasNext()) {
                    DataContainerGVO data = itr.next();
                    if (data.getKind()==DataContainerGVO.KIND_MAP){
                        DataMap m = data.getDataMap();

                        // TODO: id should be changed to value and value should be changed to displayname.
                        String key = getMappedValue("id", setValueGVO.getMapping(), m);
                        String value = getMappedValue("value", setValueGVO.getMapping(), m);
                        if(key == null) {
                            key = value;
                        }
                        listBox.addItem(value, key); // value is the text that will be displayed.
                    }
                }
            }
        }
    }
    
    private String getMappedValue(final String field, Map<String, String> mapping, DataMap dataMap) {
        String mappedValue = null;

        // first find the field you want the value of:
        if (mapping != null) {
            String fieldValue = mapping.get(field);
            if (fieldValue != null) {
                // If a (database) resource has a null key value this key value will be set to ""
                if (dataMap.get(fieldValue) != null) {
                    mappedValue = dataMap.get(fieldValue).toString();
                } else {
                    mappedValue ="";
                    ClientApplicationContext.getInstance().log("The entry key [" + field + "] has a null value");
                }
            }
        }
        return mappedValue;
    }
    
    private void processValue4ListBox(ListBox listBox, Object value, String action) {
        if (SetValueGVO.ACTION_SET.equals(action)) {
            if (value == null) {
                int indexOfValue = -1;
                if (DropDownRenderer.hasEmptyItem(listBox)) {
                    indexOfValue = 0;
                }
                listBox.setSelectedIndex(indexOfValue);
            } else if (!(value instanceof List)) {
                int indexOfValue = DropDownRenderer.getIndexOfValue(listBox, String.valueOf(value));
                if (indexOfValue > -1) {
                    listBox.setSelectedIndex(indexOfValue);
                } else if (listBox.getItemCount() > 0) {
                    indexOfValue = -1;
                    if (DropDownRenderer.hasEmptyItem(listBox)) {
                        indexOfValue = 0;
                    }
                    listBox.setSelectedIndex(indexOfValue);
                } else {
                    DropDownRenderer.adaptItem(listBox, value);
                }
            } else if (value instanceof List) {
                List itemList = (List)value;
                DropDownRenderer.adaptItems(listBox, itemList, true);
            }
        } else if (SetValueGVO.ACTION_ADD.equals(action)) {
            if (value instanceof String) {
                DropDownRenderer.adaptItem(listBox, value);
            } else if (value instanceof List) {
                List itemList = (List)value;
                DropDownRenderer.adaptItems(listBox, itemList, false);
            }
        }
    }
    
    // CHECKSTYLE.OFF: CyclomaticComplexity
    private void processNamedComponent(Widget widget, Object value, SetValueGVO setValueGVO, DataContainerGVO dataContainerGVO) {
        if (dataContainerGVO == null) {
            return;
        }
        UIObject uiObject = widget;
        if (!isNamedComponent(uiObject)) {
            return;
        }
        
        String componentName = getComponentName(uiObject);
        switch (dataContainerGVO.getKind()) {
            case DataContainerGVO.KIND_VALUE: {
                process(uiObject, (String)value, setValueGVO, dataContainerGVO);
            } break;
            case DataContainerGVO.KIND_STRING: {
                process(uiObject, dataContainerGVO.getDataString(), setValueGVO, dataContainerGVO);
            } break;
            case DataContainerGVO.KIND_MAP: {
                DataMap dataMap = dataContainerGVO.getDataMap();
                Object data = null;
                DataContainerGVO dcGVO = null;
                if (dataMap.containsKey(componentName.toUpperCase()) || dataMap.containsKey(componentName.toLowerCase())) {
                    if (dataMap.containsKey(componentName.toUpperCase())) {// for database interaction every field is capitalized.
                        data = DataContainerGVO.createType(dataMap.get(componentName.toUpperCase()));
                        dcGVO = dataMap.get(componentName.toUpperCase());
                    } else if (dataMap.containsKey(componentName.toLowerCase())) {
                        data = DataContainerGVO.createType(dataMap.get(componentName.toLowerCase()));
                        dcGVO = dataMap.get(componentName.toLowerCase());
                    }
                    if (widget instanceof TitledComponent) {
                        uiObject = ((TitledComponent) widget).getDataComponent();
                    }
                    processValue(uiObject, data, setValueGVO, dcGVO);
                } else if (dataMap.containsKey(componentName)) { // TODO Probably not needed anymore!
                    data = DataContainerGVO.resolveValue(dataMap.get(componentName));
                    if (widget instanceof TitledComponent) {
                        uiObject = ((TitledComponent) widget).getDataComponent();
                    }
                    processValue(uiObject, data, setValueGVO, dataMap.get(componentName));
                }
            } break;
            case DataContainerGVO.KIND_COLLECTION: {
                List<DataContainerGVO> dataContainerGVOs = dataContainerGVO.getListofDC();
                if (dataContainerGVOs != null) {
                    for(DataContainerGVO dcGVO: dataContainerGVOs) {
                        DataContainerGVO containerGVO = new DataContainerGVO();
                        if (dcGVO.getKind() == DataContainerGVO.KIND_MAP){
                            containerGVO.setDataMap(dcGVO.getDataMap());
                            containerGVO.setKind(DataContainerGVO.KIND_MAP);
                            setValueGVO.setDataContainer(containerGVO);
                            processNamedComponent(widget, value, setValueGVO, dataContainerGVO);
                        }
                    }
                }
            } break;
        }
    }
    // CHECKSTYLE.ON: CyclomaticComplexity

    private void processWidgets(HasWidgets hasWidgets, Object value, SetValueGVO setValueGVO, DataContainerGVO dataContainerGVO) {
        Iterator<Widget> itr = hasWidgets.iterator();
        while (itr.hasNext()) {
            Widget widget = itr.next();
            processNamedComponent(widget, value, setValueGVO, dataContainerGVO);
            if (widget instanceof HasWidgets) {
                HasWidgets innerHasWidget = (HasWidgets) widget;
                processWidgets(innerHasWidget, value, setValueGVO, dataContainerGVO);
            }
        }
    }
}