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
package com.qualogy.qafe.gwt.client.vo.ui;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * The BundleGVO describes the client-side messages bundle which contains
 * localized text components.
 * 
 * @author wvandenbosch
 * 
 */
public class BundleGVO implements IsSerializable {

	public final static String DEFAULT_BUNDLE_ID = "";
	public final static String DEFAULT_LOCALE_KEY = "";

	private Map<String, Map<String, String>> localizedMessages = new HashMap<String, Map<String, String>>();

	/**
	 * Gets a localized message value based on locale and message id.
	 * 
	 * @param locale
	 *            the language value as String.
	 * @param messageKey
	 *            the id of the text component.
	 * @return a localized message, or null if no such message id is defined as
	 *         a text component.
	 */
	public String getLocalizedMessage(String locale, String messageKey) {
		Map<String, String> messageMap = localizedMessages.get(locale);
		if (messageMap == null) {
			messageMap = localizedMessages.get(DEFAULT_LOCALE_KEY);
		}
		if (messageMap != null) {
			return messageMap.get(messageKey);
		}
		return null;
	}

	/**
	 * Adds a localized message to the messages map.
	 * 
	 * @param locale
	 *            the locale to store the message under.
	 * @param messageKey
	 *            the id of the text component.
	 * @param value
	 *            the text value of the text component.
	 */
	public void addLocalizedMessage(String locale, String messageKey,
			String value) {
		if (messageKey == null) {
			return;
		}
		if (!localizedMessages.containsKey(locale)) {
			localizedMessages.put(locale, new HashMap<String, String>());
		}
		Map<String, String> messageMap = localizedMessages.get(locale);
		messageMap.put(messageKey, value);
	}
}
