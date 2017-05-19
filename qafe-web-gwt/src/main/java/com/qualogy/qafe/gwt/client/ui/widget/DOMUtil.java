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
package com.qualogy.qafe.gwt.client.ui.widget;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.Event;

/**
 * A non-instantiable class with static DOM utility methods.
 * 
 * @author epstein
 */
public abstract class DOMUtil {

	/**
	 * Get the XY co-ordinates of an event. Similar to DOM.eventGetClientX() and
	 * DOM.eventGetClientY() except it corrects of scrolling of the browser
	 * window.
	 * 
	 * @param e
	 *            a mouse event
	 * @return A JavaScript int array (int[]) wrapped in an opaque
	 *         JavaScriptObject handle.
	 */
	public static native JavaScriptObject eventGetXYPosition(Event e) /*-{
	 var scrOfX = 0;
	 var scrOfY = 0;
	 if ( typeof( $wnd.pageXOffset ) == 'number' || typeof(
	 $wnd.pageYOffset ) == 'number' ) {
	 //Netscape compliant
	 scrOfX = $wnd.pageXOffset;
	 scrOfY = $doc.documentElement.scrollTop;
	 } else if ( $doc.body && ( $doc.body.scrollLeft ||
	 $doc.body.scrollTop ) ) {
	 //DOM compliant
	 scrOfX = $doc.body.scrollLeft;
	 scrOfY = $doc.body.scrollTop;
	 } else if ( $doc.documentElement && ( $doc.documentElement.scrollLeft
	 || $doc.documentElement.scrollTop ) ) {
	 //IE6 standards compliant mode
	 scrOfX = $doc.documentElement.scrollLeft;
	 scrOfY = $wnd.pageYOffset;
	 }
	 return [scrOfX + e.clientX, scrOfY + e.clientY];
	 }-*/;

	public static native int getIntAtIndex(JavaScriptObject intArray, int idx)
	/*-{
	 
	 return intArray[idx];
	 }-*/;

	public static int eventGetXPosition(Event e) {
		return getIntAtIndex(eventGetXYPosition(e), 0);
	};

	public static int eventGetYPosition(Event e) {
		return getIntAtIndex(eventGetXYPosition(e), 1);
	};
}
