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
package com.qualogy.qafe.presentation.handler.executors;

import java.util.Collection;
import java.util.List;

import com.qualogy.qafe.bind.core.application.ApplicationContext;
import com.qualogy.qafe.bind.presentation.event.Event;
import com.qualogy.qafe.bind.presentation.event.EventItem;
import com.qualogy.qafe.bind.presentation.event.function.BuiltInFunction;
import com.qualogy.qafe.bind.presentation.event.function.OpenWindow;
import com.qualogy.qafe.core.datastore.DataIdentifier;
import com.qualogy.qafe.core.errorhandling.ExternalException;
import com.qualogy.qafe.core.framework.presentation.EventData;
import com.qualogy.qafe.core.security.Authorize;
import com.qualogy.qafe.core.security.Restriction;
import com.qualogy.qafe.core.security.Rule;
import com.qualogy.qafe.core.security.SetRestriction;
import com.qualogy.qafe.presentation.EventHandlerImpl;
import com.qualogy.qafe.presentation.handler.ExecuteEventItem;


public class AuthorizeExecute extends AbstractEventItemExecute implements ExecuteEventItem {
	
	public boolean execute(EventItem eventItem, ApplicationContext context,
			Event event, EventData eventData, Collection<BuiltInFunction> listToExecute,
			EventHandlerImpl eventHandler, DataIdentifier dataId) throws ExternalException{
	
		if (eventItem instanceof Authorize) {
			Authorize authorize = (Authorize) eventItem;
			
//			String appId = context.getId().stringValueOf();
//			String windowId = null;
			
			List<Restriction> restrictionList = null;
			if (authorize.hasRestrictions()) {
				restrictionList = authorize.getRestrictions();
			}
			
			if (authorize.hasToOpenWindows()) {
				List<String> toOpenWindowList = authorize.getToOpenWindows();
				for (int i=0; i<toOpenWindowList.size(); i++) {
					String toOpenWindow = toOpenWindowList.get(i);
					OpenWindow openWindow = new OpenWindow();
					openWindow.setWindowData(toOpenWindow);
					listToExecute.add(openWindow);

					String appId = context.getId().stringValueOf();
					String windowId = toOpenWindow;
					if (toOpenWindow.indexOf(".") > -1) {
						appId = toOpenWindow.substring(0, toOpenWindow.indexOf("."));
						windowId = toOpenWindow.substring(toOpenWindow.indexOf(".") + 1);
					}
					if (authorize.hasRestrictions(appId, windowId)) {
						if (restrictionList != null) {
							restrictionList.addAll(authorize.getRestrictions());
						} else {
							restrictionList = authorize.getRestrictions();	
						}
					}
					
					if ((authorize.getUserInfo() != null) && authorize.getUserInfo().isLoggedOutApp(appId)) {
						authorize.getUserInfo().removeLoggedOutApp(appId);	
					}
				}
			}
			
			if (restrictionList != null) {
				for (int i=0; i< restrictionList.size(); i++) {
					Restriction restriction = restrictionList.get(i);
					String property = restriction.getNoAccess().equals(Rule.NOACCESS_DISABLED) ? Rule.NOACCESS_DISABLED : "visible";
					Boolean propertyValue = property.equals(Rule.NOACCESS_DISABLED) ? true : false;
					SetRestriction setRestriction = new SetRestriction();
					setRestriction.setApplicationId(restriction.getApplicationId());
					setRestriction.setWindowId(restriction.getWindowId());
					setRestriction.setComponentId(restriction.getComponentId());
					setRestriction.setProperty(property);
					setRestriction.setPropertyValue(propertyValue);
					listToExecute.add(setRestriction);
				}
			}
		}
		return false;
	}
}
