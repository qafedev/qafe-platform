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
package com.qualogy.qafe.gwt.server.ui.assembler;

import com.qualogy.qafe.bind.core.application.ApplicationContext;
import com.qualogy.qafe.bind.domain.ApplicationMapping;
import com.qualogy.qafe.bind.presentation.component.CategoryAxis;
import com.qualogy.qafe.bind.presentation.component.Component;
import com.qualogy.qafe.bind.presentation.component.Window;
import com.qualogy.qafe.gwt.client.vo.ui.CategoryAxisGVO;
import com.qualogy.qafe.gwt.client.vo.ui.ComponentGVO;
import com.qualogy.qafe.gwt.server.helper.UIAssemblerHelper;
import com.qualogy.qafe.web.util.SessionContainer;

public class CategoryAxisUIAssembler implements UIAssembler {

	public ComponentGVO convert(Component component, Window currentWindow, ApplicationMapping applicationMapping, ApplicationContext context, SessionContainer ss) {
		ComponentGVO vo = null;
		if (component != null) {
			if (component instanceof CategoryAxis) {
				CategoryAxis categoryAxis = (CategoryAxis) component;
				CategoryAxisGVO voTemp = new CategoryAxisGVO();
				UIAssemblerHelper.copyFields(categoryAxis, currentWindow, voTemp, applicationMapping, context, ss);
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
