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
package com.qualogy.qafe.business.resource.rdb.query.enhancer;

import com.qualogy.qafe.bind.resource.query.Call;
import com.qualogy.qafe.bind.resource.query.Insert;
import com.qualogy.qafe.bind.resource.query.Query;
import com.qualogy.qafe.bind.resource.query.Update;

public class EnhancerFactory {
	public static Enhancer create(Query query){
		Enhancer enhancer = null;
		if(query instanceof Update){
			enhancer = new SQLQueryEnhancer();
		}else if(query instanceof Insert){
			enhancer = new SQLQueryEnhancer();
		}else if(query instanceof Call){
			enhancer = new CallEnhancer();
		}
		return enhancer;
	}
}
