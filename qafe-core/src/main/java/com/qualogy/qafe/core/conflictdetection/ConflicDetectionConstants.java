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
package com.qualogy.qafe.core.conflictdetection;

import java.util.HashSet;
import java.util.Set;


public class ConflicDetectionConstants {

	public static final String QAFE_CHECKSUM = "QAFE_CHECKSUM";
	public static final String RNUM 		 = "RNUM";
	
	public static final Set<String> CONSTANTS = new HashSet<String>();
	
	static {
		CONSTANTS.add(QAFE_CHECKSUM);
		CONSTANTS.add(RNUM);
	}
}
