package com.qualogy.qafe.business.transaction;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import com.qualogy.qafe.bind.business.action.BusinessAction;
import com.qualogy.qafe.bind.business.transaction.TransactionBehaviour;
import com.qualogy.qafe.bind.core.application.ApplicationContext;
import com.qualogy.qafe.business.BusinessActionManagerImpl;
import com.qualogy.qafe.core.datastore.DataIdentifier;
import com.qualogy.qafe.core.errorhandling.ExternalException;


public aspect Transactional {
	
	private static final Log logger = LogFactory.getLog(Transactional.class);
	/**
	 * manage method
	 */
    void around(final ApplicationContext context, final DataIdentifier dataId, final BusinessAction action, final boolean isNestedAction) throws ExternalException, TransActionRolledBackException: 
    	execution(* BusinessActionManagerImpl.doManage(ApplicationContext, DataIdentifier, BusinessAction, boolean) throws ExternalException) && args(context, dataId, action, isNestedAction) {
    	
    	try{
    		PlatformTransactionManager transactionManager = TransactionManagerBuilder.build(context, action);
    		
    		if(transactionManager==null){
    			proceed(context, dataId, action, isNestedAction);
    		}else{
    			TransactionBehaviour behaviour = action.getTransactionBehaviour();
    			TransactionTemplate transactionTemplate = TransactionTemplateBuilder.build(transactionManager, behaviour); 
    			
    			transactionTemplate.execute(new TransactionCallbackWithoutResult() {
    	            protected void doInTransactionWithoutResult(TransactionStatus status) {
    	            	try{
    	            		logger.info("proceeding with action["+action.getId()+"] within transaction");
    	            		proceed(context, dataId, action, isNestedAction);
    	            	}catch(ExternalException e){
    	            		throw new TransActionRolledBackException(e);
    	            	}catch(RuntimeException e){
    	            		throw new TransActionRolledBackException(e);
    	            	}
					}
    	        });
    		}
    	}finally{
//    		if(logger.isDebugEnabled())
//    			logger.debug("after transaction -> " + DataStore.toLogString(id));
    	}
    	//TODO: after rollback datastore?
    }
}
