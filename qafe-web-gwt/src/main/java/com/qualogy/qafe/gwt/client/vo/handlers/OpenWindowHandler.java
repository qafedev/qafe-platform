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

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.UIObject;
import com.qualogy.qafe.gwt.client.context.ClientApplicationContext;
import com.qualogy.qafe.gwt.client.factory.MainFactory;
import com.qualogy.qafe.gwt.client.factory.MainFactoryActions;
import com.qualogy.qafe.gwt.client.factory.WindowFactory;
import com.qualogy.qafe.gwt.client.vo.functions.BuiltInFunctionGVO;
import com.qualogy.qafe.gwt.client.vo.functions.OpenWindowGVO;
import com.qualogy.qafe.gwt.client.vo.ui.event.ParameterGVO;

/**
 * 
 */
public final class OpenWindowHandler extends AbstractBuiltInHandler {

    private static final Map<String, String> NORMALIZE_OPTIONS = new HashMap<String, String>();

    static {
        NORMALIZE_OPTIONS.put("yes", "yes");
        NORMALIZE_OPTIONS.put("true", "yes");
        NORMALIZE_OPTIONS.put("1", "yes");
        NORMALIZE_OPTIONS.put("no", "no");
        NORMALIZE_OPTIONS.put("false", "no");
        NORMALIZE_OPTIONS.put("0", "no");
    }

    private static final String WIDTH = "width";

    private static final String HEIGHT = "height";

    private static final String LEFT = "left";

    private static final String TOP = "top";

    private static final String SCREEN_X = "screenX";

    private static final String SCREEN_Y = "screenY";

    private static final String MENUBAR = "menubar";

    private static final String SCROLLBARS = "scrollbars";

    private static final String TOOLBAR = "toolbar";

    private static final String STATUS = "status";

    private static final String RESIZABLE = "resizable";

    private static final String RESIZE = "resize";

    private static final String MODAL = "modal";

    private static final String SCREENX = "screenX";

    private static final String SCREENY = "screenY";

    public boolean handleBuiltIn(final UIObject sender, final String listenerType,
            final Map<String, String> mouseInfo, final BuiltInFunctionGVO builtInFunctionGVO,
            final String appId, final String windowId, final String eventSessionId) {
        final OpenWindowGVO openWindowGVO = (OpenWindowGVO) builtInFunctionGVO;
        showWindow(sender, openWindowGVO, appId, windowId, eventSessionId);
        return false;
    }

    private void showWindow(final UIObject sender, final OpenWindowGVO openWindowGVO, final String appId,
            final String windowId, final String eventSessionId) {
        if (openWindowGVO.getWindowGVO() != null) {
            openWindow(sender, openWindowGVO, appId, windowId, eventSessionId);
        } else if (openWindowGVO.getUrlGVO() != null) {
            openUrl(sender, openWindowGVO, appId, windowId, eventSessionId);
        }
    }

    private void openWindow(final UIObject sender, final OpenWindowGVO openWindowGVO, final String appId,
            final String windowId, final String eventSessionId) {
        final String openWindowId =
            (String) getValue(sender, openWindowGVO.getWindowGVO(), appId, windowId, eventSessionId);
        closeOpenedWindow(openWindowId, appId, appId);
        final List<ParameterGVO> dataParamGVOs = openWindowGVO.getDataParamGVOList();
        storeDataParams(sender, openWindowId, dataParamGVOs, appId, windowId, eventSessionId);
        MainFactoryActions.getUIByUUID(appId, openWindowId);
    }

    private void storeDataParams(final UIObject sender, final String openWindowId,
            final List<ParameterGVO> dataParamGVOs, final String appId, final String windowId,
            final String eventSessionId) {
        if (dataParamGVOs == null) {
            return;
        }
        final String target = BuiltInFunctionGVO.SOURCE_APP_LOCAL_STORE_ID;
        for (final ParameterGVO dataParamGVO : dataParamGVOs) {
            final String dataId = generateDataId(target, appId, openWindowId, eventSessionId);
            final String name = dataParamGVO.getName();
            final Object value = getValue(sender, dataParamGVO, appId, windowId, eventSessionId);
            storeData(dataId, name, value);
        }
    }

    private void openUrl(final UIObject sender, final OpenWindowGVO openWindowGVO, final String appId,
            final String windowId, final String eventSessionId) {
        final String windowUrl =
            (String) getValue(sender, openWindowGVO.getUrlGVO(), appId, windowId, eventSessionId);
        final String windowParams =
            (String) getValue(sender, openWindowGVO.getParamsGVO(), appId, windowId, eventSessionId);
        final Map<String, String> paramMap = parseParams(windowParams);

        if (!hasPosition(paramMap)) {
            calculatePosition(paramMap, openWindowGVO);
        }
        if (openWindowGVO.getExternal()) {
            openUrlExternal(windowUrl, paramMap);
        } else {
            String windowTitle =
                (String) getValue(sender, openWindowGVO.getTitleGVO(), appId, windowId, eventSessionId);
            if (windowTitle == null) {
                windowTitle = windowUrl;
            } else {
                windowTitle = windowTitle.replace(" ", "_");
            }
            openUrlInternal(windowTitle, windowUrl, paramMap);
        }
    }

