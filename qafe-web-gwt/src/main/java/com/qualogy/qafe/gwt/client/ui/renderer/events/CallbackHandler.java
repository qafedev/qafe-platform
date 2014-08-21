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
package com.qualogy.qafe.gwt.client.ui.renderer.events;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.UIObject;
import com.qualogy.qafe.gwt.client.component.HasDataModel;
import com.qualogy.qafe.gwt.client.context.ClientApplicationContext;
import com.qualogy.qafe.gwt.client.exception.GWTServiceException;
import com.qualogy.qafe.gwt.client.service.RPCService;
import com.qualogy.qafe.gwt.client.service.RPCServiceAsync;
import com.qualogy.qafe.gwt.client.ui.renderer.RendererHelper;
import com.qualogy.qafe.gwt.client.ui.renderer.events.exception.RequiredFieldException;
import com.qualogy.qafe.gwt.client.util.ComponentRepository;
import com.qualogy.qafe.gwt.client.util.QAMLConstants;
import com.qualogy.qafe.gwt.client.vo.data.EventDataGVO;
import com.qualogy.qafe.gwt.client.vo.data.EventDataI;
import com.qualogy.qafe.gwt.client.vo.data.GDataObject;
import com.qualogy.qafe.gwt.client.vo.functions.BuiltInFunctionGVO;
import com.qualogy.qafe.gwt.client.vo.functions.DataContainerGVO;
import com.qualogy.qafe.gwt.client.vo.functions.execute.FunctionsExecutor;
import com.qualogy.qafe.gwt.client.vo.handlers.BuiltinHandlerHelper;
import com.qualogy.qafe.gwt.client.vo.handlers.EventHandler;
import com.qualogy.qafe.gwt.client.vo.ui.event.EventListenerGVO;
import com.qualogy.qafe.gwt.client.vo.ui.event.InputVariableGVO;

public class CallbackHandler {

	private final static RPCServiceAsync service = (RPCServiceAsync) GWT.create(RPCService.class);
	private static AsyncCallback<?> callback = null;

	final public static void createCallBack(final Object sender, final String listenerType, final EventListenerGVO eventGVO, List<InputVariableGVO> listOfInputVariables, Map<String,String> mouseInfo) {
		if (sender instanceof UIObject) {
			createCallBack((UIObject) sender, listenerType, eventGVO, listOfInputVariables, null, mouseInfo);
		}
	}

	final public static void createCallBack(final Object sender, final String listenerType, final EventListenerGVO eventGVO, List<InputVariableGVO> listOfInputVariables) {
		createCallBack((UIObject) sender, listenerType, eventGVO, listOfInputVariables, null);
	}

	final private static AsyncCallback<?> createCallBack(final String listenerType) {
		ServiceDefTarget endpoint = (ServiceDefTarget) service;
		String moduleRelativeURL = GWT.getModuleBaseURL() + "rpc.service";
		endpoint.setServiceEntryPoint(moduleRelativeURL);

		if (callback == null){
				callback = new AsyncCallback<Object>() {
				public void onSuccess(Object result) {
					GDataObject data = (GDataObject) result;
					//this is to trigger handler registered to do on success of event body execution.
					ClientApplicationContext.getInstance().fireResult(data);
					/* Data is processed after the event firing is done.
					 * This makes it possible to get handle to newly added rows on a datagrid when the add button event on datagrid tool bar is triggered.
					 * Also enables to set a cell value when new row is added in the same event.
					 */
					processOutput(data);
					ClientApplicationContext.getInstance().setBusy(false);
				}

				public void onFailure(Throwable caught) {
					ClientApplicationContext.getInstance().log("Event execution for " + listenerType + " failed", caught.getMessage(), true, false, caught);
					ClientApplicationContext.getInstance().setBusy(false);
					if (caught instanceof GWTServiceException) {
						GWTServiceException gWTServiceException = (GWTServiceException) caught;
						processOutput(gWTServiceException.getGDataObject());
					}
				}

			};
		}
		return callback;
	}

