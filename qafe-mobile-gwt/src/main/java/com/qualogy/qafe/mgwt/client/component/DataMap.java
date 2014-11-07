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
package com.qualogy.qafe.mgwt.client.component;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.qualogy.qafe.mgwt.client.vo.functions.DataContainerGVO;

public class DataMap extends HashMap<String, DataContainerGVO> implements IsSerializable {

	private static final long serialVersionUID = -1873453039049810608L;

	private Set<String> keys = new LinkedHashSet<String>();
	
	@Deprecated
	public static String ROW_STATUS_KEY = "rowStatus";
	@Deprecated
	public static String ROW_NUMBER = "rowNumber";
	@Deprecated
	public static String ROW_STATUS_UNMODIFIED = "$$UNMODIFIED";
	@Deprecated
	public static String ROW_STATUS_NEW = "$$NEW";
	@Deprecated
	public static String ROW_STATUS_DELETED = "$$DELETED";
	@Deprecated
	public static String ROW_STATUS_MODIFIED = "$$MODIFIED";
	
	// CHECKSTYLE.OFF: CyclomaticComplexity
	public static DataContainerGVO createDataContainerGVO(Object value) {
		if (value instanceof DataContainerGVO) {
			return (DataContainerGVO)value;	
		} else if (value instanceof List) {
			List<DataContainerGVO> dataContainers = new ArrayList<DataContainerGVO>();
			List list = (List)value;
			for (int i=0; i<list.size(); i++) {
				Object itemValue = list.get(i);
				DataContainerGVO dataContainerGVO = createDataContainerGVO(itemValue);
				if (dataContainerGVO != null) {
					dataContainers.add(dataContainerGVO);
				}
			}
			DataContainerGVO dataContainerGVO = new DataContainerGVO();
			dataContainerGVO.setListofDC(dataContainers);
			dataContainerGVO.setKind(DataContainerGVO.KIND_COLLECTION);
			return dataContainerGVO;	
		} else if (value instanceof Map) {
			DataMap dataMap = new DataMap();
			Map map = (Map)value;
			Iterator itrKey = map.keySet().iterator();
			while (itrKey.hasNext()) {
				Object key = itrKey.next();
				Object keyValue = map.get(key);
				DataContainerGVO dataContainerGVO = createDataContainerGVO(keyValue);
				if (dataContainerGVO != null) {
					dataMap.put((String)key, dataContainerGVO);	
				}
			}
			DataContainerGVO dataContainerGVO = new DataContainerGVO();
			dataContainerGVO.setDataMap(dataMap);
			dataContainerGVO.setKind(DataContainerGVO.KIND_MAP);
			return dataContainerGVO;	
		} else if (value instanceof String) {
			DataContainerGVO dataContainerGVO = new DataContainerGVO();
			dataContainerGVO.setDataString((String)value);
			dataContainerGVO.setKind(DataContainerGVO.KIND_STRING);
			return dataContainerGVO;	
		} else if (value instanceof Date) {
			DataContainerGVO dataContainerGVO = new DataContainerGVO();
			dataContainerGVO.setDataString(((Date)value).toString());
			dataContainerGVO.setDateData((Date)value);
			dataContainerGVO.setKind(DataContainerGVO.KIND_STRING);
			dataContainerGVO.setStringDataType(DataContainerGVO.TYPE_DATE);
			return dataContainerGVO;	
		} else if (value != null) {
			DataContainerGVO dataContainerGVO = new DataContainerGVO();
			dataContainerGVO.setDataString(value.toString());
			dataContainerGVO.setKind(DataContainerGVO.KIND_STRING);
			int dataType = DataContainerGVO.TYPE_STRING;
			if (value instanceof Integer) {
				dataType = DataContainerGVO.TYPE_INT;
			} else if (value instanceof Double) {
				dataType = DataContainerGVO.TYPE_DOUBLE;
			} else if (value instanceof Boolean) {
				dataType = DataContainerGVO.TYPE_BOOLEAN;
			} else if (value instanceof Byte) {
				dataType = DataContainerGVO.TYPE_BYTE;
			} else if (value instanceof BigDecimal) {
				dataType = DataContainerGVO.TYPE_BIGDECIMAL;
			} else if (value instanceof BigInteger) {
				dataType = DataContainerGVO.TYPE_BIGINTEGER;
			} else if (value instanceof Float) {
				dataType = DataContainerGVO.TYPE_FLOAT;
			} else if (value instanceof Long) {
				dataType = DataContainerGVO.TYPE_LONG;
			} else if (value instanceof Short) {
				dataType = DataContainerGVO.TYPE_SHORT;
			}
			dataContainerGVO.setStringDataType(dataType);
			return dataContainerGVO;
		}
		return null;
	}
	// CHECKSTYLE.ON: CyclomaticComplexity
	
	public DataContainerGVO put(String key, Object value) {
		DataContainerGVO dataContainerGVO = createDataContainerGVO(value);
		return put(key, dataContainerGVO);
	}
	
	@Override
	public DataContainerGVO put(String key, DataContainerGVO value) {
		keys.add(key);
		return super.put(key, value);
	}
	
	@Override
	public DataContainerGVO remove(Object key) {
		keys.remove(key);
		return super.remove(key);
	}
	
	/**
	 * Returns backing ordered keySet
	 */
	@Override
	public Set<String> keySet() {
		return keys;
	}
	
	/**
	 * @see java.util.HashMap#clear()
	 */
	@Override
	public void clear() {
		keys.clear();
		super.clear();
	}
}