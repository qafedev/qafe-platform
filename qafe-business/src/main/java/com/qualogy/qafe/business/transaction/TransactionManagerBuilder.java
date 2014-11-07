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

import java.util.Collection;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.jta.JtaTransactionManager;

import com.qualogy.qafe.bind.business.action.BusinessAction;
import com.qualogy.qafe.bind.business.transaction.TransactionBehaviour;
import com.qualogy.qafe.bind.core.application.ApplicationContext;
import com.qualogy.qafe.business.UnsupportedFeatureException;
import com.qualogy.qafe.business.resource.ResourceLookup;

public class TransactionManagerBuilder{
	
	private static final Logger logger = Logger.getLogger(TransactionManagerBuilder.class.getName());
	
	/**
	 * Method to setup a transactionmanager based upon the given action. First the behaviour
	 * transaction attribute is evaluated to what type of txmanager is needed, than after in case of a 
	 * local transaction strategy the datasources are retrieved linked to the action. 
	 * 
	 * - In case of multiple dbs and local strategy an exception will be raised since this behaviour is unsupported at the moment. 
	 * - When no database is specified when having a local strategy no txmanager is created, a warning will be raised
	 * - When behaviour is null or set to none a warning is reported about having no txmanager
	 *  
	 * @param action
	 * @return
	 */
	public static PlatformTransactionManager build(ApplicationContext context, BusinessAction action){
		PlatformTransactionManager txManager = null;
		TransactionBehaviour behaviour = action.getTransactionBehaviour();
		if(behaviour==null || !behaviour.isManaged()){
			//TODO: post warning 
    		logger.warning("Transactional behaviour not set for ["+ action.getId() +"], proceeding processing of this action without transaction management");
		}else if(behaviour.useLocal()){
			Collection dbs = ResourceLookup.getDatasourcesRelatedTo(context.getId(), action);
			if(dbs.size()>1){
				throw new UnsupportedFeatureException("using local as transactionstrategy and having more than one (different) database linked to the services related to this action is unsupported");
			}
			
			if(dbs.size()==0){
				logger.warning("transactional behaviour was set to local but no database was found related to this action and services");
			}else{
				txManager = new DataSourceTransactionManager((DataSource)dbs.iterator().next());
			}
		}else if(behaviour.useGlobal()){
			txManager = new JtaTransactionManager();
		}
		return txManager;
	}
}