	final public static void createCallBack(final UIObject sender, final String listenerType, final EventListenerGVO eventGVO, List<InputVariableGVO> listOfInputVariables) {
		createCallBack(sender, listenerType, eventGVO, listOfInputVariables, null);
	}

    // CHECKSTYLE.OFF: CyclomaticComplexity
	final public static void createCallBack(final UIObject sender, final String listenerType, final EventListenerGVO eventGVO, List<InputVariableGVO> listOfInputVariables, Map<String,Object> internalVariables, Map<String,String> mouseInfo) {
	    if (ClientApplicationContext.getInstance().isClientSideEventEnabled()) {
	        EventCallbackHandler.setRpcService(service);
	        EventHandler.getInstance().handleEvent(sender, listenerType, eventGVO, mouseInfo);
	        ClientApplicationContext.getInstance().log("Sender: " + sender + " - " + listenerType);
	        return;
	    }
	    
		if (eventGVO != null) {

			String senderUUID = DOM.getElementProperty(sender.getElement(), "uuid");
			String senderId = DOM.getElementAttribute(sender.getElement(), "id");

			if (senderId != null && senderId.length() > 0) {

				String context = RendererHelper.getComponentContext(sender);
				String parent = RendererHelper.getParentComponent(sender);
				String senderName = RendererHelper.getNamedComponentName(sender);
				ClientApplicationContext.getInstance().setBusy(true);

				AsyncCallback<?> callback = createCallBack(listenerType);

				EventDataGVO eventDataObject = new EventDataGVO();
				eventDataObject.setUuid(senderUUID);
				eventDataObject.setEventId(eventGVO.getEventId());
				eventDataObject.setListenerType(listenerType);
				eventDataObject.setSenderName(senderName);
				eventDataObject.setUserUID(ClientApplicationContext.getInstance().getAppUUID());
				eventDataObject.setWindowSession(ClientApplicationContext.getInstance().getWindowSession());
				eventDataObject.setInternalVariables(internalVariables);
				eventDataObject.setParameters(ClientApplicationContext.getInstance().getParameters());
				eventDataObject.setSender(senderId);
				eventDataObject.setOriginalSenderId(senderId); // unmanipulated Mouse Coordinates
				if(senderId.startsWith(QAMLConstants.TOKEN_INDEXING)) {
					eventDataObject.setIndex(senderId.substring(senderId.indexOf(QAMLConstants.TOKEN_INDEXING) + QAMLConstants.TOKEN_INDEXING.length(), senderId.lastIndexOf(QAMLConstants.TOKEN_INDEXING)));
				}

				if (mouseInfo!=null){
					eventDataObject.setMouseCoordinates(mouseInfo.get(EventDataI.MOUSE_X), mouseInfo.get(EventDataI.MOUSE_Y));
				}

				// sender id
				eventDataObject.setParent(parent);
				if (eventGVO.getSourceId() != null) {
					eventDataObject.setSourceId(eventGVO.getSourceId());
					if (!senderId.startsWith("||") && !senderId.startsWith(QAMLConstants.TOKEN_INDEXING)) {
						if (senderId.indexOf('|') > 0) {
							eventDataObject.setSourceIdValue(senderId.substring(0, senderId.indexOf('|')));
						}
					}
				}

				if (eventGVO.getSourceName() != null) {
					eventDataObject.setSourceName(eventGVO.getSourceName());
					if (RendererHelper.isNamedComponent(sender)) {
						eventDataObject.setSourceNameValue(RendererHelper.getNamedComponentName(sender));
					}
				}
				if (eventGVO.getSourceValue() != null) {
					eventDataObject.setSourceValue(eventGVO.getSourceValue());
					Object o = getValue(sender, sender, eventDataObject, true, null);
					if (o instanceof String) {
						eventDataObject.setSourceValueValue(o.toString());
					}
				}
				if (eventGVO.getSourceListenerType() != null) {
					eventDataObject.setSourceListenerType(eventGVO.getSourceListenerType());
					eventDataObject.setSourceListenerTypeValue(listenerType);
				}

				if (senderId.startsWith("||")) { // so this is a click from a table{
					eventDataObject.setSender(senderId.substring(senderId.lastIndexOf("||") + 2));
				} else if (senderId.startsWith(QAMLConstants.TOKEN_INDEXING)) { // so this is a click from a tile or an inner component in datagrid
					eventDataObject.setSender(senderId.substring(senderId.lastIndexOf(QAMLConstants.TOKEN_INDEXING) + QAMLConstants.TOKEN_INDEXING.length()));
				}

				boolean error = false;

				if (listOfInputVariables != null) {
					try {
						for (InputVariableGVO inputVariables : listOfInputVariables) {

							String uuid = DOM.getElementProperty(sender.getElement(), "uuid");
							String value = null;

							DataContainerGVO dataContainerObject = null;

							String inputVariableReference = inputVariables.getReference();
							if (hasAttribute(inputVariableReference)) {
								value = getAttributeValue(inputVariableReference, uuid, parent, context);
							} else if(inputVariableReference.contains(".$$")){
								dataContainerObject = fetchDatagridRowValues(inputVariableReference, uuid, parent, context);
							} else if(inputVariableReference.contains("[")){
								dataContainerObject = fetchDatagridCellValue(inputVariableReference, uuid, parent, context);
							} else {
								String key = RendererHelper.generateId(inputVariableReference, parent, context); // inputVariables[i][1]
								ClientApplicationContext.getInstance().log(key);
								List<UIObject> uiObjects = ComponentRepository.getInstance().getComponent(key);

								// since the parameter can be a complex object, we need to create a substitute for it.
								// This can only be used in the name variant though (namedcomponents)
								if (uiObjects != null) {
									for (UIObject uiObject : uiObjects) {

										Object o = getValue(uiObject, sender, eventDataObject, false, null);

										if (o instanceof String) {
											value = o.toString();
										} else if (o instanceof DataContainerGVO) {
											dataContainerObject = (DataContainerGVO) o;
										}

										if (uiObject instanceof HasDataModel) {
											// Get also the data model behind
											HasDataModel hasDataModel = (HasDataModel)uiObject;
											if (hasDataModel.getDataModel() != null) {
												Object dataModel = hasDataModel.getDataModel();
												String newInputVariableReference = inputVariableReference + QAMLConstants.DATAMODEL_POSTFIX;
												if (eventDataObject.getInternalVariables() == null) {
													eventDataObject.setInternalVariables(new HashMap<String,Object>());
												}
												eventDataObject.getInternalVariables().put(newInputVariableReference, dataModel);
											}
										}

									}

								} else {
									// so the object could not be found in the ComponentRepository, maybe we try by name to find it.
									ClientApplicationContext.getInstance().log("Reference" + inputVariables.getReference());
									String[] keysSet = inputVariables.getReference().split("[.]");
									if (keysSet != null) {
										String searchKey = null;
										if (keysSet.length == 1) {// so only the key
											searchKey = key;
										} else {
											searchKey = RendererHelper.generateId(keysSet[0], parent, context);
										}
										if (searchKey != null) {
											uiObjects = ComponentRepository.getInstance().getNamedComponent(searchKey);
											if (uiObjects != null) {
												for (UIObject uiObject : uiObjects) {
													// Collect all the data from a list of named components
													DataContainerGVO dataContainer = createDataContainer(inputVariables.getReference(), uiObject, sender, eventDataObject);
													if (dataContainerObject == null) {
														dataContainerObject = dataContainer;
													} else if (dataContainer != null) {
														if (dataContainer.getKind() == dataContainerObject.getKind()) {
															switch (dataContainer.getKind()) {
																case DataContainerGVO.KIND_MAP : {
																	dataContainerObject.getDataMap().putAll(dataContainer.getDataMap());
																} break;
															}
														}
													}

													// Get value of a data member
													if(keysSet.length > 1){
														if(dataContainerObject.getDataMap().get(keysSet[1]) != null){
															value = dataContainerObject.getDataMap().get(keysSet[1]).getDataString();
														}
														dataContainerObject = null;
													}
												}
											} else {   // Apparently we have to search for the Group now.
												dataContainerObject = BuiltinHandlerHelper.getGroupedComponentValue(sender, inputVariables.getReference(), eventDataObject, key);

											}
										}
									}
								}
							}

							String x = inputVariables.getComponentValue() != null ? inputVariables.getComponentValue() : value;
							if ((inputVariableReference != null) && inputVariableReference.startsWith("||")) {
								// so this is a click from a table
								inputVariableReference = (inputVariables.getReference().substring(senderId.lastIndexOf("||") + 2));
							}
							eventDataObject.getInputVariables().add(new InputVariableGVO(inputVariables.getName(), inputVariableReference, inputVariables.getDefaultValue(), x, dataContainerObject));
						}
					} catch (RequiredFieldException e) {
						ClientApplicationContext.getInstance().log(e.getTitle(), e.getMessage(), true, true, e);
						error = true;
					} catch (TypeValidationException e) {
						ClientApplicationContext.getInstance().log("Validation Error: ", e.getMessage(), true, true, e);
						error = true;
					}
				}
				if (!error) {
					service.executeEvent(EnrichEventUtil.enrichEvent(eventDataObject), callback);
				}
			} else {
				ClientApplicationContext.getInstance().log("Callbackhander: Trying to call event while id is empty !!");
			}
		} else {
			ClientApplicationContext.getInstance().log("Callbackhander: eventGVO cannot be null !!");
		}

	}
	
