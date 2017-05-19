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
package com.qualogy.qafe.bind.util;

import java.util.Iterator;
import java.util.List;

import com.qualogy.qafe.bind.PostProcessing;

public class PostProcessor {
	
	public final static String BIND_PACKAGE_PRECEDER = "com.qualogy.qafe.bind";
	
	/**
	 * method which performs postprocessing on the given
	 * object and its members recursivly (iow the members of the members, etc.)
	 * @param object
	 * @throws IllegalArgumentException when object is null
	 */
	public static void process(Object object){
		if (object == null) 
        	throw new IllegalArgumentException("obj is null");
        
		List objects = InterfaceScanner.scan(object, PostProcessing.class, BIND_PACKAGE_PRECEDER);
		postProcess(objects);
	}
	
	/**
	 * @pre assuming objects are not null and implement postprocessing, perform postprocessing
	 * @param object
	 */
	private static void postProcess(List objects){
		for (Iterator iter = objects.iterator(); iter.hasNext();) {
			PostProcessing pp = (PostProcessing) iter.next();
			pp.performPostProcessing();
		}
	}
}
