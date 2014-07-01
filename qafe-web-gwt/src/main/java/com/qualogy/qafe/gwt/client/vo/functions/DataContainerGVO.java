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
package com.qualogy.qafe.gwt.client.vo.functions;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.qualogy.qafe.gwt.client.component.DataMap;

public class DataContainerGVO implements IsSerializable {

	public static String ROW_STATUS_KEY = "rowStatus";
	public static String ROW_NUMBER = "rowNumber";
	public static String ROW_STATUS_UNMODIFIED = "$$UNMODIFIED";
	public static String ROW_STATUS_NEW = "$$NEW";
	public static String ROW_STATUS_DELETED = "$$DELETED";
	public static String ROW_STATUS_MODIFIED = "$$MODIFIED";
	public static String ROW_STATUS_UNSELECTED = "$$UNSELECTED";
	public static String ROW_STATUS_ALL = "$$ALL";

	public static final String QAFE_CHECKSUM = "QAFE_CHECKSUM";

    public static final int KIND_VALUE = -1;
    public static final int KIND_STRING = 0;
    public static final int KIND_MAP = 1;
    public static final int KIND_COLLECTION = 5;
    
    public static final int TYPE_INT = 0;
    public static final int TYPE_DOUBLE = 1;
    public static final int TYPE_BOOLEAN = 2;
    public static final int TYPE_CHAR = 3;
    public static final int TYPE_STRING = 4;
    public static final int TYPE_BYTE = 5;
    public static final int TYPE_BIGDECIMAL = 6;
    public static final int TYPE_BIGINTEGER = 7;
    public static final int TYPE_FLOAT = 8;
    public static final int TYPE_LONG = 9;
    public static final int TYPE_SHORT = 10;
    public static final int TYPE_DATE = 11;
    
	private String dataString;
    private String parameterName;
    private DataMap dataMap;
    private int kind = KIND_VALUE;
    private int stringDataType = TYPE_STRING;
    private Date dateData = null;
    private List<DataContainerGVO> listofDC;

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
				if(isEmptyOrNullNumberType(data)) {
					// If the value is empty or null for a number type then parsing to corresponding type  will give exception.
					returnValue = data.getDataString();
				} else {
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
						/**
						 *  If the databse column is of type NUMBER type and if the value from the database is an integer value (10) ,
						 *  then the datacontainer stringDataType is set as TYPE_INT. (Check SetValueBuiltInRenderer - createContainer method)
						 *  Now if that value is updated by the user in the front-end as a decimal value (10.2) it is giving exception while
						 *  converting to Integer. In that case we should convert that value to Double.
						 *
						 */
						try {
							returnValue = new Integer(data.getDataString());
						} catch(Exception e){
							returnValue = new Double(data.getDataString());
						}
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
		}

		return returnValue;
	}
    // CHECKSTYLE.ON: CyclomaticComplexity
	
    // CHECKSTYLE.OFF: CyclomaticComplexity
    public static DataContainerGVO create(Object object) {
        DataContainerGVO data = null;
        if (object != null) {
            if (object instanceof DataContainerGVO) {
                DataContainerGVO dc = (DataContainerGVO) object;
                data = create(DataContainerGVO.createType(dc));
            } else {
                data = new DataContainerGVO();
                boolean valueIsSet = false;
                if (object instanceof Boolean || object instanceof Character || object instanceof String || object instanceof Date || object instanceof Number) {
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
                                int intValue = Integer.parseInt(object.toString());
                                data.setStringDataType(DataContainerGVO.TYPE_INT);
                                data.setDataString("" + intValue);
                                valueIsSet = true;
                            } catch (Exception e) {
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

                    if (!valueIsSet) {
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
                        list.add(create(o));
                    }
                }
            }
        }
        return data;
    }
    // CHECKSTYLE.ON: CyclomaticComplexity
    
    private static void processMap(Object returnedObject, DataContainerGVO data) {
        data.setDataMap(processMap(returnedObject));
        data.setKind(DataContainerGVO.KIND_MAP);
    }

    private static DataMap processMap(Object returnedObject) {
        Map<String, Object> map = (Map<String, Object>) returnedObject;
        DataMap dataMap = new DataMap();
        for (Map.Entry<String,Object> entry: map.entrySet()){
            dataMap.put(entry.getKey(),create(entry.getValue()));
        }
        return dataMap;
    }
    
	private static boolean isEmptyOrNullNumberType(DataContainerGVO data) {
		boolean emptyNumberType = false;
		switch (data.getStringDataType()) {
		case TYPE_BYTE:
		case TYPE_BIGDECIMAL:
		case TYPE_BIGINTEGER:
		case TYPE_DOUBLE:
		case TYPE_FLOAT:
		case TYPE_LONG:
		case TYPE_SHORT:
		case TYPE_INT:
			if(data.getDataString() == null || data.getDataString().isEmpty()) {
				emptyNumberType = true;
			}
			break;
		}
		return emptyNumberType;
	}

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

    public boolean equalsValue(Object object) {
        if (this == object) {
            return true;
        }    
        if (object == null) {
            return false;
        }    
        if (getClass() != object.getClass()) {
            return false;
        }    
        DataContainerGVO other = (DataContainerGVO)object;
        Object thisValue = createType();
        Object otherValue = other.createType();
        if ((thisValue == null) && (otherValue != null)) {
            return false;
        }
        if ((thisValue != null) && (otherValue == null)) {
            return false;
        }
        if ((thisValue == null) && (otherValue == null)) {
            return true;
        }
        return thisValue.equals(otherValue);
    }
}