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
package com.qualogy.qafe.bind.resource;

public class JNDIDatasourceResource extends DatasourceBindResource{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1410825060025580242L;
	
	protected String jndiname;

	public String getJndiname() {
		return jndiname;
	}

	public void setJndiname(String jndiname) {
		this.jndiname = jndiname;
	}
}
