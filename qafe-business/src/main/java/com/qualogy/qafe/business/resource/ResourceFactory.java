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
package com.qualogy.qafe.business.resource;

import com.qualogy.qafe.bind.resource.BindResource;
import com.qualogy.qafe.bind.resource.DatasourceBindResource;
import com.qualogy.qafe.bind.resource.JavaResource;
import com.qualogy.qafe.bind.resource.SpringContextResource;
import com.qualogy.qafe.business.resource.java.JavaClass;
import com.qualogy.qafe.business.resource.java.spring.SpringContext;
import com.qualogy.qafe.business.resource.rdb.RDBDataSourceFactory;

/**
 * factory used to create resources
 * @author mvanderwurff
 *
 */
public class ResourceFactory {
	
	/**
	 * creates and initializes resource
	 * @param type
	 * @param properties
	 * @return
	 */
	public static Resource create(BindResource bindResource){
		Resource resource = null;
		
		if (bindResource == null) {
			throw new IllegalArgumentException("cannot create a resource for null binding");
		}
		
		if (bindResource instanceof DatasourceBindResource) {
			resource = RDBDataSourceFactory.create(bindResource);
		} else if (bindResource instanceof JavaResource) {
			resource = new JavaClass(bindResource);
		} else if (bindResource instanceof SpringContextResource) {
			resource = new SpringContext((SpringContextResource)bindResource);
		} else {
			throw new UnImplementedResourceTypeException("cannot create resource for type ["+bindResource+"]");
		}
		
		return resource;
	}
}
