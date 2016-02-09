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
package com.qualogy.qafe.gwt.client.util;


import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.UIObject;

public class Print {

    public static native void it() /*-{
        $wnd.print();
    }-*/;

    public static native void it(String html) /*-{
        var frame = $doc.getElementById('__printingFrame');
        if (!frame) {
            $wnd.alert("Error: Can't find printing frame.");
            return;
        }
        frame = frame.contentWindow;
        var doc = frame.document;
        doc.open();
        doc.write(html);
        doc.close();
        frame.focus();
        frame.print();
    }-*/;

    public static void it(UIObject obj) {
        it("", obj.getElement().toString());
    }

    public static void it(Element element) {
        it("", element.toString());
    }

    public static void it(String style, String it) {
        it("<html><header>"+style+"</header><body>"+it+"</body></html>");
    } 
    public static void it(String style, UIObject obj) {
        it(style, obj.getElement().toString());
    }

    public static void it(String style, Element element) {
        it(style, element.toString());
    }

} // end of class Print
