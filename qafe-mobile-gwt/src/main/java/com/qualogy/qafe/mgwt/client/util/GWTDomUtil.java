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
package com.qualogy.qafe.mgwt.client.util;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;

/**
 * Utilities class for adding functionality to DOM before they are integrated
 * into Google Web Toolkit.
 */

public class GWTDomUtil {

	/**
	 * Helper class to add a css element to the head of the document.
	 * 
	 * @param cssUrl
	 *            The url of the css file.
	 */
	public static void addCSS(String cssUrl) {
		Element head = getElementByTagName("head");
		Element cssLink = DOM.createElement("link");
		setAttribute(cssLink, "type", "text/css");
		setAttribute(cssLink, "rel", "stylesheet");
		setAttribute(cssLink, "href", cssUrl);

		DOM.appendChild(head, cssLink);
	}

	/**
	 * @param el
	 *            The element to modify the attribute of.
	 * @param key
	 *            The key, or name of the attribute.
	 * @param val
	 *            The value of the attribute.
	 */
	public native static void setAttribute(Element el, String key, String val) /*-{
	 el.setAttribute(key, val);
	 }-*/;

	/**
	 * Gets an element by its tag name; handy for single elements like HTML,
	 * HEAD, BODY.
	 * 
	 * @param tagName
	 *            The name of the tag.
	 * @return The element with that tag name.
	 */
	public native static Element getElementByTagName(String tagName) /*-{
	 var elem = $doc.getElementsByTagName(tagName);
	 return elem ? elem[0] : null;
	 }-*/;

}
