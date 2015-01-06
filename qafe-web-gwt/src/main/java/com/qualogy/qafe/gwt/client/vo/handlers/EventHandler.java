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
package com.qualogy.qafe.gwt.client.vo.handlers;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Stack;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.UIObject;
import com.qualogy.qafe.gwt.client.context.ClientApplicationContext;
import com.qualogy.qafe.gwt.client.service.RPCService;
import com.qualogy.qafe.gwt.client.service.RPCServiceAsync;
import com.qualogy.qafe.gwt.client.storage.DataStorage;
import com.qualogy.qafe.gwt.client.ui.renderer.RendererHelper;
import com.qualogy.qafe.gwt.client.vo.functions.BuiltInFunctionGVO;
import com.qualogy.qafe.gwt.client.vo.functions.BusinessActionRefGVO;
import com.qualogy.qafe.gwt.client.vo.functions.ClosePanelGVO;
import com.qualogy.qafe.gwt.client.vo.functions.CloseWindowGVO;
import com.qualogy.qafe.gwt.client.vo.functions.EventGVO;
import com.qualogy.qafe.gwt.client.vo.functions.EventRefGVO;
import com.qualogy.qafe.gwt.client.vo.functions.FocusGVO;
import com.qualogy.qafe.gwt.client.vo.functions.LocalStoreGVO;
import com.qualogy.qafe.gwt.client.vo.functions.LogFunctionGVO;
import com.qualogy.qafe.gwt.client.vo.functions.OpenWindowGVO;
import com.qualogy.qafe.gwt.client.vo.functions.SetPanelGVO;
import com.qualogy.qafe.gwt.client.vo.functions.SetValueGVO;
import com.qualogy.qafe.gwt.client.vo.functions.ShowPanelGVO;
import com.qualogy.qafe.gwt.client.vo.functions.StoreClearGVO;
import com.qualogy.qafe.gwt.client.vo.functions.ToggleGVO;
import com.qualogy.qafe.gwt.client.vo.functions.dialog.GenericDialogGVO;
import com.qualogy.qafe.gwt.client.vo.ui.UIGVO;
import com.qualogy.qafe.gwt.client.vo.ui.event.EventListenerGVO;

public class EventHandler {

    private static EventHandler instance = null;
    
    private RPCServiceAsync rpcService = null;

    private Map<String,Stack<Queue>> eventMap = new HashMap<String, Stack<Queue>>();
    
    private Map<String, Map<String, Object>> processingBuiltInsMap = new HashMap<String, Map<String, Object>>();
    
    private final Map<String, BuiltInHandler> BUILTIN_MAP = new HashMap<String, BuiltInHandler>();

    private EventHandler() {
        BUILTIN_MAP.put(OpenWindowGVO.CLASS_NAME, new OpenWindowHandler());
        BUILTIN_MAP.put(LocalStoreGVO.CLASS_NAME, new LocalStoreHandler());
        BUILTIN_MAP.put(StoreClearGVO.CLASS_NAME, new StoreClearHandler());
        BUILTIN_MAP.put(BusinessActionRefGVO.CLASS_NAME, new BusinessActionRefHandler());
        BUILTIN_MAP.put(GenericDialogGVO.CLASS_NAME, new GenericDialogHandler());
        BUILTIN_MAP.put(SetValueGVO.CLASS_NAME, new SetValueHandler());
        BUILTIN_MAP.put(SetPanelGVO.CLASS_NAME, new SetPanelHandler());
        BUILTIN_MAP.put(ShowPanelGVO.CLASS_NAME, new ShowPanelHandler());
        BUILTIN_MAP.put(ClosePanelGVO.CLASS_NAME, new ClosePanelHandler());
        BUILTIN_MAP.put(ToggleGVO.CLASS_NAME, new ToggleHandler());
        BUILTIN_MAP.put(LogFunctionGVO.CLASS_NAME, new LogHandler());
        BUILTIN_MAP.put(CloseWindowGVO.CLASS_NAME, new CloseWindowHandler());
        BUILTIN_MAP.put(EventRefGVO.CLASS_NAME, new EventRefHandler());
        BUILTIN_MAP.put(FocusGVO.CLASS_NAME, new FocusHandler());
    }

