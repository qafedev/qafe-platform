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
package com.qualogy.qafe.gwt.client.storage.impl;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.user.client.Random;
import com.qualogy.qafe.gwt.client.storage.DataStorage;

public class LocalDataStorage implements DataStorage {
    
    private Map<String, Map<String, Object>> storage = new HashMap<String, Map<String,Object>>();

    public void storeData(String dataId, String name, Object data) {
        if (!storage.containsKey(dataId)) {
            storage.put(dataId, new HashMap<String, Object>());
        }
        Map<String, Object> values = storage.get(dataId);
        values.put(name, data);
    }

    public void removeData(String dataId) {
        if (storage.containsKey(dataId)) {
            storage.remove(dataId);
        }
    }
    
    public Object getData(String dataId, String name) {
        Map<String, Object> values = storage.get(dataId);
        if (values == null) {
            return null;
        }
        return values.get(name);
    }

    public String register() {
        String uniqueId = String.valueOf(Random.nextInt());
        return uniqueId;
    }

    public void unregister(String dataId) {
        removeData(dataId);
    }
}