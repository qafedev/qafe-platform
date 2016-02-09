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
package com.qualogy.qafe.mgwt.server.event.assembler;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.qualogy.qafe.bind.core.application.ApplicationContext;
import com.qualogy.qafe.bind.presentation.event.EventItem;
import com.qualogy.qafe.bind.presentation.event.function.SetValue;
import com.qualogy.qafe.bind.presentation.event.function.SetValueMapping;
import com.qualogy.qafe.mgwt.client.component.DataMap;
import com.qualogy.qafe.mgwt.client.vo.data.EventDataGVO;
import com.qualogy.qafe.mgwt.client.vo.functions.BuiltInFunctionGVO;
import com.qualogy.qafe.mgwt.client.vo.functions.DataContainerGVO;
import com.qualogy.qafe.mgwt.client.vo.functions.SetValueGVO;
import com.qualogy.qafe.web.util.SessionContainer;

public class SetValueBuiltInRenderer extends AbstractEventRenderer implements EventAssembler {

	public BuiltInFunctionGVO convert(EventItem eventItem, EventDataGVO eventData,ApplicationContext context, SessionContainer sc) {
		BuiltInFunctionGVO bif = null;
		if (eventItem instanceof SetValue) {
			SetValueGVO setValue = new SetValueGVO();

			bif = setValue;
			bif.setUuid(eventData.getUuid());
			SetValue in = (SetValue) eventItem;
			setValue.setBuiltInComponentGVO(getBuiltInComponentGVO(in.getComponentId(), eventData));
			if (in.getParameter().getName()!=null && in.getParameter().getName().length()>0){
				setValue.setNamedComponentId(parseComponent(in.getParameter().getName(),eventData));
			}

			if (in.getComponentId()!=null && in.getComponentId().length()>0){
				setValue.setComponentId(parseComponent(in.getComponentId(),eventData));// + (eventData.getParent() == null ? "" : "|" + eventData.getParent()) + "|" + eventData.getUuid());
			}

			if (in.getGroup()!=null && in.getGroup().length()>0){
				setValue.setGroup(parseComponent(in.getGroup(),eventData));// + (eventData.getParent() == null ? "" : "|" + eventData.getParent()) + "|" + eventData.getUuid());
			}
			Object returnedObject = in.getDataObject();
			setValue.setMapping(convertMapping(in));


			setValue.setDataContainer(createContainer(returnedObject));
			setValue.setAction(in.getAction());

			//processDataContainer(dataContainer,returnedObject,in.getParameter());

		}
		return bif;
	}

    // CHECKSTYLE.OFF: CyclomaticComplexity
	public  DataContainerGVO createContainer(Object object) {
		DataContainerGVO data = null;
		if (object != null) {
			if (object instanceof DataContainerGVO){
				DataContainerGVO dc = (DataContainerGVO)object;
				data = createContainer(DataContainerGVO.createType(dc));
			} else {

				data = new DataContainerGVO();
				boolean valueIsSet =false;
				if (object instanceof Boolean ||object instanceof Character || object instanceof String ||object instanceof Date ||object instanceof Number ){
					if (object instanceof Boolean) {
						data.setStringDataType(DataContainerGVO.TYPE_BOOLEAN);
					} else if (object instanceof Character) {
						data.setStringDataType(DataContainerGVO.TYPE_CHAR);
					} else if (object instanceof String) {
						data.setStringDataType(DataContainerGVO.TYPE_STRING);
					} else if (object instanceof Date) {
						data.setStringDataType(DataContainerGVO.TYPE_DATE);
						data.setDateData(new Date(((Date) object).getTime()));
					} else if (object instanceof Number) {
						if (object instanceof Byte) {
							data.setStringDataType(DataContainerGVO.TYPE_BYTE);
						} else if (object instanceof BigDecimal) {
							try {
								int intValue =Integer.parseInt(object.toString());
								data.setStringDataType(DataContainerGVO.TYPE_INT);
								data.setDataString(""+intValue);
								valueIsSet=true;
							} catch(Exception e){
								data.setStringDataType(DataContainerGVO.TYPE_DOUBLE);
							}

						} else if (object instanceof BigInteger) {
							data.setStringDataType(DataContainerGVO.TYPE_BIGINTEGER);
						} else if (object instanceof Double) {
							data.setStringDataType(DataContainerGVO.TYPE_DOUBLE);
						} else if (object instanceof Float) {
							data.setStringDataType(DataContainerGVO.TYPE_FLOAT);
						} else if (object instanceof Integer) {
							data.setStringDataType(DataContainerGVO.TYPE_INT);
						} else if (object instanceof Long) {
							data.setStringDataType(DataContainerGVO.TYPE_LONG);
						} else if (object instanceof Short) {
							data.setStringDataType(DataContainerGVO.TYPE_SHORT);
						}
					}


				if (!valueIsSet){
					data.setDataString(object.toString());
				}

				data.setKind(DataContainerGVO.KIND_STRING);
				} else if (object instanceof Map) {
					processMap(object, data);

				} else if (object instanceof Collection) {
					Collection c = (Collection) object;
					data.setKind(DataContainerGVO.KIND_COLLECTION);
					List<DataContainerGVO> list = new ArrayList<DataContainerGVO>();
					data.setListofDC(list);
					List<Object> l = (List<Object>) c;
					for (Object o : l) {
						list.add(createContainer(o));
						// }
					}

//					int index = 0;
//					Iterator itr = c.iterator();
//					boolean isMap = false; // so initially think about String
//					boolean isList = false; // so initially think about String
//					while (itr.hasNext() && index == 0) {
//						Object o = itr.next();
//						if (o instanceof Map) {
//							isMap = true;
//						}
//						if (o instanceof List) {
//							isList = true;
//							logger.debug("Huh...a list of a list?");
//						}
//
//						index++;
//					}
//
//					if (isMap) {
//						List<Map<String, Object>> listOfMap = (List<Map<String, Object>>) c;
//						data.setKind(DataContainerGVO.KIND_COLLECTION_OF_MAP);
//						List<DataMap> l = new ArrayList<DataMap>();
//						data.setListOfDataMap(l);
//						for (Map<String, Object> map : listOfMap) {
//							l.add(processMap(map));
//						}
//					} else {
//						data.setKind(DataContainerGVO.KIND_COLLECTION_OF_STRING);
//						List<String> listOfStrings = new ArrayList<String>();
//						data.setListOfString(listOfStrings);
//						List<String> lString = (List<String>) c;
//						for (String string : lString) {
//							listOfStrings.add(string);
//						}
//					}
				}
			}



		}
		return data;
	}
    // CHECKSTYLE.ON: CyclomaticComplexity

	private  void processMap(Object returnedObject, DataContainerGVO data) {
		data.setDataMap(processMap(returnedObject));
		data.setKind(DataContainerGVO.KIND_MAP);

	}

	private  DataMap processMap(Object returnedObject) {
		Map<String, Object> map = (Map<String, Object>) returnedObject;
		DataMap dataMap = new DataMap();
		for (Map.Entry<String,Object> entry: map.entrySet()){
			dataMap.put(entry.getKey(),createContainer(entry.getValue()));
		}
//		for (String key : map.keySet()) {
//			dataMap.put(key, createContainer(map.get(key)));
//		}
		return dataMap;

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
