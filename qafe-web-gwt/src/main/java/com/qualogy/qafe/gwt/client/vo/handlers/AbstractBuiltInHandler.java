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
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.UIObject;
import com.qualogy.qafe.gwt.client.component.HasDataGridMethods;
import com.qualogy.qafe.gwt.client.storage.DataStorage;
import com.qualogy.qafe.gwt.client.ui.renderer.AnyComponentRenderer;
import com.qualogy.qafe.gwt.client.ui.renderer.RendererHelper;
import com.qualogy.qafe.gwt.client.util.ComponentRepository;
import com.qualogy.qafe.gwt.client.util.JNSIUtil;
import com.qualogy.qafe.gwt.client.util.QAMLConstants;
import com.qualogy.qafe.gwt.client.vo.data.EventDataGVO;
import com.qualogy.qafe.gwt.client.vo.data.EventItemDataGVO;
import com.qualogy.qafe.gwt.client.vo.data.GEventItemDataObject;
import com.qualogy.qafe.gwt.client.vo.functions.BuiltInFunctionGVO;
import com.qualogy.qafe.gwt.client.vo.functions.DataContainerGVO;
import com.qualogy.qafe.gwt.client.vo.ui.ComponentGVO;
import com.qualogy.qafe.gwt.client.vo.ui.event.ParameterGVO;

public abstract class AbstractBuiltInHandler implements BuiltInHandler {

	@Override
	public final BuiltInState handleBuiltIn(UIObject sender, String listenerType, Map<String, String> mouseInfo
			, BuiltInFunctionGVO builtInGVO, String appId, String windowId
			, String eventSessionId, Queue derivedBuiltIns) {
		return executeBuiltIn(sender, listenerType, mouseInfo, builtInGVO, appId, windowId, eventSessionId, derivedBuiltIns);
	}
	 
    protected Object getValue(UIObject sender, ParameterGVO parameterGVO, String appId, String windowId,
            String eventSessionId) {
        Object value = null;
        if (parameterGVO == null) {
            return value;
        }

        Map<String, Object> placeHolderValues = resolvePlaceholderValues(parameterGVO, sender, appId, windowId, eventSessionId);
        value = getValue(sender, parameterGVO, appId, windowId, eventSessionId, placeHolderValues);
        if (value instanceof DataContainerGVO) {
        	value = DataContainerGVO.createType((DataContainerGVO)value);	
        }
        return value;
    }

