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

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.UIObject;
import com.qualogy.qafe.gwt.client.context.ClientApplicationContext;
import com.qualogy.qafe.gwt.client.exception.GWTServiceException;
import com.qualogy.qafe.gwt.client.service.RPCServiceAsync;
import com.qualogy.qafe.gwt.client.vo.data.EventItemDataGVO;
import com.qualogy.qafe.gwt.client.vo.data.GEventItemDataObject;
import com.qualogy.qafe.gwt.client.vo.handlers.EventHandler;

public class EventCallbackHandler {

    private static EventCallbackHandler hanlder = null;

    private static RPCServiceAsync rpcService;

    public static void setRpcService(RPCServiceAsync rpcService) {
        EventCallbackHandler.rpcService = rpcService;
    }

    private static AsyncCallback<?> callback = null;

    private EventCallbackHandler() {
    }

    public static EventCallbackHandler getInstance() {
        if (hanlder == null) {
            hanlder = new EventCallbackHandler();
        }
        return hanlder;
    }

    final public static AsyncCallback<?> createEventCallBack(final UIObject sender, final String listenerType,
            final EventItemDataGVO eventItemDataGVO, final String appId, final String windowId,
            final String eventSessionId) {
        ServiceDefTarget endpoint = (ServiceDefTarget) rpcService;
        String moduleRelativeURL = GWT.getModuleBaseURL() + "rpc.service";
        endpoint.setServiceEntryPoint(moduleRelativeURL);

        if (callback == null) {
            callback = new AsyncCallback<Object>() {

                public void onSuccess(Object result) {
                    GEventItemDataObject output = (GEventItemDataObject) result;
                    EventHandler.getInstance().handleEventItems(sender, listenerType, appId, windowId, eventSessionId, output);
                }

                public void onFailure(Throwable caught) {
                    // EventHandler.getInstance().handleEventItems(sender, appId, windowId, eventSessionId);
                    // ClientApplicationContext.getInstance().log("Event execution for " + listenerType +
                    // " failed", caught.getMessage(), true, false, caught);
                    ClientApplicationContext.getInstance().setBusy(false);
                    if (caught instanceof GWTServiceException) {
                        GWTServiceException gWTServiceException = (GWTServiceException) caught;
                        // processOutput(gWTServiceException.getGDataObject());
                    }
                }

            };
        }
        return callback;
    }

    final public static void invokeService(final UIObject sender, final String listenerType, 
            final EventItemDataGVO eventItemDataGVO, final String appId, final String windowId,
            final String eventSessionId) {
        callback = createEventCallBack(sender, listenerType, eventItemDataGVO, appId, windowId, eventSessionId);
        try {
            rpcService.executeEventItem(eventItemDataGVO, callback);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
