package com.qualogy.qafe.gwt.client.vo.handlers;

import java.util.List;
import java.util.Map;

import org.gwt.mosaic.ui.client.WindowPanel;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.UIObject;
import com.qualogy.qafe.gwt.client.context.ClientApplicationContext;
import com.qualogy.qafe.gwt.client.factory.WindowFactory;
import com.qualogy.qafe.gwt.client.vo.functions.BuiltInFunctionGVO;
import com.qualogy.qafe.gwt.client.vo.functions.CloseWindowGVO;

public class CloseWindowHandler extends AbstractBuiltInHandler {

    public boolean handleBuiltIn(UIObject sender, String listenerType, Map<String, String> mouseInfo, BuiltInFunctionGVO builtInFunctionGVO, String appId, String windowId, String eventSessionId) {
        CloseWindowGVO closeWindowGVO = (CloseWindowGVO) builtInFunctionGVO;
        closeWindow(closeWindowGVO, sender, appId, windowId, eventSessionId);
        return false;
    }
    
    private void closeWindow(CloseWindowGVO closeWindowGVO, UIObject sender, String appId, String windowId, String eventSessionId) {
        String window = (String) getValue(sender, closeWindowGVO.getWindowGVO(), appId, windowId, eventSessionId);
        if (window == null || window.length() == 0) {
        	return;
        }
        if (ClientApplicationContext.getInstance().isMDI()){
        	String uuid = getUUId(sender);
			ClientApplicationContext.getInstance().removeWindow(window, appId, uuid);
		} else {
			WindowFactory.clearWidgetFromMainPanel();
		}
        removeVariables(appId, window, eventSessionId);
    }
    
    private void removeVariables(String appId, String windowId, String eventSessionId) {
    	removeUserVariables(appId, windowId, eventSessionId);
    	removeGlobalVariables(appId, windowId, eventSessionId);
    }
    
    private void removeUserVariables(String appId, String windowId, String eventSessionId) {
    	String dataId = generateDataId(BuiltInFunctionGVO.SOURCE_APP_LOCAL_STORE_ID, appId, windowId, eventSessionId);
		ClientApplicationContext.getInstance().getDataStorage().removeData(dataId);
    }
    
    private void removeGlobalVariables(String appId, String windowId, String eventSessionId) {
    	List<WindowPanel> existingWindows = ClientApplicationContext.getInstance().getWindows();
    	if (existingWindows.size() > 0) {
    		for (WindowPanel window : existingWindows) {
    			String openedWindowAppId = getAppId(window);
    			if (openedWindowAppId.equals(appId)) {
    				return;
    			}
    		}
    	}
    	String dataId = generateDataId(BuiltInFunctionGVO.SOURCE_APP_GLOBAL_STORE_ID, appId, windowId, eventSessionId);
		ClientApplicationContext.getInstance().getDataStorage().removeData(dataId);    	
    }    
}