    public static EventHandler getInstance() {
        if (instance == null) {
            instance = new EventHandler();
        }
        return instance;
    }

    public RPCServiceAsync getRPCService() {
    	if (rpcService == null) {
    		rpcService = (RPCServiceAsync) GWT.create(RPCService.class);
    		ServiceDefTarget endpoint = (ServiceDefTarget) rpcService;
            String moduleRelativeURL = GWT.getModuleBaseURL() + "rpc.service";
            endpoint.setServiceEntryPoint(moduleRelativeURL);
    	}
    	return rpcService;
    }
    
    public void handleEvent(final UIObject sender, String listenerType, EventListenerGVO eventListenerGVO, Map<String, String> mouseInfo) {
        String appId = getAppId(sender);
        String windowId = getWindowId(sender);
        UIGVO applicationGVO = getApplication(appId);
        if (applicationGVO == null) {
            return;
        }
        ClientApplicationContext.getInstance().setPrerenderingUIVO(appId, applicationGVO);

        String eventId = eventListenerGVO.getEventId();
        EventGVO eventGVO = getEvent(eventId, windowId, applicationGVO);
        if (eventGVO == null) {
            return;
        }

        final String eventSessionId = register();
        Stack<Queue> builtInsStack = new Stack<Queue>();
        Queue builtIns = getBuiltIns(eventGVO);
        builtInsStack.add(builtIns); 
        eventMap.put(eventSessionId, builtInsStack);
        handleEvent(eventSessionId, sender, listenerType, mouseInfo, appId, windowId);
    }
    
	public void handleEvent(final String eventSessionId, final UIObject sender, final String listenerType
    		, final  Map<String, String> mouseInfo, final String appId, final String windowId) {
    	Stack<Queue> builtInsStack = eventMap.get(eventSessionId);
		if ((builtInsStack == null) || builtInsStack.isEmpty()) {
			cleanup(eventSessionId);
			return;
		}
		Queue builtIns = builtInsStack.peek();
		while (!builtIns.isEmpty()) {
			Object builtIn = builtIns.poll();
			try {
				// Meant for logging the execution of built-ins
				Map<String, Object> processingBuiltIns = processingBuiltInsMap.get(eventSessionId);
				if (processingBuiltIns == null) {
					processingBuiltIns = new HashMap<String, Object>();
					processingBuiltInsMap.put(eventSessionId, processingBuiltIns);
				}
				String keyOfBuiltIn = null;
				if (builtIn instanceof String) {
					keyOfBuiltIn = (String) builtIn;
					builtIn = processingBuiltIns.remove(keyOfBuiltIn);
					BuiltInFunctionGVO builtInGVO = (BuiltInFunctionGVO) builtIn;
					log("Exiting", sender, listenerType, mouseInfo, builtInGVO, appId, windowId);
					continue;
				}
				if (builtIn instanceof BuiltInFunctionGVO) {
					keyOfBuiltIn = builtIn.toString();
					if (!processingBuiltIns.containsKey(keyOfBuiltIn)) {
						BuiltInFunctionGVO builtInGVO = (BuiltInFunctionGVO) builtIn;
						log("Entering", sender, listenerType, mouseInfo, builtInGVO, appId, windowId);
						processingBuiltIns.put(keyOfBuiltIn, builtIn);
						((LinkedList)builtIns).addFirst(keyOfBuiltIn);						
					}
				}
				
				Queue derivedBuiltIns = new LinkedList();
				BuiltInState state = handleBuiltIn(builtIn, sender, listenerType, mouseInfo
						, appId, windowId, eventSessionId, derivedBuiltIns);
				switch (state) {
					case SUSPEND: {
						/*
						 * NOTE:
						 * The callback is handled in the superclass of all built-in handlers,
						 * call handleEvent(eventSessionId, ...) after processing the corresponding built-in in the callback
						 */
						return;
					}
					case REPEAT: {
						((LinkedList)builtIns).addFirst(builtIn);
					} break;
				}
				if (!derivedBuiltIns.isEmpty()) {
					// Postpone current processing and process the new top of the stack
					builtInsStack.add(derivedBuiltIns);
					break;
				}
			} catch (Exception e) {
				handleException(e, builtIn, sender, listenerType, mouseInfo, appId, windowId, eventSessionId);
				return;
			}
		}
		if (builtIns.isEmpty()) {
			builtInsStack.remove(builtIns);
		}
		handleEvent(eventSessionId, sender, listenerType, mouseInfo, appId, windowId);
    }

