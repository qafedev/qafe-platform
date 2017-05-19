/**
 * Copyright 2008-2017 Qualogy Solutions B.V.
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
package com.qualogy.qafe.gwt.client.storage.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.Random;
import com.qualogy.qafe.gwt.client.storage.DataStorage;
import com.qualogy.qafe.gwt.client.vo.functions.DataContainerGVO;

public class LocalDataStorage implements DataStorage {

    private Map<String, Map<String, Object>> storage = new HashMap<String, Map<String, Object>>();

    public final void storeData(final String dataId, final String name, final Object data) {
        if (!storage.containsKey(dataId)) {
            storage.put(dataId, new HashMap<String, Object>());
        }
        final Map<String, Object> values = storage.get(dataId);
        values.put(name, data);
    }

    public final void removeData(final String dataId) {
        if (storage.containsKey(dataId)) {
            storage.remove(dataId);
        }
    }
    
    public final void removeData(final String dataId, String name) {
       if (storage.containsKey(dataId)) {
           Map<String, Object> values = storage.get(dataId);
           
           if (values == null) {
               return;
           }
           values.remove(name);
       }
    }
    
    public final Object getData(final String dataId, final String name) {
        final Map<String, Object> values = storage.get(dataId);
        if (values == null) {
            return null;
        }
        if (name == null) {
        	return values.get(name);
        }
        Object result = null;
        result = resolveIndex(name, values, result);
        result = resolveDotOperator(name, values, result);
        
        if (result == null) {
            result = values.get(name);
        }
        return result;
    }

    /**
     * If the reference contains a dot, we retrieve data
     * by using what comes after the . as key
     * 
     * @param name the reference
     * @param values the values to retrieve data from
     * @param result the result object after resolving is done
     */
    private Object resolveDotOperator(final String name, final Map<String, Object> values, Object result) {
        if (name.contains(".")) {
            String[] splitName = name.split("\\.");
            String key = splitName[0];
            String attribute = splitName[1];
            Object keyValue = values.get(key);
            if (result != null) {
                keyValue = result;
            }
            if (keyValue instanceof DataContainerGVO) {
                DataContainerGVO dataContainer = (DataContainerGVO) keyValue;
                switch (dataContainer.getKind()) {
                    case DataContainerGVO.KIND_MAP: {
                        keyValue = dataContainer.getDataMap();
                    }
                }
            }
            if (keyValue instanceof Map) {
                result = ((Map) keyValue).get(attribute);
            }
        }
        return result;
    }

    /**
     * Resolve the value at the specified index
     * 
     * @param name the reference
     * @param values a map of values where the data will be retrieved from
     * @param result the object in which the final variable will be set
     */
    private Object resolveIndex(final String name, final Map<String, Object> values, Object result) {
        if (name.contains("[")) {
            String newName = name.replace("[", ":").replace("]", ":");
            String[] splitName = newName.split(":");
            String key = splitName[0];
            String index = splitName[1];
            Object keyValue = values.get(key);
            if (keyValue instanceof DataContainerGVO) {
                DataContainerGVO dataContainer = (DataContainerGVO) keyValue;
                switch (dataContainer.getKind()) {
                    case DataContainerGVO.KIND_COLLECTION: {
                        keyValue = dataContainer.getListofDC();
                    }
                }
            }
            if (keyValue instanceof List) {
                List listValue = (List) keyValue;
                result = listValue.get(Integer.valueOf(index));
            }
        }
        return result;
    }

    public final String register() {
        final String uniqueId = String.valueOf(Random.nextInt());
        return uniqueId;
    }

    public final void unregister(String dataId) {
        removeData(dataId);
    }
}
