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
package com.qualogy.qafe.bind.business.transaction;

import com.qualogy.qafe.bind.core.pattern.ActionIdPattern;

public class DefaultBusinessActionTransactionBehaviour extends TransactionBehaviour implements Comparable<DefaultBusinessActionTransactionBehaviour>{

	private static final long serialVersionUID = -1408113974302051964L;

	protected ActionIdPattern idpattern;

	public ActionIdPattern getIdPattern() {
		return idpattern;
	}

	public int compareTo(DefaultBusinessActionTransactionBehaviour otherBehaviour) {
		if(otherBehaviour==null || otherBehaviour.getIdPattern()==null){
			return -1;
		}
		if(this.getIdPattern()==null){
			return 1;
		}

		return this.getIdPattern().compareTo(otherBehaviour.getIdPattern());
	}
}
