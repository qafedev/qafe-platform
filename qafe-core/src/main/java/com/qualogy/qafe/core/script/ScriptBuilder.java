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
package com.qualogy.qafe.core.script;

import java.util.List;

import com.qualogy.qafe.bind.commons.type.Parameter;
import com.qualogy.qafe.bind.core.application.ApplicationContext;
import com.qualogy.qafe.core.datastore.ApplicationStoreIdentifier;
import com.qualogy.qafe.core.datastore.DataIdentifier;
import com.qualogy.qafe.core.placeholder.PlaceHolderResolver;

public class ScriptBuilder {
	
	
	public static Script build(ApplicationContext context, ApplicationStoreIdentifier storeId, DataIdentifier dataId, String value, Parameter parameter, String localStoreId){
		Script script = new Script(value);
	
		if(parameter.hasPlaceHolders()){
			script.addAll(context, storeId, dataId, parameter.getPlaceHolders(), localStoreId);
		}
		
		List placeHolders = PlaceHolderResolver.createPlaceholders(script.getValue());
		script.addAll(context, storeId, dataId, placeHolders, localStoreId);
		return script;
	}
}
