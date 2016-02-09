/**
 * Copyright 2008-2016 Qualogy Solutions B.V.
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
package com.qualogy.qafe.core.pattern;

import java.util.ArrayList;
import java.util.List;

import com.qualogy.qafe.bind.core.pattern.IdPattern;
import com.qualogy.qafe.core.id.Identifiable;

public class PatternMatcher{
	
	public static List<Identifiable> filter(List<Identifiable> identifiables, IdPattern idPattern){
		List<Identifiable> result = new ArrayList<Identifiable>();
		for (Identifiable identifiable : identifiables) {
			if(idPattern.matches(identifiable.getStringValueOfId())){
				result.add(identifiable);
			}
		}
		return result;
	}
}
