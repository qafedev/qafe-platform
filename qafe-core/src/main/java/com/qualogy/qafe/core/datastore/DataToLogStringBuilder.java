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
package com.qualogy.qafe.core.datastore;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class DataToLogStringBuilder{

	private DataToLogStringBuilder() {
	}

	public static String build(Map elements, StringBuilder builder){
		
		if(elements!=null){
			for (Iterator iter = elements.keySet().iterator(); iter.hasNext();) {
				String key = (String) iter.next();
				keyStrValueToString(0, key, elements.get(key),builder);
			}
		}
		
		return builder.toString();
	}
	
	private static void keyStrValueToString( int level, String key, Object value, StringBuilder builder){
		if(value instanceof Map){
			builder.append( key + " (map)\n");;
			mapValueToString(level+1, (Map)value, builder);
		}else if(value instanceof List){
			builder.append( key + " (list(" + ((List)value).size() + "))\n");
			listValueToString(level+1, (List)value, builder);
		}else{
			builder.append(key + "=["  + value + "]" + ((value!=null)?" (" + value.getClass().getName() + ")":"") + "\n");
		}
		//return builder.toString();
	}
	
	private static void listValueToString(int level, List list, StringBuilder builder){
		int i = 0;
		for (Iterator iter = list.iterator(); iter.hasNext();i++) {
			Object element = iter.next();
			keyStrValueToString(level, generateLevelPrefix(level) + i, element,builder);
		}
		//return builder.toString();
	}
	
	private static void mapValueToString( int level, Map map, StringBuilder builder){
		int i = 0;
		for (Iterator iter = map.keySet().iterator(); iter.hasNext();i++) {
			String key = (String)iter.next();
			keyStrValueToString( level, generateLevelPrefix(level) + key, map.get(key),builder);
		}
		//return builder.toString();
	}
	
	private static String generateLevelPrefix(int level){
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < level; i++) {
			builder.append( "  ");
		}
		builder.append("|-");
		return builder.toString();
	}
}