package com.qualogy.qafe.business;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.qualogy.qafe.bind.business.action.BusinessAction;
import com.qualogy.qafe.bind.business.action.BusinessActionRef;
import com.qualogy.qafe.bind.core.application.ApplicationContext;
import com.qualogy.qafe.bind.core.statement.ControlStatement;
import com.qualogy.qafe.bind.integration.service.ServiceRef;
import com.qualogy.qafe.bind.item.Item;
import com.qualogy.qafe.business.action.ItemExecuter;
import com.qualogy.qafe.core.datastore.DataIdentifier;
import com.qualogy.qafe.core.datastore.DataStore;
import com.qualogy.qafe.core.errorhandling.ExternalException;
import com.qualogy.qafe.core.framework.business.UnableToProcessException;

public aspect BusinessManagerStackTraceProducer {
	
	public final static Log logger = LogFactory.getLog(BusinessActionManagerImpl.class);
	
	pointcut businessActionManagerManage(final ApplicationContext context, final DataIdentifier dataId, final BusinessAction action, final boolean isNestedAction):
		execution(* BusinessActionManagerImpl.doManage(ApplicationContext, DataIdentifier, BusinessAction, boolean) throws ExternalException) && args(context, dataId, action, isNestedAction);
	
	pointcut itemExecuterExecute(final ApplicationContext context, final DataIdentifier dataId, final Item item):
		execution(* ItemExecuter.execute(ApplicationContext, DataIdentifier, Item) throws UnableToProcessException) && args(context, dataId, item);
	
	after(final ApplicationContext context, final DataIdentifier dataId, final BusinessAction action, final boolean isNestedAction) throwing (Throwable throwable) : businessActionManagerManage(context, dataId, action, isNestedAction){
		String header = "Stack, error occured:\n" +
				"BusinessAction: [" + action!=null?action.getId():null + "] \n" +
				"Exception: " + throwable.getMessage() + "\n" + 
				DataStore.toLogString(dataId);
		StackRecorder.addHeader(dataId, header);
		logger.error(StackRecorder.toLogString(dataId));
	}
	
	after(final ApplicationContext context, final DataIdentifier dataId, final BusinessAction action, final boolean isNestedAction) returning () : businessActionManagerManage(context, dataId, action, isNestedAction){
		String message = "Succesfully performed: " + action.getId() + "\n";
		if(logger.isDebugEnabled())
			message += DataStore.toLogString(dataId);;
		
		logger.info(message);
	}
	
	before(final ApplicationContext context, final DataIdentifier dataId, final Item item) : itemExecuterExecute(context, dataId, item){
		String entry = null;
		if(item instanceof BusinessActionRef){
			entry = "Businessaction > " + ((((BusinessActionRef)item).getRef()!=null) ? ((BusinessActionRef)item).getRef().getId() : null);
		}else if(item instanceof ServiceRef){
			entry = "Service > " + (((ServiceRef)item).getRef()!=null ? ((ServiceRef)item).getRef().getId() : null);
		}else if(item instanceof ControlStatement){
			entry = "";
		}
		StackRecorder.add(dataId, entry);
	}
}
