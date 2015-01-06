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
package com.qualogy.qafe.util;

import org.apache.commons.lang.StringUtils;

public class StyleDomUtil {

	private StyleDomUtil(){};
	
	/**
	 * Convert for example background-color to backgroundColor
	 * @param style
	 * @return
	 */
	public static String initCapitalize(String style){
		String newValue ="";
		if(style!=null){
			String[] parts = StringUtils.split(style,'-');
			if (parts.length>1){
				for(int j=0;j<parts.length;j++){
					if (j>0){
						if (parts[j].length()>0){
							String capString = parts[j].substring(0,1);
							capString = capString.toUpperCase();
							capString += parts[j].substring(1);
							newValue+=capString;
						}
					} else {
						newValue+=parts[j];
					}
				}
			} else {
				newValue= style;
			}
		}
		return newValue;
	}
	/**
	 * convert for example backgrounColor to background-color
	 * @param style
	 * @return
	 */
	public static String deCamelCaseStyle(String style){
		
		String property = "";
		for (int j = 0; j < style.length(); j++) {
			char character = style.charAt(j);
			if (Character.isUpperCase(character)) {
				property += "-" + Character.toLowerCase(character);
			} else {
				property += character;
			}
		}
		return property;
	}
}
