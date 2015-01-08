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
package com.qualogy.qafe.util.inspection.java;

import java.util.ArrayList;
import java.util.List;

public class ClassDescriptor extends Modifier {
	
	private String packageName;
	private String name;
	private List<ConstructorDescriptor> constructorDescriptors;
	private List<MethodDescriptor> methodDescriptors;
	
	public static class Builder {
		// Required parameters
		private int access;
		private String packageName;
		private String name;
		
		// Optional parameters
		private List<ConstructorDescriptor> constructorDescriptors;
		private List<MethodDescriptor> methodDescriptors;
		
		public Builder(int access, String packageName, String name) {
			this.access = access;
			this.packageName = packageName;
			this.name = name;
			this.constructorDescriptors = new ArrayList<ConstructorDescriptor>();
			this.methodDescriptors = new ArrayList<MethodDescriptor>();
		}
		
		public Builder addConstructorDescriptor(ConstructorDescriptor constructorDescriptor) {
			constructorDescriptors.add(constructorDescriptor);
			return this;
		}
		
		public Builder addMethodDescriptor(MethodDescriptor methodDescriptor) {
			methodDescriptors.add(methodDescriptor);
			return this;
		}
		
		public ClassDescriptor build() {
			return new ClassDescriptor(this);
		}
	}
	
	private ClassDescriptor(Builder builder) {
		super(builder.access);
		this.packageName = builder.packageName;
		this.name = builder.name;
		this.constructorDescriptors = builder.constructorDescriptors;
		this.methodDescriptors = builder.methodDescriptors;
	}
	
	public String getPackageName() {
		return packageName;
	}
	
	public String getName() {
		return name;
	}
	
	public List<ConstructorDescriptor> getConstructorDescriptors() {
		return constructorDescriptors;
	}
	
	public List<MethodDescriptor> getMethodDescriptors() {
		return methodDescriptors;
	}
	
	public String getFullName() {
		return getPackageName() + "." + getName();
	}
}
