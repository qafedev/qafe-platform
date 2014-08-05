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

public class OpenWindowHandler extends AbstractBuiltInHandler {
    
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

    public boolean handleBuiltIn(UIObject sender, String listenerType, Map<String, String> mouseInfo, BuiltInFunctionGVO builtInFunctionGVO, String appId, String windowId, String eventSessionId) {
        OpenWindowGVO openWindowGVO = (OpenWindowGVO) builtInFunctionGVO;
        showWindow(sender, openWindowGVO, appId, windowId, eventSessionId);
        return false;
    }
    
    private void showWindow(UIObject sender, OpenWindowGVO openWindowGVO, String appId, String windowId, String eventSessionId) {
        if (openWindowGVO.getWindowGVO() != null) {
            openWindow(sender, openWindowGVO, appId, windowId, eventSessionId);
        } else if (openWindowGVO.getUrlGVO() != null) {
            openUrl(sender, openWindowGVO, appId, windowId, eventSessionId);
        }    
    }
    
    private void openWindow(UIObject sender, OpenWindowGVO openWindowGVO, String appId, String windowId, String eventSessionId) {
        String openWindowId = (String) getValue(sender, openWindowGVO.getWindowGVO(), appId, windowId, eventSessionId);
        closeOpenedWindow(openWindowId, appId, appId);
        List<ParameterGVO> dataParamGVOs = openWindowGVO.getDataParamGVOList();
        storeDataParams(sender, openWindowId, dataParamGVOs, appId, windowId, eventSessionId);
        MainFactoryActions.getUIByUUID(appId, openWindowId);
    }
    
    private void storeDataParams(UIObject sender, String openWindowId, List<ParameterGVO> dataParamGVOs, String appId, String windowId, String eventSessionId) {
        if (dataParamGVOs == null) {
            return;
        }
        String target = BuiltInFunctionGVO.SOURCE_APP_LOCAL_STORE_ID;
        for (ParameterGVO dataParamGVO : dataParamGVOs) {
            String dataId = generateDataId(target, appId, openWindowId, eventSessionId);
            String name = dataParamGVO.getName();
            Object value = getValue(sender, dataParamGVO, appId, windowId, eventSessionId);
            storeData(dataId, name, value);
        }
    }
    
    private void openUrl(UIObject sender, OpenWindowGVO openWindowGVO, String appId, String windowId, String eventSessionId) {
        String windowUrl = (String) getValue(sender, openWindowGVO.getUrlGVO(), appId, windowId, eventSessionId);
        String windowParams = (String) getValue(sender, openWindowGVO.getParamsGVO(), appId, windowId, eventSessionId);
        Map<String, String> paramMap = parseParams(windowParams);
        
        if (!hasPosition(paramMap)) {
            calculatePosition(paramMap, openWindowGVO);
        }
        if (openWindowGVO.getExternal()) {
            openUrlExternal(windowUrl, paramMap);
        } else {
            String windowTitle = (String) getValue(sender, openWindowGVO.getTitleGVO(), appId, windowId, eventSessionId);
            if (windowTitle == null) {
                windowTitle = windowUrl;
            } else {
                windowTitle = windowTitle.replace(" ", "_");
            }
            openUrlInternal(windowTitle, windowUrl, paramMap);
        }
    }

    private void openUrlInternal(String title, String url, Map<String, String> paramMap) {
        boolean resizable = isResizable(paramMap, true);
        boolean modal = isModal(paramMap, false);
        boolean centered = false;  
        int height = getHeight(paramMap, 450);
        int width = getWidth(paramMap, 600);
        int left = getLeft(paramMap, 0);
        int top = getTop(paramMap, 0);
        MainFactory.createWindowWithUrl(title, url, width, height, resizable, centered, top, left, modal);
        ClientApplicationContext.getInstance().internalWindowCount++;
    }
    
    private void openUrlExternal(String url, Map<String, String> paramMap) {           
        // Set the title to "" when open a window externally, the title will not be shown anyway,
        // in IE this will give an exception when title is not empty          
        String title = "";
        String features = toCommaSeperated(paramMap);
        Window.open(url, title, features);
        ClientApplicationContext.getInstance().externalWindowCount++;
    }
    
