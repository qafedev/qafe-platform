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
package com.qualogy.qafe.bind.resource;

import java.util.List;

import com.qualogy.qafe.bind.commons.type.Parameter;
import com.qualogy.qafe.bind.io.FileLocation;

public class JavaResource extends BindResource{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3189982140829522724L;
	
	protected String className;
	protected FileLocation jarfileLocation;
	protected List<Parameter> arguments;
	
	public List<Parameter> getArguments() {
		return arguments;
	}

	public void setArguments(List<Parameter> arguments) {
		this.arguments = arguments;
	}

	public String getClassName() {
		return className;
	}
	
	public void setClassName(String className) {
		this.className = className;
	}
	
	public FileLocation getJarfileLocation() {
		return jarfileLocation;
	}
	
	public void setJarfileLocation(FileLocation jarfileLocation) {
		this.jarfileLocation = jarfileLocation;
	}
	
}
