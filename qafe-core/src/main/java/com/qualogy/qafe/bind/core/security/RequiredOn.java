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
package com.qualogy.qafe.bind.core.security;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.qualogy.qafe.bind.core.pattern.IdPattern;

public class RequiredOn {
	protected List<IdPattern> idPatterns;

	public List<IdPattern> getIdPatterns() {
		return idPatterns;
	}
	public void merge(RequiredOn otherRequiredOn){
		if(otherRequiredOn!=null){
			if(this.idPatterns==null){
				this.idPatterns = otherRequiredOn.idPatterns;
			}else if(CollectionUtils.isNotEmpty(otherRequiredOn.idPatterns)){
				this.idPatterns.addAll(otherRequiredOn.idPatterns);
			}
		}
	}
	public RequiredOn clone() {
		RequiredOn newRequiredOn = new RequiredOn();
		if (idPatterns != null) {
			newRequiredOn.idPatterns = new ArrayList<IdPattern>();
			newRequiredOn.idPatterns.addAll(idPatterns);
		}
		return newRequiredOn;
	}
}
