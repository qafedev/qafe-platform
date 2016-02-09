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
package com.qualogy.qafe.bind.business.transaction;

import java.io.Serializable;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.jibx.runtime.IUnmarshallingContext;

import com.qualogy.qafe.bind.PostProcessing;

public class GlobalTransactions implements PostProcessing, Serializable{
	
	private static final long serialVersionUID = -8291272797870417537L;
	
	protected List<DefaultBusinessActionTransactionBehaviour> defaultBusinessActionTransactionBehaviours;
	
	/**
	 * 
	 * @param businessActionId
	 */
	public TransactionBehaviour getBehaviourForBA(String businessActionId){
		
		TransactionBehaviour behaviourForBA = null;
		if(defaultBusinessActionTransactionBehaviours!=null){
			for (Iterator iter = defaultBusinessActionTransactionBehaviours.iterator(); iter.hasNext() && behaviourForBA==null;) {
				DefaultBusinessActionTransactionBehaviour bATBh = (DefaultBusinessActionTransactionBehaviour) iter.next();
				if(bATBh.getIdPattern()==null)
					throw new IllegalArgumentException("cannot have null pattern on business action, use pattern='*'");
						
				if(bATBh.getIdPattern().matches(businessActionId)){
					behaviourForBA = bATBh;
					break;
				}
			}
		}
		return behaviourForBA;
	}

	
	public void postset(IUnmarshallingContext context){
		performPostProcessing();
	}
	public void performPostProcessing() {
		Collections.sort(defaultBusinessActionTransactionBehaviours);		
	}
}
