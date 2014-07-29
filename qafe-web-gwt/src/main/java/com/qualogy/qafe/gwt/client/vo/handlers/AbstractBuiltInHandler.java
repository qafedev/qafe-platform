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

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;
import com.qualogy.qafe.gwt.client.context.ClientApplicationContext;
import com.qualogy.qafe.gwt.client.storage.DataStorage;
import com.qualogy.qafe.gwt.client.ui.renderer.AnyComponentRenderer;
import com.qualogy.qafe.gwt.client.ui.renderer.RendererHelper;
import com.qualogy.qafe.gwt.client.util.ComponentRepository;
import com.qualogy.qafe.gwt.client.vo.data.EventDataGVO;
import com.qualogy.qafe.gwt.client.vo.functions.BuiltInFunctionGVO;
import com.qualogy.qafe.gwt.client.vo.functions.DataContainerGVO;
import com.qualogy.qafe.gwt.client.vo.ui.ComponentGVO;
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

    // CHECKSTYLE.OFF: CyclomaticComplexity
    // The solution is based on retrieving value from textfield
    // TODO: Refactor to handle multiple components
    private Object getComponentValue(final UIObject sender, final String reference, String appId,
            String windowId) {
        EventDataGVO dummyEventDataObject = new EventDataGVO();// TODO EventDataGVO is used in server side
                                                               // event handling only, now we use common code
                                                               // that is why we are passing this dummy
                                                               // object.
        final String senderId = getSenderId(sender);

        if (senderId == null || senderId.length() <= 0) {
            return null;
        }

        final String uuid = DOM.getElementProperty(sender.getElement(), "uuid");
        String value = null;

        DataContainerGVO dataContainerObject = null;

        if (BuiltinHandlerHelper.hasAttribute(reference)) {
            value = BuiltinHandlerHelper.getAttributeValue(reference, uuid, windowId, appId);
            dataContainerObject = DataContainerGVO.create(value);
            
        } else if (reference.contains(".$$")) {
            dataContainerObject =
                BuiltinHandlerHelper.fetchDatagridRowValues(reference, uuid, windowId, appId);
        } else if (reference.contains("[")) {
            dataContainerObject =
                BuiltinHandlerHelper.fetchDatagridCellValue(reference, uuid, windowId, appId);
        } else {
            final String key = RendererHelper.generateId(reference, windowId, appId); // inputVariables[i][1]
            ClientApplicationContext.getInstance().log(key);
            List<UIObject> uiObjects = ComponentRepository.getInstance().getComponent(key);

            // since the parameter can be a complex object, we need to create a
            // substitute for it.
            // This can only be used in the name variant though
            // (namedcomponents)
            if (uiObjects != null) {
                for (UIObject uiObject : uiObjects) {

                    Object o = BuiltinHandlerHelper.getValue(uiObject, sender, null, false, null);
                    /* value = ((HasText) uiObject).getText(); */

                    if (o instanceof String) {
                        //value = o.toString();
                        dataContainerObject = DataContainerGVO.create(o.toString());
                    } else if (o instanceof DataContainerGVO) {
                        dataContainerObject = (DataContainerGVO) o;
                    }
                }

            } else {
                // so the object could not be found in the ComponentRepository,
                // maybe we try by name to find it.
                ClientApplicationContext.getInstance().log("Reference" + reference);
                String[] keysSet = reference.split("[.]");
                if (keysSet != null) {
                    String searchKey = null;
                    if (keysSet.length == 1) {// so only the key
                        searchKey = key;
                    } else {
                        searchKey = RendererHelper.generateId(keysSet[0], windowId, appId);
                    }
                    if (searchKey != null) {
                        uiObjects = ComponentRepository.getInstance().getNamedComponent(searchKey);
                        if (uiObjects != null) {
                            for (UIObject uiObject : uiObjects) {
                                ClientApplicationContext.getInstance().log("Title" + uiObject.getTitle());

                                // Collect all the data from a list of named
                                // components
                                DataContainerGVO dataContainer =
                                    BuiltinHandlerHelper.createDataContainer(reference, uiObject, sender,
                                        dummyEventDataObject);
                                if (dataContainerObject == null) {
                                    dataContainerObject = dataContainer;
                                } else if (dataContainer != null) {
                                    if (dataContainer.getKind() == dataContainerObject.getKind()) {
                                        switch (dataContainer.getKind()) {
                                            case DataContainerGVO.KIND_MAP: {
                                                dataContainerObject.getDataMap().putAll(
                                                    dataContainer.getDataMap());
                                            }
                                                break;
                                        }
                                    }
                                }

                                // Get value of a data member
                                if (keysSet.length > 1) {
                                    if (dataContainerObject.getDataMap().get(keysSet[1]) != null) {
                                        value =
                                            dataContainerObject.getDataMap().get(keysSet[1]).getDataString();
                                        dataContainerObject = DataContainerGVO.create(value);
                                    }
                                }
                            }
                        } else { // Apparently we have to search for the Group
                                 // now.
                            dataContainerObject =
                                BuiltinHandlerHelper.getGroupedComponentValue(sender, reference,
                                    dummyEventDataObject, key);
                        }
                    }
                }
            }
        }

        
        return dataContainerObject;
    }

    // CHECKSTYLE.ON: CyclomaticComplexity

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

    protected UIObject renderComponent(ComponentGVO componentGVO, String appId, String windowId, String eventSessionId) {
    	return AnyComponentRenderer.getInstance().render(componentGVO, eventSessionId, windowId, appId);    	
    }
    
    protected void log(String message) {
        ClientApplicationContext.getInstance().log(message);
    }
}
