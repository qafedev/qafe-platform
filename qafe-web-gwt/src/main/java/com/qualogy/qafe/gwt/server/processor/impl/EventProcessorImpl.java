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
package com.qualogy.qafe.gwt.server.processor.impl;

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
import com.qualogy.qafe.gwt.client.vo.data.EventDataGVO;
import com.qualogy.qafe.gwt.client.vo.data.GDataObject;
import com.qualogy.qafe.gwt.client.vo.functions.BuiltInFunctionGVO;
import com.qualogy.qafe.gwt.client.vo.functions.DataContainerGVO;
import com.qualogy.qafe.gwt.client.vo.functions.SetRestrictionGVO;
import com.qualogy.qafe.gwt.client.vo.ui.event.InputVariableGVO;
import com.qualogy.qafe.gwt.server.event.assembler.EventUIAssembler;
import com.qualogy.qafe.gwt.server.processor.EventProcessor;
import com.qualogy.qafe.presentation.EventDataContainerImpl;
import com.qualogy.qafe.presentation.EventDataObject;
import com.qualogy.qafe.util.ExceptionHelper;
import com.qualogy.qafe.web.util.SessionContainer;

public class EventProcessorImpl implements EventProcessor {

	public final static Logger logger = Logger.getLogger(EventProcessorImpl.class.getName());

	public GDataObject execute(EventDataGVO eventData, ApplicationIdentifier appId, SessionContainer sessionContainer) {

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
		if (senderInfo.length == 2 || senderInfo.length == 3) {
			if (senderInfo.length == 2) {// dealing with a RootPanel only
				windowId = senderInfo[0];
			}
			if (senderInfo.length == 3) {

				windowId = senderInfo[1];
			}
			try {

				List<InputVariable> inputVariables = new ArrayList<InputVariable>();

				if (eventData.getInputVariables() != null && eventData.getInputVariables().size() > 0) {
					for (InputVariableGVO inVar : eventData.getInputVariables()) {
						inputVariables.add(new InputVariable(inVar.getName(), inVar.getReference(), inVar.getDefaultValue(), inVar.getComponentValue(), convert(inVar.getDataContainerObject())));
					}
				}

				EventDataObject eventDataObject = new EventDataObject(eventData.getEventId(), eventData.getSourceInfo(), appId, eventData.getListenerType(), inputVariables, eventData.getInternalVariables(), senderInfo[0], eventData.getSenderName(), windowId, eventData.getUuid(), eventData.getUserUID(),eventData.getWindowSession(), eventData.getRequest(),eventData.getParameters(),eventData.getMouse());
				eventDataObject.setApplicationStoreIdentifier(appStoreId);

				Collection<?> builtInFunctionsToExecute = eventHandler.manage(eventDataObject);
				Iterator<?> itr = builtInFunctionsToExecute.iterator();
				Collection<BuiltInFunctionGVO> builtInFunctions = new ArrayList<BuiltInFunctionGVO>();
				while (itr.hasNext()) {
					BuiltInFunction builtInFunctionToExecute = (BuiltInFunction) itr.next();
					BuiltInFunctionGVO builtInFunction = EventUIAssembler.convert((EventItem) builtInFunctionToExecute, eventData, context, sessionContainer);
					if (builtInFunction != null) {
						if (builtInFunction instanceof SetRestrictionGVO) {
							// Authorization rules should be placed prior to OpenWindow,
							// so the rules can be added before opening the window
							// Note: before, the order of execution is OpenWindow -> SetRestriction,
							// because of the change in the method MainFactoryActions.getUIByUUID()
							// the window will not be created immediately if it is not created before
							// then this will result in timing issue
							((List)builtInFunctions).add(0, builtInFunction);
						} else {
							builtInFunctions.add(builtInFunction);	
						}
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
			
		}
		return edci;
	}
}