    private void openUrlInternal(final String title, final String url, final Map<String, String> paramMap) {
        final boolean resizable = isResizable(paramMap, true);
        final boolean modal = isModal(paramMap, false);
        final boolean centered = false;
        final int height = getHeight(paramMap, 450);
        final int width = getWidth(paramMap, 600);
        final int left = getLeft(paramMap, 0);
        final int top = getTop(paramMap, 0);
        MainFactory.createWindowWithUrl(title, url, width, height, resizable, centered, top, left, modal);
        ClientApplicationContext.getInstance().internalWindowCount++;
    }

    private void openUrlExternal(final String url, final Map<String, String> paramMap) {
        // Set the title to "" when open a window externally, the title will not be shown anyway,
        // in IE this will give an exception when title is not empty
        final String title = "";
        final String features = toCommaSeperated(paramMap);
        Window.open(url, title, features);
        ClientApplicationContext.getInstance().externalWindowCount++;
    }

    private void closeOpenedWindow(final String windowId, final String context, final String uuid) {
        if (ClientApplicationContext.getInstance().isMDI()) {
            ClientApplicationContext.getInstance().removeWindow(windowId, context, uuid);
        } else {
            WindowFactory.clearWidgetFromMainPanel();
        }
    }

    private void calculatePosition(final Map<String, String> paramMap, final OpenWindowGVO openWindowGVO) {
        final String placement = openWindowGVO.getPlacement();
        final boolean external = openWindowGVO.getExternal();

        if (OpenWindowGVO.PLACEMENT_CASCADE.equals(placement)) {
            calculateCascade(paramMap, external);
        } else if (OpenWindowGVO.PLACEMENT_CENTER_CASCADE.equals(placement)) {
            calculateCenterCascade(paramMap, external);
        } else if (OpenWindowGVO.PLACEMENT_TILED.equals(placement) && !external) {
            calculateTiled(paramMap);
        }
    }

    private void calculateCascade(final Map<String, String> paramMap, final boolean external) {
        calculateCascadePosition(paramMap, 20, 20, external);
    }

    private void calculateCenterCascade(final Map<String, String> paramMap, final boolean external) {
        final int width = getWidth(paramMap, 0);
        final int height = getHeight(paramMap, 0);
        final int startLeft = ((Window.getClientWidth() - width) / 2);
        final int startTop = ((Window.getClientHeight() - height) / 2);
        calculateCascadePosition(paramMap, startLeft, startTop, external);
    }

    private void calculateTiled(final Map<String, String> paramMap) {
        final int width = getWidth(paramMap, 0);
        final int height = getHeight(paramMap, 0);
        int top = 30;
        int left = 0;
        if (ClientApplicationContext.getInstance().internalWindowCount > 0) {
            int row = 1;
            int column = 1;
            boolean makeNextRow = false;
            for (int i = 0; i < ClientApplicationContext.getInstance().internalWindowCount; i++) {
                left = (width * (i + 1));
                if ((left + width) > Window.getClientWidth()) {
                    left = 0;
                    makeNextRow = true;
                    if (row > 1) {
                        left = (width * column);
                        column++;
                        if ((left + width) > Window.getClientWidth()) {
                            makeNextRow = true;
                        } else {
                            makeNextRow = false;
                        }
                    }
                    if (makeNextRow) {
                        left = 0;
                        column = 1;
                        top = 30 + (height * row);
                        row++;
                        makeNextRow = false;
                    }
                } else {
                    top = 30;
                }
            }
        }
        paramMap.put(LEFT, String.valueOf(left));
        paramMap.put(TOP, String.valueOf(top));
    }

    private void calculateCascadePosition(final Map<String, String> paramMap, final int startLeft,
            final int startTop, final boolean external) {
        int left = startLeft;
        int top = startTop;
        int windowCount = ClientApplicationContext.getInstance().internalWindowCount;
        if (external) {
            windowCount = ClientApplicationContext.getInstance().externalWindowCount;
        }
        if (windowCount > 0) {
            for (int i = 0; i < windowCount; i++) {
                left = left + 20;
                top = top + 20;
            }
        }
        paramMap.put(LEFT, String.valueOf(left));
        paramMap.put(TOP, String.valueOf(top));
    }

    private String toCommaSeperated(final Map<String, String> paramMap) {
        final StringBuilder sb = new StringBuilder();
        final Iterator<Entry<String, String>> it = paramMap.entrySet().iterator();
        while (it.hasNext()) {
            final Map.Entry<String, String> pairs = it.next();

            if (LEFT.equals(pairs.getKey())) {
                sb.append(SCREENX);
            } else if (TOP.equals(pairs.getKey())) {
                sb.append(SCREENY);
            } else {
                sb.append(pairs.getKey());
            }

            sb.append("=");
            sb.append(pairs.getValue());
            if (it.hasNext()) {
                sb.append(",");
            }
        }
        return sb.toString();
    }

