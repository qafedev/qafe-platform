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

import java.util.Collection;
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
import com.qualogy.qafe.gwt.client.vo.data.GDataObject;
import com.qualogy.qafe.gwt.client.vo.functions.BuiltInFunctionGVO;
import com.qualogy.qafe.gwt.client.vo.functions.BusinessActionRefGVO;
import com.qualogy.qafe.gwt.client.vo.functions.ChangeStyleGVO;
import com.qualogy.qafe.gwt.client.vo.functions.ClearGVO;
import com.qualogy.qafe.gwt.client.vo.functions.ClosePanelGVO;
import com.qualogy.qafe.gwt.client.vo.functions.CloseWindowGVO;
import com.qualogy.qafe.gwt.client.vo.functions.CopyGVO;
import com.qualogy.qafe.gwt.client.vo.functions.ErrorHandlerGVO;
import com.qualogy.qafe.gwt.client.vo.functions.EventGVO;
import com.qualogy.qafe.gwt.client.vo.functions.EventRefGVO;
import com.qualogy.qafe.gwt.client.vo.functions.FocusGVO;
import com.qualogy.qafe.gwt.client.vo.functions.IfGVO;
import com.qualogy.qafe.gwt.client.vo.functions.IterationGVO;
import com.qualogy.qafe.gwt.client.vo.functions.LocalStoreGVO;
import com.qualogy.qafe.gwt.client.vo.functions.LogFunctionGVO;
import com.qualogy.qafe.gwt.client.vo.functions.OpenWindowGVO;
import com.qualogy.qafe.gwt.client.vo.functions.RegExpValidateGVO;
import com.qualogy.qafe.gwt.client.vo.functions.ReturnGVO;
import com.qualogy.qafe.gwt.client.vo.functions.CallScriptGVO;
import com.qualogy.qafe.gwt.client.vo.functions.SetPanelGVO;
import com.qualogy.qafe.gwt.client.vo.functions.SetPropertyGVO;
import com.qualogy.qafe.gwt.client.vo.functions.SetValueGVO;
import com.qualogy.qafe.gwt.client.vo.functions.ShowPanelGVO;
import com.qualogy.qafe.gwt.client.vo.functions.StoreClearGVO;
import com.qualogy.qafe.gwt.client.vo.functions.SwitchGVO;
import com.qualogy.qafe.gwt.client.vo.functions.ToggleGVO;
import com.qualogy.qafe.gwt.client.vo.functions.dialog.GenericDialogGVO;
import com.qualogy.qafe.gwt.client.vo.ui.UIGVO;
import com.qualogy.qafe.gwt.client.vo.ui.event.EventListenerGVO;

public class EventHandler {

    private static EventHandler instance = null;

    private RPCServiceAsync rpcService = null;

    private Map<String, Stack<Queue<Object>>> eventMap = new HashMap<String, Stack<Queue<Object>>>();

