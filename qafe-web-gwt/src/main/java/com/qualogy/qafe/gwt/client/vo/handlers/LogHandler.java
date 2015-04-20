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

import java.util.Map;
import java.util.Queue;

import org.gwt.mosaic.ui.client.WindowPanel;

import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;
import com.qualogy.qafe.gwt.client.component.QRootPanel;
import com.qualogy.qafe.gwt.client.component.QWindowPanel;
import com.qualogy.qafe.gwt.client.context.ClientApplicationContext;
import com.qualogy.qafe.gwt.client.vo.functions.BuiltInFunctionGVO;
import com.qualogy.qafe.gwt.client.vo.functions.LogFunctionGVO;
import com.qualogy.qafe.gwt.client.vo.ui.event.ParameterGVO;

public class LogHandler extends AbstractBuiltInHandler {

    protected final BuiltInState executeBuiltIn(final UIObject sender, final String listenerType,
            final Map<String, String> mouseInfo, final BuiltInFunctionGVO builtInGVO, final String appId,
            final String windowId, final String eventSessionId, final Queue derivedBuiltIns) {
        final LogFunctionGVO logFunctionGVO = (LogFunctionGVO) builtInGVO;
        if (!logFunctionGVO.getDebug().booleanValue()) {
            executeLog(sender, logFunctionGVO, appId, windowId, eventSessionId);
        }
        return BuiltInState.EXECUTED;
    }

    private void executeLog(final UIObject sender, final LogFunctionGVO logFunctionGVO, final String appId,
            final String windowId, final String eventSessionId) {
        if (ClientApplicationContext.getInstance().isMDI()) {
            executeLogForMDIMode(sender, logFunctionGVO, appId, windowId, eventSessionId);
        } else {
            executeLogForSDIMode(sender, logFunctionGVO, appId, windowId, eventSessionId);
        }
    }

    private void executeLogForSDIMode(final UIObject sender, final LogFunctionGVO logFunctionGVO,
            final String appId, final String windowId, final String eventSessionId) {
        final Widget widget = ClientApplicationContext.getInstance().getMainPanel().getWidget();
        if (widget instanceof QRootPanel) {
            final QRootPanel rootPanel =
                (QRootPanel) ClientApplicationContext.getInstance().getMainPanel().getWidget();
            final String message = getLogMessage(sender, logFunctionGVO, appId, windowId, eventSessionId);
            rootPanel.showMessage(message, logFunctionGVO.getDelay(), logFunctionGVO.getStyleClass(),
                logFunctionGVO.getStyleProperties());
        }
    }

    private void executeLogForMDIMode(final UIObject sender, final LogFunctionGVO logFunctionGVO,
            final String appId, final String windowId, final String eventSessionId) {
        final String uuid = getUUId(sender);
        final WindowPanel wp = ClientApplicationContext.getInstance().getWindow(uuid, windowId);
        if (wp instanceof QWindowPanel) {
            final QWindowPanel qwp = (QWindowPanel) wp;
            final String message = getLogMessage(sender, logFunctionGVO, appId, windowId, eventSessionId);
            qwp.showMessage(message, logFunctionGVO.getDelay(), logFunctionGVO.getStyleClass(),
                logFunctionGVO.getStyleProperties());
        }
    }

    private String getLogMessage(final UIObject sender, final LogFunctionGVO logFunctionGVO,
            final String appId, final String windowId, final String eventSessionId) {
        final ParameterGVO parameterGVO = logFunctionGVO.getMessageGVO();
        final String message = getValue(sender, parameterGVO, appId, windowId, eventSessionId).toString();
        ClientApplicationContext.getInstance().log(message);
        return message;
    }
}