    static void addParameter(final Map<String, String> paramMap, final String key, final String value,
            final String defaultValue) {

        final String checkValue;
        if ("".equals(value)) {
            checkValue = null;
        } else {
            checkValue = value;
        }

        String normalizedValue = normalizeParam(checkValue, defaultValue);
        // Fall-back to source value if normalized to nothing
        if (normalizedValue == null) {
            normalizedValue = checkValue;
        }

        if (normalizedValue != null) {
            paramMap.put(key, normalizedValue);
        }
    }

    private static String normalizeParam(final String value, final String defaultValue) {

        final String normalizedValue = NORMALIZE_OPTIONS.get(value);

        if (normalizedValue == null) {
            return defaultValue;
        }

        return normalizedValue;
    }

    Map<String, String> parseParams(final String windowParams) {
        final Map<String, String> paramMap = new HashMap<String, String>();
        if (windowParams != null && windowParams.length() != 0) {
            final String[] paramArr = windowParams.split(",");

            for (int i = 0; i < paramArr.length; i++) {
                final String value = paramArr[i].substring(paramArr[i].indexOf("=") + 1);

                if (paramArr[i].indexOf(WIDTH) > -1) {
                    addParameter(paramMap, WIDTH, value, null);
                } else if (paramArr[i].indexOf(HEIGHT) > -1) {
                    addParameter(paramMap, HEIGHT, value, null);
                } else if (paramArr[i].indexOf(LEFT) > -1) {
                    addParameter(paramMap, LEFT, value, null);
                } else if (paramArr[i].indexOf(TOP) > -1) {
                    addParameter(paramMap, TOP, value, null);
                } else if (paramArr[i].indexOf(MENUBAR) > -1) {
                    addParameter(paramMap, MENUBAR, value, "no");
                } else if (paramArr[i].indexOf(SCROLLBARS) > -1) {
                    addParameter(paramMap, SCROLLBARS, value, "no");
                } else if (paramArr[i].indexOf(TOOLBAR) > -1) {
                    addParameter(paramMap, TOOLBAR, value, "no");
                } else if (paramArr[i].indexOf(STATUS) > -1) {
                    addParameter(paramMap, STATUS, value, "no");
                } else if (paramArr[i].indexOf(RESIZABLE) > -1) {
                    addParameter(paramMap, RESIZABLE, value, "yes");
                } else if (paramArr[i].indexOf("modal") > -1) {
                    addParameter(paramMap, MODAL, value, "no");
                }
            }
        }
        return paramMap;
    }

    private boolean hasPosition(final Map<String, String> params) {
        if ((params.containsKey(LEFT) && params.containsKey(TOP))
                || (params.containsKey(SCREEN_X) && params.containsKey(SCREEN_Y))) {
            return true;
        }
        return false;
    }

    private boolean hasAttribute(final Map<String, String> map, final String key) {
        if (map.containsKey(key)) {
            return true;
        }
        return false;
    }

    private String getAttribute(final Map<String, String> map, final String key) {
        return map.get(key);
    }

    private boolean isResizable(final Map<String, String> map, final boolean defaultValue) {
        boolean resizable = true;
        if (hasAttribute(map, RESIZE)) {
            final String resize = getAttribute(map, RESIZE);
            if (resize.equals("") || resize.equals("no")) {
                resizable = false;
            }
        } else {
            resizable = defaultValue;
        }
        return resizable;
    }

    private boolean isModal(final Map<String, String> map, final boolean defaultValue) {
        boolean isModal = false;
        if (hasAttribute(map, MODAL)) {
            final String modal = getAttribute(map, MODAL);
            if (modal.equals("yes")) {
                isModal = true;
            }
        } else {
            isModal = defaultValue;
        }
        return isModal;
    }

    private int getWidth(final Map<String, String> paramMap, final int defaultValue) {
        int width = 0;
        if (hasAttribute(paramMap, WIDTH)) {
            width = Integer.parseInt(getAttribute(paramMap, WIDTH));
        } else {
            width = defaultValue;
        }
        return width;
    }

    private int getHeight(final Map<String, String> paramMap, final int defaultValue) {
        int height = 0;
        if (hasAttribute(paramMap, HEIGHT)) {
            height = Integer.parseInt(getAttribute(paramMap, HEIGHT));
        } else {
            height = defaultValue;
        }
        return height;
    }

    private int getLeft(final Map<String, String> paramMap, final int defaultValue) {
        int left = 0;
        if (hasAttribute(paramMap, LEFT)) {
            left = Integer.parseInt(getAttribute(paramMap, LEFT));
        } else {
            left = defaultValue;
        }
        return left;
    }

    private int getTop(final Map<String, String> paramMap, final int defaultValue) {
        int top = 0;
        if (hasAttribute(paramMap, TOP)) {
            top = Integer.parseInt(getAttribute(paramMap, TOP));
        } else {
            top = defaultValue;
        }
        return top;
    }
}