    private Map<String, Map<String, Object>> processingBuiltInsMap =
        new HashMap<String, Map<String, Object>>();

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
        BUILTIN_MAP.put(IfGVO.CLASS_NAME, new IfHandler());
        BUILTIN_MAP.put(ClearGVO.CLASS_NAME, new ClearHandler());
        BUILTIN_MAP.put(ReturnGVO.CLASS_NAME, new ReturnHandler());
        BUILTIN_MAP.put(CopyGVO.CLASS_NAME, new CopyHandler());
        BUILTIN_MAP.put(ChangeStyleGVO.CLASS_NAME, new ChangeStyleHandler());
        BUILTIN_MAP.put(SetPropertyGVO.CLASS_NAME, new SetPropertyHandler());
        BUILTIN_MAP.put(IterationGVO.CLASS_NAME, new IterationHandler());
        BUILTIN_MAP.put(SwitchGVO.CLASS_NAME, new SwitchHandler());
        BUILTIN_MAP.put(RegExpValidateGVO.CLASS_NAME, new RegExpValidateHandler());
        BUILTIN_MAP.put(CallScriptGVO.CLASS_NAME, new CallScriptHandler());
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
            final ServiceDefTarget endpoint = (ServiceDefTarget) rpcService;
            final String moduleRelativeURL = GWT.getModuleBaseURL() + "rpc.service";
            endpoint.setServiceEntryPoint(moduleRelativeURL);
        }
        return rpcService;
    }

    public void handleEvent(final UIObject sender, final String listenerType,
            EventListenerGVO eventListenerGVO, Map<String, String> mouseInfo, Map<String, Object> internalVariables) {
        final String appId = getAppId(sender);
        final String windowId = getWindowId(sender);
        final UIGVO applicationGVO = getApplication(appId);
        if (applicationGVO == null) {
            return;
        }
        ClientApplicationContext.getInstance().setPrerenderingUIVO(appId, applicationGVO);
        
        handleResultListeners(sender, listenerType);

        if (eventListenerGVO == null) {
        	return;
        }
        String eventId = eventListenerGVO.getEventId();
        EventGVO eventGVO = getEvent(eventId, windowId, applicationGVO);
        if (eventGVO == null) {
            return;
        }

        final String eventSessionId = register();
        storeEventAttributes(eventGVO, sender, listenerType, appId, windowId, eventSessionId);

        // Store the internal pagesize and offset in the pipe.
        storeInternalVariables(eventGVO, eventSessionId, internalVariables);

        Stack<Queue<Object>> builtInsStack = new Stack<Queue<Object>>();
        Queue<Object> builtIns = getBuiltIns(eventGVO);
        builtInsStack.add(builtIns);
        eventMap.put(eventSessionId, builtInsStack);
        handleEvent(eventSessionId, sender, listenerType, mouseInfo, appId, windowId);
    }
    
    private void storeInternalVariables(EventGVO eventGVO,
			String eventSessionId, Map<String, Object> internalVariables) {
    	if (internalVariables == null) {
    		return;
    	}
    	for (String key : internalVariables.keySet()) {
			Object value = internalVariables.get(key);
			storeData(eventSessionId, key, value);
		}
	}

	// CHECKSTYLE.OFF: CyclomaticComplexity
    public void handleEvent(final String eventSessionId, final UIObject sender, final String listenerType,
            final Map<String, String> mouseInfo, final String appId, final String windowId) {
        Stack<Queue<Object>> builtInsStack = eventMap.get(eventSessionId);
        if ((builtInsStack == null) || builtInsStack.isEmpty()) {
            cleanup(eventSessionId);
            return;
        }
        Queue builtIns = builtInsStack.peek();
        while (!builtIns.isEmpty()) {
            Object builtIn = builtIns.poll();
            try {
                if (!canProcess(builtIn)) {
                    continue;
                }

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
                        ((LinkedList) builtIns).addFirst(keyOfBuiltIn);
                    }
                }

                Queue<Object> derivedBuiltIns = new LinkedList<Object>();
                BuiltInState state =
                    handleBuiltIn(builtIn, sender, listenerType, mouseInfo, appId, windowId, eventSessionId,
                        derivedBuiltIns);
                switch (state) {
                    case SUSPEND: {
                        /*
                         * NOTE: The callback is handled in the superclass of all built-in handlers, call
                         * handleEvent(eventSessionId, ...) after processing the corresponding built-in in the
                         * callback
                         */
                        return;
                    }
                    case REPEAT: {
                        ((LinkedList) builtIns).addFirst(builtIn);
                    }
                        break;
                    case ENTER_CALL: {
                        if (!derivedBuiltIns.isEmpty()) {
                            derivedBuiltIns.add(BuiltInMarker.EXIT_POINT);
                        }
                    }
                        break;
                    case EXIT_CALL: {
                        handleExitCall(eventSessionId, sender, listenerType, mouseInfo, appId, windowId);
                        return;
                    }
                    case TERMINATE: {
                    	cleanup(eventSessionId);
                        return;
                    }
                }
                if (pushBuiltIns(eventSessionId, derivedBuiltIns)) {
                	// Postpone current processing and process the new top of the stack
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
    // CHECKSTYLE.ON: CyclomaticComplexity

    public boolean pushBuiltIns(final String eventSessionId, final Collection<BuiltInFunctionGVO> eventItems) {
    	if ((eventItems == null) || eventItems.isEmpty()) {
    		return false;
    	}
    	Queue<Object> builtIns = new LinkedList<Object>(eventItems);
    	return pushBuiltIns(eventSessionId, builtIns);
    }
    
    private boolean pushBuiltIns(final String eventSessionId, final Queue<Object> builtIns) {
    	if ((builtIns == null) || builtIns.isEmpty()) {
    		return false;
    	}
    	Stack<Queue<Object>> builtInsStack = eventMap.get(eventSessionId);
        if (builtInsStack == null) {
            return false;
        }
        return builtInsStack.add(builtIns);
    }
    
    /**
     * Handles defined internal listeners. 
     * 
     * @param sender the sender UI object
     * @param listenerType the listener type
     */
    private void handleResultListeners(final UIObject sender,
			final String listenerType) {
		GDataObject dataObject = new GDataObject();
        String senderId = getComponentId(sender);
        dataObject.setSenderId(senderId);
        dataObject.setListenerType(listenerType);
        ClientApplicationContext.getInstance().fireResult(dataObject);
	}
    
    private boolean canProcess(Object builtIn) {
        if (builtIn == BuiltInMarker.EXIT_POINT) {
            return false;
        }
        if (builtIn instanceof ErrorHandlerGVO) {
            return false;
        }
        return true;
    }

    /**
     * Handles exiting an event caused by a return built in by removing built ins until an exit point is hit.
     * When an exit point is found, we return to normal processing of the caller event
     * 
     * @param eventSessionId the current sessions id
     * @param sender the component that triggered the event
     * @param listenerType the type of trigger causing the event
     * @param mouseInfo information such as x and y position about the mouse
     * @param appId the qafe application id where the event occurred
     * @param windowId the window within the application where the event occurred
     */
    private void handleExitCall(String eventSessionId, UIObject sender, String listenerType,
            Map<String, String> mouseInfo, String appId, String windowId) {

        Stack<Queue<Object>> builtInsStack = eventMap.get(eventSessionId);
        if ((builtInsStack == null) || builtInsStack.isEmpty()) {
            cleanup(eventSessionId);
            return;
        }

        Queue<Object> builtIns = builtInsStack.peek();
        while (!builtIns.isEmpty()) {
            Object builtIn = builtIns.poll();
            if (builtIn == BuiltInMarker.EXIT_POINT) {
                handleEvent(eventSessionId, sender, listenerType, mouseInfo, appId, windowId);
                return;
            }
        }

        if (builtIns.isEmpty()) {
            builtInsStack.remove(builtIns);
        }
        handleExitCall(eventSessionId, sender, listenerType, mouseInfo, appId, windowId);
    }

    private BuiltInState handleBuiltIn(Object builtIn, final UIObject sender, final String listenerType,
            final Map<String, String> mouseInfo, final String appId, final String windowId,
            final String eventSessionId, Queue derivedBuiltIns) {
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
        return builtInHandler.handleBuiltIn(sender, listenerType, mouseInfo, builtInGVO, appId, windowId,
            eventSessionId, derivedBuiltIns);
    }

    public void handleException(final Throwable exception, final Object currentBuiltIn
    		, final UIObject sender, final String listenerType, final Map<String, String> mouseInfo
    		, final String appId, final String windowId, final String eventSessionId) {
        Stack<Queue<Object>> builtInsStack = eventMap.get(eventSessionId);
        if ((builtInsStack == null) || builtInsStack.isEmpty()) {
            String builtInShortenName = getShortenName(currentBuiltIn);
            String title = "Fail to execute " + builtInShortenName;
            String message = exception.getMessage();
            showMessage(title, message, exception);
            cleanup(eventSessionId);
            return;
        }
        Queue builtIns = builtInsStack.peek();
        while (!builtIns.isEmpty()) {
            Object builtIn = builtIns.poll();
            ErrorHandlerGVO errorHandlerGVO = resolveErrorHandler(builtIn, appId, windowId, exception);
            if (errorHandlerGVO != null) {
                String finalAction = errorHandlerGVO.getFinalAction();
                if (ErrorHandlerGVO.FINALLY_RETHROW.equals(finalAction)) {
                    continue;
                }
                storeData(eventSessionId, DataStorage.KEY_ERROR_MESSAGE, exception.getMessage());
                Collection<BuiltInFunctionGVO> eventItems = errorHandlerGVO.getEventItems();
                Queue exceptionBuiltIns = new LinkedList();
                exceptionBuiltIns.addAll(eventItems);
                builtInsStack.add(exceptionBuiltIns);
                handleEvent(eventSessionId, sender, listenerType, mouseInfo, appId, windowId);
                return;
            }
        }
        if (builtIns.isEmpty()) {
            builtInsStack.remove(builtIns);
        }
        handleException(exception, currentBuiltIn, sender, listenerType, mouseInfo, appId, windowId,
            eventSessionId);
    }

    private ErrorHandlerGVO resolveErrorHandler(Object builtIn, String appId, String windowId,
            Throwable exception) {
        if (builtIn instanceof ErrorHandlerGVO) {
            ErrorHandlerGVO errorHandlerGVO = (ErrorHandlerGVO) builtIn;
            if (matchException(errorHandlerGVO, exception)) {
                return errorHandlerGVO;
            }
            return null;
        }
        if (builtIn instanceof EventRefGVO) {
            EventRefGVO eventRefGVO = (EventRefGVO) builtIn;
            String eventId = eventRefGVO.getEventId();
            EventGVO eventGVO = getEvent(eventId, windowId, appId);
            if (eventGVO == null) {
                return null;
            }
            Collection<BuiltInFunctionGVO> eventItems = eventGVO.getEventItems();
            if (eventItems == null) {
                return null;
            }
            for (BuiltInFunctionGVO eventItemGVO : eventItems) {
                ErrorHandlerGVO errorHandlerGVO =
                    resolveErrorHandler(eventItemGVO, appId, windowId, exception);
                if (errorHandlerGVO != null) {
                    return errorHandlerGVO;
                }
            }
            return null;
        }
        return null;
    }

    private boolean matchException(ErrorHandlerGVO builtIn, Throwable exception) {
        if (exception == null) {
            return false;
        }
        String exceptionMessage = exception.toString();
        String errorHandlerMessage = builtIn.getException();
        return exceptionMessage.contains(errorHandlerMessage);
    }

    public UIGVO getApplication(final String appId) {
        return ClientApplicationContext.getInstance().getApplication(appId);
    }

    public EventGVO getEvent(String eventId, String windowId, String appId) {
        UIGVO applicationGVO = EventHandler.getInstance().getApplication(appId);
        if (applicationGVO == null) {
            return null;
        }
        return getEvent(eventId, windowId, applicationGVO);
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

    public void log(String action, UIObject sender, String listenerType, Map<String, String> mouseInfo,
            BuiltInFunctionGVO builtInGVO, String appId, String windowId) {
        String builtInShortenName = getShortenName(builtInGVO);
        String componentId = getComponentId(sender);
        StringBuffer logMessage = new StringBuffer();
        logMessage.append(action + " Built-In [" + builtInShortenName + "]");
        logMessage.append(": Sender=" + componentId + " - ListenerType=" + listenerType);
        logMessage.append(" - WindowId=" + windowId + " - AppId=" + appId);
        logMessage.append(" - MouseInfo=" + mouseInfo);
        log(logMessage.toString());
    }

    public void log(String dataId, String name, Object data) {
        String logMessage = "dataId=" + dataId + " - " + name;
        if (data != null) {
            logMessage += "=" + data.toString();
        }
        log(logMessage);
    }

    public void log(String message) {
        log("Log", message);

    }

    public void log(String title, String message) {
        log("Log", message, false);
    }

    public void log(String title, String message, boolean alert) {
        ClientApplicationContext.getInstance().log(title, message, alert);
    }

    /**
     * Stores data about the event in the pipe
     * 
     * @param sender
     * 
     * @param appId the id of the application
     * @param windowId the window on where the event occured in
     * @param sessionId the session id, also used as dataId for the storeData
     * @param eventSessionId
     */
    private void storeEventAttributes(EventGVO event, UIObject sender, String listenerType, String appId,
            String windowId, String eventSessionId) {
        String srcId = getComponentId(sender);
        String srcName = getComponentName(sender);
        Object srcValue = getComponentValue(sender, appId, windowId, srcId);
        String srcListener = listenerType;

        storeData(eventSessionId, event.getSourceId(), srcId);
        storeData(eventSessionId, event.getSourceName(), srcName);
        storeData(eventSessionId, event.getSourceValue(), srcValue);
        storeData(eventSessionId, event.getSourceListenerType(), srcListener);
    }

    public void storeData(String dataId, String name, Object data) {
        getDataStorage().storeData(dataId, name, data);
        log(dataId, name, data);
    }

    public DataStorage getDataStorage() {
        return ClientApplicationContext.getInstance().getDataStorage();
    }

    public String getShortenName(Object builtIn) {
        if (builtIn == null) {
            return null;
        }
        String shortenName = builtIn.toString();
        int dotIndex = shortenName.lastIndexOf(".");
        if (dotIndex > -1) {
            shortenName = shortenName.substring(dotIndex + 1);
        }
        return shortenName;
    }

    public String getSessionId() {
        return ClientApplicationContext.getInstance().getWindowSession();
    }
    
    private String getAppId(final UIObject sender) {
        return RendererHelper.getComponentContext(sender);
    }

    private String getWindowId(final UIObject sender) {
        return RendererHelper.getParentComponent(sender);
    }

    private String getComponentId(final UIObject sender) {
    	String id = RendererHelper.getComponentId(sender);
    	String[] idSplitted = id.split("\\|", 2);
    	
    	if (idSplitted.length == 2) {
    		return idSplitted[0];
    	}
    	
        return id;
    }
    
    private String getComponentName(final UIObject sender) {
        return RendererHelper.getNamedComponentName(sender);
    }

    private Object getComponentValue(UIObject sender, String appId, String windowId, String srcId) {
        return BuiltinHandlerHelper.getValue(sender, sender, false, null);
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
