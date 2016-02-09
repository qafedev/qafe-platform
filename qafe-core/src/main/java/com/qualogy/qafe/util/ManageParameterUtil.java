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
package com.qualogy.qafe.util;

import com.qualogy.qafe.bind.commons.type.Parameter;
import com.qualogy.qafe.bind.commons.type.Reference;
import com.qualogy.qafe.bind.commons.type.Value;

/**
 * This Utility class is used to manipulate Parameter object
 */

public class ManageParameterUtil {
	
	/**
	 * Method get a refId of type String, creates a Reference object,
	 * assigns reference value for it and assigns to the newly created 
	 * parameter object which is the return value.
	 * @param String refId
	 * @return Parameter
	 */
	public static Parameter getParameterObjWithReference(String refId) {
		Parameter parameter = new Parameter();
		Reference reference = new Reference();
		reference.setRef(refId);
		parameter.setRef(reference);
		return parameter;
	}
	
	/**
	 * Method get a value of type String, creates a Value object,
	 * assigns value for it and then assigns to the newly created 
	 * parameter object which is the return value.
	 * @param String val
	 * @return Parameter
	 */
	public static Parameter getParameterObjWithValue(String val) {
		Parameter parameter = new Parameter();
		Value value = new Value();
		value.setStaticValue(val);
		parameter.setValue(value);
		return parameter;
	}
	
	/**
	 * Method get a src of type String, creates a Reference object, 
	 * assigns the source for the component type and assigns to the 
	 * newly created parameter object which is the return value.
	 * @param String src
	 * @return Parameter
	 */
	public static Parameter getParameterObjWithComponent(String src) {
		Parameter parameter = new Parameter();
		Reference reference = new Reference();
		reference.setSource(src);
		parameter.setRef(reference);
		return parameter;
	}

}
