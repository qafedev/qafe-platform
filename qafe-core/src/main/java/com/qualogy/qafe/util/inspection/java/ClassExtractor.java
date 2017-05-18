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
package com.qualogy.qafe.util.inspection.java;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

public class ClassExtractor  implements ClassVisitor {
	
	private static final String COMPILED_CONSTRUCTOR_NAME = "<init>";
	private ClassDescriptor.Builder builder;
	
	public ClassDescriptor getClassDescriptor() {
		if (builder != null) {
			return builder.build();
		} else {
			throw new IllegalStateException();
		}
	}
	
	public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
		//System.out.println("########################");
		//System.out.println(name + " extends " + superName + " {");
		
		String fullClassName = name.replace('/', '.');//Type.getObjectType(name).getClassName()
		int lastIndex = fullClassName.lastIndexOf('.');
		String packageName = fullClassName.substring(0, lastIndex);
		String className = fullClassName.substring(lastIndex + 1);
		builder = new ClassDescriptor.Builder(access, packageName, className);
	}

	public AnnotationVisitor visitAnnotation(String arg0, boolean arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	public void visitAttribute(Attribute arg0) {
		// TODO Auto-generated method stub
		return;
	}

	public void visitEnd() {
		//System.out.println("}");
		//System.out.println("########################");
	}

	public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
		//System.out.println(" " + desc + " " + name);
		return null;
	}

	public void visitInnerClass(String arg0, String arg1, String arg2, int arg3) {
		// TODO Auto-generated method stub
		return;
	}

	public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
		//System.out.println(" " + name + desc);
		Type[] types = Type.getArgumentTypes(desc);
		ParameterDescriptor[] parameterDescriptors = new ParameterDescriptor[types.length];
		int index = 0;
		for (Type type:types) {
			String typeName = type.getClassName();
			parameterDescriptors[index] = new ParameterDescriptor(index, typeName);
			index++;
		}
		if (isConstructor(name)) {
			ConstructorDescriptor constructorDescriptor = new ConstructorDescriptor(access, name, parameterDescriptors);
			builder.addConstructorDescriptor(constructorDescriptor);
		} else {
			String returnTypeName = Type.getReturnType(desc).getClassName();
			MethodDescriptor methodDescriptor = new MethodDescriptor(access, returnTypeName, name, parameterDescriptors);
			builder.addMethodDescriptor(methodDescriptor);
		}
		return null;
	}

	public void visitOuterClass(String arg0, String arg1, String arg2) {
		// TODO Auto-generated method stub
		return;
	}

	public void visitSource(String arg0, String arg1) {
		// TODO Auto-generated method stub
		return;
	}
	
	public static boolean isConstructor(String compiledName) {
		return COMPILED_CONSTRUCTOR_NAME.equals(compiledName);
	}
}