    private void closeOpenedWindow(String windowId, String context, String uuid) {
        if (ClientApplicationContext.getInstance().isMDI()) {
            ClientApplicationContext.getInstance().removeWindow(windowId, context, uuid);
        } else {
            WindowFactory.clearWidgetFromMainPanel();
        }
    }
    
    private void calculatePosition(Map<String, String> paramMap, OpenWindowGVO openWindowGVO) {       
        String placement = openWindowGVO.getPlacement();
        boolean external = openWindowGVO.getExternal();
        
        if (OpenWindowGVO.PLACEMENT_CASCADE.equals(placement)) {
            calculateCascade(paramMap, external);
        } else if (OpenWindowGVO.PLACEMENT_CENTER_CASCADE.equals(placement)) {
            calculateCenterCascade(paramMap, external);  
        } else if (OpenWindowGVO.PLACEMENT_TILED.equals(placement) && !external) {
            calculateTiled(paramMap);
        }
    }    
        
    private void calculateCascade(Map<String, String> paramMap, boolean external) {
        calculateCascadePosition(paramMap, 20, 20, external);
    }
    
    private void calculateCenterCascade(Map<String, String> paramMap, boolean external) {
        int width = getWidth(paramMap, 0);        
        int height = getHeight(paramMap, 0);
        int startLeft = ((Window.getClientWidth() - width) /2);
        int startTop = ((Window.getClientHeight() - height) /2);
        calculateCascadePosition(paramMap, startLeft, startTop, external);
    }
    
