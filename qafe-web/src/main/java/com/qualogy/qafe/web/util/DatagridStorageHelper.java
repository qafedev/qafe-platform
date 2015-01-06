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
package com.qualogy.qafe.web.util;

import java.util.List;
import java.util.Map;

import com.qualogy.qafe.core.datastore.ApplicationLocalStore;
import com.qualogy.qafe.util.UUIDHelper;

public class DatagridStorageHelper {

	private final static String EXPORT_TYPE="TYPE";
	private final static String EXPORT_HEADER="HEADER";
	private final static String EXPORT_GENERATECOLUMNHEADER="GENERATECOLUMNHEADER";
	private final static String EXPORT_COLUMNSEQUENCE="COLUMNSEQUENCE";
	
	private DatagridStorageHelper(){}
	
	public static String storeDataGridData(List<Map<String,Object>> data, String exportCode,String header, boolean generateColumnHeader){
		return storeDataGridData(data, null, exportCode, header, generateColumnHeader);
	}
	
	public static String storeDataGridData(List<Map<String,Object>> data, List<String> columnSequence, String exportCode,String header, boolean generateColumnHeader){
		String uuid = UUIDHelper.generateUUID();
		ApplicationLocalStore.getInstance().store(uuid, uuid, data);
		ApplicationLocalStore.getInstance().store(uuid, uuid+EXPORT_COLUMNSEQUENCE, columnSequence);
		ApplicationLocalStore.getInstance().store(uuid, uuid+EXPORT_TYPE, exportCode);
		ApplicationLocalStore.getInstance().store(uuid, uuid+EXPORT_HEADER, header);
		ApplicationLocalStore.getInstance().store(uuid, uuid+EXPORT_GENERATECOLUMNHEADER,Boolean.valueOf(generateColumnHeader).toString());
		return uuid;
	}
	
	public static List<Map<String,Object>> getData(String uuid){
		Object   o = ApplicationLocalStore.getInstance().retrieve(uuid, uuid);
		if (o instanceof List<?>){
			return (List<Map<String,Object>>)o;
		}
		return null;
	}
	
	public static List<String> getColumnSequence(String uuid){
		Object   o = ApplicationLocalStore.getInstance().retrieve(uuid, uuid+EXPORT_COLUMNSEQUENCE);
		if (o instanceof List<?>){
			return (List<String>)o;
		}
		return null;
	}
	
	public static String getExportType(String uuid){
		Object   o = ApplicationLocalStore.getInstance().retrieve(uuid, uuid+EXPORT_TYPE);
		if (o instanceof String){
			return o.toString();
		}
		return null;
	}
	
	public static String getHeader(String uuid){
		Object   o = ApplicationLocalStore.getInstance().retrieve(uuid, uuid+EXPORT_HEADER);
		if (o instanceof String){
			return o.toString();
		}
		return null;	
	}
	
	public static boolean isGenerateColumnHeader(String uuid){
		Object   o = ApplicationLocalStore.getInstance().retrieve(uuid, uuid+EXPORT_GENERATECOLUMNHEADER);
		if (o instanceof String){
			return Boolean.valueOf(o.toString()).booleanValue();
		}
		return false;	
	}
	
	public static void clear(String uuid){
		ApplicationLocalStore.getInstance().delete(uuid,uuid);
		ApplicationLocalStore.getInstance().delete(uuid,uuid+EXPORT_TYPE);
		
	}
}
