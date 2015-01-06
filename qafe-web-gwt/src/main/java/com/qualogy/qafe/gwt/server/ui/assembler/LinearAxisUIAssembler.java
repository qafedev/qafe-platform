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
package com.qualogy.qafe.gwt.server.ui.assembler;

import com.qualogy.qafe.bind.core.application.ApplicationContext;
import com.qualogy.qafe.bind.domain.ApplicationMapping;
import com.qualogy.qafe.bind.presentation.component.Component;
import com.qualogy.qafe.bind.presentation.component.LinearAxis;
import com.qualogy.qafe.bind.presentation.component.Window;
import com.qualogy.qafe.gwt.client.vo.ui.ComponentGVO;
import com.qualogy.qafe.gwt.client.vo.ui.LinearAxisGVO;
import com.qualogy.qafe.gwt.server.helper.UIAssemblerHelper;
import com.qualogy.qafe.web.util.SessionContainer;

public class LinearAxisUIAssembler implements UIAssembler {

	public ComponentGVO convert(Component component, Window currentWindow, ApplicationMapping applicationMapping, ApplicationContext context, SessionContainer ss) {
		ComponentGVO vo = null;
		if (component != null) {
			if (component instanceof LinearAxis) {
				LinearAxis linearAxis = (LinearAxis) component;
				LinearAxisGVO voTemp = new LinearAxisGVO();
				UIAssemblerHelper.copyFields(linearAxis, currentWindow, voTemp,applicationMapping, context, ss);
				if(linearAxis.getMinValue() != null){
					voTemp.setMinValue(linearAxis.getMinValue());
				}
				if(linearAxis.getMaxValue() != null){
					voTemp.setMaxValue(linearAxis.getMaxValue());
				}
				if(linearAxis.getTickSize() != null){
					voTemp.setTickSize(linearAxis.getTickSize());
				}
				vo = voTemp;
			}
		}
		return vo;
	}

	public String getStaticStyleName() {
		// TODO Auto-generated method stub
		return null;
	}

}
