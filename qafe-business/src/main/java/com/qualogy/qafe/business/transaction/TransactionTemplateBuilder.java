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
package com.qualogy.qafe.business.transaction;

import java.util.HashMap;
import java.util.Map;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

import com.qualogy.qafe.bind.business.transaction.TransactionBehaviour;



public class TransactionTemplateBuilder{
	
	private static Map<String, Integer> PROPAGATION_MAPPING;
	private static Map<String, Integer> ISOLATION_MAPPING;
	static{
		PROPAGATION_MAPPING = new HashMap<String, Integer>();
		PROPAGATION_MAPPING.put(TransactionBehaviour.PROPAGATION_MANDATORY, new Integer(DefaultTransactionDefinition.PROPAGATION_MANDATORY));
		PROPAGATION_MAPPING.put(TransactionBehaviour.PROPAGATION_NESTED, new Integer(DefaultTransactionDefinition.PROPAGATION_NESTED));
		PROPAGATION_MAPPING.put(TransactionBehaviour.PROPAGATION_NEVER, new Integer(DefaultTransactionDefinition.PROPAGATION_NEVER));
		PROPAGATION_MAPPING.put(TransactionBehaviour.PROPAGATION_NOT_SUPPORTED, new Integer(DefaultTransactionDefinition.PROPAGATION_NOT_SUPPORTED));
		PROPAGATION_MAPPING.put(TransactionBehaviour.PROPAGATION_REQUIRED, new Integer(DefaultTransactionDefinition.PROPAGATION_REQUIRED));
		PROPAGATION_MAPPING.put(TransactionBehaviour.PROPAGATION_REQUIRES_NEW, new Integer(DefaultTransactionDefinition.PROPAGATION_REQUIRES_NEW));
		PROPAGATION_MAPPING.put(TransactionBehaviour.PROPAGATION_SUPPORTS, new Integer(DefaultTransactionDefinition.PROPAGATION_SUPPORTS));
		
		ISOLATION_MAPPING = new HashMap<String, Integer>();
		ISOLATION_MAPPING.put(TransactionBehaviour.ISOLATION_DEFAULT, new Integer(DefaultTransactionDefinition.ISOLATION_DEFAULT));
		ISOLATION_MAPPING.put(TransactionBehaviour.ISOLATION_READ_COMMITTED, new Integer(DefaultTransactionDefinition.ISOLATION_READ_COMMITTED));
		ISOLATION_MAPPING.put(TransactionBehaviour.ISOLATION_READ_UNCOMMITTED, new Integer(DefaultTransactionDefinition.ISOLATION_READ_UNCOMMITTED));
		ISOLATION_MAPPING.put(TransactionBehaviour.ISOLATION_REPEATABLE_READ, new Integer(DefaultTransactionDefinition.ISOLATION_REPEATABLE_READ));
		ISOLATION_MAPPING.put(TransactionBehaviour.ISOLATION_SERIALIZABLE, new Integer(DefaultTransactionDefinition.ISOLATION_SERIALIZABLE));
	}
	
	public static TransactionTemplate build(PlatformTransactionManager transactionManager, TransactionBehaviour behaviour){
		TransactionTemplate template = new TransactionTemplate(transactionManager);
		template.setIsolationLevel(((Integer)ISOLATION_MAPPING.get(behaviour.getIsolation())).intValue());
		template.setPropagationBehavior(((Integer)PROPAGATION_MAPPING.get(behaviour.getPropagation())).intValue());
		template.setTimeout(behaviour.getTimeout());
		return template;
	}
}
