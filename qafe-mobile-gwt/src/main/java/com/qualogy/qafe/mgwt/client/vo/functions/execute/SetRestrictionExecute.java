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
package com.qualogy.qafe.mgwt.client.vo.functions.execute;

import java.util.ArrayList;
import java.util.List;

import com.qualogy.qafe.mgwt.client.activities.AbstractActivity;
import com.qualogy.qafe.mgwt.client.activities.WindowActivity;
import com.qualogy.qafe.mgwt.client.places.WindowPlace;
import com.qualogy.qafe.mgwt.client.vo.functions.BuiltInComponentGVO;
import com.qualogy.qafe.mgwt.client.vo.functions.BuiltInFunctionGVO;
import com.qualogy.qafe.mgwt.client.vo.functions.SetPropertyGVO;
import com.qualogy.qafe.mgwt.client.vo.functions.SetRestrictionGVO;

public class SetRestrictionExecute extends BuiltInExecute {

	public void execute(BuiltInFunctionGVO builtInFunctionGVO, AbstractActivity activity) {
		if (builtInFunctionGVO instanceof SetRestrictionGVO) {
			SetRestrictionGVO setRestrictionGVO = (SetRestrictionGVO)builtInFunctionGVO;
			setRestriction(setRestrictionGVO, activity);
		}
		FunctionsExecutor.setProcessedBuiltIn(true);
	}
	
	private void setRestriction(SetRestrictionGVO setRestrictionGVO, AbstractActivity activity) {
		String context = setRestrictionGVO.getApplicationId();
		String windowId = setRestrictionGVO.getWindowId();
		String componentId = setRestrictionGVO.getComponentId();
		if (isTargetActivity(windowId, context, activity)) {
			if ((componentId != null) && (activity instanceof WindowActivity)) {
				if (activity.hasView()) {
					String componentKey = activity.getClientFactory().generateComponentKey(componentId, windowId, context);
					String property = setRestrictionGVO.getProperty();
					boolean propertyValue = setRestrictionGVO.getPropertyValue();
					
					BuiltInComponentGVO builtInComponentGVO = new BuiltInComponentGVO();
					builtInComponentGVO.setWindowId(windowId);
					builtInComponentGVO.setComponentId(componentId);
					builtInComponentGVO.setComponentIdUUID(componentKey);
					List<BuiltInComponentGVO> builtInComponents = new ArrayList<BuiltInComponentGVO>();
					builtInComponents.add(builtInComponentGVO);
					SetPropertyGVO setPropertyGVO = new SetPropertyGVO();
					setPropertyGVO.setComponents(builtInComponents);
					setPropertyGVO.setProperty(property);
					setPropertyGVO.setValue(String.valueOf(propertyValue));
					FunctionsExecutor.getInstance().execute(setPropertyGVO, activity);
					return;
				}
			}	
		}
		String key = null;
		if (componentId != null) {
			key = activity.getClientFactory().generateComponentKey(null, windowId, context);
		} else if (windowId != null) {
			key = activity.getClientFactory().generateComponentKey(null, null, context);
		}
		RestrictionsExecutor.getInstance().add(key, setRestrictionGVO); 
	}

	private boolean isTargetActivity(String targetWindowId, String targetContext, AbstractActivity activity) {
		if (activity instanceof WindowActivity) {
			WindowPlace place = ((WindowActivity)activity).getPlace();
			String context = place.getContext();
			String windowId = place.getId();
			if (targetWindowId.equals(windowId) && targetContext.equals(context)) {
				return true;
			}
		}
		return false;
	}
}