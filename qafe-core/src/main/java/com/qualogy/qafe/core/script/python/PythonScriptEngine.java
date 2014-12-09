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
package com.qualogy.qafe.core.script.python;

import java.util.Iterator;
import java.util.Properties;

import org.python.core.PyObject;
import org.python.util.PythonInterpreter;

import com.qualogy.qafe.core.script.Script;
import com.qualogy.qafe.core.script.ScriptEngine;
import com.qualogy.qafe.core.script.ScriptProcessingFailedException;

public class PythonScriptEngine implements ScriptEngine {
	
	public final static String SCRIPT_PREFIX = "str(";
	public final static String SCRIPT_POSTFIX = ")";
	
	private static PythonInterpreter interp;
	
	public PythonScriptEngine(){
	}
	
	public synchronized void init() {
		if(interp == null){
			PythonInterpreter.initialize(System.getProperties(), new Properties(), new String[]{});
			interp = new PythonInterpreter();
			interp.exec("import sys");
		}
	}
	
	private synchronized PyObject exec(Script script){
		interp.set("result", null);
		try {
			for (Iterator iter = script.getParameters().keySet().iterator(); iter.hasNext();) {
				String key = (String)iter.next();
				Object value = script.getParameters().get(key);
				interp.set(key, value);
			}
			interp.exec("result=" + SCRIPT_PREFIX + script.getValue() + SCRIPT_POSTFIX);
		} catch (Exception e) {
			throw new ScriptProcessingFailedException(script.toString(), e);
		}
		return interp.get("result");
	}

	public boolean eval(String expression) {
		return eval(new Script(expression));
	}

	public boolean eval(Script script) {
		return "1".equals(process(script));
	}

	
	public String process(String expression) {
		return process(new Script(expression));
	}
	
	public String process(Script script) {
		return exec(script).toString();
	}
	
	public void destroy() {
		if(interp!=null)
			interp.cleanup();
	}
}
