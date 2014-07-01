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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.widgetideas.client.SliderBar;
import com.google.gwt.widgetideas.client.ValueSpinner;
import com.qualogy.qafe.gwt.client.component.AreaWidget;
import com.qualogy.qafe.gwt.client.component.DataMap;
import com.qualogy.qafe.gwt.client.component.HasChoice;
import com.qualogy.qafe.gwt.client.component.HasData;
import com.qualogy.qafe.gwt.client.component.HasDataGridMethods;
import com.qualogy.qafe.gwt.client.component.HasDataModel;
import com.qualogy.qafe.gwt.client.component.HasRequiredValidationMessage;
import com.qualogy.qafe.gwt.client.component.LabeledTextFieldWidget;
import com.qualogy.qafe.gwt.client.component.MapWidget;
import com.qualogy.qafe.gwt.client.component.QDatePicker;
import com.qualogy.qafe.gwt.client.component.QPagingScrollTable;
import com.qualogy.qafe.gwt.client.component.QRadioButton;
import com.qualogy.qafe.gwt.client.component.Tiles;
import com.qualogy.qafe.gwt.client.component.TitledComponent;
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
import com.qualogy.qafe.gwt.client.vo.handlers.EventHandler;
import com.qualogy.qafe.gwt.client.vo.ui.CheckBoxGVO;
import com.qualogy.qafe.gwt.client.vo.ui.TextFieldGVO;
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
	        EventHandler.getInstance().handleEvent(sender, listenerType, eventGVO);
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
												uiObjects = ComponentRepository.getInstance().getGroupedComponent(key);
												if (uiObjects != null) {
													DataContainerGVO dataContainer = new DataContainerGVO();
													dataContainer.setParameterName(inputVariables.getReference());
													dataContainer.setKind(DataContainerGVO.KIND_MAP);
													DataMap dataMap = new DataMap();
													dataContainer.setDataMap(dataMap);
													for (UIObject uiObject : uiObjects) {
														// Collect all the data from a list of named components
														fillDataContainerMapForGroup(dataMap,inputVariables.getReference(), uiObject, sender, eventDataObject);
													}
													dataContainerObject = dataContainer;
												}

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
		String[] inputRef = inputVariableReference.split("[.]");
		String key = RendererHelper.generateId(inputRef[0], parent, context);
		List<UIObject> uiObjects = ComponentRepository.getInstance().getComponent(key);
		DataContainerGVO dtc = null;
		if (uiObjects != null) {
			dtc = fetchDatagridRowValues(inputRef, uiObjects);
		}
		return dtc;
	}

	private static DataContainerGVO fetchDatagridRowValues(String[] inputRef, List<UIObject> uiObjects) {
		DataContainerGVO dtc = null;
		if (inputRef == null || uiObjects == null) {
			return null;
		}
		for(UIObject uiObject : uiObjects){
			if(uiObject instanceof QPagingScrollTable) {
				QPagingScrollTable qps = (QPagingScrollTable) uiObject;
				String[] rowStatusAndColumnGroup = null;
				Object obj = null;
				if(inputRef[1].contains(QAMLConstants.TOKEN_INDEXING)){ // Reference is by row status and group name on columns.
					rowStatusAndColumnGroup = inputRef[1].split(QAMLConstants.TOKEN_INDEXING);
				}
				if(rowStatusAndColumnGroup == null){
					obj = qps.getData(inputRef[1], null); // Fetch data based on row status alone.
				} else {
					obj = qps.getData(rowStatusAndColumnGroup[0], rowStatusAndColumnGroup[1]); // Fetch data based on row status and group name on columns.
				}
				dtc = convertToDataGVO(obj);
			}
		}
		return dtc;
	}

	private static DataContainerGVO fetchDatagridCellValue(String inputVariableReference, String uuid, String parent,String context) {
		String datagrid = inputVariableReference.replaceFirst("\\[.+", "");
		String datagridId = RendererHelper.generateId(datagrid, parent, context);
		String column = null;
		if (inputVariableReference.contains(".")) {
			column = inputVariableReference.replaceFirst(".+\\.", "");
		}
		String selectedIndex = inputVariableReference.replaceFirst(".+\\[", "").replaceFirst("\\].*", "");
		DataContainerGVO value = null;
		List<UIObject> uiObjects = ComponentRepository.getInstance().getComponent(datagridId);
		if (uiObjects != null) {
			for (UIObject uiObject : uiObjects){
				if (uiObject instanceof HasDataGridMethods) {
					HasDataGridMethods hasDataGridMethods = (HasDataGridMethods)uiObject;
					int rowIndex = hasDataGridMethods.getRowIndex(selectedIndex);
					if (rowIndex == -1) {
						continue;
					}
					value = hasDataGridMethods.getRowValue(rowIndex);
					if ((value == null) || (column == null)) {
						continue;
					}
					value = value.getDataMap().get(column);
				}
			}
		}
		return value;
	}

	private static boolean hasAttribute(String inputVariableReference) {
		if ((inputVariableReference != null) && (inputVariableReference.indexOf("@") > -1)) {
			return true;
		}
		return false;
	}

	private static String getAttributeValue(String inputVariableReference, String uuid, String parent, String context) {
		String value = "";
		if (hasAttribute(inputVariableReference)) {
			String inputReference = inputVariableReference.substring(0, inputVariableReference.indexOf("@"));
			String attribute = inputVariableReference.substring(inputVariableReference.indexOf("@") + 1);
			String key = RendererHelper.generateId(inputReference, parent, context);
			List<UIObject> uiObjects = ComponentRepository.getInstance().getComponent(key);
			if (uiObjects != null) {
				for (UIObject uiObject : uiObjects) {
					if ("pagesize".equals(attribute)) {
						if (uiObject instanceof HasDataGridMethods) {
							HasDataGridMethods dataGridSortableTable = (HasDataGridMethods) uiObject;
							value = String.valueOf(dataGridSortableTable.getPageSize());
						}
					} else if ("currentpage".equals(attribute)) {
						if (uiObject instanceof HasDataGridMethods) {
							HasDataGridMethods dataGridSortableTable = (HasDataGridMethods) uiObject;
							value = String.valueOf(dataGridSortableTable.getCurrentPage());
						}
					}
				}
			}
		}
		return value;
	}
	private static void fillDataContainerMapForGroup(DataMap dataMap,String groupName, UIObject uiObject, final UIObject sender, EventDataGVO eventDataObject) throws RequiredFieldException {

			if (uiObject instanceof Widget){

				Widget widget = (Widget)uiObject;
				if (widget instanceof HasWidgets && !(widget instanceof ValueSpinner) && !(widget instanceof FormPanel) && !(widget instanceof HasDataGridMethods)) {

					HasWidgets innerHasWidget = (HasWidgets) widget;
					processWidgets(innerHasWidget, dataMap, sender, eventDataObject);
				} else {
					processNamedComponent(widget, dataMap, sender, eventDataObject, groupName);
			}
		}

	}


	public static DataContainerGVO createDataContainer(String parameterName, UIObject uiObject, final UIObject sender, EventDataGVO eventDataObject) throws RequiredFieldException {
		DataContainerGVO dtc = new DataContainerGVO();
		dtc.setParameterName(parameterName);
		if (uiObject != null) {
			dtc.setKind(DataContainerGVO.KIND_MAP);
			DataMap dataMap = new DataMap();
			dtc.setDataMap(dataMap);

			if (uiObject instanceof Tiles) {
				Tiles tiles = (Tiles) uiObject;
				if (eventDataObject.getOriginalSenderId() != null) {
					String index = eventDataObject.getOriginalSenderId().substring(0, eventDataObject.getOriginalSenderId().lastIndexOf(QAMLConstants.TOKEN_INDEXING));
					index = index.replace(QAMLConstants.TOKEN_INDEXING, "");
					Integer i = Integer.parseInt(index);
					UIObject tileElement = tiles.getTileElements().get(i);
					if (tileElement instanceof HasWidgets) {
						HasWidgets hasWidgets = (HasWidgets) tileElement;
						processWidgets(hasWidgets, dataMap, sender, eventDataObject);
					}
					if (parameterName != null && parameterName.contains(".")) {
						String[] parameterParts = parameterName.split("[.]");
						if (parameterParts != null && parameterParts.length > 1) {
							DataContainerGVO dtcInner = new DataContainerGVO();
							dtcInner.setParameterName(parameterName);
							dtcInner.setKind(DataContainerGVO.KIND_STRING);
							dtcInner.setDataString(dataMap.get(parameterParts[1]) != null ? dataMap.get(parameterParts[1]).toString() : null);
							dtc = dtcInner;
						}
					}
				}
			} else if (uiObject instanceof HasWidgets) {
				HasWidgets hasWidgets = (HasWidgets) uiObject;
				processWidgets(hasWidgets, dataMap, sender, eventDataObject);

			}
		}
		return dtc;

	}

	/**
	 * @throws RequiredFieldException
	 */
	private static void processWidgets(HasWidgets hasWidgets, DataMap dataMap, final UIObject sender, EventDataGVO eventDataObject) throws RequiredFieldException {
		for (Widget widget : hasWidgets) {

			processNamedComponent(widget, dataMap, sender, eventDataObject, null);
			if (widget instanceof HasWidgets && !(widget instanceof ValueSpinner) && !(widget instanceof FormPanel) && !(widget instanceof HasDataGridMethods)) {
				// not clear what is this code was meant for.
				/*DataContainerGVO dtc = new DataContainerGVO();
				dtc.setKind(DataContainerGVO.KIND_MAP);
				DataMap innerMap = new DataMap();
				dtc.setDataMap(innerMap);

				if (RendererHelper.isNamedComponent(widget)) {
					String name = RendererHelper.getNamedComponentName(widget);
					dtc.setParameterName(name);
					dataMap.put(name, dtc);
				}*/
				HasWidgets innerHasWidget = (HasWidgets) widget;
				processWidgets(innerHasWidget, dataMap, sender, eventDataObject);
			}
		}
	}

	/**
	 * @throws RequiredFieldException
	 */
	private static void processNamedComponent(Widget widget, DataMap dataMap, final UIObject sender, EventDataGVO eventDataObject, String groupName) throws RequiredFieldException {
		UIObject uiObject = widget;
		if (widget instanceof TitledComponent) {
			uiObject = ((TitledComponent)widget).getDataComponent();
		}

		if (RendererHelper.isNamedComponent(uiObject)) {
			String name = RendererHelper.getNamedComponentName(uiObject);
			String value = null;
			boolean valueOnly = false;
			if (uiObject instanceof ListBox) {
				valueOnly = true;
			}

			DataContainerGVO data = new DataContainerGVO();

			Object valueObject = getValue(uiObject, sender, eventDataObject, valueOnly, groupName);
			if (valueObject instanceof String) {
				value = String.valueOf(valueObject);
				if (uiObject instanceof QDatePicker) {
					data.setKind(DataContainerGVO.KIND_STRING);
					data.setStringDataType(DataContainerGVO.TYPE_DATE);
					data.setDateData(((QDatePicker) uiObject).getValue());
				} else {
					data.setKind(DataContainerGVO.KIND_STRING);
					data.setDataString(value);
					/**
					 * When database column type is NUMBER(5,2) type which is accepting decimal values, If we pass string type with decimal value to the jdbc template it is giving exception.
					 * So the type of the textfield should be considered for setting datacontainer gvo type.
					 * This is now applicable only for textfield, same issue can come in any inputfield. But we dont have an option to set type to other components now.
					 */
					String type = DOM.getElementAttribute(uiObject.getElement(), TextFieldGVO.REGEXPTYPE);
					if(TextFieldGVO.TYPE_DOUBLE.equals(type)) {
						data.setStringDataType(DataContainerGVO.TYPE_DOUBLE);
					} else if(TextFieldGVO.TYPE_INTEGER.equals(type)) {
						data.setStringDataType(DataContainerGVO.TYPE_INT);
					} else {
						data.setStringDataType(DataContainerGVO.TYPE_STRING);
					}
				}
			} else if (valueObject instanceof DataContainerGVO) {
				data = (DataContainerGVO) valueObject;
			}
			
			if (dataMap.containsKey(name)) {
				// If value already exists, override the existing value when null or new value is not null,
				// this case can occur when multiple components are added with the same name, like TextArea
				Object oldData = dataMap.get(name);
				if ((oldData == null) || (data != null)) {
					dataMap.put(name, data);
				}
			} else {
				dataMap.put(name, data);
			}
		}
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

    // CHECKSTYLE.OFF: CyclomaticComplexity
	public static Object getValue(UIObject uiObject, final UIObject sender, EventDataGVO eventDataObject, boolean idValueOnly, String groupName){
		Object returnObject = null;
		boolean hasSimpleValue = false;
		if (uiObject instanceof QPagingScrollTable) {
			QPagingScrollTable qps = (QPagingScrollTable) uiObject;
			returnObject = qps.getData(null, groupName);
			DataContainerGVO dtc = convertToDataGVO(returnObject);
			returnObject = dtc;
		} else if (uiObject instanceof QRadioButton) {
			QRadioButton qRadioButton = (QRadioButton) uiObject;
			returnObject = qRadioButton.getText();
			hasSimpleValue = true;
		} else if (uiObject instanceof HasChoice) {
			HasChoice hasChoice = (HasChoice)uiObject;
			returnObject = hasChoice.getData();
			hasSimpleValue = true;
		} else if (uiObject instanceof HasData) {
			HasData hasData = (HasData)uiObject;
			returnObject = hasData.getData();
			hasSimpleValue = true;
			if (!(returnObject instanceof String)) {
				DataContainerGVO dtc = convertToDataGVO(returnObject);
				if (dtc != null) {
					returnObject = dtc;
					hasSimpleValue = false;
				}
			}
		} else if (uiObject instanceof CheckBox) {
			CheckBox checkBox = (CheckBox) uiObject;
			returnObject = checkBox.getValue().toString();
			hasSimpleValue = true;
			if (checkBox.getValue()) {
				String attributeValue = DOM.getElementAttribute(checkBox.getElement(), CheckBoxGVO.CHECKED_VALUE_ATTRIBUTE_TAG);
				if (attributeValue != null && attributeValue.length() > 0) {
					returnObject = attributeValue;
				}
			} else {
				String attributeValue = DOM.getElementAttribute(checkBox.getElement(), CheckBoxGVO.UNCHECKED_VALUE_ATTRIBUTE_TAG);
				if (attributeValue != null && attributeValue.length() > 0) {
					returnObject = attributeValue;
				}
			}
		} else if (uiObject instanceof FormPanel) {
			FormPanel fp = (FormPanel) uiObject;
			if (fp instanceof HasWidgets) {
				HasWidgets hasWidgets = (HasWidgets) fp;
				Iterator<Widget> itr = hasWidgets.iterator();
				while (itr.hasNext()) {
					Widget widget = itr.next();
					if (widget instanceof Grid) {
						Grid gridPanel = (Grid) widget;
						FileUpload fileUpload = (FileUpload) gridPanel.getWidget(0, 0);
						returnObject = DOM.getElementAttribute(fileUpload.getElement(), "fu-uuid");
						hasSimpleValue = true;
					}
				}
			}
		} else if (uiObject instanceof ListBox) {
			ListBox listBox = (ListBox) uiObject;
			if (!(listBox.isMultipleSelect()) && listBox.getSelectedIndex() != -1) { // dropdown
				int index = listBox.getSelectedIndex();
				if (idValueOnly) {
					returnObject = listBox.getValue(index);
					hasSimpleValue = true;
				} else {
					DataMap dm = new DataMap();
					dm.put("id", new DataContainerGVO(listBox.getValue(index)));
					dm.put("value", new DataContainerGVO(listBox.getItemText(index)));

					DataContainerGVO dtcMap = new DataContainerGVO();
					dtcMap.setKind(DataContainerGVO.KIND_MAP);
					dtcMap.setDataMap(dm);
					returnObject = dtcMap;

					// TODO: refactor, this is a workaround for checking simple value
					dtcMap.setDataString(listBox.getValue(index));
					hasSimpleValue = true;
				}
			} else if (listBox.getSelectedIndex() != -1) {
				DataContainerGVO dtclist = new DataContainerGVO();
				dtclist.setKind(DataContainerGVO.KIND_COLLECTION);
				List<DataContainerGVO> list = new ArrayList<DataContainerGVO>();
				dtclist.setListofDC(list);
				int items = listBox.getItemCount();
				for (int itemIndex = 0; itemIndex < items; itemIndex++) {
					if (listBox.isItemSelected(itemIndex)) {
						DataMap dataMap = new DataMap();
						DataContainerGVO dtcId = new DataContainerGVO();
						dtcId.setKind(DataContainerGVO.KIND_STRING);
						dtcId.setDataString(listBox.getValue(itemIndex));
						dtcId.setStringDataType(DataContainerGVO.TYPE_STRING);
						dataMap.put("id", dtcId);

						DataContainerGVO dtcValue = new DataContainerGVO();
						dtcValue.setKind(DataContainerGVO.KIND_STRING);
						dtcValue.setDataString(listBox.getItemText(itemIndex));
						dtcValue.setStringDataType(DataContainerGVO.TYPE_STRING);
						dataMap.put("value", dtcValue);

						list.add(new DataContainerGVO(dataMap));
					}
				}
				returnObject = dtclist;
			}
		} else if (uiObject instanceof QDatePicker) {
			DataContainerGVO data = new DataContainerGVO();
			data.setKind(DataContainerGVO.KIND_STRING);
			data.setStringDataType(DataContainerGVO.TYPE_DATE);
			data.setDateData(((QDatePicker) uiObject).getValue());
			returnObject = data;
			hasSimpleValue = true;
		} else if (uiObject instanceof HasText) {
			returnObject = ((HasText) uiObject).getText();
			hasSimpleValue = true;
		} else if (uiObject instanceof MapWidget) {
			MapWidget mapWidget = (MapWidget) uiObject;
			AreaWidget[] areas = mapWidget.getItems();
			if (areas != null) {
				for (int k = 0; k < areas.length; k++) {
					if (areas[k] == sender) {
						returnObject = sender.getTitle();
						hasSimpleValue = true;
						// The senderId has to be the one of the Map, not of the area.
						String senderId = DOM.getElementProperty(mapWidget.getElement(), "id");
						eventDataObject.setSender(senderId);
					}
				}
			}
		} else if (uiObject instanceof HasDataGridMethods) {
			HasDataGridMethods dataGridSortableTable = (HasDataGridMethods) uiObject;
			// MaxRowSize with the call
			if (dataGridSortableTable.getMaxRows() != null) {
				eventDataObject.getInputVariables().add(new InputVariableGVO(DOM.getElementAttribute(uiObject.getElement(), "id") + ".max_rows", null, dataGridSortableTable.getMaxRows().toString()));
			}
			eventDataObject.getInputVariables().add(new InputVariableGVO(DOM.getElementAttribute(uiObject.getElement(), "id") + ".pagesize", null, "" + dataGridSortableTable.getPageSize()));
		} else if (uiObject instanceof Image) {
			Image img = (Image) uiObject;
			if (img.getUrl() != null) {
				returnObject = img.getUrl();
				hasSimpleValue = true;
			}
		} else if (uiObject instanceof ValueSpinner) {
			ValueSpinner spinner = (ValueSpinner) uiObject;
			if (spinner.getTextBox() != null) {
				returnObject = spinner.getTextBox().getValue();
				hasSimpleValue = true;
			}
		} else if (uiObject instanceof Tiles) {
			Tiles tiles = (Tiles) uiObject;
			DataContainerGVO dtc = new DataContainerGVO();
			dtc.setKind(DataContainerGVO.KIND_MAP);
			DataMap dataMap = new DataMap();
			dtc.setDataMap(dataMap);
			if (eventDataObject.getOriginalSenderId() != null) {
				String index = eventDataObject.getOriginalSenderId().substring(0, eventDataObject.getOriginalSenderId().lastIndexOf(QAMLConstants.TOKEN_INDEXING));
				index = index.replace(QAMLConstants.TOKEN_INDEXING, "");
				Integer i = Integer.parseInt(index);
				UIObject tileElement = tiles.getTileElements().get(i);
				if (tileElement instanceof HasWidgets) {
					HasWidgets hasWidgets = (HasWidgets) tileElement;
						processWidgets(hasWidgets, dataMap, sender, eventDataObject);
				}

			}
			returnObject = dtc;
		} else if(uiObject instanceof SliderBar){
			SliderBar slider = (SliderBar)uiObject;
			if(slider.getCurrentValue() > 0){
				returnObject = String.valueOf(slider.getCurrentValue());
			}
		} else if (isDataGridField(uiObject)) {
			returnObject = getDataGridValue(uiObject, sender, eventDataObject);
		}

		if (hasSimpleValue) {
			String value = null;
			if (returnObject instanceof String) {
				value = (String)returnObject;
			} else if (returnObject instanceof DataContainerGVO) {
				DataContainerGVO gvo = (DataContainerGVO)returnObject;
				if (gvo.getDataString() != null) {
					value = gvo.getDataString();
				} else if (gvo.getDateData() != null) {
					value = gvo.getDateData().toString();
				}
			}
			// Required field check
			handleRequired(uiObject, value);
			// Validation based on type- for textfield with type
			handleTypeValidation(uiObject, value);
		}

		return returnObject;
	}
    // CHECKSTYLE.ON: CyclomaticComplexity

	private static DataContainerGVO convertToDataGVO(Object returnObject) {
		DataContainerGVO dtc = null;
		if (returnObject instanceof DataMap) {
			dtc = new DataContainerGVO();
			dtc.setDataMap((DataMap) returnObject);
			dtc.setKind(DataContainerGVO.KIND_MAP);

		}  else if (returnObject instanceof List) {
			dtc = new DataContainerGVO();
			dtc.setListofDC((List<DataContainerGVO>) returnObject);
			dtc.setKind(DataContainerGVO.KIND_COLLECTION);
		}
		return dtc;
	}

	private static void handleRequired(UIObject uiObject, String value) {
		if (RendererHelper.isRequiredComponent(uiObject)) {
			if(uiObject instanceof ListBox) {
				ListBox listBox = (ListBox) uiObject;
				if(listBox.getSelectedIndex() == 0 ) {
					value = null;
				}
			}
			
			if (value == null || value.trim().length() == 0) {
				 ClientApplicationContext.getInstance().log("Required field not filled in", "Please check the input since a required field is not filled in.(" + RendererHelper.getComponentId(uiObject) + ").", false);
				 String componentFullId =  RendererHelper.getComponentId(uiObject);
				 String componentId = componentFullId;
				 if(componentFullId != null) {
					 componentId = componentFullId.substring(0, componentFullId.indexOf('|'));
				 }
				 String message = null;
				 String title = null;
				 if(uiObject instanceof HasRequiredValidationMessage) {
					 HasRequiredValidationMessage hasRequiredValidationMessage = (HasRequiredValidationMessage) uiObject;
					 message = hasRequiredValidationMessage.getRequiredValidationMessage();
					 title = hasRequiredValidationMessage.getRequiredValidationTitle();
				 }
				 if(message == null){
					 message = QAMLConstants.DEFAULT_REQUIRED_VALIDATION_MESSAGE + componentId;
				 }
				 if(title == null){
					 title = QAMLConstants.DEFAULT_REQUIRED_VALIDATION_TITLE;
				 }
				 throw new RequiredFieldException(title, message);
			}

		}
	}

	private static void handleTypeValidation(UIObject uiObject, String value) {
		String type = null;
		if(uiObject instanceof TextBox ||  uiObject instanceof LabeledTextFieldWidget ) {

			type = RendererHelper.getComponentAttributeValue(uiObject, TextFieldGVO.REGEXPTYPE);

			if(type!= null && type.trim().length() >0 ) {
				if( value!=null && value.length()!=0 && !isValidType(uiObject,value) )  {

					String componentFullId =  RendererHelper.getComponentId(uiObject);
					String componentId = componentFullId;
					if(componentFullId != null) {
						componentId = componentFullId.substring(0, componentFullId.indexOf('|'));
					}

					throw new TypeValidationException(TextFieldGVO.getRegExpMessage(type) + "(" + componentId + ").");
				}
			}
		}
	}


	private static boolean isValidType(UIObject uiObject, String value) {
		boolean valid = false;
		String type = RendererHelper.getComponentAttributeValue(uiObject, TextFieldGVO.REGEXPTYPE);
		String regExp = TextFieldGVO.getRegExp(type);

		if( regExp == null || value == null || value.replaceFirst(regExp, "").length()>0) {
			return false;
		}
		return true;
	}

	private static Object getDataGridValue(UIObject uiObject, UIObject sender, EventDataGVO eventDataObject) {
		String id = DOM.getElementAttribute(sender.getElement(), "id");
		String row = id.substring(0, id.lastIndexOf(QAMLConstants.TOKEN_INDEXING) + QAMLConstants.TOKEN_INDEXING.length());
		row = row.replace(QAMLConstants.TOKEN_INDEXING, "");

		String datagridUUID = id.substring(id.lastIndexOf(QAMLConstants.TOKEN_INDEXING) + QAMLConstants.TOKEN_INDEXING.length());
		;
		String postfix = datagridUUID.substring(datagridUUID.indexOf("|") + 1);
		String prefix = datagridUUID.substring(0, datagridUUID.indexOf("|"));
		if (prefix.contains(".")) {
			prefix = prefix.substring(0, prefix.indexOf("."));
		}
		DataContainerGVO dtc = null;
		List<UIObject> ui = ComponentRepository.getInstance().getComponent(prefix + "|" + postfix);
		if (ui != null) {
			for (UIObject u : ui) {
				if (u instanceof QPagingScrollTable) {
					QPagingScrollTable qps = (QPagingScrollTable) u;
					dtc = qps.getRowValue(Integer.valueOf(row));

				}
			}
		}

		return dtc;
	}

	private static boolean isDataGridField(UIObject uiObject) {
		String id = DOM.getElementAttribute(uiObject.getElement(), "id");
		return (id != null && id.startsWith(QAMLConstants.TOKEN_INDEXING));
	}
	
	
	
}
