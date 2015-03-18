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

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import com.google.gwt.user.client.ui.UIObject;
import com.qualogy.qafe.gwt.client.vo.functions.BuiltInFunctionGVO;
import com.qualogy.qafe.gwt.client.vo.functions.CallScriptGVO;
import com.qualogy.qafe.gwt.client.vo.ui.event.ParameterGVO;

/**
 * 
 */
public final class CallScriptHandler extends AbstractBuiltInHandler {
    
    @Override
    protected BuiltInState executeBuiltIn(UIObject sender, String listenerType,
            Map<String, String> mouseInfo, BuiltInFunctionGVO builtInGVO, String appId, String windowId,
            String eventSessionId, Queue derivedBuiltIns) {
        final CallScriptGVO scriptGVO = (CallScriptGVO) builtInGVO;
        executeScript(sender, scriptGVO, appId, windowId, eventSessionId);
        return BuiltInState.EXECUTED;
    }

    /**
     * Delegates executing to methods specific for the script type.
     */
    private void executeScript(UIObject sender, CallScriptGVO scriptGVO, String appId, String windowId,
            String eventSessionId) {
        if (CallScriptGVO.TYPE_JAVASCRIPT.equals(scriptGVO.getType())) {
            String functionName = scriptGVO.getFunctionName();
            List<ParameterGVO> paramGVOs = scriptGVO.getParams();
            Object[] params = resolveParams(paramGVOs, sender, appId, windowId, eventSessionId);
            executeJavascript(functionName, params);
        }
    }

    /**
     * Obtain for all parameter elements the value.
     */
    private Object[] resolveParams(List<ParameterGVO> paramGVOs, UIObject sender, String appId,
            String windowId, String eventSessionId) {
        if (paramGVOs == null) {
            return null;
        }

        int size = paramGVOs.size();
        Object[] params = new Object[size];
        for (int i=0; i<size; i++) {
            ParameterGVO paramGVO = paramGVOs.get(i);
            Object value = getValue(sender, paramGVO, appId, windowId, eventSessionId);
            params[i] = value;
        }
        return params;
    }
    
    /**
     * Convert Java objects to their JavaScript string representations.
     */
    String resolveJSValue(Object value) {
        if (value == null) {
            return "null";
        }
        
        if (value instanceof Boolean) {
            boolean bool = (Boolean) value;
            return bool ? "true" : "false";     
        }
        
        if (value instanceof Date) {
            return resolveJSValue(value.toString());
        }
        
        if (value instanceof Number) {
            return value.toString();
        }
        
        return "'" + value.toString() + "'";
    }

    /**
     * Aggregates the given parameters to a string representation and invokes 
     * {@link CallScriptHandler#executeJavascript(String, String)}.
     * 
     * @param functionName  Name of an available JavaScript function.
     * @param params        Parameters to be passed to the JavaScript function.
     * @return              The result of the executed function.
     */
    private String executeJavascript(String functionName, Object[] params) {
        String parameters = "";
        if (params != null) {
            for (Object arg : params) {
                String value = resolveJSValue(arg);
                if (parameters.length() > 0) {
                    parameters += ",";
                }
                parameters += value.toString();
            }
        }
        return executeJavascript(functionName, parameters);
    }
    
    /**
     * Execute the given JavaScript function with the given parameters.
     * 
     * @param functionName  Name of an available JavaScript function.
     * @param params        Parameters to be passed to the JavaScript function.
     * @return              The result of the executed function.
     */
    private native String executeJavascript(String functionName, String params) /*-{
        var result = eval("$wnd."+ functionName + "(" + params + ")");    
        return String(result);                                                                     
    }-*/;
}
