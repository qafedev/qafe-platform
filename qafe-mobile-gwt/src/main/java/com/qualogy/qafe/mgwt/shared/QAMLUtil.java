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
package com.qualogy.qafe.mgwt.shared;

import java.util.Date;
import java.util.List;

public class QAMLUtil {

	public static String toString(Object value, String defaultValue) {
		if (value instanceof String) {
			return (String)value;	
		}
		if (value != null) {
			return value.toString();
		}
		return defaultValue;
	}

	public static Boolean toBoolean(Object value, Boolean defaultValue) {
		return toBoolean(value, null, null, defaultValue);
	}

	public static Boolean toBoolean(Object value, String value4True, String value4False, Boolean defaultValue) {
		if (value instanceof Boolean) {
			return (Boolean)value;	
		}
		if (value instanceof String) {
			if (value.equals(value4True)) {
				return true;
			}
			if (value.equals(value4False)) {
				return false;
			}
			return Boolean.valueOf((String)value);
		}
		return defaultValue;
	}
	
	public static Integer toInteger(Object value) {
		return toInteger(value, null);
	}
	
	public static Integer toInteger(Object value, Integer defaultValue) {
		if (value instanceof Integer) {
			return (Integer)value;
		}
		if (value instanceof Number) {
			return ((Number)value).intValue();
		}
		try {
			return Integer.parseInt(toString(value, null));
		} catch (Exception e) {
		}
		return defaultValue;
	}
	
	public static Double toDouble(Object value) {
		return toDouble(value, null);
	}
	
	public static Double toDouble(Object value, Double defaultValue) {
		if (value instanceof Double) {
			return (Double)value;
		}
		if (value instanceof Number) {
			return ((Number)value).doubleValue();
		}
		try {
			return Double.parseDouble(toString(value, null));
		} catch (Exception e) {
		}
		return defaultValue;
	}
	
	public static Date toDate(Object value) {
		return toDate(value, null);
	}
	
	public static Date toDate(Object value, Date defaultValue) {
		if (value instanceof Date) {
			return (Date)value;	
		} 
		try {
			return new Date(toString(value, null));
		} catch (Exception e) {
		}
		return defaultValue;
	}
	
	public static boolean isEmpty(String value) {
		return (value == null) || (value.length() == 0);
	}
	
	public static boolean isEmpty(List<?> value) {
		return (value == null) || (value.size() == 0);
	}
	
	public static boolean isNumber(String value) {
		try {
			Double.parseDouble(value);
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	
	public static String camelize(String value) {
		if ((value == null) || (value.length() == 0)) {
			return value;
		}
		if (value.indexOf("-") == -1) {
			return value;
		}
		String[] tokens = value.split("-");
		StringBuffer newValue = new StringBuffer(tokens[0].toLowerCase());
		for (int i=1; i<tokens.length; i++) {
			String token = tokens[i];
			newValue.append(token.substring(0, 1).toUpperCase());
			newValue.append(token.substring(1));
		}
		return newValue.toString();
	}
}