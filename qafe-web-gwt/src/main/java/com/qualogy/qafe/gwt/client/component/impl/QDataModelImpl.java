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
package com.qualogy.qafe.gwt.client.component.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.qualogy.qafe.gwt.client.component.DataMap;
import com.qualogy.qafe.gwt.client.component.QDataModel;

public class QDataModelImpl implements QDataModel {

	private List<String> idfields= new ArrayList<String>();
	private Map<Integer, DataMap> dataSet = new HashMap<Integer, DataMap>();
	private Map<Integer, DataMap> modifiedDataSet = new HashMap<Integer, DataMap>();

	
	public DataMap getRow(int row) {
		if (dataSet.containsKey(row)){
			return dataSet.get(row);
		} else {
			return null;
		}
	}

	public void setData(int index,DataMap datamap) {
		dataSet.put(index, datamap);
		
	}

	public DataMap getRowModifiedDataSet(int row) {
		if (modifiedDataSet.containsKey(row)){
			return modifiedDataSet.get(row);
		} else {
			return null;
		}
	}

	public void clear() {
		dataSet.clear();
		modifiedDataSet.clear();		
	}

	public void setModifiedData(int row, DataMap datamap) {
		modifiedDataSet.put(row,datamap);		
	}

	public void addIndentifyingField(String id) {
		idfields.add(id);		
	}

}
