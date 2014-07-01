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
package com.qualogy.qafe.bind;

/**
 * Convinience interface to add validation support to bind domain,
 * since either not all validation is possible in third party bind mechanism
 * or it's simply to hard to implement in the 3rd party framework.
 * @author 
 *
 */
public interface Validatable {
	
	/**
	 * Class who implement this interface are validatable by implementing this method.
	 * This method throws an exception if one of its validation rules do not
	 * comply with the objects internal state.
	 * @throws ValidationException if the objects internal state does not comply with the 
	 * validationrules for the domain class
	 */
	abstract void validate() throws ValidationException;
}
