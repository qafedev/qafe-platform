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

import java.util.Iterator;

import com.qualogy.qafe.bind.core.application.ApplicationContext;
import com.qualogy.qafe.bind.domain.ApplicationMapping;
import com.qualogy.qafe.bind.presentation.component.Choice;
import com.qualogy.qafe.bind.presentation.component.ChoiceItem;
import com.qualogy.qafe.bind.presentation.component.Component;
import com.qualogy.qafe.bind.presentation.component.Window;
import com.qualogy.qafe.mgwt.client.vo.ui.ChoiceGVO;
import com.qualogy.qafe.mgwt.client.vo.ui.ChoiceItemGVO;
import com.qualogy.qafe.mgwt.client.vo.ui.ComponentGVO;
import com.qualogy.qafe.mgwt.server.helper.UIAssemblerHelper;
import com.qualogy.qafe.web.util.SessionContainer;

public class ChoiceUIAssembler implements UIAssembler {

	public ChoiceUIAssembler() {
	}

	public ComponentGVO convert(Component object,Window currentWindow,ApplicationMapping applicationMapping, ApplicationContext context, SessionContainer ss) {
		ComponentGVO vo = null;
		if (object != null) {
			if (object instanceof Choice) {
				Choice choice = (Choice)object;
				ChoiceGVO voTemp  = new ChoiceGVO();
				UIAssemblerHelper.copyFields(choice, currentWindow,voTemp,applicationMapping, context, ss);		
				voTemp.setHorizontalOrientation(choice.getHorizontalOrientation());
				if (choice.getChoiceItems()!=null){
					ChoiceItemGVO[] items = new ChoiceItemGVO[choice.getChoiceItems().size()];
					Iterator<ChoiceItem> itr = choice.getChoiceItems().iterator();
					int index=0;
					while (itr.hasNext()){
						ChoiceItem item = (ChoiceItem) itr.next();
						items[index] = (ChoiceItemGVO)ComponentUIAssembler.convert(item,currentWindow,applicationMapping,context, ss);
						index++;
					}
					voTemp.setChoiceItems(items);
				}
				vo =voTemp;
			}
		}
		return vo;
	}

	public String getStaticStyleName() {
		return "choice"; 
	}
}
