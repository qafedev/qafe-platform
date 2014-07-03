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

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.objectweb.asm.ClassReader;

public class ClassInspector {
	
	private final static String DOTCLASS = ".class";
	
	static public Map<String, List<ClassDescriptor>> inspect(String jarFileName, ClassDescriptorFilter classDescriptorFilter) throws IOException {
		JarFile jarFile = new JarFile(jarFileName.trim());
		Map<String, List<ClassDescriptor>> map = inspect(jarFile, classDescriptorFilter);
		jarFile.close();
		return map;
	}
	
	static public Map<String, List<ClassDescriptor>> inspect(JarFile jarFile, ClassDescriptorFilter classDescriptorFilter) throws IOException {
		Map<String, List<ClassDescriptor>> map = new TreeMap<String, List<ClassDescriptor>>();
		Enumeration<JarEntry> enumFile = jarFile.entries();
        while (enumFile.hasMoreElements()) {
            JarEntry jarEntry = enumFile.nextElement();
            String fullClassName = getFullClassNameOfClassEntry(jarEntry);
            if (fullClassName != null) {
            	//System.out.println(fullClassName);
            	ClassDescriptor classDescriptor = inspect(jarFile, jarEntry);
            	if (classDescriptorFilter.filter(classDescriptor)) {
                    String packageName = classDescriptor.getPackageName();
                    List<ClassDescriptor> classDescriptors = (List<ClassDescriptor>) map.get(packageName);
                    if (classDescriptors != null) {
                    	classDescriptors.add(classDescriptor);
                    } else {
                    	classDescriptors = new ArrayList<ClassDescriptor>();
                    	classDescriptors.add(classDescriptor);
                    }
                    Collections.sort(classDescriptors, new CaseInsensitiveClassDescriptorComparator());
                    if (!map.containsKey(packageName)) {
                        map.put(packageName, classDescriptors);
                    }                        	
                }
            }
        }
		return map;
	}
	
	static public ClassDescriptor inspect(JarFile jarFile, JarEntry jarEntry) throws IOException {
		InputStream jarEntryInputStream = jarFile.getInputStream(jarEntry);
		ClassDescriptor classDescriptor = inspect(jarEntryInputStream);
		jarEntryInputStream.close();
		return classDescriptor;
	}
    
	static public ClassDescriptor inspect(InputStream inputStream) throws IOException {
		ClassExtractor classExtractor = new ClassExtractor();
		ClassReader classReader = new ClassReader(inputStream);
		classReader.accept(classExtractor, (ClassReader.SKIP_DEBUG | ClassReader.SKIP_CODE | ClassReader.SKIP_FRAMES));
		return classExtractor.getClassDescriptor();
	}
	
    static private String getFullClassNameOfClassEntry(JarEntry jarEntry) {
    	String fullClassName = ""; 
    	String className = jarEntry.getName();
        String replacedString = className.replace('/', '.');
        int position = replacedString.indexOf(DOTCLASS);
        
        if (position != -1 && ((position + DOTCLASS.length()) == replacedString.length())) {
        	fullClassName = replacedString.substring(0, position);
        }
        if ("".equals(fullClassName)) {
        	return null;
        } else {
        	return fullClassName;
        }
    }
	
	public static class CaseInsensitiveClassDescriptorComparator implements Comparator<ClassDescriptor> {

		public int compare(ClassDescriptor o1, ClassDescriptor o2) {
			return o1.getFullName().compareToIgnoreCase(o2.getFullName());
		}
	}
}
