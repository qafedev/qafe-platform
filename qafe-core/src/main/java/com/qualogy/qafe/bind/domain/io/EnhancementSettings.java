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
package com.qualogy.qafe.bind.domain.io;

public class EnhancementSettings {
	private boolean addOrder = false;
	
	public static EnhancementSettings create(boolean addOrder){
		EnhancementSettings settings = new EnhancementSettings();
		settings.addOrder = addOrder;
		return settings;
	}
	public boolean addOrder(){
		return addOrder;
	}
	/**
	 * method to check if any enhancement is needed based upon the settings
	 * @return
	 */ 
	public boolean isSetToEnhance(){
		return addOrder;
	}
}
