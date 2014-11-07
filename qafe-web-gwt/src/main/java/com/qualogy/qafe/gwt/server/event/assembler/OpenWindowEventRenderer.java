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
package com.qualogy.qafe.gwt.server.event.assembler;

import java.util.ArrayList;
import java.util.List;

import com.qualogy.qafe.bind.commons.type.Parameter;
import com.qualogy.qafe.bind.core.application.ApplicationContext;
import com.qualogy.qafe.bind.presentation.event.EventItem;
import com.qualogy.qafe.bind.presentation.event.function.OpenWindow;
import com.qualogy.qafe.gwt.client.vo.data.EventDataGVO;
import com.qualogy.qafe.gwt.client.vo.functions.BuiltInFunctionGVO;
import com.qualogy.qafe.gwt.client.vo.functions.DataParameterGVO;
import com.qualogy.qafe.gwt.client.vo.functions.OpenWindowGVO;
import com.qualogy.qafe.web.util.SessionContainer;

@Deprecated
public class OpenWindowEventRenderer extends AbstractEventRenderer implements EventAssembler{

	private SetValueBuiltInRenderer setValueBuiltInRenderer = new SetValueBuiltInRenderer();
	
	public BuiltInFunctionGVO convert(EventItem eventItem, EventDataGVO eventData,ApplicationContext context, SessionContainer sc) {
		BuiltInFunctionGVO bif = null;
		if (eventItem instanceof OpenWindow) {
			OpenWindowGVO openWindowGVO = new OpenWindowGVO();
			
			bif = openWindowGVO;
			fillIn(eventItem, openWindowGVO, eventData);
		
			OpenWindow in = (OpenWindow)eventItem;
		
			openWindowGVO.setContext(context.getId().toString());
			openWindowGVO.setWindow(in.getWindowData());	
			openWindowGVO.setUrl(in.getUrlData());		
			openWindowGVO.setTitle(in.getTitleData());
			openWindowGVO.setParams(in.getParamsData());
			openWindowGVO.setExternal(in.getExternal());
			openWindowGVO.setPlacement(in.getPlacement());
			List<DataParameterGVO> dataParameterGVOList = new ArrayList<DataParameterGVO>();
			if (in.getDataParameters() != null) {
				for (Parameter parameter : in.getDataParameters()) {
					DataParameterGVO dataParameterGVO = new DataParameterGVO();
					dataParameterGVO.setName(parameter.getName());
					dataParameterGVO.setDataContainerGVO(setValueBuiltInRenderer.createContainer(parameter.getData()));
					dataParameterGVOList.add(dataParameterGVO);
				}
			}
			openWindowGVO.setDataParameterGVOList(dataParameterGVOList);
		}
		return bif;
	}

}
