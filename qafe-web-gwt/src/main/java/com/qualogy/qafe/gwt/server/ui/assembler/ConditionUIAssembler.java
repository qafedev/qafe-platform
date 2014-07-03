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

import com.qualogy.qafe.bind.presentation.style.Condition;
import com.qualogy.qafe.gwt.client.vo.ui.ConditionGVO;

public class ConditionUIAssembler {
	
	public static ConditionGVO convert(Condition condition){
		ConditionGVO vo = null;
		if (condition != null){			
			vo = new ConditionGVO();
			vo.setExpr(condition.getExpr());
			vo.setStyle(condition.getStyle());
			vo.setStyleClass(condition.getStyleClass());
		}
		
		return vo;
	}
}
