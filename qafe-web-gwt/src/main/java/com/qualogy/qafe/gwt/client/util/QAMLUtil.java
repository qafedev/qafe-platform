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
package com.qualogy.qafe.gwt.client.util;

import com.google.gwt.dom.client.Style.Unit;

public class QAMLUtil {

	/**
	 * IMPORTANT: this method must be an exact copy of the one used in the qafe-business project
	 */
	public static long calculateChecksum(String key, Object value) {
		long checksum = key.hashCode();
		if (value instanceof Number) {
			checksum += value.toString().hashCode();
		} else if (value != null) {
			checksum += value.hashCode();
		}
		return checksum;
	}
	
	public static String unitPX(String value) {
		if ((value != null) && value.matches("\\d+")) {
			value += Unit.PX;
		}
		return value;
	}
}
