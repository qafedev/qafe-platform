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
package com.qualogy.qafe.bind.commons.type;

import java.util.List;

/**
 * interface for objects who have references to data stored in the datastore
 * @author 
 *
 */
public interface ReferencingItem {
	/**
	 * Method must be implemented with a shallow clone, the user of the cloned object
	 * must clone deeper when necessary
	 * @return ReferencingItem
	 * @throws CloneNotSupportedException
	 */
	public Object clone() throws CloneNotSupportedException;

	/**
	 * get the items parameters 
	 * @return
	 */
	public List<Parameter> getParameters();

	/**
	 * method to replace an old param for a new one, will in fact remove the old param
	 * and add the new one. This method does not guarantee a replace action. 
	 * @param oldParam
	 * @param newParam
	 * @return
	 */
	public void replace(Parameter oldParam, Parameter newParam);
}
