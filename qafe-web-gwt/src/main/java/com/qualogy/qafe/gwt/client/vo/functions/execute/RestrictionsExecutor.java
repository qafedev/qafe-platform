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
package com.qualogy.qafe.gwt.client.vo.functions.execute;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.qualogy.qafe.gwt.client.ui.renderer.RendererHelper;
import com.qualogy.qafe.gwt.client.vo.functions.BuiltInComponentGVO;
import com.qualogy.qafe.gwt.client.vo.functions.SetPropertyGVO;
import com.qualogy.qafe.gwt.client.vo.functions.SetRestrictionGVO;

public class RestrictionsExecutor {

	private static RestrictionsExecutor instance;

	private Map<String,List<SetRestrictionGVO>> window2Restrictions = new HashMap<String,List<SetRestrictionGVO>>();
	
	public static RestrictionsExecutor getInstance() {
		if (instance == null) {
			instance = new RestrictionsExecutor();
		}
		return instance;
	}
	
	private RestrictionsExecutor() {
	}
	
	private String generateKey(String windowId, String applicationId) {
		return RendererHelper.generateId("", windowId, applicationId);
	}
	
	public void add(String windowId, String applicationId, SetRestrictionGVO setRestrictionGVO) {
		String key = generateKey(windowId, applicationId);
		if (!window2Restrictions.containsKey(key)) {
			List<SetRestrictionGVO> setRestrictionGVOList = new ArrayList<SetRestrictionGVO>();
			window2Restrictions.put(key, setRestrictionGVOList);
		}
		List<SetRestrictionGVO> restrictionGVOList = window2Restrictions.get(key);
		restrictionGVOList.add(setRestrictionGVO);
	}
	
	public boolean execute(String windowId, String applicationId) {
		String key = generateKey(windowId, applicationId);
		if (window2Restrictions.containsKey(key)) {
			List<SetRestrictionGVO> setRestrictionGVOList = window2Restrictions.get(key);
			for (SetRestrictionGVO setRestrictionGVO : setRestrictionGVOList) {
				execute(setRestrictionGVO);
			}
			setRestrictionGVOList.clear();
			window2Restrictions.remove(key);
			return true;
		}
		return false;
	}
	
	public boolean execute(SetRestrictionGVO setRestrictionGVO) {
		if (setRestrictionGVO != null) {
			String appId = setRestrictionGVO.getApplicationId();
			String winId = setRestrictionGVO.getWindowId();
			String componentId = setRestrictionGVO.getComponentId();
			String componentUUID = RendererHelper.generateId(componentId, winId, appId);
			if ((componentId == null) || (componentId.length() == 0)) {
				if ((winId == null) || (winId.length() == 0)) {
					componentUUID = appId;
				} else {
					componentUUID = RendererHelper.generateId(winId, null, appId);					
				}
			}
			BuiltInComponentGVO builtInComponentGVO = new BuiltInComponentGVO();
			builtInComponentGVO.setWindowId(winId);
			builtInComponentGVO.setComponentId(componentId);
			builtInComponentGVO.setComponentIdUUID(componentUUID);
			
			List<BuiltInComponentGVO> builtInComponentGVOList = new ArrayList<BuiltInComponentGVO>();
			builtInComponentGVOList.add(builtInComponentGVO);
			
			SetPropertyGVO setPropertyGVO = new SetPropertyGVO();
			setPropertyGVO.setComponents(builtInComponentGVOList);
			setPropertyGVO.setProperty(setRestrictionGVO.getProperty());
			setPropertyGVO.setValue(String.valueOf(setRestrictionGVO.getPropertyValue()));
			
			FunctionsExecutor.getInstance().execute(setPropertyGVO);
			return true;
		}
		return false;
	}
}
