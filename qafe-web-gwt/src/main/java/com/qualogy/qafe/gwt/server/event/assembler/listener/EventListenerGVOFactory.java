/**
 * Copyright 2008-2016 Qualogy Solutions B.V.
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
package com.qualogy.qafe.gwt.server.event.assembler.listener;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.qualogy.qafe.bind.presentation.event.Component;
import com.qualogy.qafe.bind.presentation.event.Event;
import com.qualogy.qafe.bind.presentation.event.InputVariable;
import com.qualogy.qafe.bind.presentation.event.Listener;
import com.qualogy.qafe.bind.presentation.event.ListenerParameter;
import com.qualogy.qafe.gwt.client.vo.data.EventDataI;
import com.qualogy.qafe.gwt.client.vo.ui.event.ClickEventListenerGVO;
import com.qualogy.qafe.gwt.client.vo.ui.event.DoubleClickEventListenerGVO;
import com.qualogy.qafe.gwt.client.vo.ui.event.EventListenerGVO;
import com.qualogy.qafe.gwt.client.vo.ui.event.InputVariableGVO;
import com.qualogy.qafe.gwt.client.vo.ui.event.OnChangeEventListenerGVO;
import com.qualogy.qafe.gwt.client.vo.ui.event.OnEnterEventListenerGVO;
import com.qualogy.qafe.gwt.client.vo.ui.event.OnExitEventListenerGVO;
import com.qualogy.qafe.gwt.client.vo.ui.event.OnFetchDataEventListenerGVO;
import com.qualogy.qafe.gwt.client.vo.ui.event.OnFinishEventListenerGVO;
import com.qualogy.qafe.gwt.client.vo.ui.event.OnFocusEventListenerGVO;
import com.qualogy.qafe.gwt.client.vo.ui.event.OnKeyDownEventListenerGVO;
import com.qualogy.qafe.gwt.client.vo.ui.event.OnKeyPressEventListenerGVO;
import com.qualogy.qafe.gwt.client.vo.ui.event.OnKeyUpEventListenerGVO;
import com.qualogy.qafe.gwt.client.vo.ui.event.OnLoadEventListenerGVO;
import com.qualogy.qafe.gwt.client.vo.ui.event.OnMouseDownEventListenerGVO;
import com.qualogy.qafe.gwt.client.vo.ui.event.OnMouseEnterEventListenerGVO;
import com.qualogy.qafe.gwt.client.vo.ui.event.OnMouseExitEventListenerGVO;
import com.qualogy.qafe.gwt.client.vo.ui.event.OnMouseMoveEventListenerGVO;
import com.qualogy.qafe.gwt.client.vo.ui.event.OnMouseUpEventListenerGVO;
import com.qualogy.qafe.gwt.client.vo.ui.event.OnScrollEventListenerGVO;
import com.qualogy.qafe.gwt.client.vo.ui.event.OnTimerEventListenerGVO;
import com.qualogy.qafe.gwt.client.vo.ui.event.OnUnLoadEventListenerGVO;
import com.qualogy.qafe.gwt.client.vo.ui.event.ParameterGVO;

public class EventListenerGVOFactory {


	public final static Logger logger = Logger.getLogger(EventListenerGVOFactory.class.getName());
	private EventListenerGVOFactory() {

	}

	public static EventListenerGVO createEventListenerGVO(Listener listener,Event event) {
		return createEventListenerGVO(listener, null, event);
	}

    // CHECKSTYLE.OFF: CyclomaticComplexity
	public static EventListenerGVO createEventListenerGVO(Listener listener, Component eventComponent, Event event) {
		List<ParameterGVO> parameters = createParameters(listener);
		List<InputVariableGVO> inputVariables = createInputVariables(event);
		Map<String,String> sourceInfo = createSourceInfo(event);
		EventListenerGVO eventListenerGVO = null;

		String eventListenerType = null;
		if (listener != null) {
			eventListenerType = listener.getType();
		}
		String eventComponentId = null;
		if (eventComponent != null) {
			eventComponentId = eventComponent.getComponentId();
		}

//		if (Listener.EVENT_ONSUBMIT.equals(listener.getType())) {
//			eventListenerGVO = new OnSubmitEventListenerGVO(event.getId(),sourceInfo,inputVariables,parameters);
//		} else
		if (Listener.EVENT_ONCLICK.equals(listener.getType())) {
			eventListenerGVO = new ClickEventListenerGVO(event.getId(), sourceInfo, inputVariables, parameters, eventListenerType, eventComponentId);
		} else if (Listener.EVENT_ONDBLCLICK.equals(listener.getType())){
			eventListenerGVO = new DoubleClickEventListenerGVO(event.getId(), sourceInfo, inputVariables, parameters, eventListenerType, eventComponentId);
		} else if (Listener.EVENT_ONEXIT.equals(listener.getType())) {
			eventListenerGVO = new OnExitEventListenerGVO(event.getId(),sourceInfo,inputVariables,parameters);
		} else if (Listener.EVENT_ONFOCUS.equals(listener.getType())) {
			eventListenerGVO = new OnFocusEventListenerGVO(event.getId(),sourceInfo,inputVariables,parameters);
		} else if (Listener.EVENT_ONCHANGE.equals(listener.getType())) {
			eventListenerGVO = new OnChangeEventListenerGVO(event.getId(),sourceInfo,inputVariables,parameters);
		} else if (Listener.EVENT_ON_MOUSE_ENTER.equals(listener.getType())){
			eventListenerGVO  = new OnMouseEnterEventListenerGVO(event.getId(),sourceInfo,inputVariables,parameters);
		} else if (Listener.EVENT_ON_MOUSE_EXIT.equals(listener.getType())){
			eventListenerGVO  = new OnMouseExitEventListenerGVO(event.getId(),sourceInfo,inputVariables,parameters);
		} else if (Listener.EVENT_ON_MOUSE_MOVE.equals(listener.getType())){
			eventListenerGVO  = new OnMouseMoveEventListenerGVO(event.getId(),sourceInfo,inputVariables,parameters);
		} else if (Listener.EVENT_ON_MOUSE_DOWN.equals(listener.getType())){
			eventListenerGVO  = new OnMouseDownEventListenerGVO(event.getId(),sourceInfo,inputVariables,parameters);
		} else if (Listener.EVENT_ON_MOUSE_UP.equals(listener.getType())){
			eventListenerGVO  = new OnMouseUpEventListenerGVO(event.getId(),sourceInfo,inputVariables,parameters);
		}else if (Listener.EVENT_ONKEYPRESS.equals(listener.getType())){
			eventListenerGVO = new OnKeyPressEventListenerGVO(event.getId(),sourceInfo,inputVariables,parameters);
		} else if (Listener.EVENT_ONKEYDOWN.equals(listener.getType())){
			eventListenerGVO = new OnKeyDownEventListenerGVO(event.getId(),sourceInfo,inputVariables,parameters);
		} else if (Listener.EVENT_ONKEYUP.equals(listener.getType())){
			eventListenerGVO = new OnKeyUpEventListenerGVO(event.getId(),sourceInfo,inputVariables,parameters);
		} else if (Listener.EVENT_ONLOAD.equals(listener.getType())){
			eventListenerGVO = new OnLoadEventListenerGVO(event.getId(),sourceInfo,inputVariables,parameters);
		} else if (Listener.EVENT_ONUNLOAD.equals(listener.getType())) {
			eventListenerGVO = new OnUnLoadEventListenerGVO(event.getId(),sourceInfo,inputVariables,parameters);
		} else if (Listener.EVENT_ONENTER.equals(listener.getType())) {
			eventListenerGVO = new OnEnterEventListenerGVO(event.getId(),sourceInfo,inputVariables,parameters);
		} else if (Listener.EVENT_ONFINISH.equals(listener.getType())) {
			eventListenerGVO = new OnFinishEventListenerGVO(event.getId(),sourceInfo,inputVariables,parameters);
		} else if (Listener.EVENT_ONFETCHDATA.equals(listener.getType())) {
			eventListenerGVO = new OnFetchDataEventListenerGVO(event.getId(), sourceInfo, inputVariables, parameters, eventListenerType, eventComponentId);
		} else if (Listener.EVENT_ONSCROLL_TOP.equals(listener.getType())) {
			eventListenerGVO = new OnScrollEventListenerGVO(event.getId(), sourceInfo, inputVariables, parameters, eventListenerType, eventComponentId);
		} else if (Listener.EVENT_ONSCROLL_BOTTOM.equals(listener.getType())) {
			eventListenerGVO = new OnScrollEventListenerGVO(event.getId(), sourceInfo, inputVariables, parameters, eventListenerType, eventComponentId);
		} else if (Listener.EVENT_ONTIMER.equals(listener.getType())) {
			eventListenerGVO = new OnTimerEventListenerGVO(event.getId(), sourceInfo, inputVariables, parameters, eventListenerType, eventComponentId);
		} else {

			logger.warning("listener type " + listener.getType()+ "  needs to implemented");
		}
		return eventListenerGVO;
	}
    // CHECKSTYLE.ON: CyclomaticComplexity

	private static List<InputVariableGVO> createInputVariables(Event event) {
		List<InputVariableGVO> inputVariables  = new ArrayList<InputVariableGVO>();
		if (event!=null && event.getInput()!=null ){
			for (InputVariable inputVariable: event.getInput()) {
				inputVariables.add(new InputVariableGVO(inputVariable.getName(),inputVariable.getReference(),inputVariable.getDefaultValue()));
			}
		}

		return inputVariables;
	}

	private static List<ParameterGVO> createParameters(Listener listener) {
		List<ParameterGVO> parameters = new ArrayList<ParameterGVO>();
		if (listener!=null){
			if (listener.getParameters()!=null){
				int index=0;
				for (ListenerParameter listenerParameter: listener.getParameters()) {
						parameters.add(new ParameterGVO(listenerParameter.getName(),listenerParameter.getValue()));
						index++;
				}
			}
		}
		return parameters;
	}

	private static Map<String,String> createSourceInfo(Event event){
		Map<String,String> sourceInfo = new HashMap<String,String>();
		if (event.getSourceId()!=null){
			sourceInfo.put(EventDataI.SOURCE_ID, event.getSourceId());
		}

		if (event.getSourceName()!=null){
			sourceInfo.put(EventDataI.SOURCE_NAME, event.getSourceName());
		}

		if (event.getSourceValue()!=null){
			sourceInfo.put(EventDataI.SOURCE_VALUE, event.getSourceValue());
		}

		if (event.getSourceListenerType()!=null){
			sourceInfo.put(EventDataI.SOURCE_LISTENER_TYPE, event.getSourceListenerType());
		}

		return sourceInfo;
	}
}
