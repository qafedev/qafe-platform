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
package com.qualogy.qafe.mgwt.server.processor.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.StopWatch;

import com.qualogy.qafe.bind.core.application.ApplicationContext;
import com.qualogy.qafe.bind.core.application.ApplicationIdentifier;
import com.qualogy.qafe.bind.presentation.event.EventItem;
import com.qualogy.qafe.bind.presentation.event.InputVariable;
import com.qualogy.qafe.bind.presentation.event.function.BuiltInFunction;
import com.qualogy.qafe.core.application.ApplicationCluster;
import com.qualogy.qafe.core.datastore.ApplicationStoreIdentifier;
import com.qualogy.qafe.core.errorhandling.ExternalException;
import com.qualogy.qafe.core.framework.presentation.EventDataContainer;
import com.qualogy.qafe.core.framework.presentation.EventHandler;
import com.qualogy.qafe.mgwt.client.vo.data.EventDataGVO;
import com.qualogy.qafe.mgwt.client.vo.data.GDataObject;
import com.qualogy.qafe.mgwt.client.vo.functions.BuiltInFunctionGVO;
import com.qualogy.qafe.mgwt.client.vo.functions.DataContainerGVO;
import com.qualogy.qafe.mgwt.client.vo.ui.event.InputVariableGVO;
import com.qualogy.qafe.mgwt.server.event.assembler.EventUIAssembler;
import com.qualogy.qafe.mgwt.server.processor.EventProcessor;
import com.qualogy.qafe.presentation.EventDataContainerImpl;
import com.qualogy.qafe.presentation.EventDataObject;
import com.qualogy.qafe.util.ExceptionHelper;
import com.qualogy.qafe.web.util.SessionContainer;

public class EventProcessorImpl implements EventProcessor {

	// EventHandler eventHandler = ApplicationContext.
	// EventHandlerFactory.create();
	public final static Logger logger = Logger.getLogger(EventProcessorImpl.class.getName());

	public GDataObject execute(EventDataGVO eventData, ApplicationIdentifier appId, SessionContainer ss) {

		ApplicationContext context = ApplicationCluster.getInstance().get(appId);
		EventHandler eventHandler = context.getEventHandler();

		StopWatch stopWatch = new StopWatch();
		stopWatch.start();

		GDataObject data = new GDataObject();
		data.setSenderId(eventData.getSender());
		data.setListenerType(eventData.getListenerType());
		data.setEventId(eventData.getEventId());
		data.setUuid(eventData.getUuid());
		data.setParent(eventData.getParent());
		ApplicationStoreIdentifier appStoreId = new ApplicationStoreIdentifier(eventData.getUuid());
		String[] senderInfo = StringUtils.split(eventData.getSender(), '|');
		logger.fine(eventData.toString() + ", WindowID " + senderInfo[1]);

		String windowId = "";
		// if (senderInfo.length==2 || senderInfo.length==3){
		if (senderInfo.length == 2 || senderInfo.length == 3) {
			if (senderInfo.length == 2) {// dealing with a RootPanel only
				windowId = senderInfo[0];
			}
			if (senderInfo.length == 3) {

				windowId = senderInfo[1];
			}
			try {

				List<InputVariable> inputVariables = new ArrayList<InputVariable>();

				if (eventData.getInputVariables() != null) {
					for (InputVariableGVO inVar : eventData.getInputVariables()) {
						inputVariables.add(new InputVariable(inVar.getName(), inVar.getReference(), inVar.getDefaultValue(), inVar.getComponentValue(), convert(inVar.getDataContainerObject())));
					}
				}

				EventDataObject eventDataObject = new EventDataObject(eventData.getEventId(), eventData.getSourceInfo(), appId, eventData.getListenerType(), inputVariables, eventData.getInternalVariables(), senderInfo[0], eventData.getSenderName(), windowId, eventData.getUuid(), eventData.getUserUID(),eventData.getWindowSession(), eventData.getRequest(),eventData.getParameters(),eventData.getMouse());
				/*
				 * EventDataContainer edci =
				 * convert(eventData.getDataContainer()); if (edci != null) {
				 * eventDataObject.getInputVariables().add(edci); }
				 */
				eventDataObject.setApplicationStoreIdentifier(appStoreId);

				// Collection builtInFunctionsToExecute
				// =eventHandler.manage(senderInfo[0],
				// eventId,windowId,params,appId,event);
				Collection<?> builtInFunctionsToExecute = eventHandler.manage(eventDataObject);
				Iterator<?> itr = builtInFunctionsToExecute.iterator();
				Collection<BuiltInFunctionGVO> builtInFunctions = new ArrayList<BuiltInFunctionGVO>();
				while (itr.hasNext()) {
					BuiltInFunction builtInFunctionToExecute = (BuiltInFunction) itr.next();
					BuiltInFunctionGVO builtInFunction = EventUIAssembler.convert((EventItem) builtInFunctionToExecute, eventData, context,ss);
					if (builtInFunction != null) {
						builtInFunctions.add(builtInFunction);	
					}
				}
				data.setFunctions((BuiltInFunctionGVO[]) builtInFunctions.toArray(new BuiltInFunctionGVO[] {}));
			} catch (RuntimeException e) {
				ExceptionHelper.printStackTrace(e);
				throw e;

			} catch (ExternalException e) {
				ExceptionHelper.printStackTrace(e.getCause());
				throw new RuntimeException(e.getMessage(), e);
			}
		} else {
			logger.warning("Error in sender string (is not partionable in size 2) : " + eventData.getSender());
		}
		stopWatch.stop();

		logger.warning("EventProcessorImpl uuid:[" + eventData.getUuid() + "], sender[" + eventData.getSender() + "], eventId [" + eventData.getEventName() + "]  took  [" + stopWatch.getTime() + "]ms ");
		return data;
	}

