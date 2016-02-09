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
package com.qualogy.qafe.gwt.client.vo.handlers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public final class OpenWindowHandlerTest {

    private Map<String, String> paramMap;

    private final OpenWindowHandler openWindowHandler = new OpenWindowHandler();

    @Before
    public void initialize() {
        paramMap = new HashMap<String, String>();
    }

    @Test
    public void basicParamTestNoDefault() {

        // Arrange
        OpenWindowHandler.addParameter(paramMap, "test", "hoeba", null);

        // Act
        final String value = paramMap.get("test");

        // Assert
        assertNotNull(value);
        assertEquals("hoeba", value);
    }

    @Test
    public void basicParamTestWithDefault() {

        // Arrange
        OpenWindowHandler.addParameter(paramMap, "test", "hoeba", "check");

        // Act
        final String value = paramMap.get("test");

        // Assert
        assertNotNull(value);
        assertEquals("check", value);
    }

    @Test
    public void parseParameterInputSanityCheck() {

        // Act
        final Map<String, String> params1 = openWindowHandler.parseParams(null);
        final Map<String, String> params2 = openWindowHandler.parseParams("");

        assertNotNull(params1);
        assertNotNull(params2);

        assertEquals(0, params1.size());
        assertEquals(0, params2.size());
    }

    @Test
    public void parseParameterIdentity() {

        // Act
        final Map<String, String> parseParams =
            openWindowHandler
                .parseParams("width=900,height=400,left=30,top=30,menubar=true,scrollbars=true,toolbar=true,status=true,resizable=true");

        // Assert
        assertNotNull(parseParams);
        assertNotNull(parseParams.get("width"));
        assertNotNull(parseParams.get("height"));
        assertNotNull(parseParams.get("left"));
        assertNotNull(parseParams.get("top"));
        assertNotNull(parseParams.get("menubar"));
        assertNotNull(parseParams.get("scrollbars"));
        assertNotNull(parseParams.get("toolbar"));
        assertNotNull(parseParams.get("status"));
        assertNotNull(parseParams.get("resizable"));
    }

    @Test
    public void parseParameterValues() {

        // Act
        final Map<String, String> parseParams =
            openWindowHandler
                .parseParams("width=900,height=400,left=30,top=30,menubar=true,scrollbars=true,toolbar=true,status=true,resizable=true");

        assertEquals("900", parseParams.get("width"));
        assertEquals("400", parseParams.get("height"));
        assertEquals("30", parseParams.get("left"));
        assertEquals("30", parseParams.get("top"));
        assertEquals("yes", parseParams.get("menubar"));
        assertEquals("yes", parseParams.get("scrollbars"));
        assertEquals("yes", parseParams.get("toolbar"));
        assertEquals("yes", parseParams.get("status"));
        assertEquals("yes", parseParams.get("resizable"));
    }

    @Test
    public void parseParameterDefaults() {
        assertEquals("yes", openWindowHandler.parseParams("resizable=").get("resizable"));
        assertEquals("no", openWindowHandler.parseParams("menubar=").get("menubar"));
        assertEquals("no", openWindowHandler.parseParams("scrollbars=").get("scrollbars"));
        assertEquals("no", openWindowHandler.parseParams("toolbar=").get("toolbar"));
        assertEquals("no", openWindowHandler.parseParams("status=").get("status"));
        assertEquals("no", openWindowHandler.parseParams("modal=").get("modal"));
    }

    @Test
    public void parseParameterNoDefaults() {
        assertNull(openWindowHandler.parseParams("top=").get("top"));
        assertNull(openWindowHandler.parseParams("left=").get("left"));
        assertNull(openWindowHandler.parseParams("width=").get("width"));
        assertNull(openWindowHandler.parseParams("height=").get("height"));
    }

    @Test
    public void parseParameterAllowDifferentFacesOfTrue() {
        assertEquals("yes", openWindowHandler.parseParams("scrollbars=true").get("scrollbars"));
        assertEquals("yes", openWindowHandler.parseParams("scrollbars=1").get("scrollbars"));
        assertEquals("yes", openWindowHandler.parseParams("scrollbars=yes").get("scrollbars"));
        assertEquals("no", openWindowHandler.parseParams("scrollbars=false").get("scrollbars"));
        assertEquals("no", openWindowHandler.parseParams("scrollbars=0").get("scrollbars"));
        assertEquals("no", openWindowHandler.parseParams("scrollbars=no").get("scrollbars"));
    }

}
