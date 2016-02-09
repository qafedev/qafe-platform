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
package com.qualogy.qafe.presentation.builtins;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gson.Gson;
import com.qualogy.qafe.bind.presentation.event.function.BuiltInFunction;
import com.qualogy.qafe.bind.presentation.event.function.Clear;
import com.qualogy.qafe.bind.presentation.event.function.Focus;
import com.qualogy.qafe.bind.presentation.event.function.SetProperty;
import com.qualogy.qafe.core.datastore.DataStore;

public class BuiltinConvertorImpl implements BuiltinConvertor {
	
	private static final Map<String,Class<?>> BUILTIN_CONVERTER_MAP = new HashMap<String,Class<?>>();	
    static {
    	BUILTIN_CONVERTER_MAP.put("set-property", SetProperty.class);
    	BUILTIN_CONVERTER_MAP.put("clear", Clear.class); 
    	BUILTIN_CONVERTER_MAP.put("focus", Focus.class); 
	}
    
    Gson gson = new Gson();
	
	public List<BuiltInFunction> convert(String builtInData) {
		Logger.getLogger(this.getClass().getName()).log(Level.INFO,"Converting builtins from backend : "+ builtInData);
		List<BuiltInFunction> builtInFunctions = new ArrayList<BuiltInFunction>();
		BuiltInFunction builtInFunction = null;
		if(builtInData != null) {
			String[] builtins = builtInData.split("#,#");
			for(String builtin: builtins) {
				String[] data = builtin.split("#=#");
				if(data != null && data.length == 2) {
					String builtInType = data[0];
					String builtInGsonData = data[1];
					Class<?> builtinClass = BUILTIN_CONVERTER_MAP.get(builtInType);
					if(builtinClass != null) {
						builtInFunction = (BuiltInFunction) gson.fromJson(builtInGsonData, builtinClass);
						builtInFunctions.add(builtInFunction);
					}
				}
			}
		}
		return builtInFunctions;
	}
}
