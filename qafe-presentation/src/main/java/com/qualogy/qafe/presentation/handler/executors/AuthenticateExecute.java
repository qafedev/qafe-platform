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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.qualogy.qafe.bind.business.action.BusinessActionItem;
import com.qualogy.qafe.bind.commons.type.Out;
import com.qualogy.qafe.bind.core.application.ApplicationContext;
import com.qualogy.qafe.bind.integration.service.ServiceRef;
import com.qualogy.qafe.bind.presentation.event.Event;
import com.qualogy.qafe.bind.presentation.event.EventItem;
import com.qualogy.qafe.bind.presentation.event.function.Authenticate;
import com.qualogy.qafe.bind.presentation.event.function.BuiltInFunction;
import com.qualogy.qafe.bind.presentation.event.function.CloseWindow;
import com.qualogy.qafe.bind.presentation.event.function.dialog.GenericDialog;
import com.qualogy.qafe.core.application.UserInfo;
import com.qualogy.qafe.core.datastore.DataIdentifier;
import com.qualogy.qafe.core.datastore.DataStore;
import com.qualogy.qafe.core.errorhandling.ExternalException;
import com.qualogy.qafe.core.framework.business.UnableToManageException;
import com.qualogy.qafe.core.framework.presentation.EventData;
import com.qualogy.qafe.core.framework.presentation.EventHandlerException;
import com.qualogy.qafe.core.security.Authorize;
import com.qualogy.qafe.core.security.Restriction;
import com.qualogy.qafe.core.security.RestrictionRule;
import com.qualogy.qafe.core.security.SecurityInfo;
import com.qualogy.qafe.presentation.EventHandlerImpl;
import com.qualogy.qafe.presentation.handler.EventItemExecutor;
import com.qualogy.qafe.presentation.handler.ExecuteEventItem;
import com.qualogy.qafe.util.ExceptionHelper;

public class AuthenticateExecute extends AbstractEventItemExecute implements ExecuteEventItem {

	public boolean execute(EventItem eventItem, ApplicationContext context,
			Event event, EventData eventData, Collection<BuiltInFunction> listToExecute,
			EventHandlerImpl eventHandler, DataIdentifier dataId) throws ExternalException{

		if (eventItem instanceof Authenticate) {
			Authenticate authenticate = (Authenticate) eventItem;

			try {
				if (authenticate.getUsername() != null) {
					Object o = getValue(context, dataId, authenticate.getUsername(), eventData);
					DataStore.store(dataId, authenticate.getUsername().getRef().getRootRef(), o);
				}
				if (authenticate.getPassword() != null) {
					Object o = getValue(context, dataId, authenticate.getPassword(), eventData);
					DataStore.store(dataId, authenticate.getPassword().getRef().getRootRef(), o);
				}

				context.getBusinessManager().manage(context, dataId, authenticate.getRef().getRef());

				String resourceId = context.getSecuritySettings().getAuthenticationSettings().getModule().getResourceRef();
				String resourceAppId = context.getSecuritySettings().getAuthenticationSettings().getModule().getApplicationRef();

				Iterator<BusinessActionItem> itr = authenticate.getRef().getRef().getBusinessActionItems().iterator();
				Object authenticateResult = null;
				Object authorizeResult = null;
				while(itr.hasNext()) {
					BusinessActionItem businessActionItem = itr.next();
					if (businessActionItem instanceof ServiceRef){
						ServiceRef serviceRef = (ServiceRef) businessActionItem;
						if (serviceRef.getMethod().getId().indexOf(SecurityInfo.METHOD_AUTHENTICATE) > -1) {
							Out out = (Out) serviceRef.getMethod().getOutput().get(0);
							authenticateResult = DataStore.getValue(dataId, out.getNameRoot());
						}
						if (serviceRef.getMethod().getId().indexOf(SecurityInfo.METHOD_GET_RESTRICTIONS) > -1) {
							Out out = (Out) serviceRef.getMethod().getOutput().get(0);
							authorizeResult = DataStore.getValue(dataId, out.getNameRoot());
						}
					}
				}

				if (isAuthenticated(authenticateResult)) {
					UserInfo userInfo = SecurityInfo.getInstance().getUserInfo(eventData.getUserUID());
					userInfo.setAuthenticated();
					userInfo.setUsedAuthenticationResource(resourceAppId, resourceId);

					List<Restriction> restrictionList = convertToRestrictions(authorizeResult);
					if (restrictionList != null) {
						for (int i=0; i<restrictionList.size(); i++) {
							SecurityInfo.getInstance().addRestriction(restrictionList.get(i));
							userInfo.addRestriction(restrictionList.get(i));
						}
					}

					String openedAuthenticationWindow = eventData.getWindowId();
					CloseWindow closeWindow = new CloseWindow();
					closeWindow.setWindowData(openedAuthenticationWindow);
					listToExecute.add(closeWindow);

					Authorize authorize = new Authorize(userInfo.getUserUID(), resourceAppId, resourceId);
					EventItemExecutor.getInstance().execute(authorize, context, event, eventData, listToExecute, eventHandler, dataId);
				} else {
					GenericDialog genericDialog = new GenericDialog("Login failed.", "Username/password combination does not exists.");
					EventItemExecutor.getInstance().execute(genericDialog, context, event, eventData, listToExecute, eventHandler, dataId);
				}
			} catch (ExternalException e) {
				ExceptionHelper.printStackTrace(e);
				throw e;
			} catch (UnableToManageException e) {
				ExceptionHelper.printStackTrace(e);
				throw new EventHandlerException(e);
			} catch (RuntimeException e){
				ExceptionHelper.printStackTrace(e);
				throw e;
			}
		}
		return false;
	}

