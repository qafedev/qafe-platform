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

import java.util.HashMap;
import java.util.Map;

public class DummyPersonMoreComplex extends DummyPerson {
	
	Map<String,String> members = new HashMap<String,String>();
	
	public DummyPersonMoreComplex(){
	
	}
	
	public DummyPersonMoreComplex(String name, String lastName){
		super(name,lastName);
	}
	
	public void add(String key,String value){
		members.put(key,value);
	}
}
