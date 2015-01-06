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
package com.qualogy.qafe.gwt.client.util;


public class JNSIUtil { 
	
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
}		