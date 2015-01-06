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
package com.qualogy.qafe.gwt.server.event.assembler;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.qualogy.qafe.bind.core.application.ApplicationContext;
import com.qualogy.qafe.bind.presentation.event.EventItem;
import com.qualogy.qafe.bind.presentation.event.function.SetValue;
import com.qualogy.qafe.bind.presentation.event.function.SetValueMapping;
import com.qualogy.qafe.gwt.client.vo.data.EventDataGVO;
import com.qualogy.qafe.gwt.client.vo.functions.BuiltInFunctionGVO;
import com.qualogy.qafe.gwt.client.vo.functions.DataContainerGVO;
import com.qualogy.qafe.gwt.client.vo.functions.SetValueGVO;
import com.qualogy.qafe.web.util.SessionContainer;

@Deprecated
public class SetValueBuiltInRenderer extends AbstractEventRenderer implements EventAssembler {

	public BuiltInFunctionGVO convert(EventItem eventItem, EventDataGVO eventData,ApplicationContext context, SessionContainer sc) {
		BuiltInFunctionGVO bif = null;
		if (eventItem instanceof SetValue) {
			SetValueGVO setValue = new SetValueGVO();

			bif = setValue;
			bif.setUuid(eventData.getUuid());
			SetValue in = (SetValue) eventItem;
			String componentId = in.getComponentId();
			if(in.getComponentId() != null && in.getComponentId().length() > 0  && in.getComponentId().contains("[")) { // Then set value is to be done on a datagrid cell
				String[] inputRef = in.getComponentId().split("[.]");
				componentId = inputRef[0].toString().substring(0, inputRef[0].indexOf("["));
				setValue.setComponentId(in.getComponentId());
			} else {
				setValue.setComponentId(parseComponent(in.getComponentId(),eventData));
			}

			setValue.setBuiltInComponentGVO(getBuiltInComponentGVO(componentId, eventData));
			if (in.getParameter().getName()!=null && in.getParameter().getName().length()>0){
				setValue.setNamedComponentId(parseComponent(in.getParameter().getName(),eventData));
			}

			if (in.getGroup()!=null && in.getGroup().length()>0){
				setValue.setGroup(parseComponent(in.getGroup(),eventData));// + (eventData.getParent() == null ? "" : "|" + eventData.getParent()) + "|" + eventData.getUuid());
			}
			Object returnedObject = in.getDataObject();
			setValue.setMapping(convertMapping(in));
			setValue.setDataContainer(createContainer(returnedObject));
			setValue.setAction(in.getAction());
		}
		return bif;
	}

	public DataContainerGVO createContainer(Object object) {
	    return DataContainerGVO.create(object);
	}
	
	private Map<String,String> convertMapping(SetValue in) {
		HashMap<String,String> mapping = null;
		if (in.getMapping() != null) {
			mapping = new HashMap<String,String>(17);
			Iterator<SetValueMapping> itr = in.getMapping().iterator();
			while (itr.hasNext()) {
				SetValueMapping setValueMapping = (SetValueMapping) itr.next();
				mapping.put(setValueMapping.getKey(), setValueMapping.getValue());
			}

		}
		return mapping;
	}

}
