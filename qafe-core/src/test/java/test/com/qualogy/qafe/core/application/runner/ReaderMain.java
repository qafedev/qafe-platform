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
package test.com.qualogy.qafe.core.application.runner;


import java.net.URI;

import org.apache.commons.lang.StringUtils;

import com.qualogy.qafe.bind.domain.ApplicationMapping;
import com.qualogy.qafe.bind.io.Reader;
import com.qualogy.qafe.bind.resource.BindResource;
import com.qualogy.qafe.bind.resource.JavaResource;
import com.qualogy.qafe.util.ClassUtils;

public class ReaderMain {
	
	private static String getSamplesDir(){
		String pckName = ClassUtils.getPackageName(ReaderMain.class);
		return StringUtils.replace(pckName, ".", "/") + "/";
	}
	
	public static void main(String args[]){
		Reader reader = new Reader(ApplicationMapping.class);
		ApplicationMapping mapping = (ApplicationMapping)reader.read(ReaderMain.getSamplesDir() + "sample.xml");
		BindResource resource  = mapping.getResourceTier().getResource("testResource");
		System.out.println(((JavaResource)resource).getJarfileLocation());
		
		URI uri = ((JavaResource)resource).getJarfileLocation().toURI();
		
		reader = new Reader(ApplicationMapping.class);
		mapping = (ApplicationMapping)reader.read(ReaderMain.getSamplesDir() + "sample2.xml");
		resource  = mapping.getResourceTier().getResource("testResource");
		System.out.println(((JavaResource)resource).getJarfileLocation());
		
		uri = ((JavaResource)resource).getJarfileLocation().toURI();
	}
}
