/**
 * Copyright 2008-2016 Qualogy Solutions B.V.
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
package com.qualogy.qafe.gwt.server.ui.assembler;

import org.apache.commons.lang.StringUtils;

import com.qualogy.qafe.bind.core.application.ApplicationContext;
import com.qualogy.qafe.bind.domain.ApplicationMapping;
import com.qualogy.qafe.bind.presentation.component.Element;
import com.qualogy.qafe.bind.presentation.component.Window;
import com.qualogy.qafe.gwt.client.vo.ui.ElementGVO;
import com.qualogy.qafe.util.StyleDomUtil;
import com.qualogy.qafe.web.util.SessionContainer;

public class ElementUIAssembler {

	public static ElementGVO convert(Element object, Window currentWindow,ApplicationMapping applicationMapping,ApplicationContext context,SessionContainer sc){
		ElementGVO vo = null;
		if (object !=null){
			vo = new ElementGVO();
			vo.setComponent(ComponentUIAssembler.convert(object.getComponent(),currentWindow,applicationMapping,context, sc));
			vo.setGridheight(object.getGridheight());
			vo.setGridwidth(object.getGridwidth());
			vo.setStyle(object.getStyle());
			vo.setStyleClass(object.getStyleClass());
			vo.setX(object.getX());
			vo.setY(object.getY());
			
			String[] properties = StringUtils.split(object.getStyle()==null ? "": object.getStyle(), ';');
			String[][] styleProperties = new String[properties.length][2];
			for (int i=0;i<properties.length;i++){
				styleProperties[i]= StringUtils.split(properties[i],':');
			}			
			
			/*
			 * Modify the properties since this is DOM manipulation : font-size for css has to become fontSize for DOM
			 */
			for (int i=0;i<styleProperties.length;i++){
				
				styleProperties[i][0] =StyleDomUtil.initCapitalize(styleProperties[i][0]); 				
			}
			
			vo.setStyleProperties(styleProperties);
		}
		return vo;
	}
}
