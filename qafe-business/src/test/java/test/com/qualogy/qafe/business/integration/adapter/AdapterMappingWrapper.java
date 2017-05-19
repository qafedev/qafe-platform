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
package test.com.qualogy.qafe.business.integration.adapter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.qualogy.qafe.bind.commons.type.AdapterMapping;

public class AdapterMappingWrapper {
	private List adapters;
	
	public final static String ADAPTER_ID_ONLY = "1";
	public final static String ADAPTER_NOATTRS_ADAPTALLTRUE = "1";
	
	public static AdapterMappingWrapper create(){
		AdapterMappingWrapper wrapper = new AdapterMappingWrapper();
		wrapper.adapters = new ArrayList();
		
		AdapterMapping mapping1 = new AdapterMapping();
		mapping1.setId(ADAPTER_ID_ONLY);
		
		wrapper.adapters.add(mapping1); 
		
		AdapterMapping mapping2 = new AdapterMapping();
		mapping2.setId(ADAPTER_NOATTRS_ADAPTALLTRUE);
		mapping2.setAdaptAll(Boolean.TRUE);
		wrapper.adapters.add(mapping2); 
		
		return wrapper;
	}
	
	public AdapterMapping get(String mappingName){
		AdapterMapping mapping = null;
		for (Iterator iter = adapters.iterator(); mappingName!=null && iter.hasNext();) {
			AdapterMapping tmp = (AdapterMapping) iter.next();
			if(tmp.getId().equals(mappingName)){
				mapping = tmp;
				break;
			}
		}
		return mapping;
	}
}