    private void calculateTiled(Map<String, String> paramMap) {
        int width = getWidth(paramMap, 0);        
        int height = getHeight(paramMap, 0);
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
    
    private void calculateCascadePosition(Map<String, String> paramMap, int startLeft, int startTop, boolean external) {
        int left = startLeft;
        int top = startTop;
        int windowCount = ClientApplicationContext.getInstance().internalWindowCount;
        if (external) {
            windowCount = ClientApplicationContext.getInstance().externalWindowCount;
        }
        if (windowCount > 0) {
            for (int i=0; i<windowCount; i++) {
                left = left + 20;
                top = top + 20;
            }   
        }
        paramMap.put(LEFT, String.valueOf(left));
        paramMap.put(TOP, String.valueOf(top));
    }
    
    private String toCommaSeperated(final Map<String, String> paramMap) {
        StringBuilder sb = new StringBuilder();
        Iterator<Entry<String, String>> it = paramMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> pairs = (Map.Entry<String, String>)it.next();
            
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
        
    private Map<String, String> parseParams(String windowParams) { 
        Map<String, String> paramMap = new HashMap<String, String>();
        if (windowParams != null && windowParams.length() != 0) {
            String[] paramArr = windowParams.split(",");
            
            String temp = null;
            for (int i = 0; i < paramArr.length; i++) {
                if (paramArr[i].indexOf(WIDTH) > -1) {
                    String width = paramArr[i].substring(paramArr[i].indexOf("=") + 1);
                    if (width != null) {
                        paramMap.put(WIDTH, width);
                    }
                } else if (paramArr[i].indexOf(HEIGHT) > -1) {
                    String height = paramArr[i].substring(paramArr[i].indexOf("=") + 1);
                    if (height != null) {
                        paramMap.put(HEIGHT, height);
                    }
                } else if (paramArr[i].indexOf(LEFT) > -1) {
                    String left = paramArr[i].substring(paramArr[i].indexOf("=") + 1);
                    if (left != null) {
                        paramMap.put(LEFT, left);
                    }
                } else if (paramArr[i].indexOf(TOP) > -1) {
                    String top = paramArr[i].substring(paramArr[i].indexOf("=") + 1);
                    if (top != null) {
                        paramMap.put(TOP, top);
                    }
                } else if (paramArr[i].indexOf(MENUBAR) > -1) {
                    temp = paramArr[i].substring(paramArr[i].indexOf("=") + 1);
                    String menubar = "no";
                    if(temp.equalsIgnoreCase("yes") || temp.equalsIgnoreCase("true") || temp.equals("1")){
                        menubar = "yes";
                    }
                    paramMap.put(MENUBAR, menubar);
                } else if (paramArr[i].indexOf(SCROLLBARS) > -1) {
                    temp = paramArr[i].substring(paramArr[i].indexOf("=") + 1);
                    String scrollbars = "no";
                    if(temp.equalsIgnoreCase("yes") || temp.equalsIgnoreCase("true") || temp.equals("1")){
                        scrollbars = "yes";
                    }
                    paramMap.put(SCROLLBARS, scrollbars);
                } else if (paramArr[i].indexOf(TOOLBAR) > -1) {
                    temp = paramArr[i].substring(paramArr[i].indexOf("=") + 1);
                    String toolbar = "no";
                    if(temp.equalsIgnoreCase("yes") || temp.equalsIgnoreCase("true") || temp.equals("1")){
                        toolbar = "yes";
                    } 
                    paramMap.put(TOOLBAR, toolbar);
                } else if (paramArr[i].indexOf(STATUS) > -1) {
                    temp = paramArr[i].substring(paramArr[i].indexOf("=") + 1);
                    String status = "no";
                    if(temp.equalsIgnoreCase("yes") || temp.equalsIgnoreCase("true") || temp.equals("1")){
                        status = "yes";
                    } 
                    paramMap.put(STATUS, status);
                } else if (paramArr[i].indexOf(RESIZABLE) > -1) {
                    temp = paramArr[i].substring(paramArr[i].indexOf("=") + 1);
                    String resize = "yes";
                    if(temp.equalsIgnoreCase("no") || temp.equalsIgnoreCase("false") || temp.equals("0")){
                        resize = "no";
                    }
                    paramMap.put(RESIZABLE, resize);
                } else if (paramArr[i].indexOf("modal") > -1) {
                    temp = paramArr[i].substring(paramArr[i].indexOf("=") + 1);
                    String modal = "No";
                    if(temp.equalsIgnoreCase("yes") || temp.equalsIgnoreCase("true") || temp.equals("1")){
                        modal = "yes";
                    }
                    paramMap.put(MODAL, modal);
                }
            }
        }
        return paramMap;
    }
    
    private boolean hasPosition(Map<String, String> params) {
        if ((params.containsKey(LEFT) && params.containsKey(TOP)) || (params.containsKey(SCREEN_X) && params.containsKey(SCREEN_Y))) {
            return true;
        }
        return false;
    }
    
    private boolean hasAttribute(Map<String, String> map, String key) {
        if (map.containsKey(key)) {
            return true;
        }
        return false;
    }
    
    private String getAttribute(Map<String, String> map, String key) {
        return map.get(key);
    }
    
    private boolean isResizable(Map<String, String> map, boolean defaultValue) {
        boolean resizable = true;
        if (hasAttribute(map, RESIZE)) {
            String resize = getAttribute(map, RESIZE);
            if(resize.equals("")|| resize.equals("no")){
                resizable = false;
            }
        } else {
            resizable = defaultValue;
        }
        return resizable;
    }
    
    private boolean isModal(Map<String, String> map, boolean defaultValue) {
        boolean isModal = false;
        if (hasAttribute(map, MODAL)) {
            String modal = getAttribute(map, MODAL);
            if(modal.equals("yes")){
                isModal = true;
            }
        } else {
            isModal = defaultValue;
        }
        return isModal;
    }
    
    private int getWidth(Map<String, String> paramMap, int defaultValue) {
        int width = 0;
        if (hasAttribute(paramMap, WIDTH)) {
            width = Integer.parseInt(getAttribute(paramMap, WIDTH));
        } else {
            width = defaultValue;
        }
        return width;
    }
    
    private int getHeight(Map<String, String> paramMap, int defaultValue) {
        int height = 0;
        if (hasAttribute(paramMap, HEIGHT)) {
            height = Integer.parseInt(getAttribute(paramMap, HEIGHT));
        } else {
            height = defaultValue;
        }
        return height;
    }
    
    private int getLeft(Map<String, String> paramMap, int defaultValue) {
        int left = 0;
        if (hasAttribute(paramMap, LEFT)) {
            left = Integer.parseInt(getAttribute(paramMap, LEFT));
        } else {
            left = defaultValue;
        }
        return left;
    }
    
    private int getTop(Map<String, String> paramMap, int defaultValue) {
        int top = 0;
        if (hasAttribute(paramMap, TOP)) {
            top = Integer.parseInt(getAttribute(paramMap, TOP));
        } else {
            top = defaultValue;
        }
        return top;
    }
}