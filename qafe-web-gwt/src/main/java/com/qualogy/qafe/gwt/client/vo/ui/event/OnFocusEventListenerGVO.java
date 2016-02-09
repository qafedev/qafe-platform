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
package com.qualogy.qafe.gwt.client.vo.ui.event;

import java.util.List;
import java.util.Map;

/**
 * @author rjankie
 * This class can be registered to any Component type which needs to
 * handle click events.
 */
public class OnFocusEventListenerGVO extends EventListenerGVO {



	public OnFocusEventListenerGVO(String eventId,Map<String,String> sourceInfo,List<InputVariableGVO> input,List<ParameterGVO> parameters) {
		super(eventId,sourceInfo,input,parameters);

	}

	public OnFocusEventListenerGVO(){}
}
