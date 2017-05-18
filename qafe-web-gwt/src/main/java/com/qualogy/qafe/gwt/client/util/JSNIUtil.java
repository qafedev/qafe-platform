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
package com.qualogy.qafe.gwt.client.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONValue;
import com.qualogy.qafe.gwt.client.vo.handlers.EventHandler;



public class JSNIUtil { 
	
	public static final String ROOT_PANEL_PARAM = "rootpanel";
	
	public static native String getString(String jsVar, String defaultValue)/*-{ 
    	var value = eval('$wnd.'+jsVar); 
	    if(value){ 
	    	return value; 
      	} 
	    return defaultValue; 
    }-*/;
	
	/**
	 * Retrieve parameters in a script element defined as key1=value1,key2=value2 
	 * 
	 * @param moduleName the name of the module to ge the params from
	 * 
	 * @return the value of the paramName or an empty string
	 **/
	public static native String getScriptParameter(String moduleName, String paramName)/*-{		
		var search = "/" + moduleName + ".nocache.js";
	    var scripts = $doc.getElementsByTagName("script");
	    
	    for (var i = 0; i < scripts.length; ++i) {   	
	        if (scripts[i].src != null && scripts[i].src.indexOf(search) != -1 ) {
	        	// returns all formats that match key=value (note that spaces and dashes ("-") are not supported)
	            var parameters = scripts[i].src.match(/\w+=\w+/g);
	            if (parameters != null) {
		            for (var j = 0; j < parameters.length; ++j) {
		                var keyvalue = parameters[j].split("=");
		                if (keyvalue.length == 2 && keyvalue[0] == paramName) {
		                    return unescape(keyvalue[1]);
		                }
		            }
	            }
	        } 
	    }
	    return null;
	}-*/;
	
	public static native String evaluateExpression(String expression) /*-{
	 	var expr = "resultValue=" + expression;
		result = eval($wnd.Sk.importMainWithBody("<stdin>", false, expr));
		return String(result.$d.resultValue.v);
	}-*/;
	
    /**
     * Execute the given JavaScript function with the given parameters.
     * 
     * @param functionName  Name of an available JavaScript function.
     * @param params        Parameters to be passed to the JavaScript function.
     * @return              The result of the executed function.
     */
    public static native String executeJavascript(String functionName, String params) /*-{
        var result = eval("$wnd."+ functionName + "(" + params + ")");    
        return String(result);                                                                     
    }-*/;
	
    /**
     * Assigns JavaScript functions, converted from Java functions by means of JSNI, 
     * to JavaScript Window identifiers. 
     */
	public static native void exportQafeFunctions() /*-{
       $wnd.invokeQafeEvent = @com.qualogy.qafe.gwt.client.util.JSNIUtil::invokeQafeEvent(*);                                                                     
  	}-*/;

	public static void invokeQafeEvent(String eventId, String windowId, String appId, JavaScriptObject javaScriptObject) {
		Map<String, Object> value = resolveJavaMap(javaScriptObject);
		EventHandler.getInstance().handleEvent(eventId, windowId, appId, value);
	}
	
	private static Map<String, Object> resolveJavaMap(JavaScriptObject jsValue) {
		Map<String, Object> value = new HashMap<String, Object>();
		if (jsValue == null) {
			return value;
		}
		
		JSONObject jsonObject = new JSONObject(jsValue);
		if (jsonObject.isObject() == null) {
			return value;
		}
		
		value = resolveJavaMap(jsonObject);
		
		return value;
	}
	
	private static Map<String, Object> resolveJavaMap(JSONObject jsonObject) {
		if (jsonObject == null) {
			return null;
		}
		Map<String, Object> value = new HashMap<String, Object>();
		Set<String> keys = jsonObject.keySet();
		for (String key : keys) {
			JSONValue jsonValue = jsonObject.get(key);
			Object keyValue = resolveJavaValue(jsonValue);
			value.put(key, keyValue);
		}
		return value;
	}
	
    /**
     * Convert JavaScript objects to their Java representations.
     */
    static Object resolveJavaValue(JSONValue jsonValue) {
		if (jsonValue == null) {
			return null;
		}
		Object value = null;
		if (jsonValue.isArray() != null) {
			value = resolveJavaList(jsonValue.isArray());
		} else if (jsonValue.isObject() != null) {
			value = resolveJavaMap(jsonValue.isObject());
		} else if (jsonValue.isBoolean() != null) {
			value = jsonValue.isBoolean().booleanValue();
		} else if (jsonValue.isNumber() != null) {
			value = jsonValue.isNumber().doubleValue();
		} else if (jsonValue.isString() != null) {
			value = jsonValue.isString().stringValue();
		}
		return value;
    }
	
	private static List<Object> resolveJavaList(JSONArray jsonArray) {
		if (jsonArray == null) {
			return null;
		}
		List<Object> value = new ArrayList<Object>();
		for (int i=0; i<jsonArray.size(); i++) {
			JSONValue jsonValue = jsonArray.get(i);
			Object keyValue = resolveJavaValue(jsonValue);
			if (keyValue != null) {
				value.add(keyValue);
			}
		}
		return value;
	}    
}		