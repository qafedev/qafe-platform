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
package com.qualogy.qafe.presentation.io;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import com.qualogy.qafe.bind.domain.ApplicationMapping;
import com.qualogy.qafe.bind.io.Reader;
import com.qualogy.qafe.bind.io.Writer;

@Deprecated 
/**
 * The replacement class in in core: com.qualogy.qafe.util.
 */
public class ApplicationMappingUtil {

	public static boolean isValidXML(final String xml){
		ByteArrayInputStream bis = new ByteArrayInputStream(xml!=null ? xml.getBytes(): null);
		try {
			new Reader(ApplicationMapping.class).read(bis);
			return true;
		} catch (Exception e){
			return false;
		}
	}
	
	public static ApplicationMapping readFromXML(final String xml){
		ByteArrayInputStream bis = new ByteArrayInputStream(xml!=null ? xml.getBytes(): null);
		ApplicationMapping applicationMapping = null;
		applicationMapping =  (ApplicationMapping)new Reader(ApplicationMapping.class).read(bis);
		return applicationMapping;
	}
	
	
	public static String toXML(final ApplicationMapping applicationMapping){
		String xml="";
		if (applicationMapping!=null){
			ByteArrayOutputStream bous = new ByteArrayOutputStream();
			new Writer().write(applicationMapping,bous);
			xml = bous.toString();
		}
		return xml;
		
	}
	
	

}
