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
package com.qualogy.qafe.gwt.client.vo.handlers;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import com.google.gwt.user.client.ui.UIObject;
import com.qualogy.qafe.gwt.client.context.ClientApplicationContext;
import com.qualogy.qafe.gwt.client.storage.DataStorage;
import com.qualogy.qafe.gwt.client.ui.renderer.RendererHelper;
import com.qualogy.qafe.gwt.client.vo.data.GEventItemDataObject;
import com.qualogy.qafe.gwt.client.vo.functions.BuiltInFunctionGVO;
import com.qualogy.qafe.gwt.client.vo.functions.BusinessActionRefGVO;
import com.qualogy.qafe.gwt.client.vo.functions.EventGVO;
import com.qualogy.qafe.gwt.client.vo.functions.LocalStoreGVO;
import com.qualogy.qafe.gwt.client.vo.functions.OpenWindowGVO;
import com.qualogy.qafe.gwt.client.vo.functions.SetPanelGVO;
import com.qualogy.qafe.gwt.client.vo.functions.SetValueGVO;
import com.qualogy.qafe.gwt.client.vo.functions.ShowPanelGVO;
import com.qualogy.qafe.gwt.client.vo.ui.UIGVO;
import com.qualogy.qafe.gwt.client.vo.ui.event.EventListenerGVO;

public class EventHandler {

    private static EventHandler instance;

    private static Map<String, Stack<BuiltInFunctionGVO>> eventItemsToExecute =
        new HashMap<String, Stack<BuiltInFunctionGVO>>();

    private final Map<String, BuiltInHandler> BUILTIN_MAP = new HashMap<String, BuiltInHandler>();

    // private final List<BuiltInFunctionGVO> BUILTINS_SERVER_SIDE_MAP = new HashMap<String,
    // BuiltInHandler>();

    private EventHandler() {
        BUILTIN_MAP.put(OpenWindowGVO.CLASS_NAME, new OpenWindowHandler());
        BUILTIN_MAP.put(LocalStoreGVO.CLASS_NAME, new LocalStoreHandler());
        BUILTIN_MAP.put(BusinessActionRefGVO.CLASS_NAME, new BusinessActionRefHandler());
        BUILTIN_MAP.put(SetValueGVO.CLASS_NAME, new SetValueHandler());
        BUILTIN_MAP.put(SetPanelGVO.CLASS_NAME, new SetPanelHandler());
        BUILTIN_MAP.put(ShowPanelGVO.CLASS_NAME, new ShowPanelHandler());
    }

    public static EventHandler getInstance() {
        if (instance == null) {
            instance = new EventHandler();
        }
        return instance;
    }

    public void handleEvent(final UIObject sender, String listenerType, EventListenerGVO eventListenerGVO, Map<String, String> mouseInfo) {
        String appId = getAppId(sender);
        String windowId = getWindowId(sender);
        UIGVO applicationGVO = ClientApplicationContext.getInstance().getApplication(appId);
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

        collectEventItems(eventSessionId, eventGVO);

        handleEventItems(sender, listenerType, mouseInfo, appId, windowId, eventSessionId);

    }

    private void collectEventItems(final String eventSessionId, EventGVO eventGVO) {
        Collection<BuiltInFunctionGVO> eventItemGVOs = eventGVO.getEventItems();
        Stack<BuiltInFunctionGVO> eventItemsStack = new Stack<BuiltInFunctionGVO>();

        for (BuiltInFunctionGVO eventItemGVO : eventItemGVOs) {
            eventItemsStack.insertElementAt(eventItemGVO, 0);
        }
        eventItemsToExecute.put(eventSessionId, eventItemsStack);
    }

    public void handleEventItems(final UIObject sender, final String listenerType, final Map<String, String> mouseInfo, final String appId,
            final String windowId, final String eventSessionId) {
        final Stack<BuiltInFunctionGVO> eventItems = eventItemsToExecute.get(eventSessionId);
        if (isAllEventItemsProcessed(eventItems)) {
            doCleanUp(eventSessionId);
            return;
        }

        final BuiltInFunctionGVO eventItemGVO = eventItems.pop();
        handleEventItem(sender, listenerType, mouseInfo, appId, windowId, eventSessionId, eventItemGVO);

        if (isClientSideEventItem(eventItemGVO)) {
            handleEventItems(sender, listenerType, mouseInfo, appId, windowId, eventSessionId);
        }
    }

    public void handleEventItems(final UIObject sender, final String listenerType, final Map<String, String> mouseInfo, final String appId, final String windowId,
            final String eventSessionId, GEventItemDataObject output) {
        storeOutputVariables(eventSessionId, output);
        handleEventItems(sender, listenerType, mouseInfo, appId, windowId, eventSessionId);
    }

    private void storeOutputVariables(String eventSessionId, GEventItemDataObject output) {
        LocalStoreHandler localStoreHandler = new LocalStoreHandler();
        HashMap<String, Object> outputValues = output.getOutputValues();
        if (outputValues == null) {
            return;

        }
        for (Map.Entry<String, Object> outputValue : outputValues.entrySet()) {
            localStoreHandler.storeData(eventSessionId, outputValue.getKey(), outputValue.getValue());
        }
    }

    private void doCleanUp(final String eventSessionId) {
        unregister(eventSessionId);
        eventItemsToExecute.remove(eventSessionId);
    }

    private boolean isAllEventItemsProcessed(final Stack<BuiltInFunctionGVO> eventItems) {
        return eventItems == null || eventItems.isEmpty();
    }

    private boolean isClientSideEventItem(final BuiltInFunctionGVO eventItemGVO) {
        return !(eventItemGVO instanceof BusinessActionRefGVO);
    }

    private void handleEventItem(final UIObject sender, final String listenerType, final Map<String, String> mouseInfo, final String appId,
            final String windowId, final String eventSessionId, BuiltInFunctionGVO eventItemGVO) {
        final BuiltInHandler builtInHandler = (BuiltInHandler) BUILTIN_MAP.get(eventItemGVO.getClassName());
        if (builtInHandler != null) {
            builtInHandler.handleBuiltIn(sender, listenerType, mouseInfo, eventItemGVO, appId, windowId, eventSessionId);
        }
    }

    private String getAppId(final UIObject sender) {
        return RendererHelper.getComponentContext(sender);
    }

    private String getWindowId(final UIObject sender) {
        return RendererHelper.getParentComponent(sender);
    }
    
    private EventGVO getEvent(final String eventId, final String windowId, UIGVO applicationGVO) {
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

    private String register() {
        final DataStorage dataStorage = ClientApplicationContext.getInstance().getDataStorage();
        return dataStorage.register();
    }

    private void unregister(final String dataId) {
        final DataStorage dataStorage = ClientApplicationContext.getInstance().getDataStorage();
        dataStorage.unregister(dataId);
    }
}
