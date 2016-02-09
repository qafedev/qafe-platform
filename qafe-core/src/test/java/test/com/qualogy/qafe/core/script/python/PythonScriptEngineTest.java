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
package test.com.qualogy.qafe.core.script.python;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import com.qualogy.qafe.core.script.Script;
import com.qualogy.qafe.core.script.python.PythonScriptEngine;

public class PythonScriptEngineTest extends TestCase {

	private PythonScriptEngine engine;
	private Map params = new HashMap();
	
	public void testNullCheck(){

		params.put("name", "jan");
		
		assertEquals(false, engine.eval(new Script("name == None", params)));
		assertEquals(true, engine.eval(new Script("name == 'jan'", params)));
	}
	
	public void testCalcOutput(){
		
		params.put("ja", new Integer(8));
		
		assertEquals("1", engine.process(new Script("((1+1)*4)/ja", params)));
	}
	
	public void testEval(){
		String expr = "(not (a == None)) and a>=b";
		
		params.put("a", null);
		params.put("b", new Integer(8));
		assertEquals(false, engine.eval(new Script(expr, params)));
		
		params.put("a", new Integer(8));
		params.put("b", new Integer(8));
		assertEquals(true, engine.eval(new Script(expr, params)));
		
		params.put("a", new Integer(8));
		params.put("b", new Integer(9));
		assertEquals(false, engine.eval(new Script(expr, params)));
		
		params.put("a", new Integer(8));
		params.put("b", new Integer(6));
		assertEquals(true, engine.eval(new Script(expr, params)));
	}
	
	public void testDigits(){
		String expr = "x*y";
		
		params.put("x", new Double(10.25));
		params.put("y", new Integer(4));
		Object result = engine.process(new Script(expr, params));
		assertEquals("41.0", result);
	}

	protected void setUp() throws Exception {
		super.setUp();
		engine = new PythonScriptEngine();
		engine.init();
		
		params = new HashMap();
	}
	
}
