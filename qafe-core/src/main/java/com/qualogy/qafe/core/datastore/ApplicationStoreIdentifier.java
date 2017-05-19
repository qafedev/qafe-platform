/**
 * Copyright 2008-2017 Qualogy Solutions B.V.
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
 * wrapper for identifier strings
 * @author 
 *
 */
public class ApplicationStoreIdentifier {
	
	private String uuid;

	public ApplicationStoreIdentifier(String uuid) {
		this.uuid = uuid;
	}
	
	public String stringValueOf(){
		return uuid;
	}

	public String toString() {
		return uuid;
	}
	
	
}
