/**
 * Copyright 2008-2017 Qualogy Solutions B.V.
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
package com.qualogy.qafe.mgwt.server.ui.assembler;

import java.util.ArrayList;
import java.util.List;

import com.qualogy.qafe.bind.core.application.ApplicationContext;
import com.qualogy.qafe.bind.domain.ApplicationMapping;
import com.qualogy.qafe.bind.presentation.component.Component;
import com.qualogy.qafe.bind.presentation.component.DropDown;
import com.qualogy.qafe.bind.presentation.component.DropDownItem;
import com.qualogy.qafe.bind.presentation.component.Window;
import com.qualogy.qafe.mgwt.client.vo.ui.ComponentGVO;
import com.qualogy.qafe.mgwt.client.vo.ui.DropDownGVO;
import com.qualogy.qafe.mgwt.client.vo.ui.DropDownItemGVO;
import com.qualogy.qafe.mgwt.server.helper.UIAssemblerHelper;
import com.qualogy.qafe.web.util.SessionContainer;

public class DropDownUIAssembler implements UIAssembler {

	public DropDownUIAssembler() {
	}

	public ComponentGVO convert(Component object, Window currentWindow,ApplicationMapping applicationMapping, ApplicationContext context, SessionContainer ss) {
		ComponentGVO vo = null;
		if (object != null) {
			if (object instanceof DropDown) {
				DropDown dropDown = (DropDown)object;
				DropDownGVO voTemp  = new DropDownGVO();
				UIAssemblerHelper.copyFields(dropDown, currentWindow,voTemp,applicationMapping, context, null);
				List<DropDownItemGVO> items = new ArrayList<DropDownItemGVO>();
				voTemp.setEmptyItemDisplayName(dropDown.getEmptyItemDisplayName());
				voTemp.setEmptyItemValue(dropDown.getEmptyItemValue());
				voTemp.setEmptyItemMessageKey(dropDown.getEmptyItemMessageKey());
				voTemp.setRequiredStyleClassName(dropDown.getRequiredStyleClassName());
								
				if (dropDown.getDropDownItems()!=null){
					for (DropDownItem item  : dropDown.getDropDownItems()) {						
						items.add((DropDownItemGVO)ComponentUIAssembler.convert(item,currentWindow,applicationMapping,context, ss));
					}
				}
				voTemp.setDropDownItems(items.toArray(new DropDownItemGVO[]{}));
				
				vo =voTemp;
			}
		}
		return vo;
	}

	public String getStaticStyleName() {
		return "dropdown";
	}
}
