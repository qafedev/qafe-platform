/**
 * Copyright 2008-2017 Qualogy Solutions B.V.
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.qualogy.qafe.bind.commons.type.Parameter;
import com.qualogy.qafe.bind.core.application.ApplicationContext;
import com.qualogy.qafe.bind.presentation.event.Event;
import com.qualogy.qafe.bind.presentation.event.EventItem;
import com.qualogy.qafe.bind.presentation.event.function.BuiltInFunction;
import com.qualogy.qafe.bind.presentation.event.function.OpenWindow;
import com.qualogy.qafe.core.datastore.ApplicationLocalStore;
import com.qualogy.qafe.core.datastore.DataIdentifier;
import com.qualogy.qafe.core.errorhandling.ExternalException;
import com.qualogy.qafe.core.framework.presentation.EventData;
import com.qualogy.qafe.presentation.EventHandlerImpl;
import com.qualogy.qafe.presentation.handler.ExecuteEventItem;

@Deprecated
public class OpenWindowExecute extends AbstractEventItemExecute implements ExecuteEventItem {

	// CHECKSTYLE.OFF: CyclomaticComplexity
	public boolean execute(EventItem eventItem, ApplicationContext context,
			Event event, EventData eventData, Collection<BuiltInFunction> listToExecute,
			EventHandlerImpl eventHandler, DataIdentifier dataId) throws ExternalException{

		if (eventItem instanceof OpenWindow) {
			OpenWindow openWindow = (OpenWindow) eventItem;

			OpenWindow copyOpenWindow = new OpenWindow();
			copyOpenWindow.setPlacement(openWindow.getPlacement());
			copyOpenWindow.setExternal(openWindow.getExternal());
			if(openWindow.getTitle() != null){
				copyOpenWindow.setTitle(openWindow.getTitle());
				Object o =getValue(context, dataId, openWindow.getTitle(), eventData);
				copyOpenWindow.setTitleData( o!=null ? o.toString(): null);
			}
			if(openWindow.getUrl() != null){
				copyOpenWindow.setUrl(openWindow.getUrl());
				Object o =getValue(context, dataId, openWindow.getUrl(), eventData);
				copyOpenWindow.setUrlData(o!=null ? o.toString(): null);
			}
			if(openWindow.getWindow() != null){
				try {
					copyOpenWindow.setWindow((Parameter)openWindow.getWindow().clone());
				} catch (CloneNotSupportedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Object o = getValue(context, dataId, copyOpenWindow.getWindow(), eventData);
				copyOpenWindow.setWindowData(o!=null? o.toString():null);
			}
			if (openWindow.getParams() != null){
				copyOpenWindow.setParams(openWindow.getParams());
				Object o = getValue(context, dataId, openWindow.getParams(), eventData);
				copyOpenWindow.setParamsData(o!=null? o.toString():null);
			}
			if (openWindow.getDataParameters() != null){
				String localStoreId = generateLocalStoreId(eventData.getWindowSession(), context, copyOpenWindow.getWindowData());
				List<Parameter> clonedList = new ArrayList<Parameter>();
				for (Parameter p :openWindow.getDataParameters()){
					try {
						Parameter clone = (Parameter) p.clone();
						clone.setData(getValue(context, dataId, p, eventData));
						clonedList.add(clone);
						ApplicationLocalStore.getInstance().storeTemporary(localStoreId, clone.getName(), clone.getData());
					} catch (CloneNotSupportedException e) {
						e.printStackTrace();
					}
				}
				copyOpenWindow.setDataParameters(clonedList);
			}
			listToExecute.add(copyOpenWindow);
		}
		return false;
	}
    // CHECKSTYLE.OFF: CyclomaticComplexity
}
