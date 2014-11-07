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
package com.qualogy.qafe.core.datastore;

/**
 * Holder for DataObjects within a Data object. This object is intended to hold the actual data
 * within the object and the original type info. F.i. if data is mapped to a Map from a Car type,
 * type will be Car
 * @author 
 *
 */

public class DataObject {
	private Object object;
	private String type;
	public DataObject(Object object, String type) {
		super();
		this.object = object;
		this.type = type;
	}
	public Object toObject() {
		return object;
	}
	public String getType() {
		return type;
	}
}
