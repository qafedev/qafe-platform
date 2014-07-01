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
package com.qualogy.qafe.bind.io.document;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import com.qualogy.qafe.util.io.PropertiesUtils;

public class LocalXSDResolver {

	private static Properties properties;
	private static final String INCLUDE_SCHEMALOCATION = "<xs:include schemaLocation=";
	private static final String INCLUDE_SCHEMALOCATION_END = "\"";
	private static final String INCLUDE_SCHEMALOCATION_CLOSE = "/>";
	private static final String INCLUDE_SCHEMA_OPEN = "<xs:schema";
	private static final String INCLUDE_SCHEMA_END = ">";
	private static final String INCLUDE_SCHEMA_CLOSE = "</xs:schema>";
	
	public LocalXSDResolver() {
		try {
			properties = PropertiesUtils.loadAllProperties("qafe.schemas");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public InputStream resolveToLocalXSDs(InputStream is) {
		String resolvedXSD = streamToString(is);
		List<IncludeSchemaToReplace> includeSchemaToReplaces = findIncludeSchemaToReplace(resolvedXSD, 0);
		while (includeSchemaToReplaces.size() > 0) {
			resolvedXSD = mergeIncludes(resolvedXSD, includeSchemaToReplaces);
			includeSchemaToReplaces = findIncludeSchemaToReplace(resolvedXSD, 0);
		}
		return stringToStream(resolvedXSD);
	}
	
	private String mergeIncludes(String xsdWithIncludes, List<IncludeSchemaToReplace> includeSchemaToReplaces) {
		String xsdWithoutIncludes = xsdWithIncludes;
		for (IncludeSchemaToReplace includeSchemaToReplace: includeSchemaToReplaces) {
			xsdWithoutIncludes = StringUtils.replace(xsdWithoutIncludes, includeSchemaToReplace.includeToReplace, includeSchemaToReplace.includeXSD);
		}
		return xsdWithoutIncludes;
	}
	
	private List<IncludeSchemaToReplace> findIncludeSchemaToReplace(String xsdString, int startFrom) {
		List<IncludeSchemaToReplace> includeSchemaToReplaces = new ArrayList<IncludeSchemaToReplace>();
		int nextStart = startFrom;
		while (nextStart > -1) {
			nextStart = xsdString.indexOf(INCLUDE_SCHEMALOCATION, nextStart);
			if (nextStart > -1) {
				int sIndex = nextStart + INCLUDE_SCHEMALOCATION.length() + 1;
				int eIndex = xsdString.indexOf(INCLUDE_SCHEMALOCATION_END, sIndex);
				String locationKey = xsdString.substring(sIndex, eIndex);
				String locationValue = properties.getProperty(locationKey);
				String includeXSD = getXSDBodyToInclude(locationValue);
				int closeIndex = xsdString.indexOf(INCLUDE_SCHEMALOCATION_CLOSE, eIndex);
				String includeToReplace = xsdString.substring(nextStart, (closeIndex + INCLUDE_SCHEMALOCATION_CLOSE.length()));
				IncludeSchemaToReplace includeSchemaToReplace = new IncludeSchemaToReplace(includeToReplace, includeXSD);
				includeSchemaToReplaces.add(includeSchemaToReplace);
				nextStart += INCLUDE_SCHEMALOCATION.length();
			}
		}
		return includeSchemaToReplaces;
	}
	
	private String getXSDBodyToInclude(String resourceName) {
		InputStream is = DocumentLoader.getContextClassLoader().getResourceAsStream(resourceName);
		String result = streamToString(is);
		int sIndex = result.indexOf(INCLUDE_SCHEMA_OPEN);
		sIndex = result.indexOf(INCLUDE_SCHEMA_END, sIndex) + 1;
		int eIndex = result.indexOf(INCLUDE_SCHEMA_CLOSE);
		result = result.substring(sIndex, eIndex);
		return result;
	}
	
	private String streamToString(InputStream inputStream) {
		String result = null;
		try {
			result = IOUtils.toString(inputStream, "UTF-8");
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		return result;
	}
	
	private InputStream stringToStream(String string) {
		InputStream result = null;
		try {
			result = IOUtils.toInputStream(string, "UTF-8");
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		return result;
	}
	
	private class IncludeSchemaToReplace {
		String includeXSD;
		String includeToReplace;

		public IncludeSchemaToReplace(String includeToReplace, String includeXSD) {
			this.includeToReplace = includeToReplace;
			this.includeXSD = includeXSD;
		}
	}
	
	public static void main(String[] args) {
		try {
			InputStream is = new FileInputStream("/media/Data/develop/svn/qafe/platform/trunk/qafe-core/src/main/resources/application-context.xsd");
			System.out.print(new LocalXSDResolver().resolveToLocalXSDs(is));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
