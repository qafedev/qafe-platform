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
package com.qualogy.qafe.gwt.client.vo.data;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.qualogy.qafe.gwt.client.vo.functions.BuiltInFunctionGVO;

public class EventItemDataGVO implements IsSerializable {
    
    private static final long serialVersionUID = 8381696301664889297L;

    private String appId;
    private String windowId;
    private String sessionId;
    
    private Map<String, Object> inputValues = new HashMap<String, Object>();
    
    private Map<String, String> outputVariables = new HashMap<String, String>();
    
    private Map<String, Object> internalVariables = new HashMap<String, Object>();

    private BuiltInFunctionGVO builtInGVO;

    public final String getAppId() {
        return appId;
    }

    public final void setAppId(final String applicationId) {
        this.appId = applicationId;
    }

    public final String getWindowId() {
        return windowId;
    }

    public final void setWindowId(final String windowId) {
        this.windowId = windowId;
    }

    public final String getSessionId() {
        return sessionId;
    }

    public final void setSessionId(final String sessionId) {
        this.sessionId = sessionId;
    }
    
    public final BuiltInFunctionGVO getBuiltInGVO() {
        return builtInGVO;
    }

    public final void setBuiltInGVO(final BuiltInFunctionGVO builtInGVO) {
        this.builtInGVO = builtInGVO;
    }
    
    public Map<String, Object> getInputValues() {
        return inputValues;
    }

    public void setInputValues(Map<String, Object> inputValues) {
        this.inputValues = inputValues;
    }
	
    public Map<String, String> getOutputVariables() {
        return outputVariables;
    }
    
    public void setOutputVariables(Map<String, String> outputVariables) {
        this.outputVariables = outputVariables;
    }

    /**
	 * @return the internalVariables
	 */
	public Map<String,Object> getInternalVariables() {
		return internalVariables;
	}

	/**
	 * @param internalVariables the internalVariables to set
	 */
	public void setInternalVariables(Map<String,Object> internalVariables) {
		this.internalVariables = internalVariables;
	}
}