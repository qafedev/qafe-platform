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
package com.qualogy.qafe.core.script;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import com.qualogy.qafe.bind.core.application.ApplicationContext;
import com.qualogy.qafe.bind.core.messages.PlaceHolder;
import com.qualogy.qafe.core.datastore.ApplicationStoreIdentifier;
import com.qualogy.qafe.core.datastore.DataIdentifier;
import com.qualogy.qafe.core.datastore.ParameterValueHandler;

public class Script {

	public static final String IDENTIFIER_PREFIX = "_qidpfx_";
	
	private String expression;
	private Map parameters = new HashMap();
	
	public Script(String value) {
		this.expression = value;
	}
	
	public Script(String value, Map parameters) {
		this.expression = value;
		this.parameters = parameters;
	}
	
	public Map getParameters() {
		return parameters;
	}
	
	public String getValue() {
		return expression;
	}
		
	public void addAll(ApplicationContext context, ApplicationStoreIdentifier storeId, DataIdentifier dataId, List placeHolders, String localStoreId){
		
		for (Iterator iter = placeHolders.iterator(); iter.hasNext();) {
			PlaceHolder placeHolder = (PlaceHolder)iter.next();
			if(placeHolder!=null){
				Object placeHolderValue = ParameterValueHandler.get(context, storeId, dataId, placeHolder, localStoreId);
				
				if(placeHolderValue instanceof String && NumberUtils.isNumber((String)placeHolderValue)){
					placeHolderValue = NumberUtils.createNumber((String)placeHolderValue);
				}
				if(placeHolderValue instanceof BigDecimal){
					placeHolderValue = new Double(((BigDecimal)placeHolderValue).doubleValue());
				}
				
				String scriptKey = placeHolder.getName();
				scriptKey = IDENTIFIER_PREFIX + parameters.keySet().size();
				
				expression = StringUtils.replace(expression, placeHolder.getPlaceHolderKey(), scriptKey);
				parameters.put(scriptKey, placeHolderValue);
			}
		}
	}
	
	public String toString(){
		String s = "Expression ["+expression+"] Parameters [";
		for (Iterator iter = parameters.keySet().iterator(); iter.hasNext();) {
			String key = (String) iter.next();
			s += key + "="+parameters.get(key)+((parameters.get(key)!=null)?parameters.get(key).getClass():null);
			if(iter.hasNext())
				s += ", ";
		}
		s += "]";
		return s;
	}
}
