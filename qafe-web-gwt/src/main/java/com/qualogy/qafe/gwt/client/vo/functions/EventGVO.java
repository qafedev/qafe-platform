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
package com.qualogy.qafe.gwt.client.vo.functions;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class EventGVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String sourceName;
    private String sourceId;
    private String sourceValue;
    private String sourceListenerType;
    private List<BuiltInFunctionGVO> eventItems = new ArrayList<BuiltInFunctionGVO>();

    public Collection<BuiltInFunctionGVO> getEventItems() {
        return eventItems;
    }

    public void addEventItem(BuiltInFunctionGVO eventItem) {
        if (eventItem == null) {
            return;
        }
        eventItems.add(eventItem);
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getSourceValue() {
        return sourceValue;
    }

    public void setSourceValue(String sourceValue) {
        this.sourceValue = sourceValue;
    }

    public String getSourceListenerType() {
        return sourceListenerType;
    }

    public void setSourceListenerType(String sourceListenerType) {
        this.sourceListenerType = sourceListenerType;
    }
}