	private EventDataContainer convert(DataContainerGVO dataContainer) {
		EventDataContainerImpl edci = null;
		if (dataContainer != null) {
			edci = new EventDataContainerImpl();
			edci.setParameterName(dataContainer.getParameterName());
			edci.setDataObject(dataContainer.createType());
			
			// need to handle
			//dataContainer.getDataMap()
			//dataContainer.getListofDC()
				
			
//			/*if (dataContainer.getKind() == DataContainerGVO.KIND_STRING) {
//				edci.setDataObject(dataContainer.createType());
//			} else if (dataContainer.getKind() == DataContainerGVO.KIND_MAP) {
//				DataMap dataMap = dataContainer.getDataMap();
//
//				if (dataMap != null) {
//					Map<String, Object> data = new HashMap<String, Object>();
//					processDataMap(data, dataMap);				
//					edci.setDataObject(data);
//				}
//			} else if (dataContainer.getKind() == DataContainerGVO.KIND_COLLECTION_OF_MAP) {
//				List<DataMap> list = dataContainer.getListOfDataMap();
//				if (list != null) {
//					List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
//					for (DataMap dataMap : list) {
//						Map<String, Object> convertedData = new HashMap<String, Object>();						
//						processDataMap(convertedData, dataMap);											
//						data.add(convertedData);
//					}
//					edci.setDataObject(data);
//				}
//			} else if (dataContainer.getKind() == DataContainerGVO.KIND_COLLECTION_OF_STRING) {
//				edci.setDataObject(dataContainer.getListOfString());
//			}*/
		}
		return edci;
	}

//	private void processDataMap(Map<String, Object> data, DataMap dataMap) {
//		if (dataMap != null && data != null) {
//			for (Entry<String, DataContainerGVO> entry : dataMap.entrySet()) {
//				DataContainerGVO vo = entry.getValue();
//				Object o = convertDataContainerGVO(vo);
//				data.put(entry.getKey(), o);
//			}
//		}
//	}

//	private Object convertDataContainerGVO(DataContainerGVO vo) {
//		Object o = null;
//		if (vo != null) {
//			switch (vo.getKind()) {
//			case DataContainerGVO.KIND_STRING:
//				o = vo.createType();
//				break;
//			case DataContainerGVO.TYPE_DATE:
//				o = vo.createType();
//				break;
//			case DataContainerGVO.KIND_MAP:
//				if (vo.getDataMap() != null) {
//					Map<String, Object> map = new HashMap<String, Object>();
//					o = map;
//					for (Entry<String,DataContainerGVO > entry : vo.getDataMap().entrySet()) {
//						map.put(entry.getKey(), convertDataContainerGVO(entry.getValue()));
//					}
//				}
//
//			case DataContainerGVO.KIND_COLLECTION_OF_STRING:
//				if (vo.getListOfString() != null) {
//					List<String> l = new ArrayList<String>();
//					o = l;
//					for (String v : vo.getListOfString()) {
//						l.add(v);
//					}
//				}
//				break;
//
//			case DataContainerGVO.KIND_COLLECTION_OF_MAP:
//				if (vo.getListOfDataMap() != null) {
//					List<Map<String, Object>> l = new ArrayList<Map<String, Object>>();
//					o = l;
//					for (DataMap m : vo.getListOfDataMap()) {
//						Map<String, Object> map = new HashMap<String, Object>();
//						//o = map;
//						for (Entry<String,DataContainerGVO> entry : m.entrySet()) {
//							map.put(entry.getKey(), convertDataContainerGVO(entry.getValue()));
//						}
//						l.add(map);
//					}
//				}
//				break;
//			default:
//				break;
//
//			}
//
//		}
//		return o;
//	}

}
