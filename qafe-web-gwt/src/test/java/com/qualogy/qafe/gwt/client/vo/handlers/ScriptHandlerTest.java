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

import static org.junit.Assert.assertEquals;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;

public class ScriptHandlerTest {

    @Test
    public void testResolveJSValue() throws ParseException {
        CallScriptHandler scriptHandler = new CallScriptHandler();
        
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date date = sdf.parse("2015-01-01 14:22:33");
        String expectedDateString = "'" + date.toString() + "'";
        
        assertEquals(scriptHandler.resolveJSValue(42), "42");
        assertEquals(scriptHandler.resolveJSValue(2.4d), "2.4");
        assertEquals(scriptHandler.resolveJSValue(3.1f), "3.1");
        assertEquals(scriptHandler.resolveJSValue("foo"), "'foo'");
        assertEquals(scriptHandler.resolveJSValue(true), "true");
        assertEquals(scriptHandler.resolveJSValue(null), "null");
        assertEquals(scriptHandler.resolveJSValue(date), expectedDateString);
    }
}
