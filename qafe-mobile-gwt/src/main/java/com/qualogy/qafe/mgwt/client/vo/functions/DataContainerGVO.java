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
package com.qualogy.qafe.mgwt.client.vo.functions;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.qualogy.qafe.mgwt.client.component.DataMap;

public class DataContainerGVO implements IsSerializable {

	public static String ROW_STATUS_KEY = "rowStatus";
	public static String ROW_NUMBER = "rowNumber";
	public static String ROW_STATUS_UNMODIFIED = "$$UNMODIFIED";
	public static String ROW_STATUS_NEW = "$$NEW";
	public static String ROW_STATUS_DELETED = "$$DELETED";
	public static String ROW_STATUS_MODIFIED = "$$MODIFIED";

	public DataContainerGVO(){}

	public DataContainerGVO(String value){
		setKind(KIND_STRING);
		setDataString(value);
	}
	public DataContainerGVO(DataMap map){
		dataMap = map;
		setKind(KIND_MAP);
	}

	public boolean isMap(){
		return (kind==KIND_MAP);
	}

	public DataContainerGVO(DataContainerGVO clone){
		if(clone!=null){
			this.kind = clone.kind;
			this.dataString = clone.dataString;
			this.parameterName = clone.parameterName;
			this.dateData = clone.dateData;

			if (clone.dataMap!=null){
				this.dataMap = (DataMap)dataMap.clone();
			}
			if (clone.listofDC!=null){
				this.listofDC = new ArrayList<DataContainerGVO>();
				for(DataContainerGVO dcg: clone.listofDC){
					this.listofDC.add(new DataContainerGVO(dcg));
				}
			}
		}
	}

	private String dataString;

	private String parameterName;

	private DataMap dataMap;


	public final static int KIND_VALUE = -1;

	public final static int KIND_STRING = 0;

	public final static int KIND_MAP = 1;

	public final static int KIND_COLLECTION = 5;



	private int kind = KIND_VALUE;

	public final static int TYPE_INT = 0;
	public final static int TYPE_DOUBLE = 1;
	public final static int TYPE_BOOLEAN = 2;
	public final static int TYPE_CHAR = 3;
	public final static int TYPE_STRING = 4;
	public static final int TYPE_BYTE = 5;
	public static final int TYPE_BIGDECIMAL = 6;
	public static final int TYPE_BIGINTEGER = 7;
	public static final int TYPE_FLOAT = 8;
	public static final int TYPE_LONG = 9;
	public static final int TYPE_SHORT = 10;
	public static final int TYPE_DATE = 11;

	private int stringDataType = TYPE_STRING;

	private Date dateData = null;

	private List<DataContainerGVO> listofDC;

	public List<DataContainerGVO> getListofDC() {
		return listofDC;
	}

	public void setListofDC(List<DataContainerGVO> listofDC) {
		this.listofDC = listofDC;
	}

	public Date getDateData() {
		return dateData;
	}

	public void setDateData(Date dateData) {
		this.dateData = dateData;
	}

	public String getDataString() {
		return dataString;
	}

	public void setDataString(String dataString) {
		this.dataString = dataString;
	}

	public int getKind() {
		return kind;
	}

	public void setKind(int kind) {
		this.kind = kind;
	}

	public DataMap getDataMap() {
		return dataMap;
	}

	public void setDataMap(DataMap dataMap) {
		this.dataMap = dataMap;
	}



	public String getParameterName() {
		return parameterName;
	}

	public void setParameterName(String parameterName) {
		this.parameterName = parameterName;
	}



	public int getStringDataType() {
		return stringDataType;
	}

	public void setStringDataType(int stringDataType) {
		this.stringDataType = stringDataType;
	}

	public static String resolveValue(DataContainerGVO vo) {
		String returnValue = null;
		if (vo != null) {
			if (vo.getKind() == DataContainerGVO.KIND_STRING) {
				returnValue = vo.getDataString();
			}
		}
		return returnValue;
	}

	public Object createType() {
		return createType(this);
	}

    // CHECKSTYLE.OFF: CyclomaticComplexity
	public static Object createType(DataContainerGVO data) {
		Object returnValue = null;
		if (data != null) {
			if (data.getKind() == DataContainerGVO.KIND_MAP) {
				//returnValue = data.getDataMap();
				HashMap<String, Object> map = new HashMap<String, Object>();
				for(String key : data.getDataMap().keySet()) {
					map.put(key, createType(data.getDataMap().get(key)));
				}
				returnValue = map;
			} else if (data.getKind()==DataContainerGVO.KIND_COLLECTION){
				//returnValue = data.getListofDC();
				List<Object> listOfMap = new ArrayList<Object>();
				for(DataContainerGVO gvo : data.getListofDC()) {
					listOfMap.add(createType(gvo));
				}
				returnValue = listOfMap;
			}
			else {
				switch (data.getStringDataType()) {
				case TYPE_BOOLEAN:
					returnValue = new Boolean(data.getDataString());
					break;
				case TYPE_CHAR:
					returnValue = Character.valueOf(' ');
					break;
				case TYPE_STRING:
					returnValue = data.getDataString();
					break;
				case TYPE_DATE:
					returnValue = data.getDateData();
					break;
				case TYPE_BYTE:
					returnValue = new Byte(data.getDataString());
					break;
				case TYPE_BIGDECIMAL:
					returnValue = new Long(data.getDataString());
					break;
				case TYPE_BIGINTEGER:
					returnValue = new Long(data.getDataString());
					break;
				case TYPE_DOUBLE:
					returnValue = new Double(data.getDataString());
					break;
				case TYPE_FLOAT:
					returnValue = new Float(data.getDataString());
					break;
				case TYPE_INT:
					returnValue = new Integer(data.getDataString());
					break;
				case TYPE_LONG:
					returnValue = new Long(data.getDataString());
					break;
				case TYPE_SHORT:
					returnValue = new Short(data.getDataString());
					break;

				default:
					break;
				}
			}
		}

		return returnValue;
	}
    // CHECKSTYLE.ON: CyclomaticComplexity

	@Override
	public String toString() {
		switch (getKind()) {
		case KIND_STRING:
			if (getDataString() != null) {
				return getDataString();
			} else {
				return "null";
			}
		case KIND_MAP:
			if (getDataMap() != null) {
				return getDataMap().toString();
			} else {
				return "null";
			}
		case KIND_COLLECTION:
			if (getListofDC() != null) {
				return getListofDC().toString();
			} else {
				return "null";
			}


		default:
			return "<not-assigned>";
		}

	}

	public void clear(){
		switch (getKind()) {
		case KIND_STRING: break;

		case KIND_MAP:
			if (getDataMap() != null) {
				for(Entry<String,DataContainerGVO> entry:getDataMap().entrySet()){
					if (entry.getValue()!=null){
						entry.getValue().clear();
					}
				}
				getDataMap().clear();
			} break;
		case KIND_COLLECTION:
			if (getListofDC() != null) {
				for (DataContainerGVO dcvo : getListofDC()){

					dcvo.clear();
					}
				}

				getListofDC().clear();
				break;

		default:
			break;
		}
	}

}