	private Boolean isAuthenticated(Object authenticateResult) {
		if (authenticateResult != null) {

			Number authenticateCode = null;

			if (authenticateResult instanceof Map) {
				Map map = (Map) authenticateResult;
				String[] attributes = new String[1];
				attributes[0] = "result_code";
				for (int i=0; i<attributes.length; i++) {
					if (map.containsKey(attributes[i])) {
						authenticateCode = (Number) map.get(attributes[i]);
						break;
					}
					if (map.containsKey(attributes[i].toUpperCase())) {
						authenticateCode = (Number) map.get(attributes[i].toUpperCase());
						break;
					}
				}
			} else if (authenticateResult instanceof Number) {
				authenticateCode = (Number) authenticateResult;
			}

			if ((authenticateCode != null) && (authenticateCode.intValue() == 0)) {
				return true;
			}
		}
		return false;
	}

	private List<Restriction> convertToRestrictions(Object authorizeResult) {
		List<Restriction> restrictionList = new ArrayList<Restriction>();

		if (authorizeResult != null) {
			if (authorizeResult instanceof List) {
				List resultList = (List) authorizeResult;
				for (int i=0; i<resultList.size(); i++) {
					Restriction restriction = convertToRestriction(resultList.get(i));
					if (restriction != null) {
						restrictionList.add(restriction);
					}
				}
			} else if (authorizeResult instanceof Map) {
				Restriction restriction = convertToRestriction(authorizeResult);
				if (restriction != null) {
					restrictionList.add(restriction);
				}
			}
		}

		if (restrictionList.size() > 0) {
			return restrictionList;
		}
		return null;
	}

    // CHECKSTYLE.OFF: CyclomaticComplexity
	private Restriction convertToRestriction(Object authorizeResult) {
		Restriction restriction = null;

		if (authorizeResult != null) {
			if (authorizeResult instanceof Map) {
				Map map = (Map) authorizeResult;

				RestrictionRule restrictionRule = null;

				String[] attributes = new String[6];
				attributes[0] = "roleName";
				attributes[1] = "roleOrder";
				attributes[2] = "applicationId";
				attributes[3] = "windowId";
				attributes[4] = "componentId";
				attributes[5] = "noAccess";
				for (int i=0; i<attributes.length; i++) {
					String attribute = attributes[i];
					Object value = null;
					if (map.containsKey(attribute)) {
						value = map.get(attribute);
					} else if (map.containsKey(attribute.toUpperCase())) {
						value = map.get(attribute.toUpperCase());
					}

					if ((value != null) && (restrictionRule == null)) {
						restrictionRule = new RestrictionRule();
					}

					if (restrictionRule != null) {
						switch (i) {
							case 0: {
								restrictionRule.setRoleName((String) value);
							} break;
							case 1: {
								restrictionRule.setRoleOrder(((Number) value).intValue());
							} break;
							case 2: {
								restrictionRule.setApplicationId((String) value);
							} break;
							case 3: {
								restrictionRule.setWindowId((String) value);
							} break;
							case 4: {
								restrictionRule.setComponentId((String) value);
							} break;
							case 5: {
								restrictionRule.setNoAccess((String) value);
							} break;
						}
					}
				}
				restriction = restrictionRule;
			} else if (authorizeResult instanceof Restriction) {
				restriction = (Restriction) authorizeResult;
			}
		}
		return restriction;
	}
	// CHECKSTYLE.ON: CyclomaticComplexity
}
