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
package com.qualogy.qafe.business.test;

import java.util.Iterator;
import java.util.List;

import com.qualogy.qafe.bind.business.action.BusinessAction;
/**
 * convinience class to lookup an businessaction
 * @author mvanderwurff
 *
 */
public class BusinessActionLookup {

	/**
	 * method to retrieve a businessaction for a given id from a given
	 * businessactionslist
	 * 
	 * @param actions
	 * @param baid
	 * @return
	 */
	public static BusinessAction getBusinessActionForId(List actions,
			String baid) {
		BusinessAction result = null;
		if(actions!=null){
			for (Iterator iter = actions.iterator(); iter.hasNext();) {
				BusinessAction ba = (BusinessAction) iter.next();
				if (ba.getId().equals(baid)) {
					result = ba;
					break;
				}
			}
		}
		return result;
	}

}