	// CHECKSTYLE.ON: CyclomaticComplexity

	private static DataContainerGVO fetchDatagridRowValues(String inputVariableReference, String uuid, String parent,String context) {
		return BuiltinHandlerHelper.fetchDatagridRowValues(inputVariableReference, uuid, parent, context);
	}

	private static DataContainerGVO fetchDatagridCellValue(String inputVariableReference, String uuid, String parent,String context) {
		return BuiltinHandlerHelper.fetchDatagridCellValue(inputVariableReference, uuid, parent, context);
	}

	private static boolean hasAttribute(String inputVariableReference) {
		return BuiltinHandlerHelper.hasAttribute(inputVariableReference);
	}

	private static String getAttributeValue(String inputVariableReference, String uuid, String parent, String context) {
		return BuiltinHandlerHelper.getAttributeValue(inputVariableReference, uuid, parent, context);
	}
	
	public static DataContainerGVO createDataContainer(String parameterName, UIObject uiObject, final UIObject sender, EventDataGVO eventDataObject) throws RequiredFieldException {
		return BuiltinHandlerHelper.createDataContainer(parameterName, uiObject, sender, eventDataObject);
	}

	private static void processOutput(GDataObject data) {
		if (data != null) {
			BuiltInFunctionGVO[] builtInFunctions = data.getFunctions();
			if (builtInFunctions != null) {
				for (int i = 0; i < builtInFunctions.length; i++) {
					BuiltInFunctionGVO builtInFunctionGVO = builtInFunctions[i];
					builtInFunctionGVO.setSenderId(data.getSenderId());
					builtInFunctionGVO.setListenerType(data.getListenerType());
					GWT.log("Process output:" + builtInFunctionGVO.getClassName(), null);
					FunctionsExecutor.getInstance().execute(builtInFunctionGVO);
				}
			}
			ClientApplicationContext.getInstance().setLogText("Execution time:" + data.getTime() + " ms");
		}
	}

	public static Object getValue(UIObject uiObject, final UIObject sender, EventDataGVO eventDataObject, boolean idValueOnly, String groupName){
		return BuiltinHandlerHelper.getValue(uiObject, sender, eventDataObject, idValueOnly, groupName);
	}
	
	
}
