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
package com.qualogy.qafe.gwt.client.storage.impl;

import java.util.Collection;
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
        Object result = null;
        if (values == null) {
            return null;
        }
        if (name == null) {
        	return values.get(name);
        }
        if (name.contains(".")) {
        	String[] splitName = name.split(".");
        	String dataStoreKey = splitName[0];
        	String attribute = splitName[1];
        	Object intermediateResult = values.get(dataStoreKey);
            if (intermediateResult instanceof DataContainerGVO) {
                DataContainerGVO dataContainer = (DataContainerGVO) intermediateResult;
                switch (dataContainer.getKind()) {
                    case DataContainerGVO.KIND_MAP: {
                        result = dataContainer.getDataMap().get(attribute);
                    }
                }
            }
        } else if (name.contains("[")) {
        	String newName = name.replace("[", ":").replace("]", ":");
        	String[] splitName = newName.split(":");
        	String dataStoreKey = splitName[0];
        	String index = splitName[1];
        	Object value = values.get(dataStoreKey);
        	if (value instanceof List) {
        		List collectionValue = (List) value;
        		result = collectionValue.get(Integer.valueOf(index));
        	}
        } else {
            result = values.get(name);
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
