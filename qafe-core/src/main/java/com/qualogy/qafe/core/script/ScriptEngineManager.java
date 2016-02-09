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
package com.qualogy.qafe.core.script;

import com.qualogy.qafe.bind.core.application.ApplicationContext;
import com.qualogy.qafe.bind.core.application.Configuration;
import com.qualogy.qafe.core.script.python.PythonScriptEngine;

public class ScriptEngineManager {

	public final static String ENGINE_NAME_PYTHON = "python";
	
	private static ScriptEngine pythonScriptEngine = new PythonScriptEngine();
	
	private ScriptEngineManager(){}
	
	private static ScriptEngine getEngineByName(String engineName){
		ScriptEngine scriptEngine =null;
		if (ENGINE_NAME_PYTHON.equalsIgnoreCase(engineName)){
			scriptEngine = pythonScriptEngine; 
		}else{
			throw new IllegalArgumentException("there is no scriptengine defined for enginename ["+engineName+"]");
		}
		return scriptEngine;
	}
	
	public static ScriptEngine getEngine(ApplicationContext context) {
		String engineName = context.getConfigurationItem(Configuration.SCRIPT_EGINE_NAME);
		return getEngineByName(engineName);
	}
	
	public static ScriptEngine getDefaultEngine(){
		return pythonScriptEngine;
	}
	
}
