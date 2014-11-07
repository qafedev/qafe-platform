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
package com.qualogy.qafe.presentation.handler.executors;

import java.util.Collection;

import com.qualogy.qafe.bind.commons.type.Parameter;
import com.qualogy.qafe.bind.core.application.ApplicationContext;
import com.qualogy.qafe.bind.presentation.event.Event;
import com.qualogy.qafe.bind.presentation.event.EventItem;
import com.qualogy.qafe.bind.presentation.event.function.BuiltInFunction;
import com.qualogy.qafe.bind.presentation.event.function.dialog.GenericDialog;
import com.qualogy.qafe.core.datastore.DataIdentifier;
import com.qualogy.qafe.core.framework.presentation.EventData;
import com.qualogy.qafe.presentation.EventHandlerImpl;
import com.qualogy.qafe.presentation.handler.ExecuteEventItem;

public class GenericDialogExecute extends AbstractEventItemExecute implements ExecuteEventItem {

	public boolean execute(EventItem eventItem, ApplicationContext context,
			Event event, EventData eventData, Collection<BuiltInFunction> listToExecute,
			EventHandlerImpl eventHandler, DataIdentifier dataId) {
		GenericDialog origDialog =(GenericDialog) eventItem;
		
		Parameter origMessage = origDialog.getMessage();
		Parameter origTitle = origDialog.getTitle();
		if(origTitle==null){
			throw new IllegalArgumentException("At least one of the title fields is mandatory");
		}
		// do not change the object from the applcationmapping.
		// Make a copy instead, filled in with the correct data
		GenericDialog copyDialog = new GenericDialog();
		
		Parameter copyTitle = null;
		try {
			copyTitle = (Parameter) origTitle.clone();
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException("Cloneable interface on Parameter has changed", e);
		}
		copyDialog.setTitle(copyTitle);
		copyDialog.setTitleData(getValue(context, dataId, copyTitle, eventData));
		copyDialog.setType(origDialog.getType());
		copyDialog.setWidth(origDialog.getWidth());
		copyDialog.setHeight(origDialog.getHeight());
		
		listToExecute.add(copyDialog);
		
		Object messageData = null; 
		if(origMessage!=null){
			Parameter copyMessage = null;
			try {
				copyMessage = (Parameter) origMessage.clone();
			} catch (CloneNotSupportedException e) {
				throw new RuntimeException("Cloneable interface on Parameter has changed", e);
			}
			copyDialog.setMessage(copyMessage);
			messageData = getValue(context, dataId, copyMessage, eventData);
		}else{
			messageData = copyDialog.getTitleData();
		}
		copyDialog.setMessageData(messageData);
		return false;
	}
}