    private final Object getValue(final UIObject sender, final ParameterGVO parameterGVO,
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
            final String reference = resolveVariables(parameterGVO.getReference(), placeHolderValues, eventSessionId);

            if (BuiltInFunctionGVO.SOURCE_COMPONENT_ID.equals(source)) {
                value = getComponentValue(sender, reference, appId, windowId, eventSessionId);
            } else {
                if (BuiltInFunctionGVO.SOURCE_DATASTORE_ID.equals(source)
                        && placeHolderValues.containsKey(reference)) {
                    value = placeHolderValues.get(reference);
                } else {
                    final String dataId = generateDataId(source, appId, windowId, eventSessionId);

                    value = getData(dataId, reference);
                }
            }
        } else if (parameterGVO.getExpression() != null) {
        	String expr = parameterGVO.getExpression();
        	expr = resolveExpression(expr, placeHolderValues, eventSessionId);
        	value = evaluateExpression(expr);
        }
        return value;
    }

    // CHECKSTYLE.OFF: CyclomaticComplexity
    // The solution is based on retrieving value from textfield
    // TODO: Refactor to handle multiple components
    private Object getComponentValue(final UIObject sender, final String reference, String appId,
            String windowId, String eventSessionId) {
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
            final String key = generateId(reference, windowId, appId, eventSessionId); // inputVariables[i][1]
            log(key);
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
                log("Reference" + reference);
                String[] keysSet = reference.split("[.]");
                if (keysSet != null) {
                    String searchKey = null;
                    if (keysSet.length == 1) {// so only the key
                        searchKey = key;
                    } else {
                        searchKey = generateId(keysSet[0], windowId, appId, eventSessionId);
                    }
                    if (searchKey != null) {
                        uiObjects = ComponentRepository.getInstance().getNamedComponent(searchKey);
                        if (uiObjects != null) {
                            for (UIObject uiObject : uiObjects) {
                                log("Title" + uiObject.getTitle());

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
    
    /**
	 * 
	 * @param name
	 * @param placeHolderValues
	 * @param eventSessionId
	 * @return
	 */
	protected String resolveExpression(String name, Map<String, Object> placeHolderValues,
            String eventSessionId) {

        while (name != null && name.contains("${")) {
            String varName = name.substring(name.indexOf("{") + 1, name.indexOf("}"));
            Object value = null;
            if (placeHolderValues != null && placeHolderValues.containsKey(varName)) {
                value = placeHolderValues.get(varName);
            } else {
                value = getData(eventSessionId, varName);
            }
            if (value instanceof DataContainerGVO) {
        		value = DataContainerGVO.createType((DataContainerGVO) value);
        	}  
        	value = resolveExpression(value);
            name = name.replace("${" + varName + "}", value.toString());
        }
        return name;
    }
	
	private String resolveExpression(Object value) {
		if (value == null) {
			return "None";
		}
		if (value instanceof String) {
    		return "'" + value + "'";
    	}
		if (value instanceof Boolean) {
        	boolean bool = (Boolean) value;
        	return bool ? "True" : "False";		
        }
		if (value instanceof Date) {
			return resolveExpression(value.toString());
		}
		if (value instanceof Map) {
			Map mapValue = (Map) value;
			StringBuilder newValueBuilder = new StringBuilder();
			newValueBuilder.append("{");
			for (Object key : mapValue.keySet()) {
				newValueBuilder.append(resolveExpression(key));
				newValueBuilder.append(":");
				Object keyValue = mapValue.get(key);
				newValueBuilder.append(resolveExpression(keyValue));
				newValueBuilder.append(",");
			}
			newValueBuilder.append("}");
			return newValueBuilder.toString();
		}
		if (value instanceof Collection) {
			Collection collectionValue = (Collection) value;
			StringBuilder newValueBuilder = new StringBuilder();
			newValueBuilder.append("[");
			for (Object item : collectionValue) {
				newValueBuilder.append(resolveExpression(item));
				newValueBuilder.append(",");
			}
			newValueBuilder.append("]");
			return newValueBuilder.toString();
		}
		return value.toString();
	}
	
    protected Map<String, Object> resolvePlaceholderValues(ParameterGVO parameterGVO, UIObject sender, String appId, String windowId, String eventSessionId) {
		Map<String, Object> placeHolderValues = new HashMap<String, Object>();
        if (parameterGVO.getPlaceHolders() != null && !parameterGVO.getPlaceHolders().isEmpty()) {
            for (ParameterGVO placeholder : parameterGVO.getPlaceHolders()) {
                Object placeHolderValue = getValue(sender, placeholder, appId, windowId, eventSessionId);
                placeHolderValues.put(placeholder.getName(), placeHolderValue);
            }
        }
        return placeHolderValues;
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
            if (value == null) {
                value = "null";
            }
            name = name.replace("${" + varName + "}", value.toString());

        }
        return name;
    }

    protected int getRowIndex(String component, HasDataGridMethods hasDataGridMethods) {
		int rowIndex = -1;
		int indexPrefix = component.indexOf("[");
		if (indexPrefix > -1) {
			int indexPostfix = component.indexOf("]");
			String selectedIndex = component.substring(indexPrefix + 1, indexPostfix);
			rowIndex = hasDataGridMethods.getRowIndex(selectedIndex);
		}
		return rowIndex;
	}
	
	protected List<UIObject> collectCellUIObjects(String component, int rowIndex, List<UIObject> cellUIObjects) {
		if (cellUIObjects == null) {
			cellUIObjects = new ArrayList<UIObject>();
		}
		component = component.replaceFirst("\\[.+\\]", "");
		boolean rowSelection = (rowIndex > -1);
		while (true) {
			String cellKey = QAMLConstants.TOKEN_INDEXING + rowIndex + QAMLConstants.TOKEN_INDEXING + component;  
			List<UIObject> uiObjects = ComponentRepository.getInstance().getComponent(cellKey);
			if (uiObjects != null) {
				cellUIObjects.addAll(uiObjects);
			}
			if (rowSelection || (uiObjects == null)) {
				break;
			}
			rowIndex++;
		}
		return cellUIObjects;
	}
	
	protected List<UIObject> getParentUIObjects(String component) {
		String parentKey = component.replaceFirst("\\.[\\w\\$]+\\|", "|").replaceFirst("\\[.+\\]", "");
		return ComponentRepository.getInstance().getComponent(parentKey);
	}
	
    protected void storeData(String dataId, String name, Object data) {
    	EventHandler.getInstance().storeData(dataId, name, data);
    }

    protected Object getData(String dataId, String name) {
        return getDataStorage().getData(dataId, name);
    }
    
    protected void removeData(String dataId) {
        getDataStorage().removeData(dataId);
    }
    
    protected void removeData(String dataId, String name) {
        getDataStorage().removeData(dataId, name);
    }
    
    protected DataStorage getDataStorage() {
    	return EventHandler.getInstance().getDataStorage();
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

	/**
	 * Get the component key.
	 * 
	 * @param componentId
	 * @param appId
	 * @param windowId
	 * @param eventSessionId
	 * @return The key
	 */
    protected String generateId(String componentId, String windowId, String appId, String eventSessionId) {
        if (componentId != null && componentId.contains("[")) {
            // extract component id when dealing with indexes
            componentId = componentId.substring(0, componentId.indexOf("["));
        }
        
    	componentId = resolveVariables(componentId, null, eventSessionId);
        return RendererHelper.generateId(componentId, windowId, appId);
    }
    
	/**
	 * Search for UIObjects considering ref is id.
	 * 
	 * @param key
	 * @return List of UIObjects
	 */
	protected List<UIObject> getUIObjectsById(String key) {
		return ComponentRepository.getInstance().getComponent(key);
	}
	
	/**
	 * Search for UIObjects considering ref is name.
	 * 
	 * @param key
	 * @return List of UIObjects
	 */
	protected List<UIObject> getUIObjectsByName(String key) {		
		return ComponentRepository.getInstance().getNamedComponent(key);
	}
	
	/**
	 * Search for UIObjects considering ref is group.
	 * 
	 * @param key
	 * @return List of UIObjects
	 */
	protected List<UIObject> getUIObjectsByGroup(String key) {		
		return ComponentRepository.getInstance().getGroupedComponent(key);
	}
	
	/**
	 * Search for UIObjects
	 * @param key
	 * @return
	 */
	protected List<UIObject> getUIObjects(String key) {		
		List<UIObject> uiObjects = getUIObjectsById(key);
		if (uiObjects != null) {
			return uiObjects;
		}
		uiObjects = getUIObjectsByName(key);
		if (uiObjects != null) {
			return uiObjects;
		}
		uiObjects = getUIObjectsByGroup(key);
		return uiObjects;
	}
    
    protected String getSenderId(final UIObject sender) {
        return RendererHelper.getComponentId(sender);
    }

    protected String getComponentName(final UIObject uiObject) {
        return RendererHelper.getNamedComponentName(uiObject);
    }
    
    protected String getUUId(UIObject sender) {
    	return RendererHelper.getUUId(sender);
    }
    
    protected String getAppId(final UIObject sender) {
        return RendererHelper.getComponentContext(sender);
    }

    protected boolean isNamedComponent(final UIObject uiObject) {
        return RendererHelper.isNamedComponent(uiObject);
    }

    protected UIObject renderComponent(ComponentGVO componentGVO, String appId, String windowId, String eventSessionId) {
    	return AnyComponentRenderer.getInstance().render(componentGVO, eventSessionId, windowId, appId);    	
    }
    
    protected void log(String message) {
        log("Log", message);
    }
    
    protected void log(String title, String message) {
        log(title, message, false);
    }
    
    protected void log(String title, String message, boolean alert) {
        EventHandler.getInstance().log(title, message, alert);
    }
    
    protected void executeBuiltInServerSide(UIObject sender, String listenerType, Map<String, String> mouseInfo
			, EventItemDataGVO eventItemDataGVO, String appId, String windowId, String eventSessionId) {
    	setBusy(true);
    	AsyncCallback<?> callback = createCallback(sender, listenerType, mouseInfo, eventItemDataGVO, appId, windowId, eventSessionId);
    	EventHandler.getInstance().getRPCService().executeEventItem(eventItemDataGVO, callback);
    }
    
    private AsyncCallback<?> createCallback(final UIObject sender, final String listenerType
    		, final Map<String, String> mouseInfo, final EventItemDataGVO eventItemDataGVO
    		, final String appId, final String windowId, final String eventSessionId) {

        return new AsyncCallback<Object>() {

            @Override
			public void onSuccess(Object result) {
            	setBusy(false);
                GEventItemDataObject data = (GEventItemDataObject) result;
                storeOutputValues(eventSessionId, data);
                EventHandler.getInstance().handleEvent(eventSessionId, sender, listenerType
                		, mouseInfo, appId, windowId);
            }

            @Override
			public void onFailure(Throwable exception) {
            	setBusy(false);
            	Object currentBuiltIn = eventItemDataGVO.getBuiltInGVO();
            	EventHandler.getInstance().handleException(exception, currentBuiltIn
            			, sender, listenerType, mouseInfo, appId, windowId, eventSessionId);
            }
        };
    }

    private void storeOutputValues(String eventSessionId, GEventItemDataObject data) {
        Map<String, Object> outputValues = data.getOutputValues();
        if (outputValues == null) {
            return;
        }
        Iterator<String> itrOutputName = outputValues.keySet().iterator();
        while (itrOutputName.hasNext()) {
        	String outputName = itrOutputName.next();
        	Object outputValue = outputValues.get(outputName);
        	if (outputValue instanceof DataContainerGVO) {
        		outputValue = DataContainerGVO.createType((DataContainerGVO) outputValue);
        	}
        	storeData(eventSessionId, outputName, outputValue);
        }
    }
    
    protected void setBusy(boolean busy) {
    	EventHandler.getInstance().setBusy(busy);
    }
    
    protected void showMessage(String title, String message) {
   	 	showMessage(title, message, null);
    }
    
    protected void showMessage(String title, String message, Throwable exception) {
    	EventHandler.getInstance().showMessage(title, message, exception);
    }
    
    protected String evaluateExpression(String expression) {
    	return JNSIUtil.evaluateExpression(expression);
    }
    
    protected String getShortenName(Object builtIn) {
    	return EventHandler.getInstance().getShortenName(builtIn);
	}
    
    protected String getSessionId() {
        return EventHandler.getInstance().getSessionId();
    }
    
    protected abstract BuiltInState executeBuiltIn(UIObject sender, String listenerType, Map<String, String> mouseInfo
			, BuiltInFunctionGVO builtInGVO, String appId, String windowId
			, String eventSessionId, Queue derivedBuiltIns);	
}
