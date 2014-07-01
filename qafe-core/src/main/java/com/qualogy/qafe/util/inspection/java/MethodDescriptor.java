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
package com.qualogy.qafe.util.inspection.java;

public class MethodDescriptor extends Modifier {
	
	private String returnTypeName;
	private String name;
	private ParameterDescriptor[] parameterDescriptors;
	
	private String methodSignature;
	
	public MethodDescriptor(int access, String returnTypeName, String name, ParameterDescriptor[] parameterDescriptors) {
		super(access);
		this.returnTypeName = returnTypeName;
		this.name = name;
		this.parameterDescriptors = parameterDescriptors;
		this.methodSignature = new StringBuilder(getReturnTypeName()).append(' ').append(getName()).append(getParametersAsString()).toString();
	}
	
	public String getReturnTypeName() {
		return returnTypeName;
	}
	
	public String getName() {
		return name;
	}
	
	public ParameterDescriptor[] getParameterDescriptors() {
		return parameterDescriptors;
	}
	
	public String getMethodSignature() {
		return methodSignature;
	}
	
	protected String getParametersAsString() {
		StringBuilder parameterString = new StringBuilder("(");
		for (ParameterDescriptor parameterDescriptor:parameterDescriptors) {
			int parameterOrder = parameterDescriptor.getOrder();
			if (parameterOrder == (parameterDescriptors.length - 1)) {
				parameterString.append(parameterDescriptor.getTypeName()).append(' ').append(parameterOrder);
            } else {
            	parameterString.append(parameterDescriptor.getTypeName()).append(' ').append(parameterOrder).append(',');
            }
		}
		parameterString.append(')');
		return parameterString.toString();
	}
}
