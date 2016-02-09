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
package com.qualogy.qafe.core.placeholder;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import org.apache.commons.lang.StringUtils;

import com.qualogy.qafe.bind.commons.type.Parameter;
import com.qualogy.qafe.bind.commons.type.Reference;
import com.qualogy.qafe.bind.core.application.ApplicationContext;
import com.qualogy.qafe.bind.core.messages.PlaceHolder;
import com.qualogy.qafe.core.datastore.ApplicationStoreIdentifier;
import com.qualogy.qafe.core.datastore.DataIdentifier;
import com.qualogy.qafe.core.datastore.ParameterValueHandler;

public class PlaceHolderResolver {
	
	public final static Logger logger = Logger.getLogger(PlaceHolderResolver.class.getName());
	
	public static Object resolve(ApplicationContext context, ApplicationStoreIdentifier storeId, DataIdentifier dataId, Object value, Parameter parameter) {
		return resolve(context, storeId, dataId, value, parameter, null); 
	}
	
	public static Object resolve(ApplicationContext context, ApplicationStoreIdentifier storeId, DataIdentifier dataId, Object value, Parameter parameter, String localStoreId) {
		if ((value != null) && (parameter != null) && parameter.hasPlaceHolders()) {
			if (value instanceof String) {
				value = implementPlaceholders(context, storeId, dataId, (String)value, parameter.getPlaceHolders(), localStoreId);
			} else {
				logger.warning("Cannot resolve placeholders on a [" + (value!=null ? value.getClass().getName() : null) + "] objecttype");
			}	
		}
		if (value instanceof String) {
			List placeHolders = createPlaceholders((String)value);
			value = implementPlaceholders(context, storeId, dataId, (String)value, placeHolders, localStoreId);
		}
		return value;
	}
	
	private static String implementPlaceholders(ApplicationContext context, ApplicationStoreIdentifier storeId, DataIdentifier dataId, String value, List placeHolders, String localStoreId) {
		for (Iterator iter = placeHolders.iterator(); iter.hasNext();) {
			PlaceHolder placeHolder = (PlaceHolder)iter.next();
			if(placeHolder!=null){
				Object placeHolderValue = ParameterValueHandler.get(context, storeId, dataId, placeHolder, localStoreId);
				
				if(placeHolderValue==null)
					placeHolderValue = "";
				
				value = StringUtils.replace(value, placeHolder.getPlaceHolderKey(), placeHolderValue.toString());
			}
		}
		return value;
	}
	/**
	 * try to replace placeholders with reference from datastore 
	 */
	public static List createPlaceholders(String value){
		List placeHolders = new ArrayList();
		if(value.indexOf(PlaceHolder.PREFIX)>-1 && value.indexOf(PlaceHolder.POSTFIX)>-1){
			int from = 0;
		
			while(value.indexOf(PlaceHolder.PREFIX, from)>-1 && value.indexOf(PlaceHolder.POSTFIX, value.indexOf(PlaceHolder.PREFIX, from))>-1){
				int nameFrom = value.indexOf(PlaceHolder.PREFIX, from)+PlaceHolder.PREFIX.length();
				int nameTo = value.indexOf(PlaceHolder.POSTFIX, nameFrom);
				String name = value.substring(nameFrom, nameTo);
				placeHolders.add(new PlaceHolder(name, new Reference(name)));
				
				from = nameTo;
			}
		}
		return placeHolders;
	}
}
