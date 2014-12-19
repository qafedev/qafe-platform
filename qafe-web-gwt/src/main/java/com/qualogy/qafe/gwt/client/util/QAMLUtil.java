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
		
	public static final String DEFAULT_UNIT = Unit.PX.getType(); 
	
	/**
	 * Check to see if a string contains a unit (PX, %, EM, etc)
	 * 
	 * @param value the string to check against
	 * @return true if the string contains a supported unit identifier
	 */
	public static boolean containsUnitIdentifier(String value) {
		if (value == null) {
			return false;
		}
		
		for (Unit unit : Unit.values()) {
			if (value.toLowerCase().contains(unit.getType())) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Check if the string contains a positive number
	 * 
	 * @TODO give a more sensible name
	 * @param value the string to parse for positive number
	 * @return true if the string contains a positive number
	 */
	public static boolean isPositiveDimension(String value) {
		// removes all non number and . characters from the string
		String valueAsNumber = value.replaceAll("\\D+","");
		int valueToInt = Integer.parseInt(valueAsNumber);
		if (valueToInt > 0) {
			return true;
		}
		
		return false;
	}
}
