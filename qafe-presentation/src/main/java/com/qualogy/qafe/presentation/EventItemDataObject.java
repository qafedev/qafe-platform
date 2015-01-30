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
package com.qualogy.qafe.presentation;

import java.io.Serializable;

public class EventItemDataObject implements Serializable {

    private static final long serialVersionUID = 349178336673442712L;
    
    private String sessionId;
    
    private String applicationId;
    
    private String windowId;
   
    public EventItemDataObject(final String sessionId, final String appId
            , final String windowId) {
        this.sessionId = sessionId;
        this.applicationId = appId;
        this.windowId = windowId;
    }

    public String getApplicationId() {
        return applicationId;
    }
    
    public String getSessionId() {
        return sessionId;
    }
    
    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getWindowId() {
        return windowId;
    }
    
    public void setWindowId(String windowId) {
        this.windowId = windowId;
    }
   
}
