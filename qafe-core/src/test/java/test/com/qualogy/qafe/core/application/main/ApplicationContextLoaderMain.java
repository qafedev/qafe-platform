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
package test.com.qualogy.qafe.core.application.main;

import java.util.Iterator;

import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.StringUtils;

import com.qualogy.qafe.bind.core.application.ApplicationContext;
import com.qualogy.qafe.core.application.ApplicationCluster;
import com.qualogy.qafe.core.application.ApplicationContextLoader;


public class ApplicationContextLoaderMain{
	public final static String PACKAGE = "test.com.qualogy.genesis.core.application.";
	
	private static String getSamplesDir(){
		String pckName = ClassUtils.getPackageName(ApplicationContextLoaderMain.class);
		return StringUtils.replace(pckName, ".", "/") + "/";
	}
	
	public static void main(String args[]){
		ApplicationContextLoader.load(ApplicationContextLoaderMain.getSamplesDir() + "application-config.xml");
		for (Iterator<ApplicationContext> iterator = ApplicationCluster.getInstance().iterator(); iterator.hasNext();) {
			ApplicationContext context = iterator.next();
			//ApplicationMapping mapping = context.getApplicationMapping();
			System.out.println(context.toString());
		}
	}
	
}