    private BuiltInState handleBuiltIn(Object builtIn, final UIObject sender, final String listenerType
    		, final Map<String, String> mouseInfo, final String appId, final String windowId
    		, final String eventSessionId, Queue derivedBuiltIns) {
    	BuiltInFunctionGVO builtInGVO = null;
    	if (builtIn instanceof BuiltInFunctionGVO) {
    		builtInGVO = (BuiltInFunctionGVO) builtIn;
    	}
    	if (builtInGVO == null) {
    		return null;
    	}
        BuiltInHandler builtInHandler = BUILTIN_MAP.get(builtInGVO.getClassName());
        if (builtInHandler == null) {
            return null;
        }
        return builtInHandler.handleBuiltIn(sender, listenerType, mouseInfo, builtInGVO, appId, windowId
        		, eventSessionId, derivedBuiltIns);
    }
    
    public void handleException(final Throwable exception, final Object currentBuiltIn
    		, final UIObject sender, final String listenerType, final Map<String, String> mouseInfo
    		, final String appId, final String windowId, final String eventSessionId) {
    	String title = "Fail to execute " + currentBuiltIn;
        String message = exception.getMessage();
        showMessage(title, message, exception);
	}
    
    public UIGVO getApplication(final String appId) {
    	 return ClientApplicationContext.getInstance().getApplication(appId);
    }
    
    public EventGVO getEvent(final String eventId, final String windowId, UIGVO applicationGVO) {
        EventGVO eventGVO = null;
        if (applicationGVO == null) {
            return eventGVO;
        }
        eventGVO = applicationGVO.getEvent(eventId);
        if (eventGVO == null) {
            eventGVO = applicationGVO.getEvent(windowId + eventId);
        }
        return eventGVO;
    }
    
    public Queue getBuiltIns(EventGVO eventGVO) {
    	Queue builtIns = new LinkedList();
    	if (eventGVO != null) {
    		builtIns.addAll(eventGVO.getEventItems());
    	}
		return builtIns;
	}
    
    public void setBusy(boolean busy) {
    	ClientApplicationContext.getInstance().setBusy(busy);
    }
    
    public void showMessage(String title, String message, Throwable exception) {
    	 ClientApplicationContext.getInstance().log(title, message, true, false, exception);
    }
    
    public void log(String action, UIObject sender, String listenerType, Map<String, String> mouseInfo
			, BuiltInFunctionGVO builtInGVO, String appId, String windowId) {
    	StringBuffer logMessage = new StringBuffer();
    	logMessage.append(action + " Built-In [" + builtInGVO + "]");
    	logMessage.append(": Sender=" + sender + " -- ListenerType=" + listenerType);
    	logMessage.append(" -- MouseInfo=" + mouseInfo);
    	logMessage.append(" -- AppId=" + appId + " -- WindowId=" + windowId);
        log(logMessage.toString());
    }
    
    public void log(String message) {
        ClientApplicationContext.getInstance().log(message);
    }
    
    public DataStorage getDataStorage() {
        return ClientApplicationContext.getInstance().getDataStorage();
    }
    
    private String getAppId(final UIObject sender) {
        return RendererHelper.getComponentContext(sender);
    }

    private String getWindowId(final UIObject sender) {
        return RendererHelper.getParentComponent(sender);
    }
    
    private void cleanup(String eventSessionId) {
		unregister(eventSessionId);
    	eventMap.remove(eventSessionId);
    	processingBuiltInsMap.remove(eventSessionId);
	}

    private String register() {
        return getDataStorage().register();
    }

    private void unregister(final String dataId) {
        getDataStorage().unregister(dataId);
    }
}