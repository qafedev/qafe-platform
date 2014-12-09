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
package com.qualogy.qafe.util;

import org.apache.commons.lang.StringUtils;

public class Assert {
	public static void notNull(Object o, String errorMessage){
		if(o==null)
			throw new IllegalArgumentException(errorMessage);
	}

	public static void hasLength(String className, String errorMessage) {
		if(StringUtils.isEmpty(className))
			throw new IllegalArgumentException(errorMessage);
	}

	public static void notEmpty(Object[] objectArr, String errorMessage) {
		if(objectArr==null || objectArr.length==0)
			throw new IllegalArgumentException(errorMessage);
		
	}
}
