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
package com.qualogy.qafe.presentation.handler;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.LogFactory;

import com.qualogy.qafe.bind.business.action.BusinessActionRef;
import com.qualogy.qafe.bind.core.application.ApplicationContext;
import com.qualogy.qafe.bind.core.statement.IfStatement;
import com.qualogy.qafe.bind.core.statement.Iteration;
import com.qualogy.qafe.bind.core.statement.SwitchStatement;
import com.qualogy.qafe.bind.presentation.event.Event;
import com.qualogy.qafe.bind.presentation.event.EventItem;
import com.qualogy.qafe.bind.presentation.event.function.BuiltInFunction;
import com.qualogy.qafe.bind.presentation.event.function.EventRef;
import com.qualogy.qafe.bind.presentation.event.function.Focus;
import com.qualogy.qafe.bind.presentation.event.function.LocalDelete;
import com.qualogy.qafe.bind.presentation.event.function.LocalStore;
import com.qualogy.qafe.bind.presentation.event.function.LogFunction;
import com.qualogy.qafe.bind.presentation.event.function.OpenWindow;
import com.qualogy.qafe.bind.presentation.event.function.Return;
import com.qualogy.qafe.bind.presentation.event.function.SetProperty;
import com.qualogy.qafe.bind.presentation.event.function.SetValue;
import com.qualogy.qafe.bind.presentation.event.function.dialog.GenericDialog;
import com.qualogy.qafe.core.datastore.DataIdentifier;
import com.qualogy.qafe.core.errorhandling.ExternalException;
import com.qualogy.qafe.core.framework.presentation.EventData;
import com.qualogy.qafe.presentation.EventHandlerImpl;
import com.qualogy.qafe.presentation.handler.executors.AbstractEventItemExecute;
import com.qualogy.qafe.presentation.handler.executors.BusinessActionRefExecute;
import com.qualogy.qafe.presentation.handler.executors.EventRefExecute;
import com.qualogy.qafe.presentation.handler.executors.FocusExecute;
import com.qualogy.qafe.presentation.handler.executors.GenericDialogExecute;
import com.qualogy.qafe.presentation.handler.executors.IfStatementExecute;
import com.qualogy.qafe.presentation.handler.executors.IterationExecute;
import com.qualogy.qafe.presentation.handler.executors.LocalDeleteExecute;
import com.qualogy.qafe.presentation.handler.executors.LocalStoreExecute;
import com.qualogy.qafe.presentation.handler.executors.LogFunctionExecute;
import com.qualogy.qafe.presentation.handler.executors.OpenWindowExecute;
import com.qualogy.qafe.presentation.handler.executors.ReturnBuiltInException;
import com.qualogy.qafe.presentation.handler.executors.ReturnExecute;
import com.qualogy.qafe.presentation.handler.executors.SetPropertyExecute;
import com.qualogy.qafe.presentation.handler.executors.SetValueExecute;
import com.qualogy.qafe.presentation.handler.executors.SwitchStatementExecute;
import com.qualogy.qafe.util.ExceptionHelper;


public class EventItemExecutor {
	private static EventItemExecutor singleton = null;
	
	private  final Map<Class<?>, Class<?>> EXECUTOR_MAP = new HashMap<Class<?>, Class<?>>();
	
	private EventItemExecutor(){
		EXECUTOR_MAP.put(EventRef.class, EventRefExecute.class);			
		EXECUTOR_MAP.put(GenericDialog.class, GenericDialogExecute.class);
		EXECUTOR_MAP.put(BusinessActionRef.class, BusinessActionRefExecute.class);
		EXECUTOR_MAP.put(LocalStore.class, LocalStoreExecute.class);
	    EXECUTOR_MAP.put(LocalDelete.class, LocalDeleteExecute.class);
		EXECUTOR_MAP.put(IfStatement.class, IfStatementExecute.class);
		EXECUTOR_MAP.put(SwitchStatement.class, SwitchStatementExecute.class);
		EXECUTOR_MAP.put(Iteration.class, IterationExecute.class);		
		EXECUTOR_MAP.put(SetValue.class, SetValueExecute.class);
		EXECUTOR_MAP.put(SetProperty.class, SetPropertyExecute.class);
		EXECUTOR_MAP.put(OpenWindow.class, OpenWindowExecute.class);
		EXECUTOR_MAP.put(LogFunction.class, LogFunctionExecute.class);
		EXECUTOR_MAP.put(Return.class, ReturnExecute.class);
		EXECUTOR_MAP.put(Focus.class, FocusExecute.class);
	};
	
	public static EventItemExecutor getInstance(){
		if (singleton==null){
			singleton = new EventItemExecutor();
		} 
		return singleton;
	}

	public boolean execute(EventItem eventItem,ApplicationContext context, Event event, EventData eventData,Collection<BuiltInFunction> listToExecute,EventHandlerImpl eventHandler,DataIdentifier dataId) throws ExternalException{
		boolean stopProcessing = false;
		if (eventItem != null) {
			try {
				ExecuteEventItem executeEventItem = createExecuteEventItem(eventItem);
				if (executeEventItem != null) {	
					stopProcessing = executeEventItem.execute(eventItem,context,event, eventData,listToExecute,eventHandler,dataId);
				} else if (eventItem instanceof BuiltInFunction) {
					listToExecute.add((BuiltInFunction)eventItem);
				} else {
					LogFactory.getLog(getClass().getName()).info("Unable to find renderer for class " + eventItem.getClass().getName());
				}
			} catch (ReturnBuiltInException e) {
				throw e;
			} catch (ExternalException e) {
				throw e;
			} catch (Exception e) {
				LogFactory.getLog(getClass().getName()).info("AbstractComponentRenderer:renderChildComponent\n" + ExceptionHelper.printStackTrace(e));
				throw new ExternalException(e.getMessage(), e);
			}
		}
		return stopProcessing;
	}
	
	public AbstractEventItemExecute getExecutor(EventItem eventItem) {
		if (eventItem != null) {
			try {
				ExecuteEventItem executeEventItem = createExecuteEventItem(eventItem);
				if (executeEventItem instanceof AbstractEventItemExecute) {
					return (AbstractEventItemExecute)executeEventItem;	
				}	
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		return null;
	}
	
	private ExecuteEventItem createExecuteEventItem(EventItem eventItem) throws Exception {
		try {
			Class clazz = EXECUTOR_MAP.get(eventItem.getClass());
			if (clazz != null) {
				return (ExecuteEventItem)clazz.newInstance();	
			}			
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return null;
	}	
}
