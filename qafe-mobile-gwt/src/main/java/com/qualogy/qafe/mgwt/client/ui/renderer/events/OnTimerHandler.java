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
package com.qualogy.qafe.mgwt.client.ui.renderer.events;

import java.util.List;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.UIObject;
import com.qualogy.qafe.mgwt.client.context.ClientApplicationContext;
import com.qualogy.qafe.mgwt.client.vo.ui.ComponentGVO;
import com.qualogy.qafe.mgwt.client.vo.ui.event.EventListenerGVO;
import com.qualogy.qafe.mgwt.client.vo.ui.event.InputVariableGVO;
import com.qualogy.qafe.mgwt.client.vo.ui.event.ParameterGVO;

public class OnTimerHandler implements WindowCloseHandler{
	
	private static final String PARAM_TIME_OUT = "time-out";
	private static final String PARAM_REPEAT = "repeat";
	
	private Timer timer;
	
	public void processOnTimer(UIObject ui, ComponentGVO vo, String appId, EventListenerGVO event, List<InputVariableGVO> inputVariables) {
		List<ParameterGVO> parameterList = event.getParameterList();
		if(parameterList != null) {
			int timeOutValue = -1;
			int repeat = -1;
			for(ParameterGVO parameter: parameterList) {
				if(parameter != null) {
					String paramName = parameter.getName();
					if(paramName != null) {
						if(paramName.equals(PARAM_TIME_OUT)) {
							try {
								timeOutValue = Integer.parseInt(parameter.getValue());
							}catch(Exception e) {
								ClientApplicationContext.getInstance().log("Listener param " + PARAM_TIME_OUT + "should be an integer", e);
							}									
						} else if(paramName.equals(PARAM_REPEAT)) {
							try {
								repeat = Integer.parseInt(parameter.getValue());
							}catch(Exception e) {
								ClientApplicationContext.getInstance().log("Listener param " + PARAM_REPEAT + "should be an integer", e);
							}
						}
					}
				}
			}
			
			if(timeOutValue > -1) {
				timer = EventFactory.createTimerListener(ui, event, inputVariables); 
				if(repeat == 0) {
					timer.scheduleRepeating(timeOutValue);
				} else {
					timer.schedule(timeOutValue);
				}
			}
			
			registerWindowCloseHandler(vo, appId, event);
			
		}
	}

	private void registerWindowCloseHandler(ComponentGVO vo, String appId, EventListenerGVO event) {
		String windowId = null;
		if(vo != null) {
			windowId = vo.getWindow();
		}			
	//	ClientApplicationContext.getInstance().addWindowCloseHandler(appId, windowId, event.getEventId(), this);
	}

	public void onWindowClose(String appId, String windowId) {
		if(timer != null) {
			timer.cancel();
		}
	}
		
}
